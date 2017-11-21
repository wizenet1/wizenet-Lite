package com.example.user.wizenet2;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;



public class MainActivity extends Activity{
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    //public final String DEMOURL = "http://main.wizenet.co.il/webservices/freelance.asmx";//default
    EditText url;
    Button button;

    //String msgID="",msgSubject="",msgComment="",msgUrl="",msgDate="",msgRead="",msgType= "";
Helper helper;
    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    private TimePicker alarmTimePicker;
    private Context context;
    DatabaseHelper db;
    public static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        boolean flag = isNetworkAvailable(context);


        db = DatabaseHelper.getInstance(getApplicationContext());
        ctx=this;


            try{
                //db.getInstance(getApplicationContext()).addMessage(new Message("e","e","e","www.wizenet.co.il","e","e","e"));
            }catch (Exception e){
                Log.e("myTag",e.toString());
            }
            url = (EditText) findViewById(R.id.edittext);


            if(!DatabaseHelper.getInstance(getApplicationContext()).verification("URL")) {
                DatabaseHelper.getInstance(getApplicationContext()).addControlPanel("username","");
                DatabaseHelper.getInstance(getApplicationContext()).addControlPanel("BACKGROUND","1");
                DatabaseHelper.getInstance(getApplicationContext()).addControlPanel("GPS","0");
                DatabaseHelper.getInstance(getApplicationContext()).addControlPanel("URL","");// url.getText().toString());

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c_week = Calendar.getInstance();
                c_week.add(Calendar.DAY_OF_YEAR, 7);
                String formatted = df.format(c_week.getTime());

                DatabaseHelper.getInstance(getApplicationContext()).addControlPanel("PRODUCTS_UPDATE",formatted);
                DatabaseHelper.getInstance(getApplicationContext()).addControlPanel("CLIENTS_PRODUCTS_UPDATE",formatted);




                Toast.makeText(getBaseContext(),"url is not exists", Toast.LENGTH_SHORT).show();
            }else{
                url.setText(DatabaseHelper.getInstance(getApplicationContext()).getValueByKey("URL"));
                //Toast.makeText(getBaseContext(),"url is exists", Toast.LENGTH_SHORT).show();
            }
            //if file does not exist
            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/wizenet/");
            if (!dir.exists()) {
                dir.mkdir();
            }


            button = (Button) findViewById(R.id.continuebutton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String urlString = url.getText().toString().trim().toLowerCase();
                    DatabaseHelper.getInstance(getApplicationContext()).updateValue("URL",urlString);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }

            });


        if(!db.getValueByKey("URL").equals("")){
            //Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

            if (flag){
                Toast.makeText(getApplicationContext(),"internet valid", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(getApplicationContext(),"internet invalid", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MenuOfflineActivity.class);
                startActivity(intent);
            }


        }
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "MainActivity destroyed...", Toast.LENGTH_LONG).show();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
//        case R.id.add:
//            //add the function to perform here
//            return(true);
//        case R.id.reset:
//            //add the function to perform here
//            return(true);
//        case R.id.about:
//            //add the function to perform here
//            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }



}
