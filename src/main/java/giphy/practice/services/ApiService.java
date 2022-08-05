package giphy.practice.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ApiService {

    @Value("${API_KEY}")
    private String API_KEY;

    private static final String endpoint = "https://api.giphy.com/v1/gifs/search";

    public List<String> fetch(String q, String limit, String rating) {
        String payload;
        RequestEntity<Void> req;
        ResponseEntity<String> res;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("api_key", API_KEY);
        params.add("offset", "0");
        params.add("q", q);
        params.add("limit", limit);
        params.add("rating", rating);

        String url = UriComponentsBuilder.fromUriString(endpoint)
                .queryParams(params).toUriString();

        req = RequestEntity.get(url).build();

        // Fetch from API
        RestTemplate template = new RestTemplate();
        try {
            System.out.println(">> Calling API");
            res = template.exchange(req, String.class);
        } catch (Exception e) {
            System.err.print(e);
            return Collections.emptyList();
        }

        // Get response
        payload = res.getBody();

        // Convert to String using Reader Object
        Reader in = new StringReader(payload);
        JsonReader jr = Json.createReader(in);

        // Convert to JSON object
        JsonObject response = jr.readObject();
        JsonArray data = response.getJsonArray("data");
        List<String> listOfUrl = new LinkedList<>();

        for (int i = 0; i < data.size(); i++) {
            JsonObject dataEl = data.getJsonObject(i);
            JsonObject images = dataEl.getJsonObject("images");
            JsonObject fixedHeight = images.getJsonObject("fixed_height");
            String gifUrl = fixedHeight.get("url").toString();
            listOfUrl.add(gifUrl);
        }

        return listOfUrl;
    }
}
