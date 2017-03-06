'use strict';
var config = require('./config');
var dataProvider = require('./dataProvider');
var routes = require('./routes');

const Hapi = require('hapi');

// Init DataProvider connection
dataProvider.init
    .then(function(dataProviderConnection) {
        var handlers = require('./handlers');
        // Init server
        const server = new Hapi.Server();
        server.connection({ port: config.application.port, host: config.application.host });

        server.register(require('inert'), (err) => {
            if (err) {
                throw err;
            }

                // Login & register routes
            routes.login(server, handlers.login);
            routes.register(server, handlers.register);
                // static images/files
            routes.static.images(server, handlers.static.images);
            routes.static.files(server, handlers.static.files);
                // products
            routes.products.getAll(server, handlers.products.getAll);
            routes.products.getById(server, handlers.products.getById);
            routes.products.create(server, handlers.products.create);
            routes.products.edit(server, handlers.products.edit);
            routes.products.delete(server, handlers.products.delete);
                // orders
            routes.orders.getAllMy(server, handlers.orders.getAllMy);
            routes.orders.getById(server, handlers.orders.getById);
            routes.orders.create(server, handlers.orders.create);
            routes.orders.edit(server, handlers.orders.edit);
                // profile
            routes.profile.get(server, handlers.profile.get);
            routes.profile.edit(server, handlers.profile.edit);

            // Start server
            server.start((err) => {
                if (err) {
                    throw err;
                }
                console.log(`Server running at: ${server.info.uri}`);
            });
        });        
    });
