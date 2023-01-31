package com.skripsi.area31;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import static com.skripsi.area31.util.FieldName.Constants.FIREBASE_API_URL;
import static com.skripsi.area31.util.FieldName.Constants.FIREBASE_SERVER_KEY;


public class FcmClient {
    public void sendPushNotification(String key, String studentName) throws JSONException {
        JSONObject msg = new JSONObject();

        msg.put("studentName", studentName);
        callToFcmServer(msg, key, FIREBASE_SERVER_KEY);
    }

    private void callToFcmServer(JSONObject message, String receiverFcmKey, String key)
        throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + key);
        httpHeaders.set("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        json.put("data", message);
        json.put("to", receiverFcmKey);

        HttpEntity<String> httpEntity = new HttpEntity<>(json.toString(), httpHeaders);
        restTemplate.postForObject(FIREBASE_API_URL, httpEntity, String.class);
    }
}
