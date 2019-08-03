package com.example.coifsalonclient;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ShopDetailsActivity extends AppCompatActivity {
    static Response.Listener<JSONObject> volleyListener;
    static Response.ErrorListener volleyErrorListener;
    static RequestQueue requestQueue;

    ViewPager viewPager;
    TabLayout tabLayout;
    CustomFragmentPagerAdapter  customFragmentPagerAdapter;


    String ShopNameFromRecyclerView;

    static final String URL="";

   static Context mContext;

    public String EmailAddress;
    public String Password;
    public String FirstName;
    public String LastName;
    public String PhoneNumber;
    public Boolean isEmployee=false;
    public Boolean isBusinessOwner=false;
    public String SalonName;
    public String SelectedState;
    public String SelectedCommune;
    public Boolean UseCoordinatesAKAaddMap=false;
    public Double StoreLatitude;
    public Double StoreLongitude;
    public Boolean isMen=true;
    public Bitmap SelectedImage;
    public String StorePhoneNumber;
    public String FacebookLink;
    public String InstagramLink;
    public Boolean Coiffure=false;
    public Boolean MakeUp=false;
    public Boolean Meches=false;
    public Boolean Tinte=false;
    public Boolean Pedcure=false;
    public Boolean Manage=false;
    public Boolean Manicure=false;
    public Boolean Coupe=false;
    public String Saturday;
    public String Sunday;
    public String Monday;
    public String Tuesday;
    public String Wednesday;
    public String Thursday;
    public String Friday;

    public static  ArrayList<String> ServicesHairCutsNames=new ArrayList<>();
    public static ArrayList<String> ServicesHairCutsPrices=new ArrayList<>();
    public static  ArrayList<String> ServicesHairCutsDuration=new ArrayList<>();
    public static String successfullyBookedServicesHaircut=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        mContext=this;
        volleyErrorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        volleyListener=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String subject="";
                try {
                   subject=response.getString("Subject");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(subject.equals("StoreInfo")){
                    ServerResponseWithStoreInfo(response);
                }else if(subject.equals("Booking")){
                    ServerResponseWithBookingResult(response);
                }

            }

        };
       Toast.makeText(this,""+ getIntent().getIntExtra("ShopIndexInRecycler",22),Toast.LENGTH_LONG).show();
        ShopNameFromRecyclerView=getIntent().getStringExtra("ShopName");
       GetStoreInfo(ShopNameFromRecyclerView);


        // we might want to use the code below after we receive the data
        viewPager=findViewById(R.id.viewPagerShopDetails);
        customFragmentPagerAdapter=new CustomFragmentPagerAdapter(getSupportFragmentManager());
        customFragmentPagerAdapter.addFragment(new ShopDetails_Frag1(), "ShopDetails_Frag1");
        customFragmentPagerAdapter.addFragment(new ShopDetails_Frag2(), "ShopDetails_Frag2");
        viewPager.setAdapter(customFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tabLayout.getTabAt(i).select();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout=findViewById(R.id.tabLayoutShopDetails);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    void GetStoreInfo(String shopName){

        ServicesHairCutsNames.add("Haircut1");
        ServicesHairCutsNames.add("Haircut2");
        ServicesHairCutsNames.add("Haircut3");
        ServicesHairCutsNames.add("Haircut4");
        ServicesHairCutsNames.add("Haircut5");
        ServicesHairCutsNames.add("Haircut6");
        ServicesHairCutsNames.add("Haircut7");

        ServicesHairCutsPrices.add("200");
        ServicesHairCutsPrices.add("400");
        ServicesHairCutsPrices.add("600");
        ServicesHairCutsPrices.add("700");
        ServicesHairCutsPrices.add("800");
        ServicesHairCutsPrices.add("900");
        ServicesHairCutsPrices.add("1000");

        ServicesHairCutsDuration.add("30");
        ServicesHairCutsDuration.add("40");
        ServicesHairCutsDuration.add("50");
        ServicesHairCutsDuration.add("60");
        ServicesHairCutsDuration.add("70");
        ServicesHairCutsDuration.add("80");
        ServicesHairCutsDuration.add("90");
        /*
      JSONObject getStoreInfo=new JSONObject();
        try {
            getStoreInfo.put("RequestStoreInfo_StoreName", shopName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, getStoreInfo, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);*/
    }
    void ServerResponseWithStoreInfo(JSONObject result){

        try {

            EmailAddress  = result.getString("EmailAddress");
            Password    = result.getString("Password");
            FirstName= result.getString("FirstName");
            LastName     = result.getString("LastName");
            PhoneNumber  = result.getString("PhoneNumber");
            isBusinessOwner  = result.getBoolean("isBusinessOwner");
            SalonName  = result.getString("SalonName");
            SelectedState   = result.getString("SelectedState");
            SelectedCommune  = result.getString("SelectedCommune");
            UseCoordinatesAKAaddMap     = result.getBoolean("UseCoordinatesAKAaddMap");
            StoreLatitude   = result.getDouble("StoreLatitude");
            StoreLongitude   = result.getDouble("StoreLongitude");
            isMen  = result.getBoolean("isMen");
            SelectedImage   = ConvertStringToBitmap(result.getString("SelectedImage"));
            StorePhoneNumber   = result.getString("StorePhoneNumber");
            FacebookLink  = result.getString("FacebookLink");
            InstagramLink  = result.getString("InstagramLink");
            Coiffure    = result.getBoolean("Coiffure");
            Meches = result.getBoolean("Meches");
            Tinte  = result.getBoolean("Tinte");
            Pedcure = result.getBoolean("Pedcure");
            Manage = result.getBoolean("Manage");
            Manicure = result.getBoolean("Manicure");
            Coupe = result.getBoolean("Coupe");
            Saturday = result.getString("Saturday");
            Sunday = result.getString("Sunday");
            Monday  = result.getString("Monday");
            Tuesday  = result.getString("Tuesday");
            Wednesday = result.getString("Wednesday");
            Thursday = result.getString("Thursday");
            Friday = result.getString("Friday");

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
    void ServerResponseWithBookingResult(JSONObject result){
        String bookingResult="";
        try {
           bookingResult =result.getString("BookingResult");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(bookingResult.equals("OK")){
            ShopDetails_Frag1.BookWasSuccessfulNorifyRecyclerViewAdapter();
        }else{
            //turn button red or something
        }

    }
    public static void book(final String ServiceHairCutToReserve){

        successfullyBookedServicesHaircut=ServiceHairCutToReserve;
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Reserve Confirmation");
        alertDialogBuilder.setMessage("Do you want to book for "+ServiceHairCutToReserve+" ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, ServiceHairCutToReserve, Toast.LENGTH_LONG).show();


            }
        });
       alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               Toast.makeText(mContext, "We didnt book any thing", Toast.LENGTH_LONG).show();

           }
       });
       alertDialogBuilder.create().show();

        /*
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("RequestBook",ServiceHairCutToReserve);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, jsonObject, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);*/
    }

   Bitmap ConvertStringToBitmap(String image){
       byte[] bytes;
       bytes=Base64.decode(image, Base64.DEFAULT);
      return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
   }

}
