const mongoose = require('mongoose');
const kategoriSchema = mongoose.Schema({
    kode_barang1     : {type: String, unique: true},
    nama_barang1     : String,
    jenis_barang1    : String,
    created_at1		: String
});
module.exports = mongoose.model('kategori', kategoriSchema);
