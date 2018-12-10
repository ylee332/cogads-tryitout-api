package com.ibm.cogads.utilities;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;

@Component
public class GenericUtility {

    @Autowired
    private CloudantCommunication cloudantCommunication;

    public static JSONObject retrieveCloudantDoc(String docName, Database db) throws IOException, NoDocumentException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(db.find(docName), writer, "utf-8");
        String dataString = writer.toString();
        return new JSONObject(dataString);
    }

    public Database callCloudantDB(String dbName) throws Exception{
        CloudantClient client = cloudantCommunication.getClient();
        return client.database(dbName, false);
    }
}
