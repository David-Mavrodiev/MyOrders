'use strict';
var connection = require('../dataProvider').getConnection();
var helpers = require('../helpers.js');

module.exports = {
    getAllMy: function(request, reply) {
        console.log('fetching my orders...');
        let headers = helpers.parseRequestHeader(request);

        let query = `SELECT o.id, o.quantity, o.singlePrice, o.totalPrice, o.deliveryAddress, o.dateOrdered, o.status, p.title as productTitle, p.imageIdentifier as productImageIdentifier, p.description as productDescription` +
                        `, u.firstName as productSellerFirstName, u.lastName as productSellerLastName, u.address as productSellerAddress,u.phone as productSellerPhone, u.username as productSellerUsername,` +
                        `ub.firstName as buyerFirstName, ub.lastName as buyerLastName, ub.address as buyerAddress, ub.phone as buyerPhone, ub.username as buyerUsername ` +
                    `FROM orders o ` +
                    `INNER JOIN users ub ON o.buyerId = ub.id ` + 
                    `INNER JOIN products p ON p.id = o.productId ` +
                    `INNER JOIN users u on u.id = p.sellerId `;

        var productIdQueryParam = request.query.productId;
        if (headers.role === 'seller') {
            if (productIdQueryParam) {
                query += `WHERE p.sellerId = ${headers.id} AND p.id = ${productIdQueryParam} `;
            } else {
                query += `WHERE p.sellerId = ${headers.id} `;
            }
            query += 'ORDER BY id DESC';
        } else {
            if (productIdQueryParam) {
                query += `WHERE o.buyerId = ${headers.id} AND p.id = ${productIdQueryParam} `;
            } else {
                query += `WHERE o.buyerId = ${headers.id} `;
            }
            query += 'ORDER BY id DESC';
        }

        connection.query(query)
            .then(function(dataResult) {
                var parsedDataResult = dataResult.map(function(singleResult) {
                    var dateOrdered = new Date(singleResult.dateOrdered);
                    singleResult.dateOrdered = dateOrdered;
                    return singleResult
                });
                reply(parsedDataResult);
            });
    },
    getById: function(request, reply) {
        console.log('get order by id...');
        let headers = helpers.parseRequestHeader(request);
        let orderId = request.params.orderId;

        let query = `SELECT o.id, o.quantity, o.singlePrice, o.totalPrice, o.deliveryAddress, o.dateOrdered, o.status, p.title as productTitle, p.imageIdentifier as productImageIdentifier, p.description as productDescription` +
                        `, u.firstName as productSellerFirstName, u.lastName as productSellerLastName, u.address as productSellerAddress,u.phone as productSellerPhone, u.username as productSellerUsername,` +
                        `ub.firstName as buyerFirstName, ub.lastName as buyerLastName, ub.address as buyerAddress, ub.phone as buyerPhone, ub.username as buyerUsername ` +
                    `FROM orders o ` +
                    `INNER JOIN users ub ON o.buyerId = ub.id ` +
                    `INNER JOIN products p ON p.id = o.productId ` +
                    `INNER JOIN users u on u.id = p.sellerId `;

        if (headers.role === 'seller') {
            query += `WHERE p.sellerId = ${headers.id} AND o.id = ${orderId}`;
        } else {
            query += `WHERE o.buyerId = ${headers.id} AND o.id = ${orderId}`
        }

        connection.query(query)
            .then(function(dataResult) {
                if (dataResult.length === 1) {
                    var dateOrdered = new Date(dataResult[0].dateOrdered);
                    dataResult[0].dateOrdered = dateOrdered;
                    reply(dataResult[0]);
                } else {
                    reply({});
                }
            })
    },
    create: function(request, reply) {
        console.log('creating new order');
        let headers = helpers.parseRequestHeader(request);

        if (headers.role === 'buyer') {
            let productId = request.payload.productId;
            let quantity = request.payload.quantity;
            let deliveryAddress = request.payload.deliveryAddress;

            let productQuery = `SELECT * FROM products WHERE id = ${productId}`;
            connection.query(productQuery)
                .then(function(dataResult) {
                    if (dataResult.length > 0) {
                        let product = dataResult[0];
                        let orderQuery = 'INSERT INTO orders (productId, buyerId, quantity, singlePrice, totalPrice, deliveryAddress) ' +
                                          `VALUES (${product.id}, ${headers.id}, ${quantity}, ${product.price}, ${product.price * quantity}, "${deliveryAddress}");`;
                        return connection.query(orderQuery);
                    } else {
                        return;
                    }
                })
                .then(function(orderQuery) {
                    if (orderQuery) {
                        reply({
                            success: true
                        });
                    } else {
                        reply({
                            error: {
                                type: 'order.productNotFound',
                                message: 'Product not found'
                            }
                        });
                    }
                });
        } else {
            reply({
                error: {
                    type: 'order.noPermissions',
                    message: 'You have no permissions to make an order'
                }
            });
        }
    },
    edit: function(request, reply) {
        console.log('editing existing order...');
        let headers = helpers.parseRequestHeader(request);
        let orderId = request.params.orderId;

        let newStatus = request.payload.status;

        let query = 'UPDATE orders ' +
                        'SET `status` = "' + newStatus + '" ' + 
                        `WHERE id = ${orderId}`;

        let errorObject = {
            type: 'order.noPermissions',
            message: 'You have no permissions to change order status'
        };

        var checkPermission = function(role, newStatus) {
            if (role === 'seller') {
                return newStatus === 'send' || newStatus === 'rejected';
            } else if (role === 'buyer') {
                return newStatus === 'received' || newStatus === 'notReceived';
            } else {
                return false;
            }
        };
        var hasPermission = checkPermission(headers.role, newStatus);

        if (hasPermission) {
            connection.query(query)
                .then(function(dataResult) {
                    if (dataResult && dataResult.affectedRows === 1) {
                        reply({
                            id: orderId,
                            status: newStatus
                        });
                    } else {
                        reply(errorObject);
                    }
                })
        } else {
            reply(errorObject);
        }
        
    }
};
