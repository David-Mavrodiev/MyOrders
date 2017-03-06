'use strict';
var fs = require('fs');
var multiparty = require('multiparty');
const uuidV1 = require('uuid/v1');
var Jimp = require("jimp");

var applicationConfig = require('../config.js').application;
var connection = require('../dataProvider').getConnection();
var helpers = require('../helpers.js');

module.exports = {
    getAll: function(request, reply) {
        console.log('fetching products...');
        let headers = helpers.parseRequestHeader(request);

        let query = 'SELECT p.id, p.title, p.price, p.quantity, p.imageIdentifier, p.description, p.dateAdded, u.username as sellerUsername, u.firstName as sellerFirstName, u.lastName as sellerLastName ' +
                    'FROM products p ' +
                    'INNER JOIN users u ON u.id = p.sellerId ';
        if (headers.role === 'seller') {
            query += `WHERE p.sellerId = ${headers.id} `;
        }
        query += 'ORDER BY id DESC';

        connection.query(query)
            .then(function(dataResult) {
                var parsedDataResult = dataResult.map(function(singleResult) {
                    var dateAdded = new Date(singleResult.dateAdded);
                    singleResult.dateAdded = dateAdded;
                    return singleResult
                });
                reply(parsedDataResult);
            });
    },
    getById: function(request, reply) {
        console.log('get product by id...');
        let headers = helpers.parseRequestHeader(request);
        let productId = request.params.productId;

        let query = 'SELECT p.id, p.title, p.price, p.quantity, p.imageIdentifier, p.description, p.dateAdded, u.username as sellerUsername, u.firstName as sellerFirstName, u.lastName as sellerLastName ' +
                    'FROM products p ' +
                    'INNER JOIN users u ON u.id = p.sellerId ';
        if (headers.role === 'seller') {
            query += `WHERE p.id = ${productId} AND p.sellerId = ${headers.id}`;
        } else {
            query += `WHERE p.id = ${productId}`;
        }

        connection.query(query)
            .then(function(dataResult) {
                if (dataResult.length === 1) {
                    var dateAdded = new Date(dataResult[0].dateAdded);
                    dataResult[0].dateAdded = dateAdded;
                    reply(dataResult[0]);
                } else {
                    reply({});
                }
            });
    },
    create: function(request, reply) {
        console.log('creating new product');

        var imageIdentifier = uuidV1();
        var form = new multiparty.Form();
        form.parse(request.payload, function(error, fields, files) {
            if (error) {
                return reply(error);
            } else {
                fs.readFile(files.image[0].path, function(readFilerError, data) {
                    var originalFilename = files.image[0].originalFilename;
                    var imageExtension = originalFilename.split('.').pop();
                    var imageNameWithExtension = `${imageIdentifier}.${imageExtension}`;

                    // checkFileExist();
                    Jimp.read(files.image[0].path, function (jimpError, uploadImage) {
                        if (jimpError) {
                            reply(jimpError);
                        }
                        uploadImage
                            .cover(256, 256)             // scale the image to the given width and height
                            .quality(60)                 // set JPEG quality
                            .write(`${applicationConfig.uploadsFolder}${imageNameWithExtension}`); // save

                        // SQL query
                        let headers = helpers.parseRequestHeader(request);
                        let title = fields.title[0];
                        let price = fields.price[0];
                        let quantity = fields.quantity[0];
                        let description = fields.description[0];

                        let query = 'INSERT INTO products (sellerId, title, price, quantity, imageIdentifier, description) ' +
                                    `VALUES (${headers.id}, '${title}', ${price}, ${quantity}, '${imageNameWithExtension}', '${description}');`;
                        connection.query(query)
                            .then(function(dataResult) {
                                reply({
                                    success: true,
                                    title,
                                    price,
                                    quantity,
                                    imageIdentifier,
                                    description
                                });
                            });
                    }).catch(function (err) {
                        reply(jimpError);
                    });
                });
            }
        });
    },
    delete: function(request, reply) {
        console.log('deleting existing product...');
        let headers = helpers.parseRequestHeader(request);
        let productId = request.params.productId;

        let query = `DELETE FROM products WHERE id = ${productId} AND sellerId = ${headers.id}`;
        connection.query(query)
            .then(function(dataResult) {
                if (dataResult && dataResult.affectedRows === 1) {
                    reply({
                        id: productId
                    });
                } else {
                    reply({
                        error: {
                            type: 'product.productNotFound',
                            message: 'Product was not found'
                        }
                    });
                }
            });
    },
    edit: function(request, reply) {
        console.log('editing existing products...');
        let headers = helpers.parseRequestHeader(request);
        let productId = request.params.productId;

        let title = request.payload.title;
        let price = request.payload.price;
        let quantity = request.payload.quantity;
        let description = request.payload.description;

        let query = 'UPDATE products ' +
                    `SET title='${title}', price = ${price}, quantity = ${quantity}, description = '${description}' ` +
                    `WHERE id = ${productId} AND sellerId = ${headers.id}`;
        connection.query(query)
            .then(function(dataResult) {
                if (dataResult && dataResult.affectedRows === 1) {
                    reply({ 
                        id: productId,
                        title,
                        price,
                        quantity,
                        description
                    });
                } else {
                    reply({
                        error: {
                            type: 'product.productNotFound',
                            message: 'Product was not found'
                        }
                    });
                }
            });
    }
};
