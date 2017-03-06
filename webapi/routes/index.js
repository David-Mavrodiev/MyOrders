var loginRoutes = require('./login.js');
var static = require('./static.js');
var registerRoutes = require('./register');
var productsRoutes = require('./products.js');
var ordersRoutes = require('./orders.js');
var profileRoutes = require('./profile.js');

module.exports = {
    login: loginRoutes,
    static: static,
    register: registerRoutes,
    products: productsRoutes,
    orders: ordersRoutes,
    profile: profileRoutes
};
