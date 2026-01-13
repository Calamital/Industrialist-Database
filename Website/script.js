// note: if youre looking at this please dont make fun of me im so bad at javascript it works and thats all that matters

(async () => {
    const { default: FastAverageColor } = await import(
        "https://cdn.jsdelivr.net/npm/fast-average-color/dist/index.browser.min.js"
    );
    await main();
})();
async function main() {
    let result = await search("find");
    let factories = result[1];
}
document.getElementById("search").addEventListener("click", () => {
    document.querySelectorAll(".itemSpace").forEach((itemSpace) => {
        itemSpace.remove();
    })
    search("none");
});
async function search(type) {
    const recipesFile = await fetch("../Resources/Recipes.json");
    const recipes = await recipesFile.json();
    const factoryFilter = document.getElementById("factories");
    let index = 0;
    let itemSpaces = [];
    let factories = [];
    while (recipes[index] != null) {
        const recipe = recipes[index];
        let result = await loadItemSpace(recipe, itemSpaces, type, type == "find" ? "any" : factoryFilter.value);
        let details;
        if (type != "find") {
            if (result[1] == -1) {
                index++;
                continue;
            }
            details = result[0];
            let itemSpace = result[1];
            itemSpaces.push(itemSpace);
        } else {
            details = result;
        }
        let factory = details.Machine;
        if (type == "find") {
            if (factories.indexOf(factory) == -1) {
                factories.push(factory);
                let option = document.createElement("option");
                option.value = factory;
                option.innerHTML = factory;
                factoryFilter.appendChild(option);
            }
        }
        index++;
    }
    return [itemSpaces, factories];
}
async function loadItemSpace(recipe, itemSpaces, filter, machineFilter) {
    const name = Object.keys(recipe)[0];
    const efficiency = Object.keys(recipe[name])[0]
    const details = recipe[name][efficiency];
    const machine = details.Machine;
    const time = details.Time;
    const MF = details.MF;
    const inputs = details.Inputs;
    if (filter == "find") {
        return details;
    } else if (filter == "none") {
        if (machineFilter != "any") {
            if (machine != machineFilter) {
                return [details, -1];
            }
        }
    }
    document.getElementById("debug").innerHTML = name + "A";
    const itemSpace = document.getElementById("itemSpace").cloneNode(true);
    itemSpace.id = name;
    document.getElementById("debug").innerHTML = name + "B";
    document.body.appendChild(itemSpace);
    document.getElementById("debug").innerHTML = name + "C";
    itemSpace.className = "itemSpace";
    itemSpace.querySelector("#machine").innerHTML = `Machine: ${machine}`;
    itemSpace.querySelector("#time").innerHTML = `Time: ${time}s`;
    itemSpace.querySelector("#mf").innerHTML = `Energy: ${MF}`;
    setItemSpace(itemSpace, itemSpaces);
    return [details, itemSpace];
}
async function createItem(name, needed) {
    const item = document.getElementById("item").cloneNode(true);
    item.className = "item";
    item.id = name;
    item.querySelector("#name").innerHTML = name.toUpperCase().replace(/-/g, " ") + 
        `: ${Math.round(100 * parseFloat(needed)) / 100}/s`;
    const icon = `../Resources/Icons/Icon-${name}.png`;
    const fastAverageColor = new window.FastAverageColor();
    const color = await fastAverageColor.getColorAsync(icon);
    item.querySelector("#icon").style.backgroundImage = `url(\"${icon}\")`;
    item.style.backgroundColor = color.hex + "60";
    let colorList = color.rgba.replace(/[^\d,]/g, '').split(',');
    colorList = colorList.map(color => 255 - color);
    let textColor = color.isDark ? "#fff" : "#222";
    item.querySelector("#name").style.backgroundColor = color.hex + "60";
    item.querySelector("#name").style.color = textColor;
    item.querySelector("#name").style.textShadow = "0px 2px 8px " + color.hex;
}
async function setItemSpace(itemSpace) {
    document.getElementById("debug").innerHTML = itemSpace.id;
    const icon = `../Resources/Icons/Icon-${itemSpace.id}.png`;
    const fastAverageColor = new window.FastAverageColor();
    const color = await fastAverageColor.getColorAsync(icon);
    itemSpace.style.backgroundColor = color.hex + "60";
    let colorList = color.rgba.replace(/[^\d,]/g, '').split(',');
    colorList = colorList.map(color => 255 - color);
    let textColor = color.isDark ? "#fff" : "#222";
    for (const p of itemSpace.getElementsByTagName("p")) {
        p.style.color = textColor;
        p.style.textShadow = "0px 2px 4px " + color.rgb;
        p.style.backgroundColor = color.hex + "80";
    }
}