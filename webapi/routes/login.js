var applicationConfig = require('../config.js').application;

module.exports = function(server, handler) {
    server.route({
        method: 'POST',
        path: applicationConfig.apiUrl + 'login',
        handler: handler
    });
};
