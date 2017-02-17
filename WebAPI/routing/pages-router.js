'use strict';

var multer = require('multer'),
	path = require('path');

    var storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, './public/uploads/')
  },
  filename: function (req, file, cb) {
    cb(null, req.body.title + '.jpg') //Appending .jpg
  }
})

var upload = multer({ storage: storage });

const router = require('express').Router(),
    createPagesController = require('../controller/pages-controller');

const productData = require('../data/products-data');
const userData = require('../data/users-data');

const pagesController = createPagesController(productData, userData);

module.exports = app => {
    router
        .get('/home', pagesController.getHome)
        .get('/addItem', pagesController.getAddItem)
        .post('/addItem', multer({ storage: storage }).single('upl'), pagesController.addItem)
        .get('/DetailProduct/:title', pagesController.getDetailProduct)
        .post('/addOrder', pagesController.addOrderToProduct)
        .get('/myProducts', pagesController.getUserProducts)
        .get('/myOrders', pagesController.getUserOrders)
        .get('/', pagesController.getHome);

    app.use(router);
};