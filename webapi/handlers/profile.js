'use strict';
var connection = require('../dataProvider').getConnection();
var helpers = require('../helpers.js');

module.exports = {
    get: function(request, reply) {
        console.log('fetching user information...');
        let headers = helpers.parseRequestHeader(request);

        let query = `SELECT * FROM users WHERE id = ${headers.id}`;

        connection.query(query)
            .then(function(dataResult) {
                if (dataResult && dataResult.length === 1) {
                    var userResult = dataResult[0];
                    delete userResult.id;
                    reply(userResult);
                } else {
                    reply({});
                }
            });
    },
    edit: function(request, reply) {
        console.log('editing new products');
        let headers = helpers.parseRequestHeader(request);

        let password = request.payload.password;
        let firstName = request.payload.firstName;
        let lastName = request.payload.lastName;
        let address = request.payload.address;
        let phone = request.payload.phone;

        let query = 'UPDATE users ' +
                    `SET password = '${password}', firstName = '${firstName}', lastName = '${lastName}', address = '${address}', phone = '${phone}' ` +
                    `WHERE id = ${headers.id}`;

        connection.query(query)
            .then(function(dataResult) {
                if (dataResult && dataResult.affectedRows === 1) {
                    reply({ success: true });
                } else {
                    reply({
                        error: {
                            type: 'user.userNotFound',
                            message: 'User was not found'
                        }
                    });
                }
            });
    }
};
