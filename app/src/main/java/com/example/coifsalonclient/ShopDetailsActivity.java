package com.example.coifsalonclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopDetailsActivity extends FragmentActivity {
    static Response.Listener<JSONObject> volleyListener;
    static Response.ErrorListener volleyErrorListener;
    static RequestQueue requestQueue;
    static final String URL = "http://192.168.43.139:8888/Client.php";

    ViewPager viewPager;
    TabLayout tabLayout;
    CustomFragmentPagerAdapter customFragmentPagerAdapter;


    public static String ShopNameFromRecyclerView;
    public String ShopMainImageForMainActivityLinkFromRecyclerView;


    static Context mContext;


    public String EmailAddress;
    public String Password;
    public String FirstName;
    public String LastName;
    public String PhoneNumber;
    public Boolean isEmployee = false;
    public Boolean isBusinessOwner = false;
    public String SalonName;
    public String SelectedState;
    public String SelectedCommune;
    public static Boolean UsesCoordinates = false;
    public static Double ShopLatitude;
    public static Double ShopLongitude;
    public Boolean isMen = true;
    public String ShopPhoneNumber;
    public String FacebookLink;
    public String InstagramLink;
    public Boolean Coiffure = false;
    public Boolean MakeUp = false;
    public Boolean Meches = false;
    public Boolean Tinte = false;
    public Boolean Pedcure = false;
    public Boolean Manage = false;
    public Boolean Manicure = false;
    public Boolean Coupe = false;
    public String Saturday;
    public String Sunday;
    public String Monday;
    public String Tuesday;
    public String Wednesday;
    public String Thursday;
    public String Friday;


    //////////////////////////////////////////////////////////////////////////////
    //FRAGMENT 1 SERVICES
    public static ArrayList<String> ServicesHairCutsNames = new ArrayList<>();
    public static ArrayList<String> ServicesHairCutsPrices = new ArrayList<>();
    public static ArrayList<String> ServicesHairCutsDuration = new ArrayList<>();
    public static String successfullyBookedHaircut = null;
    public static String successfullyBookedShop = null;
    ///////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
    //FRAGMENT 3 REVIEWS
    public static ArrayList<String> reviewersNames = new ArrayList<>();
    public static ArrayList<String> reviewersComments = new ArrayList<>();
    public static ArrayList<String> reviewersCommentDate = new ArrayList<>();
    public static ArrayList<Float> reviewersGivenStars = new ArrayList<>();
    ///////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    //FRAGMENT 4 PORTFOLIO
    ArrayList<String> ImagesLinkFromRecyclerView = new ArrayList<>();
   public static ArrayList<Bitmap> PortfolioImages = new ArrayList<>();//the first image is the main shop image
    ArrayList<String> PortfolioImagesAsStrings = new ArrayList<>();
    ArrayList<String> PortfolioImagesLinks = new ArrayList<>();//we use this in local memory so we are sure that we received the image pointed to by the link
    Integer IndexOfImageToReceiveNext =0;
    ArrayList<String> PortfolioImagesLinksToBeRequested = new ArrayList<>();
    //////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        mContext = this;

        volleyErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("VolleyErrors", "onErrorResponse: IN SHOPDETAILS ACTIVITY " + error.toString());
            }
        };
        volleyListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("VolleyReceived", "IN SHOP DETAILS ACTIVITY" + response.toString());


                if (response.has("ShopDetailsInfo")) {
                    try {
                        ServerResponseWithShopInfo(response.getJSONObject("ShopDetailsInfo"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response.has("BookResult")) {
                    try {
                        ServerResponseWithBookingResult(response.getJSONObject("BookResult"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (response.has("ReviewResult")) {
                    try {
                        ServerResponseWithAddReviewResult(response.getJSONObject("ReviewResult"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }


        };

        ShopNameFromRecyclerView = getIntent().getStringExtra("ShopName");
        ImagesLinkFromRecyclerView = getIntent().getStringArrayListExtra("ImagesLinks");//we get the links from mainactivity because when we load from cache we check the links
        successfullyBookedShop =getIntent().getStringExtra("successfullyBookedShop");
        successfullyBookedHaircut =getIntent().getStringExtra("successfullyBookedHaircut");
        requestQueue = Volley.newRequestQueue(this);
        LoadLocalData(ShopNameFromRecyclerView);

    }

    void WeGotTheDataDisplayIt() {

        viewPager = findViewById(R.id.viewPagerShopDetails);

        customFragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        customFragmentPagerAdapter.addFragment(new ShopDetails_Frag1(), "ShopDetails_Frag1");
        customFragmentPagerAdapter.addFragment(new ShopDetails_Frag2(), "ShopDetails_Frag2");
        customFragmentPagerAdapter.addFragment(new ShopDetails_Frag3(), "ShopDetails_Frag3");
        customFragmentPagerAdapter.addFragment(new ShopDetails_Frag4(), "ShopDetails_Frag4");
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
        tabLayout = findViewById(R.id.tabLayoutShopDetails);

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

    public static void GetShopServicesInfo(String shopName) {
/*
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
        //"the function below" even though it is intended  for Book response it also usable for info since it updates every thing
        ShopDetails_Frag1.BookWasSuccessfulNorifyRecyclerViewAdapter();
*/
/*
      JSONObject getShopInfo=new JSONObject();
        try {
            getShopInfo.put("RequestShopServicesInfo_ShopName", shopName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, getShopInfo, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);*/
    }

    public static void GetShopReviewsInfo(String shopName) {
/*
        reviewersNames.add("Anouar Touati");
        reviewersNames.add("Marouan Touati");
        reviewersNames.add("Fares Karim");
        reviewersNames.add("Said Nacer");

        reviewersComments.add("Good");
        reviewersComments.add("Bad");
        reviewersComments.add("Exclent");
        reviewersComments.add("Poor");

        reviewersCommentDate.add("2019/06/02");
        reviewersCommentDate.add("2018/04/15");
        reviewersCommentDate.add("2019/11/09");
        reviewersCommentDate.add("2019/04/13");

        reviewersGivenStars.add(4);
        reviewersGivenStars.add(5);
        reviewersGivenStars.add(2);
        reviewersGivenStars.add(3);
        ShopDetails_Frag3.ReceivedReviewersInfoNotifyRecyclerViewAdapter();
*/
/*
        JSONObject getShopInfo=new JSONObject();
        try {
            getShopInfo.put("RequestShopReviewsInfo_ShopName", shopName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, getShopInfo, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);*/
    }

    void ServerResponseWithShopInfo(JSONObject ShopDetailsInfo) {
        // dont change the order of these two functions
        WriteNewShopDataToLocalMemory(ShopDetailsInfo);//this should be here to save some info without images and it will be called again when download of images is done
        UseTheAcquiredData(ShopDetailsInfo,true);



    }

    void ServerResponseWithBookingResult(JSONObject BookResult) {


        try {
            if (BookResult.getString("Successful").equals("true")) {
                successfullyBookedHaircut = BookResult.getString("ServicesHairCutToReserve");
                successfullyBookedShop = BookResult.getString("BookedShop");
                ShopDetails_Frag1.BookWasSuccessfulNorifyRecyclerViewAdapter();
            } else {
                //turn button red or something
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void Book(final String ServicesHairCutToReserve) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Reserve Confirmation");
        alertDialogBuilder.setMessage("Do you want to Book for " + ServicesHairCutToReserve + " ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, ServicesHairCutToReserve, Toast.LENGTH_LONG).show();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Request", "Book");
                    jsonObject.put("ServicesHairCutToReserve", ServicesHairCutToReserve);
                    jsonObject.put("ShopNameToBook", ShopNameFromRecyclerView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, volleyListener, volleyErrorListener);
                requestQueue.add(jsonObjectRequest);

            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, "We didnt Book any thing", Toast.LENGTH_LONG).show();

            }
        });
        alertDialogBuilder.create().show();


    }
    public void AddReview(String ReviewerName,String ReviewerComment,float ReviewerGivenStars){

        Map<String,Object> map=new HashMap<>();
        map.put("Request","GiveReview");
        map.put("ReviewerName",ReviewerName);
        map.put("ReviewerComment",ReviewerComment);
        map.put("ReviewerGivenStars",ReviewerGivenStars);

        JSONObject Data=new JSONObject(map);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(URL, Data, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);
    }
  void ServerResponseWithAddReviewResult(JSONObject AddReviewResult){
      try {
          if(AddReviewResult.getString("Successful").equals("true")){
              reviewersNames.add(AddReviewResult.getString("ReviewerName"));
              reviewersComments.add(AddReviewResult.getString("ReviewerComment"));
              reviewersGivenStars.add((float)AddReviewResult.getDouble("ReviewerGivenStars"));
              reviewersCommentDate.add(AddReviewResult.getString("ReviewDate"));
              SaveUpdatedShopDataToMemoryAndNotifyPortfolioRecyclerView();
          }
      } catch (JSONException e) {
          e.printStackTrace();
      }

  }
    public String LoadJSONFile(String jsonFileName) {
        String[] mFileList = fileList();
        Boolean fileExists = false;

        for (int i = 0; i < mFileList.length; i++) {
            if (jsonFileName.equals(mFileList[i])) {
                fileExists = true;
                break;
            }
        }
        if (fileExists) {
            String jsonAsString = null;

            try {
                FileInputStream fis = openFileInput(jsonFileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    sb.append(text);
                }
                jsonAsString = sb.toString();

                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonAsString;
        } else {

            FileOutputStream fos = null;
            JSONObject emptyJSONObject = new JSONObject();
            try {
                fos = openFileOutput("ShopsData.txt", MODE_PRIVATE);
                fos.write(emptyJSONObject.toString().getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



            return emptyJSONObject.toString();
        }


    }

    public void WriteNewShopDataToLocalMemory(JSONObject NewShopDataJSON) {
        try {


            String localMemoryJsonAsString = LoadJSONFile("ShopsData.txt");
            if (localMemoryJsonAsString != null) {

                JSONObject localMemoryJsonObject = new JSONObject(localMemoryJsonAsString);
                if (localMemoryJsonObject.has(ShopNameFromRecyclerView)) {
                    localMemoryJsonObject.remove(ShopNameFromRecyclerView);

                }
                localMemoryJsonObject.put(ShopNameFromRecyclerView, NewShopDataJSON);

                FileOutputStream fos = openFileOutput("ShopsData.txt", MODE_PRIVATE);
                fos.write(localMemoryJsonObject.toString().getBytes());

                fos.close();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void LoadLocalData(String ShopName) {

        try {
            PortfolioImages.clear();
            PortfolioImagesAsStrings.clear();

            String jsonAsString = LoadJSONFile("ShopsData.txt");

            if (jsonAsString != null ) {

                JSONObject jsonObject = new JSONObject(jsonAsString);

                if (jsonObject.has(ShopName)) {


                    JSONObject ShopDataJSONObject = jsonObject.getJSONObject(ShopName);


                   if(ShopDataJSONObject.has("PortfolioImagesLinks")){
                       ArrayList<String> ListOfImagesThatDidNotChange = new ArrayList<>();
                       ArrayList<Integer> IndexOfTheImageThatDidNotChange = new ArrayList<>();
                       ArrayList<String> ListOfImagesLinksThatDidNotChange = new ArrayList<>();

                      // ArrayList<String> PortfolioImagesLinks= ParseJSONtoArrayListOfStrings(ShopDataJSONObject.get("PortfolioImagesLinks").toString());
                       ArrayList<String> PortfolioImagesLinksLocal=new ArrayList<>();
                       for (int i=0;i<ShopDataJSONObject.getJSONArray("PortfolioImagesLinks").length();i++){
                            PortfolioImagesLinksLocal.add(i, ShopDataJSONObject.getJSONArray("PortfolioImagesLinks").getString(i));
                       }

                       Log.v("LoadFromCache","Loaded Portfolio Links :"+PortfolioImagesLinksLocal);

                      ArrayList<String> PortfolioImagesAsStringsLocal=new ArrayList<>();
                      for ( int i=0;i<ShopDataJSONObject.getJSONArray("PortfolioImagesAsStrings").length();i++){
                          PortfolioImagesAsStringsLocal.add(i, ShopDataJSONObject.getJSONArray("PortfolioImagesAsStrings").getString(i));
                      }
                       PortfolioImagesLinksToBeRequested.clear();
                      IndexOfImageToReceiveNext=0;
                       for (int i = 1; i < ImagesLinkFromRecyclerView.size(); i++) { //i=1 to ignore the main shop image



                               Boolean LinkNotFound = true;
                               for (int j = 0; j <PortfolioImagesLinksLocal.size(); j++) {

                                   if (PortfolioImagesLinksLocal.get(j).equals(ImagesLinkFromRecyclerView.get(i))) {
                                       LinkNotFound = false;
                                       IndexOfTheImageThatDidNotChange.add(j);
                                       ListOfImagesThatDidNotChange.add(PortfolioImagesAsStringsLocal.get(j));
                                       ListOfImagesLinksThatDidNotChange.add(PortfolioImagesLinksLocal.get(j));
                                   }
                               }
                               if (LinkNotFound) {


                                   PortfolioImagesLinksToBeRequested.add(ImagesLinkFromRecyclerView.get(i));
                               }



                       }
                       if(PortfolioImagesLinksToBeRequested.size()>0){
                           RequestImage(PortfolioImagesLinksToBeRequested.get(IndexOfImageToReceiveNext));//IndexOfImageToReceiveNext should equal zero at this stage
                       }
                       ShopDataJSONObject.remove("PortfolioImagesAsStrings");
                       ShopDataJSONObject.remove("PortfolioImagesLinks");
                       PortfolioImagesAsStrings.clear();
                       PortfolioImagesAsStrings=ListOfImagesThatDidNotChange;
                       PortfolioImagesLinks.clear();
                       PortfolioImagesLinks=ListOfImagesLinksThatDidNotChange;

                       PortfolioImages.clear();
                       for (int i=0;i<PortfolioImagesAsStringsLocal.size();i++){
                           PortfolioImages.add(ConvertStringToBitmap(PortfolioImagesAsStringsLocal.get(i)));
                       }


                   } else{
                       PortfolioImagesLinksToBeRequested.clear();
                       IndexOfImageToReceiveNext=0;
                       PortfolioImagesLinksToBeRequested.addAll(ImagesLinkFromRecyclerView);
                       PortfolioImagesLinksToBeRequested.remove(0);// to remove shop main image
                       if(PortfolioImagesLinksToBeRequested.size()>0){
                           RequestImage(PortfolioImagesLinksToBeRequested.get(IndexOfImageToReceiveNext));//IndexOfImageToReceiveNext should equal zero at this stage
                       }
                      /*
                       for(int i=1;i<ImagesLinkFromRecyclerView.size();i++){//i=1 to ignore the main shop image
                           RequestImage(ImagesLinkFromRecyclerView.get(i));
                       }

                       */
                   }


                    UseTheAcquiredData(ShopDataJSONObject,false);


                } else {

                    GetShopDetailsInfoFromServer(ShopName);


                }
            } else {
                //null is handled in the calling function LoadJSONFile()
            }


        } catch (JSONException e) {
            Log.v("LoadFromCache","Error parsing JSON in ShopDetailsActivity LoadLocalData function");
            e.printStackTrace();
        }
    }

    void GetShopDetailsInfoFromServer(String shopName) {

        JSONObject getShopDetailsInfo = new JSONObject();
        try {
            getShopDetailsInfo.put("Request", "ShopDetailsInfo");
            getShopDetailsInfo.put("ShopName", shopName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, getShopDetailsInfo, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);
    }

    void UseTheAcquiredData(JSONObject dataToUse, Boolean MethodCalledFromServer) {
        try {

       /*     EmailAddress  = dataToUse.getString("EmailAddress");
            Password    = dataToUse.getString("Password");
            FirstName= dataToUse.getString("FirstName");
            LastName     = dataToUse.getString("LastName");
            PhoneNumber  = dataToUse.getString("PhoneNumber");
            isBusinessOwner  = dataToUse.getBoolean("isBusinessOwner");
            SalonName  = dataToUse.getString("SalonName");
            SelectedState   = dataToUse.getString("SelectedState");
            SelectedCommune  = dataToUse.getString("SelectedCommune");*/
            UsesCoordinates = dataToUse.getBoolean("UsesCoordinates");
            ShopLatitude   = dataToUse.getDouble("ShopLatitude");
            ShopLongitude   = dataToUse.getDouble("ShopLongitude");
        /*    isMen  = dataToUse.getBoolean("isMen");
           //this line is no longer needed since image is downloaded separately// ShopMainImageForMainActivity   = ConvertStringToBitmap(dataToUse.getString("ShopMainImageForMainActivity"));
            ShopPhoneNumber   = dataToUse.getString("ShopPhoneNumber");
            FacebookLink  = dataToUse.getString("FacebookLink");
            InstagramLink  = dataToUse.getString("InstagramLink");
            Coiffure    = dataToUse.getBoolean("Coiffure");
            Meches = dataToUse.getBoolean("Meches");
            Tinte  = dataToUse.getBoolean("Tinte");
            Pedcure = dataToUse.getBoolean("Pedcure");
            Manage = dataToUse.getBoolean("Manage");
            Manicure = dataToUse.getBoolean("Manicure");
            Coupe = dataToUse.getBoolean("Coupe");
            Saturday = dataToUse.getString("Saturday");
            Sunday = dataToUse.getString("Sunday");
            Monday  = dataToUse.getString("Monday");
            Tuesday  = dataToUse.getString("Tuesday");
            Wednesday = dataToUse.getString("Wednesday");
            Thursday = dataToUse.getString("Thursday");
            Friday = dataToUse.getString("Friday");
*/
            ServicesHairCutsNames.clear();

            for (int i = 0; i < dataToUse.getJSONArray("ServicesHairCutsNames").length(); i++) {
                ServicesHairCutsNames.add(dataToUse.getJSONArray("ServicesHairCutsNames").getString(i));
            }




            ServicesHairCutsPrices.clear();

            for (int i = 0; i < dataToUse.getJSONArray("ServicesHairCutsPrices").length(); i++) {
                ServicesHairCutsPrices.add(dataToUse.getJSONArray("ServicesHairCutsPrices").getString(i));
            }



            ServicesHairCutsDuration.clear();

            for (int i = 0; i < dataToUse.getJSONArray("ServicesHairCutsDuration").length(); i++) {
                ServicesHairCutsDuration.add(dataToUse.getJSONArray("ServicesHairCutsDuration").getString(i));
            }


            reviewersNames.clear();

            for (int i = 0; i < dataToUse.getJSONArray("reviewersNames").length(); i++) {
                reviewersNames.add(dataToUse.getJSONArray("reviewersNames").getString(i));
            }


            reviewersComments.clear();

            for (int i = 0; i < dataToUse.getJSONArray("reviewersComments").length(); i++) {
                reviewersComments.add(dataToUse.getJSONArray("reviewersComments").getString(i));
            }


            reviewersCommentDate.clear();

            for (int i = 0; i < dataToUse.getJSONArray("reviewersCommentDate").length(); i++) {
                reviewersCommentDate.add(dataToUse.getJSONArray("reviewersCommentDate").getString(i));
            }


            reviewersGivenStars.clear();

            for (int i = 0; i < dataToUse.getJSONArray("reviewersGivenStars").length(); i++) {
                reviewersGivenStars.add((float)dataToUse.getJSONArray("reviewersGivenStars").getDouble(i));
            }

            if(MethodCalledFromServer){
                /// WE CALL THIS SO WE UPDATE PROTFOLIO FRAGMENT
                /// AND SAVE THE NEW DATA WHEN ALL IMAGES ARE DOWNLOADED IN VOLLEY IMAGE LISTENER

                LoadLocalData(ShopNameFromRecyclerView);
            }
            WeGotTheDataDisplayIt();

        } catch (JSONException e) {
            Toast.makeText(this, "Problem in UseTheAcquiredData function", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }


    void RequestImage(final String ImageLink) {

            ImageRequest imageRequest = new ImageRequest(ImageLink, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {

                    PortfolioImages.add(response);
                    PortfolioImagesAsStrings.add(BitmapToString(response));
                    PortfolioImagesLinks.add(ImageLink);

                  SaveUpdatedShopDataToMemoryAndNotifyPortfolioRecyclerView();
                    IndexOfImageToReceiveNext++;
                    if(IndexOfImageToReceiveNext<PortfolioImagesLinksToBeRequested.size()){
                        RequestImage(PortfolioImagesLinksToBeRequested.get(IndexOfImageToReceiveNext));
                    }

                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("VolleyErrors", "onErrorResponse: IN SHOPDETAILS ACTIVITY IMAGES ImagesForPortfolio" + error.toString());
                }
            });
            requestQueue.add(imageRequest);
        }



    void SaveUpdatedShopDataToMemoryAndNotifyPortfolioRecyclerView() {
        if(ShopDetails_Frag4.mContext!=null){
            ShopDetails_Frag4.ReceivedNewImagesNotifyRecyclerView();
        }
        if(ShopDetails_Frag3.mContext!=null){
            ShopDetails_Frag3.ReceivedNewReviewsNotifyRecyclerView();
        }
        Map<String,Object> UpdatedShopDataMap=new HashMap<>();

        UpdatedShopDataMap.put("ServicesHairCutsDuration",ServicesHairCutsDuration);
        UpdatedShopDataMap.put("ServicesHairCutsPrices", ServicesHairCutsPrices);
        UpdatedShopDataMap.put("ServicesHairCutsNames", ServicesHairCutsNames);
        UpdatedShopDataMap.put("reviewersGivenStars", reviewersGivenStars);
        UpdatedShopDataMap.put("reviewersCommentDate",reviewersCommentDate );
        UpdatedShopDataMap.put("reviewersComments",reviewersComments );
        UpdatedShopDataMap.put("reviewersNames",reviewersNames );
        UpdatedShopDataMap.put("PortfolioImagesAsStrings",PortfolioImagesAsStrings);
        UpdatedShopDataMap.put("PortfolioImagesLinks", PortfolioImagesLinks);

        UpdatedShopDataMap.put("UsesCoordinates",UsesCoordinates);
        UpdatedShopDataMap.put("ShopLatitude", ShopLatitude);
        UpdatedShopDataMap.put("ShopLongitude",ShopLongitude);

        JSONObject jsonObject=new JSONObject(UpdatedShopDataMap);

        WriteNewShopDataToLocalMemory(jsonObject);
    }

    public String BitmapToString(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 85, byteArrayOutputStream);
        byte[] byteImage = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteImage, Base64.DEFAULT);
    }

    public Bitmap ConvertStringToBitmap(String image) {
        byte[] bytes;
        bytes = Base64.decode(image, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



}
