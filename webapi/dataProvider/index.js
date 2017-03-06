'use strict';
var config = require('../config.js');
var dbConfig = config.db;

var mysql = require('promise-mysql');

let connection;

let init = new Promise(function(resolve, reject) {
    console.log('Initing db connection...');
    mysql.createConnection({
        host: dbConfig.host,
        user: dbConfig.user,
        password: dbConfig.password,
        database: dbConfig.database,
        port: dbConfig.port
    }).then(function(conn){
        connection = conn;
        console.log('Db connection was init successfully.');
        resolve(conn);
    });
});

module.exports = {
    init: init,
    getConnection: () => { return connection; }
};
