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
  let result =[[ "Country", ...ENERGY_SOURCES], ...(COUNTRY_LIST.map(x =>[x, ...((new multipleResult(x)).values)]))];
  console.log(result)
  res.render('index.ejs', { data : JSON.stringify(result) }); 
});

/*
router.get('/SolarSweden', function(req, res, next) {
  let result = queryResult("Sweden", "Solar");
  console.log(result)
  res.render('index', { quantity: result.value });
});

router.get('/generationSweden', function(req, res, next) {
  let result = new multipleResult("Sweden")
  console.log(result)
  res.render('index', { title: result.values }); 
});

router.get('/fossilGas', function(req, res, next) {
  let result = queryResult(req.query.source, "Fossil Gas");
  console.log(result)
  res.render('index', { title: result.value });
});


router.get('/generation', function(req, res, next) {
  let result = new multipleResult(req.query.country)
  console.log(req.query.country)
  res.render('index', { title: [req.query.country, ...result.values] }); 
});


router.get('/map', function(req, res, next) {
  let result =[ENERGY_SOURCES, ...(COUNTRY_LIST.map(x =>[x, ...((new multipleResult(x)).values)]))]
  console.log(result)
  res.render('index', { title: JSON.stringify(result) }); //(new queryResult()).value } );
});
*/

module.exports = router;