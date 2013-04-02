<ns3:request xmlns:ns3="http://www.i2b2.org/xsd/hive/msg/1.1/"
	xmlns:ns4="http://www.i2b2.org/xsd/cell/ont/1.1/" xmlns:ns2="http://www.i2b2.org/xsd/hive/plugin/">
	<message_header>
		<i2b2_version_compatible>1.1</i2b2_version_compatible>
		<hl7_version_compatible>2.4</hl7_version_compatible>
		<sending_application>
			<application_name>i2b2 Ontology </application_name>
			<application_version>1.2</application_version>
		</sending_application>
		<sending_facility>
			<facility_name>i2b2 Hive</facility_name>
		</sending_facility>
		<receiving_application>
			<application_name>Ontology Cell</application_name>
			<application_version>1.0</application_version>
		</receiving_application>
		<receiving_facility>
			<facility_name>i2b2 Hive</facility_name>
		</receiving_facility>
		<datetime_of_message>${messageDatetime}</datetime_of_message>
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
		<accept_acknowledgement_type>AL</accept_acknowledgement_type>
		<application_acknowledgement_type>AL</application_acknowledgement_type>
		<country_code>US</country_code>
		<project_id>${projectId}</project_id>
	</message_header>
	<request_header>
		<result_waittime_ms>180000</result_waittime_ms>
	</request_header>
	<message_body>
		<ns4:get_term_info>
			<self>${conceptPath}</self>
		</ns4:get_term_info>
	</message_body>
</ns3:request>