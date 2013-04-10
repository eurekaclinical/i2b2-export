package edu.emory.cci.aiw.i2b2datadownloader.resource;

import com.google.inject.Inject;
import edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderException;
import edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderXmlException;
import edu.emory.cci.aiw.i2b2datadownloader.comm.DetailedRequest;
import edu.emory.cci.aiw.i2b2datadownloader.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2datadownloader.dao.OutputConfigurationDao;
import edu.emory.cci.aiw.i2b2datadownloader.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2PdoRetriever;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2UserAuthenticator;
import edu.emory.cci.aiw.i2b2datadownloader.output.DataOutputFormatter;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Path("/download")
public final class DataResource {

	private final OutputConfigurationDao dao;

	@Inject
	public DataResource(OutputConfigurationDao dao) {
		this.dao = dao;
	}

	/**
	 * Fetches the requested data from i2b2 and sends an output file back to the
	 * client formatted according to the configuration indicated by the configuration ID in the request.
	 *
	 * @param i2b2Domain
	 * @param i2b2Username
	 * @param i2b2PasswordNode
	 * @param i2b2ProjectId
	 * @param outputConfigId
	 * @param i2b2PatientSetCollId
	 * @return the formatted output or a status code indicating failure
	 * @throws DataDownloaderException if something goes wrong
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
													   ("patient-set-coll-id") Integer i2b2PatientSetCollId)
			throws
			DataDownloaderException {
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain(i2b2Domain);
		authMetadata.setUsername(i2b2Username);
		authMetadata.setPasswordNode(i2b2PasswordNode);
		authMetadata.setProjectId(i2b2ProjectId);
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(authMetadata);
		try {
			if (ua.authenticateUser()) {
				OutputConfiguration outputConfig = this.dao.getById
						(outputConfigId);
				String output = new DataOutputFormatter(outputConfig,
						new I2b2PdoRetriever(authMetadata, i2b2PatientSetCollId).
								retrieve(extractConcepts(outputConfig))).format();
				if (outputConfig.isTemporary()) {
					this.dao.delete(outputConfig);
				}
				return Response.ok(output, "text/csv").header
                        ("Content-Disposition", "attachment;" +
                                "filename=i2b2PatientData.csv")
						.build();
			} else {
				return Response.status(300).build();
			}
		} catch (DataDownloaderXmlException e) {
			throw new DataDownloaderException(e);
		}
	}

	/**
	 * Fetches the requested data from i2b2 and sends an output file back to the
	 * client formatted according to the configuration given in the XML.
	 *
	 * @param request contains the request details, including i2b2 authentication tokens, configuration specification and i2b2 patient set ID
	 * @return either the formatted output as a CSV file or a status code
	 *         indicating an error
	 * @throws edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderException
	 *
	 */
	@POST
	@Path("/configDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateOutputFromConfigDetails(DetailedRequest request) throws DataDownloaderException {
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(request.getI2b2AuthMetadata());
		try {
			if (ua.authenticateUser()) {
				request.getOutputConfiguration().setUsername(request
						.getI2b2AuthMetadata().getUsername());
				request.getOutputConfiguration().setTemporary(Boolean.TRUE);
				this.dao.create(request.getOutputConfiguration());
				return Response.ok().entity(request.getOutputConfiguration()
						.getId()).build();

			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (DataDownloaderXmlException e) {
			throw new DataDownloaderException(e);
		}
	}

	private Collection<I2b2Concept> extractConcepts(OutputConfiguration config) throws DataDownloaderXmlException {
		Collection<I2b2Concept> result = new
				HashSet<I2b2Concept>();

		for (OutputColumnConfiguration colConfig : config.getColumnConfigs()) {
			result.add(colConfig.getI2b2Concept());
		}

		return result;
	}

	public static void main(String[] args) throws IOException, DataDownloaderException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter ow = mapper.defaultPrettyPrintingWriter();

		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("i2b2demo");
		authMetadata.setUsername("i2b2");
		authMetadata.setPasswordNode("<password token_ms_timeout=\"1800000\" is_token=\"true\">SessionKey:gEuDmP3jZi7BSqazk7wr</password>");
		authMetadata.setProjectId("Demo2");

		OutputConfiguration config = new OutputConfiguration();
		config.setUsername("i2b2");
		config.setName("foo");
		config.setRowDimension(OutputConfiguration.RowDimension.PATIENT);
		config.setMissingValue("(NULL)");
		config.setSeparator(",");
		config.setWhitespaceReplacement("_");
		config.setColumnConfigs(new ArrayList<OutputColumnConfiguration>());

		OutputColumnConfiguration colConfig1 = new OutputColumnConfiguration();
		colConfig1.setColumnOrder(1);
		I2b2Concept concept1 = new I2b2Concept
				("\\\\i2b2\\Concepts\\MyConcept3", 2, "concept_dimension",
						"MyConcept3", "N");
		colConfig1.setI2b2Concept(concept1);
		colConfig1.setColumnName("Concept 3");
		colConfig1.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.EXISTENCE);

		OutputColumnConfiguration colConfig2 = new OutputColumnConfiguration();
		colConfig2.setColumnOrder(2);
		I2b2Concept concept2 = new I2b2Concept
				("\\\\i2b2\\Concepts\\MyConcept1", 2, "concept_dimension",
						"MyConcept1", "N");
		colConfig2.setI2b2Concept(concept2);
		colConfig2.setColumnName("Concept 1");
		colConfig2.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);
		colConfig2.setHowMany(3);
		colConfig2.setIncludeUnits(true);
		colConfig2.setIncludeTimeRange(false);

