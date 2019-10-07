package com.example.coifsalonclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.Gson;


import org.json.JSONArray;
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

public class ShopDetailsActivity extends AppCompatActivity {
    static Response.Listener<JSONObject> volleyListener;
    static Response.ErrorListener volleyErrorListener;
    static RequestQueue requestQueue;
    static final String URL = "http://192.168.43.139:81/SecondPage.php";

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
    public Boolean UseCoordinatesAKAaddMap = false;
    public Double StoreLatitude;
    public Double StoreLongitude;
    public Boolean isMen = true;
    public String StorePhoneNumber;
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

    public Bitmap ShopMainImageForMainActivity;
    public String ShopMainImageForMainActivityAsString;
    public String ShopMainImageForMainActivityLink;
    //////////////////////////////////////////////////////////////////////////////
    //FRAGMENT 1 SERVICES
    public static ArrayList<String> ServicesHairCutsNames = new ArrayList<>();
    public static ArrayList<String> ServicesHairCutsPrices = new ArrayList<>();
    public static ArrayList<String> ServicesHairCutsDuration = new ArrayList<>();
    public static String successfullyBookedServicesHaircut = null;
    public static String successfullyBookedStore = null;
    ///////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
    //FRAGMENT 3 REVIEWS
    public static ArrayList<String> reviewersNames = new ArrayList<>();
    public static ArrayList<String> reviewersComments = new ArrayList<>();
    public static ArrayList<String> reviewersCommentDate = new ArrayList<>();
    public static ArrayList<Integer> reviewersGivenStars = new ArrayList<>();
    ///////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    //FRAGMENT 4 PORTFOLIO
    ArrayList<String> ImagesLinkFromRecyclerView = new ArrayList<>();
   public static ArrayList<Bitmap> PortfolioImages = new ArrayList<>();
    ArrayList<String> PortfolioImagesAsStrings = new ArrayList<>();
    ArrayList<String> PortfolioImagesLinks = new ArrayList<>();//we use this in local memory so we are sure that we received the image pointed to by the link
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


