<#--
 #%L
 i2b2 Export Service
 %%
 Copyright (C) 2013 Emory University
 %%
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 #L%
-->
<ns6:request xmlns:ns4="http://www.i2b2.org/xsd/cell/crc/psm/1.1/"
	xmlns:ns7="http://www.i2b2.org/xsd/cell/crc/psm/querydefinition/1.1/"
	xmlns:ns3="http://www.i2b2.org/xsd/cell/crc/pdo/1.1/" xmlns:ns5="http://www.i2b2.org/xsd/hive/plugin/"
	xmlns:ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/" xmlns:ns6="http://www.i2b2.org/xsd/hive/msg/1.1/">
	<message_header>
		<proxy>
			<redirect_url>${redirectHost}/i2b2/services/QueryToolService/pdorequest</redirect_url>
		</proxy>
		<sending_application>
			<application_name>i2b2_QueryTool</application_name>
			<application_version>0.2</application_version>
		</sending_application>
		<sending_facility>
			<facility_name>PHS</facility_name>
		</sending_facility>
		<receiving_application>
			<application_name>i2b2_DataRepositoryCell</application_name>
			<application_version>0.2</application_version>
		</receiving_application>
		<receiving_facility>
			<facility_name>PHS</facility_name>
		</receiving_facility>
		<message_type>
			<message_code>Q04</message_code>
			<event_type>EQQ</event_type>
		</message_type>
		<security>
			<domain>${domain}</domain>
            <username>${username}</username>
            ${passwordNode}
		</security>
		<message_control_id>
			<message_num>${messageId}</message_num>
			<instance_num>0</instance_num>
		</message_control_id>
		<processing_id>
			<processing_id>P</processing_id>
			<processing_mode>I</processing_mode>
		</processing_id>
		<accept_acknowledgement_type>messageId</accept_acknowledgement_type>
		<project_id>${projectId}</project_id>
	</message_header>
	<request_header>
		<result_waittime_ms>180000</result_waittime_ms>
	</request_header>
	<message_body>
		<ns3:pdoheader>
			<estimated_time>180000</estimated_time>
			<request_type>getPDO_fromInputList</request_type>
		</ns3:pdoheader>
		<ns3:request xsi:type="ns3:GetPDOFromInputList_requestType"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			<input_list>
				<patient_list max="${patientListMax}" min="${patientListMin}">
					<patient_set_coll_id>${patientSetCollId}</patient_set_coll_id>
				</patient_list>
			</input_list>
			<filter_list>
				<#list items as item>
				<panel name="${item.i2b2Key}">
					<panel_number>0</panel_number>
					<panel_accuracy_scale>0</panel_accuracy_scale>
					<invert>0</invert>
					<item>
						<item_key>${item.i2b2Key}</item_key>
						<hlevel>${item.level}</hlevel>
						<dim_tablename>${item.tableName?xml}</dim_tablename>
						<dim_columnname>${item.columnName?xml}</dim_columnname>
						<dim_dimcode>${item.dimensionCode?xml}</dim_dimcode>
						<dim_columndatatype>${item.columnDataType?xml}</dim_columndatatype>
						<dim_operator>${item.operator?xml}</dim_operator>
						<facttablecolumn>${item.factTableColumn?xml}</facttablecolumn>
						<item_is_synonym>${item.isSynonym}</item_is_synonym>
					</item>
				</panel>
				</#list>
			</filter_list>
			<output_option>
				<patient_set select="using_input_list" onlykeys="false" />
				<event_set select="using_input_list" onlykeys="false" />
				<observation_set blob="false" onlykeys="false" withmodifiers="false"/>
				<observer_set_using_filter_list onlykeys="false" />
				<!--<pid_set select="using_input_list" onlykeys="false"/>-->
			</output_option>
		</ns3:request>
	</message_body>
</ns6:request>
