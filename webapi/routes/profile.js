'use strict';

var applicationConfig = require('../config.js').application;
var profileUrl = applicationConfig.apiUrl + 'profile';

module.exports = {
    get: function(server, handler) {
        server.route({
            method: 'GET',
            path: profileUrl,
            handler: handler
        });
    },
    edit: function(server, handler) {
        server.route({
            method: 'PUT',
            path: profileUrl,
            handler: handler
        });
    }
};
