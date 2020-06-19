package org.aksw.agdistis.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetWikiData {
    public static String search_entity = "dresden";
    public Map<String, WikiData> docs= null;
    public Map<String, WikiData> finalList= new HashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        GetWikiData getwikidata = new GetWikiData();
        for (Map.Entry<String, WikiData> data : getwikidata.sendData(search_entity).entrySet()) {
            WikiData wikidata = data.getValue();
            System.out.println(wikidata.getLabel() + wikidata.getUrl() + wikidata.getUnique_identifier() + wikidata.getDescription());
        }
    }
    public Map<String, WikiData> sendData(String search_entity) throws IOException {
        this.docs = TripleIndex.sendRequest(search_entity);
        for (Map.Entry<String, WikiData> data : this.docs.entrySet()) {
            WikiData wikiData = data.getValue();
            wikiData.setDescription(getDescription(wikiData.getUrl()));
            wikiData.setUnique_identifier(wikiData.getUrl().replace("http://www.wikidata.org/entity/",""));
            this.finalList.put(wikiData.unique_identifier, wikiData);
        }
        return finalList;
    }

    private static String getDescription(String url2) throws IOException, JSONException {
        String value = "";
        String unique_identifier= url2.replace("http://www.wikidata.org/entity/","");
        try {

            String url = url2+".json";
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setReadTimeout(5000);
            conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.addRequestProperty("User-Agent", "Mozilla");
            conn.addRequestProperty("Referer", "google.com");
            boolean redirect = false;
            // normally, 3xx is redirect
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }
            if (redirect) {

                String newUrl = conn.getHeaderField("Location");
                String cookies = conn.getHeaderField("Set-Cookie");
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                conn.setRequestProperty("Cookie", cookies);
                conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                conn.addRequestProperty("User-Agent", "Mozilla");
                conn.addRequestProperty("Referer", "google.com");
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer html = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }
            in.close();
            JSONObject obj_json = new JSONObject(html.toString());
            if (obj_json.getJSONObject("entities") != null) {
                JSONObject entities = obj_json.getJSONObject("entities");
                if (entities.getJSONObject(unique_identifier) != null) {
                    JSONObject unique_id = entities.getJSONObject(unique_identifier);
                    if (unique_id.getJSONObject("descriptions") != null) {
                        JSONObject description = unique_id.getJSONObject("descriptions");
                        if (description.has("en")) {
                            JSONObject en_obj = description.getJSONObject("en");
                            if (en_obj.getString("value") != null) {
                                value = en_obj.getString("value");
                                return value;
                            }
                        } else if (description.has("de")) {
                            JSONObject en_obj = description.getJSONObject("de");
                            if (en_obj.getString("value") != null) {
                                value = en_obj.getString("value");
                                return value;
                            }
                        } else {
                            value = "Description is not available in English/German";
                        }
                    }

                }
            }

        } catch (Exception e) {
            //e.printStackTrace();
            value = "exception occurred";
        }
        return value;
    }
}

