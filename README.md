# Caiamitai's Industrialist Database
### *This repository is a full compendium of every single recipe for every single item in the Roblox game Industrialist by Mamytema.*
#### *All data in this repository is available for public use without needing permission nor payments.*

# Contents
#### ```main/Resources/IndustrialistWiki.xml/``` *→ Contains the exported xml files from [the wiki](https://industrialist.miraheze.org/wiki/Main_Page)*
#### ```main/Resources/Recipes.json/``` *→ Contains all the recipes in JSON format.*

# Recipes.JSON

### Recipes Contain the Following:
* *Production time*
* *Production MamyFlux*
* *Inputs + Amounts*
* *Outputs + Amounts*
* *Output Efficiency*
* *Production Machine*

### JSON Format:
```
<ITEMNAME>: 
    <ITEMAMOUNT / PRODUCTIONTIME>: {
        "Inputs": [
            <INPUTITEMS>
        ],
        "Outputs": [
            <OUTPUTITEMS>
        ],
        "InputAmounts": [
            <INPUTAMOUNTS>
        ],
        "OutputAmounts": [
            <OUTPUTAMOUNTS>
        ],
        "Time": <PRODUCTIONTIME>,
        "MF": <PRODUCTIONMAMYFLUX>,
        "Machine": <PRODUCTIONMACHINE>
    }
```
### Example:
```
"raw-gas": {
    "30.0": {
        "OutputAmounts": [
            30
        ],
        "InputAmounts": [],
        "Outputs": [
            "raw-gas"
        ],
        "MF": "45kMF",
        "Time": 1,
        "Inputs": [],
        "Machine": "Natural Gas Well"
    }
}
```