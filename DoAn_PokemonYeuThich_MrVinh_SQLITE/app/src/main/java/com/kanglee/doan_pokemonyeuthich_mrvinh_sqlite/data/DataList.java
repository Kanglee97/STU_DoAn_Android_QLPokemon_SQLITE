package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data;

import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.IdAuto;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Pokemon;
import com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model.Type;

import java.util.ArrayList;

public class DataList {
    public static ArrayList<Pokemon> pokemonArrayList;
    public  static  ArrayList<Type> typeArrayList;

    public static IdAuto idPokemon   = new IdAuto();
    public static IdAuto idType  = new IdAuto();
}
