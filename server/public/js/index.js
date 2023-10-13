let scaleSelection;
let energyTypeSelection;
let dataSourceSelector;
let isDataSourceProd=true;
const countryMappings = {
  'Bosnia and Herz.': 'Bosnia and Herzegovina',
  'North Macedonia': 'Macedonia',
  'Kosovo': {v: 'XK', f: 'Kosovo'}
};
let inputData = dataProd;


let themeToggle = document.getElementById("theme-toggle");
let darkMode = false;

let mapContainers = [];
let activeMapIndex = 0;

let bootstrapColors = {};

let countrySelection = "Europe";

const renewableSources = [ "Geothermal", "Hydro Pumped Storage", "Hydro Run-of-river and poundage", "Hydro Water Reservoir", "Marine", "Other renewable", "Solar", "Wind Offshore", "Wind Onshore"];
const fossileSources = ["Fossil Brown coal/Lignite", "Fossil Coal-derived gas", "Fossil Gas", "Fossil Hard coal", "Fossil Oil", "Fossil Oil shale", "Fossil Peat"];
const nonRenewableSources = ["Nuclear","Waste","Biomass","Other"]

document.addEventListener("DOMContentLoaded", function() {
  scaleSelection = document.getElementById("colorScale");
  energyTypeSelection = document.getElementById("energyType");
  dataSourceSelector = document.getElementById("dataType");
  
  mapContainers.push(document.getElementById("map-container-1"));
  mapContainers.push(document.getElementById("map-container-2"));

  scaleSelection.addEventListener("change", onSettingsChanged);
  energyTypeSelection.addEventListener("change", onSettingsChanged);
  dataSourceSelector.addEventListener("change", onDataSourceChange);

  window.addEventListener('resize', drawCharts, false);

  google.charts.load("current", {
    packages: ["geochart", "corechart"]
  });
  google.charts.setOnLoadCallback(() => {
    drawCharts();
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

function onDataSourceChange(){
  isDataSourceProd=!isDataSourceProd;
  console.log(isDataSourceProd);
  if(isDataSourceProd){
    inputData=dataProd;
  }else{
    inputData=dataInst;
  }
  onSettingsChanged();
}


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
  drawCharts();
}

function onSettingsChanged() {
  console.info("Selected:", scaleSelection.value, energyTypeSelection.value);
  drawCharts();
}

function drawCharts() {
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

  inputData = removeNoData();

  drawMap();
  drawPieChart();
  drawBarChart();
}

function drawMap() {
  const data = google.visualization.arrayToDataTable(buildTable());

  // Options for the map
  var mapOptions = {
    dataMode: 'regions',
    region: "150", //europe
    backgroundColor: "#212969",
    colorAxis: {minValue: 0},
    datalessRegionColor: "#a6a6a6",
  };

  // Create the map and render it in the specified 
  const prevMapIndex = activeMapIndex;
  activeMapIndex = (activeMapIndex + 1) % mapContainers.length;
  const map = new google.visualization.GeoChart(mapContainers[activeMapIndex]);


  google.visualization.events.addListener(map, 'select', () => {
    const selection = map.getSelection()[0];
    if (selection) {
      if (countrySelection != data.getValue(selection.row, 0)) {
        countrySelection = data.getValue(selection.row, 0);
        console.info("The user selected", countrySelection);
      }
      else {
        countrySelection = "Europe";
        console.info("The user selected Europe");
      }
      drawCharts();
    }
  });

  mapContainers[activeMapIndex].style.zIndex = -1;
  map.draw(data, mapOptions);
  mapContainers[prevMapIndex].style.zIndex = -2;
}

function drawPieChart() {
  const themeColor = darkMode ? bootstrapColors.textLight : bootstrapColors.textDark;

  const pieChartData = google.visualization.arrayToDataTable(
    countrySelection === "Europe"
      ? sumProductionPerEnergyType(inputData)
      : createPieChartTable(countrySelection)
  );

  // Options for the piechart
  const pieChartOptions = {
    title: `Energy types in ${countrySelection === "XK" ? "Kosovo" : countrySelection}`,
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

  const pieChart = new google.visualization.PieChart(document.getElementById('donutchart'));
  pieChart.draw(pieChartData, pieChartOptions);
}

function drawBarChart() {
  const themeColor = darkMode ? bootstrapColors.textLight : bootstrapColors.textDark;

  const renewableBarChartData = google.visualization.arrayToDataTable(
    countrySelection === "Europe"
      ? divideRenewableAndNot(inputData, countrySelection)
      : divideRenewableAndNot(createPieChartTable(countrySelection), countrySelection)
  );

  // Options for the barchart
  var barChartOptions = {
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

  const barChart = new google.visualization.BarChart(document.getElementById('renewableBarChart'));
  barChart.draw(renewableBarChartData, barChartOptions);
}


// find the column index of the requested energy type
function findCol(energyType) {
  const energyTypes = inputData[0];
  const colIndex = energyTypes.indexOf(energyType);
  if (colIndex === -1) {
    console.info(`No data for energy type "${value}"`);
  }
  return colIndex;
}

// The `createPieChartTable` function takes a country as input and creates a table of data for a pie chart.
function createPieChartTable(country) {
  let table = [];

  if (country) {
    const row = findRow(country);
    for (let i = 0; i < inputData[0].length; i++) {
      if (inputData[row][i] !== -1) {
        table.push([inputData[0][i], inputData[row][i]]);
      }
    }
  }
  else {
    table.push([inputData[0][0], `${inputData[0][col]} [MW]`]);
    for (let i = 1; i < inputData.length; i++) {
      if(inputData[i][col] !== -1){
        table.push([inputData[i][0], inputData[i][col]]);
      }
    }
  }

  return table;
}

function findRow(country) {
  const rowIndex = inputData.findIndex(row => 
    row[0] === country || row[0].v === country
  );

  if (rowIndex === -1) {
    console.info(`No data for country "${country}"`);
  }
  return rowIndex;
}

// removes countries that have no data at all
function removeNoData() {
  let tempArr = [];
  tempArr[0] = inputData[0];
  let tempArrIndex = 1;
  for (let i = 1; i < inputData.length; i++) {
    if (sumArrayIndex(inputData[i], 1, inputData[i].length) > 0) {
      tempArr[tempArrIndex] = inputData[i];
      tempArrIndex++;
    }
  }
  console.info("Removed countries with no data");
  return tempArr;
}


/**
 * Summs all elements in an array between startIndex and endIndex
 * @param {Array} arr The array to sum
 * @param {Number} startIndex (Inclusive) The index to start summing from 
 * @param {Number} endIndex (Exclusive) The index to stop summing at 
 *  */ 
function sumArrayIndex(arr, startIndex, endIndex) {
  return arr.slice(startIndex, endIndex).reduce((a, b) => a + b);
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
    const rawValue = inputData[i][colIndex];
    if(rawValue !== -1){
      const value = (scaleSelection.value === "Linear") ? rawValue : { v: Math.log10(rawValue + 1), f: rawValue };
      table.push([inputData[i][0], value]);
    } else {
      table.push([inputData[i][0], { v: 0, f: "No data" }]);
    }
  }

  return table;
}

// Responsible for dividing the energy data into two categories: renewable and non-renewable.
function divideRenewableAndNot(data, country) {
  let sortedArray = [
    ["Type", "Production [MW]", { role: "style" }],
    ["Renewable", 0, "green"], 
    ["NonRenewable", 0 ,"blue"],
    ["Fossile", 0, "red"]
  ];

  // Loop through the data and add the values to the appropriate array
  if (country === "Europe") {
    for (let i = 1; i < data.length; i++) {
      const row = data[i];
      for (let col = 1; col < row.length; col++) {
        if (row[col] !== -1) {
          if (renewableSources.includes(data[0][col])) {
            sortedArray[1][1] += row[col];
          }
          else if (fossileSources.includes(data[0][col])) {
            sortedArray[2][1] += row[col];
          } else if (nonRenewableSources.includes(data[0][col])) {
            sortedArray[3][1] += row[col];
        }
      }
    }
  }
}
  else {
    for (let i = 1; i < data.length; i++) {
      const row = data[i];
      if (row[1] !== -1) {
        if (renewableSources.includes(row[0])) {
          sortedArray[1][1] += row[1];
        }
        else if (nonRenewableSources.includes(row[0])) {
          sortedArray[2][1] += row[1];
        } else if (fossileSources.includes(row[0])) {
          sortedArray[3][1] += row[1];
        }
      }
    }
  }

  return sortedArray;
}

// Responsible for summing all production of each energy type
function sumProductionPerEnergyType(data) {
  let result = [];
  result.push(["Energy Type", "Production [MW]"]);
  for (let col = 1; col < data[0].length; col++) {
    let energyType = [data[0][col], 0];
    for (let row = 1; row < data.length; row++) {
      if (data[row][col] !== -1) {
        energyType[1] += data[row][col];
      }
    }
    if (energyType[1] > 0) {
      result.push(energyType);
    }
  }
  return result;
}