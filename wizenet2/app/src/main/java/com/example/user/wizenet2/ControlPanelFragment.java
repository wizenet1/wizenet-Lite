package com.example.user.wizenet2;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.wizenet2.MenuActivity;
import com.example.user.wizenet2.model.Model;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by User on 11/08/2016.
 */
public class ControlPanelFragment extends android.support.v4.app.Fragment  {

    EditText key_et, val_et;
    //Button addmem_btn,remove_btn;
    DatabaseHelper db;
    ListView myList;
    CustomAdapter adapter;
    List<ControlPanel> data2 = new ArrayList<ControlPanel>() ;
    String dataName;
    CheckBox cb,running_cb;
    LocationManager manager = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = DatabaseHelper.getInstance(getContext());
        View v = inflater.inflate(R.layout.panel_control_fragment, null);
        setHasOptionsMenu(true);
        manager = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        //key_et = (EditText) v.findViewById(R.id.key_et_id);
        //val_et = (EditText) v.findViewById(R.id.value_et_id);
        //remove_btn = (Button) v.findViewById(R.id.btn_remove);
        //addmem_btn = (Button) v.findViewById(R.id.add_btn_id);
        cb = (CheckBox) v.findViewById(R.id.checkBox2);
        running_cb = (CheckBox) v.findViewById(R.id.running_id) ;
        db = DatabaseHelper.getInstance(getContext());


        boolean isBackground = db.getValueByKey("BACKGROUND").equals("1");
        boolean isGPS = db.getValueByKey("GPS").equals("1");


        running_cb.setChecked(isBackground);
        cb.setChecked(isGPS);
        //Log.e("myTag","running_cb:"+ isBackground);
        //Log.e("myTag","cb: "+isGPS);

        running_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!running_cb.isChecked()){ //if true (running)
                    stopService1();
                    db.updateValue("BACKGROUND","0");
                    Toast.makeText(getActivity(),"stop service",Toast.LENGTH_LONG).show();
                    Log.e("myTag","stop service");
                }else{
                    startService();
                   // Intent alarm = new Intent(getContext(), Alarm_Receiver.class);
                    //boolean alarmRunning = (PendingIntent.getBroadcast(getContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
                    //if(alarmRunning == false) {
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarm, 0);
//                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//                    // TODO: 05/09/2016 interval time every X minutes
//                        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
                    db.updateValue("BACKGROUND","1");
                    Log.e("myTag","start service");
                    Toast.makeText(getActivity(),"Start Service",Toast.LENGTH_LONG).show();
                    //}
                }
            }
        });

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb.isChecked() == true) {

                    if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        db.getInstance(getContext()).updateValue("GPS","1");
                        startActivity(intent);

                    }else{
                        db.getInstance(getContext()).updateValue("GPS","1");
                        ((MenuActivity)getActivity()).startRepeatingTask();
                        Toast.makeText(getActivity(),"start tracking",Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }

                }else{
                    db.getInstance(getContext()).updateValue("GPS","0");
                    ((MenuActivity)getActivity()).stopRepeatingTask();
                    Log.e("myTag","stop tracking");
                    Toast.makeText(getActivity(),"stop tracking",Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().popBackStack();

                }
            }
        });

        List<ControlPanel> cps=  db.getAllKeysAndValues();  // getCustomersFromJson(myBundle);
        for (ControlPanel c : cps){
            data2.add(c);
        }
        myList = (ListView) v.findViewById(R.id.panel_control_list);
        adapter = new CustomAdapter();
        myList.setAdapter(adapter);
        //myList.setBackgroundColor(Color.parseColor("#cdebf9"));


        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String u = data2.get(position).toString();
                Toast.makeText(getActivity(), "item click " + u, Toast.LENGTH_LONG).show();

                dataName = "";
            }
        });
        return v;
    };

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        for(int i=0; i<menu.size(); i++){
            menu.getItem(i).setEnabled(false);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
//    private void isServiceRunning(){
//        Intent alarm = new Intent(getContext(), Alarm_Receiver.class);
//        boolean alarmRunning = (PendingIntent.getBroadcast(getContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
//        if(alarmRunning == false) {
//
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("myLog","this is on resume");

        if(!cb.isChecked()) {

        } else if(!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && db.getValueByKey("GPS").equals("0")) {
             //if()
             cb.setChecked(false);
            //_switch.setChecked(false);
             //db.getInstance(getContext()).updateValue("GPS","0");
        }else if(!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && db.getValueByKey("GPS").equals("1")) {
             cb.setChecked(false);
           // _switch.setChecked(false);
             db.getInstance(getContext()).updateValue("GPS", "0");
         }else if(manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && db.getValueByKey("GPS").equals("1")) {
            cb.setChecked(true);
           // _switch.setChecked(true);
            db.getInstance(getContext()).updateValue("GPS", "1");
             ((MenuActivity)getActivity()).startRepeatingTask();
             Log.e("myTag","stop tracking");
    }

    }

    @Override
    public void onStop() {
        super.onStop();
        //if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
         //   db.updateValue("GPS","0");
        //}

    }



    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data2.size();
        }

        @Override
        public Object getItem(int position) {
            return data2.get(position).toString();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.panel_control,null);

            }
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View rowView = inflater.inflate(R.layout.panel_control, parent, false);

            final TextView key = (TextView) convertView.findViewById(R.id.key_text);
            EditText value = (EditText) convertView.findViewById(R.id.value_text);
            //Button remove_btn = (Button) convertView.findViewById(R.id.btn_remove);

            key.setText(data2.get(position).getKey().toString());
            value.setText(data2.get(position).getValue());


            convertView.setTag(position);
            return convertView;
        }
    }

    private void stopService1(){
        Intent intent = new Intent(getContext(), Alarm_Receiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    private void startService(){
        Intent alarm = new Intent(getContext(), Alarm_Receiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            // TODO: 05/09/2016  just note the time is every 5 minutes
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),240000, pendingIntent);
        }
    }
}
