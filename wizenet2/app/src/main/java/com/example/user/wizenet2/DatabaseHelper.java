package com.example.user.wizenet2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04/08/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static DatabaseHelper mInstance;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Wizenet";
    // Contacts table name
    private static final String TABLE_CONTROL_PANEL = "ControlPanel";
    private static final String TABLE_MESSAGES = "Messages";
    private static final String mgnet_items = "mgnet_items";

    // Table Columns ControlPanel
    private static final String ID = "id";
    private static final String KEY = "_key";
    private static final String VALUE = "_value";
    private static final String DESCRIPTION = "_description";
    public static final String DEMOURL = "http://main.wizenet.co.il/webservices/freelance.asmx";//default
    //Table Columns MESSAGES
    private static final String MsgID = "msgID";
    private static final String MsgSUBJECT ="msgSubject";
    private static final String MsgCOMMENT = "msgComment";
    private static final String MsgURL = "msgUrl";
    private static final String MsgDATE = "msgDate";
    private static final String MsgREAD = "msgRead";
    private static final String MsgTYPE = "msgType";

    //Table Columns TEMP
    //private static final String TMP_PARENTID = "msgID";
    //private static final String MsgSUBJECT ="msgSubject";
    //private static final String MsgCOMMENT = "msgComment";
    //private static final String MsgURL = "msgUrl";
    //private static final String MsgDATE = "msgDate";

    static private Context mCtx;

    public static DatabaseHelper getInstance(Context ctx) {

        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
            mCtx = ctx;
        }
        return mInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mCtx = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_mgnet_items =
                "CREATE TABLE " + "mgnet_items" + "("
//                        + "id" +  " TEXT PRIMARY KEY AUTOINCREMENT,"
                        + "Pname" + " TEXT, "
                        + "Pmakat" + " TEXT, "
                        + "Pprice" + " TEXT, "
                        + "Poprice" + " TEXT "
                        + ")";
        String CREATE_mgnet_client_items =
                "CREATE TABLE " + "mgnet_client_items" + "("
//                        + "id" +  " TEXT PRIMARY KEY AUTOINCREMENT,"
                        + "Pname" + " TEXT, "
                        + "Pmakat" + " TEXT, "
                        + "Pprice" + " TEXT, "
                        + "Poprice" + " TEXT "
                        + ")";
        String CREATE_CP_TABLE =
                "CREATE TABLE " + TABLE_CONTROL_PANEL + "("
                        + KEY +  " TEXT PRIMARY KEY,"
                        + VALUE + " TEXT, "
                        + DESCRIPTION + " TEXT "
                        + ")";

        String CREATE_MESSAGES_TABLE =
                "CREATE TABLE " + TABLE_MESSAGES + "("
                        + MsgID +  " TEXT PRIMARY KEY,"
                        + MsgSUBJECT + " TEXT, "
                        + MsgCOMMENT + " TEXT, "
                        + MsgURL + " TEXT, "
                        + MsgDATE + " TEXT, "
                        + MsgREAD + " TEXT, "
                        + MsgTYPE + " TEXT "
                        + ")";
        db.execSQL(CREATE_mgnet_items);
        db.execSQL(CREATE_CP_TABLE);
        db.execSQL(CREATE_MESSAGES_TABLE);
        db.execSQL(CREATE_mgnet_client_items);
        //db.execSQL("DROP TABLE "+TABLE_CONTROL_PANEL+" ");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTROL_PANEL);
// Creating tables again
        onCreate(db);
    }


//    public DatabaseHelper open() throws SQLException {
//        SQLiteDatabase db = this.getWritableDatabase();
//        mInstance = this.getInstance(mCtx);
//        db = mInstance.getWritableDatabase();
//        return this;
//
//    }


