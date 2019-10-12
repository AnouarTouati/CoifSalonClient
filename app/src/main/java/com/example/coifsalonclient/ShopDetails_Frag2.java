package com.example.coifsalonclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class ShopDetails_Frag2 extends Fragment implements  OnMapReadyCallback {
    View view;
    GoogleMap mMap;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.shopdetails_frag2, container,false);
///THE IMPORTANT PART IS TO USE this.getChildFragmentManager() instead of getSupportFragmentManager
        if(ShopDetailsActivity.UsesCoordinates){
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.mapView_Frag2);
            mapFragment.getMapAsync(this);

        }else{
            view.findViewById(R.id.mapView_Frag2).setVisibility(View.GONE);
        }

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng position=new LatLng(ShopDetailsActivity.ShopLatitude, ShopDetailsActivity.ShopLongitude);
        mMap.addMarker(new MarkerOptions().position(position).title(ShopDetailsActivity.ShopNameFromRecyclerView).visible(true)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));//range is 2 to 21
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }
}
