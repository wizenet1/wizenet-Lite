package com.example.user.wizenet2.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.user.wizenet2.CallSoap;
import com.example.user.wizenet2.DatabaseHelper;
import com.example.user.wizenet2.Helper;
import com.example.user.wizenet2.Message;
import com.example.user.wizenet2.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 05/09/2016.
 */
public class Model {
    private final static Model instance = new Model();
    Context context;
    //ModelSql model = new ModelSql();
    //ModelParse model = new ModelParse();

    private Model() {
    }

    public static Model getInstance() {
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        //model.init(context);
    }

    //###################################
    //SYNCH LOGIN CALL
    //###################################
    public String syncLogin(final String macAddress,final String username,final String pass) {

        CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
        String response = cs.Call(macAddress, username, pass);//login
        //Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_LONG).show();

        String myResponse = response;
        if (!myResponse.startsWith("incorrect")) {
            //my regex changes
            myResponse = myResponse.replaceAll("USER_LoginResponse", "");
            myResponse = myResponse.replaceAll("USER_LoginResult=", "Login:");
            myResponse = myResponse.replaceAll(";", "");

        } else {
            return "incorrect URL";
        }
        String myNewResponse = myResponse;

        JSONObject j = null;

        boolean status = false;
        String msg = null;
        try {
            j = new JSONObject(myNewResponse);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Login");
            JSONObject object = jarray.getJSONObject(0);
            String estatus = jarray.getJSONObject(0).getString("Status");
            msg = jarray.getJSONObject(0).getString("Msg");
            if (estatus.equals("1")) {
                status = true;
            } else {
                status = false;
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String result = "";
        if (status) {
            //Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
            result = "1";
        } else {
            result = "0";
            //Toast.makeText(getApplicationContext(), "username or password incorrect", Toast.LENGTH_LONG).show();
        }

        //#####   UPDATE USERNAME IN DATABASE   #####
        if (DatabaseHelper.getInstance(context).getValueByKey("username").equals("")) {
            DatabaseHelper.getInstance(context).updateValue("username", username);
        }
        return result;
    }


    public interface LoginListener{
        public void onResult(String str);
    }
    //###################################
    //ASYNCH LOGIN CALL
    //###################################
    public void AsyncLogin(final String macAddress,final String username,final String pass, final LoginListener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {

                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Call(macAddress,username,pass);//login
                //Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_LONG).show();

                String myResponse = response;
                if (!myResponse.startsWith("incorrect")){
                    //my regex changes
                    myResponse = myResponse.replaceAll("USER_LoginResponse", "");
                    myResponse = myResponse.replaceAll("USER_LoginResult=", "Login:");
                    myResponse = myResponse.replaceAll(";", "");
                    return myResponse.toString();
                }
                return "incorrect URL";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                JSONObject j = null;

                boolean status = false;
                String msg = null;
                try {
                    j = new JSONObject(result);
                    //get the array [...] in json
                    JSONArray jarray = j.getJSONArray("Login");
                    JSONObject object = jarray.getJSONObject(0);
                    String estatus = jarray.getJSONObject(0).getString("Status");
                    msg = jarray.getJSONObject(0).getString("Msg");
                    if (estatus.equals("1")) {
                        status = true;
                    } else {
                        status = false;
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (result.startsWith("incorrect")) {
                    result = "incorrect";
                } else if (status) {
                    //Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
                   result="1";
                } else {
                    result="0";
                    //Toast.makeText(getApplicationContext(), "username or password incorrect", Toast.LENGTH_LONG).show();
                }

                //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                if(DatabaseHelper.getInstance(context).getValueByKey("username").equals("")){
                    DatabaseHelper.getInstance(context).updateValue("username",username);
                }
                listener.onResult(result);
            }
        };
        task.execute();
    }

    public interface StatusListener{
        public void onResult(String str);
    }
    //###################################
    //ASYNCH STATUS CALL
    //###################################
    public void AsyncStatus(final String macAddress,final String longtitude,final String latitude, final StatusListener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {

                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.Call3(macAddress,longtitude,latitude);//GPS
                //Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_LONG).show();

                String myResponse = response;//GET STATUS JSON
                //my regex changes
                myResponse = myResponse.replaceAll("USER_StatusResponse", "");
                myResponse = myResponse.replaceAll("USER_StatusResult=", "Status:");
                myResponse = myResponse.replaceAll(";", "");
                return myResponse.toString();
            }

            @Override
            protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    JSONObject j = null;

                    boolean mystatus = false;
                    String msg = null;
                    try {
                        j = new JSONObject(result);
                        //get the array [...] in json
                        JSONArray jarray = j.getJSONArray("Status");
                        String estatus = jarray.getJSONObject(0).getString("Status");//1 or 0
                        msg = jarray.getJSONObject(0).getString("Msg");//
                        if (estatus.equals("1")) {
                            mystatus = true;
                            result = "1";
                        } else {
                            //Toast.makeText(getApplicationContext(),"wrong username or password", Toast.LENGTH_LONG).show();
                            mystatus = false;
                            result = "0";
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                listener.onResult(result);
            }
        };
        task.execute();
    }

    public interface ReminderListener{
        public void onResult(String str,String str2,int size);
    }
    //###################################
    //ASYNCH REMINDER CALL
    //###################################
    public void AsyncReminder(final String macAddress, final ReminderListener listener) {

        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {

                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//databaseHelper.getControlPanel(1).getUrl());
                //String mac_address = getMacAddress();
                String response = cs.Call4(macAddress);
                //Log.e("myTag",response.toString());
                String myResponse = response;//GET STATUS JSON
                if(!response.startsWith("Err")){
                    //my regex changes
                    myResponse = myResponse.replaceAll("REMINDERS_retAlertResponse", "");
                    myResponse = myResponse.replaceAll("REMINDERS_retAlertResult=", "retAlertResult:");
                    myResponse = myResponse.replaceAll(";", "");
                }

                return myResponse.toString();
            }

            @Override
            protected void onPostExecute(String result) {
                String msgID="",msgSubject="",msgComment="",msgUrl="",msgDate="",msgRead="",msgType= "";
                super.onPostExecute(result);

                if(!result.startsWith("Err")){
                    JSONObject j = null;
                    try {
                        j = new JSONObject(result);

                        JSONArray jarray = j.getJSONArray("retAlertResult");

                        Log.e("myTag",jarray.toString()+"  Size = "+jarray.length());
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                        Date now = new Date();
                        String content = formatter.format(now);
                        //writeTextToFile(content+"+  Size:"+jarray.length()+"\n");
                        //String e_retAlert = jarray.getJSONObject(0).getString("retAlertResult");//1 or 0
                        int mySize=0;
                        //JSONArray array = new JSONArray(string_of_json_array);
                        if((jarray.length() > 0)){
                            //flag = true;
                            mySize = jarray.length();
                            for (int i = 0; i < jarray.length(); i++) {
                                msgID = jarray.getJSONObject(i).getString("msgID");
                                msgSubject = jarray.getJSONObject(i).getString("msgSubject");
                                msgComment = jarray.getJSONObject(i).getString("msgComment");
                                msgUrl = jarray.getJSONObject(i).getString("msgUrl");
                                msgDate = jarray.getJSONObject(i).getString("msgDate");
                                msgRead = jarray.getJSONObject(i).getString("msgRead");
                                msgType = jarray.getJSONObject(i).getString("msgType");
                                Message m = new Message(msgID,msgSubject,msgComment,msgUrl,msgDate,msgRead,msgType);
                                DatabaseHelper.getInstance(context).addMessage(m);
                            }
                            if(mySize == 1){
                                listener.onResult(msgSubject,msgComment,1);
                                //pushNotification(msgSubject,msgComment);
                            }else{
                                listener.onResult("Wizenet",mySize+" new messages",mySize);
                                //pushNotification("Wizenet",mySize+" new messages");
                            }

                        }else{
                            //    flag = false;
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();

                    }
                }
                //listener.onResult(result);
            }
        };
        task.execute();
    }


    public interface User_Details_Listener{
        public void onResult(String str);
    }
    //###################################
    //ASYNCH User_Details_Listener
    //###################################
    public void Async_User_Details_Listener(final String macAddress, final User_Details_Listener listener) {

        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {
            @Override
            protected String doInBackground(String... params) {

                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//databaseHelper.getControlPanel(1).getUrl());
                //String mac_address = getMacAddress();
                String response = cs.Call_USER_Details(macAddress);
                //Log.e("myTag",response.toString());
                String myResponse = response;//GET STATUS JSON
                //my regex changes
                myResponse = myResponse.replaceAll("USER_DetailsResponse", "");
                myResponse = myResponse.replaceAll("USER_DetailsResult=", "DetailsResult:");
                myResponse = myResponse.replaceAll(";", "");
                return myResponse.toString();
                //return response.toString();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                JSONObject j = null;
                String myStr="";
                String fname,lname;
                try {
                    j = new JSONObject(result);
                    //get the array [...] in json
                    JSONArray jarray = j.getJSONArray("DetailsResult");
                    fname = jarray.getJSONObject(0).getString("Cfname");
                    lname = jarray.getJSONObject(0).getString("Clname");
                    myStr = fname+" "+lname;
                    //myStr=(jarray.getJSONObject(0).getString("Cfname"));//.concat(" ");//1 or 0
                    //myStr.concat(jarray.getJSONObject(0).getString("Clname"));//

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


                listener.onResult(myStr);//result);
            }
        };
        task.execute();
    }

    public interface get_clients_Listener{
        public void onResult(String str);
    }

    public void Async_Get_Clients_Listener(final String macAddress, final get_clients_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.Call2(macAddress);
                String myResponse = response;
                myResponse = myResponse.replaceAll("USER_ClientsResponse", "");
                myResponse = myResponse.replaceAll("USER_ClientsResult=", "Customers:");
                myResponse = myResponse.replaceAll(";", "");


//            FileWriter fw = null;
//            try {
//                fw = new FileWriter(myFile);
//                BufferedWriter bw = new BufferedWriter(fw);
//                bw.write(myResponse);
//                bw.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


                return myResponse.toString();
            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);


            }
        };
        task.execute();
    }

