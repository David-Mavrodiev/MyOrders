/*globals require module*/

let mongoose = require('mongoose');

let productSchema = new mongoose.Schema({
    title: {
        type: String,
        required: true
    },
    price: {
        type: Number,
        required: true
    },
    description: {
        type: String,
        required: true
    },
    owner: {
        type: String,
        required: true
    },
    orders: {
        type: [{
            username: String,
        }]
    }
});

mongoose.model('Product', productSchema);

let productModel = mongoose.model('Product', productSchema);

module.exports = productModel;