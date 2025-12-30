import java.io.IOException;

import Processor.ParseXML;
import WikiScraper.FindIcons.GetIcons;
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Processor.ParseXML XMLParser = new ParseXML();
        XMLParser.GetRecipes();
        GetIcons.CollectIcons();
    }
}