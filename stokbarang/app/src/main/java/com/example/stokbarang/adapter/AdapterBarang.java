package com.example.stokbarang.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stokbarang.R;
import com.example.stokbarang.data.DataBarang;

import java.util.List;

public class AdapterBarang extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataBarang> item;

    public AdapterBarang(Activity activity, List<DataBarang> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.content_barang, null);


        TextView kodebarang         = (TextView) convertView.findViewById(R.id.kodebarang);
        TextView namabarang       = (TextView) convertView.findViewById(R.id.namabarang);
        TextView jenisbarang    = (TextView) convertView.findViewById(R.id.jenisbarang);
        TextView hargaa        = (TextView) convertView.findViewById(R.id.harga);

        kodebarang.setText(": "+item.get(position).getKodebarang());
        namabarang.setText(": "+item.get(position).getNamabarang());
        jenisbarang.setText(": "+item.get(position).getJenis_barang());
        hargaa.setText(": "+item.get(position).getHargaa());

        return convertView;
    }
}

