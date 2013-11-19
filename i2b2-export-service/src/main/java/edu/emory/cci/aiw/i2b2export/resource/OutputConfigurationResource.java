package edu.emory.cci.aiw.i2b2export.resource;

/*
 * #%L
 * i2b2 Patient Data Export Service
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
import edu.emory.cci.aiw.i2b2export.comm.DeleteRequest;
import edu.emory.cci.aiw.i2b2export.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2export.comm.LoadRequest;
import edu.emory.cci.aiw.i2b2export.comm.OutputConfigurationSummary;
import edu.emory.cci.aiw.i2b2export.comm.SaveRequest;
import edu.emory.cci.aiw.i2b2export.dao.OutputConfigurationDao;
import edu.emory.cci.aiw.i2b2export.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2export.i2b2.I2b2UserAuthenticator;
import edu.emory.cci.aiw.i2b2export.xml.I2b2ExportServiceXmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * A Jersey resource for handling requests relating to output configurations.
 *
 * @author Michel Mansour
 */
@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OutputConfigurationResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(OutputConfigurationResource.class);

	private final I2b2UserAuthenticator userAuthenticator;
	private final OutputConfigurationDao dao;

	@Inject
	public OutputConfigurationResource(OutputConfigurationDao dao,
									I2b2UserAuthenticator userAuthenticator) {
		this.userAuthenticator = userAuthenticator;
		this.dao = dao;
	}

	/**
	 * Saves the output configuration as specified in the given request.
	 *
	 * @param request the create request, containing the configuration to create
	 *                along with the i2b2 authentication tokens
	 * @return a status code indicating success or failure
	 * @throws I2b2ExportServiceException if any errors occur while handling the request
	 *
	 */
	@POST
	@Path("/save")
	public Response saveConfiguration(SaveRequest request) throws I2b2ExportServiceException {
		LOGGER.info("Received request to save configuration for user: {}", request.getAuthMetadata().getUsername());

		try {
			if (this.userAuthenticator.authenticateUser(request.getAuthMetadata())) {
				OutputConfiguration config = this.dao
						.getByUsernameAndConfigName(request
								.getAuthMetadata().getUsername(),
								request.getOutputConfiguration().getName());
				if (config != null) {
					if (config.getUsername().equals(request
							.getAuthMetadata().getUsername())) {
						LOGGER.info("Configuration with name: {} already exists for user: {}. Updating existing configuration.",
								config.getName(), config.getUsername());
						this.dao.update(config, request.getOutputConfiguration());
					} else {
						LOGGER.warn("Usernames do not match: request username: {}, existing configuration username: {}",
								request.getAuthMetadata().getUsername(), config.getUsername());
						return Response.status(Response.Status.UNAUTHORIZED)
								.build();
					}
				} else {
					LOGGER.info("Creating new configuration for user: {} with name: {}", request.getAuthMetadata().getUsername(),
							request.getOutputConfiguration().getName());
					request.getOutputConfiguration().setUsername(request
							.getAuthMetadata().getUsername());
					this.dao.create(request.getOutputConfiguration());
				}

				return Response.ok().build();
			} else {
				LOGGER.warn("User not authenticated: {}", request.getAuthMetadata().getUsername());
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (I2b2ExportServiceXmlException e) {
			logError(e);
			throw new I2b2ExportServiceException(e);
		}
	}

	/**
	 * Loads the output configuration specified in the given request.
	 *
	 * @param request contains the information needed to complete the load
	 *                operation, including the configuration ID and the
	 *                i2b2 authentication tokens
	 * @return the output configuration or a status code indicating failure
	 * @throws I2b2ExportServiceException if any errors occur while handling the request
	 *
	 */
	@POST
	@Path("/load")
	public Response loadConfiguration(LoadRequest request) throws I2b2ExportServiceException {
		LOGGER.info("Received request to load configuration for user: {} with id: {}",
				request.getAuthMetadata().getUsername(), request.getOutputConfigurationId());

		try {
			if (this.userAuthenticator.authenticateUser(request.getAuthMetadata())) {
				OutputConfiguration config = this.dao
						.getById(request.getOutputConfigurationId());
				if (config != null) {
					if (config.getUsername().equals(request.getAuthMetadata().getUsername())) {
						LOGGER.info("Found configuration with name: {}", config.getName());
						return Response.ok().entity(config).build();
					} else {
						LOGGER.warn("Found configuration with name: {}, but usernames do not match: request user: {}, config user: {}",
								new String[]{config.getName(), request.getAuthMetadata().getUsername(), config.getUsername()});
						return Response.status(Response.Status.UNAUTHORIZED).build();
					}
				} else {
					LOGGER.warn("Configuration not found with id: {}", request.getOutputConfigurationId());
					return Response.status(Response.Status.NOT_FOUND).build();
				}
			} else {
				LOGGER.warn("User not authenticated: {}", request.getAuthMetadata().getUsername());
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (I2b2ExportServiceXmlException e) {
			logError(e);
			throw new I2b2ExportServiceException(e);
		}
	}

	/**
	 * Retrieves the names of all of the specified user's output
	 * configurations.
	 *
	 * @param authMetadata the i2b2 authentication tokens identifying the user
	 * @return a list of configuration names or a status indicating failure
	 * @throws I2b2ExportServiceException if any errors occur while handling the request
	 */
	@POST
	@Path("/getAll")
	public Response getConfigurationsByUser(I2b2AuthMetadata authMetadata) throws I2b2ExportServiceException {
		LOGGER.info("Received request to retrieve all configurations for user: {}", authMetadata.getUsername());

		try {
			if (this.userAuthenticator.authenticateUser(authMetadata)) {
				List<OutputConfigurationSummary> result = new ArrayList<>();
				List<OutputConfiguration> configs = this.dao.getAllByUsername(authMetadata.getUsername());
				for (OutputConfiguration config : configs) {
					if (config.getUsername().equals(authMetadata.getUsername())) {
						result.add(new OutputConfigurationSummary(config.getId(),
								config.getName()));
					} else {
						LOGGER.warn("Skipping configuration with id: {} because configuration username does not match" +
								" request username: request user: {}, config user: {}",
								new String[]{config.getId().toString(), authMetadata.getUsername(), config.getUsername()});
					}
				}

				return Response.ok().entity(result).build();
			} else {
				LOGGER.warn("User not authenticated: {}", authMetadata.getUsername());
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (I2b2ExportServiceXmlException e) {
			logError(e);
			throw new I2b2ExportServiceException(e);
		}
	}

	/**
	 * Deletes the output configurations specified in the given request.
	 *
	 * @param request contains the information needed to complete the delete
	 *                operation, including the configuration ID and the
	 *                i2b2 authentication tokens
	 * @return a status code indicating success or failure
	 * @throws I2b2ExportServiceException if any errors occur while handling the request
	 *
	 */
	@POST
	@Path("/delete")
	public Response deleteConfiguration(DeleteRequest request) throws I2b2ExportServiceException {
		LOGGER.info("Received request to delete configuration with id: {}", request.getOutputConfigurationId());

		try {
			if (this.userAuthenticator.authenticateUser(request.getAuthMetadata())) {
				OutputConfiguration config = this.dao.getById(request.getOutputConfigurationId());
				if (config != null) {
					LOGGER.debug("Found configuration with id: {}", config.getId());
					if (config.getUsername().equals(request.getAuthMetadata().getUsername())) {
						this.dao.delete(config);
						return Response.ok().build();
					} else {
						LOGGER.warn("Not deleting configuration with id: {} because request username does not match" +
								" configuration username: request user: {}, configuration user: {}",
								new String[]{request.getOutputConfigurationId().toString(),
										request.getAuthMetadata().getUsername(), config.getUsername()});
						return Response.status(Response.Status.UNAUTHORIZED).build();
					}
				}
				LOGGER.warn("Configuration with id: {} not found", request.getOutputConfigurationId());
				return Response.status(Response.Status.NOT_FOUND).build();
			} else {
				LOGGER.warn("User not authenticated: {}", request.getAuthMetadata().getUsername());
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (I2b2ExportServiceXmlException e) {
			logError(e);
			throw new I2b2ExportServiceException(e);
		}
	}

	private static void logError(Throwable e) {
		LOGGER.error("Exception thrown: {}", e);
	}
}
