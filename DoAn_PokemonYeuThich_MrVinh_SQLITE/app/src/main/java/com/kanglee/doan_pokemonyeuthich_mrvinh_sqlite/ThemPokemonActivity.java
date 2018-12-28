package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Pokemon;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Type;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DataList;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DatabaseManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ThemPokemonActivity extends AppCompatActivity {

    final String DATABASE_NAME = "qlpokemondoan.sqlite";

    EditText txtNhapTenPokemon;
    EditText txtNhapCanNang;
    EditText txtNhapChieuCao;
    EditText txtHTIdPokemon;

    Button btnThemPokemon;
    Button btnThoatPokemon;

    Spinner spinnerType;

    ImageView imgAnh;
    ImageButton btnFolder;

    int posType;

    int REQUEST_CODE_FOLDER = 456;

    private String idPokemon;

    ArrayAdapter<Type> adapter;

    private Boolean checkChooseImage = false;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_pokemon);

        addControls();
        addEvents();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgAnh.setImageBitmap(bitmap);
                checkChooseImage = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addEvents() {
        btnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//chi lay hinh anh
                // Enable if permission granted
                if (ContextCompat.checkSelfPermission(ThemPokemonActivity.this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                    btnFolder.setEnabled(true);
                }
// Else ask for permission
                else {
                    ActivityCompat.requestPermissions(ThemPokemonActivity.this, new String[]
                            { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                }
                startActivityForResult(intent,REQUEST_CODE_FOLDER);
            }
        });

        btnThemPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themPokemon();
            }
        });
        btnThoatPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHTIdPokemon.setText("");
                txtNhapCanNang.setText("");
                txtNhapChieuCao.setText("");
                txtNhapTenPokemon.setText("");
            }
        });


    }

    private void addControls() {

        //anh xa
        spinnerType = findViewById(R.id.spnHe);
        btnThemPokemon = findViewById(R.id.btnThemPokemonAddPokemon);
        btnThoatPokemon = findViewById(R.id.btnHuyThemAddPokemon);
        txtNhapTenPokemon = findViewById(R.id.txtNhapTenPokemon);
        txtNhapCanNang = findViewById(R.id.txtNhapCanNangPokemon);
        txtNhapChieuCao = findViewById(R.id.txtNhapChieuCaoPokemon);
        txtHTIdPokemon = findViewById(R.id.txtHTIdPokemonAddPokemon);
        btnFolder = findViewById(R.id.btnChonFolder);
        imgAnh = findViewById(R.id.imgThemAnhPokemon);
        createIdPokemon();
        spinner();



    }
    private void createIdPokemon(){
        this.idPokemon = DataList.idPokemon.nextId();
        txtHTIdPokemon.setText(this.idPokemon);

    }
    private void spinner(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, DataList.typeArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerType.setAdapter(adapter);

    }


    private void themPokemon(){
        String id = this.idPokemon;
        String ten = txtNhapTenPokemon.getText().toString();
        Double canNang = Double.parseDouble(txtNhapCanNang.getText().toString());
        Double chieuCao = Double.parseDouble(txtNhapChieuCao.getText().toString());
        Type type = (Type) spinnerType.getSelectedItem();
        //chuyen du lieu imageview sang byte[]
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgAnh.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] hinhAnh = byteArrayOutputStream.toByteArray();

        if (ten.trim().length() > 0 && txtNhapCanNang.getText().toString().length() >0 && txtNhapChieuCao.getText().toString().length() > 0
                && spinnerType.getSelectedItem() != null) {
            if (checkChooseImage == false){
                Toast.makeText(ThemPokemonActivity.this,ThemPokemonActivity.this.getText(R.string.ChooseImage), Toast.LENGTH_SHORT).show();
            }else {

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseManager.ID_POKEMON, id);
                contentValues.put(DatabaseManager.NAME_POKEMON,ten);
                contentValues.put(DatabaseManager.CAN_NANG, canNang);
                contentValues.put(DatabaseManager.CHIEU_CAO,chieuCao);
                contentValues.put(DatabaseManager.ANH_POKEMON, hinhAnh);
                contentValues.put(DatabaseManager.ID_HE,type.getTypeId());

                database = DatabaseManager.CreateDatabase(ThemPokemonActivity.this,DATABASE_NAME);
                database.insert(DatabaseManager.TABLE_POKEMON, null, contentValues);

                //tang id len
                Integer idTang = DataList.idPokemon.getNextId();
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put(DatabaseManager.ID_POKEMON, idTang);
                long returnValue = database.update(DatabaseManager.TABLE_ID, contentValues1, null, null);
                if (returnValue <0){
                    Toast.makeText(ThemPokemonActivity.this, "that bai", Toast.LENGTH_SHORT).show();
                }

                Pokemon pokemon = new Pokemon(id, ten, canNang, chieuCao, hinhAnh, type);
                Intent intent = new Intent(ThemPokemonActivity.this, TrangChuActivity.class);
                intent.putExtra("ADD", pokemon);
                setResult(100, intent);
                Toast.makeText(ThemPokemonActivity.this,ThemPokemonActivity.this.getText(R.string.messAddSuccess), Toast.LENGTH_SHORT).show();
                finish();

            }

        }
        else {
            Toast.makeText(ThemPokemonActivity.this, String.valueOf(ThemPokemonActivity.this.getString(R.string.messErrorSpace)), Toast.LENGTH_SHORT).show();
        }





    }

}
