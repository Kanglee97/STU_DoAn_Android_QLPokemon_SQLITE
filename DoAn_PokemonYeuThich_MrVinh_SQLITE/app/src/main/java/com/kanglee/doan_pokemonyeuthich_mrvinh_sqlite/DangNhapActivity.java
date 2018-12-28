package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Pokemon;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Type;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DataList;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DatabaseManager;

import java.util.ArrayList;

public class DangNhapActivity extends AppCompatActivity {


    EditText txtNhapTenDN, txtNhapPass;
    Button btnDangNhap, btnDangKy;
    SharedPreferences sharedPreferences;
    String TaiKhoang, MatKhau;

    final String DATABASE_NAME = "qlpokemondoan.sqlite";
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        TaiKhoang = sharedPreferences.getString("TaiKhoang", "");
        MatKhau = sharedPreferences.getString("MatKhau", "");

        addControls();
        addEvents();

    }

    private void addEvents() {

        //Chuyen sang trang dang ki
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhapActivity.this, DangKyActivity.class));
            }
        });

        //Dang nhap
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String TK = txtNhapTenDN.getText().toString();
                String MK = txtNhapPass.getText().toString();
                if (TK.equals(TaiKhoang) && MK.equals(MatKhau)) {
                    Toast.makeText(DangNhapActivity.this, DangNhapActivity.this.getText(R.string.messSuccess), Toast.LENGTH_SHORT).show();
                    getAllType();
                    getAllPokemon();
                    createCode();
                    startActivity(new Intent(DangNhapActivity.this, TrangChuActivity.class));
                } else {
                    Toast.makeText(DangNhapActivity.this, DangNhapActivity.this.getText(R.string.messFailed), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void addControls() {

        txtNhapTenDN = findViewById(R.id.txtNhapUser);
        txtNhapPass = findViewById(R.id.txtNhappassword);
        btnDangNhap = findViewById(R.id.btnLogin);
        btnDangKy = findViewById(R.id.btnDangKy);
    }
    private void createCode(){
        database = DatabaseManager.CreateDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM "+ DatabaseManager.TABLE_ID, null);
        cursor.moveToFirst();
        int idPokemon = cursor.getInt(0);
        int idType = cursor.getInt(1);
        cursor.close();

        DataList.idPokemon.setKey("PK");
        DataList.idPokemon.setNextId(idPokemon);


        DataList.idType.setKey("TY");
        DataList.idType.setNextId(idType);
    }
    private void getAllPokemon(){
        DataList.pokemonArrayList = new ArrayList<Pokemon>();

        database = DatabaseManager.CreateDatabase(this, DATABASE_NAME);

        Cursor cursor = database.rawQuery("SELECT * FROM "+DatabaseManager.TABLE_POKEMON, null);
        while (cursor.moveToNext()){

            String id = cursor.getString(0);
            String ten = cursor.getString(1);
            Double cN = cursor.getDouble(2);
            Double cC = cursor.getDouble(3);
            byte[] anh = cursor.getBlob(4);
            String idType = cursor.getString(5);

            String sqlType = "SELECT * FROM type WHERE idType = '" + idType + "'";
            Cursor cursor1 = database.rawQuery(sqlType, null);
            //Cursor cursor1 = database.rawQuery("SELECT * FROM type Where idType = '" + idType + "'" , null);
            cursor1.moveToFirst();
            String idType2 = cursor1.getString(0);
            String tentype = cursor1.getString(1);
            Type type = new Type(idType2, tentype);
            cursor1.close();

            Pokemon p = new Pokemon(id,ten,cN,cC,anh,type);
            DataList.pokemonArrayList.add(p);
        }
        cursor.close();
    }


    private void getAllType(){

        DataList.typeArrayList = new ArrayList<Type>();
        database = DatabaseManager.CreateDatabase(this,DATABASE_NAME);


        Cursor cursor = database.rawQuery("SELECT * FROM "+ DatabaseManager.TABLE_TYPE, null);
        while (cursor.moveToNext()) {
            String idType = cursor.getString(0);
            String tenType = cursor.getString(1);
            Type type= new Type(idType, tenType);
            DataList.typeArrayList.add(type);
        }
        cursor.close();
    }


}
