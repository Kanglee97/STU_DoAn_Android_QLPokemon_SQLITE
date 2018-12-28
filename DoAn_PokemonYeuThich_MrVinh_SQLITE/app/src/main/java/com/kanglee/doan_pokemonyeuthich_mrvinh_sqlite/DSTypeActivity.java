package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Type;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DataList;

public class DSTypeActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<Type> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dstype);

        addControls();
        addEvents();
    }

    private void addEvents() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DSTypeActivity.this, DSPokemonTheoIDTypeActivity.class);
                intent.putExtra("ID", adapter.getItem(position));
                startActivity(intent);

            }
        });

    }

    private void addControls() {
        listView = findViewById(R.id.lstHienThiDSHe01);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, DataList.typeArrayList);
        listView.setAdapter(adapter);
    }

}
