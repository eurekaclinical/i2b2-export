package edu.emory.cci.aiw.i2b2patientdataexport.resource;

import com.sun.jersey.api.client.ClientResponse;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.DeleteRequest;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.I2b2AuthMetadata;
import edu.emory.cci.aiw.i2b2patientdataexport.comm.LoadRequest;
import junit.framework.Assert;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.List;

public class OutputConfigurationResourceTest extends AbstractResourceTest {

	@Test
	public void testLoadConfigurationOk() {
		LoadRequest request = new LoadRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("test-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);
		request.setOutputConfigurationId(1L);

		ClientResponse response = this.resource().path("/rest/config/load")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());
	}

	@Test
	public void testLoadConfigurationUserNotAuthorized() {
		LoadRequest request = new LoadRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("bad-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);
		request.setOutputConfigurationId(1L);

		ClientResponse response = this.resource().path("/rest/config/load")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.UNAUTHORIZED, response.getClientResponseStatus());
	}

	@Test
	public void testLoadConfigurationNotFound() {
		LoadRequest request = new LoadRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("test-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);
		request.setOutputConfigurationId(-1L);

		ClientResponse response = this.resource().path("/rest/config/load")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.NOT_FOUND, response.getClientResponseStatus());
	}

	@Test
	public void testLoadConfigurationWrongUsername() {
		LoadRequest request = new LoadRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("test-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);
		request.setOutputConfigurationId(0L);

		ClientResponse response = this.resource().path("/rest/config/load")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.UNAUTHORIZED, response.getClientResponseStatus());
	}

	@Test
	public void testGetAllOk() {
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("test-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");

		ClientResponse response = this.resource().path("/rest/config/getAll")
				.entity(authMetadata).accept(MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());
	}

	@Test
	public void testGetAllNotAuthorized() {
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("bad-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");

		ClientResponse response = this.resource().path("/rest/config/getAll")
				.entity(authMetadata).accept(MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.UNAUTHORIZED, response.getClientResponseStatus());
	}

	@Test
	public void testGetAllCount() {
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("test-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");

		ClientResponse response = this.resource().path("/rest/config/getAll")
				.entity(authMetadata).accept(MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(1, response.getEntity(List.class).size());
	}

	@Test
	public void testDeleteOk() {
		DeleteRequest request = new DeleteRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("test-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);
		request.setOutputConfigurationId(1L);

		ClientResponse response = this.resource().path("/rest/config/delete")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());
	}

	@Test
	public void testDeleteNotAuthorized() {
		DeleteRequest request = new DeleteRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("bad-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);
		request.setOutputConfigurationId(1L);

		ClientResponse response = this.resource().path("/rest/config/delete")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.UNAUTHORIZED, response.getClientResponseStatus());
	}

	@Test
	public void testDeleteNotFound() {
		DeleteRequest request = new DeleteRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("test-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);
		request.setOutputConfigurationId(-1L);

		ClientResponse response = this.resource().path("/rest/config/delete")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.NOT_FOUND, response.getClientResponseStatus());
	}

	@Test
	public void testDeleteWrongUsername() {
		DeleteRequest request = new DeleteRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("test-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);
		request.setOutputConfigurationId(0L);

		ClientResponse response = this.resource().path("/rest/config/delete")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.UNAUTHORIZED, response.getClientResponseStatus());
	}
}
