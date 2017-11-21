package com.example.user.wizenet2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31/08/2016.
 */
public class FragmentCalls extends android.support.v4.app.Fragment {


    EditText key_et, val_et;
    Button addmem_btn,remove_btn;
    DatabaseHelper db;
    ListView myList;
    CustomAdapter adapter;
    List<Message> data2 = new ArrayList<Message>() ;
    String dataName;
    CheckBox cb;
    LocationManager manager = null;
    String firstname="";
    String lastname="";
    boolean result = false;
    ListView myList; //mSearchNFilterLv
    private EditText mSearchEdt;
    CustomersAdapter customersAdapter; //to refresh the list
    ArrayList<Ccustomer> data2 = new ArrayList<Ccustomer>() ;
    private TextWatcher mSearchTw;
    //CustomersAdapter customersAdapter;
    String dataName,myString;
    //ImageButton goToTelephone, goToSms;
    TextView goToTelephone, goToSms;
    //EditText customer_search;
    FragmentCustomer.CustomAdapter adapter; //for listview here
    String myBundle= "";


            calls_list
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//
//        ListView myList; //mSearchNFilterLv
//        private EditText mSearchEdt;
//        CustomersAdapter customersAdapter; //to refresh the list
//        ArrayList<Ccustomer> data2 = new ArrayList<Ccustomer>() ;
//        private TextWatcher mSearchTw;
////CustomersAdapter customersAdapter;
//        String dataName,myString;
//        //ImageButton goToTelephone, goToSms;
//        TextView goToTelephone, goToSms;
//        //EditText customer_search;
//        FragmentCustomer.CustomAdapter adapter; //for listview here
//        String myBundle= "";
//        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.customer_fragment, null);
            mSearchEdt = (EditText) v.findViewById(R.id.mSearchEdt);
            //initUI
            Helper helper = new Helper();
            String mac = helper.getMacAddr();

            data2.clear();
            for (Ccustomer c : getCustomerList()){
                data2.add(c);
                //data2.add(c.getCfname()+" "+c.getClname()+" "+c.getCcell());
            }
            myList = (ListView) v.findViewById(R.id.customer_list);
            //adapter = new CustomAdapter();
            //myList.setAdapter(adapter);

//        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String u = data2.get(position).toString();
//                //Toast.makeText(getActivity(), "item click " + u, Toast.LENGTH_LONG).show();
//                alertDialog(data2.get(position));
//                dataName = "";
//            }
//        });
            //init

            customersAdapter=new CustomersAdapter(data2,getContext());
            myList.setAdapter(customersAdapter);
            mSearchTw=new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    customersAdapter.getFilter().filter(s);
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
            return v;
        };
//     for(Datapoint d : dataPointList){
//        if(d.getName() != null && d.getName().contains(search))
//        //something here
//    }
        private Ccustomer[] getCustomerList(){
            Helper helper = new Helper();
            myString=helper.readTextFromFileCustomers();
            JSONObject j = null;
            int length = 0;
            Ccustomer[] ccustomers ;//= new Ccustomer[5];
            try {
                j = new JSONObject(myString);
                JSONArray jarray = j.getJSONArray("Customers");
                length = jarray.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ccustomers = new Ccustomer[length];
            ccustomers=     helper.getCustomersFromJson2(myString);
            return ccustomers;
        }



}
