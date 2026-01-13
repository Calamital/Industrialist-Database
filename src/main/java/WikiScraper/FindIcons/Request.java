package WikiScraper.FindIcons;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class Request {
    public static String SendRequest(String URL) throws IOException, InterruptedException {
        HttpClient Client = HttpClient.newHttpClient();
        HttpRequest Request = HttpRequest.newBuilder().uri(URI.create(URL)).GET().build();
        HttpResponse<String> Response = Client.send(Request, HttpResponse.BodyHandlers.ofString());
        return Response.body().toString();
    }
    public static void Download(String URL, String FileName) throws IOException, InterruptedException {
        Path NewFile = Paths.get(System.getProperty("user.dir"), "Resources", "Icons", FileName);
        Files.createDirectories(NewFile.getParent());
        HttpClient Client = HttpClient.newHttpClient();
        HttpRequest Request = HttpRequest.newBuilder().uri(URI.create(URL)).GET().build();
        Client.send(Request, HttpResponse.BodyHandlers.ofFile(NewFile));
    }
}