//    public Cursor readEntry() {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        String[] allColumns = new String[] { mInstance.KEY,mInstance.VALUE,mInstance.DESCRIPTION };
//        Cursor mcursor = db.query(mInstance.TABLE_CONTROL_PANEL, allColumns, null, null, null, null, null);
//        mcursor.moveToFirst();
//        db.close();
//        return  mcursor;
//    }
    public boolean isEmpty(String s){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_CONTROL_PANEL;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        db.close();
        if(icount>0){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean mgnet_items_isEmpty(String parameter){
        boolean flag = false;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String count = "";
            if (parameter.equals("all")){
                count = "SELECT count(Pname) FROM mgnet_items";
            }else{
                count = "SELECT count(Pname) FROM mgnet_client_items";

            }
            Cursor mcursor = db.rawQuery(count, null);
            mcursor.moveToFirst();
            int icount = mcursor.getInt(0);
            db.close();
            if(icount>0){
                flag = false;
            }
            else{
                flag = true;
            }
        }catch(Exception ex){
            Log.e("MYTAG",ex.getMessage());
        }
        return  flag;
    }
    public List<Order> get_mgnet_items_by_Pname(String pname,String allORclient) {
        List<Order> orderList = new ArrayList<Order>();
// Select All Query
        String selectQuery = "";
        if (allORclient.equals("all")) {
            selectQuery = "SELECT * FROM mgnet_items where Pname like '%" + pname + "%' ";

        } else{
            selectQuery = "SELECT * FROM mgnet_client_items where Pname like '%" +pname+ "%' " ;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Order cp= new Order(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)

                );

// Adding contact to list
                orderList.add(cp);
            } while (cursor.moveToNext());
        }
        db.close();
        return orderList;
    }
    public List<Order> get_mgnet_items(String allORclient) {
        List<Order> orderList = new ArrayList<Order>();
// Select All Query
        String selectQuery ="";
        if (allORclient.equals("all")){
            selectQuery = "SELECT * FROM mgnet_items " ;
        }else{
            selectQuery = "SELECT * FROM mgnet_client_items " ;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Order cp= new Order(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)

                );

// Adding contact to list
                orderList.add(cp);
            } while (cursor.moveToNext());
        }
        db.close();
        return orderList;
    }

    public Order get_mgnet_item_by_pmakat(String pmakat) {
        List<Order> orderList = new ArrayList<Order>();
// Select All Query
        String selectQuery = "SELECT top 1 * FROM mgnet_items where Pmakat like '%" + pmakat + "%'" ;
        Order cp = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("MYTAG","db step 1 ");

//
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Log.e("MYTAG","db step 2 ");
                cp = new Order(
                        is_empty_cursor(cursor.getString(0)),
                        is_empty_cursor(cursor.getString(1)),
                        is_empty_cursor(cursor.getString(2)),
                        is_empty_cursor(cursor.getString(3)));
            }
            db.close();
        }catch(Exception e){
            Log.e("MYTAG","db + "+e.getMessage());
        }
        return cp;
    }
    public String is_empty_cursor(String s){
        if (isEmpty(s)) {
            return "";
        }else{
            return s;

        }
    }
    public boolean delete_from_mgnet_items(String allORclient) {
        boolean flag = true;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            if (allORclient.equals("all")){
                db.delete("mgnet_items", null, null);
            }else{
                db.delete("mgnet_client_items", null, null);

            }

            //db.insert("mgnet_items", null, values);
            db.close(); // Closing database connection
        }catch (Exception e){
            flag = false;
            e.printStackTrace();
            Log.e("MYTAG",e.getMessage());
        }
        return flag;
    }
    public void add_mgnet_items(String pname,String Pmakat,String Pprice,String Poprice,String allORclient) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Pname", pname);
            values.put("Pmakat", Pmakat);
            values.put("Pprice", Pprice);
            values.put("Poprice", Poprice);
            // Inserting Row
            if (allORclient.equals("all")){
                db.insert("mgnet_items", null, values);
            }else{
                db.insert("mgnet_client_items", null, values);
            }

            db.close(); // Closing database connection
        }catch (Exception e){
            e.printStackTrace();
            Log.e("MYTAG",e.getMessage());
        }

    }

    public void addControlPanel(String key,String val) {

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY, key);
            values.put(VALUE, val);
            values.put(DESCRIPTION, "");
            // Inserting Row
            db.insert(TABLE_CONTROL_PANEL, null, values);
            db.close(); // Closing database connection
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void addMessage(Message message) {

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MsgID , message.getMsgID());
            values.put(MsgSUBJECT, message.getMsgSubject());
            values.put(MsgCOMMENT, message.getMsgComment());
            values.put(MsgURL , message.getMsgUrl());
            values.put(MsgDATE  , message.getMsgDate());
            values.put(MsgREAD  , message.getMsgRead());
            values.put(MsgTYPE  , message.getMsgType());

            // Inserting Row
            db.insert(TABLE_MESSAGES, null, values);
            db.close(); // Closing database connection
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    //if isEmpty
    public boolean verification(String _username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = -1;
        Cursor c = null;
        try {
            String query = "SELECT COUNT(*) FROM "
                    + TABLE_CONTROL_PANEL + " WHERE " + KEY + " = ?";
            c = db.rawQuery(query, new String[] {_username});
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            return count > 0;
        }
        finally {
            if (c != null) {
                c.close();
                db.close();
            }
        }
    }

    public boolean checkIfExists(String stringKey) {

        SQLiteDatabase db = this.getWritableDatabase();

        //String count = "SELECT * FROM " + TABLE_CONTROL_PANEL + " WHERE " + KEY + " = " + stringKey;
        Cursor mcursor = db.rawQuery("select count(*) from " + TABLE_CONTROL_PANEL + " WHERE " + KEY + " = '" + stringKey +"'", null);
        mcursor.moveToFirst();
        int icount = mcursor.getCount();
        if(icount>0){
            db.close();
            return true;
        }else{
            db.close();
            return false;
        }
    }


    public ControlPanel getControlPanel(String key) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_CONTROL_PANEL + " WHERE "
               + KEY + " = '" + key+"'";
        Cursor c = db.rawQuery(query, null);

        if (c != null)
            c.moveToFirst();

        ControlPanel cp = new ControlPanel();
        //cp.setId(c.getInt(c.getColumnIndex(ID)));
        cp.setKey((c.getString(c.getColumnIndex(KEY))));
        cp.setValue(c.getString(c.getColumnIndex(VALUE)));

        db.close();
        return cp;
    }

    public Message getMsgByKey(String msgID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_MESSAGES + " WHERE "
                + MsgID + " = '" + msgID+"'";
        Cursor c = db.rawQuery(query, null);

        if (c != null)
            c.moveToFirst();

        Message cp = new Message();
        //cp.setId(c.getInt(c.getColumnIndex(ID)));
        cp.setMsgID     (c.getString(c.getColumnIndex(MsgID)));
        cp.setMsgSubject(c.getString(c.getColumnIndex(MsgSUBJECT)));
        cp.setMsgComment(c.getString(c.getColumnIndex(MsgCOMMENT)));
        cp.setMsgUrl    (c.getString(c.getColumnIndex(MsgURL)));
        cp.setMsgDate   (c.getString(c.getColumnIndex(MsgDATE)));
        cp.setMsgRead   (c.getString(c.getColumnIndex(MsgREAD)));
        cp.setMsgType   (c.getString(c.getColumnIndex(MsgTYPE)));

        db.close();
        return cp;
    }

    public String getValueByKey(String key) {

        SQLiteDatabase db = this.getReadableDatabase();
        ControlPanel cp = new ControlPanel();
        String query = "SELECT  * FROM " + TABLE_CONTROL_PANEL + " WHERE "
                + KEY + " = '" + key+"'";
        try{
            Cursor c = db.rawQuery(query, null);

            if (c != null)
                c.moveToFirst();
            try {

                //cp.setId(c.getInt(c.getColumnIndex(ID)));
                cp.setKey((c.getString(c.getColumnIndex(KEY))));
                cp.setValue(c.getString(c.getColumnIndex(VALUE)));
                db.close();
                return cp.getValue();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }catch(Exception e){

        }

        db.close();
        return "";
    }

    public List<ControlPanel> getAllKeysAndValues() {
        List<ControlPanel> control_panel_list = new ArrayList<ControlPanel>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CONTROL_PANEL;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ControlPanel cp= new ControlPanel();
                //cp.setId(Integer.parseInt(cursor.getString(0)));
                cp.setKey(cursor.getString(0));
                cp.setValue(cursor.getString(1));
                cp.setDescription(cursor.getString(2));
// Adding contact to list
                control_panel_list.add(cp);
            } while (cursor.moveToNext());
        }
        db.close();
        return control_panel_list;
    }

    //// TODO: 01/09/2016 fix it 
    public List<Message> getAllMessages() {
        List<Message> messages_list = new ArrayList<Message>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MESSAGES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Message cp= new Message();
                cp.setMsgID(cursor.getString(0));
                cp.setMsgSubject(cursor.getString(1));
                cp.setMsgComment(cursor.getString(2));
                cp.setMsgUrl(cursor.getString(3));
                cp.setMsgDate(cursor.getString(4));
                cp.setMsgRead(cursor.getString(5));
                cp.setMsgType(cursor.getString(6));
                        //cp.setValue(cursor.getString(1));
                //cp.setDescription(cursor.getString(2));
// Adding contact to list
                messages_list.add(cp);
            } while (cursor.moveToNext());
        }
        db.close();
        return messages_list;
    }

    public void updateValue(String key,String val) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VALUE, val);
        //values.put(DESCRIPTION, "");
        db.update(TABLE_CONTROL_PANEL, values, KEY + " = '"+key+"'", null);
        db.close();
    }


    public void resetURL(ControlPanel cp) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY, cp.getKey());
        values.put(VALUE, cp.getValue());
// updating row
        db.update(TABLE_CONTROL_PANEL, values, ID + " = " + cp.getId(), null);
        db.close();
    }


    public void deleteRowByKey(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTROL_PANEL, KEY + " = '" + key+"'", null);
        //db.execSQL("delete from "+ TABLE_CONTROL_PANEL);
        db.close();
    }
    public void deleteRowByMsgID(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, MsgID + " = '" + id+"'", null);
        //db.execSQL("delete from "+ TABLE_CONTROL_PANEL);
        db.close();
    }

}


