package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.data;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseManager {
    private static final String TAG = "DBMANAGER";

    public static final int VERSION = 2;

    public static final String TABLE_TYPE = "type";
    public static final String ID_HE = "idType";
    public static final String NAME_HE = "tenType";

    public static final String TABLE_POKEMON = "pokemon";
    public static final String ID_POKEMON = "idPokemon";
    public static final String NAME_POKEMON = "tenPokemon";
    public static final String CAN_NANG = "canNang";
    public static final String CHIEU_CAO = "chieuCao";
    public static final String ANH_POKEMON = "anhPokemon";

    public static final String TABLE_ID = "id";


    public static SQLiteDatabase CreateDatabase(Activity activity, String databaseName){
        try {
            String outFileName = activity.getApplicationInfo().dataDir + "/databases/" + databaseName;
            File f = new File(outFileName);
            if(!f.exists()) {
                InputStream e = activity.getAssets().open(databaseName);
                File folder = new File(activity.getApplicationInfo().dataDir + "/databases/");
                if (!folder.exists()) {
                    folder.mkdir();
                }
                FileOutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];

                int length;
                while ((length = e.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                e.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return activity.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
    }
}
