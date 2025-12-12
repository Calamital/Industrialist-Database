
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.regex.*;
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
        while (RecipeMatcher.find()) {
            Recipe FormattedRecipe = FormatRecipe(RecipeMatcher.group(1));
            return;
        }
    }
    public Recipe FormatRecipe(String RecipeText) {
        Recipe NewRecipe = new Recipe();
        for (String Piece : RecipeText.split("\\|")) {
            String[] Pair = Piece.split("\\=");
            switch (Pair[0]) {
                case "time" -> NewRecipe.Time = Float.parseFloat(Pair[1]);
                case "Mamyflux" -> NewRecipe.MF = Float.parseFloat(Pair[1].split("[kM]")[0]);
                case "machine" -> NewRecipe.Machine = Pair[1];
            }
            try {
                if (Pair[0].contains("Amount")) {
                    if (Pair[0].subSequence(1, 5).equals("nput")) {
                        NewRecipe.InputAmounts.add(Float.valueOf(Pair[1]));
                    }
                    if (Pair[0].subSequence(1, 6).equals("utput")) {
                        NewRecipe.OutputAmounts.add(Float.valueOf(Pair[1]));
                    }
                } else {
                    if (Pair[0].subSequence(1, 5).equals("nput")) {
                        Integer InputNumber = Integer.valueOf(Pair[0].split("nput")[1]);
                        NewRecipe.Inputs.put(InputNumber, Pair[1]);
                    }
                    if (Pair[0].subSequence(1, 6).equals("utput")) {
                        Integer OutputNumber = Integer.valueOf(Pair[0].split("utput")[1]);
                        NewRecipe.Outputs.put(OutputNumber, Pair[1]);
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {}
        }
        return NewRecipe;
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
            "Time" : TIME,
            "MF" : MF,
            "Machine" : MACHINE
        }, ...
    }, ...
 }
*/