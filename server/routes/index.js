var express = require('express');
var router = express.Router();
const java = require('java');
java.classpath.push('java_results');
const queryResult = java.import("ResultOfQuery");
const multipleResult = java.import("ResultMultipleSources");
let ENERGY_SOURCES=["Mixed", "Generation", "Load", "Biomass", "Fossil Brown coal/Lignite", "Fossil Coal-derived gas", "Fossil Gas", "Fossil Hard coal", "Fossil Oil", "Fossil Oil shale", "Fossil Peat", "Geothermal", "Hydro Pumped Storage", "Hydro Run-of-river and poundage", "Hydro Water Reservoir", "Marine", "Nuclear", "Other renewable", "Solar", "Waste", "Wind Offshore", "Wind Onshore", "Other", "AC Link", "DC Link", "Substation", "Transformer"];
let COUNTRY_LIST=["Albania","Armenia","Austria","Azerbaijan","Belarus","Belgium","Bosnia and Herz.","Bulgaria","Croatia","Cyprus","Czech Republic","Denmark","Estonia","Finland","France","Georgia","Germany","Greece","Hungary","Iceland ","Ireland","Italy","Kosovo","Latvia","Lithuania","Luxembourg","Malta ","Moldova","Montenegro","Netherlands","North Macedonia","Norway","Poland","Portugal","Romania","Russia","Serbia","Slovakia","Slovenia","Spain","Sweden","Switzerland","Turkey","Ukraine","United Kingdom"];

/* GET home page. 
    parameter for index.ejs file: data 
*/
router.get('/', function(req, res, next) {
  let resultProd =[[ "Country", ...ENERGY_SOURCES], ...(COUNTRY_LIST.map(x =>[x, ...((new multipleResult(x,"EnergyProduction")).values)]))];
  let resInstalled= [[ "Country", ...ENERGY_SOURCES], ...(COUNTRY_LIST.map(x =>[x, ...((new multipleResult(x,"EnergyInstalled")).values)]))];
  //console.log(result)
  res.render('index.ejs', { dataProd : JSON.stringify(resultProd),dataInst : JSON.stringify(resInstalled) }); 
});

module.exports = router;