package com.ibm.cogads.utilities;


import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@Component
public class AppIdCommunicator {

    private Logger logger = Logger.getLogger(AppIdCommunicator.class);

    private static final String APP_ID_URL = "https://appid-oauth.ng.bluemix.net/oauth/v3/";

    private static final String INTROSPECT_PATH = "/introspect";

    private static final String TOKEN = "token";

    public String postAppIdIntrospect(String token) throws Exception{

        List<String> appIDcredentials = getAppIDCredentials();

        HttpClient client = HttpClients.createDefault();

        URI uri = UriComponentsBuilder
                .fromHttpUrl(APP_ID_URL)
                .path(appIDcredentials.get(2))
                .path(INTROSPECT_PATH)
                .build()
                .toUri();

        HttpPost post = new HttpPost(uri);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair(TOKEN, token));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        UsernamePasswordCredentials creds
                = new UsernamePasswordCredentials(appIDcredentials.get(0), appIDcredentials.get(1));
        post.addHeader(new BasicScheme().authenticate(creds, post, null));
        HttpResponse response = client.execute(post);
        return EntityUtils.toString(response.getEntity(), "UTF-8");

    }

    private static List<String> getAppIDCredentials() throws Exception {

        List<String> credentials = new ArrayList<>();

        String vcap_services = System.getenv("VCAP_SERVICES");
        String clientId = null, clientSecret = null, tenantId = null;
        JSONObject json = null;

        if (vcap_services != null) {
            JSONObject vcapJson = new JSONObject(vcap_services);
            for (String service : vcapJson.keySet()) {
                if (service.startsWith("AppID")) {
                    json = vcapJson.getJSONArray(service).getJSONObject(0).getJSONObject("credentials");
                }
            }
        } else {
            String jsonString = FileUtils.readFileToString(new File("config.json"), "utf-8");
            json = new JSONObject(jsonString).getJSONObject("appId");

        }

        clientId = json.getString("clientId");
        clientSecret = json.getString("secret");
        tenantId = json.getString("tenantId");

        credentials.add(clientId);
        credentials.add(clientSecret);
        credentials.add(tenantId);

        return credentials;
    }
}
