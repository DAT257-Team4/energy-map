let scaleSelection;
let energyTypeSelection;
const countryMappings = {
  'Bosnia and Herz.': 'Bosnia and Herzegovina',
  'North Macedonia': 'Macedonia',
  'Kosovo': {v: 'XK', f: 'Kosovo'}
};
const inputData = JSON.parse(data);

let mapContainers = [];
let activeMapIndex = 0;


document.addEventListener("DOMContentLoaded", function() {
  scaleSelection = document.getElementById("colorScale");
  energyTypeSelection = document.getElementById("energyType");
  
  mapContainers.push(document.getElementById("regions_div_1"));
  mapContainers.push(document.getElementById("regions_div_2"));

  scaleSelection.addEventListener("change", onSettingsChanged);
  energyTypeSelection.addEventListener("change", onSettingsChanged);

  window.addEventListener('resize', drawRegionsMap, false);

  google.charts.load("current", {
    packages: ["geochart"]
  });
  google.charts.setOnLoadCallback(() => {
    drawRegionsMap();
  });
})

function onSettingsChanged() {
  console.info("Selected:", scaleSelection.value, energyTypeSelection.value);
  drawRegionsMap();
}

function drawRegionsMap() {
  if (!google.visualization) {
    console.error("Google Visualization API not loaded");
    return;
  }

  inputData.forEach(row => {
    const countryName = row[0];
    if (countryMappings[countryName]) {
      row[0] = countryMappings[countryName];
    }
  });

  // Convert the input data to a DataTable
  const data = google.visualization.arrayToDataTable(buildTable());

  // Options for the map
  const options = {
    region: "150", //europe
    backgroundColor: "#212969",
    colorAxis: { minValue: 0, scaleType: 'log' },
    datalessRegionColor: "#a6a6a6",
  };

  // Create the map and render it in the specified container
  activeMapIndex = (activeMapIndex + 1) % mapContainers.length;
  const map = new google.visualization.GeoChart(mapContainers[activeMapIndex]);

  map.draw(data, options);
}

// find the column index of the requested energy type
function findCol(energyType) {
  const energyTypes = inputData[0];
  const index = energyTypes.indexOf(energyType);
  if (index === -1) {
    console.info(`No data for energy type "${value}"`);
  }
  return index;
}

function buildTable() {
  const col = findCol(energyType.value);
  // if there is no data for the energy type return a table with no data
  if (col === -1) {
    return [[inputData[0][0], "no-data"]];
  }

  const table = [[inputData[0][0], `${inputData[0][col]} [MW]`]];
  for (let i = 1; i < inputData.length; i++)
  {
    const value = (scaleSelection.value === "Linear") ? inputData[i][col] : {v: Math.log10(inputData[i][col]), f: inputData[i][col]};
    table.push([inputData[i][0], value]);
  }

  return table;
}