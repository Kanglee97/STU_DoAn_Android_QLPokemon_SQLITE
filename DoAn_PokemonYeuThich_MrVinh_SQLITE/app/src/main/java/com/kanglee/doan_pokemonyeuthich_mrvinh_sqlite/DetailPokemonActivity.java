package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.text.MessageFormat;
import java.util.ArrayList;

import static com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DatabaseManager.TABLE_POKEMON;

public class DetailPokemonActivity extends AppCompatActivity {

    EditText txtTen, txtCanNang, txtChieuCao, txtId;
    Spinner spinnerDetail;
    ImageView imageView;
    Button btnThem, btnXoa, btnSua;

    Pokemon pokemon;
    private ArrayAdapter<Type> adapter;
    int RESQUEST_CHOOSE_PHOTO = 176;

    SQLiteDatabase database;
    final String DATABASE_NAME = "qlpokemondoan.sqlite";
     int pos = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pokemon);
        addControls();
        addEvents();
        getData();




    }

    private void addEvents() {
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aler = new AlertDialog.Builder(DetailPokemonActivity.this);
                aler.setIcon(android.R.drawable.ic_delete);
                aler.setTitle(DetailPokemonActivity.this.getText(R.string.title_delete));
                aler.setMessage(DetailPokemonActivity.this.getText(R.string.messErrorSpace));
                aler.setPositiveButton(DetailPokemonActivity.this.getText(R.string.title_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePokemon(pokemon.getiD_Pokemon());
                    }
                });
                aler.setNegativeButton(DetailPokemonActivity.this.getText(R.string.title_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog2 = aler.create();
                dialog2.show();

            }

        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aler = new AlertDialog.Builder(DetailPokemonActivity.this);
                aler.setIcon(android.R.drawable.ic_menu_edit);
                aler.setTitle(DetailPokemonActivity.this.getText(R.string.title_update));
                aler.setMessage(DetailPokemonActivity.this.getText(R.string.title_mee_edit));
                aler.setPositiveButton(DetailPokemonActivity.this.getText(R.string.title_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editPokemon();
                    }
                });
                aler.setNegativeButton(DetailPokemonActivity.this.getText(R.string.title_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = aler.create();
                dialog.show();

            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailPokemonActivity.this,ThemPokemonActivity.class));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESQUEST_CHOOSE_PHOTO);
            }
        });
    }




    private void spinner() {
        adapter = new ArrayAdapter<Type>(DetailPokemonActivity.this,android.R.layout.simple_list_item_1, DataList.typeArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerDetail.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == RESQUEST_CHOOSE_PHOTO){
                try {
                    Uri imageUri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }}}
    }




    private void getData() {

        if (getIntent().getExtras() != null) {
            pokemon = (Pokemon) getIntent().getSerializableExtra("EDIT");

            String sql = "SELECT * FROM pokemon WHERE idPokemon = ?";
            database = DatabaseManager.CreateDatabase(this, DATABASE_NAME);
            Cursor cursor = database.rawQuery(sql, new String[]{pokemon.getiD_Pokemon() + ""});

            cursor.moveToFirst();
            String id = cursor.getString(0);
            String ten = cursor.getString(1);
            Double cN = cursor.getDouble(2);
            Double cC = cursor.getDouble(3);
            byte[] anh = cursor.getBlob(4);
            String idType = cursor.getString(5);
            cursor.close();

            //lay du lieu Type
            String sqlType = "SELECT * FROM type WHERE idType = ?";
            Cursor cursor1 = database.rawQuery(sqlType, new String[] {idType + ""});
            //Cursor cursor1 = database.rawQuery("SELECT * FROM type Where idType = '" + idType + "'" , null);
            cursor1.moveToFirst();
            String idType2 = cursor1.getString(0);
            String tentype = cursor1.getString(1);

            final Type type = new Type(idType2, tentype);
            cursor1.close();


            //Bitmap image
            Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);
            //dua len giao dien
            imageView.setImageBitmap(bitmap);
            txtId.setText(id);
            txtTen.setText(ten);
            txtCanNang.setText(String.valueOf(cN));
            txtChieuCao.setText( String.valueOf(cC));
            spinnerDetail.setSelection(adapter.getPosition(type));


           // ArrayAdapter<Type> arrayAdapter = new ArrayAdapter<Type>(this,android.R.layout.simple_list_item_1,DataList.typeArrayList);
            //spinnerDetail.setSelection(DataList.typeArrayList.);
           // arrayAdapter.notifyDataSetChanged();




            //spinner();

            /*spinnerDetail.post(new Runnable() {
                @Override
                public void run() {
                    spinnerDetail.setSelection(pos);
                }
            });*/


        }
    }
    private void addControls() {
        txtId = findViewById(R.id.txtHienThiIdChiTietActivity);
        txtTen = findViewById(R.id.txtHienThiTenChiTietActivity);
        txtCanNang = findViewById(R.id.txtHienThiCanNangChiTietActivity);
        txtChieuCao = findViewById(R.id.txtHienThiChieuCaoChiTietActivity);
        spinnerDetail = findViewById(R.id.spinnerChiTietActivity01);
        //spinnerDetail.ob

        btnThem= findViewById(R.id.btnThemChiTietActivity);
        btnSua = findViewById(R.id.btnCapNhatChiTietActivity);
        btnXoa = findViewById(R.id.btnXoaChiTietActivity);
        imageView = findViewById(R.id.imgHienThiAnhChiTietActivity);

        spinner();

        // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource();

        //adapter = new ArrayAdapter<Type>(DetailPokemonActivity.this,android.R.layout.simple_list_item_1, DataList.typeArrayList);
        // adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        //spinnerDetail.setAdapter(adapter);

    }

    private void deletePokemon(String idPokemon) {
         database = DatabaseManager.CreateDatabase(this,DATABASE_NAME);
        database.delete(DatabaseManager.TABLE_POKEMON,"idPokemon = ?", new String[] {idPokemon + ""});

        Intent intent = new Intent(DetailPokemonActivity.this, MainActivity.class);
        intent.putExtra("DEL", pokemon.getiD_Pokemon());
        setResult(300, intent);
        startActivity(intent);
        Toast.makeText(DetailPokemonActivity.this,DetailPokemonActivity.this.getText(R.string.messSuccess),Toast.LENGTH_SHORT).show();
        //finish();
    }

    private void editPokemon() {

        String id = pokemon.getiD_Pokemon();
        String ten = txtTen.getText().toString();
        Double cN = Double.parseDouble(txtCanNang.getText().toString());
        Double cC = Double.parseDouble(txtChieuCao.getText().toString());
        //chuyen du lieu imageview sang byte[]
        byte[] anh = convertImageViewToByte(imageView);

        Type type = (Type) spinnerDetail.getSelectedItem();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseManager.ID_POKEMON, id);
        contentValues.put(DatabaseManager.NAME_POKEMON, ten);
        contentValues.put(DatabaseManager.CAN_NANG, cN);
        contentValues.put(DatabaseManager.CHIEU_CAO, cC);
        contentValues.put(DatabaseManager.ID_HE, type.getTypeId());
        contentValues.put(DatabaseManager.ANH_POKEMON,anh);


        database = DatabaseManager.CreateDatabase(this, DATABASE_NAME);
        database.update(DatabaseManager.TABLE_POKEMON, contentValues, "idPokemon = ?", new String[]{id + ""});

        Pokemon pokemon = new Pokemon(id, ten, cN, cC, anh, type);



        Intent intent = new Intent(DetailPokemonActivity.this, MainActivity.class);
        intent.putExtra("EDIT", pokemon);
        setResult(200, intent);
        startActivity(intent);
        Toast.makeText(DetailPokemonActivity.this,DetailPokemonActivity.this.getText(R.string.messSuccess),Toast.LENGTH_SHORT).show();
        //finish();


    }

    public byte[] convertImageViewToByte(ImageView img) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }




}