    public interface Call_getClientsContactsListener{
        public void onResult(String str);
    }

    public void Async_Get_Clients_Contacts_Listener(final String macAddress, final Call_getClientsContactsListener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.Call_getClientsContacts(macAddress);
                String myResponse = response;
                myResponse = myResponse.replaceAll("getClientsContactsResponse", "");
                myResponse = myResponse.replaceAll("getClientsContactsResult=", "Contacts:");
                myResponse = myResponse.replaceAll(";", "");

                return myResponse.toString();
            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }

    public interface get_mgnet_items_Listener{
        public void onResult(String str);
    }

    //PRODUCTS_ITEMS_LIST
    public void Async_Get_mgnet_items_Listener(final String macAddress, final get_mgnet_items_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.getOrdersProducts(macAddress);
                String myResponse = response;
                myResponse = myResponse.replaceAll("getOrdersProductsResponse", "");
                myResponse = myResponse.replaceAll("getOrdersProductsResult=", "Orders:");
                myResponse = myResponse.replaceAll(";", "");

                return myResponse.toString();
            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }


    public interface get_mgnet_client_items_Listener{
        public void onResult(String str);
    }
    //PRODUCTS_CLIENTS_ITEMS_LIST
    public void Async_Get_mgnet_client_items_Listener(final String macAddress,final String cid, final get_mgnet_client_items_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);

