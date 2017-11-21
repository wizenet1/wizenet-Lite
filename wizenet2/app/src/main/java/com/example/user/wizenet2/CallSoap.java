package com.example.user.wizenet2;


import android.content.Entity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by doron on 05/02/2016.
 */
public class CallSoap {
    //#############operation name######################
    public  final String USER_Login = "USER_Login";
    public  final String USER_Clients = "USER_Clients";
    public  final String USER_Status = "USER_Status";
    public  final String REMINDERS_retAlert = "REMINDERS_retAlert";
    public  final String USER_Details = "USER_Details";
    public  final String USER_getClientsContacts = "getClientsContacts";
    public  final String ORDER_getMgnetItemsList = "PRODUCTS_ITEMS_LIST";
    public  final String CREATE_OFFLINE = "CREATE_OFFLINE";
    public  final String ORDER_getMgnetClientItemsList = "PRODUCTS_CLIENTS_ITEMS_LIST";
    public  final String CALLS_List = "CALLS_List";

    //#############name space######################
    public  final String NAMESPACE = "http://tempuri.org/";
    //#############SOAP ACTION######################
    public final String USER_Login_SOAP_ACTION = "http://tempuri.org/USER_Login";
    public final String USER_Clients_SOAP_ACTION2 = "http://tempuri.org/USER_Clients";
    public final String USER_Status_SOAP_ACTION3 = "http://tempuri.org/USER_Status";
    public final String REMINDERS_retAlert_SOAP_ACTION4 = "http://tempuri.org/REMINDERS_retAlert";
    public final String USER_Details_SOAP_ACTION5 = "http://tempuri.org/USER_Details";
    public final String USER_getClientsContacts_SOAP_ACTION6 = "http://tempuri.org/getClientsContacts";
    public final String ORDER_getMgnetItemsList_SOAP_ACTION7 = "http://tempuri.org/PRODUCTS_ITEMS_LIST";
    public final String CREATE_OFFLINE_SOAP_ACTION8 = "http://tempuri.org/CREATE_OFFLINE";
    public final String ORDER_getMgnetClientItemsList_SOAP_ACTION9 = "http://tempuri.org/PRODUCTS_CLIENTS_ITEMS_LIST";
    public final String CALLS_List_SOAP_ACTION10 = "http://tempuri.org/CALLS_List";

    //public  final String URL = "http://main.wizenet.co.il/webservices/freelance.asmx";
    public String URL;

    //public  final String URL =readTextFromFile();
    String suffix = "/webservices/freelance.asmx";

    //URL is transfer because i cant pass the getInstance to here
    public CallSoap(String url)
    {
        this.URL = url+suffix;//db.getControlPanel(1).getUrl();
    }
    //###################################
    //LOGIN CALL
    //###################################
    //region login
    public String Call(String mac_address, String email, String pass)
    {
        SoapObject request = new SoapObject(NAMESPACE,USER_Login);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("Email",email);
        request.addProperty("Pass",pass);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        Object response=null;
        try
        {
            httpTransport.call(USER_Login_SOAP_ACTION, envelope);
            response = envelope.bodyIn;
        }catch (Exception exception)
        {
            //response=exception.toString();
        }
        try{
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "incorrect";
        }



    }
//endregion

//////////REMINDER///////////
//region reminder
public String Call4(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE,REMINDERS_retAlert);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(REMINDERS_retAlert_SOAP_ACTION4, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {}
        try{
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }
    //endregion
    //////////REMINDER///////////
//region user_details
    public String Call_USER_Details(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE,USER_Details);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(USER_Details_SOAP_ACTION5, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {}
        try{
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "Error";
        }
    }
    //endregion
    //###################################
    //CUSTOMERS CALL
    //###################################
    //region clients
    public String Call2(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE,USER_Clients);//namespace , operation
        PropertyInfo pi=new PropertyInfo();
        pi.setName("MACaddress");
        pi.setValue(mac_address);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;

        try
        {
            httpTransport.call(USER_Clients_SOAP_ACTION2, envelope);
            SoapObject result = (SoapObject)envelope.bodyIn;
            response = result;
        }
        catch (Exception exception)
        {
            //response=exception.toString();
        }
        return response.toString();
    }
    //endregion
    //###################################
    //STATUS CALL
    //###################################
//region status
    public String Call3(String mac_address, String longtitude, String latitude)
    {
        SoapObject request = new SoapObject(NAMESPACE,USER_Status);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("latitude",latitude);
        request.addProperty("longitude",longtitude);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(USER_Status_SOAP_ACTION3, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            //response=exception.toString();
        }
        return response.toString();

    }
//endregion

    //region clientsContacts
    public String Call_getClientsContacts(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE, USER_getClientsContacts);//namespace , operation
        request.addProperty("MACaddress",mac_address);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(USER_getClientsContacts_SOAP_ACTION6, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
//endregion

    //region get item_order products
    public String getOrdersProducts(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE, ORDER_getMgnetItemsList);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("PcatID",-1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(ORDER_getMgnetItemsList_SOAP_ACTION7, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
//endregion


    //region client items
    public String get_mgnet_client_items_list(String mac_address,String cid)
    {
        SoapObject request = new SoapObject(NAMESPACE, ORDER_getMgnetClientItemsList);//namespace , operation



        request.addProperty("MACaddress",mac_address);

        request.addProperty("CardCode",cid);

        request.addProperty("PcatID",-1);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(ORDER_getMgnetClientItemsList_SOAP_ACTION9, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    //endregion

    //region create_offline
    public String CREATE_OFFLINE(String mac_address,String json)
    {
        SoapObject request = new SoapObject(NAMESPACE, CREATE_OFFLINE);//namespace , operation
        request.addProperty("MACaddress",mac_address);
        request.addProperty("json",json);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(CREATE_OFFLINE_SOAP_ACTION8, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
//endregion

    //region create_offline
    public String GET_CALLS_LIST(String mac_address)
    {
        SoapObject request = new SoapObject(NAMESPACE, CALLS_List);//namespace , operation
        request.addProperty("MACaddress",mac_address);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        Object response=null;
        try
        {
            httpTransport.call(CALLS_List_SOAP_ACTION10, envelope);
            response = envelope.bodyIn;
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();

    }
//endregion


    public String readTextFromFile() {
        String ret = "";

        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/wizenet/myurl.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            ret = aBuffer.toString().trim();
            myReader.close();
            //Toast.makeText(, ret, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            InputStream inputStream = openFileInput("myurl.txt");
//
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ((receiveString = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(receiveString);
//                }
//
//                inputStream.close();
//                ret = stringBuilder.toString();
//                Toast.makeText(getApplicationContext(), ret.toString(), Toast.LENGTH_LONG).show();


//        } catch (FileNotFoundException e) {
//            Log.e(TAG, "File not found: " + e.toString());
//            return false;
//        } catch (IOException e) {
//            Log.e(TAG, "Can not read file: " + e.toString());
//            return false;
//        }
        return ret;
    }

}
