'use strict';
var loginHandler = require('./login.js');
var registerHandler = require('./register');
var staticHandler = require('./static');
var productsHandlers = require('./products.js');
var ordersHandlers = require('./orders.js');
var profileHandlers = require('./profile.js');

module.exports = {
    login: loginHandler,
    register: registerHandler,
    static: staticHandler,
    products: productsHandlers,
    orders: ordersHandlers,
    profile: profileHandlers
};
