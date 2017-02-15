/*globals require module*/
'use strict';

const mongoose = require('mongoose');
const constants = require('../config/utils/constants');
const Product = require('../models/product-model');

mongoose.connect(constants.connectionString);

const db = mongoose.connection;

db.on("error", (err) => {
    console.log("Error with connection: " + err);
});

db.on("open", () => {
    console.log("Successfully connected to database");
});

module.exports = {
    findAll(){
        let query = Product.find().where({});
        return Promise.resolve(query.exec());
    },
    findByTitle(title) {
        let query = Product.findOne()
            .where({
                title: new RegExp(title, "i")
            });

        return Promise.resolve(query.exec());
    },
    createProduct(obj) {
        let product = new Product({
            title: obj.title,
            price: obj.price,
            description: obj.description,
            owner: obj.owner
        });

        return Promise.resolve(product.save());
    }
};
