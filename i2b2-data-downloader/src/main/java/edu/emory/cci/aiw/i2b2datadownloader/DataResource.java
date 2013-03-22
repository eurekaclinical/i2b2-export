package edu.emory.cci.aiw.i2b2datadownloader;

import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2UserAuthenticator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/data")
public final class DataResource {
    /**
     * Fetches the requested data from i2b2 and sends an output file back to the
     * client formatted according to the configuration given in the XML.
     * 
     * @param xml
     *            Contains all required parameters to complete the request,
     *            including i2b2 username and authentication token, i2b2 patient
     *            set to query, and the output format configuration.
     * @return either the formatted output as a CSV file or a status code
     *         indicating an error
     * @throws DataDownloaderException
     */
    @POST
    @Path("/download")
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateOutput(String xml) throws DataDownloaderException {
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
