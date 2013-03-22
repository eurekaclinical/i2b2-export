package edu.emory.cci.aiw.i2b2datadownloader.i2b2;

import freemarker.template.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public final class I2b2PdoRetriever {

    private final Configuration config;

    private String security = "<domain>i2b2demo</domain>\n" +
			"<username>i2b2</username>\n" +
			"<password token_ms_timeout=\"1800000\" is_token=\"true\">SessionKey:nhYuVSgVtb0G1H944mFY</password>";
    private String projectId = "Demo2";

    public I2b2PdoRetriever() {
        this.config = new Configuration();
        this.config.setClassForTemplateLoading(this.getClass(), "/");
        this.config.setObjectWrapper(new DefaultObjectWrapper());
    }

    public void retrieve(Collection<I2b2Concept> concepts) {
        try {
            Template tmpl = this.config.getTemplate("i2b2_pdo_request.ftl");
            StringWriter writer = new StringWriter();

            String messageId = I2b2CommUtil.generateMessageId();

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("messageId", messageId);
            params.put("projectId", projectId);
            params.put("securityNode", security);
            params.put("patientSetLimit", "100");
            params.put("patientListMax", "1");
            params.put("patientListMin", "6");
            params.put("patientSetCollId", "82");
            params.put("items", concepts);

            tmpl.process(params, writer);
            System.out.println(writer.toString());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TemplateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] args) {
        I2b2PdoRetriever r = new I2b2PdoRetriever();
        I2b2Concept c = new I2b2Concept("key", 3, "tableName", "dimcode", "N");
        r.retrieve(Collections.singleton(c));
    }
}
