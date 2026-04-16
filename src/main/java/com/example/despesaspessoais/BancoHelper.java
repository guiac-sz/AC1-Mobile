package com.example.despesaspessoais;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class BancoHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "despesas.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "despesas";

    private static final String ID = "id";
    private static final String DESCRICAO = "descricao";
    private static final String VALOR = "valor";
    private static final String DATA = "data";
    private static final String CATEGORIA = "categoria";
    private static final String FORMADEPAGAMENTO = "formaDePagamento";
    private static final String STATUS = "status";

    public BancoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DESCRICAO + " TEXT, "
                + VALOR + " TEXT, "
                + DATA + " TEXT, "
                + CATEGORIA + " TEXT, "
                + FORMADEPAGAMENTO + " TEXT, "
                + STATUS + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long inserirDespesa(String descricao, String valor, String data,
                              String categoria, String formaDePagamento, String status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DESCRICAO, descricao);
        values.put(VALOR, valor);
        values.put(DATA, data);
        values.put(CATEGORIA, categoria);
        values.put(FORMADEPAGAMENTO, formaDePagamento);
        values.put(STATUS, status);

        return db.insert(TABLE_NAME, null, values);
    }
    public Cursor listarDespesa() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM despesas", null);
    }

    public int atualizarDespesa(int id, String descricao, String valor,
                               String data, String categoria,
                               String formaDePagamento, String status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("descricao", descricao);
        values.put("valor", valor);
        values.put("data", data);
        values.put("categoria", categoria);
        values.put("formaDePagamento", formaDePagamento);
        values.put("status", status);

        return db.update("despesas", values, "id=?", new String[]{String.valueOf(id)});
    }

    public int excluirDespesa(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("despesas", "id=?", new String[]{String.valueOf(id)});
    }
}