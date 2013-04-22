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

	@Test
	public void testExportConfigIdNotFound() {
		ClientResponse response = this.resource().path
				("/rest/export/configId").queryParam("i2b2-domain",
				"test-domain").queryParam
				("i2b2-user", "test-user").queryParam("i2b2-pass",
				"test-pass").queryParam("i2b2-project",
				"test-project").queryParam("config-id",
				"-1").queryParam("patient-set-coll-id",
				"1").queryParam("patient-set-size",
				"10").accept("text/csv").type(MediaType
				.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse
				.class);

		Assert.assertEquals(ClientResponse.Status.NOT_FOUND, response.getClientResponseStatus());
	}

	@Test
	public void testExportConfigIdOk() {
//		ClientResponse response = this.resource().path
//				("/rest/export/configId").queryParam("i2b2-domain",
//				"test-domain").queryParam
//						("i2b2-user", "test-user").queryParam("i2b2-pass",
//						"test-pass").queryParam("i2b2-project",
//						"test-project").queryParam("config-id",
//						"1").queryParam("patient-set-coll-id",
//						"1").queryParam("patient-set-size",
//						"10").accept("text/csv").type(MediaType
//				.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse
//				.class);
//
//		Assert.assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());
	}

	@Test
	public void testPreviewBadRequest() {
//		ClientResponse response = this.resource().path
//				("/rest/export/preview").entity(null).type(MediaType
//				.APPLICATION_JSON_TYPE).post(ClientResponse.class);
//
//		Assert.assertEquals(ClientResponse.Status.INTERNAL_SERVER_ERROR,
//				response.getClientResponseStatus());
	}
}
