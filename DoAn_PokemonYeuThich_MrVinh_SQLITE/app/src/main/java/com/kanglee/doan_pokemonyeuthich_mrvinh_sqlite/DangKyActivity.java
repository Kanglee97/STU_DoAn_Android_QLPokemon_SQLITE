package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DangKyActivity extends AppCompatActivity {
    EditText txtDKTen, txtDKPass, txtNhapLaiMK;
    Button btnDKTaiKhoang;
    String ten,mk, nhaplaimk;
    SharedPreferences sharedPreferences;
    String TaiKhoang, MatKhau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        TaiKhoang = sharedPreferences.getString("TaiKhoan", "");
        MatKhau = sharedPreferences.getString("MatKhau", "");


        addControls();
        addEvents();
    }

    private void addEvents() {

        btnDKTaiKhoang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ten = txtDKTen.getText().toString().trim();
                mk = txtDKPass.getText().toString().trim();
                nhaplaimk = txtNhapLaiMK.getText().toString().trim();

                if (ten.length()> 0 && mk.length() >0){

                    if (!nhaplaimk.equals(mk)) {
                        Toast.makeText(getApplicationContext(),  DangKyActivity.this.getText(R.string.messpassWrong), Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("TaiKhoang", ten);
                        editor.putString("MatKhau", mk);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), DangKyActivity.this.getText(R.string.messSuccess), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
                    }

                }else {
                    Toast.makeText(getApplicationContext(),  DangKyActivity.this.getText(R.string.messFailed), Toast.LENGTH_SHORT).show();
                }



            }
        });


    }

    private void addControls() {

        txtDKTen = findViewById(R.id.txtNhapUserDK);
        txtDKPass = findViewById(R.id.txtNhappasswordDK);
        btnDKTaiKhoang = findViewById(R.id.btnDangKyTaiKhoang);
        txtNhapLaiMK = findViewById(R.id.txtNhaplaipasswordDK);

    }
}
