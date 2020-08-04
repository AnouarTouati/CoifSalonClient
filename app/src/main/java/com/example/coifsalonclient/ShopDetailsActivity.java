package com.example.coifsalonclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ShopDetailsActivity extends FragmentActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;

    ViewPager viewPager;
    TabLayout tabLayout;
    CustomFragmentPagerAdapter customFragmentPagerAdapter;

    ArrayList<String> portfolioPhotosReferencesLocal = new ArrayList<>();//we use this in local memory so we are sure that we received the image pointed to by the link
    Integer indexOfImageToReceiveNext = 0;
    ArrayList<String> portfolioPhotosReferencesToBeRequested = new ArrayList<>();
    public static ArrayList<Bitmap> portfolioPhotos = new ArrayList<>();
    ArrayList<String> portfolioPhotosAsStrings = new ArrayList<>();
    AShop aShop;

    ShopDetails_Frag1 shopDetails_Frag1;
    ShopDetails_Frag3 shopDetails_frag3;
    ShopDetails_Frag4 shopDetails_frag4;

    Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        mContext = this;


        aShop=getIntent().getParcelableExtra("aShop");

       firebaseFirestore=FirebaseFirestore.getInstance();
       firebaseAuth=FirebaseAuth.getInstance();
       firebaseStorage=FirebaseStorage.getInstance();
       firebaseUser=firebaseAuth.getCurrentUser();

       weGotTheDataDisplayIt();
    }

/*
    void getShopDetailsInfoFromServer(String shopUid) {

    firebaseFirestore.collection("Shops").document(shopUid).collection("Info").document("Advanced").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
          serverResponseWithShopInfo(documentSnapshot);
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.v("MyFirebase",e.getMessage());
        }
    });

    }
*/
/*
    void serverResponseWithShopInfo(DocumentSnapshot shopAdvancedInfoSnapshot) {
        // dont change the order of these two functions
     //   writeNewShopDataToLocalMemory(shopAdvancedInfoSnapshot);//this should be here to save some info without images and it will be called again when download of images is done
        useTheAcquiredData(shopAdvancedInfoSnapshot, true);

    }
*/
/*

    void useTheAcquiredData(DocumentSnapshot shopAdvancedInfoSnapshot, Boolean MethodCalledFromServer) {
        try {
            freshPhotosReferencesFromServer.clear();
            freshPhotosReferencesFromServer.addAll((List<String>)shopAdvancedInfoSnapshot.get("PhotosPathsInFireStorage"));

       */
