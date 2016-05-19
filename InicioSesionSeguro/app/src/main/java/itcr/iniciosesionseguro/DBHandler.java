package itcr.iniciosesionseguro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database nombre
    private static final String DATABASE_nombre = "UsuarioInfo";
    // Contacts table nombre
    private static final String TABLE_Usuario = "Usuario";
    // Shops Table Columns nombres
    private static final String KEY_ID = "id";
    private static final String KEY_nombre = "nombre";
    private static final String KEY_correo = "correo";
    private static final String KEY_contraseña = "contraseña";

    public DBHandler(Context context) {
        super(context, DATABASE_nombre, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_Usuario_TABLE = "CREATE TABLE " +
                TABLE_Usuario + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_nombre + " TEXT,"
                + KEY_correo + " TEXT," + KEY_contraseña + " TEXT"
                + ")";
        db.execSQL(CREATE_Usuario_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion){
// Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Usuario);
// Creating tables again
        onCreate(db);
    }
    // Adding new Usuario
    public void addUsuario(Usuario Usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_nombre, Usuario.getNombre()); // Contact nombre
        values.put(KEY_correo, Usuario.getCorreo()); // Contact correo Number
        values.put(KEY_contraseña, Usuario.getContraseña());
        // Inserting Row
        db.insert(TABLE_Usuario, null, values);
        db.close(); // Closing database connection
    }
    // Getting one shop
    public Usuario getUsuario(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_Usuario,
                new String[] { KEY_ID, KEY_nombre, KEY_correo,
                        KEY_contraseña},
                KEY_correo + "=?",
                new String[] { String.valueOf(correo) }, null,
                null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        Usuario Usuario = new
                Usuario(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                cursor.getString(3));
        // Return shop
        return Usuario;
    }

    public List<Usuario> getAllUsers() {
        List<Usuario> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_Usuario;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Looping through all the rows and adding to list
        if (cursor.moveToFirst()){
            do {
                Usuario Usuario = new Usuario();
                Usuario.setId(Integer.parseInt(cursor.getString(0)));
                Usuario.setNombre(cursor.getString(1));
                Usuario.setCorreo(cursor.getString(2));
                Usuario.setContraseña(cursor.getString(3));
                // Adding contact to list
                userList.add(Usuario);
            } while (cursor.moveToNext());
        }
        // Return contact list
        return userList;
    }

    // Getting shops count
    public int getUserCount() {
        String countQuery = "SELECT * FROM " + TABLE_Usuario;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // Return count
        return cursor.getCount();
    }

    public int updateUsuario(Usuario Usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_nombre, Usuario.getNombre());
        values.put(KEY_correo, Usuario.getCorreo());
        values.put(KEY_contraseña, Usuario.getContraseña());
        // Updating row
        return db.update(TABLE_Usuario, values, KEY_ID + " = ?",
                new String[] { String.valueOf(Usuario.getId()) });
    }

    public void deleteUser(Usuario Usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Usuario, KEY_ID + " = ?",
                new String[] { String.valueOf(Usuario.getId()) });
        db.close();
    }
}