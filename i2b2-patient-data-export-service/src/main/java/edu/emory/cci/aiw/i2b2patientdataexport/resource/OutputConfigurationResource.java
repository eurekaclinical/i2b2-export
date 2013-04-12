package edu.emory.cci.aiw.i2b2patientdataexport.resource;

import com.google.inject.Inject;
import edu.emory.cci.aiw.i2b2patientdataexport.DataDownloaderException;
import edu.emory.cci.aiw.i2b2patientdataexport.DataDownloaderXmlException;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.DeleteRequest;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.LoadRequest;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.OutputConfigurationSummary;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.SaveRequest;
import edu.emory.cci.aiw.i2b2patientdataexport.dao.OutputConfigurationDao;
import edu.emory.cci.aiw.i2b2patientdataexport.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2UserAuthenticator;

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
	 * @throws edu.emory.cci.aiw.i2b2patientdataexport.DataDownloaderException
	 *
	 */
	@POST
	@Path("/save")
	public Response saveConfiguration(SaveRequest request) throws
			DataDownloaderException {
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
						this.dao.update(config, request.getOutputConfiguration());
					} else {
						return Response.status(Response.Status.UNAUTHORIZED)
								.build();
					}
				} else {
					request.getOutputConfiguration().setUsername(request
							.getI2b2AuthMetadata().getUsername());
					request.getOutputConfiguration().setTemporary(Boolean.FALSE);
                    this.dao.create(request.getOutputConfiguration());
                }

				return Response.ok().build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (DataDownloaderXmlException e) {
			throw new DataDownloaderException(e);
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
	 * @throws DataDownloaderException
	 */
	@POST
	@Path("/load")
	public Response loadConfiguration(LoadRequest request)
			throws
			DataDownloaderException {
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(request.getAuthMetadata());
		try {
			if (ua.authenticateUser()) {
				OutputConfiguration config = this.dao
						.getById(request.getOutputConfigurationId());
                if (config != null) {
                    if (config.getUsername().equals(request.getAuthMetadata().getUsername())) {
                        return Response.ok().entity(config).build();
                    } else {
                        return Response.status(Response.Status.UNAUTHORIZED).build();
                    }
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (DataDownloaderXmlException e) {
			throw new DataDownloaderException(e);
		}
	}

	@POST
	@Path("/getAll")
	public Response getConfigurationsByUser(I2b2AuthMetadata
															authMetadata) throws DataDownloaderException {
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
                    }
				}

				return Response.ok().entity(result).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (DataDownloaderXmlException e) {
			throw new DataDownloaderException(e);
		}
	}

	/**
	 * Deletes the output configurations specified in the given request.
	 *
	 * @param request contains the information needed to complete the delete
	 *                   operation, including the configuration ID and the
	 *                   i2b2 authentication tokens
	 * @return a status code indicating success or failure
	 * @throws DataDownloaderException
	 */
    @POST
	@Path("/delete")
	public Response deleteConfiguration(DeleteRequest request) throws DataDownloaderException {
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(request
				.getAuthMetadata());
		try {
			if (ua.authenticateUser()) {
				OutputConfiguration config = this.dao.getById(request.getOutputConfigurationId());
                if (config != null) {
                    if (config.getUsername().equals(request.getAuthMetadata().getUsername())) {
                        this.dao.delete(config);
                        return Response.ok().build();
                    } else {
                        return Response.status(Response.Status.UNAUTHORIZED).build();
                    }
                }
                return Response.status(Response.Status.NOT_FOUND).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (DataDownloaderXmlException e) {
			throw new DataDownloaderException(e);
		}
	}
}
