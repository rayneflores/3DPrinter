package com.ryfsystems.a3dprinter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.ryfsystems.a3dprinter.utilities.Utilities.CREATE_TABLE_PRINTER;
import static com.ryfsystems.a3dprinter.utilities.Utilities.CREATE_TABLE_ROLE;
import static com.ryfsystems.a3dprinter.utilities.Utilities.CREATE_TABLE_USER;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_ROLE);
        db.execSQL(CREATE_TABLE_PRINTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS role");
        db.execSQL("DROP TABLE IF EXISTS printer");
        onCreate(db);
    }
}
