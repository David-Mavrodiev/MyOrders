'use strict';

const router = require('express').Router(),
    createAuthController = require('../controller/auth-controller'),
    createUsersController = require('../controller/users-controller'),
    usersData = require('../data/users-data'),
    passport = require('passport');

const authController = createAuthController(usersData),
    usersController = createUsersController(usersData);

module.exports = app => {
    router
        .get('/login', usersController.getLogin)
        .post('/login', authController.loginLocal)
        .get('/logout', authController.logout)
        .post('/register', authController.register)
        .get('/profile', usersController.getProfile)
        .get('/unauthorized', usersController.getUnauthorized);

    app.use(router);
};