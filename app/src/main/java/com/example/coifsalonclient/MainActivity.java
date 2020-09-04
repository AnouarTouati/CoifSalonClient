package com.example.coifsalonclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context mContext;
    ArrayList<AShop> aShopsList = new ArrayList<>();
    ArrayList<String> shopsMainPhotoAsStrings = new ArrayList<>();
    ArrayList<Bitmap> shopsMainPhoto = new ArrayList<>();
    Integer indexOfShopPhotoToReceiveNext = 0;
    AShop successfullyBookedShop;

    EditText searchEditText;
    CustomRecyclerViewAdapter customRecyclerViewAdapter;
    RecyclerView recyclerView;
    TextView bookedShopMainActivityTextView;
    TextView bookedHairCutMainActivityTextView;
    Button goToBookedShopMainActivityButton;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        if(!isAuthenticated()){
            Intent goToSignInOrUp=new Intent(this,SignInOrUp.class);
            startActivity(goToSignInOrUp);
            this.finish();
        } else{
            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();

            // getActionBar().hide();

            bookedShopMainActivityTextView = findViewById(R.id.BookedShopMainActivityTextView);
            bookedShopMainActivityTextView.setVisibility(View.GONE);
            bookedHairCutMainActivityTextView = findViewById(R.id.BookedHairCutMainActivityTextView);
            bookedHairCutMainActivityTextView.setVisibility(View.GONE);
            goToBookedShopMainActivityButton = findViewById(R.id.GoToBookedShopFromMainActivityButton);
            goToBookedShopMainActivityButton.setVisibility(View.GONE);
            recyclerView = findViewById(R.id.searchResultRecyclerView);
            searchEditText = findViewById(R.id.searchEditText);

            customRecyclerViewAdapter = new CustomRecyclerViewAdapter(this, this,aShopsList, shopsMainPhoto);
            recyclerView.setAdapter(customRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            goToBookedShopMainActivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goToShopDetailsActivity = new Intent(mContext, ShopDetailsActivity.class);
                    goToShopDetailsActivity.putExtra("aShop", successfullyBookedShop);
                    mContext.startActivity(goToShopDetailsActivity);
                }
            });

            searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH || keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (keyEvent == null || !keyEvent.isShiftPressed()) {
                            search(searchEditText.getText().toString());
                            View view = getCurrentFocus();
                            if (view != null) {
                                //this code is used to close on screen keyboard
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            }
                            return true;
                        }
                    }
                    return false;
                }

            });


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //doing it here to prevent persistence of booking  and unbooking
        //and also load new data when book and get back to main activity

        // we shouldnt get here if we are not authenticated by just in case
        //double check
         if(isAuthenticated()){
             getListOfShopsData();
             getBookedShop();
         }

    }

    private boolean isAuthenticated(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser==null){return false;}
        else{return true;}

    }
    void getListOfShopsData() {

        clearDataForPreviousResults();

        firebaseFirestore.collection("Shops").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                serverResponseWithListOfShopsData(queryDocumentSnapshots.getDocuments());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("MyFirebase", e.getMessage());
            }
        });

    }
    void getBookedShop(){
        successfullyBookedShop=null;
        hideBookInfoVIEWSAndClear();
        firebaseFirestore.collection("Clients").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              if(documentSnapshot.contains("ShopUid")){
                getBookedShopData(documentSnapshot.get("ShopUid").toString(),documentSnapshot.get("Services").toString());
              }else{
                  successfullyBookedShop=null;
                  hideBookInfoVIEWSAndClear();
              }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            Log.v("MyFirebase","Failed to get booked shop status");
            }
        });
    }
    void getBookedShopData(String bookedShopUid, final String services){
        firebaseFirestore.collection("Shops").document(bookedShopUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                   successfullyBookedShop=convertDocumentSnapshotToAShop(documentSnapshot);
                   successfullyBookedShop.setBookedShop(true);
                   successfullyBookedShop.setSuccessfullyBookedHaircut(services);
                   showBookInfoViews();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            Log.v("MyFirebase","Failed to get booked shop data");
            }
        });
    }

    void showBookInfoViews(){
        bookedShopMainActivityTextView.setText(successfullyBookedShop.getShopName());
        bookedShopMainActivityTextView.setVisibility(View.VISIBLE);
        bookedHairCutMainActivityTextView.setText(successfullyBookedShop.getSuccessfullyBookedHaircut());
        bookedHairCutMainActivityTextView.setVisibility(View.VISIBLE);
        goToBookedShopMainActivityButton.setVisibility(View.VISIBLE);}

    void hideBookInfoVIEWSAndClear() {
        bookedShopMainActivityTextView.setVisibility(View.GONE);
        bookedHairCutMainActivityTextView.setVisibility(View.GONE);
        goToBookedShopMainActivityButton.setVisibility(View.GONE);
        bookedShopMainActivityTextView.setText("");
        bookedHairCutMainActivityTextView.setText("");
    }

    void serverResponseWithListOfShopsData(List<DocumentSnapshot> allShopsData) {
        try {

            if(allShopsData.size()>0){
                for (int i = 0; i < allShopsData.size(); i++) {
                    aShopsList.add(convertDocumentSnapshotToAShop(allShopsData.get(i)));
                }
                requestPhoto(aShopsList.get(indexOfShopPhotoToReceiveNext).getShopMainPhotoReference());

                // customRecyclerViewAdapter.notifyDataSetChanged(); NOTTIFYING DID NOT WORK PROPERLY SINCE SUCCESSFULLYBOKKEDSTORE AND HAIRCUT DID NOT UPDATE TO NEW VALUE
                customRecyclerViewAdapter = new CustomRecyclerViewAdapter(this,this, aShopsList, shopsMainPhoto);
                recyclerView.swapAdapter(customRecyclerViewAdapter, true);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Problem in ServerResponseWith Shops Data function", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
private void clearDataForPreviousResults(){
    aShopsList.clear();
    shopsMainPhoto.clear();
    shopsMainPhotoAsStrings.clear();
    indexOfShopPhotoToReceiveNext = 0;
    customRecyclerViewAdapter = new CustomRecyclerViewAdapter(this,this, aShopsList, shopsMainPhoto);
    recyclerView.swapAdapter(customRecyclerViewAdapter, true);
}
    private String getShopUidFromPath(String path) {
        path = path.replace("Shops/", "");
        return path;
    }

    void requestPhoto(String photoStoragePath) {

        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference photoReference = storageReference.child(photoStoragePath);
        photoReference.getBytes(6 * 1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                shopsMainPhoto.add(photo);
                shopsMainPhotoAsStrings.add(CommonMehods.bitmapToString(photo));
                customRecyclerViewAdapter.notifyDataSetChanged();
                indexOfShopPhotoToReceiveNext++;
                if (indexOfShopPhotoToReceiveNext < aShopsList.size()) {
                    requestPhoto(aShopsList.get(indexOfShopPhotoToReceiveNext).getShopMainPhotoReference());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("MyFirebase", "Getting Shop Main Photo Failed " + e.getMessage());
            }
        });

    }

    void search(String shopName) {
        clearDataForPreviousResults();
        //trim removes trailing and leading white spaces
        //split("\\s+") removes ALL white spaces between each two words
        String[] keyWords=shopName.trim().split("\\s+");
        firebaseFirestore.collection("Shops").whereIn("ShopName", Arrays.asList(keyWords)).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                serverResponseWithListOfShopsData(queryDocumentSnapshots.getDocuments());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("MyFirebase", e.getMessage());
            }
        });
    }

    AShop convertDocumentSnapshotToAShop(DocumentSnapshot snapshot){
        AShop aShop=new AShop();

           aShop.setShopUid(getShopUidFromPath(snapshot.getReference().getPath()));

           aShop.setShopName(snapshot.get("ShopName").toString());


           aShop.setSelectedCommune(snapshot.get("SelectedCommune").toString());
           aShop.setSelectedState(snapshot.get("SelectedState").toString());

           aShop.setShopMainPhotoReference(snapshot.get("MainShopPhotoReferenceInStorage").toString());

           if(snapshot.get("PhotosPathsInFireStorage")!=null){
               aShop.setFreshPhotosReferencesFromServer((List<String>) snapshot.get("PhotosPathsInFireStorage"));
           }else{
               aShop.setFreshPhotosReferencesFromServer(new ArrayList<String>());
           }


           if(snapshot.get("ServicesHairCutsNames")!=null){
               aShop.setServicesHairCutsNames((List<String>) snapshot.get("ServicesHairCutsNames"));

               aShop.setServicesHairCutsDuration((List<String>) snapshot.get("ServicesHairCutsDuration"));

               aShop.setServicesHairCutsPrices((List<String>) snapshot.get("ServicesHairCutsPrices"));

           }else{
               aShop.setServicesHairCutsNames(new ArrayList<String>());

               aShop.setServicesHairCutsDuration(new ArrayList<String>());

               aShop.setServicesHairCutsPrices(new ArrayList<String>());
           }

           if(snapshot.get("EmailAddress")!=null){
               aShop.setEmailAddress(snapshot.get("EmailAddress").toString());
           }else{
               aShop.setEmailAddress("");
           }

           if(snapshot.get("UseCoordinatesAKAaddMap")!=null){
               aShop.setUsesCoordinates((boolean) snapshot.get("UseCoordinatesAKAaddMap"));
               if ((boolean) snapshot.get("UseCoordinatesAKAaddMap")) {
                   if(snapshot.get("ShopLatitude")!=null && snapshot.get("ShopLongitude")!=null){
                       aShop.setShopLatitude((double) snapshot.get("ShopLatitude"));
                       aShop.setShopLongitude((double) snapshot.get("ShopLongitude"));}
                   }else{
                   aShop.setShopLatitude(0d);
                   aShop.setShopLongitude(0d);}
               }else{
               aShop.setUsesCoordinates(false);
           }

           if(snapshot.get("IsMen")!=null){
               aShop.setMen((boolean) snapshot.get("IsMen"));
           }else{
               aShop.setMen(null);
           }
           if(snapshot.get("ShopPhoneNumber")!=null){
               aShop.setShopPhoneNumber(snapshot.get("ShopPhoneNumber").toString());
           }else{
               aShop.setShopPhoneNumber("");
           }

           if(snapshot.get("FacebookLink")!=null){
               aShop.setFacebookLink(snapshot.get("FacebookLink").toString());
           }else{
               aShop.setFacebookLink("www.facebook.com");
           }
           if(snapshot.get("instagramLink")!=null){
               aShop.setInstagramLink(snapshot.get("instagramLink").toString());
           }else{
               aShop.setInstagramLink("www.instagram.com");
           }


            if(snapshot.get("Saturday")!=null)
                aShop.setSaturday(snapshot.get("Saturday").toString());

            if(snapshot.get("Sunday")!=null)
                aShop.setSunday(snapshot.get("Sunday").toString());

            if(snapshot.get("Monday")!=null)
           aShop.setMonday(snapshot.get("Monday").toString());

            if(snapshot.get("Tuesday")!=null)
           aShop.setTuesday(snapshot.get("Tuesday").toString());
            if(snapshot.get("Wednesday")!=null)
           aShop.setWednesday(snapshot.get("Wednesday").toString());

            if(snapshot.get("Thursday")!=null)
           aShop.setThursday(snapshot.get("Thursday").toString());

            if(snapshot.get("Friday")!=null)
           aShop.setFriday(snapshot.get("Friday").toString());

           return  aShop;
}
}
