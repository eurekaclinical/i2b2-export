<%@ page import="edu.emory.cci.aiw.i2b2patientdataexport.i2b2.I2b2CommUtil" %>

<html>
<body>
<h2>I2b2PatientDataExportService</h2>
<b>i2b2 Proxy URL:</b> <%= I2b2CommUtil.getProxyUrl() %>
<br/>
<b>i2b2 Service Host URL:</b> <%= I2b2CommUtil.getI2b2ServiceHostUrl() %>
</body>
</html>
