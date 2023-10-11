var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

const CronJob = require('cron').CronJob;
const java = require('java');

java.classpath.push('java_results');
java.classpath.push('../lib/sqlite-jdbc-3.43.0.0.jar');
const dbUpdate = java.import("DBupdate");

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

var app = express();

// Cron job for updating database
const dbJob = new CronJob('15 * * * *', 
  onTick=() => { // Run 15 mins past every hour
    console.log("Initializing the database")
    dbUpdate.updateValues();
    console.log("Database updated correctly")
  },
  onComplete=null,
  start=true, // Start job
  timeZone=null,
  context=null,
  runOnInit=true // Run when server starts
);


// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
