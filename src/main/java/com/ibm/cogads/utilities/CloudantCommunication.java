package com.ibm.cogads.utilities;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;

@Component
public class CloudantCommunication {

    public static CloudantClient getClient() throws Exception {
        String vcap_services = System.getenv("VCAP_SERVICES");
        String username = null, password = null, host = null;
        JSONObject json = null;

        if (vcap_services != null) {
            JSONObject vcapJson = new JSONObject(vcap_services);
            for (String service : vcapJson.keySet()) {
                if (service.startsWith("cloudantNoSQLDB")) {
                    json = vcapJson.getJSONArray(service).getJSONObject(0).getJSONObject("credentials");
                }
            }
        } else {
            String jsonString = FileUtils.readFileToString(new File("config.json"), "utf-8");
            json = new JSONObject(jsonString).getJSONObject("couchdb");

        }

        username = json.getString("username");
        password = json.getString("password");
        host = json.getString("url");

        if (username == null || password == null || host == null) {
            throw new Exception("Unable to load cloudant credentials.  Either create config.json, " +
                    "bind the service in bmix or add VCAP_SERVICES to env.");
        }
        return ClientBuilder.url(new URL(host))
                .username(username)
                .password(password)
                .build();
    }

}
