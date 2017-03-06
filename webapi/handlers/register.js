'use strict';
var connection = require('../dataProvider').getConnection();

module.exports = function(request, reply) {
    console.log('registering user...');
    var username = request.payload.username;
    var password = request.payload.password;
    var role = request.payload.role;

    var firstName = request.payload.firstName;
    var lastName = request.payload.lastName;
    var address = request.payload.address;
    var phone = request.payload.phone;

    let query = 'INSERT INTO users (username, `password`, role, firstName, lastName, address, phone) ' +
                `VALUES ("${username}", "${password}", "${role}", "${firstName}", "${lastName}", "${address}", "${phone}");`;
    connection.query(query)
        .then(function(dataResult) {
            if (dataResult && dataResult.affectedRows === 1) {
                reply({ success: true });
            } else {
                reply({
                    error: {
                        type: 'user.registerError',
                        message: 'User was not able to be registered'
                    }
                });
            }
        });
};
