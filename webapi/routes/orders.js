'use strict';

var applicationConfig = require('../config.js').application;
var ordersUrl = applicationConfig.apiUrl + 'orders';
var orderWithIdUrl = ordersUrl + '/{orderId}';

module.exports = {
    getAllMy: function(server, handler) {
        server.route({
            method: 'GET',
            path: ordersUrl,
            handler: handler
        });
    },
    getById: function(server, handler) {
        server.route({
            method: 'GET',
            path: orderWithIdUrl,
            handler: handler
        });
    },
    create: function(server, handler) {
        server.route({
            method: 'POST',
            path: ordersUrl,
            handler: handler
        });
    },
    edit: function(server, handler) {
        server.route({
            method: 'PUT',
            path: orderWithIdUrl,
            handler: handler
        });
    }
};
