package com.example.user.wizenet2;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.user.wizenet2.model.Model;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 23/08/2016.
 */
public class Alarm_Receiver_sync_client_products extends BroadcastReceiver {
    NotificationManager nm;
    Notification myNotication;

    long[] vibrate = { 400, 400, 200,200,200 };
    Calendar calendar = Calendar.getInstance();
Helper helper;
    int hours = calendar.get(Calendar.HOUR_OF_DAY);
    int minutes = calendar.get(Calendar.MINUTE);
    int seconds = calendar.get(Calendar.SECOND);
    private Context _context;

    //String msgID="",msgSubject="",msgComment="",msgUrl="",msgDate="",msgRead="",msgType= "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("MYTAG","88888888888888888888888888888888888888888");

        this._context=context;
        helper = new Helper();
        //Toast.makeText(context, "succeed", Toast.LENGTH_LONG).show();
        int len;
        if (helper.isNetworkAvailable(_context)){
            Log.e("MYTAG","hello");
            List<String> listCIDS = new ArrayList<String>();
            listCIDS=helper.getCIDSlist();
            Log.e("MYTAG","hello list cids:" + listCIDS);
            len = listCIDS.size();
            String rr = "";
            int i=0;
            for (String c: listCIDS) {

                File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/client_products/"+"pl_" + c +".txt");
                    if(!myFile.exists()) {
                        helper.CLIENTProductsSync(_context,c);
                    }


//                final String cc = c;
//                File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/client_products/"+"pl_" + cc +".txt");
//                if(!myFile.exists()){
//                    CallSoap cs = new CallSoap(DatabaseHelper.getInstance(context).getValueByKey("URL"));//db.getControlPanel(1).getUrl());
//                    //String response = cs.Call(mac_address, memail, mpass);
//                    try{
//                        String response = cs.get_mgnet_client_items_list(helper.getMacAddr(),c);
//                        String myResponse = response;
//                        myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResponse", "");
//                        myResponse = myResponse.replaceAll("PRODUCTS_CLIENTS_ITEMS_LISTResult=", "Orders:");
//                        myResponse = myResponse.replaceAll(";", "");
//                        helper.writeTextToClientDirectory("client_products","pl_" + cc +".txt", myResponse);
//                        Log.e("MYTAG","pl_" + cc +".txt ---" +myResponse);
//                    }catch(Exception ex){
//                        Log.e("MYTAG",ex.getMessage());
//                    }
//
////                    Model.getInstance().Async_Get_mgnet_client_items_Listener(helper.getMacAddr(),c, new Model.get_mgnet_client_items_Listener() {
////                        @Override
////                        public void onResult(String str) {
////                            Log.e("MYTAG","pl_" + cc +".txt ---" +str);
////                            helper.writeTextToClientDirectory("client_products","pl_" + cc +".txt", str);
////                        };
////                        ;});
//                    i++;
//
//                }


            };
        }else{

        }



//        Model.getInstance().AsyncReminder(helper.getMacAddr(), new Model.ReminderListener() {
//            @Override
//            public void onResult(String str, String str2, int size) {
//                if(size==1){
//                    pushNotification(str,str2);
//                }else{
//                    pushNotification("Wizenet",size+" new messages");
//                }
//            }
//        });
    }


    //###################################
    //get mac address
    //###################################
//    public String getMacAddress() {
//        WifiManager manager = (WifiManager) this._context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress();
//        return address;
//    }

    //###################################
    //WRITE URL TO FILE
    //###################################
//    public void writeTextToFile(String param){
//
//        FileWriter f;
//        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/test_file_service.txt");
//        try {
//            f = new FileWriter(file,true);
//            if(!file.exists()) {
//                file.createNewFile();
//            }
//            f.write("\r\n"+param);
//            f.flush();
//            f.close();
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.e("myTag",e.toString());
//        }
//    }
    public long FileSize(String fileName) {
        File file = new File(fileName);
        long filesize = file.length();
        return  filesize / 1024;
    }

}

