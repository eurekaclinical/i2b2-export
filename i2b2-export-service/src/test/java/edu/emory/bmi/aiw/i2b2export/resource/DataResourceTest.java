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
import org.junit.Assert;
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
		ClientResponse response = this.resource().path
				("/rest/export/configId").queryParam("i2b2-domain",
				"test-domain").queryParam
				("i2b2-user", "test-user").queryParam("i2b2-pass",
				"test-pass").queryParam("i2b2-project",
				"test-project").queryParam("config-id",
				"1").queryParam("patient-set-coll-id",
				"1").queryParam("patient-set-size",
				"10").accept("text/csv").type(MediaType
				.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse
				.class);

		Assert.assertEquals(ClientResponse.Status.OK, response.getClientResponseStatus());
	}

	@Test
	public void testPreviewBadRequest() {
		ClientResponse response = this.resource().path
				("/rest/export/preview").entity(null).type(MediaType
				.APPLICATION_JSON_TYPE).post(ClientResponse.class);

		Assert.assertEquals(ClientResponse.Status.INTERNAL_SERVER_ERROR,
				response.getClientResponseStatus());
	}
}
