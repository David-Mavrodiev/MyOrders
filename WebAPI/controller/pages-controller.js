'use strict';

var path = require('path'),
    fs = require('fs');

module.exports = function() {
    return {
        getHome(req, res) {
            if (req.isAuthenticated()) {
                const user = req.user;
                res.render("../views/home.pug", {
                    result: {
                        isUserAuthenticated: true,
                        username: user.username,
                        IsSeller: user.IsSeller
                    }
                });
            } else {
                res.render("../views/home.pug", {
                    result: {
                        isUserAuthenticated: false
                     }
                });
            }
        },
        getAddItem(req, res){
            if (req.isAuthenticated()) {
                const user = req.user;
                res.render("../views/addItem.pug", {
                    result: req.user
                });
            } else {
                res.render("../views/login.pug");
            }
        },
        addItem(req, res){
            console.log(req.body);
            console.log(req.file);
        }
    }
}