/*     emailAddress  = dataToUse.getString("emailAddress");
            password    = dataToUse.getString("password");
            firstName= dataToUse.getString("firstName");
            lastName     = dataToUse.getString("lastName");
            phoneNumber  = dataToUse.getString("phoneNumber");
            isBusinessOwner  = dataToUse.getBoolean("isBusinessOwner");
            salonName  = dataToUse.getString("salonName");
            selectedState   = dataToUse.getString("selectedState");
            SelectedCommune  = dataToUse.getString("SelectedCommune");*//*

            usesCoordinates = shopAdvancedInfoSnapshot.getBoolean("UsesCoordinates");
            shopLatitude = shopAdvancedInfoSnapshot.getDouble("ShopLatitude");
            shopLongitude = shopAdvancedInfoSnapshot.getDouble("ShopLongitude");
        */
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
*//*

            servicesHairCutsNames.clear();

            for (int i = 0; i < shopAdvancedInfoSnapshot.get("ServicesHairCutsNames").length(); i++) {
                servicesHairCutsNames.add(shopAdvancedInfoSnapshot.getJSONArray("ServicesHairCutsNames").getString(i));
            }


            servicesHairCutsPrices.clear();

            for (int i = 0; i < shopAdvancedInfoSnapshot.getJSONArray("ServicesHairCutsPrices").length(); i++) {
                servicesHairCutsPrices.add(shopAdvancedInfoSnapshot.getJSONArray("ServicesHairCutsPrices").getString(i));
            }


            servicesHairCutsDuration.clear();

            for (int i = 0; i < shopAdvancedInfoSnapshot.getJSONArray("ServicesHairCutsDuration").length(); i++) {
                servicesHairCutsDuration.add(shopAdvancedInfoSnapshot.getJSONArray("ServicesHairCutsDuration").getString(i));
            }


            reviewersNames.clear();

            for (int i = 0; i < shopAdvancedInfoSnapshot.getJSONArray("ReviewersNames").length(); i++) {
                reviewersNames.add(shopAdvancedInfoSnapshot.getJSONArray("ReviewersNames").getString(i));
            }


            reviewersComments.clear();

            for (int i = 0; i < shopAdvancedInfoSnapshot.getJSONArray("ReviewersComments").length(); i++) {
                reviewersComments.add(shopAdvancedInfoSnapshot.getJSONArray("ReviewersComments").getString(i));
            }


            reviewersCommentDate.clear();

            for (int i = 0; i < shopAdvancedInfoSnapshot.getJSONArray("ReviewersCommentDate").length(); i++) {
                reviewersCommentDate.add(shopAdvancedInfoSnapshot.getJSONArray("ReviewersCommentDate").getString(i));
            }


            reviewersGivenStars.clear();

            for (int i = 0; i < shopAdvancedInfoSnapshot.getJSONArray("ReviewersGivenStars").length(); i++) {
                reviewersGivenStars.add((float) shopAdvancedInfoSnapshot.getJSONArray("ReviewersGivenStars").getDouble(i));
            }

            if (MethodCalledFromServer) {
                /// WE CALL THIS SO WE UPDATE PROTFOLIO FRAGMENT
                /// AND SAVE THE NEW DATA WHEN ALL IMAGES ARE DOWNLOADED IN VOLLEY IMAGE LISTENER

                loadLocalData(shopUidFromRecyclerView);
            }
            weGotTheDataDisplayIt();

        } catch (JSONException e) {
            Toast.makeText(this, "Problem in useTheAcquiredData function", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }
*/

/*
    public void writeNewShopDataToLocalMemory(JSONObject NewShopDataJSON) {
        try {

            String localMemoryJsonAsString = CommonMehods.loadJSONFile("ShopsData.txt", getApplicationContext());
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
*/
    public void loadLocalData(String shopUid) {

        try {
            portfolioPhotos.clear();
            portfolioPhotosAsStrings.clear();

            String jsonAsString = CommonMehods.loadJSONFile("ShopsData.txt", getApplicationContext());

            if (jsonAsString != null) {

                JSONObject jsonObject = new JSONObject(jsonAsString);

                if (jsonObject.has(shopUid)) {


                    JSONObject ShopDataJSONObject = jsonObject.getJSONObject(shopUid);


                    if (ShopDataJSONObject.has("PortfolioImagesLinks")) {
                        ArrayList<String> ListOfImagesThatDidNotChange = new ArrayList<>();
                        ArrayList<Integer> IndexOfTheImageThatDidNotChange = new ArrayList<>();
                        ArrayList<String> ListOfImagesLinksThatDidNotChange = new ArrayList<>();

                        ArrayList<String> PortfolioImagesLinksLocal = new ArrayList<>();
                        for (int i = 0; i < ShopDataJSONObject.getJSONArray("PortfolioImagesLinks").length(); i++) {
                            PortfolioImagesLinksLocal.add(i, ShopDataJSONObject.getJSONArray("PortfolioImagesLinks").getString(i));
                        }

                        Log.v("LoadFromCache", "Loaded Portfolio Links :" + PortfolioImagesLinksLocal);

                        ArrayList<String> PortfolioImagesAsStringsLocal = new ArrayList<>();
                        for (int i = 0; i < ShopDataJSONObject.getJSONArray("PortfolioImagesAsStrings").length(); i++) {
                            PortfolioImagesAsStringsLocal.add(i, ShopDataJSONObject.getJSONArray("PortfolioImagesAsStrings").getString(i));
                        }
                        portfolioPhotosReferencesToBeRequested.clear();
                        indexOfImageToReceiveNext = 0;
                        for (int i = 1; i < aShop.getFreshPhotosReferencesFromServer().size(); i++) { //i=1 to ignore the main shop image


                            Boolean LinkNotFound = true;
                            for (int j = 0; j < PortfolioImagesLinksLocal.size(); j++) {

                                if (PortfolioImagesLinksLocal.get(j).equals(aShop.getFreshPhotosReferencesFromServer().get(i))) {
                                    LinkNotFound = false;
                                    IndexOfTheImageThatDidNotChange.add(j);
                                    ListOfImagesThatDidNotChange.add(PortfolioImagesAsStringsLocal.get(j));
                                    ListOfImagesLinksThatDidNotChange.add(PortfolioImagesLinksLocal.get(j));
                                }
                            }
                            if (LinkNotFound) {


                                portfolioPhotosReferencesToBeRequested.add(aShop.getFreshPhotosReferencesFromServer().get(i));
                            }


                        }
                        if (portfolioPhotosReferencesToBeRequested.size() > 0) {
                            requestImage(portfolioPhotosReferencesToBeRequested.get(indexOfImageToReceiveNext));//indexOfImageToReceiveNext should equal zero at this stage
                        }
                        ShopDataJSONObject.remove("PortfolioImagesAsStrings");
                        ShopDataJSONObject.remove("PortfolioImagesLinks");
                        portfolioPhotosAsStrings.clear();
                        portfolioPhotosAsStrings = ListOfImagesThatDidNotChange;
                        portfolioPhotosReferencesLocal.clear();
                        portfolioPhotosReferencesLocal = ListOfImagesLinksThatDidNotChange;

                        portfolioPhotos.clear();
                        for (int i = 0; i < PortfolioImagesAsStringsLocal.size(); i++) {
                            portfolioPhotos.add(CommonMehods.convertStringToBitmap(PortfolioImagesAsStringsLocal.get(i)));
                        }


                    } else {
                        portfolioPhotosReferencesToBeRequested.clear();
                        indexOfImageToReceiveNext = 0;
                        portfolioPhotosReferencesToBeRequested.addAll(aShop.getFreshPhotosReferencesFromServer());
                        if (portfolioPhotosReferencesToBeRequested.size() > 0) {
                            requestImage(portfolioPhotosReferencesToBeRequested.get(indexOfImageToReceiveNext));//indexOfImageToReceiveNext should equal zero at this stage
                        }
                      /*
                       for(int i=1;i<imagesLinkFromRecyclerView.size();i++){//i=1 to ignore the main shop image
                           requestImage(imagesLinkFromRecyclerView.get(i));
                       }

                       */
                    }

                }
            } else {
                //null is handled in the calling function loadJSONFile()
            }


        } catch (JSONException e) {
            Log.v("LoadFromCache", "Error parsing JSON in ShopDetailsActivity loadLocalData function");
            e.printStackTrace();
        }
    }

    void weGotTheDataDisplayIt() {

        viewPager = findViewById(R.id.viewPagerShopDetails);

        customFragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        customFragmentPagerAdapter.addFragment(shopDetails_Frag1=new ShopDetails_Frag1(this), "ShopDetails_Frag1");
        customFragmentPagerAdapter.addFragment(new ShopDetails_Frag2(this), "ShopDetails_Frag2");
        customFragmentPagerAdapter.addFragment(shopDetails_frag3=new ShopDetails_Frag3(this), "ShopDetails_Frag3");
        customFragmentPagerAdapter.addFragment(shopDetails_frag4=new ShopDetails_Frag4(), "ShopDetails_Frag4");
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

   /*
    public static void getShopServicesInfo(String shopName) {
*//*
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
*//*
*//*
      JSONObject getShopInfo=new JSONObject();
        try {
            getShopInfo.put("RequestShopServicesInfo_ShopName", shopName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, getShopInfo, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);*//*
    }

    public static void getShopReviewsInfo(String shopName) {
*//*
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
*//*
*//*
        JSONObject getShopInfo=new JSONObject();
        try {
            getShopInfo.put("RequestShopReviewsInfo_ShopName", shopName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, getShopInfo, volleyListener, volleyErrorListener);
        requestQueue.add(jsonObjectRequest);*//*
    }
*/
/*
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
*/
    public void book(final String ServicesHairCutToReserve) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Reserve Confirmation");
        alertDialogBuilder.setMessage("Do you want to book for " + ServicesHairCutToReserve + " ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               Map<String,Object> map=new HashMap<>();
               map.put("ServicesHairCut",ServicesHairCutToReserve);

            firebaseFirestore.collection("Shops").document(aShop.getShopUid()).collection("ClientsPending").document(firebaseUser.getUid())
                    .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    aShop.setSuccessfullyBookedHaircut(ServicesHairCutToReserve);
                    shopDetails_Frag1.BookWasSuccessfulNotifyRecyclerViewAdapter();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                 Log.v("MyFirebase","Failed to book");
                }
            });
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mContext, "We didn't book anything", Toast.LENGTH_LONG).show();

            }
        });
        alertDialogBuilder.create().show();


    }

    public void addReview(final String ReviewerName, final String ReviewerComment, final float ReviewerGivenStars) {

        Map<String, Object> map = new HashMap<>();
        map.put("ReviewerName", ReviewerName);
        map.put("ReviewerComment", ReviewerComment);
        map.put("ReviewerGivenStars", ReviewerGivenStars);

      firebaseFirestore.collection("Shops").document(aShop.getShopUid()).collection("Reviews").document(firebaseUser.getUid())
              .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
          aShop.getReviewersNames().add(0,ReviewerName);
          aShop.getReviewersComments().add(0,ReviewerComment);
          aShop.getReviewersGivenStars().add(0,ReviewerGivenStars);
          aShop.getReviewersCommentDate().add(0, new Date().toString());
          shopDetails_frag3.ReceivedNewReviewsNotifyRecyclerView();
          
          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
          Log.v("MyFirebase","Failed to add a review");
          }
      });
    }
