package edu.emory.bmi.aiw.i2b2export.resource;

/*
 * #%L
 * i2b2 Export Service
 * %%
 * Copyright (C) 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.inject.Inject;
import edu.emory.bmi.aiw.i2b2export.comm.I2b2AuthMetadata;
import edu.emory.bmi.aiw.i2b2export.comm.I2b2PatientSet;
import edu.emory.bmi.aiw.i2b2export.dao.OutputConfigurationDao;
import edu.emory.bmi.aiw.i2b2export.entity.I2b2Concept;
import edu.emory.bmi.aiw.i2b2export.entity.OutputColumnConfiguration;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfiguration;
import edu.emory.bmi.aiw.i2b2export.i2b2.I2b2PdoRetriever;
import edu.emory.bmi.aiw.i2b2export.i2b2.I2b2UserAuthenticator;
import edu.emory.bmi.aiw.i2b2export.output.DataOutputFormatter;
import edu.emory.bmi.aiw.i2b2export.output.HeaderRowOutputFormatter;
import edu.emory.bmi.aiw.i2b2export.xml.I2b2ExportServiceXmlException;
import java.io.BufferedWriter;
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashSet;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

/**
 * A Jersey resource for handling requests relating to retrieving data from i2b2.
 *
 * @author Michel Mansour
 * @since 1.0
 */
@Path("/export")
public final class DataResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataResource.class);

	private final I2b2UserAuthenticator userAuthenticator;
	private final I2b2PdoRetriever pdoRetriever;
	private final OutputConfigurationDao dao;

	/**
	 * Default constructor for the resource.
	 *
	 * @param dao the output configuration DAO
	 * @param userAuthenticator the user authenticator
	 * @param pdoRetriever the i2b2 PDO retriever
	 */
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
									OutputConfiguration outputConfig) throws I2b2ExportServiceXmlException {
		I2b2PatientSet patientSet = new I2b2PatientSet();
		patientSet.setPatientSetCollId(i2b2PatientSetCollId);
		patientSet.setPatientSetSize(i2b2PatientSetSize);

		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain(i2b2Domain);
		authMetadata.setUsername(i2b2Username);
		authMetadata.setPasswordNode(i2b2PasswordNode);
		authMetadata.setProjectId(i2b2ProjectId);

		if (this.userAuthenticator.authenticateUser(authMetadata)) {
			final DataOutputFormatter dataOutputFormatter = 
					new DataOutputFormatter(outputConfig,
					this.pdoRetriever.retrieve(authMetadata,
							extractConcepts(outputConfig), patientSet));
			
			StreamingOutput stream = new StreamingOutput() {
				
				@Override
				public void write(OutputStream out) throws IOException, WebApplicationException {
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
					dataOutputFormatter.format(writer);
					writer.flush();
				}
				
			};
			return Response.ok(stream, "text/csv")
					.header("Content-Disposition", "attachment;" +
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
	 * 
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
	 * @throws I2b2ExportServiceException
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
													Integer i2b2PatientSetSize) throws I2b2ExportServiceException {
		LOGGER.info("Received request to export data from config details for " +
				"user: {}", i2b2Username);
		try {
			ObjectMapper mapper = new ObjectMapper();
			OutputConfiguration outputConfig = mapper.readValue(outputConfigJson, OutputConfiguration.class);
			return generateOutput(i2b2Domain, i2b2Username, i2b2PasswordNode,
					i2b2ProjectId, i2b2PatientSetCollId, i2b2PatientSetSize,
					outputConfig);
		} catch (IOException e) {
			logError(e);
			throw new I2b2ExportServiceException(e);
		} catch (I2b2ExportServiceException e) {
			logError(e);
			throw e;
		}
	}

	/**
	 * Fetches the requested data from i2b2 and sends an output file back to the
	 * client formatted according to the configuration indicated by the
	 * configuration ID in the request. The user must be authenticated
	 * according to the security tokens passed in.
	 * 
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
	 * @throws I2b2ExportServiceException
	 *          if something goes wrong
	 */
	@POST
	@Path("/configId")
	@Produces("text/csv")
	public Response generateOutputFromConfigId(@FormParam("i2b2-domain") String i2b2Domain, @FormParam("i2b2-user") String i2b2Username,
											@FormParam("i2b2-pass") String i2b2PasswordNode, @FormParam("i2b2-project") String i2b2ProjectId,
											@FormParam("config-id") Long outputConfigId, @FormParam ("patient-set-coll-id") Integer i2b2PatientSetCollId,
											@FormParam("patient-set-size") Integer i2b2PatientSetSize) throws I2b2ExportServiceException {
		LOGGER.info("Received request to export data from configuration id: " +
				"{} from user: {}", i2b2Username, outputConfigId);

		OutputConfiguration outputConfig = this.dao.getById(outputConfigId);
		if (null == outputConfig) {
			LOGGER.warn("No configuration found with id: {}", outputConfigId);
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			try {
				return generateOutput(i2b2Domain, i2b2Username,
						i2b2PasswordNode, i2b2ProjectId,
						i2b2PatientSetCollId, i2b2PatientSetSize,
						outputConfig);
			} catch (I2b2ExportServiceException e) {
				logError(e);
				throw e;
			}
		}
	}

	/**
	 * Convenience method for pulling out the i2b2 concepts from an output configuration.
	 *
	 * @param config the output configuration to get the concepts from
	 * @return a collection of {@link I2b2Concept}s
	 */
	private Collection<I2b2Concept> extractConcepts(OutputConfiguration config) {
		Collection<I2b2Concept> result = new
				HashSet<>();

		for (OutputColumnConfiguration colConfig : config.getColumnConfigs()) {
			result.add(colConfig.getI2b2Concept());
		}

		return result;
	}

	/**
	 * Generates a preview of the CSV header row based on the provided output configuration. The preview is returned
	 * to the client as a JSON string.
	 *
	 * @param config the {@link OutputConfiguration} to use to format the header row
	 * @return the formatted header or a status code indicating failure
	 */
	@POST
	@Path("/preview")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response generatePreview(OutputConfiguration config) {
		if (null != config) {
			final HeaderRowOutputFormatter headerFormatter = new
					HeaderRowOutputFormatter(config);
			StreamingOutput stream = new StreamingOutput() {
				
				@Override
				public void write(OutputStream out) throws IOException, WebApplicationException {
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
					headerFormatter.format(writer);
					writer.flush();
				}
				
			};
			return Response.ok(stream).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	private static void logError(Throwable e) {
		LOGGER.error("Exception thrown: {}", e);
	}
}
