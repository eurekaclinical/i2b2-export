package edu.emory.cci.aiw.i2b2datadownloader.resource;

import edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderException;
import edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderXmlException;
import edu.emory.cci.aiw.i2b2datadownloader.comm.DetailedRequest;
import edu.emory.cci.aiw.i2b2datadownloader.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2datadownloader.comm.SummarizedRequest;
import edu.emory.cci.aiw.i2b2datadownloader.entity.I2b2Concept;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputColumnConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.entity.OutputConfiguration;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2ConceptRetriever;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2PdoRetriever;
import edu.emory.cci.aiw.i2b2datadownloader.i2b2.I2b2UserAuthenticator;
import edu.emory.cci.aiw.i2b2datadownloader.output.DataOutputFormatter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashSet;

@Path("/download")
public final class DataResource {

	@GET
	@Path("/echo")
	public Response echo(@QueryParam("msg") String msg) {
		return Response.status(200).entity(msg).build();
	}

	@POST
	@Path("/configId")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response generateOutputFromConfigId(I2b2AuthMetadata authMetadata,
											   SummarizedRequest request) throws DataDownloaderXmlException {
		I2b2UserAuthenticator ua = new I2b2UserAuthenticator(authMetadata);
		try {
			if (ua.authenticateUser()) {

			} else {
				return Response.status(300).build();
			}
		}
	}

    /**
     * Fetches the requested data from i2b2 and sends an output file back to the
     * client formatted according to the configuration given in the XML.
     * 
     * @param metadata
     *            metadata required to authenticate with i2b2
     * @param request
     *            contains the request details, including configuration specification and i2b2 patient set ID
     * @return either the formatted output as a CSV file or a status code
     *         indicating an error
     * @throws edu.emory.cci.aiw.i2b2datadownloader.DataDownloaderException
     */
    @POST
    @Path("/configDetails")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateOutput(I2b2AuthMetadata metadata, DetailedRequest request) throws DataDownloaderException {
        I2b2UserAuthenticator ua = new I2b2UserAuthenticator(metadata);
        try {
            if (ua.authenticateUser()) {
                String output = new DataOutputFormatter(request.getOutputConfiguration(), new I2b2PdoRetriever(metadata, request.getPatientSetCollId()).retrieve(extractConcepts(request.getOutputConfiguration(), metadata))).format();
                return Response.ok().entity(output).build();
            } else {
                return Response.status(300).build();
            }
        } catch (DataDownloaderXmlException e) {
            throw new DataDownloaderException(e);
        }
    }

    private Collection<I2b2Concept> extractConcepts(OutputConfiguration config, I2b2AuthMetadata authMetadata) throws DataDownloaderXmlException {
        Collection<I2b2Concept> result = new
				HashSet<I2b2Concept>();
        I2b2ConceptRetriever retriever = new I2b2ConceptRetriever(authMetadata);
        for (OutputColumnConfiguration colConfig : config.getColumnConfigs()) {
            result.add(retriever.retrieveConcept(colConfig.getI2b2Concept().getKey()));
        }
        return result;
    }
}