                if (response.has("StoreDetailsInfo")) {
                    try {
                        ServerResponseWithStoreInfo(response.getJSONObject("StoreDetailsInfo"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response.has("BookResult")) {
                    try {
                        ServerResponseWithBookingResult(response.getJSONObject("BookResult"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }


        };

        ShopNameFromRecyclerView = getIntent().getStringExtra("ShopName");
        ImagesLinkFromRecyclerView = getIntent().getStringArrayListExtra("ImagesLinks");//we get the links from mainactivity because when we load from cache we check the links
       // ShopMainImageForMainActivityLinkFromRecyclerView = getIntent().getStringExtra("ShopMainImageForMainActivityLink");
        //line above it temprarly commented
        ShopMainImageForMainActivityLinkFromRecyclerView=ImagesLinkFromRecyclerView.get(0);
        requestQueue = Volley.newRequestQueue(this);
        LoadLocalData(ShopNameFromRecyclerView);


    }

    void WeGotTheDataDisplayIt() {
        // we might want to use the code below after we receive the data
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

    public static void GetStoreServicesInfo(String shopName) {
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
        //"the function below" even though it is intended  for book response it also usable for info since it updates every thing
        ShopDetails_Frag1.BookWasSuccessfulNorifyRecyclerViewAdapter();
*/
/*
      JSONObject getStoreInfo=new JSONObject();
        try {
            getStoreInfo.put("RequestStoreServicesInfo_StoreName", shopName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, getStoreInfo, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);*/
    }

    public static void GetStoreReviewsInfo(String shopName) {
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
        JSONObject getStoreInfo=new JSONObject();
        try {
            getStoreInfo.put("RequestStoreReviewsInfo_StoreName", shopName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, getStoreInfo, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);*/
    }

    void ServerResponseWithStoreInfo(JSONObject StoreDetailsInfo) {

        useTheAcquiredData(StoreDetailsInfo,true);
        Log.v("LoadFromCache","Write Store Data for first time :"+StoreDetailsInfo);
        writeNewShopDataToLocalMemory(StoreDetailsInfo);//this should be here to save some info without images and it will be called again when download of images is done

    }

    void ServerResponseWithBookingResult(JSONObject BookResult) {


        try {
            if (BookResult.getString("Successful").equals("true")) {
                successfullyBookedServicesHaircut = BookResult.getString("ServicesHairCutToReserve");
                successfullyBookedStore = BookResult.getString("BookedStore");
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
                    jsonObject.put("StoreNameToBook", ShopNameFromRecyclerView);
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

    public String loadJSONFile(String jsonFileName) {
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
            /*
            File file = new File(this.getFilesDir(), jsonFileName);
            try {
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
*/
            FileOutputStream fos = null;
            try {
                fos = openFileOutput("StoresData.txt", MODE_PRIVATE);
                JSONObject emptyJSONObject = new JSONObject();
                fos.write(emptyJSONObject.toString().getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            getStoreDetailsInfoFromServer(ShopNameFromRecyclerView);
            //  fakeRequestAndResponde();
            //requesting data from server
//            GetStoreServicesInfo(ShopNameFromRecyclerView);
            //          GetStoreReviewsInfo(ShopNameFromRecyclerView);
            return null;
        }
          /*      String json = null;
                try {
                    InputStream is = getAssets().open(jsonFileName+".json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return null;
                }
                return json;*/

    }

    public void writeNewShopDataToLocalMemory(JSONObject newShopDataJSON) {
        try {


            String localMemoryJsonAsString = loadJSONFile("StoresData.txt");
            if (localMemoryJsonAsString != null) {

                JSONObject localMemoryJsonObject = new JSONObject(localMemoryJsonAsString);
                if (localMemoryJsonObject.has(ShopNameFromRecyclerView)) {
                    localMemoryJsonObject.remove(ShopNameFromRecyclerView);

                }
                localMemoryJsonObject.put(ShopNameFromRecyclerView, newShopDataJSON);

                FileOutputStream fos = openFileOutput("StoresData.txt", MODE_PRIVATE);
                //  fos.write(localMemoryJsonObject.toString().getBytes());
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

            String jsonAsString = loadJSONFile("StoresData.txt");
            Log.v("LoadFromCache","From Load Local jsonAsString :"+jsonAsString);
            if (jsonAsString != null) {

                JSONObject jsonObject = new JSONObject(jsonAsString);

                if (jsonObject.has(ShopName)) {

                    /// for the FOR loop the case of ImagesLinkFromRecyclerView.size()> jsonObject.getJSONObject(ShopName).getJSONArray("ImagesLinks").lenght()
                    /// were the server has less images meaning ShopDeleted them will be addressed later
                    JSONObject ShopDataJSONObject = jsonObject.getJSONObject(ShopName);

                    if(ShopDataJSONObject.has("ShopMainImageForMainActivityLink")){
                        if (!ShopDataJSONObject.getString("ShopMainImageForMainActivityLink").equals(ShopMainImageForMainActivityLinkFromRecyclerView)) {
                            RequestImage(ShopMainImageForMainActivityLinkFromRecyclerView, true);
                        }
                    }else{
                        RequestImage(ShopMainImageForMainActivityLinkFromRecyclerView, true);
                    }

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
                    //   ArrayList<String> PortfolioImagesAsStrings=ParseJSONtoArrayListOfStrings(ShopDataJSONObject.get("PortfolioImagesAsStrings").toString());
                      Log.v("LoadFromCache", "No Error in loading images as strings");
                       for (int i = 0; i < ImagesLinkFromRecyclerView.size(); i++) {


                           if (i < PortfolioImagesLinksLocal.size()) {
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

                                   RequestImage(ImagesLinkFromRecyclerView.get(i), false);
                               }

                           } else {
                               RequestImage(ImagesLinkFromRecyclerView.get(i), false);
                           }

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
                       for(int i=0;i<ImagesLinkFromRecyclerView.size();i++){
                           RequestImage(ImagesLinkFromRecyclerView.get(i), false);
                       }
                   }


                    useTheAcquiredData(ShopDataJSONObject,false);


                } else {

                    //  fakeRequestAndResponde();
                    getStoreDetailsInfoFromServer(ShopName);
                    //requesting data from server
                    // GetStoreServicesInfo(ShopNameFromRecyclerView);
                    // GetStoreReviewsInfo(ShopNameFromRecyclerView);

                }
            } else {
                //null is handled in the calling function loadJSONFile()
            }


        } catch (JSONException e) {
            Log.v("LoadFromCache","Error parsing JSON in ShopDetailsActivity LoadLocalData function");
            e.printStackTrace();
        }
    }

    void getStoreDetailsInfoFromServer(String shopName) {
        JSONObject getStoreDetailsInfo = new JSONObject();
        try {
            getStoreDetailsInfo.put("Request", "StoreDetailsInfo");
            getStoreDetailsInfo.put("ShopName", shopName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, getStoreDetailsInfo, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);
    }

    void useTheAcquiredData(JSONObject dataToUse,Boolean MethodCalledFromServer) {
        try {

       /*     EmailAddress  = dataToUse.getString("EmailAddress");
            Password    = dataToUse.getString("Password");
            FirstName= dataToUse.getString("FirstName");
            LastName     = dataToUse.getString("LastName");
            PhoneNumber  = dataToUse.getString("PhoneNumber");
            isBusinessOwner  = dataToUse.getBoolean("isBusinessOwner");
            SalonName  = dataToUse.getString("SalonName");
            SelectedState   = dataToUse.getString("SelectedState");
            SelectedCommune  = dataToUse.getString("SelectedCommune");
            UseCoordinatesAKAaddMap     = dataToUse.getBoolean("UseCoordinatesAKAaddMap");
            StoreLatitude   = dataToUse.getDouble("StoreLatitude");
            StoreLongitude   = dataToUse.getDouble("StoreLongitude");
            isMen  = dataToUse.getBoolean("isMen");
           //this line is no longer needed since image is downloaded separately// ShopMainImageForMainActivity   = ConvertStringToBitmap(dataToUse.getString("ShopMainImageForMainActivity"));
            StorePhoneNumber   = dataToUse.getString("StorePhoneNumber");
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
                reviewersGivenStars.add(dataToUse.getJSONArray("reviewersGivenStars").getInt(i));
            }

            if(MethodCalledFromServer){
                /// WE CALL THIS SO WE UPDATE PROTFOLIO FRAGMENT
                /// AND SAVE THE NEW DATA WHEN ALL IMAGES ARE DOWNLOADED IN VOLLEY IMAGE LISTENER

                LoadLocalData(ShopNameFromRecyclerView);
            }
            WeGotTheDataDisplayIt();

        } catch (JSONException e) {
            Toast.makeText(this, "Problem in useTheAcquiredData function", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

    void fakeRequestAndResponde() {

        JSONArray reviewersNamesJSON = new JSONArray();
        reviewersNamesJSON.put("3azdine Nourine");
        reviewersNamesJSON.put("Gaceb Walid");
        reviewersNamesJSON.put("Nasrou dfef");
        reviewersNamesJSON.put("Zitouni ahmed");


        JSONArray reviewersCommentsJSON = new JSONArray();
        reviewersCommentsJSON.put("GOOD");
        reviewersCommentsJSON.put("BAAAD");
        reviewersCommentsJSON.put("EXELLLLLLEBT");
        reviewersCommentsJSON.put("POOOOOOOOR");


        JSONArray reviewersCommentDateJSON = new JSONArray();
        reviewersCommentDateJSON.put("2019/06/02");
        reviewersCommentDateJSON.put("1999/06/02");
        reviewersCommentDateJSON.put("4004/06/02");
        reviewersCommentDateJSON.put("3004/06/02");

        JSONArray reviewersGivenStarsJSON = new JSONArray();
        reviewersGivenStarsJSON.put(1);
        reviewersGivenStarsJSON.put(5);
        reviewersGivenStarsJSON.put(3);
        reviewersGivenStarsJSON.put(2);

        JSONArray serviceHaircutsNamesJSON = new JSONArray();
        serviceHaircutsNamesJSON.put("Hair1");
        serviceHaircutsNamesJSON.put("Hair2");
        serviceHaircutsNamesJSON.put("Hair3");
        serviceHaircutsNamesJSON.put("Hair4");
        serviceHaircutsNamesJSON.put("Hair5");
        serviceHaircutsNamesJSON.put("Hair6");
        serviceHaircutsNamesJSON.put("Hair7");

        JSONArray ServicesHairCutsPricesJSON = new JSONArray();
        ServicesHairCutsPricesJSON.put("1000");
        ServicesHairCutsPricesJSON.put("2000");
        ServicesHairCutsPricesJSON.put("3000");
        ServicesHairCutsPricesJSON.put("4000");
        ServicesHairCutsPricesJSON.put("5000");
        ServicesHairCutsPricesJSON.put("6000");
        ServicesHairCutsPricesJSON.put("7000");

        JSONArray ServicesHairCutsDurationsJSON = new JSONArray();
        ServicesHairCutsDurationsJSON.put("3000");
        ServicesHairCutsDurationsJSON.put("4000");
        ServicesHairCutsDurationsJSON.put("5000");
        ServicesHairCutsDurationsJSON.put("6000");
        ServicesHairCutsDurationsJSON.put("7000");
        ServicesHairCutsDurationsJSON.put("8000");
        ServicesHairCutsDurationsJSON.put("9000");


        JSONObject fakeserverResult = new JSONObject();
        try {
            fakeserverResult.put("reviewersNames", reviewersNamesJSON);
            fakeserverResult.put("reviewersComments", reviewersCommentsJSON);
            fakeserverResult.put("reviewersCommentDate", reviewersCommentDateJSON);
            fakeserverResult.put("reviewersGivenStars", reviewersGivenStarsJSON);

            fakeserverResult.put("ServicesHairCutsNames", serviceHaircutsNamesJSON);
            fakeserverResult.put("ServicesHairCutsPrices", ServicesHairCutsPricesJSON);
            fakeserverResult.put("ServicesHairCutsDurations", ServicesHairCutsDurationsJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ServerResponseWithStoreInfo(fakeserverResult);
    }

    void RequestImage(final String ImageLink, Boolean IsTheImageForMainActivity) {
        if (IsTheImageForMainActivity) {
            ImageRequest imageRequest = new ImageRequest(ImageLink, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    Log.v("VolleyReceived", "IN SHOP DETAILS ACTIVITY" + response.toString());
                    ShopMainImageForMainActivity = response;
                    ShopMainImageForMainActivityAsString = BitmapToString(response);
                    ShopMainImageForMainActivityLink = ImageLink;
                    Toast.makeText(mContext, "Received Main Image", Toast.LENGTH_SHORT).show();

                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("VolleyErrors", "onErrorResponse: IN SHOPDETAILS ACTIVITY IMAGES ImageForMainActivity " + error.toString());
                }
            });
            requestQueue.add(imageRequest);
        } else {
            ImageRequest imageRequest = new ImageRequest(ImageLink, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    Log.v("VolleyReceived", "IN SHOP DETAILS ACTIVITY Portfolio Image "+ ImageLink);
                    PortfolioImages.add(response);
                    PortfolioImagesAsStrings.add(BitmapToString(response));
                    PortfolioImagesLinks.add(ImageLink);
                    Toast.makeText(mContext, "Received Portfolio Image", Toast.LENGTH_SHORT).show();
                  SaveUpdatedStoreDataToMemoryAndNotifyPortfolioRecyclerView();

                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("VolleyErrors", "onErrorResponse: IN SHOPDETAILS ACTIVITY IMAGES ImagesForPortfolio" + error.toString());
                }
            });
            requestQueue.add(imageRequest);
        }

    }

    void SaveUpdatedStoreDataToMemoryAndNotifyPortfolioRecyclerView() {
     //   ShopDetails_Frag4.ReceivedNewImagesNotifyRecyclerView();
   //     JSONObject UpdatedShopData=new JSONObject();
    /*    try {

            UpdatedShopData.put("ServicesHairCutsDuration",AddQuoationMarksToStringArrayList(ServicesHairCutsDuration));
            UpdatedShopData.put("ServicesHairCutsPrices", AddQuoationMarksToStringArrayList(ServicesHairCutsPrices));
            UpdatedShopData.put("ServicesHairCutsNames",AddQuoationMarksToStringArrayList(ServicesHairCutsNames) );

            UpdatedShopData.put("reviewersGivenStars", reviewersGivenStars);
            UpdatedShopData.put("reviewersCommentDate",AddQuoationMarksToStringArrayList(reviewersCommentDate) );
            UpdatedShopData.put("reviewersComments",AddQuoationMarksToStringArrayList(reviewersComments) );
            UpdatedShopData.put("reviewersNames",AddQuoationMarksToStringArrayList(reviewersNames) );


            UpdatedShopData.put("ShopMainImageForMainActivityAsString",AddQuotaionMarkToString(ShopMainImageForMainActivityAsString));
            UpdatedShopData.put("ShopMainImageForMainActivityLink",AddQuotaionMarkToString(ShopMainImageForMainActivityLink));


            UpdatedShopData.put("PortfolioImagesAsStrings",AddQuoationMarksToStringArrayList(PortfolioImagesAsStrings));
            UpdatedShopData.put("PortfolioImagesLinks", AddQuoationMarksToStringArrayList(PortfolioImagesLinks));

*/

         /*   String json=new Gson().toJson(UpdatedShopDataMap);
   ImageView image=findViewById(R.id.imageView2_Frag2);
   image.setImageBitmap(ConvertStringToBitmap(((ArrayList<String>)UpdatedShopData.get("PortfolioImagesAsStrings")).get(0)));*/
       //     Log.v("LoadFromCache","Save Updated "+(ArrayList<String>)UpdatedShopData.get("PortfolioImagesLinks"));
/*
        } catch (JSONException e) {
            Log.v("LoadFromCache","Problem in save updated data");
            e.printStackTrace();
        }*/
        Map<String,Object> UpdatedShopDataMap=new HashMap<>();

        UpdatedShopDataMap.put("ServicesHairCutsDuration",ServicesHairCutsDuration);
        UpdatedShopDataMap.put("ServicesHairCutsPrices", ServicesHairCutsPrices);
        UpdatedShopDataMap.put("ServicesHairCutsNames", ServicesHairCutsNames);
        UpdatedShopDataMap.put("reviewersGivenStars", reviewersGivenStars);
        UpdatedShopDataMap.put("reviewersCommentDate",reviewersCommentDate );
        UpdatedShopDataMap.put("reviewersComments",reviewersComments );
        UpdatedShopDataMap.put("reviewersNames",reviewersNames );
        UpdatedShopDataMap.put("ShopMainImageForMainActivityAsString",ShopMainImageForMainActivityAsString);
        UpdatedShopDataMap.put("ShopMainImageForMainActivityLink",ShopMainImageForMainActivityLink);
        UpdatedShopDataMap.put("PortfolioImagesAsStrings",PortfolioImagesAsStrings);
        UpdatedShopDataMap.put("PortfolioImagesLinks", PortfolioImagesLinks);


        JSONObject jsonObject=new JSONObject(UpdatedShopDataMap);
        Log.v("LoadFromCache","Updating Store Data to :"+jsonObject);
        writeNewShopDataToLocalMemory(jsonObject);
    }

    public String BitmapToString(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      //  Toast.makeText(this, "Bitmap Size is " + bitmap.getByteCount(), Toast.LENGTH_LONG).show();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 85, byteArrayOutputStream);
        byte[] byteImage = byteArrayOutputStream.toByteArray();
      // Toast.makeText(this, "ByteImage Size is " + byteImage.length, Toast.LENGTH_LONG).show();
      // Toast.makeText(this, "Base64 encoded Size is " + Base64.encodeToString(byteImage, Base64.DEFAULT).getBytes().length, Toast.LENGTH_LONG).show();
        return Base64.encodeToString(byteImage, Base64.DEFAULT);
    }

    public Bitmap ConvertStringToBitmap(String image) {
        byte[] bytes;
        bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    ArrayList<String> ParseJSONtoArrayListOfStrings(String JsonArrayAsString){
        ArrayList<String> parsedArrayList=new ArrayList<>();
        JsonArrayAsString=JsonArrayAsString.replace("[", "");
        JsonArrayAsString=JsonArrayAsString.replace("]", "");
        String onebackslash="\\\\";
        JsonArrayAsString=JsonArrayAsString.replaceAll(onebackslash,"");
        char quote='"';
        JsonArrayAsString=JsonArrayAsString.replaceAll(String.valueOf(quote), "");
        if(JsonArrayAsString.contains(",")){
            /// json array has more than one item

            int LastIndexOfComma=-1;
            while(true){
                int NewIndexOfComma= JsonArrayAsString.indexOf(",", LastIndexOfComma+1);
                if(NewIndexOfComma!=-1){

                    parsedArrayList.add(JsonArrayAsString.subSequence(LastIndexOfComma+1,NewIndexOfComma).toString());
                }else{
                    break;
                }
               LastIndexOfComma=NewIndexOfComma;
            }



        }else if(!JsonArrayAsString.contains(",") && JsonArrayAsString.length()>0){

            parsedArrayList.add(JsonArrayAsString);

        }

        return parsedArrayList;
    }

}
