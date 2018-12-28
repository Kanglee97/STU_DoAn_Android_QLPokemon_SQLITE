package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Pokemon;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Type;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DataList;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

import static com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DatabaseManager.TABLE_POKEMON;

public class ThemTypeActivity extends AppCompatActivity {


    EditText txtNhapTenHePokemon;
    EditText txtHienThiIDHePokemon;
    Button btnThemHePokemon;
    Button btnLamSachPokemon;
    Button btnCapNhatHePokemon;
    Button btnXoaHePokemon;
    ListView lstHienThiHePokemon;
    ArrayAdapter<Type> adapter;
    ArrayList<Pokemon> arrayList;
    Pokemon pokemon;
    int pos = -1;
    private String idType;
    Type type0;

    final String DATABASE_NAME = "qlpokemondoan.sqlite";
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_type);

        addControls();
        addEvents();


    }

    @Override
    public boolean onSupportNavigateUp(){
        DataList.idType.moveBack();
        finish();
        return true;
    }

    private void addEvents() {

        lstHienThiHePokemon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                Type type = DataList.typeArrayList.get(position);
                type0 = type;
                txtHienThiIDHePokemon.setText(type.getTypeId());
                txtNhapTenHePokemon.setText(type.getTypeTen());
                btnThemHePokemon.setEnabled(false);
                btnCapNhatHePokemon.setEnabled(true);
            }
        });
        btnXoaHePokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aler = new AlertDialog.Builder(ThemTypeActivity.this);
                aler.setIcon(android.R.drawable.ic_delete);
                aler.setTitle(ThemTypeActivity.this.getText(R.string.title_delete));
                aler.setMessage(ThemTypeActivity.this.getText(R.string.title_mess_delete));
                aler.setPositiveButton(ThemTypeActivity.this.getText(R.string.title_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       delType(adapter.getItem(pos));

                    }
                });
                aler.setNegativeButton(ThemTypeActivity.this.getText(R.string.title_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = aler.create();
                dialog.show();
            }
        });

        btnThemHePokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idType;
                String tenType = txtNhapTenHePokemon.getText().toString();

                if (tenType.trim().isEmpty()){
                    Toast.makeText(ThemTypeActivity.this, ThemTypeActivity.this.getText(R.string.messErrorSpace), Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if (tenType.trim().length() > 0) {
                    database = DatabaseManager.CreateDatabase(ThemTypeActivity.this, DATABASE_NAME);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseManager.ID_HE,id);
                    contentValues.put(DatabaseManager.NAME_HE,tenType);
                    database.insert(DatabaseManager.TABLE_TYPE, null, contentValues); }

                    Integer idTy = DataList.idType.getNextId();
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put(DatabaseManager.ID_HE , idTy);
                    long returnValue = database.update(DatabaseManager.TABLE_ID, contentValues1, null, null);
                    if (returnValue < 0){
                        Toast.makeText(ThemTypeActivity.this,ThemTypeActivity.this.getText(R.string.messErrorSpace), Toast.LENGTH_SHORT).show();
                    }
                    Type typeNew = new Type(id, tenType);
                    DataList.typeArrayList.add(typeNew);
                    adapter.notifyDataSetChanged();
                Toast.makeText(ThemTypeActivity.this, ThemTypeActivity.this.getText(R.string.messAddSuccess), Toast.LENGTH_SHORT).show();
            }
        });

        btnCapNhatHePokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder aler = new AlertDialog.Builder(ThemTypeActivity.this);
                    aler.setIcon(android.R.drawable.ic_menu_edit);
                    aler.setTitle(ThemTypeActivity.this.getText(R.string.title_update));
                    aler.setMessage(ThemTypeActivity.this.getText(R.string.title_mee_edit));
                    aler.setPositiveButton(ThemTypeActivity.this.getText(R.string.title_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for(Type item : DataList.typeArrayList){
                                if (item.getTypeId().equals(type0.getTypeId())){
                                    item.setTypeTen(txtNhapTenHePokemon.getText().toString().trim());

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(DatabaseManager.ID_HE,item.getTypeId());
                                    contentValues.put(DatabaseManager.NAME_HE,txtNhapTenHePokemon.getText().toString().trim());
                                    database = DatabaseManager.CreateDatabase(ThemTypeActivity.this, DATABASE_NAME);
                                    database.update(DatabaseManager.TABLE_TYPE, contentValues, "idType = ?", new String[] {item.getTypeId() + ""} );

                                    Toast.makeText(ThemTypeActivity.this, String.valueOf(ThemTypeActivity.this.getString(R.string.messSuccess)), Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            adapter.notifyDataSetChanged();

                            Intent intent = new Intent(ThemTypeActivity.this, ThemTypeActivity.class);
                            intent.putExtra("EDITTYPE", type0);
                            setResult(500, intent);
                            finish();


                        }
                    });
                    aler.setNegativeButton(ThemTypeActivity.this.getText(R.string.title_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog4 = aler.create();
                    dialog4.show();
                }

        });
        resetHe();





    }
    private void addControls() {

        txtNhapTenHePokemon     = findViewById(R.id.txtNhapTenHePokemon);
        btnThemHePokemon        = findViewById(R.id.btnThemHePokemon);
        btnCapNhatHePokemon     = findViewById(R.id.btnCapNhapHePokemon);
        lstHienThiHePokemon     = findViewById(R.id.lstHienThiDSHe);
        txtHienThiIDHePokemon   = findViewById(R.id.txtIDHePokemon);
        btnLamSachPokemon       = findViewById(R.id.btnLamMoiHe);
        btnXoaHePokemon         = findViewById(R.id.btnXoa);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, DataList.typeArrayList);
        lstHienThiHePokemon.setAdapter(adapter);


        this.idType = DataList.idType.nextId();
        txtHienThiIDHePokemon.setText(this.idType);

    }

    private void resetHe(){

        btnLamSachPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHienThiIDHePokemon.setText("");
                txtNhapTenHePokemon.setText("");
                btnThemHePokemon.setEnabled(true);
                btnCapNhatHePokemon.setEnabled(false);

            }
        });


    }
    void delType(Type type){
        int viTri = -1;
        for (int i = 0 ; i< DataList.typeArrayList.size();i++){
            if (DataList.typeArrayList.get(i).getTypeId().equals(type.getTypeId())) {
                viTri = i;
                break;
            }
        }
        if (viTri < 0) {
            Toast.makeText(ThemTypeActivity.this, ThemTypeActivity.this.getText(R.string.messErrorPostion), Toast.LENGTH_SHORT).show();
        } else {
            //kiem tra
            arrayList = new ArrayList<Pokemon>();
            database = DatabaseManager.CreateDatabase(this,DATABASE_NAME);
            String sql = "SELECT * FROM pokemon WHERE idType= '" + type.getTypeId() + "'";
            Cursor cursor = database.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String ten = cursor.getString(1);
                Double cN = cursor.getDouble(2);
                Double cC = cursor.getDouble(3);
                byte[] anh = cursor.getBlob(4);
                String idType = cursor.getString(5);

                Cursor cursor1 = database.rawQuery("SELECT * FROM type Where idType = '" + idType + "'", null);
                cursor1.moveToFirst();
                String idType2 = cursor1.getString(0);
                String tentype = cursor1.getString(1);
                Type type1 = new Type(idType2, tentype);
                cursor1.close();
                Pokemon pokemon = new Pokemon(id, ten, cN, cC, anh, type1);
                arrayList.add(pokemon);
            }
            cursor.close();

            if (arrayList.size() != 0){
                Toast.makeText(this,ThemTypeActivity.this.getText(R.string.messError),Toast.LENGTH_SHORT).show();
            }
            else {
                // if ()
                DataList.idType.moveBack();
                //remove in array
                DataList.typeArrayList.remove(DataList.typeArrayList.get(viTri));
                adapter.notifyDataSetChanged();

                //remove in sqlite
                database = DatabaseManager.CreateDatabase(ThemTypeActivity.this,DATABASE_NAME);
                database.delete(DatabaseManager.TABLE_TYPE,"idType = ?", new String[] {type.getTypeId() + ""});

                //reomve pokemon idType equal
                database.delete(DatabaseManager.TABLE_POKEMON,"idType = ?", new String[] {type.getTypeId() + ""});

                Intent intent = new Intent(ThemTypeActivity.this, MainActivity.class);
                intent.putExtra("DELCATEGORY", type);
                setResult(400, intent);
                Toast.makeText(this,ThemTypeActivity.this.getText(R.string.messSuccess),Toast.LENGTH_SHORT).show();

                //finish();
            }




            }



    }




}
