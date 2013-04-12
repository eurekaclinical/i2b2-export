package edu.emory.cci.aiw.i2b2patientdataexport.resource;

import com.google.inject.Inject;
import edu.emory.cci.aiw.i2b2patientdataexport.I2b2PatientDataExportServiceException;
import edu.emory.cci.aiw.i2b2patientdataexport.I2b2PatientDataExportServiceXmlException;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.DetailedRequest;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2PatientSet;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.OutputConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2PdoRetriever;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2UserAuthenticator;
import edu.emory.cci.aiw.i2b2patientdataexport.output.DataOutputFormatter;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashSet;

@Path("/export")
public final class DataResource {

	private final OutputConfigurationDao dao;

	@Inject
	public DataResource(OutputConfigurationDao dao) {
		this.dao = dao;
	}

	/**
	 * Fetches the requested data from i2b2 and sends an output file back to the
	 * client formatted according to the configuration indicated by the
	 * configuration ID in the request. The user must be authenticated
	 * according to the security tokens passed in.
	 *
	 * We accept form parameters instead of JSON because in order for the
	 * browser to trigger its download dialog, it must submit an actual HTML
	 * form. This service endpoint accepts a constant number of parameters,
	 * so it's easier for the client to create this form than one containing
	 * the entire configuration, which contains an arbitrary number of
	 * elements.
	 *
	 * @param i2b2Domain the i2b2 security domain
	 * @param i2b2Username the i2b2 username
	 * @param i2b2PasswordNode the i2b2 password node
	 * @param i2b2ProjectId the i2b2 project ID
	 * @param outputConfigId the ID of the output configuration to use
	 * @param i2b2PatientSetCollId the i2b2 patient set ID
	 * @param i2b2PatientSetSize the i2b2 patient set size
	 *
	 * @return the formatted output or a status code indicating failure
	 * @throws edu.emory.cci.aiw.i2b2patientdataexport.I2b2PatientDataExportServiceException if something goes wrong
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
		I2b2PatientSet patientSet = new I2b2PatientSet();
		patientSet.setPatientSetCollId(i2b2PatientSetCollId);
		patientSet.setPatientSetSize(i2b2PatientSetSize);

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
						new I2b2PdoRetriever(authMetadata, patientSet).
								retrieve(extractConcepts(outputConfig))).format();

				// if the configuration is flagged as temporary,
				// then delete. It was saved by us to force the download
				// dialog to pop up rather than explicitly by the user
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
		} catch (I2b2PatientDataExportServiceXmlException e) {
			throw new I2b2PatientDataExportServiceException(e);
		}
	}

	/**
	 * Fetches the requested data from i2b2 and sends an output file back to the
	 * client formatted according to the configuration given in the request.
	 *
	 * @param request contains the request details, including i2b2 authentication tokens, configuration specification and i2b2 patient set ID
	 * @return either the formatted output as a CSV file or a status code
	 *         indicating an error
	 * @throws edu.emory.cci.aiw.i2b2patientdataexport.I2b2PatientDataExportServiceException
	 *
	 */
	@POST
	@Path("/configDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateOutputFromConfigDetails(DetailedRequest request) throws I2b2PatientDataExportServiceException {
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(request.getI2b2AuthMetadata());
		try {
			if (ua.authenticateUser()) {
				request.getOutputConfiguration().setUsername(request
						.getI2b2AuthMetadata().getUsername());

				// because the client has to POST an actual form in order to
				// trigger the download dialog, we save the details of the
				// configuration and set the temporary flag so it will be
				// deleted once the request is fulfilled in
				// generateOuputFromConfigId. This is preferable to forcing
				// the client to create a huge form with all of the
				// configuration options since the service is already
				// set up to accept and automatically convert JSON.
				request.getOutputConfiguration().setTemporary(Boolean.TRUE);
				this.dao.create(request.getOutputConfiguration());

				// return the id so the client can immediately send a request
				// to /configId with the just-created, temporary configuration
				return Response.ok().entity(request.getOutputConfiguration()
						.getId()).build();

			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (I2b2PatientDataExportServiceXmlException e) {
			throw new I2b2PatientDataExportServiceException(e);
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
}
