package edu.emory.bmi.aiw.i2b2export.resource;

/*
 * #%L
 * i2b2 Export Service
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

import com.sun.jersey.api.client.ClientResponse;
import edu.emory.bmi.aiw.i2b2export.comm.DeleteRequest;
import edu.emory.bmi.aiw.i2b2export.comm.I2b2AuthMetadata;
import edu.emory.bmi.aiw.i2b2export.comm.LoadRequest;
import edu.emory.bmi.aiw.i2b2export.comm.SaveRequest;
import edu.emory.bmi.aiw.i2b2export.entity.OutputConfiguration;
import org.junit.Assert;
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

	@Test
	public void testSaveCreateOk() {
		SaveRequest request = new SaveRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("test-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);

		OutputConfiguration config = new OutputConfiguration();
		config.setUsername("test-user");
		config.setName("config-name");
		request.setOutputConfiguration(config);

		ClientResponse response = this.resource().path("/rest/config/save")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());
	}

	@Test
	public void testSaveUpdateOk() {
		SaveRequest request = new SaveRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("test-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);

		OutputConfiguration config = new OutputConfiguration();
		config.setUsername("test-user");
		config.setName("test-config");
		request.setOutputConfiguration(config);

		ClientResponse response = this.resource().path("/rest/config/save")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());
	}

	@Test
	public void testSaveUpdateWrongUsername() {
		SaveRequest request = new SaveRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("test-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);

		OutputConfiguration config = new OutputConfiguration();
		config.setUsername("test-user");
		config.setName("bad-config");
		request.setOutputConfiguration(config);

		ClientResponse response = this.resource().path("/rest/config/save")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.UNAUTHORIZED, response.getClientResponseStatus());
	}

	@Test
	public void testSaveNotAuthorized() {
		SaveRequest request = new SaveRequest();
		I2b2AuthMetadata authMetadata = new I2b2AuthMetadata();
		authMetadata.setDomain("test-domain");
		authMetadata.setUsername("bad-user");
		authMetadata.setPasswordNode("<password>test-password</password>");
		authMetadata.setProjectId("test-project");
		request.setAuthMetadata(authMetadata);

		OutputConfiguration config = new OutputConfiguration();
		config.setUsername("test-user");
		config.setName("test-config");
		request.setOutputConfiguration(config);

		ClientResponse response = this.resource().path("/rest/config/save")
				.entity(request).accept(MediaType.APPLICATION_JSON_TYPE).type
						(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse
						.class);

		Assert.assertEquals(ClientResponse.Status.UNAUTHORIZED, response.getClientResponseStatus());

	}
}
