(async () => {
    const { default: FastAverageColor } = await import(
        "https://cdn.jsdelivr.net/npm/fast-average-color/dist/index.browser.min.js"
    );
    await main(FastAverageColor);
})();
async function main() {

    const recipesFile = await fetch("../Resources/Recipes.json");
    const recipes = await recipesFile.json();
    let index = 0;
    while (recipes[index] != null) {
        const recipe = recipes[index];
        loadItemSpace(recipe);
        index++;
    }
}
async function loadItemSpace(recipe) {
    const itemSpace = document.getElementById("itemSpace").cloneNode(true);
    document.getElementById("itemSpaces").appendChild(itemSpace);
    const name = Object.keys(recipe)[0];
    const efficiency = Object.keys(recipe[name])[0]
    itemSpace.id = name;
    itemSpace.querySelector("#name").innerHTML = name.toUpperCase() + 
        `(${Math.round(100 * parseFloat(efficiency)) / 100}/s)`;
    const details = recipe[name][efficiency];
    const machine = details.Machine;
    const time = details.Time;
    const MF = details.MF;
    const inputs = details.Inputs;
    itemSpace.querySelector("#machine").innerHTML = `Machine: ${machine}`;
    itemSpace.querySelector("#time").innerHTML = `Time: ${time}s`;
    itemSpace.querySelector("#mf").innerHTML = `Energy: ${MF}`;
    itemSpace.querySelector("#inputs").innerHTML = `Inputs: ${inputs}`;
    setItemSpace(itemSpace);
}
async function setItemSpace(itemSpace) {
    console.log("Trying...");
    const icon = `../Resources/Icons/Icon-${itemSpace.id}.png`;
    console.log(icon);
    const fastAverageColor = new window.FastAverageColor();
    const color = await fastAverageColor.getColorAsync(icon);
    itemSpace.querySelector("#icon").style.backgroundImage = `url(\"${icon}\")`;
    itemSpace.style.backgroundColor = color.rgba;
    let colorList = color.rgba.replace(/[^\d,]/g, '').split(',');
    colorList = colorList.map(color => 255 - color);
    const yiq = ((colorList[0] * 299) + (colorList[1] * 587) + (colorList[2] * 114)) / 1000;
    let invertedColor = yiq >= 128 ? "#F6F6F6" : "#323232";
    colorList = colorList.map(color => 255 - color);
    colorList = colorList.map(color => yiq >= 128 ? color - 20 : color + 20);
    let textBackground = `rgb(${colorList[0]}, ${colorList[1]}, ${colorList[2]})`
    for (const p of itemSpace.getElementsByTagName("p")) {
        p.style.color = invertedColor;
        p.style.textShadow = "0px 2px 8px " + invertedColor + "C0";
        p.style.backgroundColor = textBackground;
    }
    itemSpace.querySelector("#name").style.textShadow = "0px 2px 8px " + invertedColor + "C0";
}