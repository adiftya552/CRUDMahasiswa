package com.example.mahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class read extends AppCompatActivity implements ListView.OnItemClickListener {

    Button back;

    private ListView listView;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        back = (Button) findViewById(R.id.btn_kembali);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iBack = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(iBack);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();
    }

    private void showMahasiswa() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(konfigurasi.TAG_ID);
                String nim = jo.getString(konfigurasi.TAG_NIM);
                String nama = jo.getString(konfigurasi.TAG_NAMA);
                String jurusan = jo.getString(konfigurasi.TAG_JURUSAN);
                String email = jo.getString(konfigurasi.TAG_EMAIL);

                HashMap<String, String> mahasiswa = new HashMap<>();

                mahasiswa.put(konfigurasi.TAG_ID, id);
                mahasiswa.put(konfigurasi.TAG_NIM, nim);
                mahasiswa.put(konfigurasi.TAG_NAMA, nama);
                mahasiswa.put(konfigurasi.TAG_JURUSAN, jurusan);
                mahasiswa.put(konfigurasi.TAG_EMAIL, email);
                list.add(mahasiswa);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(read.this, list, R.layout.list_item, new String[]{
                konfigurasi.TAG_NAMA, konfigurasi.TAG_NIM,  konfigurasi.TAG_JURUSAN, konfigurasi.TAG_EMAIL}, new int[]{R.id.nama, R.id.nim, R.id.jurusan, R.id.email});
        listView.setAdapter(adapter);
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(read.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showMahasiswa();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, select.class);
        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        String mhsId = map.get(konfigurasi.TAG_ID).toString();
        intent.putExtra(konfigurasi.MHS_ID, mhsId);
        startActivity(intent);

    }
}
