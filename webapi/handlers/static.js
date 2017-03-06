'use strict';
var fs = require('fs');

var applicationConfig = require('../config.js').application;
var connection = require('../dataProvider').getConnection();

module.exports = {
    images: function(request, reply) {
        console.log("getting image...");
        var file = request.params.filename;
        var path = applicationConfig.uploadsFolder + file;
        var extension = file.substr(file.lastIndexOf('.') + 1);
        fs.readFile(path, function(error, content) {
            if (error) {
                return reply("file not found");
            }

            return reply.file(path);
        });
    },
    files: function(request, reply) {
        console.log('getting static file...');
        var file = request.params.filename;
        var path = applicationConfig.staticFolder + file;
        fs.readFile(path, function(error, content) {
            if (error) {
                return reply("file not found");
            }

            return reply.file(path);
        });
    }
};