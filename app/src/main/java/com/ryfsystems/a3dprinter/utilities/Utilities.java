package com.ryfsystems.a3dprinter.utilities;

public class Utilities {

    public static final String dbName = "threedprinter";
    public static final Integer dbVersion = 1;

    // Constantes Campos Tabla Usuarios
    public static final String TABLE_USER = "user";
    public static final String FIELD_U_ID = "uuid";
    public static final String FIELD_U_NAME = "uname";
    public static final String FIELD_U_USER = "uuserName";
    public static final String FIELD_U_PASSWORD = "upassword";
    public static final String FIELD_U_EMAIL = "uemail";
    public static final String FIELD_U_PHONE = "uphone";
    public static final String FIELD_U_ROLE = "urole";

    // Creacion de tabla Usuarios
    public static final String CREATE_TABLE_USER = "CREATE TABLE " +
            TABLE_USER + " (" +
            FIELD_U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FIELD_U_NAME + " TEXT, " +
            FIELD_U_USER + " TEXT, " +
            FIELD_U_PASSWORD + " TEXT, " +
            FIELD_U_EMAIL + " TEXT, " +
            FIELD_U_PHONE + " TEXT, " +
            FIELD_U_ROLE + " INTEGER)";

    // Constantes Campos Tabla Roles
    public static final String TABLE_ROLE = "role";
    public static final String FIELD_R_ID = "rid";
    public static final String FIELD_R_NAME = "rname";

    // Creacion de tabla Roles
    public static final String CREATE_TABLE_ROLE = "CREATE TABLE " +
            TABLE_ROLE + " (" +
            FIELD_R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FIELD_R_NAME + " TEXT)";

    // Constantes Campos Tabla Impresoras
    public static final String TABLE_PRINTER = "printer";
    public static final String FIELD_P_ID = "pid";
    public static final String FIELD_P_NAME = "pname";
    public static final String FIELD_P_BRAND = "pbrand";
    public static final String FIELD_P_MODEL = "pmodel";
    public static final String FIELD_P_IMAGE = "pimage";

    // Creacion de tabla Impresoras
    public static final String CREATE_TABLE_PRINTER = "CREATE TABLE " +
            TABLE_PRINTER + " (" +
            FIELD_P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FIELD_P_NAME + " TEXT, " +
            FIELD_P_BRAND + " TEXT, " +
            FIELD_P_MODEL + " TEXT, " +
            FIELD_P_IMAGE + " TEXT)";

}
