package com.bikeshare.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    public void sendPushNotification(String pushToken, String title, String body) {
        if (pushToken == null || !pushToken.startsWith("ExponentPushToken")) {
            return; // Invalid or missing token
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");

            Map<String, Object> message = new HashMap<>();
            message.put("to", pushToken);
            message.put("title", title);
            message.put("body", body);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(message, headers);
            // Fire and forget via RestTemplate
            restTemplate.postForEntity(EXPO_PUSH_URL, request, String.class);
            
        } catch (Exception e) {
            System.err.println("Failed to send push notification: " + e.getMessage());
        }
    }
}
