package com.example.user.wizenet2;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.wizenet2.Adapters.CallsAdapter;
import com.example.user.wizenet2.model.Model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ActivityCalls extends FragmentActivity {

    Helper helper ;
    Context ctx;

    DatabaseHelper db;
    ListView myList;
    LocationManager manager = null;
    boolean result = false;
    private EditText mSearchEdt;
    CallsAdapter callsAdapter; //to refresh the list
    ArrayList<Call> data2 = new ArrayList<Call>() ;
    private TextWatcher mSearchTw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ctx = this;
        db = DatabaseHelper.getInstance(getApplicationContext());
        helper= new Helper();
        mSearchEdt = (EditText) findViewById(R.id.mSearchEdt);


        Model.getInstance().Wz_Call_Statuses_Listener(helper.getMacAddr(), new Model.Wz_Call_Statuses_Listener() {
                    @Override
                    public void onResult(String str) {

                    }
                });
        Model.getInstance().Async_Wz_Calls_List_Listener(helper.getMacAddr(), -2, new Model.Wz_Calls_List_Listener() {
            @Override
            public void onResult(String str) {
                //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                //helper.goToCallsFragment(context);
                data2.clear();
                for (Call c : getCallsList()){
                    data2.add(c);
                }
                myList = (ListView) findViewById(R.id.calls_list);

                callsAdapter=new CallsAdapter(data2,getBaseContext());
                myList.setAdapter(callsAdapter);
            }
        });

        //initUI
        Helper helper = new Helper();
        String mac = helper.getMacAddr();

        //initList();


        mSearchTw=new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                callsAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        };
        mSearchEdt.addTextChangedListener(mSearchTw);
    }
    public void initList(){
        data2.clear();
        for (Call c : getCallsList()){
            data2.add(c);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Toast.makeText(getBaseContext(),"onRestart", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(getBaseContext(),"onResume", Toast.LENGTH_SHORT).show();
//    }

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
    private List<Call> getCallsList(){
        JSONObject j = null;
        int length = 0;

        List<Call> calls = new ArrayList<Call>() ;
        try {
            calls= DatabaseHelper.getInstance(this).getCalls();
            length = calls.size();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return calls;
    }

}
