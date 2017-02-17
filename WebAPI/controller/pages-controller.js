'use strict';

var path = require('path'),
    fs = require('fs');

module.exports = function(productData, userData) {
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
                res.redirect("/login");
            }
        },
        getDetailProduct(req, res){
            if(req.isAuthenticated()){
                let title = req.params.title;
                productData.findByTitle(title).then(product => {
                    res.render('../views/detail-item.pug', {    
                        result: {
                            product: product,
                            user: req.user
                        }
                    });
                });
            }else{
                res.redirect("/login");
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
        },
        addOrderToProduct(req, res){
            let username = req.user.username;
            let title = req.body.title;
            userData.addOrderToUser(title, username).then(user => {
                productData.addOrderToProduct(title, username).then((p) => {
                  res.redirect('/home');
                });
            })
        },
        getUserProducts(req, res){
            if(req.isAuthenticated() && req.user.IsSeller){
                let username = req.user.username;
                productData.findByOwner(username).then(products => {
                  res.render('../views/myProducts.pug', {
                      result:{
                            isUserAuthenticated: true,
                            username: req.user.username,
                            IsSeller: req.user.IsSeller,
                            products: products
                      }
                  });
                });
            }else{
                res.render('../views/error.pug', {
                        result: {
                            error: 'You are not seller!',
                            back: '/home'
                        }
                });
            }
        },
        getUserOrders(req, res){
            if(req.isAuthenticated()){
                  res.render('../views/myOrders.pug', {
                      result:{
                            isUserAuthenticated: true,
                            username: req.user.username,
                            IsSeller: req.user.IsSeller,
                            products: req.user.orders
                      }
                  });
            }else{
                res.render('../views/error.pug', {
                        result: {
                            error: 'You have to log in!',
                            back: '/home'
                        }
                });
            }
        }
    }
}