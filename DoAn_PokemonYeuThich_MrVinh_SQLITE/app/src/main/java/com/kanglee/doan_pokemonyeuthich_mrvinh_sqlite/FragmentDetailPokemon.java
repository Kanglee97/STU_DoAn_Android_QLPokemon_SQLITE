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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Pokemon;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Type;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DataList;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data.DatabaseManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


public class FragmentDetailPokemon extends Fragment {

    View view;

    int RESQUEST_CHOOSE_PHOTO = 113;
    EditText txtTen, txtCanNang, txtChieuCao, txtId, txthe;
    Spinner spinnerDetail, spinn;
    ImageView imageView;
    Button btnThem, btnXoa, btnSua;
    ArrayAdapter<Type> adapter;

    SQLiteDatabase database;
    Pokemon pokemon;
    final String DATABASE_NAME = "qlpokemondoan.sqlite";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail, container, true);

        addControls();
        addEvents();
        if (DataList.pokemonArrayList.size() != 0) {
            getData(DataList.pokemonArrayList.get(0));
        } else {
            imageView.setImageResource(R.drawable.pictures_folder);
            txtId.setText("");
            txtTen.setText("");
            txtCanNang.setText("");
            txtChieuCao.setText("");

        }

        return view;
    }

    private void addEvents() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESQUEST_CHOOSE_PHOTO);
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aler = new AlertDialog.Builder(getActivity());
                aler.setIcon(android.R.drawable.ic_menu_edit);
                aler.setTitle(getActivity().getText(R.string.title_update));
                aler.setMessage(getActivity().getText(R.string.title_mee_edit));
                aler.setPositiveButton(getActivity().getText(R.string.title_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editPokemon();
                    }
                });
                aler.setNegativeButton(getActivity().getText(R.string.title_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = aler.create();
                dialog.show();
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aler = new AlertDialog.Builder(getActivity());
                aler.setIcon(android.R.drawable.ic_delete);
                aler.setTitle(getActivity().getText(R.string.title_delete));
                aler.setMessage(getActivity().getText(R.string.messErrorSpace));
                aler.setPositiveButton(getActivity().getText(R.string.title_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePokemonFrag(pokemon.getiD_Pokemon());
                    }
                });
                aler.setNegativeButton(getActivity().getText(R.string.title_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog1 = aler.create();
                dialog1.show();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ThemPokemonActivity.class));
            }
        });

    }

    private void addControls() {
        txtId = view.findViewById(R.id.txtHienThiIdChiTiet);
        txtTen = view.findViewById(R.id.txtHienThiTenChiTiet);
        txtCanNang = view.findViewById(R.id.txtHienThiCanNangChiTiet);
        txtChieuCao = view.findViewById(R.id.txtHienThiChieuCaoChiTiet);

        spinnerDetail = view.findViewById(R.id.spinnerChiTiet02);

        btnThem = view.findViewById(R.id.btnThemChiTiet);
        btnSua = view.findViewById(R.id.btnCapNhatChiTiet);
        btnXoa = view.findViewById(R.id.btnXoaChiTiet);

        imageView = view.findViewById(R.id.imgHienThiAnhChiTiet);


        txthe = view.findViewById(R.id.txtHienThiHeChiTiet);
        spinner();


    }

    private void spinner() {
        adapter = new ArrayAdapter<Type>(getActivity(), android.R.layout.simple_list_item_1, DataList.typeArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerDetail.setAdapter(adapter);


    }

    public void getData(Pokemon poke) {
        if (poke != null) {
            pokemon = poke;

            String sql = "SELECT * FROM pokemon Where idPokemon = ?";
            database = DatabaseManager.CreateDatabase(getActivity(), DATABASE_NAME);
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
            String sqlType = "SELECT * FROM type Where idType = ?";
            Cursor cursor1 = database.rawQuery(sqlType, new String[]{idType + ""});
            //Cursor cursor1 = database.rawQuery("SELECT * FROM type Where idType = '" + idType + "'" , null);
            cursor1.moveToFirst();
            String idType2 = cursor1.getString(0);
            String tentype = cursor1.getString(1);
            final Type type = new Type(idType2, tentype);
            cursor1.close();

            //Bitmap image
            Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);


            //dua len giao dien
            txtId.setText(id);
            txtTen.setText(ten);
            txtCanNang.setText(cN + "");
            txtChieuCao.setText(cC + "");
            imageView.setImageBitmap(bitmap);
            //spinnerDetail.setSelection(adapter.getPosition(type));

            //final int pos = adapter.getPosition(type);
            spinnerDetail.setSelection(adapter.getPosition(type));

            //spinnerDetail.
            //spinnerDetail.setOnItemSelectedListener();

           // txthe.setText(type.getTypeTen());

            /*for (int i = 0 ;i<DataList.typeArrayList.size(); i++){
                if (spinnerDetail.getItemAtPosition(i).equals(type)){
                    spinnerDetail.setSelection(i);
                    break;

                }
            }*/

            //spinnerDetail.setSelection(adapter.getCount());
            // spinnerDetail.setAdapter(adapter);
            // adapter.notifyDataSetChanged();


        }

    }

    public byte[] convertImageViewToByte(ImageView img) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
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
        contentValues.put(DatabaseManager.ANH_POKEMON,anh);
        contentValues.put(DatabaseManager.ID_HE, type.getTypeId());


        database = DatabaseManager.CreateDatabase(getActivity(), DATABASE_NAME);
        database.update(DatabaseManager.TABLE_POKEMON, contentValues, "idPokemon = ?", new String[]{id + ""});

        Pokemon pokemon = new Pokemon(id, ten, cN, cC, anh, type);

        FragmentListPokemon fragmentListPokemon = (FragmentListPokemon) getFragmentManager().findFragmentById(R.id.fragListPokemon);
        fragmentListPokemon.editPokemon(pokemon);
        Toast.makeText(getActivity(),getActivity().getText(R.string.messSuccess),Toast.LENGTH_SHORT).show();

    }
    private void deletePokemonFrag(String id) {
        database = DatabaseManager.CreateDatabase(getActivity(),DATABASE_NAME);
        database.delete(DatabaseManager.TABLE_POKEMON,"idPokemon = ?", new String[] {id + ""});

        FragmentListPokemon fragmentListPokemon = (FragmentListPokemon) getFragmentManager().findFragmentById(R.id.fragListPokemon);
        fragmentListPokemon.delPokemon(id);

        imageView.setImageResource(R.drawable.pictures_folder);
        txtId.setText("");
        txtTen.setText("");
        txtCanNang.setText("");
        txtChieuCao.setText("");
        Toast.makeText(getActivity(),getActivity().getText(R.string.messSuccess),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == RESQUEST_CHOOSE_PHOTO){
                try {
                    Uri imageUri = data.getData();
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }}}
    }

}