/*
    void serverResponseWithAddReviewResult(JSONObject AddReviewResult) {
        try {
            if (AddReviewResult.getString("Successful").equals("True")) {

               aShop.getReviewersNames().add(AddReviewResult.getString("ReviewerName"));
               aShop.getReviewersComments().add(AddReviewResult.getString("ReviewerComment"));
               aShop.getReviewersGivenStars().add((float) AddReviewResult.getDouble("ReviewerGivenStars"));
               aShop.getReviewersCommentDate().add(AddReviewResult.getString("ReviewDate"));
             //   saveUpdatedShopDataToMemoryAndNotifyPortfolioRecyclerView();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

*/
    void requestImage(final String photoStoragePath) {

        StorageReference storageReference=firebaseStorage.getReference();
        StorageReference photoReference=storageReference.child(photoStoragePath);
        photoReference.getBytes(6*1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                portfolioPhotos.add(photo);
                portfolioPhotosAsStrings.add(CommonMehods.bitmapToString(photo));
                portfolioPhotosReferencesLocal.add(photoStoragePath);
                //  saveUpdatedShopDataToMemoryAndNotifyPortfolioRecyclerView();
                indexOfImageToReceiveNext++;
                if (indexOfImageToReceiveNext < portfolioPhotosReferencesToBeRequested.size()) {
                    requestImage(portfolioPhotosReferencesToBeRequested.get(indexOfImageToReceiveNext));
                }
                shopDetails_frag4.receivedNewImagesNotifyRecyclerView();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                portfolioPhotos.add(null);
                portfolioPhotosAsStrings.add(null);
                portfolioPhotosReferencesLocal.add(null);

                indexOfImageToReceiveNext++;
                if (indexOfImageToReceiveNext < portfolioPhotosReferencesToBeRequested.size()) {
                    requestImage(portfolioPhotosReferencesToBeRequested.get(indexOfImageToReceiveNext));
                }
            }
        });

    }

