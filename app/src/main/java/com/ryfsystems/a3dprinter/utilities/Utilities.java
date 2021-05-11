package com.ryfsystems.a3dprinter.utilities;

public class Utilities {

    public static final String dbName = "threedprinter";
    public static final Integer dbVersion = 1;

    // Constantes Campos Tabla Usuarios
    public static final String TABLE_USER = "user";
    public static final String FIELD_U_ID = "uid";
    public static final String FIELD_U_NAME = "name";
    public static final String FIELD_U_USER = "userName";
    public static final String FIELD_U_PASSWORD = "password";
    public static final String FIELD_U_EMAIL = "email";
    public static final String FIELD_U_PHONE = "phone";
    public static final String FIELD_U_ROLE = "role";

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
    public static final String TABLE_ROLES = "roles";
    public static final String FIELD_R_ID = "rid";
    public static final String FIELD_R_NAME = "name";

    // Creacion de tabla Roles
    public static final String CREATE_TABLE_ROLES = "CREATE TABLE " +
            TABLE_ROLES + " (" +
            FIELD_R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FIELD_R_NAME + " TEXT)";

}
