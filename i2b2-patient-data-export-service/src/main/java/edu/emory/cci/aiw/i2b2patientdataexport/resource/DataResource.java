package edu.emory.cci.aiw.i2b2patientdataexport.resource;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.inject.Inject;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2PatientSet;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.OutputConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2PdoRetriever;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2UserAuthenticator;
import edu.emory.cci.aiw.i2b2patientdataexport.output.DataOutputFormatter;
import edu.emory.cci.aiw.i2b2patientdataexport.output.HeaderRowOutputFormatter;
import edu.emory.cci.aiw.i2b2patientdataexport.xml.I2b2PatientDataExportServiceXmlException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;

@Path("/export")
public final class DataResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataResource.class);

	private final I2b2UserAuthenticator userAuthenticator;
	private final I2b2PdoRetriever pdoRetriever;
	private final OutputConfigurationDao dao;

	@Inject
	public DataResource(OutputConfigurationDao dao, I2b2UserAuthenticator
			userAuthenticator, I2b2PdoRetriever pdoRetriever) {
		this.userAuthenticator = userAuthenticator;
		this.pdoRetriever = pdoRetriever;
		this.dao = dao;
	}

	private Response generateOutput(String i2b2Domain, String i2b2Username,
									String i2b2PasswordNode,
									String i2b2ProjectId,
									Integer i2b2PatientSetCollId,
									Integer i2b2PatientSetSize,
									OutputConfiguration outputConfig) throws I2b2PatientDataExportServiceXmlException {
		I2b2PatientSet patientSet = new I2b2PatientSet();
		patientSet.setPatientSetCollId(i2b2PatientSetCollId);
		patientSet.setPatientSetSize(i2b2PatientSetSize);

		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain(i2b2Domain);
		authMetadata.setUsername(i2b2Username);
		authMetadata.setPasswordNode(i2b2PasswordNode);
		authMetadata.setProjectId(i2b2ProjectId);

		if (this.userAuthenticator.authenticateUser(authMetadata)) {
			String output = new DataOutputFormatter(outputConfig,
					this.pdoRetriever.retrieve(authMetadata,
							extractConcepts(outputConfig), patientSet)).format();

			return Response.ok(output, "text/csv").header
					("Content-Disposition", "attachment;" +
							"filename=i2b2PatientData.csv")
					.build();
		} else {
			LOGGER.warn("User not authenticated: {}", i2b2Username);
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	/**
	 * Fetches the requested data from i2b2 and sends an output file back to the
	 * client formatted according to the configuration indicated by the full
	 * configuration in the request. The user must be authenticated
	 * according to the security tokens passed in.
	 * <p/>
	 * This method accepts form parameters instead of raw JSON because in
	 * order for the browser to trigger its download dialog, it must submit an
	 * actual HTML form.
	 *
	 * @param i2b2Domain           the i2b2 security domain
	 * @param i2b2Username         the i2b2 username
	 * @param i2b2PasswordNode     the i2b2 password node
	 * @param i2b2ProjectId        the i2b2 project ID
	 * @param outputConfigJson     the JSON string of the output configuration
	 * @param i2b2PatientSetCollId the i2b2 patient set ID
	 * @param i2b2PatientSetSize   the i2b2 patient set size
	 * @return the formatted output or a status code indicating failure
	 * @throws I2b2PatientDataExportServiceException
	 *          if something goes wrong
	 */
	@POST
	@Path("/configDetails")
	@Produces("text/csv")
	public Response generateOutputFromConfigDetails(@FormParam("i2b2-domain")
													String i2b2Domain,
													@FormParam("i2b2-user") String
															i2b2Username,
													@FormParam("i2b2-pass") String
															i2b2PasswordNode,
													@FormParam("i2b2-project")
													String i2b2ProjectId,
													@FormParam("config-json")
													String outputConfigJson,
													@FormParam
															("patient-set-coll-id") Integer i2b2PatientSetCollId,
													@FormParam("patient-set-size")
													Integer i2b2PatientSetSize) throws I2b2PatientDataExportServiceException {
		LOGGER.info("Received request to export data from config details for " +
				"user: {}", i2b2Username);
		try {
			ObjectMapper mapper = new ObjectMapper();
			OutputConfiguration outputConfig = mapper.readValue
					(outputConfigJson, OutputConfiguration.class);
			return generateOutput(i2b2Domain, i2b2Username, i2b2PasswordNode,
					i2b2ProjectId, i2b2PatientSetCollId, i2b2PatientSetSize,
					outputConfig);
		} catch (IOException e) {
			logError(e);
			throw new I2b2PatientDataExportServiceException(e);
		} catch (I2b2PatientDataExportServiceException e) {
			logError(e);
			throw e;
		}
	}

	/**
	 * Fetches the requested data from i2b2 and sends an output file back to the
	 * client formatted according to the configuration indicated by the
	 * configuration ID in the request. The user must be authenticated
	 * according to the security tokens passed in.
	 * <p/>
	 * This method accepts form parameters instead of raw JSON because in
	 * order for the browser to trigger its download dialog, it must submit an
	 * actual HTML form.
	 *
	 * @param i2b2Domain           the i2b2 security domain
	 * @param i2b2Username         the i2b2 username
	 * @param i2b2PasswordNode     the i2b2 password node
	 * @param i2b2ProjectId        the i2b2 project ID
	 * @param outputConfigId       the ID of the output configuration to use
	 * @param i2b2PatientSetCollId the i2b2 patient set ID
	 * @param i2b2PatientSetSize   the i2b2 patient set size
	 * @return the formatted output or a status code indicating failure
	 * @throws I2b2PatientDataExportServiceException
	 *          if something goes wrong
	 */
	@POST
	@Path("/configId")
	@Produces("text/csv")
	public Response generateOutputFromConfigId(@FormParam("i2b2-domain")
											   String i2b2Domain,
											   @FormParam("i2b2-user") String
													   i2b2Username,
											   @FormParam("i2b2-pass") String
													   i2b2PasswordNode,
											   @FormParam("i2b2-project")
											   String i2b2ProjectId,
											   @FormParam("config-id")
											   Long outputConfigId,
											   @FormParam
													   ("patient-set-coll-id") Integer i2b2PatientSetCollId,
											   @FormParam("patient-set-size")
											   Integer i2b2PatientSetSize)
			throws
			I2b2PatientDataExportServiceException {
		LOGGER.info("Received request to export data from configuration id: " +
				"{} from user: {}", i2b2Username, outputConfigId);

		OutputConfiguration outputConfig = this.dao.getById
				(outputConfigId);
		if (null == outputConfig) {
			LOGGER.warn("No configuration found with id: {}", outputConfigId);
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			try {
				return generateOutput(i2b2Domain, i2b2Username,
						i2b2PasswordNode, i2b2ProjectId,
						i2b2PatientSetCollId, i2b2PatientSetSize,
						outputConfig);
			} catch (I2b2PatientDataExportServiceException e) {
				logError(e);
				throw e;
			}
		}
	}

	private Collection<I2b2Concept> extractConcepts(OutputConfiguration config) throws I2b2PatientDataExportServiceXmlException {
		Collection<I2b2Concept> result = new
				HashSet<I2b2Concept>();

		for (OutputColumnConfiguration colConfig : config.getColumnConfigs()) {
			result.add(colConfig.getI2b2Concept());
		}

		return result;
	}

	@POST
	@Path("/preview")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response generatePreview(OutputConfiguration config) {
		if (null != config) {
			HeaderRowOutputFormatter headerFormatter = new
					HeaderRowOutputFormatter(config);
			StringWriter csvStr = new StringWriter();
			CSVWriter writer;
			if (null == config.getQuoteChar() || config.getQuoteChar().isEmpty()) {
				writer = new CSVWriter(csvStr, config.getSeparator().charAt(0), CSVWriter.NO_QUOTE_CHARACTER);
			} else {
				writer = new CSVWriter(csvStr, config.getSeparator().charAt(0), config.getQuoteChar().charAt(0));
			}
			writer.writeNext(headerFormatter.formatHeader());
			return Response.ok().entity(csvStr.toString()).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	private static void logError(Throwable e) {
		LOGGER.error("Exception thrown: {}, e");
	}
}