/*
    void saveUpdatedShopDataToMemoryAndNotifyPortfolioRecyclerView() {
        if (ShopDetails_Frag4.mContext != null) {
            ShopDetails_Frag4.receivedNewImagesNotifyRecyclerView();
        }
        if (ShopDetails_Frag3.mContext != null) {
            ShopDetails_Frag3.ReceivedNewReviewsNotifyRecyclerView();
        }
        Map<String, Object> UpdatedShopDataMap = new HashMap<>();

        UpdatedShopDataMap.put("ServicesHairCutsDuration", servicesHairCutsDuration);
        UpdatedShopDataMap.put("ServicesHairCutsPrices", servicesHairCutsPrices);
        UpdatedShopDataMap.put("ServicesHairCutsNames", servicesHairCutsNames);
        UpdatedShopDataMap.put("ReviewersGivenStars", reviewersGivenStars);
        UpdatedShopDataMap.put("ReviewersCommentDate", reviewersCommentDate);
        UpdatedShopDataMap.put("ReviewersComments", reviewersComments);
        UpdatedShopDataMap.put("ReviewersNames", reviewersNames);
        UpdatedShopDataMap.put("PortfolioImagesAsStrings", portfolioPhotosAsStrings);
        UpdatedShopDataMap.put("PortfolioImagesLinks", portfolioPhotosReferencesLocal);

        UpdatedShopDataMap.put("UsesCoordinates", usesCoordinates);
        UpdatedShopDataMap.put("ShopLatitude", shopLatitude);
        UpdatedShopDataMap.put("ShopLongitude", shopLongitude);

        JSONObject jsonObject = new JSONObject(UpdatedShopDataMap);

        writeNewShopDataToLocalMemory(jsonObject);
    }
*/

}
