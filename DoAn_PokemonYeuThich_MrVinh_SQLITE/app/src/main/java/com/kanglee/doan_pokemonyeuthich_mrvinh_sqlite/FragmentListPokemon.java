package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Pokemon;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Type;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.adapter.PokemonAdapter;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DataList;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

import static com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DatabaseManager.TABLE_POKEMON;

public class FragmentListPokemon extends Fragment {
    PokemonAdapter adapter;
    ListView listView;
    ArrayAdapter<Type> adapterType;
    View view;
    SQLiteDatabase database;
    final String DATABASE_NAME = "qlpokemondoan.sqlite";
    FloatingActionButton btnFlg;



    int REQUEST_CODE_EDIT = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DataList.pokemonArrayList = new ArrayList<Pokemon>();

        view = inflater.inflate(R.layout.fragment_list, container, true);

        addControls();
        addEvents();
        return view;
    }


    private void addEvents() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Configuration configuration = getResources().getConfiguration();
                FragmentDetailPokemon fragmentDetailPokemon = (FragmentDetailPokemon) getFragmentManager().findFragmentById(R.id.fragInforPokemon);
                if (fragmentDetailPokemon != null && configuration.orientation == configuration.ORIENTATION_LANDSCAPE ){
                    fragmentDetailPokemon.getData((Pokemon)adapter.getItem(position));

                }else {
                    Intent intent = new Intent(getActivity(), DetailPokemonActivity.class);
                    intent.putExtra("EDIT",(Pokemon) adapter.getItem(position));
                    startActivityForResult(intent,20);
                }

            }
        });
        btnFlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),TrangChuActivity.class));
            }
        });

    }

    private void addControls() {
        getAllPokemon();
        listView = view.findViewById(R.id.lstFrgPokemon);
        btnFlg = view.findViewById(R.id.btnFlg);
        adapter = new PokemonAdapter(getActivity(), R.layout.dong_ds_pokemon, DataList.pokemonArrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private ArrayList<Pokemon> getAllPokemon() {

        DataList.pokemonArrayList = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_POKEMON;

        //khoi tao doi tuong sqlite
        //mo va chinh sua db
        database = DatabaseManager.CreateDatabase(getActivity(),DATABASE_NAME);
        Cursor cursor = database.rawQuery(sql, null);//hung ket qua tra ve
        while (cursor.moveToNext()) {


            String id = cursor.getString(0);
            String ten = cursor.getString(1);
            Double cN = cursor.getDouble(2);
            Double cC = cursor.getDouble(3);
            byte[] anh = cursor.getBlob(4);
            String idType = cursor.getString(5);

            Cursor cursor1 = database.rawQuery("SELECT * FROM type Where idType = '" + idType + "'" , null);
            cursor1.moveToFirst();
            String idType2 = cursor1.getString(0);
            String tentype = cursor1.getString(1);
            Type type = new Type(idType2, tentype);
            cursor1.close();

            Pokemon pokemon = new Pokemon(id, ten, cN, cC, anh, type);
            DataList.pokemonArrayList.add(pokemon);

        }
        cursor.close();

        return DataList.pokemonArrayList;

    }

    public void editPokemon(Pokemon pokemon){
        for (Pokemon item: DataList.pokemonArrayList) {
            if (item.getiD_Pokemon().equals(pokemon.getiD_Pokemon())) {
                item.setTen_Pokemon(pokemon.getTen_Pokemon());
                item.setCan_Nang(pokemon.getCan_Nang());
                item.setChieu_Cao(pokemon.getChieu_Cao());
                item.setAnh_Pokemon(pokemon.getAnh_Pokemon());
                item.setiDType(pokemon.getiDType());
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }
    public void delPokemon(String id) {
        int pos = -1;
        for (int i = 0; i< DataList.pokemonArrayList.size(); i++) {
            if (DataList.pokemonArrayList.get(i).getiD_Pokemon().equals(id)) {
                pos = i;
                break;
            }
        }
        if (pos< 0){
            Toast.makeText(getActivity(),"Khong xac dinh duoc vi tri", Toast.LENGTH_LONG).show();
        }else {
            DataList.pokemonArrayList.remove(DataList.pokemonArrayList.get(pos));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 10:
                if (data!=null) {
                    Pokemon pokemon = (Pokemon) data.getSerializableExtra("ADD");
                    if (resultCode == 100) {
                        adapter.getList().add(adapter.getCount(), pokemon);
                        adapter.notifyDataSetChanged();

                    }
                }
                break;
            case 20:
                if (data != null){
                    Pokemon pokemon = (Pokemon) data.getSerializableExtra("EDIT");
                    if (resultCode == 200) {
                        for (Pokemon item : DataList.pokemonArrayList) {
                            if (item.getiD_Pokemon().equals(pokemon.getiD_Pokemon())) {
                                item.setTen_Pokemon(pokemon.getTen_Pokemon());
                                item.setCan_Nang(pokemon.getCan_Nang());
                                item.setChieu_Cao(pokemon.getChieu_Cao());
                                item.setAnh_Pokemon(pokemon.getAnh_Pokemon());
                                item.setiDType(pokemon.getiDType());
                                break;
                            }

                        }
                        adapter.notifyDataSetChanged();
                        break;
                    }else if (resultCode == 300){
                        String iD = data.getExtras().getString("DEL");
                        int pos = -1;
                        for (int i = 0;i<DataList.pokemonArrayList.size(); i++){
                            if (DataList.pokemonArrayList.get(i).equals(iD)){
                                pos = i;
                                break;
                            }
                        }
                        if (pos < 0){
                            Toast.makeText(getActivity(),"Khong xac dinh duoc vi tri", Toast.LENGTH_LONG).show();
                        }
                        else {
                            DataList.pokemonArrayList.remove(DataList.pokemonArrayList.get(pos));
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(),"xoa thanh cong", Toast.LENGTH_LONG).show();

                        }
                    }
                }
                break;
            case 30:
                if (resultCode ==400 ){
                    Type type = (Type) data.getSerializableExtra("DELTYPE");
                    int pos = -1;
                    for (int i = 0; i<DataList.pokemonArrayList.size();i++){
                        if (DataList.pokemonArrayList.get(i).getiDType().equals(type)){
                            pos = i;
                            DataList.pokemonArrayList.remove(DataList.pokemonArrayList.get(i));
                            adapter.notifyDataSetChanged();
                        }
                    }
                    if (pos < 0){
                        Toast.makeText(getActivity(),"Khong xac dinh duoc vi tri", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getActivity(),"thanh cong", Toast.LENGTH_LONG).show();
                    }

                } else if (resultCode == 500) {
                    Type type = (Type) data.getSerializableExtra("EDITTYPE");

                    for (Pokemon item: DataList.pokemonArrayList){
                        if (item.getiDType().getTypeId().equals(type.getTypeId())){
                            item.setiDType(type);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                }
                break;
        }
    }
}


