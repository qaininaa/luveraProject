package com.example.luveraproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "luveraproject.db";
    private static final int DATABASE_VERSION = 2;

    //table users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_NOHP = "nomorhp";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    //table products
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID_PRODUCTS = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_IS_NEW = "is_new";
    private static final String COLUMN_DESCRIPTION = "description";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DB_PATH", context.getDatabasePath(DATABASE_NAME).getAbsolutePath());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_NOHP + " TEXT,"
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_ID_PRODUCTS + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_IMAGE + " TEXT,"
                + COLUMN_IS_NEW + " INTEGER,"
                + COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Insert data to products table
        ContentValues product1 = new ContentValues();
        product1.put(COLUMN_NAME, "Wardah Colorfit");
        product1.put(COLUMN_PRICE, 45000 );
        product1.put(COLUMN_CATEGORY, "makeup");
        product1.put(COLUMN_IMAGE, "wardah_colorfit");
        product1.put(COLUMN_IS_NEW, 1);
        product1.put(COLUMN_DESCRIPTION, "Deskripsi produk contoh ke-");

        db.insert(TABLE_PRODUCTS, null, product1);

        ContentValues product2 = new ContentValues();
        product2.put(COLUMN_NAME, "Anua");
        product2.put(COLUMN_PRICE, 100000 );
        product2.put(COLUMN_CATEGORY, "skincare");
        product2.put(COLUMN_IMAGE, "");
        product2.put(COLUMN_IS_NEW, 1);
        product2.put(COLUMN_DESCRIPTION, "Deskripsi produk contoh ke-");

        db.insert(TABLE_PRODUCTS, null, product2);

        ContentValues product3 = new ContentValues();
        product3.put(COLUMN_NAME, "BioAqua");
        product3.put(COLUMN_PRICE, 30000 );
        product3.put(COLUMN_CATEGORY, "skincare");
        product3.put(COLUMN_IMAGE, "");
        product3.put(COLUMN_IS_NEW, 1);
        product3.put(COLUMN_DESCRIPTION, "Deskripsi produk contoh ke-");

        db.insert(TABLE_PRODUCTS, null, product3);


        Log.d("DB", "Table users dan products berhasil dibuat beserta sample datanya");
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public boolean insertUser(String username, String email, String noHp, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_NOHP, noHp);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public String getUsernameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String username = "Pengguna"; // default kalau tidak ditemukan

        Cursor cursor = db.rawQuery("SELECT username FROM users WHERE email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            username = cursor.getString(0);
            cursor.close();
        }

        return username;
    }
}
