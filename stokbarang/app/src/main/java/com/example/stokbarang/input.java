package com.example.stokbarang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class input extends AppCompatActivity {

    @BindView(R.id.kodebarang)
    EditText kodebarangg;
    @BindView(R.id.namabarang)
    EditText namabarangg;
    @BindView(R.id.jenisbarang)
    EditText jenisbarangg;
    @BindView(R.id.harga)
    EditText hargaa;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
//        getSupportActionBar().hide();

        ButterKnife.bind(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    @OnClick(R.id.submit)
    void submit(){
        final String kodebarang = kodebarangg.getText().toString();
        final String namabarang= namabarangg.getText().toString();
        final String jenisbarang = jenisbarangg.getText().toString();
        final String harga     = hargaa.getText().toString();

        if (kodebarang.isEmpty()){
            Toast.makeText(getApplicationContext(), "Kode barang tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(namabarang.isEmpty()){
            Toast.makeText(getApplicationContext(), "Nama barang tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(jenisbarang.isEmpty()){
            Toast.makeText(getApplicationContext(), "Jenis barang tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(harga.isEmpty()){
            Toast.makeText(getApplicationContext(), "Harga tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else {
            input(kodebarang, namabarang, jenisbarang, harga);
        }
    }

    public void input(final String kodebarang, final String namabarang, final String jenisbarang, final String harga){

        String tag_string_req = "req_register";

        pDialog.setMessage("Loading.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.input, new Response.Listener<String>() {
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
                        Intent a = new Intent(input.this, list.class);
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
                params.put("kodebarang", kodebarang);
                params.put("namabarang", namabarang);
                params.put("jenisbarang", jenisbarang);
                params.put("harga", harga);
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

    @Override
    public void onBackPressed() {
        Intent a = new Intent(input.this, list.class);
        startActivity(a);
        finish();
    }
}


