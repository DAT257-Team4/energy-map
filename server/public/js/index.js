google.charts.load("current", {
  packages: ["geochart"]
});
google.charts.setOnLoadCallback(() => {
  drawRegionsMap();
});

const test_data = [
    ["Country", "Solar", "Nuclear", "Hydro Water Reservoir", "WindOffshore", "FossilGas"],
    ["Germany", 200, 500, 800, 600, 10000],
    ["Sweden", 300, 600, 700, 800, 1000],
    ["France", 400, 500, 300, 200, 100],
    ["Italy", 200, 400, 500, 600, 10],
    ["Spain", 300, 200, 400, 500, 0],
    ["Greece", 400, 500, 600, 700, 0],
  ];

let input_data = JSON.parse(data);
//let values= <%- data %>
// let values = JSON.parse('<%- data %>');

function drawRegionsMap() {
  // where data will be stored within the map

  console.log(input_data);

  // Check that the google.visualization object is defined
  if (!google.visualization) {
    console.error("Google Visualization API not loaded");
    return;
  }

  var countryDict = {
    'Bosnia and Herz.': 'Bosnia and Herzegovina',
    'North Macedonia': 'Macedonia',
    'Kosovo': {v: 'XK', f: 'Kosovo'}
  };
  for (var i = 0; i < input_data.length; i++)
  {
    var countryName = input_data[i][0];
    if (countryName in countryDict)
    {
      input_data[i][0] = countryDict[countryName];
    }
  }

  // Convert the input data to a DataTable
  var data = google.visualization.arrayToDataTable(buildTable());

  // Options for the map
  var options = {
    region: "150", //europe
    backgroundColor: "#212969",
    colorAxis: {minValue: 0, scaleType: 'log'},
    datalessRegionColor: "#a6a6a6",
  };

  // Create the map and render it in the specified container
  var container = document.getElementById("regions_div");
  var map = new google.visualization.GeoChart(container);
  map.draw(data, options);
}

// find the column index of the requested energy type
function findCol(value) {
  for (var i = 0; i < input_data[0].length; i++) {
    if (input_data[0][i] == value) {
      return i;
    }
  }
  // if there is no data return -1
  console.info("No data for energy type", "\"" + energyType.value + "\"");
  return -1;
}

function buildTable() {
  var table = [];
  var col = findCol(energyType.value);
  // if there is no data for the energy type return a table with no data
  if (col === -1) {
    table.push([input_data[0][0], "no-data"]);
  }
  // else if there is data and the scale is set to linear build a new table 
  // with only the column for the country names and the requested energy type
  else if (scale.value == "Linear") {
    table.push([input_data[0][0], input_data[0][col] + " [MW]"]);
    for (var i = 1; i < input_data.length; i++) {
      table.push([input_data[i][0], input_data[i][col]]);
    }
  }
  // else the scale is set to logarithmic, the data value is now an object with
  // a value v that is log10 of the original value and a formatted value f that
  // v is used for the color scale, f is used for the tooltip
  else {
    table.push([input_data[0][0], input_data[0][col] + " [MW]"]);
    for (var i = 1; i < input_data.length; i++) {
      let value = {v: Math.log10(input_data[i][col]), f: input_data[i][col]};
      table.push([input_data[i][0], value]);
    }
  }
  console.log(table);
  return table;
}

window.addEventListener('resize', drawRegionsMap, false);