package com.example.jsonrest;

import java.util.ArrayList;

public class G {
    public static String SERVER_NAME = "";
    public static String SERVER_IP = "";
    public static String SERVER_PORT = "";
    public static DatabaseHelper dbHelper;
    public static ArrayList<ServerDataModel> serverList;
    public static int LAST_SERVER_INDEX = -1;

    public static String SHARED_PREFERENCE_NAME = "JSON_REST";
    public static String SHARED_PREFERENCE_LAST_SERVER = "LAST_SERVER";
}
