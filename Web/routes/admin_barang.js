var express = require('express');
var crypto = require('crypto')

var User = require('../model/user')
var BarangMasuk = require('../model/barangmasuk')
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

router.get('/barangdata', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    BarangMasuk.find({}, function(err, barangmasuk) {
        console.log(barangmasuk);
        res.render('users/barangmasuk/tables', { session_store: session_store, barangmasuks: barangmasuk })
    }).select('_id kodebarangg namabarangg jenisbarangg createdAt')
});

router.get('/datainput', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session
    res.render('users/barangmasuk/input', { session_store: session_store})
});


//input data barang

router.post('/datainput', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    BarangMasuk.find({ kodebarangg: req.body.kodebarangg }, function(err, barangmasuk) {
        if (barangmasuk.length == 0) {
            var masuk = new BarangMasuk({
                kodebarangg: req.body.kodebarangg,
                namabarangg: req.body.namabarangg,
                jenisbarangg: req.body.jenisbarangg,

            })
            masuk.save(function(err) {
                if (err) {
                    console.log(err);
                    req.flash('msg_error', 'Maaf, nampaknya ada masalah di sistem kami')
                    res.redirect('/barangdata')
                } else {
                    req.flash('msg_info', 'User telah berhasil dibuat')
                    res.redirect('/barangdata')
                }
            })
        } else {
            req.flash('msg_error', 'Maaf, kode barang sudah ada....')
            res.render('users/barangmasuk/input', {
                session_store: session_store,
                kodebarangg: req.body.kodebarangg,
                namabarangg: req.body.namabarangg,
                jenisbarangg: req.body.jenisbarangg,
            })
        }
    })
})

//menampilkan data berdasarkan id

router.get('/:id/dataedit', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    BarangMasuk.findOne({ _id: req.params.id }, function(err, barangmasuk) {
        if (barangmasuk) {
            console.log("barangmasuks"+ barangmasuk);
            res.render('users/barangmasuk/dataedit', { session_store: session_store, barangmasuks: barangmasuk })
        } else {
            req.flash('msg_error', 'Maaf, Data tidak ditemukan')
            res.redirect('/barangdata')
        }
    })
})

router.post('/:id/dataedit', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    BarangMasuk.findById(req.params.id, function(err, barangmasuk) {
        barangmasuk.kodebarangg = req.body.kodebarangg;
        barangmasuk.namabarangg = req.body.namabarangg;
        barangmasuk.jenisbarangg = req.body.jenisbarangg;

        barangmasuk.save(function(err, user) {
            if (err) {
                req.flash('msg_error', 'Maaf, sepertinya ada masalah dengan sistem kami...');
            } else {
                req.flash('msg_info', 'Edit data berhasil!');
            }

            res.redirect('/barangdata');

        });
    });
})

router.post('/:id/deletedata', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    BarangMasuk.findById(req.params.id, function(err, barangmasuk){
        barangmasuk.remove(function(err, barangmasuk){
            if (err)
            {
                req.flash('msg_error', 'Maaf, user yang dimaksud sudah tidak ada. Dan kebetulan lagi ada masalah sama sistem kami :D');
            }
            else
            {
                req.flash('msg_info', 'Data barang berhasil dihapus!');
            }
            res.redirect('/barangdata');
        })
    })
})



module.exports = router;
