package Processor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
public class ParseXML {
    private final Path WikiXMLPath = Paths.get("Resources", "IndustrialistWiki.xml");
    private final Path WikiXML = WikiXMLPath.toAbsolutePath().normalize();
    private final Path OutputPath = Paths.get("Resources", "Recipes.json");
    private final Path Output = OutputPath.toAbsolutePath().normalize();
    public String Wiki;
    public ParseXML() throws IOException {
        Wiki = new String(Files.readAllBytes(WikiXML), StandardCharsets.UTF_8);
    }
    public void GetRecipes() throws IOException {
        OutputStream OStream = Files.newOutputStream(OutputPath, StandardOpenOption.TRUNCATE_EXISTING);
        OStream.close();
        String RecipeRegex = "\\{\\{([pP]roduction[^\\}]*)\\}\\}\s?";
        Pattern RecipePattern = Pattern.compile(RecipeRegex);
        Matcher RecipeMatcher = RecipePattern.matcher(Wiki);
        Files.writeString(Output, "[\n", StandardOpenOption.APPEND);
        while (RecipeMatcher.find()) {
            Recipe FormattedRecipe = FormatRecipe(RecipeMatcher.group(1));
            AddToJSON(FormattedRecipe);
        }
        Files.writeString(Output, "\n]", StandardOpenOption.APPEND);
    }
    public Recipe FormatRecipe(String RecipeText) {
        Recipe NewRecipe = new Recipe();
        for (String Piece : RecipeText.split("\\|")) {
            String[] Pair = Piece.split("\\=");
            if (Pair.length == 1) {
                continue;
            }
            if (Pair[1].contains("ºC")) {
                Pair[1] = Pair[1].split("ºC ")[1];
            }
            if (Pair[1].equals("?")) {
                continue;
            }
            switch (Pair[0]) {
                case "Mamyflux" -> NewRecipe.MF = Pair[1];
                case "machine" -> NewRecipe.Machine = Pair[1];
                case "time" -> { 
                    try {
                        NewRecipe.Time = Float.parseFloat(Pair[1]);
                    } catch (NumberFormatException e) {
                        NewRecipe.Time = -1;
                    }
                }
            }
            try {
                if (Pair[0].contains("Am") || Pair[0].contains("am")) {
                    if (Pair[0].subSequence(1, 5).equals("nput")) {
                        NewRecipe.InputAmounts.add(Float.valueOf(Pair[1]));
                    }
                    if (Pair[0].subSequence(1, 6).equals("utput")) {
                        NewRecipe.OutputAmounts.add(Float.valueOf(Pair[1]));
                    }
                } else {
                    if (Pair[0].subSequence(1, 5).equals("nput")) {
                        NewRecipe.Inputs.add(Pair[1]);
                    }
                    if (Pair[0].subSequence(1, 6).equals("utput")) {
                        NewRecipe.Outputs.add(Pair[1]);
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {}
        }
        return NewRecipe;
    }
    public void AddToJSON(Recipe Recipe) throws IOException {
        for (Integer index = 0; index < Recipe.Outputs.size(); index++) {
            String OutputItem = Recipe.Outputs.get(index);
            Float OutputAmount;
            try {
                OutputAmount = Recipe.OutputAmounts.get(index);
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
            JSONObject RecipeInfo = new JSONObject();
            RecipeInfo.put("Inputs", Recipe.Inputs);
            RecipeInfo.put("Outputs", Recipe.Outputs);
            RecipeInfo.put("InputAmounts", Recipe.InputAmounts);
            RecipeInfo.put("OutputAmounts", Recipe.OutputAmounts);
            RecipeInfo.put("Time", Recipe.Time);
            RecipeInfo.put("MF", Recipe.MF);
            RecipeInfo.put("Machine", Recipe.Machine);
            JSONObject RecipeInner = new JSONObject();
            RecipeInner.put(String.valueOf(OutputAmount / Recipe.Time), RecipeInfo);
            JSONObject RecipeJSON = new JSONObject();
            RecipeJSON.put(OutputItem, RecipeInner);
            Files.writeString(Output, RecipeJSON.toString() + ",\n", StandardOpenOption.APPEND);
        }
    }
}