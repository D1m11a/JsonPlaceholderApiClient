package JsonPlaceholderApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JsonPlaceholderApiClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static String createUser(String jsonBody) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(BASE_URL + "/users");
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(jsonBody));

        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        return responseBody;
    }

    public static String updateUser(int userId, String jsonBody) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPut request = new HttpPut(BASE_URL + "/users/" + userId);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(jsonBody));

        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        return responseBody;
    }

    public static int deleteUser(int userId) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpDelete request = new HttpDelete(BASE_URL + "/users/" + userId);

        HttpResponse response = httpClient.execute(request);
        return response.getStatusLine().getStatusCode();
    }

    public static String getAllUsers() throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/users");

        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        return responseBody;
    }

    public static String getUserById(int userId) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/users/" + userId);

        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        return responseBody;
    }

    public static String getUserByUsername(String username) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/users?username=" + username);

        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        return responseBody;
    }

    public static String getCommentsForLatestPost(int userId) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/users/" + userId + "/posts");
        HttpResponse response = httpClient.execute(request);

        int latestPostId = 0;
        String postResponse = EntityUtils.toString(response.getEntity());

        request = new HttpGet(BASE_URL + "/posts/" + latestPostId + "/comments");
        response = httpClient.execute(request);
        String commentsResponse = EntityUtils.toString(response.getEntity());

        String fileName = "user-" + userId + "-post-" + latestPostId + "-comments.json";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(commentsResponse);
        }

        return commentsResponse;
    }

    public static String getOpenTasksForUser(int userId) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + "/users/" + userId + "/todos");

        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        JSONArray tasksArray = new JSONArray(responseBody);
        JSONArray openTasksArray = new JSONArray();

        for (int i = 0; i < tasksArray.length(); i++) {
            JSONObject task = tasksArray.getJSONObject(i);
            boolean completed = task.getBoolean("completed");
            if (!completed) {
                openTasksArray.put(task);
            }
        }

        return openTasksArray.toString();
    }
}