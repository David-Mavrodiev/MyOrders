'use strict';

const passport = require('passport'),
    data = require('../../data/users-data');

passport.serializeUser((user, done) => {
    if (user) {
        done(null, user.username);
    }
});

passport.deserializeUser((username, done) => {
    data
        .findByUsername(username)
        .then(user => done(null, user || false))
        .catch(error => done(error, false));
});

require('./local-strategy')(passport, data);

module.exports = app => {
    app.use(passport.initialize());
    app.use(passport.session());
};