package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class TrangChuActivity extends AppCompatActivity {

    Button btnThemHe, btnListHe, btnThoat;
    Button btnThemPokemon, btnHienThiPokemon;
    Button btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        addControls();
        addEvents();
    }
    private void addEvents() {

        //chuyen sang trang them he
        btnThemHe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TrangChuActivity.this, ThemTypeActivity.class ));
            }
        });

        //chuyen sang trang them pokemon
        btnThemPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrangChuActivity.this,ThemPokemonActivity.class ));
            }
        });

        btnHienThiPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrangChuActivity.this,MainActivity.class ));
            }
        });



        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TrangChuActivity.this, DangKyActivity.class));
                //finish();
                //onBackPressed();
            }
        });
        btnListHe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrangChuActivity.this,DSTypeActivity.class ));
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrangChuActivity.this, MapActivity.class));
            }
        });




    }

    private void addControls() {

        btnThemHe = findViewById(R.id.btnThemHepokemon_TrangChu);
        btnThemPokemon = findViewById(R.id.btnThemPokemonTrangChu);
        btnHienThiPokemon = findViewById(R.id.btnHIenThiListPokemonTrangChu);
        btnListHe = findViewById(R.id.btnHienThiListHePokemon);
        btnThoat = findViewById(R.id.btnexit);
        btnMap = findViewById(R.id.btnMap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trangchu,menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnMap:
                startActivity(new Intent(TrangChuActivity.this, MapActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

