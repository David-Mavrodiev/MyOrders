var applicationConfig = require('../config.js').application;

var imageWithFilenameUrl = applicationConfig.imagesUrl + '{filename}';
var fileWithFilenameUrl = applicationConfig.filesUrl + '{filename}';

module.exports = {
    images: function(server, handler) {
        server.route({
            method: 'GET',
            path: imageWithFilenameUrl,
            handler: handler
        });
    },
    files: function(server, handler) {
        server.route({
            method: 'GET',
            path: fileWithFilenameUrl,
            handler: handler
        });
    }
}