		OutputColumnConfiguration colConfig3 = new OutputColumnConfiguration();
		colConfig3.setColumnOrder(3);
		I2b2Concept concept3 = new I2b2Concept
				("\\\\i2b2\\Concepts\\MyConcept2", 2, "concept_dimension",
						"MyConcept2", "N");
		colConfig3.setI2b2Concept(concept3);
		colConfig3.setColumnName("Concept 2");
		colConfig3.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.AGGREGATION);
		colConfig3.setAggregation(OutputColumnConfiguration.AggregationType.MAX);
		colConfig3.setIncludeUnits(true);

		OutputColumnConfiguration colConfig4 = new OutputColumnConfiguration();
		colConfig4.setColumnOrder(4);
		I2b2Concept concept4 = new I2b2Concept
				("\\\\i2b2\\Concepts\\MyConcept4", 2, "concept_dimension",
						"MyConcept4", "N");
		colConfig4.setI2b2Concept(concept4);
		colConfig4.setColumnName("Systolic");
		colConfig4.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);
		colConfig4.setIncludeTimeRange(true);
		colConfig4.setIncludeUnits(false);
		colConfig4.setHowMany(2);

		OutputColumnConfiguration colConfig5 = new OutputColumnConfiguration();
		colConfig5.setColumnOrder(5);
		I2b2Concept concept5 = new I2b2Concept
				("\\\\i2b2\\Concepts\\MyConcept5", 2, "concept_dimension",
						"MyConcept5", "N");
		colConfig5.setI2b2Concept(concept5);
		colConfig5.setColumnName("Diastolic");
		colConfig5.setDisplayFormat(OutputColumnConfiguration.DisplayFormat.VALUE);
		colConfig5.setIncludeTimeRange(true);
		colConfig5.setIncludeUnits(false);
		colConfig5.setHowMany(2);

		config.getColumnConfigs().add(colConfig1);
		config.getColumnConfigs().add(colConfig2);
		config.getColumnConfigs().add(colConfig3);
		config.getColumnConfigs().add(colConfig4);
		config.getColumnConfigs().add(colConfig5);

		DetailedRequest request = new DetailedRequest();
		request.setI2b2AuthMetadata(authMetadata);
		request.setOutputConfiguration(config);
		request.setPatientSetCollId(82);

		String reqJson = ow.writeValueAsString(request);
		System.out.println(reqJson);

//		DataResource dr = new DataResource();
//		dr.generateOutput(request);
	}
}
