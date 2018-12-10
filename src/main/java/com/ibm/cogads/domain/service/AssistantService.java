package com.ibm.cogads.domain.service;

import com.ibm.cogads.utilities.AppIdCommunicator;
import com.ibm.cogads.utilities.GenericUtility;
import com.ibm.cogads.utilities.SecurityUtil;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;

import static com.ibm.cogads.utilities.Constants.ENV_NAME_WATSON_ASSISTANT_VERSION;
import static com.ibm.cogads.utilities.Constants.WATSON_ASSISTANT_CREDENTIALS;
import static com.ibm.cogads.utilities.SecurityUtil.getVersion;

@Service
public class AssistantService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private GenericUtility genericUtility;

    @Autowired
    private AppIdCommunicator appIdCommunicator;

    public MessageResponse converse(String projectID, String text) throws Exception{
        JSONObject config = genericUtility.retrieveCloudantDoc(WATSON_ASSISTANT_CREDENTIALS, genericUtility.callCloudantDB(projectID));
        return watsonAssistantMessageAPI(config, text, projectID);
    }

    private MessageResponse watsonAssistantMessageAPI(JSONObject config, String text, String projectID) throws Exception{
        String[] configList = getEncryptedCredentials(projectID, config);
        String[] keys = securityUtil.decrypt(configList);

        IamOptions iamOptions = new IamOptions.Builder().apiKey(keys[1]).build();
        Assistant service = new Assistant(getVersion(ENV_NAME_WATSON_ASSISTANT_VERSION), iamOptions);
        InputData input = new InputData.Builder(text).build();
        MessageOptions options = new MessageOptions.Builder(configList[2])
                .input(input)
                .build();

        return service.message(options).execute();
    }

    private String[] getEncryptedCredentials(String projectID, JSONObject config) throws Exception{
        String[] encryptedCredentials = {config.getString("resourceId"),  config.getString("apiKey"), config.getString("workspaceID")};
        return encryptedCredentials;
    }
    public Boolean checkToken(String token) throws Exception{
        String response = appIdCommunicator.postAppIdIntrospect(token.replace("Bearer ", ""));

        if (response.contains("true")){
            return true;
        }

        else{
            throw new CredentialException("AppId token is incorrect or expired");
        }
    }

}
