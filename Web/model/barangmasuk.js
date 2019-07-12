const mongoose = require('mongoose');
const barangmasukSchema = mongoose.Schema({
    kodebarangg     : {type: String, unique: true},
    namabarangg     : String,
    jenisbarangg	: String,
    created_at      : String
});
module.exports = mongoose.model('barangmasuk', barangmasukSchema);
