package edu.emory.cci.aiw.i2b2datadownloader.resource;

import com.google.inject.Inject;
import edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderException;
import edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderXmlException;
import edu.emory.cci.aiw.i2b2datadownloader.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2datadownloader.dao.OutputConfigurationDao;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2UserAuthenticator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	 * Saves the output configuration as specified in the given XML.
	 *
	 * @param authMetadata the authentication data i2b2 needs
	 * @param config       the output configuration to save
	 * @return a status code indicating success or failure
	 * @throws edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderException
	 *
	 */
	@POST
	@Path("/save")
	public Response saveConfiguration(I2b2AuthMetadata authMetadata,
									  OutputConfiguration config) throws
			DataDownloaderException {
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(authMetadata);
		try {
			if (ua.authenticateUser()) {
				return Response.status(200).build();
			} else {
				return Response.status(300).build();
			}
		} catch (DataDownloaderXmlException e) {
			throw new DataDownloaderException(e);
		}
	}

	/**
	 * Loads the output configuration with the name given n the specified XML.
	 *
	 * @param authMetadata the authentication data i2b2 needs
	 * @param configId     the id of the output configuration to load
	 *                     *
	 * @return the output configuration or a status code indicating failure
	 * @throws DataDownloaderException
	 */
	@POST
	@Path("/load")
	public Response loadConfiguration(I2b2AuthMetadata authMetadata,
									  String configId)
			throws
			DataDownloaderException {
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(authMetadata);
		try {
			if (ua.authenticateUser()) {
				return Response.status(200).build();
			} else {
				return Response.status(300).build();
			}
		} catch (DataDownloaderXmlException e) {
			throw new DataDownloaderException(e);
		}
	}
}
