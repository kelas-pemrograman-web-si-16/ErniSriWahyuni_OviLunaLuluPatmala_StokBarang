const mongoose = require('mongoose');
const dataSchema = mongoose.Schema({
    kode_barang     : {type: String, unique: true},
    nama_barang     : String,
    jenis_barang 	: String,
    harga_barang	: String,
    created_at		: String
});
module.exports = mongoose.model('data', dataSchema);
