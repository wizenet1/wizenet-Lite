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


public class CustomersAdapter extends BaseAdapter implements Filterable {
    Context c;
    TextView goToTelephone, goToSms,goToCustomers;

    ArrayList<Ccustomer> players;
    CustomFilter filter;
    ArrayList<Ccustomer> filterList;
    public CustomersAdapter(ArrayList<Ccustomer> players,Context ctx) {
        this.c=ctx;
        this.players=players;
        this.filterList=players;
    }
    @Override
    public int getCount() {
        return players.size();
    }
    @Override
    public Object getItem(int pos) {
        return players.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return players.indexOf(getItem(pos));
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
                Uri uri = Uri.parse("smsto:"+players.get(pos).getCcell());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                 c.startActivity(it);
            }
        });
        //#################    TEXTVIEW    ##########################
        TextView nameTxt=(TextView) convertView.findViewById(R.id.textView);
        nameTxt.setText(players.get(pos).getCcompany()+' '+players.get(pos).getCcell());
        convertView.setTag(convertView.getId(),pos);
        convertView.getTag(pos);

        return convertView;
    }
    @Override
    public Filter getFilter() {
        if(filter == null)
        {
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
            players=(ArrayList<Ccustomer>) results.values;
            notifyDataSetChanged();
        }
    }
//    private ArrayList<Ccustomer> mStringList;
//
//    private ArrayList<Ccustomer> mStringFilterList;
//
//    private LayoutInflater mInflater;
//
//    private CustomersAdapter.ValueFilter valueFilter;
//
//    public CustomersAdapter(ArrayList<Ccustomer> mStringList,Context context) {
//
//        this.mStringList=mStringList;
//
//        this.mStringFilterList=mStringList;
//
//        mInflater=LayoutInflater.from(context);
//
//        getFilter();
//    }
//
//    //How many items are in the data set represented by this Adapter.
//    @Override
//    public int getCount() {
//
//        return mStringList.size();
//    }
//
//    //Get the data item associated with the specified position in the data set.
//    @Override
//    public Object getItem(int position) {
//
//        return mStringList.get(position).getCcompany();
//    }
//
//    //Get the row id associated with the specified position in the list.
//    @Override
//    public long getItemId(int position) {
//
//        return position;
//    }
//
//    //Get a View that displays the data at the specified position in the data set.
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        CustomersAdapter.Holder viewHolder = null;
//
//        if(convertView==null) {
//
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            View rowView = inflater.inflate(R.layout.customer, parent, false);
//            TextView name = (TextView) rowView.findViewById(R.id.textView);
//            //convertView = inflater.inflate(R.layout.customer,null);
//            //convertView.setTag(position);
//            viewHolder=new CustomersAdapter.Holder();
//            convertView=mInflater.inflate(R.layout.customer,null);
//            viewHolder.nameTv=(TextView)convertView.findViewById(R.id.textView);
//
//            //viewHolder.nameTv.setText(mStringList.get(position).getCcompany().toString());
//            convertView.setTag(viewHolder);
//
//        }else{
//            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            View rowView = inflater.inflate(R.layout.customer, parent, false);
//            TextView name = (TextView) rowView.findViewById(R.id.textView);
//            convertView = inflater.inflate(R.layout.customer,null);
//            convertView.setTag(position);
//            //viewHolder=new CustomersAdapter.Holder();
//            viewHolder=new CustomersAdapter.Holder();
//            convertView=mInflater.inflate(R.layout.customer,null);
//            viewHolder.nameTv=(TextView)convertView.findViewById(R.id.textView);
//
//            //viewHolder.nameTv.setText(mStringList.get(position).getCcompany().toString());
//            convertView.setTag(viewHolder);
//            //convertView=mInflater.inflate(R.layout.customer,null);
//
//            //viewHolder.nameTv=(TextView)convertView.findViewById(R.id.textView);
//            //viewHolder.nameTv.setText(mStringList.get(position).getCcompany().toString());
//            //viewHolder=(CustomersAdapter.Holder)convertView.getTag();
//
//        }
//
//        //viewHolder.nameTv.setText(mStringList.get(position).getCcompany().toString());
//
//        return convertView;
//    }
//
//    private class  Holder{
//
//        TextView nameTv;
//    }
//
//    //Returns a filter that can be used to constrain data with a filtering pattern.
//    @Override
//    public Filter getFilter() {
//
//        if(valueFilter==null) {
//
//            valueFilter=new CustomersAdapter.ValueFilter();
//        }
//
//        return valueFilter;
//    }
//
//
//    private class ValueFilter extends Filter {
//
//
//        //Invoked in a worker thread to filter the data according to the constraint.
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//
//            FilterResults results=new FilterResults();
//
//            if(constraint!=null && constraint.length()>0){
//
//                ArrayList<String> filterList=new ArrayList<String>();
//
//                for(int i=0;i<mStringFilterList.size();i++){
//
//                    if(mStringFilterList.get(i).getCcompany().contains(constraint)) {
//                        Log.e("TAG","");
//                        filterList.add(mStringList.get(i).getCcompany());
//
//                    }
//                }
//
//
//                results.count=filterList.size();
//
//                results.values=filterList;
//
//            }else{
//
//                results.count=mStringFilterList.size();
//
//                results.values=mStringFilterList;
//
//            }
//
//            return results;
//        }
//
//
//        //Invoked in the UI thread to publish the filtering results in the user interface.
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint,
//                                      FilterResults results) {
//
//            mStringList=(ArrayList<Ccustomer>) results.values;
//
//            notifyDataSetChanged();
//
//
//        }
//
//    }
}