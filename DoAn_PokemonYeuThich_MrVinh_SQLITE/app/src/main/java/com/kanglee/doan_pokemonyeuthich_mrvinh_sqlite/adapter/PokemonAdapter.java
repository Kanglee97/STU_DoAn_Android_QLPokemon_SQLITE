package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Pokemon;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.R;

import java.util.ArrayList;

public class PokemonAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Pokemon> list;



    public PokemonAdapter(Context context, int layout, ArrayList<Pokemon> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    public ArrayList<Pokemon> getList() {
        return list;
    }

    public void setList(ArrayList<Pokemon> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{

        ImageView imgAnhPokemon;
        TextView txtHienThiMaPokemon, txtHienThiTenPokemon, txtHienThiHePokemon;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder = new ViewHolder();

            //anh xa
            Pokemon pokemon = list.get(position);
            holder.imgAnhPokemon = convertView.findViewById(R.id.imgHienThiPokemon);
            holder.txtHienThiHePokemon = convertView.findViewById(R.id.txtHienThiHePokemon_List);
            holder.txtHienThiMaPokemon = convertView.findViewById(R.id.txtHienThiMaSoPokemon_List);
            holder.txtHienThiTenPokemon = convertView.findViewById(R.id.txtHienThiTenPokemon_List);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        //gan gia tri
        Pokemon pokemon = list.get(position);
        holder.txtHienThiTenPokemon.setText(pokemon.getTen_Pokemon());
        holder.txtHienThiMaPokemon.setText(pokemon.getiD_Pokemon());
        //holder.txtHienThiHePokemon.setText(String.valueOf(pokemon.getTentype()));
        holder.txtHienThiHePokemon.setText(pokemon.getiDType().getTypeTen());
        byte [] hinhAnhh = pokemon.getAnh_Pokemon();

        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhAnhh,0,hinhAnhh.length);
        holder.imgAnhPokemon.setImageBitmap(bitmap);

        return convertView;
    }
}
