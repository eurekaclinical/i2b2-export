package edu.emory.cci.aiw.i2b2patientdataexport.resource;

import com.google.inject.Inject;
import edu.emory.cci.aiw.i2b2patientdataexport.xml.I2b2PatientDataExportServiceXmlException;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.DeleteRequest;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.LoadRequest;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.OutputConfigurationSummary;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.SaveRequest;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.OutputConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2UserAuthenticator;
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

@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OutputConfigurationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputConfigurationResource.class);

	private final OutputConfigurationDao dao;

	@Inject
	public OutputConfigurationResource(OutputConfigurationDao dao) {
		this.dao = dao;
	}

	/**
	 * Saves the output configuration as specified in the given request.
	 *
	 * @param request the create request, containing the configuration to create
	 *                   along with the i2b2 authentication tokens
	 * @return a status code indicating success or failure
	 * @throws I2b2PatientDataExportServiceException
	 *
	 */
	@POST
	@Path("/save")
	public Response saveConfiguration(SaveRequest request) throws
			I2b2PatientDataExportServiceException {
        LOGGER.info("Received request to save configuration for user: {}", request.getI2b2AuthMetadata().getUsername());
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(request.getI2b2AuthMetadata());
		try {
			if (ua.authenticateUser()) {
				OutputConfiguration config = this.dao
						.getByUsernameAndConfigName(request
								.getI2b2AuthMetadata().getUsername(),
								request.getOutputConfiguration().getName());
				if (config != null) {
					if (config.getUsername().equals(request
							.getI2b2AuthMetadata().getUsername())) {
                        LOGGER.info("Configuration with name: {} already exists for user: {}. Updating existing configuration.",
                                config.getName(), config.getUsername());
						this.dao.update(config, request.getOutputConfiguration());
					} else {
                        LOGGER.warn("Usernames do not match: request username: {}, existing configuration username: {}",
                                request.getI2b2AuthMetadata().getUsername(), config.getUsername());
						return Response.status(Response.Status.UNAUTHORIZED)
								.build();
					}
				} else {
                    LOGGER.info("Creating new configuration for user: {} with name: {}", request.getI2b2AuthMetadata().getUsername(),
                            request.getOutputConfiguration().getName());
					request.getOutputConfiguration().setUsername(request
							.getI2b2AuthMetadata().getUsername());
					request.getOutputConfiguration().setTemporary(Boolean.FALSE);
                    this.dao.create(request.getOutputConfiguration());
                }

				return Response.ok().build();
			} else {
                LOGGER.warn("User not authenticated: {}", request.getI2b2AuthMetadata().getUsername());
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (I2b2PatientDataExportServiceXmlException e) {
            LOGGER.error("Exception thrown: {}", e);
			throw new I2b2PatientDataExportServiceException(e);
		}
	}

	/**
	 * Loads the output configuration specified in the given request.
	 *
	 * @param request contains the information needed to complete the load
	 *                   operation, including the configuration ID and the
	 *                   i2b2 authentication tokens
	 *
	 * @return the output configuration or a status code indicating failure
	 * @throws I2b2PatientDataExportServiceException
	 */
	@POST
	@Path("/load")
	public Response loadConfiguration(LoadRequest request)
			throws
			I2b2PatientDataExportServiceException {
        LOGGER.info("Received request to load configuration for user: {} with id: {}",
                request.getAuthMetadata().getUsername(), request.getOutputConfigurationId());
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(request.getAuthMetadata());
		try {
			if (ua.authenticateUser()) {
				OutputConfiguration config = this.dao
						.getById(request.getOutputConfigurationId());
                if (config != null) {
                    if (config.getUsername().equals(request.getAuthMetadata().getUsername())) {
                        LOGGER.info("Found configuration with name: {}", config.getName());
                        return Response.ok().entity(config).build();
                    } else {
                        LOGGER.warn("Found configuration with name: {}, but usernames do not match: request user: {}, config user: {}",
                                new String[] { config.getName(), request.getAuthMetadata().getUsername(), config.getUsername() });
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
		} catch (I2b2PatientDataExportServiceXmlException e) {
            LOGGER.error("Exception thrown: {}", e);
			throw new I2b2PatientDataExportServiceException(e);
		}
	}

	/**
	 * Retrieves the names of all of the specified user's output
	 * configurations.
	 *
	 * @param authMetadata the i2b2 authentication tokens identifying the user
	 *
	 * @return a list of configuration names or a status indicating failure
	 *
	 * @throws I2b2PatientDataExportServiceException if an error occurs
	 */
	@POST
	@Path("/getAll")
	public Response getConfigurationsByUser(I2b2AuthMetadata
															authMetadata) throws I2b2PatientDataExportServiceException {
        LOGGER.info("Received request to retrieve all configurations for user: {}", authMetadata.getUsername());
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(authMetadata);
		try {
			if (ua.authenticateUser()) {
				List<OutputConfigurationSummary> result = new ArrayList<OutputConfigurationSummary>();
				List<OutputConfiguration> configs = this.dao.getAllByUsername
						(authMetadata.getUsername());
				for (OutputConfiguration config : configs) {
                    if (config.getUsername().equals(authMetadata.getUsername())) {
                        result.add(new OutputConfigurationSummary(config.getId(),
                                config.getName()));
                    } else {
                        LOGGER.warn("Skipping configuration with id: {} because configuration username does not match" +
                                " request username: request user: {}, config user: {}",
                                new String[] { config.getId().toString(), authMetadata.getUsername(), config.getUsername() });
                    }
				}

				return Response.ok().entity(result).build();
			} else {
                LOGGER.warn("User not authenticated: {}", authMetadata.getUsername());
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (I2b2PatientDataExportServiceXmlException e) {
            LOGGER.error("Exception thrown: {}", e);
			throw new I2b2PatientDataExportServiceException(e);
		}
	}

	/**
	 * Deletes the output configurations specified in the given request.
	 *
	 * @param request contains the information needed to complete the delete
	 *                   operation, including the configuration ID and the
	 *                   i2b2 authentication tokens
	 * @return a status code indicating success or failure
	 * @throws I2b2PatientDataExportServiceException
	 */
    @POST
	@Path("/delete")
	public Response deleteConfiguration(DeleteRequest request) throws I2b2PatientDataExportServiceException {
        LOGGER.info("Received request to delete configuration with id: {}", request.getOutputConfigurationId());
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(request
				.getAuthMetadata());
		try {
			if (ua.authenticateUser()) {
				OutputConfiguration config = this.dao.getById(request.getOutputConfigurationId());
                if (config != null) {
                    LOGGER.debug("Found configuration with id: {}", config.getId());
                    if (config.getUsername().equals(request.getAuthMetadata().getUsername())) {
                        this.dao.delete(config);
                        return Response.ok().build();
                    } else {
                        LOGGER.warn("Not deleting configuration with id: {} because request username does not match" +
                                " configuration username: request user: {}, configuration user: {}",
                                new String[] { request.getOutputConfigurationId().toString(),
                                        request.getAuthMetadata().getUsername(), config.getUsername() });
                        return Response.status(Response.Status.UNAUTHORIZED).build();
                    }
                }
                LOGGER.warn("Configuration with id: {} not found", request.getOutputConfigurationId());
                return Response.status(Response.Status.NOT_FOUND).build();
			} else {
                LOGGER.warn("User not authenticated: {}", request.getAuthMetadata().getUsername());
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (I2b2PatientDataExportServiceXmlException e) {
            LOGGER.error("Exception thrown: {}", e);
			throw new I2b2PatientDataExportServiceException(e);
		}
	}
}
