/*globals require module*/
'use strict';

const mongoose = require('mongoose');
const constants = require('../config/utils/constants');
const User = require('../models/user-model');

mongoose.connect(constants.connectionString);

const db = mongoose.connection;

db.on("error", (err) => {
    console.log("Error with connection: " + err);
});

db.on("open", () => {
    console.log("Successfully connected to database");
});

module.exports = {
    findByUsername(username) {
        let query = User.findOne()
            .where({
                username: new RegExp(username, "i")
            });

        return Promise.resolve(query.exec());
    },
    findByIsSeller(condition) {
        let query = User.find()
            .where({
                IsSeller: condition
            });

        return Promise.resolve(query.exec());
    },
    createUser(obj) {
        let user = new User({
            username: obj.username,
            password: obj.password,
            IsSeller: Boolean(obj.IsSeller)
        });

        return Promise.resolve(user.save());
    },
    addOrderToUser(title, username) {
            return new Promise((resolve, reject) => {
                User.findOneAndUpdate({
                    username: username
                }, {
                    $push: {
                        orders: {
                            title: title
                        }
                    }
                }, (err, user) => {
                    if (err) {
                        reject(err);
                    }

                    return resolve(user);
                });
            })
    }
};
