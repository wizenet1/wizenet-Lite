package com.example.user.wizenet2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by WIZE02 on 23/05/2017.
 */

/**
 * the position of adapter is to set the content into listview
 * with the paramters we pass it.
 *  ccustomerArrayList - is the list of customers
 *  ctx - the context we must pass it (relationship between classes and fragments/activities)
 *  Filterable is the additional built in interface that's allow us to implement the filter edit text
 */
public class CustomersAdapter extends BaseAdapter implements Filterable {
    Context c;
    TextView goToTelephone, goToSms,goToCustomers;

    ArrayList<Ccustomer> ccustomerArrayList;
    CustomFilter filter;
    ArrayList<Ccustomer> filterList;
    public CustomersAdapter(ArrayList<Ccustomer> ccustomerArrayList,Context ctx) {
        this.c=ctx;
        this.ccustomerArrayList=ccustomerArrayList;
        this.filterList=ccustomerArrayList;
    }
    @Override
    public int getCount() {
        return ccustomerArrayList.size();
    }
    @Override
    public Object getItem(int pos) {
        return ccustomerArrayList.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return ccustomerArrayList.indexOf(getItem(pos));
    }
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        Icon_Manager icon_manager;
        icon_manager = new Icon_Manager();
        LayoutInflater inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.customer, null);
            convertView.getTag(pos);
        }
        //###################### TELEPHONE #############################
        goToTelephone = (TextView) convertView.findViewById(R.id.call);
        //id1 = (TextView) v.findViewById(R.id.id1);
        goToTelephone.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));

        goToTelephone.setTextSize(30);
        goToTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "0526561633"));
                c.startActivity(callIntent);
            }
        });
        //###################### SMS #############################
        goToSms = (TextView) convertView.findViewById(R.id.sendsms);
        goToSms.setTypeface(icon_manager.get_Icons("fonts/ionicons.ttf",c));
        goToSms.setTextSize(30);
        goToSms.setTag(pos);
        goToSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:"+ccustomerArrayList.get(pos).getCcell());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                 c.startActivity(it);
            }
        });
        //#################    TEXTVIEW    ##########################
        TextView nameTxt=(TextView) convertView.findViewById(R.id.textView);
        nameTxt.setText(ccustomerArrayList.get(pos).getCcompany()+' '+ccustomerArrayList.get(pos).getCcell());
        convertView.setTag(convertView.getId(),pos);
        convertView.getTag(pos);

        return convertView;
    }
    @Override
    public Filter getFilter() {
        if(filter == null)
        {
            /**
             * call the filter class to return the correct filtered list
             */
            filter=new CustomFilter();
        }
        return filter;
    }
    //INNER CLASS
    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint != null && constraint.length()>0)
            {
                //CONSTARINT TO UPPER
                constraint=constraint.toString().toUpperCase();
                ArrayList<Ccustomer> filters=new ArrayList<Ccustomer>();
                //get specific items

                /**
                 * here i loop the customers list that i've passed before
                 * and filter by chars i type in edittext
                 */
                for(int i=0;i<filterList.size();i++)
                {
                    if(filterList.get(i).getCcompany().toUpperCase().contains(constraint))
                    {
                        Ccustomer p=new Ccustomer("","",filterList.get(i).getCemail(),"",filterList.get(i).getCcell(),filterList.get(i).getCcompany(),filterList.get(i).getCID());
                        //p= new Ccustomer()
                        filters.add(p);
                    }
                }
                results.count=filters.size();
                results.values=filters;
            }else
            {
                results.count=filterList.size();
                results.values=filterList;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            /**
             * what are you going to see in the results
             */
            ccustomerArrayList=(ArrayList<Ccustomer>) results.values;
            //update the listview
            notifyDataSetChanged();
        }
    }

}