let scaleSelection;
let energyTypeSelection;
const countryMappings = {
  'Bosnia and Herz.': 'Bosnia and Herzegovina',
  'North Macedonia': 'Macedonia',
  'Kosovo': {v: 'XK', f: 'Kosovo'}
};
const inputData = JSON.parse(data);

let themeToggle = document.getElementById("theme-toggle");
let darkMode = false;

let mapContainers = [];
let activeMapIndex = 0;

let pubBackgroundColor = "rgb(54, 57, 62)";

let countrySelection = undefined;


document.addEventListener("DOMContentLoaded", function() {
  scaleSelection = document.getElementById("colorScale");
  energyTypeSelection = document.getElementById("energyType");
  
  mapContainers.push(document.getElementById("regions_div_1"));
  mapContainers.push(document.getElementById("regions_div_2"));

  scaleSelection.addEventListener("change", onSettingsChanged);
  energyTypeSelection.addEventListener("change", onSettingsChanged);

  window.addEventListener('resize', drawRegionsMap, false);

  google.charts.load("current", {
    packages: ["geochart", "corechart"]
  });
  google.charts.setOnLoadCallback(() => {
    drawRegionsMap();
  });

  themeToggle = document.getElementById("theme-toggle");
  themeToggle.addEventListener("click", toggleTheme);

  if (window.matchMedia) {
    if(window.matchMedia('(prefers-color-scheme: dark)').matches) {
      toggleTheme();
    }
  }
});

function toggleTheme() {
  darkMode = !darkMode;

  themeToggle.classList.toggle('toggled');
  
  if (darkMode) {
      document.body.setAttribute("data-bs-theme", "dark");
  } else {
      document.body.setAttribute("data-bs-theme", "light");
  }

  const elements = document.querySelectorAll('.bg');
  elements.forEach((element) => {
      if (darkMode) {
          element.classList.remove('bg-light');
          element.classList.add('bg-dark');
      } else {
          element.classList.remove('bg-dark');
          element.classList.add('bg-light');
      }
  });
}

function onSettingsChanged() {
  console.info("Selected:", scaleSelection.value, energyTypeSelection.value);
  drawRegionsMap();
}


function fixID(n){
  if(n<0)
    return 0;
  return n;
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
  if (countrySelection) {
    var pieData = google.visualization.arrayToDataTable(createPieChartTable(countrySelection));
  }
  else {
    var pieData = google.visualization.arrayToDataTable(buildTable("Linear"));
  }

  // Options for the map
  var optionsMap = {
    dataMode: 'regions',
    region: "150", //europe
    backgroundColor: "#212969",
    colorAxis: {minValue: 0},
    datalessRegionColor: "#a6a6a6",
  };

  // Options for the piechart
  var optionsPie = {
    title: countrySelection? countrySelection : "Europe",
    titleTextStyle: { color: "white",
      //fontName:,
      fontSize: 30,
      bold: true},
    pieHole: 0.4,
    backgroundColor: pubBackgroundColor,
    legend: {textStyle: {color: 'white'}},
    pieSliceBorderColor: pubBackgroundColor,
  };

  var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
  chart.draw(pieData, optionsPie);

  // Create the map and render it in the specified 
  let prevMapIndex = activeMapIndex;
  activeMapIndex = (activeMapIndex + 1) % mapContainers.length;
  const map = new google.visualization.GeoChart(mapContainers[activeMapIndex]);

  function countryClickHandler(){
    var selection = map.getSelection()[0];
    // console.log(selection);
    if (selection) {
      // console.log(selection.row);
      if (countrySelection != data.getValue(selection.row, 0)) {
        countrySelection = data.getValue(selection.row, 0);
        console.info("The user selected", countrySelection);
      }
      else {
        countrySelection = undefined;
        console.info("The user selected Europe");
      }
      drawRegionsMap();
      // alert('The user selected ' + value);
    }
  }
  google.visualization.events.addListener(map, 'select', countryClickHandler);

  mapContainers[activeMapIndex].style.zIndex = -1;
  map.draw(data, optionsMap);
  mapContainers[prevMapIndex].style.zIndex = -2;
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

// The `createPieChartTable` function takes a country as input and creates a table of data for a pie chart.
function createPieChartTable(country) {
  if (country) {
    let row = findRow(country);
    let table = [];
    for (var i = 0; i < inputData[0].length; i++) {
      if (inputData[row][i] != -1) {
        table.push([inputData[0][i], inputData[row][i]]);
      }
    }
    console.log(table);
    return table;
  }
  else {
    let table = [];
    table.push([inputData[0][0], inputData[0][col] + " [MW]"]);
    for (var i = 1; i < inputData.length; i++) {
      if(inputData[i][col]!=-1){
        table.push([inputData[i][0], inputData[i][col]]);
      }
    }
  }
}

function findRow(country) {
  for (var i = 0; i < inputData.length; i++) {
    if (inputData[i][0] == country) {
      return i;
    }
  }
  // If there is no data return -1
  console.info("No data for country", "\"" + country + "\"");
  return -1;
}

// removes countries that have no data at all
function removeNoData() {
  let tempArr = [];
  tempArr[0] = inputData[0];
  let tempArrIndex = 1;
  for (var i = 1; i < inputData.length; i++) {
    if (sumArrayIndex(inputData[i], 1, inputData[i].length) != 0) {
      tempArr[tempArrIndex] = inputData[i];
      tempArrIndex++;
    }
  }
  console.info("Removed countries with no data");
  console.log(inputData);
  return tempArr;
}

// summs all elements in an array between startIndex and endIndex
function sumArrayIndex(arr, startIndex, endIndex) {
  let sum = 0;
  for (var i = startIndex; i < endIndex; i++) {
    sum += arr[i];
  }
  return sum;
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
    const value = (scaleSelection.value === "Linear") ? inputData[i][col] : {v: Math.log10(fixID(inputData[i][col])), f: inputData[i][col]};
    console.log(value);
    if (value >= 0)
    {
      table.push([inputData[i][0], value]);
    }
    
  }

  return table;
}

// Responsible for dividing the energy data into two categories: renewable and non-renewable.
function divideRenewableAndNot(data) {
  let renewable = ["Geothermal", "Hydro Pumped Storage", "Hydro Run-of-river and poundage", 
    "Hydro Water Reservoir", "Marine", "Other renewable", "Solar", "Wind Offshore", "Wind Onshore"];
  let nonRenewable = ["Biomass", "Fossil Brown coal/Lignite", "Fossil Coal-derived gas", 
    "Fossil Gas", "Fossil Hard coal", "Fossil Oil", "Fossil Oil shale", "Fossil Peat", "Nuclear", "Waste", "Other"];
  let sortedArray = [["Renewable", "Non Renewable"], [0,0]];

  // Loop through the data and add the values to the appropriate array
  for (var i = 1; i < data.length; i++) {
    let row = data[i];
    for (var col = 1; col < row.length; col ++) {
      if (row[col] != -1) {
        if (renewable.includes(data[0][col])) {
          sortedArray[1][0] += row[col];
        }
        if (nonRenewable.includes(data[0][col])) {
          sortedArray[1][1] += row[col];
        }
      }
    }
  }

  console.log(sortedArray);
  // Return the arrays
  return sortedArray;
}