                String response = cs.get_mgnet_client_items_list(macAddress,cid);

                String myResponse = response;
                myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResponse", "");
                myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResult=", "Orders:");
                myResponse = myResponse.replaceAll(";", "");

                return myResponse.toString();
            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }


    //region CREATE_OFFLINE
    public interface CREATE_OFFLINE_Listener{
        public void onResult(String str);
    }
    public void Async_CREATE_OFFLINE_Listener(final String macAddress,final String json, final CREATE_OFFLINE_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
                //String response = cs.Call(mac_address, memail, mpass);
                String response = cs.CREATE_OFFLINE(macAddress,json);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("CREATE_OFFLINEResponse", "");
                    myResponse = myResponse.replaceAll("CREATE_OFFLINEResult=", "");
                    myResponse = myResponse.replaceAll("\\{", "");
                    myResponse = myResponse.replaceAll("\\}", "");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString();
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }



            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }
    //endregion


    //region GET_CALLS_LIST
    public interface GET_CALLS_LIST_Listener{
        public void onResult(String str);
    }
    public void Async_GET_CALLS_LIST_Listener(final String macAddress, final GET_CALLS_LIST_Listener listener) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String >() {

            //###################################
            //extract the data and return it
            //###################################
            @Override
            protected String doInBackground(String... params) {
                // USER_ClientsResponse
                CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));
                String response = cs.GET_CALLS_LIST(macAddress);
                try{
                    String myResponse = response;

                    myResponse = myResponse.replaceAll("CALLS_ListResponse", "");
                    myResponse = myResponse.replaceAll("CALLS_ListResult=", "");
                    myResponse = myResponse.replaceAll("\\{", "");
                    myResponse = myResponse.replaceAll("\\}", "");
                    myResponse = myResponse.replaceAll(";", "");
                    myResponse= myResponse.replaceAll("\\<[^>]*>","");
                    return myResponse.toString();
                }catch(Exception e){
                    return "nothing? "+e.getMessage();
                }
            }

            //###################################
            //active the fragment with json result by bundle
            //###################################
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                listener.onResult(result);
                //goToCustomersFragment(result);
            }
        };
        task.execute();
    }
    //endregion



}


