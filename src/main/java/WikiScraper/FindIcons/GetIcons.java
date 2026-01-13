package WikiScraper.FindIcons;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class GetIcons {
    public static void CollectIcons() throws IOException, InterruptedException {
        ObjectMapper Mapper = new ObjectMapper();
        String Continue = null;
        do {
            String IndustrialistAPI = "https://industrialist.miraheze.org/w/api.php"
                + "?action=query"
                + "&list=allimages"
                + "&aiprefix=" + URLEncoder.encode("Icon-", StandardCharsets.UTF_8)
                + "&ailimit=50"
                + (Continue == null ? "" : "&aicontinue=" + URLEncoder.encode(Continue, StandardCharsets.UTF_8))
                + "&format=json";
            String ResultJSON = Request.SendRequest(IndustrialistAPI);
            JsonNode Root = Mapper.readTree(ResultJSON);
            for (JsonNode Image : Root.path("query").path("allimages")) {
                String Name = Image.get("name").asText();
                String URL = Image.get("url").asText();
                Request.Download(URL, Name);
            }
            Continue = Root.path("continue").path("aicontinue").asText(null);
        } while (Continue != null);
    }
}
