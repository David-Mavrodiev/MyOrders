'use strict';

var multer = require('multer'),
	path = require('path');

    var storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, './uploads/')
  },
  filename: function (req, file, cb) {
    cb(null, req.body.title + '.jpg') //Appending .jpg
  }
})

var upload = multer({ storage: storage });

const router = require('express').Router(),
    createPagesController = require('../controller/pages-controller');

const pagesController = createPagesController();

module.exports = app => {
    router
        .get('/home', pagesController.getHome)
        .get('/addItem', pagesController.getAddItem)
        .post('/addItem', multer({ storage: storage }).single('upl'), pagesController.addItem)
        .get('/', pagesController.getHome);

    app.use(router);
};