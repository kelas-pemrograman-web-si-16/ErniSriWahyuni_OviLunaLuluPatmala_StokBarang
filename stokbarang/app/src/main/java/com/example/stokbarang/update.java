package com.example.stokbarang;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.stokbarang.server.AppController;
import com.example.stokbarang.server.Config_URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class update extends AppCompatActivity {

    @BindView(R.id.kodebarang)
    EditText kodebarang;
    @BindView(R.id.namabarang)
    EditText namabarang;
    @BindView(R.id.jenisbarang)
    EditText jenisbarang;
    @BindView(R.id.harga)
    EditText harga;

    String kodee, namaa, jeniss, hargas;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent a    = getIntent();
        kodee       = a.getStringExtra("kode");
        namaa      = a.getStringExtra("nama");
        jeniss   = a.getStringExtra("jenis");
        hargas      = a.getStringExtra("harga");
        kodebarang.setText(kodee);
        namabarang.setText(namaa);
        jenisbarang.setText(jeniss);
        harga.setText(hargas);
    }

    @OnClick(R.id.submit)
    void submit(){
        final String kodebarangg = kodebarang.getText().toString();
        final String namabarangg= namabarang.getText().toString();
        final String jenisbarangg = jenisbarang.getText().toString();
        final String hargaa     = harga.getText().toString();

        if (kodee.isEmpty()){
            Toast.makeText(getApplicationContext(), "Kode barang tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(namaa.isEmpty()){
            Toast.makeText(getApplicationContext(), "Nama barang tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(jeniss.isEmpty()){
            Toast.makeText(getApplicationContext(), "Jenis barang tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(hargaa.isEmpty()){
            Toast.makeText(getApplicationContext(), "Harga tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else {
            update(kodebarangg, namabarangg, jenisbarangg, hargaa);
        }
    }

    @OnClick(R.id.hapus)
    void hapus(){
        final String kodee = kodebarang.getText().toString();

        if (kodee.isEmpty()){
            Toast.makeText(getApplicationContext(), "Kode barang tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(Html.fromHtml("<font color='#25c5da'><b>Yakin ingin Menghapus Data ini ?</b></font>"))
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            hapus(kodee);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.setCancelable(true);
                }
            })
                    .show();
        }
    }

    public void update(final String kodebarangg, final String namabarangg, final String jenisbarangg, final String hargaa){

        String tag_string_req = "req_register";

        pDialog.setMessage("Loading.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("success");

                    if(status == true){
                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Intent a = new Intent(update.this, list.class);
                        startActivity(a);
                        finish();
                    }else {
                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }

                }catch (JSONException e){
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Rquest", "Login Error : " + error.getMessage());
                error.printStackTrace();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("kodebarang", kodebarangg);
                params.put("namabarang", namabarangg);
                params.put("jenisbarang", jenisbarangg);
                params.put("harga", hargaa);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void hapus(final String kodee){

        String tag_string_req = "req_register";

        pDialog.setMessage("Loading.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.hapus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("success");

                    if(status == true){
                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Intent a = new Intent(update.this, list.class);
                        startActivity(a);
                        finish();
                    }else {
                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }

                }catch (JSONException e){
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Rquest", "Login Error : " + error.getMessage());
                error.printStackTrace();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("kodebarang", kodee);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

