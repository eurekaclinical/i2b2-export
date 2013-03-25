package edu.emory.cci.aiw.i2b2datadownloader.resource;

import edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderException;
import edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderXmlException;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2UserAuthenticator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/config")
@Produces(MediaType.TEXT_XML)
@Consumes(MediaType.TEXT_XML)
public class OutputConfigurationResource {
    /**
     * Saves the output configuration as specified in the given XML.
     * 
     * @param xml
     *            Contains all required parameters to complete the save
     *            operation, including i2b2 username and authentication token,
     *            the output configuration, and its name.
     * @return a status code indicating success or failure
     * @throws edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderException
     */
    @POST
    @Path("/save")
    public Response saveConfiguration(String xml) throws DataDownloaderException {
        I2b2UserAuthenticator ua = new I2b2UserAuthenticator(xml);
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
     * @param xml
     *            Contains all required parameters to complete the load
     *            operation, including i2b2 username and authentication token,
     *            and the name of the output configuration to load.
     * @return the output configuration or a status code indicating failure
     * @throws DataDownloaderException
     */
    @POST
    @Path("/load")
    public Response loadConfiguration(String xml) throws DataDownloaderException {
        I2b2UserAuthenticator ua = new I2b2UserAuthenticator(xml);
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
