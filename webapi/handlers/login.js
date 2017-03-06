'use strict';
var connection = require('../dataProvider').getConnection();

module.exports = function(request, reply) {
    console.log('login...');
    var username = request.payload.username;
    var password = request.payload.password;

    let query = `SELECT * FROM users WHERE username = '${username}' AND password = '${password}'`;
    connection.query(query)
        .then(function(dataResult) {
            let result = {};
            if (dataResult.length === 1) {
                let cookie = {
                    id: dataResult[0].id,
                    username: dataResult[0].username,
                    role: dataResult[0].role
                };

                result = {
                    success: true,
                    cookie: cookie,
                    username: dataResult[0].username,
                    role: dataResult[0].role,
                    firstName: dataResult[0].firstName,
                    lastName: dataResult[0].lastName,
                    address: dataResult[0].address,
                    phone: dataResult[0].phone
                }
            } else {
                result.error = {
                    type: 'login.invalidCredentials',
                    message: 'Ivanlid credentials'
                };
            }
            reply(result);
        });
};
