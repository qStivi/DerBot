package de.qStivi.apis;

import de.qStivi.Config;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class YouTube {

    private static final String SECRET = Config.get("YOUTUBE_KEY");

    /**
     * @param searchQuery A search query in form of a {@link String} like one you would do on YouTube.
     * @return {@link String} – The video ID of the first video displayed on YouTube using the given search query.
     * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
     */
    public static String getVideoIdBySearchQuery(String searchQuery) throws IOException {
        String query = "https://youtube.googleapis.com/youtube/v3/search?part=id&maxResults=1&q=" + searchQuery + "&safeSearch=none&type=video&key=" + SECRET;
        JSONObject jsonObject = readJsonFromUrl(query);
        return jsonObject.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId");
    }

    /**
     * @param link The link to a YouTube playlist in form of a {@link String}.
     * @return Returns a {@link List<>} of {@link String} containing all IDs of the given playlist.
     * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
     */
    public static List<String> getPlaylistItemsByLink(String link) throws IOException {
        String[] strings = link.split("list=");
        String id = strings[1];
        String query = "https://youtube.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults=50&playlistId=" + id + "&key=" + SECRET;
        JSONObject jsonObject = readJsonFromUrl(query);
        JSONArray items = jsonObject.getJSONArray("items");
        List<String> videoIds = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            videoIds.add(items.getJSONObject(i).getJSONObject("contentDetails").getString("videoId"));
        }
        return videoIds;
    }

    /**
     * @param url The URL to a JSON file in form of a {@link String}.
     * @return {@link JSONObject} – The contents of the JSON file.
     * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
     */
    public static JSONObject readJsonFromUrl(String url) throws IOException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();
            return new JSONObject(jsonText);
        }
    }
}