
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

//Production|time=1|Mamyflux=21kMF|input1=raw-gas|Input1Amount=60|output1=water-free-gas|Output1Amount=30|output2=water|Output2Amount=1.5|output3=crude-oil|Output3Amount=0.3|machine=Condenser
//REDU.     |TIME  |MF            |I1            |I1A            |O1                    |O1A             |O2           |O2A              |O3               |O3A              |MACHINE

//Production|input1=paraxylene|input2=acetic-acid|time=4|Mamyflux=60kMF|output1=pta|machine=Plastic Refinery|Input1Amount=1|Input2Amount=4|Output1Amount=2
//REDU.     |I1               |I2                |TIME  |MF            |O1         |MACHINE                 |I1A           |I2A           |O1A

//Production|input1=coke-fuel|Input1Amount=4|input2=sand|Input2Amount=4|time=5|Mamyflux=15kMF|output1=silicon|Output1Amount=10|machine=Blast Furnace     
//REDU.     |I1              |I1A           |I2         |I2A           |TIME  |MF            |O1             |O1A             |MACHINE

/*=
 -- Recipes.json FORMAT
 {
    ITEM : {
        ITEMAMOUNT/TIME : {
            "Inputs" : {
                [I1, I2, ...]
            },
            "Outputs" : {
                [O1, O2, ...]
            },
            "InputAmounts" : {
                [I1A, I2A, ...]
            },
            "OutputAmounts" : {
                [O1A, O2A, ...]
            },
            "Time" : TIME,
            "MF" : MF,
            "Machine" : MACHINE
        }, ...
    }, ...
 }
*/