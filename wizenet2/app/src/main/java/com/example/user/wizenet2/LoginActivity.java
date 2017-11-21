package com.example.user.wizenet2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.wizenet2.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class LoginActivity extends FragmentActivity {

    Helper helper ;
    DatabaseHelper db;

    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    String memail, mpass, mac_address;
    private TextView sign_in,reset,view_url;
    EditText email, pass;
    Button write, read;
    GPSTracker gps = null;
    Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        db = DatabaseHelper.getInstance(getApplicationContext());
        helper= new Helper();
        setContentView(R.layout.activity_login);
        boolean flag = helper.isNetworkAvailable(ctx);

        if (flag){
            Toast.makeText(getApplicationContext(),"internet valid", Toast.LENGTH_LONG).show();


        }else{
            Toast.makeText(getApplicationContext(),"internet invalid", Toast.LENGTH_LONG).show();
            goToOfflineFragment();
        }

        view_url = (TextView) findViewById(R.id.button4);
        sign_in = (TextView) findViewById(R.id.sign_in_button);
        reset = (TextView) findViewById(R.id.reset_button);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        try{
            if(!db.getValueByKey("username").equals("")){
                email.setText(db.getValueByKey("username").toString());
            }





        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                memail = email.getText().toString();
                mpass = pass.getText().toString();
                mac_address = helper.getMacAddr();//

                if (checkEmail(memail)) {

                    //Toast.makeText(getApplicationContext(),"mac_address:" + mac_address, Toast.LENGTH_LONG).show();

                    try{
                        Model.getInstance().AsyncLogin(mac_address, memail, mpass, new Model.LoginListener() {
                            @Override
                            public void onResult(String str) {
                                if(str.equals("incorrect")){
                                    Toast.makeText(getApplicationContext(), "incorrect URL", Toast.LENGTH_LONG).show();
                                }else if(str.equals("1")){
                                    //chk if is not the same user,
                                    //if is not the same user - delete files because they not belong him.
                                    Log.e("MYTAG","db:" +db.getValueByKey("username").toString()+ " memail:" + memail);
                                    if (!db.getValueByKey("username").toString().equals(memail)) {
                                        helper.deleteAllFiles();
                                    }
                                    db.updateValue("username",memail);
                                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(), "username or password incorrect", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }catch(Exception ex){
                        Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "invalid mail , try again", Toast.LENGTH_LONG).show();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        view_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), db.getValueByKey("URL").toString(), Toast.LENGTH_LONG).show();
            }
        });
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }
    public void goToOfflineFragment(){
        //Bundle bundle=new Bundle();
        //bundle.putString("name", strBundle);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        FragmentMenuOffline frag = new FragmentMenuOffline();
        //frag.setArguments(bundle);
        ft.replace(R.id.container,frag,"FragmentMenuOffline");

        ft.addToBackStack("FragmentMenuOffline");
        ft.commit();
    }

    //###################################
//CHECK EMAIL
//###################################
    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }



    //###################################
    //get mac address
    //###################################
//    public String getMacAddress() {
//        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress();
//        return address;
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.add:
//                //add the function to perform here
//                return (true);
            //case R.id.action_filter:
            //add the function to perform here
            // return (true);
//            case R.id.about:
//                //add the function to perform here
//                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }
//    public static String getAdressMacByInterface(){
//        try {
//            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface nif : all) {
//                if (nif.getName().equalsIgnoreCase("wlan0")) {
//                    byte[] macBytes = nif.getHardwareAddress();
//                    if (macBytes == null) {
//                        return "";
//                    }
//
//                    StringBuilder res1 = new StringBuilder();
//                    for (byte b : macBytes) {
//                        res1.append(String.format("%02X:",b));
//                    }
//
//                    if (res1.length() > 0) {
//                        res1.deleteCharAt(res1.length() - 1);
//                    }
//                    return res1.toString();
//                }
//            }
//
//        } catch (Exception e) {
//            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
//        }
//        return null;
//    }
//    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
//        String ret;
//        int wifiState = wifiMan.getWifiState();
//
//        wifiMan.setWifiEnabled(true);
//        File fl = new File(fileAddressMac);
//        FileInputStream fin = new FileInputStream(fl);
//        ret = convertStreamToString(fin);
//        fin.close();
//
//        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
//        wifiMan.setWifiEnabled(enabled);
//        return ret;
//    }
//    static String convertStreamToString(java.io.InputStream is) {
//        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
//        return s.hasNext() ? s.next() : "";
//    }

}
