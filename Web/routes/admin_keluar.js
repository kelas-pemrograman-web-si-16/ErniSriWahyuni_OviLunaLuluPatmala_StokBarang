var express = require('express');
var crypto = require('crypto')

var User = require('../model/user')
var BarangKeluar = require('../model/barangkeluar')
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

router.get('/datakeluar', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    BarangKeluar.find({}, function(err, barangkeluar) {
        console.log(barangkeluar);
        res.render('users/barangkeluar/tabless', { session_store: session_store, barangkeluars: barangkeluar })
    }).select('_id kodebarang namabarang jenisbarang createdAt')
});

router.get('/inputkeluar', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session
    res.render('users/barangkeluar/input_barang', { session_store: session_store})
});


//input data barang

router.post('/inputkeluar', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    BarangKeluar.find({ kodebarang: req.body.kodebarang }, function(err, Barangkeluar) {
        if (Barangkeluar.length == 0) {
            var databarang = new BarangKeluar({
                kodebarang: req.body.kodebarang,
                namabarang: req.body.namabarang,
                jenisbarang: req.body.jenisbarang,

            })
           databarang(function(err) {
                if (err) {
                    console.log(err);
                    req.flash('msg_error', 'Maaf, nampaknya ada masalah di sistem kami')
                    res.redirect('/datakeluar')
                } else {
                    req.flash('msg_info', 'User telah berhasil dibuat')
                    res.redirect('/datakeluar')
                }
            })
        } else {
            req.flash('msg_error', 'Maaf, kode barang sudah ada....')
            res.render('users/barangkeluar/datakeluar', {
                session_store: session_store,
                kodebarang: req.body.kodebarang,
                namabarang: req.body.namabarang,
                jenisbarang: req.body.jenisbarang,
            })
        }
    })
})

//menampilkan data berdasarkan id

router.get('/:id/keluaredit', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    BarangKeluar.findOne({ _id: req.params.id }, function(err, barangkeluar) {
        if (barangkeluar) {
            console.log("barangkeluars"+barangkeluar);
            res.render('users/barangkeluar/keluaredit', { session_store: session_store, barangkeluars: barangkeluar })
        } else {
            req.flash('msg_error', 'Maaf, Data tidak ditemukan')
            res.redirect('/datakeluar')
        }
    })
})

router.post('/:id/keluaredit', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    session_store = req.session

    BarangKeluar.findById(req.params.id, function(err, barangkeluar) {
        barangkeluar.kodebarang = req.body.kodebarang;
        barangkeluar.namabarang = req.body.namabarang;
        barangkeluar.jenisbarang = req.body.jenisbarang;

        barangkeluar.save(function(err, user) {
            if (err) {
                req.flash('msg_error', 'Maaf, sepertinya ada masalah dengan sistem kami...');
            } else {
                req.flash('msg_info', 'Edit data berhasil!');
            }

            res.redirect('/datakeluar');

        });
    });
})

router.post('/:id/hapus', Auth_middleware.check_login, Auth_middleware.is_admin, function(req, res, next) {
    BarangKeluar.findById(req.params.id, function(err, barangkeluar){
        barangkeluar.remove(function(err, barangkeluar){
            if (err)
            {
                req.flash('msg_error', 'Maaf, user yang dimaksud sudah tidak ada. Dan kebetulan lagi ada masalah sama sistem kami :D');
            }
            else
            {
                req.flash('msg_info', 'Data barang berhasil dihapus!');
            }
            res.redirect('/datakeluar');
        })
    })
})



module.exports = router;
