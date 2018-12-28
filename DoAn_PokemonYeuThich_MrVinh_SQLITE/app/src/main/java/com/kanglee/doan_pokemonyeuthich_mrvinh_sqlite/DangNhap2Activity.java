package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
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

public class DangNhap2Activity extends AppCompatActivity {

    EditText txtUsername, txtPassword;
    Button btnLogin;

    String userstate = "kang";
    //TextView link_signup;
    final String DATABASE_NAME = "qlpokemondoan.sqlite";
    SQLiteDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap2);
        getSupportActionBar().hide();
        addControls();
        addEvents();
    }



    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getSharedPreferences(userstate, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username",txtUsername.getText().toString());
        editor.putString("password",txtPassword.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences(userstate, MODE_PRIVATE);
        String username = preferences.getString("username","");
        String password = preferences.getString("password","");

        txtUsername.setText(username);
        txtPassword.setText(password);
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



    private void addControls() {
        txtUsername =  findViewById(R.id.txtNhapUser);
        txtPassword = findViewById(R.id.txtNhappassword);
        btnLogin    = findViewById(R.id.btnLogin);
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsername.getText().toString().trim().equals("KangLee") && txtPassword.getText().toString().trim().equals("123456")){
                   getAllType();
                   getAllPokemon();
                   createCode();
                    Intent intentLogin = new Intent(DangNhap2Activity.this, MainActivity.class);
                    startActivity(intentLogin);
                }else{
                    //Toast.makeText(LoginActivity.this,LoginActivity.this.getText(R.string.simple_loginError),Toast.LENGTH_LONG).show();
                }
            }
        });

//        link_signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
