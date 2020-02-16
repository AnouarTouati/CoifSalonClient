package com.example.coifsalonclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


    public static String shopNameFromRecyclerView;
    public String ShopMainImageForMainActivityLinkFromRecyclerView;


    static Context mContext;


    public String emailAddress;
    public String password;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public Boolean isEmployee = false;
    public Boolean isBusinessOwner = false;
    public String salonName;
    public String selectedState;
    public String SelectedCommune;
    public static Boolean usesCoordinates = false;
    public static Double shopLatitude;
    public static Double shopLongitude;
    public Boolean isMen = true;
    public String shopPhoneNumber;
    public String facebookLink;
    public String instagramLink;
    public Boolean coiffure = false;
    public Boolean makeUp = false;
    public Boolean meches = false;
    public Boolean tinte = false;
    public Boolean pedcure = false;
    public Boolean manage = false;
    public Boolean manicure = false;
    public Boolean coupe = false;
    public String saturday;
    public String sunday;
    public String monday;
    public String tuesday;
    public String wednesday;
    public String thursday;
    public String friday;


    //////////////////////////////////////////////////////////////////////////////
    //FRAGMENT 1 SERVICES
    public static ArrayList<String> servicesHairCutsNames = new ArrayList<>();
    public static ArrayList<String> servicesHairCutsPrices = new ArrayList<>();
    public static ArrayList<String> servicesHairCutsDuration = new ArrayList<>();
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
    ArrayList<String> imagesLinkFromRecyclerView = new ArrayList<>();
   public static ArrayList<Bitmap> portfolioImages = new ArrayList<>();//the first image is the main shop image
    ArrayList<String> portfolioImagesAsStrings = new ArrayList<>();
    ArrayList<String> portfolioImagesLinks = new ArrayList<>();//we use this in local memory so we are sure that we received the image pointed to by the link
    Integer indexOfImageToReceiveNext =0;
    ArrayList<String> portfolioImagesLinksToBeRequested = new ArrayList<>();
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
                        serverResponseWithShopInfo(response.getJSONObject("ShopDetailsInfo"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response.has("BookResult")) {
                    try {
                        serverResponseWithBookingResult(response.getJSONObject("BookResult"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (response.has("ReviewResult")) {
                    try {
                        serverResponseWithAddReviewResult(response.getJSONObject("ReviewResult"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }


        };

        shopNameFromRecyclerView = getIntent().getStringExtra("ShopName");
        imagesLinkFromRecyclerView = getIntent().getStringArrayListExtra("ImagesLinks");//we get the links from mainactivity because when we load from cache we check the links
        successfullyBookedShop =getIntent().getStringExtra("SuccessfullyBookedShop");
        successfullyBookedHaircut =getIntent().getStringExtra("SuccessfullyBookedHaircut");
        requestQueue = Volley.newRequestQueue(this);
        loadLocalData(shopNameFromRecyclerView);

    }

    void weGotTheDataDisplayIt() {

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

    public static void getShopServicesInfo(String shopName) {
/*
        servicesHairCutsNames.add("Haircut1");
        servicesHairCutsNames.add("Haircut2");
        servicesHairCutsNames.add("Haircut3");
        servicesHairCutsNames.add("Haircut4");
        servicesHairCutsNames.add("Haircut5");
        servicesHairCutsNames.add("Haircut6");
        servicesHairCutsNames.add("Haircut7");

        servicesHairCutsPrices.add("200");
        servicesHairCutsPrices.add("400");
        servicesHairCutsPrices.add("600");
        servicesHairCutsPrices.add("700");
        servicesHairCutsPrices.add("800");
        servicesHairCutsPrices.add("900");
        servicesHairCutsPrices.add("1000");

        servicesHairCutsDuration.add("30");
        servicesHairCutsDuration.add("40");
        servicesHairCutsDuration.add("50");
        servicesHairCutsDuration.add("60");
        servicesHairCutsDuration.add("70");
        servicesHairCutsDuration.add("80");
        servicesHairCutsDuration.add("90");
        //"the function below" even though it is intended  for book response it also usable for info since it updates every thing
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

    public static void getShopReviewsInfo(String shopName) {
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

    void serverResponseWithShopInfo(JSONObject ShopDetailsInfo) {
        // dont change the order of these two functions
        writeNewShopDataToLocalMemory(ShopDetailsInfo);//this should be here to save some info without images and it will be called again when download of images is done
        useTheAcquiredData(ShopDetailsInfo,true);



    }

    void serverResponseWithBookingResult(JSONObject BookResult) {


        try {
            if (BookResult.getString("Successful").equals("True")) {
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

    public static void book(final String ServicesHairCutToReserve) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Reserve Confirmation");
        alertDialogBuilder.setMessage("Do you want to book for " + ServicesHairCutToReserve + " ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, ServicesHairCutToReserve, Toast.LENGTH_LONG).show();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Request", "Book");
                    jsonObject.put("ServicesHairCutToReserve", ServicesHairCutToReserve);
                    jsonObject.put("ShopNameToBook", shopNameFromRecyclerView);
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
                Toast.makeText(mContext, "We didnt book any thing", Toast.LENGTH_LONG).show();

            }
        });
        alertDialogBuilder.create().show();


    }
    public void addReview(String ReviewerName, String ReviewerComment, float ReviewerGivenStars){

        Map<String,Object> map=new HashMap<>();
        map.put("Request","GiveReview");
        map.put("ReviewerName",ReviewerName);
        map.put("ReviewerComment",ReviewerComment);
        map.put("ReviewerGivenStars",ReviewerGivenStars);

        JSONObject Data=new JSONObject(map);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(URL, Data, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);
    }
  void serverResponseWithAddReviewResult(JSONObject AddReviewResult){
      try {
          if(AddReviewResult.getString("Successful").equals("True")){

              reviewersNames.add(AddReviewResult.getString("ReviewerName"));
              reviewersComments.add(AddReviewResult.getString("ReviewerComment"));
              reviewersGivenStars.add((float)AddReviewResult.getDouble("ReviewerGivenStars"));
              reviewersCommentDate.add(AddReviewResult.getString("ReviewDate"));
              saveUpdatedShopDataToMemoryAndNotifyPortfolioRecyclerView();
          }
      } catch (JSONException e) {
          e.printStackTrace();
      }

  }

    public void writeNewShopDataToLocalMemory(JSONObject NewShopDataJSON) {
        try {


            String localMemoryJsonAsString = CommonMehods.loadJSONFile("ShopsData.txt",getApplicationContext());
            if (localMemoryJsonAsString != null) {

                JSONObject localMemoryJsonObject = new JSONObject(localMemoryJsonAsString);
                if (localMemoryJsonObject.has(shopNameFromRecyclerView)) {
                    localMemoryJsonObject.remove(shopNameFromRecyclerView);

                }
                localMemoryJsonObject.put(shopNameFromRecyclerView, NewShopDataJSON);

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


    public void loadLocalData(String ShopName) {

        try {
            portfolioImages.clear();
            portfolioImagesAsStrings.clear();

            String jsonAsString = CommonMehods.loadJSONFile("ShopsData.txt",getApplicationContext());

            if (jsonAsString != null ) {

                JSONObject jsonObject = new JSONObject(jsonAsString);

                if (jsonObject.has(ShopName)) {


                    JSONObject ShopDataJSONObject = jsonObject.getJSONObject(ShopName);


                   if(ShopDataJSONObject.has("PortfolioImagesLinks")){
                       ArrayList<String> ListOfImagesThatDidNotChange = new ArrayList<>();
                       ArrayList<Integer> IndexOfTheImageThatDidNotChange = new ArrayList<>();
                       ArrayList<String> ListOfImagesLinksThatDidNotChange = new ArrayList<>();

                      // ArrayList<String> portfolioImagesLinks= ParseJSONtoArrayListOfStrings(ShopDataJSONObject.get("portfolioImagesLinks").toString());
                       ArrayList<String> PortfolioImagesLinksLocal=new ArrayList<>();
                       for (int i=0;i<ShopDataJSONObject.getJSONArray("PortfolioImagesLinks").length();i++){
                            PortfolioImagesLinksLocal.add(i, ShopDataJSONObject.getJSONArray("PortfolioImagesLinks").getString(i));
                       }

                       Log.v("LoadFromCache","Loaded Portfolio Links :"+PortfolioImagesLinksLocal);

                      ArrayList<String> PortfolioImagesAsStringsLocal=new ArrayList<>();
                      for ( int i=0;i<ShopDataJSONObject.getJSONArray("PortfolioImagesAsStrings").length();i++){
                          PortfolioImagesAsStringsLocal.add(i, ShopDataJSONObject.getJSONArray("PortfolioImagesAsStrings").getString(i));
                      }
                       portfolioImagesLinksToBeRequested.clear();
                      indexOfImageToReceiveNext =0;
                       for (int i = 1; i < imagesLinkFromRecyclerView.size(); i++) { //i=1 to ignore the main shop image



                               Boolean LinkNotFound = true;
                               for (int j = 0; j <PortfolioImagesLinksLocal.size(); j++) {

                                   if (PortfolioImagesLinksLocal.get(j).equals(imagesLinkFromRecyclerView.get(i))) {
                                       LinkNotFound = false;
                                       IndexOfTheImageThatDidNotChange.add(j);
                                       ListOfImagesThatDidNotChange.add(PortfolioImagesAsStringsLocal.get(j));
                                       ListOfImagesLinksThatDidNotChange.add(PortfolioImagesLinksLocal.get(j));
                                   }
                               }
                               if (LinkNotFound) {


                                   portfolioImagesLinksToBeRequested.add(imagesLinkFromRecyclerView.get(i));
                               }



                       }
                       if(portfolioImagesLinksToBeRequested.size()>0){
                           requestImage(portfolioImagesLinksToBeRequested.get(indexOfImageToReceiveNext));//indexOfImageToReceiveNext should equal zero at this stage
                       }
                       ShopDataJSONObject.remove("PortfolioImagesAsStrings");
                       ShopDataJSONObject.remove("PortfolioImagesLinks");
                       portfolioImagesAsStrings.clear();
                       portfolioImagesAsStrings =ListOfImagesThatDidNotChange;
                       portfolioImagesLinks.clear();
                       portfolioImagesLinks =ListOfImagesLinksThatDidNotChange;

                       portfolioImages.clear();
                       for (int i=0;i<PortfolioImagesAsStringsLocal.size();i++){
                           portfolioImages.add(CommonMehods.convertStringToBitmap(PortfolioImagesAsStringsLocal.get(i)));
                       }


                   } else{
                       portfolioImagesLinksToBeRequested.clear();
                       indexOfImageToReceiveNext =0;
                       portfolioImagesLinksToBeRequested.addAll(imagesLinkFromRecyclerView);
                       portfolioImagesLinksToBeRequested.remove(0);// to remove shop main image
                       if(portfolioImagesLinksToBeRequested.size()>0){
                           requestImage(portfolioImagesLinksToBeRequested.get(indexOfImageToReceiveNext));//indexOfImageToReceiveNext should equal zero at this stage
                       }
                      /*
                       for(int i=1;i<imagesLinkFromRecyclerView.size();i++){//i=1 to ignore the main shop image
                           requestImage(imagesLinkFromRecyclerView.get(i));
                       }

                       */
                   }


                    useTheAcquiredData(ShopDataJSONObject,false);


                } else {

                    getShopDetailsInfoFromServer(ShopName);


                }
            } else {
                //null is handled in the calling function loadJSONFile()
            }


        } catch (JSONException e) {
            Log.v("LoadFromCache","Error parsing JSON in ShopDetailsActivity loadLocalData function");
            e.printStackTrace();
        }
    }

    void getShopDetailsInfoFromServer(String shopName) {

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

    void useTheAcquiredData(JSONObject dataToUse, Boolean MethodCalledFromServer) {
        try {

       /*     emailAddress  = dataToUse.getString("emailAddress");
            password    = dataToUse.getString("password");
            firstName= dataToUse.getString("firstName");
            lastName     = dataToUse.getString("lastName");
            phoneNumber  = dataToUse.getString("phoneNumber");
            isBusinessOwner  = dataToUse.getBoolean("isBusinessOwner");
            salonName  = dataToUse.getString("salonName");
            selectedState   = dataToUse.getString("selectedState");
            SelectedCommune  = dataToUse.getString("SelectedCommune");*/
            usesCoordinates = dataToUse.getBoolean("UsesCoordinates");
            shopLatitude = dataToUse.getDouble("ShopLatitude");
            shopLongitude = dataToUse.getDouble("ShopLongitude");
        /*    isMen  = dataToUse.getBoolean("isMen");
           //this line is no longer needed since image is downloaded separately// ShopMainImageForMainActivity   = convertStringToBitmap(dataToUse.getString("ShopMainImageForMainActivity"));
            shopPhoneNumber   = dataToUse.getString("shopPhoneNumber");
            facebookLink  = dataToUse.getString("facebookLink");
            instagramLink  = dataToUse.getString("instagramLink");
            coiffure    = dataToUse.getBoolean("coiffure");
            meches = dataToUse.getBoolean("meches");
            tinte  = dataToUse.getBoolean("tinte");
            pedcure = dataToUse.getBoolean("pedcure");
            manage = dataToUse.getBoolean("manage");
            manicure = dataToUse.getBoolean("manicure");
            coupe = dataToUse.getBoolean("coupe");
            saturday = dataToUse.getString("saturday");
            sunday = dataToUse.getString("sunday");
            monday  = dataToUse.getString("monday");
            tuesday  = dataToUse.getString("tuesday");
            wednesday = dataToUse.getString("wednesday");
            thursday = dataToUse.getString("thursday");
            friday = dataToUse.getString("friday");
*/
            servicesHairCutsNames.clear();

            for (int i = 0; i < dataToUse.getJSONArray("ServicesHairCutsNames").length(); i++) {
                servicesHairCutsNames.add(dataToUse.getJSONArray("ServicesHairCutsNames").getString(i));
            }




            servicesHairCutsPrices.clear();

            for (int i = 0; i < dataToUse.getJSONArray("ServicesHairCutsPrices").length(); i++) {
                servicesHairCutsPrices.add(dataToUse.getJSONArray("ServicesHairCutsPrices").getString(i));
            }



            servicesHairCutsDuration.clear();

            for (int i = 0; i < dataToUse.getJSONArray("ServicesHairCutsDuration").length(); i++) {
                servicesHairCutsDuration.add(dataToUse.getJSONArray("ServicesHairCutsDuration").getString(i));
            }


            reviewersNames.clear();

            for (int i = 0; i < dataToUse.getJSONArray("ReviewersNames").length(); i++) {
                reviewersNames.add(dataToUse.getJSONArray("ReviewersNames").getString(i));
            }


            reviewersComments.clear();

            for (int i = 0; i < dataToUse.getJSONArray("ReviewersComments").length(); i++) {
                reviewersComments.add(dataToUse.getJSONArray("ReviewersComments").getString(i));
            }


            reviewersCommentDate.clear();

            for (int i = 0; i < dataToUse.getJSONArray("ReviewersCommentDate").length(); i++) {
                reviewersCommentDate.add(dataToUse.getJSONArray("ReviewersCommentDate").getString(i));
            }


            reviewersGivenStars.clear();

            for (int i = 0; i < dataToUse.getJSONArray("ReviewersGivenStars").length(); i++) {
                reviewersGivenStars.add((float)dataToUse.getJSONArray("ReviewersGivenStars").getDouble(i));
            }

            if(MethodCalledFromServer){
                /// WE CALL THIS SO WE UPDATE PROTFOLIO FRAGMENT
                /// AND SAVE THE NEW DATA WHEN ALL IMAGES ARE DOWNLOADED IN VOLLEY IMAGE LISTENER

                loadLocalData(shopNameFromRecyclerView);
            }
            weGotTheDataDisplayIt();

        } catch (JSONException e) {
            Toast.makeText(this, "Problem in useTheAcquiredData function", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }


    void requestImage(final String ImageLink) {

            ImageRequest imageRequest = new ImageRequest(ImageLink, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {

                    portfolioImages.add(response);
                    portfolioImagesAsStrings.add(CommonMehods.bitmapToString(response));
                    portfolioImagesLinks.add(ImageLink);

                  saveUpdatedShopDataToMemoryAndNotifyPortfolioRecyclerView();
                    indexOfImageToReceiveNext++;
                    if(indexOfImageToReceiveNext < portfolioImagesLinksToBeRequested.size()){
                        requestImage(portfolioImagesLinksToBeRequested.get(indexOfImageToReceiveNext));
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



    void saveUpdatedShopDataToMemoryAndNotifyPortfolioRecyclerView() {
        if(ShopDetails_Frag4.mContext!=null){
            ShopDetails_Frag4.receivedNewImagesNotifyRecyclerView();
        }
        if(ShopDetails_Frag3.mContext!=null){
            ShopDetails_Frag3.ReceivedNewReviewsNotifyRecyclerView();
        }
        Map<String,Object> UpdatedShopDataMap=new HashMap<>();

        UpdatedShopDataMap.put("ServicesHairCutsDuration", servicesHairCutsDuration);
        UpdatedShopDataMap.put("ServicesHairCutsPrices", servicesHairCutsPrices);
        UpdatedShopDataMap.put("ServicesHairCutsNames", servicesHairCutsNames);
        UpdatedShopDataMap.put("ReviewersGivenStars", reviewersGivenStars);
        UpdatedShopDataMap.put("ReviewersCommentDate",reviewersCommentDate );
        UpdatedShopDataMap.put("ReviewersComments",reviewersComments );
        UpdatedShopDataMap.put("ReviewersNames",reviewersNames );
        UpdatedShopDataMap.put("PortfolioImagesAsStrings", portfolioImagesAsStrings);
        UpdatedShopDataMap.put("PortfolioImagesLinks", portfolioImagesLinks);

        UpdatedShopDataMap.put("UsesCoordinates", usesCoordinates);
        UpdatedShopDataMap.put("ShopLatitude", shopLatitude);
        UpdatedShopDataMap.put("ShopLongitude", shopLongitude);

        JSONObject jsonObject=new JSONObject(UpdatedShopDataMap);

        writeNewShopDataToLocalMemory(jsonObject);
    }




}
