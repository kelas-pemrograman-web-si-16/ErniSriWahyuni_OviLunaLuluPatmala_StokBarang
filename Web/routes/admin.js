var express = require('express');
var crypto = require('crypto')

var User = require('../model/user')
var Data = require('../model/data')
var Auth_middleware = require('../middlewares/auth')

var router = express.Router();
var secret = 'rahasia'
var session_store

/* GET users listing. */
router.get('/admin', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    User.find({}, function(err, user) {
        console.log(user);
        res.render('users/home', { session_store: session_store, users: user })
    }).select('username email firstname lastname users createdAt updatedAt')
});

router.get('/databarang', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    Data.find({}, function(err, data) {
        // console.log(data);
        res.render('users/data/table', { session_store: session_store, datas: data })
    }).select('_id kode_barang nama_barang jenis_barang harga_barang createdAt')
});

/* GET users listing. */
router.get('/inputdata', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session
    res.render('users/data/input_data', { session_store: session_store})
});


//input data barang
router.post('/inputdata', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    Data.find({ kode_barang: req.body.kode_barang }, function(err, data) {
        if (data.length == 0) {
            var stokbarang = new Data({
                kode_barang: req.body.kode_barang,
                nama_barang: req.body.nama_barang,
                jenis_barang: req.body.jenis_barang,
                harga_barang: req.body.harga_barang,
            })
            stokbarang.save(function(err) {
                if (err) {
                    console.log(err);
                    req.flash('msg_error', 'Maaf, nampaknya ada masalah di sistem kami')
                    res.redirect('/databarang')
                } else {
                    req.flash('msg_info', 'User telah berhasil dibuat')
                    res.redirect('/databarang')
                }
            })
        } else {
            req.flash('msg_error', 'Maaf, kode barang sudah ada....')
            res.render('users/data/input_data', {
                session_store: session_store,
                kode_barang: req.body.kode_barang,
                nama_barang: req.body.nama_barang,
                jenis_barang: req.body.jenis_barang,
                harga_barang: req.body.harga_barang,
            })
        }
    })
})

//menampilkan data berdasarkan id
router.get('/:id/editdata', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    Data.findOne({ _id: req.params.id }, function(err, data) {
        if (data) {
            console.log("datas"+data);
            res.render('users/data/edit_data', { session_store: session_store, datas: data })
        } else {
            req.flash('msg_error', 'Maaf, Data tidak ditemukan')
            res.redirect('/databarang')
        }
    })
})

router.post('/:id/editdata', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    Data.findById(req.params.id, function(err, data) {
        data.kode_barang = req.body.kode_barang;
        data.nama_barang = req.body.nama_barang;
        data.jenis_barang = req.body.jenis_barang;
        data.harga_barang = req.body.harga_barang;

        data.save(function(err, user) {
            if (err) {
                req.flash('msg_error', 'Maaf, sepertinya ada masalah dengan sistem kami...');
            } else {
                req.flash('msg_info', 'Edit data berhasil!');
            }

            res.redirect('/databarang');

        });
    });
})

router.post('/:id/delete', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    Data.findById(req.params.id, function(err, data){
        data.remove(function(err, Data){
            if (err)
            {
                req.flash('msg_error', 'Maaf, user yang dimaksud sudah tidak ada. Dan kebetulan lagi ada masalah sama sistem kami :D');
            }
            else
            {
                req.flash('msg_info', 'Data barang berhasil dihapus!');
            }
            res.redirect('/databarang');
        })
    })
})

module.exports = router;
