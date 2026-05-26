package com.resumematcher.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.resumematcher.config.APIConfig;
import com.resumematcher.model.JobDescription;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LinkedInJobsAPI {
    private OkHttpClient client;

    public LinkedInJobsAPI() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public List<JobDescription> searchJobs(String keyword, String location) {
        String apiKey = APIConfig.getRapidApiKey();
        
        // Use Mock Data if API Key is not configured
        if ("NOT_SET".equals(apiKey)) {
            System.out.println("Warning: RapidAPI key not set. Returning mock job data.");
            return getMockJobs(keyword, location);
        }

        List<JobDescription> jobs = new ArrayList<>();
        
        try {
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());
            String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8.toString());
            
            // Note: Endpoint depends on the specific RapidAPI provider chosen. 
            // Using a generic representation.
            String url = "https://" + APIConfig.getRapidApiHost() + "/search-jobs?keywords=" 
                    + encodedKeyword + "&location=" + encodedLocation;

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("x-rapidapi-key", apiKey)
                    .addHeader("x-rapidapi-host", APIConfig.getRapidApiHost())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    jobs = parseJsonResponse(jsonResponse);
                } else {
                    System.err.println("API Request failed with code: " + response.code());
                    // Fallback to mock on error to prevent total crash during demo
                    return getMockJobs(keyword, location);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return getMockJobs(keyword, location);
        }

        return jobs;
    }

    private List<JobDescription> parseJsonResponse(String json) {
        List<JobDescription> jobs = new ArrayList<>();
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            if (root.has("data") && root.get("data").isJsonArray()) {
                JsonArray data = root.getAsJsonArray("data");
                for (JsonElement element : data) {
                    JsonObject jobObj = element.getAsJsonObject();
                    
                    JobDescription jd = new JobDescription();
                    jd.setTitle(jobObj.has("title") ? jobObj.get("title").getAsString() : "Unknown Title");
                    jd.setCompany(jobObj.has("company") ? jobObj.get("company").getAsString() : "Unknown Company");
                    jd.setDescriptionText(jobObj.has("description") ? jobObj.get("description").getAsString() : "");
                    jd.setSource("LinkedIn via RapidAPI");
                    jd.setDateAdded(Instant.now().toString());
                    
                    jobs.add(jd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobs;
    }

    private List<JobDescription> getMockJobs(String keyword, String location) {
        List<JobDescription> mockJobs = new ArrayList<>();
        
        JobDescription jd1 = new JobDescription();
        jd1.setTitle("Senior " + keyword);
        jd1.setCompany("TechCorp " + location);
        jd1.setDescriptionText("We are looking for a Senior " + keyword + " with strong experience in Java, Spring Boot, AWS, and Microservices. 5+ years of experience required.");
        jd1.setSource("Mock Data");
        jd1.setDateAdded(Instant.now().toString());
        
        JobDescription jd2 = new JobDescription();
        jd2.setTitle(keyword + " Engineer");
        jd2.setCompany("Innovate LLC");
        jd2.setDescriptionText("Seeking a skilled engineer familiar with Python, Docker, Kubernetes, and CI/CD pipelines.");
        jd2.setSource("Mock Data");
        jd2.setDateAdded(Instant.now().toString());

        mockJobs.add(jd1);
        mockJobs.add(jd2);
        
        return mockJobs;
    }
}
