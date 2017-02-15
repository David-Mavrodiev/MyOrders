'use strict';

var path = require('path'),
    fs = require('fs');

module.exports = function(productData) {
    return {
        getHome(req, res) {
            if (req.isAuthenticated()) {
                const user = req.user;
                productData.findAll().then(products => {
                    res.render("../views/home.pug", {
                        result: {
                            isUserAuthenticated: true,
                            username: req.user.username,
                            IsSeller: req.user.IsSeller,
                            products: products
                        }
                    });
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
            let product = {
                title: req.body.title,
                price: req.body.price,
                description: req.body.description,
                owner: req.user.username
            };
            productData.findByTitle(product.title).then(p => {
                if(p != undefined && p != null){
                   res.render('../views/error.pug', {
                        result: {
                            error: 'Title is already taken',
                            back: '/home'
                        }
                    }); 
                }else{
                   productData.createProduct(product).then(() => {
                     res.redirect('/home');
                   });
                }
            });
        }
    }
}