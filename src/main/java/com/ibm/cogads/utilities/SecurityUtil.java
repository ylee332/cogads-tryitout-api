package com.ibm.cogads.utilities;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;

@Component
public class SecurityUtil {

    private final static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);
    private static String initVector;
    private static String encryptionKey;
    private static String apikey;
    private static String resourceGroup;
    private static String version;

    public static String[] encrypt(String[] wcsCredentials ){
        try {
            IvParameterSpec iv = new IvParameterSpec(getInitVector().getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(getKey().getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encryptedResourceId = cipher.doFinal(wcsCredentials[0].getBytes());
            byte[] encryptedApiKey = cipher.doFinal(wcsCredentials[1].getBytes());

            String[] encryptedWcsCredentials = {Base64.encodeBase64String(encryptedResourceId), Base64.encodeBase64String(encryptedApiKey)};

            return encryptedWcsCredentials;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String[] decrypt(String[] encrypted) {
        try {

            IvParameterSpec iv = new IvParameterSpec(getInitVector().getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(getKey().getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] originalResourceId = cipher.doFinal(Base64.decodeBase64(encrypted[0]));
            byte[] originalApiKey = cipher.doFinal(Base64.decodeBase64(encrypted[1]));

            String[] originalCredentials = {new String(originalResourceId), new String(originalApiKey)};

            return originalCredentials;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static String getKey(){
        encryptionKey = encryptionKey == null ? System.getenv("ENCRYPTION_KEY") : encryptionKey;

        if (encryptionKey == null) {
            logger.info("No encryption key found in env, checking local config.json");
            String jsonString = null;
            try {
                jsonString = FileUtils.readFileToString(new File("config.json"), "utf-8");
                encryptionKey = new JSONObject(jsonString).getString("ENCRYPTION_KEY");
            } catch (IOException e) {
                logger.info("config.json not found.");
            }
        }

        return encryptionKey;
    }

    private static String getInitVector(){
        initVector = initVector == null ? System.getenv("INIT_VECTOR") : initVector;

        if (initVector == null) {
            logger.info("No initialized vector found in env, checking local config.json");
            String jsonString = null;
            try {
                jsonString = FileUtils.readFileToString(new File("config.json"), "utf-8");
                initVector = new JSONObject(jsonString).getString("INIT_VECTOR");
            } catch (IOException e) {
                logger.info("config.json not found.");
            }
        }

        return initVector;
    }

    /**

     * Get json values from local config file
     * @param key
     * @return value for the requested key
     */
    public static String getApiKey(String key) {

        apikey = apikey == null ? System.getenv(key) : apikey;

        String jsonString = null;

        if(apikey == null) {
            try {
                jsonString = FileUtils.readFileToString(new File("config.json"), "utf-8");
                apikey = new JSONObject(jsonString).getString(key);
            } catch (IOException e) {
                logger.info("config.json not found or malformed key requested.");
            }
        }
        return apikey;
    }

    public static String getResourceGroupId(String id) {

        resourceGroup = resourceGroup == null ? System.getenv(id) : resourceGroup;

        String jsonString = null;

        if(resourceGroup == null) {
            try {
                jsonString = FileUtils.readFileToString(new File("config.json"), "utf-8");
                resourceGroup = new JSONObject(jsonString).getString(id);
            } catch (IOException e) {
                logger.info("config.json not found or malformed key requested.");
            }
        }
        return resourceGroup;
    }

    public static String getVersion(String waVersion) {

        version = version == null ? System.getenv(waVersion) : version;

        String jsonString = null;

        if(version == null) {
            try {
                jsonString = FileUtils.readFileToString(new File("config.json"), "utf-8");
                version = new JSONObject(jsonString).getString(waVersion);
            } catch (IOException e) {
                logger.info("config.json not found or malformed key requested.");
            }
        }
        return version;
    }

}
