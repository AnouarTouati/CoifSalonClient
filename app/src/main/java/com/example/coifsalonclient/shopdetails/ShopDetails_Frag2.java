package com.example.coifsalonclient.shopdetails;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coifsalonclient.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShopDetails_Frag2 extends Fragment implements  OnMapReadyCallback {

    View view;
    GoogleMap mMap;
    TextView shopPhoneNumber;
    ShopDetailsActivity shopDetailsActivity;
    public ShopDetails_Frag2(ShopDetailsActivity shopDetailsActivity){
        this.shopDetailsActivity=shopDetailsActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.shopdetails_frag2, container,false);

        if(shopDetailsActivity.aShop.getUsesCoordinates()){
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapView_Frag2);
            try {
                mapFragment.getMapAsync(this);
            }catch (Exception e){
                Log.e("MyFirebase","Error Occurred while loading maps "+e);
                Toast.makeText(shopDetailsActivity, "Error Occurred while loading maps", Toast.LENGTH_LONG).show();
                view.findViewById(R.id.mapView_Frag2).setVisibility(View.GONE);
            }

        }else{
            view.findViewById(R.id.mapView_Frag2).setVisibility(View.GONE);
        }
        shopPhoneNumber=view.findViewById(R.id.shopPhoneNumber_Frag3);
        shopPhoneNumber.setText(shopDetailsActivity.aShop.getShopPhoneNumber());
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng position=new LatLng(shopDetailsActivity.aShop.getShopLatitude(), shopDetailsActivity.aShop.getShopLongitude());
        mMap.addMarker(new MarkerOptions().position(position).title(shopDetailsActivity.aShop.getShopName()).visible(true)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));//range is 2 to 21
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }
}
