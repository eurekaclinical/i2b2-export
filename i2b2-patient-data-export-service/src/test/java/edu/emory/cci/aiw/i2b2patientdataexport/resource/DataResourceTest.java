package edu.emory.cci.aiw.i2b2patientdataexport.resource;

import com.sun.jersey.api.client.ClientResponse;
import junit.framework.Assert;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

public class DataResourceTest extends AbstractResourceTest {

	@Test
	public void testExportConfigIdUnauthorizedUser() {
		ClientResponse response = this.resource().path
				("/rest/export/configId").queryParam("i2b2-domain",
				"test-domain").queryParam
						("i2b2-user", "bad-user").queryParam("i2b2-pass",
						"test-pass").queryParam("i2b2-project",
						"test-project").queryParam("config-id",
						"1").queryParam("patient-set-coll-id",
						"1").queryParam("patient-set-size",
						"10").accept("text/csv").type(MediaType
				.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse
				.class);

		Assert.assertEquals(ClientResponse.Status.UNAUTHORIZED, response.getClientResponseStatus());
	}
}
