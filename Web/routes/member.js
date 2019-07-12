var express = require('express');
var crypto = require('crypto')

var Data = require('../model/data')
var User = require('../model/user')
var Auth_middleware = require('../middlewares/auth')

var router = express.Router();
var secret = 'rahasia'
var session_store

/* GET users listing. */
router.get('/member', Auth_middleware.check_login, Auth_middleware.is_member, function(req, res, next) {
    session_store = req.session

    User.find({}, function(err, user) {
        console.log(user);
        res.render('users/home', { session_store: session_store, users: user })
    })
});


/* GET users listing. */
router.get('/databarangmember', Auth_middleware.check_login, Auth_middleware.is_member, function(req, res, next) {
    session_store = req.session

    Data.find({}, function(err, data) {
        console.log(data);
        res.render('users/data/table', { session_store: session_store, datas: data })
    }).select(' kode_barang nama_barang jenis_barang harga_barang created_at')
});

module.exports = router;
