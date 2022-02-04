package com.example.coifsalonclient.shopdetails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;

import com.example.coifsalonclient.AShop;
import com.example.coifsalonclient.CommonMethods;
import com.example.coifsalonclient.R;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopDetailsActivity extends FragmentActivity {


    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;

    ViewPager viewPager;
    TabLayout tabLayout;
    CustomFragmentPagerAdapter customFragmentPagerAdapter;

    public  ArrayList<Bitmap> portfolioPhotos = new ArrayList<>();

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

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        aShop = getIntent().getParcelableExtra("aShop");

        setUpViews();
        getAllImagesFromServer();
        getReviews();
    }

    private void setUpViews() {
        getViewsReferences();

        customFragmentPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        customFragmentPagerAdapter.addFragment(shopDetails_Frag1 = new ShopDetails_Frag1(this), "ShopDetails_Frag1");
        customFragmentPagerAdapter.addFragment(new ShopDetails_Frag2(this), "ShopDetails_Frag2");
        customFragmentPagerAdapter.addFragment(shopDetails_frag3 = new ShopDetails_Frag3(this), "ShopDetails_Frag3");
        customFragmentPagerAdapter.addFragment(shopDetails_frag4 = new ShopDetails_Frag4(this), "ShopDetails_Frag4");
        viewPager.setAdapter(customFragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                try{
                    tabLayout.getTabAt(i).select();
                }catch (Exception e){
                    Log.e("MyFirebase","Unexpected error occurred "+e);
                    Toast.makeText(mContext, "Unexpected error occurred",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


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
    private void getViewsReferences(){

        viewPager = findViewById(R.id.viewPagerShopDetails);
        tabLayout = findViewById(R.id.tabLayoutShopDetails);
    }
   private void getAllImagesFromServer() {
        portfolioPhotos.clear();

        for (String reference : aShop.getFreshPhotosReferencesFromServer()) {
            requestImage(reference);
        }
    }
    private void requestImage(final String photoStoragePath) {

        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference photoReference = storageReference.child(photoStoragePath);
        photoReference.getBytes(6 * 1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                portfolioPhotos.add(photo);
                shopDetails_frag4.receivedNewImagesNotifyRecyclerView();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                portfolioPhotos.add(null);
            }
        });

    }
    void getReviews() {
        aShop.getReviewersNames().clear();
        aShop.getReviewersComments().clear();
        aShop.getReviewersCommentDate().clear();
        aShop.setReviewersGivenStars(new float[]{});

        firebaseFirestore.collection("Shops").document(aShop.getShopUid()).collection("Reviews").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        try {
                            extractReviewsFromServerResponse(queryDocumentSnapshots);
                            shopDetails_frag3.ReceivedNewReviewsNotifyRecyclerView();
                        } catch (Exception e) {
                            Log.e("MyFirebase", "Error parsing reviews data");
                            Toast.makeText(mContext, "Unexpected error occurred",Toast.LENGTH_LONG).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("MyFirebase", "Error getting reviews");
                Toast.makeText(mContext, "Unexpected error occurred",Toast.LENGTH_LONG).show();
            }
        });
    }


  private void extractReviewsFromServerResponse(QuerySnapshot queryDocumentSnapshots){
        try{
            List<DocumentSnapshot> reviews = queryDocumentSnapshots.getDocuments();

            List<String> reviewersNames = new ArrayList<>();
            List<String> reviewersComments = new ArrayList<>();
            List<String> reviewersCommentDate = new ArrayList<>();
            List<Double> reviewersGivenStars = new ArrayList<>();

            for (int i = 0; i < reviews.size(); i++) {
                reviewersNames.add(reviews.get(i).getString("ReviewerName"));
                reviewersComments.add(reviews.get(i).getString("ReviewerComment"));
                Long reviewerCommentDateInMillis = (Long) reviews.get(i).get("ReviewerCommentDateInMillis");

                Date date = new Date(reviewerCommentDateInMillis);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String simpleDateString = simpleDateFormat.format(date);
                reviewersCommentDate.add(simpleDateString);
                reviewersGivenStars.add((Double) reviews.get(i).get("ReviewerGivenStars"));
            }


            aShop.setReviewersNames(reviewersNames);

            aShop.setReviewersComments(reviewersComments);

            aShop.setReviewersCommentDate(reviewersCommentDate);

            aShop.setReviewersGivenStars(CommonMethods.convertFloatListToPrimitivefloatArray(reviewersGivenStars));

            aShop.setHasLoadedReviews(true);
        }catch (Exception e){
            Log.e("MyFirebase", "Error getting extracting reviews");
            Toast.makeText(mContext, "Unexpected error occurred",Toast.LENGTH_LONG).show();
        }

  }

    public void book(final String ServicesHairCutToReserve) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Reserve Confirmation");
        alertDialogBuilder.setMessage("Do you want to book for " + ServicesHairCutToReserve + " ?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Map<String, Object> map = new HashMap<>();
                map.put("ClientFakeFirebaseUid", "null");
                map.put("ClientFireBaseUid", firebaseUser.getUid());
                map.put("PersonName", firebaseUser.getDisplayName());
                map.put("Services", ServicesHairCutToReserve);
                map.put("ShopUid", aShop.getShopUid());

                firebaseFirestore.collection("Shops").document(aShop.getShopUid()).collection("ClientsPending").document(firebaseUser.getUid())
                        .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        registerBookingOnClientFilesOnFirestore(map);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MyFirebase", "Failed to book");
                        Toast.makeText(mContext, "Failed to book",Toast.LENGTH_LONG).show();
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

    void registerBookingOnClientFilesOnFirestore(final Map<String, Object> map) {
        firebaseFirestore.collection("Clients").document(firebaseUser.getUid())
                .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                try{
                    aShop.setSuccessfullyBookedHaircut(map.get("Services").toString());
                    aShop.setBookedShop(true);
                    shopDetails_Frag1.BookingStatusChangedNotifyRecyclerViewAdapter();
                }catch (Exception e){
                    Log.e("MyFirebase", "Failed to book");
                    Toast.makeText(mContext, "Failed to book",Toast.LENGTH_LONG).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("MyFirebase", "Failed to book");
                firebaseFirestore.collection("Shops").document(aShop.getShopUid()).collection("ClientsPending").document(firebaseUser.getUid()).delete();
                Toast.makeText(mContext, "Failed to book",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void unBook() {
        firebaseFirestore.collection("Shops").document(aShop.getShopUid()).collection("ClientsPending").document(firebaseUser.getUid()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseFirestore.collection("Clients").document(firebaseUser.getUid()).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        aShop.setBookedShop(false);
                                        aShop.setSuccessfullyBookedHaircut(null);
                                        shopDetails_Frag1.BookingStatusChangedNotifyRecyclerViewAdapter();
                                    }
                                });
                    }
                });
    }


    public void addReview(final String ReviewerComment, final float ReviewerGivenStars) {

        Map<String, Object> map = new HashMap<>();
        map.put("ReviewerName", firebaseUser.getDisplayName());
        map.put("ReviewerComment", ReviewerComment);
        map.put("ReviewerGivenStars", ReviewerGivenStars);

        firebaseFirestore.collection("Shops").document(aShop.getShopUid()).collection("Reviews").document(firebaseUser.getUid())
                .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                try{
                    aShop.getReviewersNames().add(0, firebaseUser.getDisplayName());
                    aShop.getReviewersComments().add(0, ReviewerComment);
                    //this conversion thing might be problematic
                    ArrayList<Double> reviewersGivenStarsList = new ArrayList<>();

                    for (float number : aShop.getReviewersGivenStars()) {
                        reviewersGivenStarsList.add(Double.valueOf(number));
                    }
                    reviewersGivenStarsList.add(0, Double.valueOf(ReviewerGivenStars));

                    aShop.setReviewersGivenStars(CommonMethods.convertFloatListToPrimitivefloatArray(reviewersGivenStarsList));
                    //////////////////////////////////////////////
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    aShop.getReviewersCommentDate().add(0, simpleDateFormat.format(new Date()));
                    shopDetails_frag3.ReceivedNewReviewsNotifyRecyclerView();
                }catch (Exception e){
                    Log.e("MyFirebase", "Failed to parse review response "+e);
                    Toast.makeText(mContext, "Unexpected Error Occurred", Toast.LENGTH_LONG).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("MyFirebase", "Failed to add a review");
                Toast.makeText(mContext, "Unexpected Error Occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

}
