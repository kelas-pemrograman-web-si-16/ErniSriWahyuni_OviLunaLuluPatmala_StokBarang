const mongoose = require('mongoose');
const barangkeluarSchema = mongoose.Schema({
    kodebarang      : {type: String, unique: true},
    namabarang      : String,
    jenisbarang 	 : String,
    created_at       : String
});
module.exports = mongoose.model('barangkeluar', barangkeluarSchema);
