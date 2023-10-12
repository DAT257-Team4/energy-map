let scaleSelection;
let energyTypeSelection;
const countryMappings = {
  'Bosnia and Herz.': 'Bosnia and Herzegovina',
  'North Macedonia': 'Macedonia',
  'Kosovo': {v: 'XK', f: 'Kosovo'}
};
let inputData = JSON.parse(data);

let themeToggle = document.getElementById("theme-toggle");
let darkMode = false;

let mapContainers = [];
let activeMapIndex = 0;

let pubBackgroundColor = "rgb(54, 57, 62)";
let bootstrapColors = {};

let countrySelection = "Europe";


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

  const style = getComputedStyle(document.body);
  bootstrapColors = {
    textLight: style.getPropertyValue('--bs-light'),
    textDark: style.getPropertyValue('--bs-dark')
  };
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
  drawRegionsMap();
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

  console.log(inputData);
  inputData = removeNoData();
  // Convert the input data to a DataTable
  const data = google.visualization.arrayToDataTable(buildTable());
  if (countrySelection != "Europe") {
    var pieData = google.visualization.arrayToDataTable(createPieChartTable(countrySelection));
    var renewableBarChartData = google.visualization.arrayToDataTable(divideRenewableAndNot(createPieChartTable(countrySelection), countrySelection));
  }
  else {
    var pieData = google.visualization.arrayToDataTable(sumProductionPerEnergyType(inputData));
    var renewableBarChartData = google.visualization.arrayToDataTable(divideRenewableAndNot(inputData, countrySelection));
  }

  // Options for the map
  var optionsMap = {
    dataMode: 'regions',
    region: "150", //europe
    backgroundColor: "#212969",
    colorAxis: {minValue: 0},
    datalessRegionColor: "#a6a6a6",
  };

  const themeColor = darkMode ? bootstrapColors.textLight : bootstrapColors.textDark;
  // Options for the piechart
  var optionsPie = {
    title: "Energy types in " + (countrySelection === "XK" ? "Kosovo" : countrySelection),
    titleTextStyle: {
      color: themeColor,
      fontSize: 20,
      bold: true
    },
    pieHole: 0.4,
    backgroundColor: 'transparent',
    legend: {
      textStyle: {
        color: themeColor
      }
    },
    pieSliceTextStyle: {
      color: themeColor
    },
    pieSliceBorderColor: themeColor
  };
  var piechart = new google.visualization.PieChart(document.getElementById('donutchart'));
  piechart.draw(pieData, optionsPie);


  // Options for the barchart
  var optionsBar = {
    title: "How clean is " + (countrySelection === "XK" ? "Kosovo" : countrySelection) + "'s energy?",
    titleTextStyle: { 
      color: themeColor,
      fontSize: 20,
      bold: true
    },
    backgroundColor: 'transparent',
    hAxis: {
      title: "Production [MW]", 
      titleTextStyle:{color: themeColor}, 
      textStyle:{color: themeColor},
      minValue: 0
    },
    vAxis: {
      textStyle:{color: themeColor, fontSize: 13}, 
    },
    legend: {position: 'none'}
  };
  var barchart = new google.visualization.BarChart(document.getElementById('renewableBarChart'));
  barchart.draw(renewableBarChartData, optionsBar);
  
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
        countrySelection = "Europe";
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
  console.log(country);
  for (var i = 0; i < inputData.length; i++) {
    if (inputData[i][0].v) {
      if (inputData[i][0].v == country) {
        return i;
      }
    }
    else if (inputData[i][0] == country) {
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
    if (sumArrayIndex(inputData[i], 1, inputData[i].length) != (-inputData[i].length+1)) {
      tempArr[tempArrIndex] = inputData[i];
      tempArrIndex++;
    }
  }
  console.info("Removed countries with no data");
  console.log(inputData);
  return tempArr;
}


/**
 * Summs all elements in an array between startIndex and endIndex
 * @param {Array} arr The array to sum
 * @param {Number} startIndex (Inclusive) The index to start summing from 
 * @param {Number} endIndex (Exclusive) The index to stop summing at 
 *  */ 
function sumArrayIndex(arr, startIndex, endIndex) {
  let sum = 0;
  for (var i = startIndex; i < endIndex; i++) {
    sum += arr[i];
  }
  return sum;
}

function buildTable() {
  const colIndex = findCol(energyType.value);
  // if there is no data for the energy type return a table with no data
  if (colIndex === -1) {
    return [[inputData[0][0], "no-data"]];
  }

  let table = [[inputData[0][0], `${inputData[0][colIndex]} [MW]`]];
  for (let i = 1; i < inputData.length; i++)
  {
    let rawValue=inputData[i][colIndex];
    if(rawValue!=-1){

      const value = (scaleSelection.value === "Linear") ? rawValue : {v: Math.log10(rawValue+1), f: rawValue};
      //console.log(value);
      table.push([inputData[i][0], value]);
    }else{
      table.push([inputData[i][0], {v:0, f: "No data"}]);
    }
  }
  console.log(table);

  return table;
}

// Responsible for dividing the energy data into two categories: renewable and non-renewable.
function divideRenewableAndNot(data, country) {
  let renewable = ["Biomass", "Geothermal", "Hydro Pumped Storage", "Hydro Run-of-river and poundage", 
    "Hydro Water Reservoir", "Marine", "Other renewable", "Solar", "Wind Offshore", "Wind Onshore"];
  let nonRenewable = ["Fossil Brown coal/Lignite", "Fossil Coal-derived gas", 
    "Fossil Gas", "Fossil Hard coal", "Fossil Oil", "Fossil Oil shale", "Fossil Peat", "Nuclear", "Waste", "Other"];
  //let sortedArray = [["Renewable", "Non Renewable"], [0,0]];
  let sortedArray = [
    ["Type", "Production [MW]", { role: "style" }],
    ["Renewable", 0, "green"], 
    ["Non Renewable", 0, "red"]
  ]
  console.log(data);
  console.log(country);
  // Loop through the data and add the values to the appropriate array
  if (country === "Europe") {
    for (var i = 1; i < data.length; i++) {
      let row = data[i];
      for (var col = 1; col < row.length; col ++) {
        if (row[col] != -1) {
          if (renewable.includes(data[0][col])) {
            sortedArray[1][1] += row[col];
          }
          if (nonRenewable.includes(data[0][col])) {
            sortedArray[2][1] += row[col];
          }
        }
      }
    }
  }
  else {
    for (var i = 1; i < data.length; i++) {
      let row = data[i];
      if (row[1] != -1) {
        if (renewable.includes(row[0])) {
          sortedArray[1][1] += row[1];
        }
        if (nonRenewable.includes(row[0])) {
          sortedArray[2][1] += row[1];
        }
      }
    }
  }

  console.log(sortedArray);
  // Return the arrays
  return sortedArray;
}

// Responsible for summing all production of each energy type
function sumProductionPerEnergyType(data) {
  let result = [];
  result.push(["Energy Type", "Production [MW]"]);
  for (var col = 1; col < data[0].length; col++) {
    let energyType = [data[0][col], 0];
    for (var row = 1; row < data.length; row++) {
      if (data[row][col] != -1) {
        energyType[1] += data[row][col];
      }
    }
    if (energyType[1] != 0) {
      result.push(energyType);
    }
  }
  console.log(result);
  return result;
}