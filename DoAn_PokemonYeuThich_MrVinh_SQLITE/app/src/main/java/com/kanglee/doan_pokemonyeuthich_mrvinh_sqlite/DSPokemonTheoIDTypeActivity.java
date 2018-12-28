package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Pokemon;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Type;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.adapter.PokemonAdapter;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DataList;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DatabaseManager;

import java.util.ArrayList;

public class DSPokemonTheoIDTypeActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton btnFlgTheoID;
    PokemonAdapter adapter;

    final String DATABASE_NAME = "qlpokemondoan.sqlite";
    SQLiteDatabase database;
    ArrayList<Pokemon> pokemonArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dspokemon_theo_idtype);

        addControls();
        addEvents();

    }

    private void addEvents() {
        getAllPokemonID();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DSPokemonTheoIDTypeActivity.this, DetailPokemonActivity.class);
                intent.putExtra("EDIT",(Pokemon) adapter.getItem(position));
                startActivityForResult(intent,20);
            }
        });
        btnFlgTheoID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DSPokemonTheoIDTypeActivity.this, TrangChuActivity.class));
            }
        });

    }

    private void addControls() {
        listView = findViewById(R.id.lstHTDSPokemonTheoID);
        btnFlgTheoID = findViewById(R.id.btnFlgDSType);

        pokemonArrayList = new ArrayList<Pokemon>();
        adapter = new PokemonAdapter(this, R.layout.dong_ds_pokemon, pokemonArrayList);
        listView.setAdapter(adapter);

    }

    private ArrayList<Pokemon> getAllPokemonID() {

        Intent intent = getIntent();
        Type type = (Type) intent.getSerializableExtra("ID");
        //pokemonArrayList = new ArrayList<Pokemon>();
        int viTri = -1;
        for (int i = 0; i < DataList.typeArrayList.size(); i++) {
            if (DataList.typeArrayList.get(i).getTypeId().equals(type.getTypeId())) {
                viTri = i;
                break;
            }
        }
        if (viTri < 0) {
            Toast.makeText(DSPokemonTheoIDTypeActivity.this, DSPokemonTheoIDTypeActivity.this.getText(R.string.messErrorPostion), Toast.LENGTH_SHORT).show();
        } else {
            database = DatabaseManager.CreateDatabase(this, DATABASE_NAME);
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
                pokemonArrayList.add(pokemon);
            }
            cursor.close();

        }
        return pokemonArrayList;
    }

}
