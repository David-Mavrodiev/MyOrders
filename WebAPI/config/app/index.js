'use strict';

const express = require('express'),
    session = require('express-session'),
    cookieParser = require('cookie-parser'),
    bodyParser = require('body-parser');

var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cookieParser());
app.use(session({ secret: 'purple unicorn' }));
app.use("/static", express.static("public"));
app.set("view engine", "pug");

require('../passport/')(app);
require('../../routing/users-router')(app);
require('../../routing/pages-router')(app);

module.exports = app;