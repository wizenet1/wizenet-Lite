package com.example.user.wizenet2;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WIZE02 on 03/04/2017.
 */

public class MapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragmentmap, container, false);



        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView =(MapView) mView.findViewById(R.id.mymap);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity().getApplicationContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        GPSTracker gps = new GPSTracker(getActivity());
        Location loc = gps.getLocation();
        try{
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(),loc.getLongitude())).title("wizenet"));
            List<Address> addressList = null;

            Geocoder geocoder = new Geocoder(getActivity());

            try{
                addressList = geocoder.getFromLocationName("יוסף נדבה הרצליה", 1);
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Accidents Here"));
                Toast.makeText(getActivity(),"success",Toast.LENGTH_SHORT).show();

                ArrayList<MarkerData> markersArray = new ArrayList<MarkerData>();
                markersArray = v();
                //Toast.makeText(getActivity(),markersArray.get(0).getLatLng().toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),"step 3",Toast.LENGTH_SHORT).show();
                for(int i = 0 ; i < markersArray.size() ; i++ ) {
                    //Log.e("myLog",Double.toString(markersArray.get(i).getLatLng().latitude)+' '+Double.toString(markersArray.get(i).getLatLng().longitude));
                    //Toast.makeText(getActivity(),Double.toString(markersArray.get(i).getLatLng().latitude)+' '+Double.toString(markersArray.get(i).getLatLng().longitude),Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getActivity(),markersArray.get(0).getLatLng().toString(),Toast.LENGTH_SHORT).show();
                    try{
                        createMarker(markersArray.get(i).getLatLng().latitude, markersArray.get(i).getLatLng().longitude, markersArray.get(i).getTitle(), "", 7);
                        //mGoogleMap.addMarker()
                          mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(markersArray.get(i).getLatLng().latitude, markersArray.get(i).getLatLng().longitude))
                                .title("Hello world")
                                .snippet("Nice Place")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));

                    }catch(Exception ex){
                        //Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
                Toast.makeText(getActivity(),"step 4",Toast.LENGTH_SHORT).show();
            }catch (IOException e)
            {
                Toast.makeText(getActivity(),"fail with markersArray",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }catch(Exception ex){
            //mGoogleMa.1.p.addMarker(new MarkerOptions().position(new LatLng(42.547654,40.24574)));
            Toast.makeText(getActivity(),ex.getMessage(),Toast.LENGTH_SHORT).show();
        }

        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(46.789,-40.789)));


    }
    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {

        return mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
            .icon(BitmapDescriptorFactory.fromResource(iconResID)));
    }
    public ArrayList<MarkerData> v(){
        ArrayList<MarkerData> markerDataList = new ArrayList<MarkerData>() ;
        ArrayList<String> data2 = new ArrayList<String>() ;
        String json = "";
        Helper helper = new Helper();
        json = helper.readTextFromFileCustomers();
        //

        JSONObject j = null;
        try {
            j = new JSONObject(json);
            //get the array [...] in json
            JSONArray jarray = j.getJSONArray("Customers");
            //Toast.makeText(getActivity(),jarray.toString(),Toast.LENGTH_SHORT).show();

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject object = jarray.getJSONObject(i);
                String address = jarray.getJSONObject(i).getString("address");

                data2.add(address);
            }
        } catch (JSONException e1) {
            Toast.makeText(getActivity(),"fail to loop addresses",Toast.LENGTH_SHORT).show();
            e1.printStackTrace();
        }

        Geocoder geocoder = new Geocoder(getActivity());
        for (String address1:data2) {
            List<Address> addressList = null;
            try {
                addressList = geocoder.getFromLocationName(address1, 1);
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                //String b = String.valueOf(address.getLatitude())+" "+String.valueOf(address.getLongitude());
                //Toast.makeText(getActivity(),b,Toast.LENGTH_SHORT).show();
                markerDataList.add(new MarkerData(latLng,"1"));
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return  markerDataList;
    }


    public class MarkerData {
        LatLng latLng;
        String title;

        public MarkerData(LatLng latLng, String s) {
            this.latLng = latLng;
            this.title = s;
        }
        //Bitmap bitmap;

        public LatLng getLatLng() {
            return latLng;
        }

        public void setLatLng(LatLng latLng) {
            this.latLng = latLng;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

//        public Bitmap getBitmap() {
//            return bitmap;
//        }
//
//        public void setBitmap(Bitmap bitmap) {
//            this.bitmap = bitmap;
//        }
    }
}
