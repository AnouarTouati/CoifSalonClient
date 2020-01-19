package com.example.coifsalonclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String URL="http://192.168.43.139:8888/Client.php";
    Response.Listener<JSONObject> volleyListener;
    Response.ErrorListener volleyErrorListener;
    RequestQueue requestQueue;
    Context mContext;

    ArrayList<String> ShopsNames =new ArrayList();
    ArrayList<String> ShopsNamesOriginal =new ArrayList();
    ArrayList<String> ShopsAddresses =new ArrayList<>();
    ArrayList<String> ShopsImagesAsStrings =new ArrayList<>();
    ArrayList<Bitmap> ShopsImages =new ArrayList<>();
    ArrayList<ArrayList<String>> ShopsImagesLinks =new ArrayList<>();
    Integer IndexOfImageToReceiveNext =0;

    EditText searchEditText;
    CustomRecyclerViewAdapter customRecyclerViewAdapter;
    RecyclerView recyclerView;
    TextView BookedShopMainActivityTextView;
    TextView BookedHairCutMainActivityTextView;
    Button GoToBookedShopMainActivityButton;
    public  String successfullyBookedHaircut =null;
    public  String successfullyBookedShop =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=this;
      // getActionBar().hide();
volleyErrorListener=new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Log.v("VolleyErrors", "onErrorResponse: IN MAIN ACTIVITY "+error.toString());


    }
};
volleyListener=new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(JSONObject response) {
        Log.v("VolleyReceived","IN MAIN ACTIVITY"+ response.toString());

        if(response.has("ListOfShopsMainDataOnly")){
            try {
                //DONT FLIP THE ORDER OF THESE TWO FUNCTIONS
                ServerResponseWithBookForMainActivity(response.getJSONObject("BookInfoForMainActivity"));
                ServerResponseWithListOfShopsMainDataOnly(response.getJSONObject("ListOfShopsMainDataOnly"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
};

        ShopsNames.add("Marouan");
        ShopsNames.add("Anouar");
        ShopsNames.add("Said");
        ShopsNames.add("Saleh");
        ShopsNames.add("Marzek");

        recyclerView=findViewById(R.id.searchResultRecyclerView);
        customRecyclerViewAdapter=new CustomRecyclerViewAdapter(this, ShopsNames, ShopsAddresses, ShopsImages, ShopsImagesLinks, successfullyBookedShop, successfullyBookedHaircut);
        recyclerView.setAdapter(customRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue= Volley.newRequestQueue(this);

     searchEditText=findViewById(R.id.searchEditText);
     searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
         @Override
         public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if(actionId== EditorInfo.IME_ACTION_DONE || actionId==EditorInfo.IME_ACTION_SEARCH ||keyEvent!=null && keyEvent.getAction()==KeyEvent.ACTION_DOWN && keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER ){
                    if(keyEvent==null || !keyEvent.isShiftPressed()){
                        Search(searchEditText.getText().toString());
                        View view = getCurrentFocus();
                        if (view != null) {
                            //this code is used to close on screen keyboard
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        }
                          return true;
                     }
             }
             return false;
         }

     });

        BookedShopMainActivityTextView =findViewById(R.id.BookedShopMainActivityTextView);
        BookedShopMainActivityTextView.setVisibility(View.GONE);
        BookedHairCutMainActivityTextView=findViewById(R.id.BookedHairCutMainActivityTextView);
        BookedHairCutMainActivityTextView.setVisibility(View.GONE);
        GoToBookedShopMainActivityButton =findViewById(R.id.GoToBookedShopFromMainActivityButton);
        GoToBookedShopMainActivityButton.setVisibility(View.GONE);
       GoToBookedShopMainActivityButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent goToShopDetailsActivity = new Intent(mContext, ShopDetailsActivity.class);
               goToShopDetailsActivity.putExtra("ShopName", successfullyBookedShop);
               goToShopDetailsActivity.putExtra("ImagesLinks", ShopsImagesLinks.get(ShopsNames.indexOf(successfullyBookedShop)));
               goToShopDetailsActivity.putExtra("successfullyBookedShop", successfullyBookedShop);
               goToShopDetailsActivity.putExtra("successfullyBookedHaircut", successfullyBookedHaircut);
               mContext.startActivity(goToShopDetailsActivity);
           }
       });
        GetListOfShopsMainDataOnly();


    }


    void showToast(){
        Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
    }
    void showToast(String toshow){
        Toast.makeText(this, toshow, Toast.LENGTH_LONG).show();
    }



    void GetListOfShopsMainDataOnly(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("Request", "ListOfShopsMainDataOnly");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, jsonObject,volleyListener,volleyErrorListener );
        requestQueue.add(jsonObjectRequest);
    }
void ServerResponseWithBookForMainActivity(JSONObject BookInfoForMainActivityJSONObject){
    try {
        successfullyBookedHaircut =BookInfoForMainActivityJSONObject.getString("ServicesHairCutToReserved");
    } catch (JSONException e) {
        e.printStackTrace();
    }
    try {
        successfullyBookedShop =BookInfoForMainActivityJSONObject.getString("BookedShop");
    } catch (JSONException e) {
        e.printStackTrace();
    }
    if(successfullyBookedShop !=null && successfullyBookedHaircut !=null){
        if(successfullyBookedShop.length()>0 && successfullyBookedHaircut.length()>0){
            BookedShopMainActivityTextView.setText(successfullyBookedShop);
            BookedShopMainActivityTextView.setVisibility(View.VISIBLE);
            BookedHairCutMainActivityTextView.setText(successfullyBookedHaircut);
            BookedHairCutMainActivityTextView.setVisibility(View.VISIBLE);
            GoToBookedShopMainActivityButton.setVisibility(View.VISIBLE);
        }else{
            HideBookInfoVIEWS();
        }

    }else{

        HideBookInfoVIEWS();
    }

}  void HideBookInfoVIEWS(){
        BookedShopMainActivityTextView.setVisibility(View.GONE);
        BookedHairCutMainActivityTextView.setVisibility(View.GONE);
        GoToBookedShopMainActivityButton.setVisibility(View.GONE);
    }
    void ServerResponseWithListOfShopsMainDataOnly(JSONObject ListOfShopsMainDataOnlyJSONObjectResponse){
        try {
            ShopsNames.clear();
            for(int i=0;i<ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsNames").length();i++){
              ShopsNames.add(ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsNames").getString(i));
            }

            ShopsAddresses.clear();
            for(int i=0;i<ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsAddresses").length();i++){
                ShopsAddresses.add(ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsAddresses").getString(i));
            }


            ShopsImagesLinks.clear();
            for (int i=0;i<ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsImagesLinks").length();i++){
                ArrayList<String> ListOfLinksOfEachShop= new ArrayList<>();
                for(int j=0;j<ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsImagesLinks").getJSONArray(i).length();j++){
                    ListOfLinksOfEachShop.add(ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsImagesLinks").getJSONArray(i).getString(j));
                }
               ShopsImagesLinks.add(ListOfLinksOfEachShop);
            }
            ShopsImages.clear();
            IndexOfImageToReceiveNext=0;
            RequestImage(ShopsImagesLinks.get(IndexOfImageToReceiveNext).get(0));


           // customRecyclerViewAdapter.notifyDataSetChanged(); NOTTIFYING DID NOT WORK PROPERLY SINCE SUCCESSFULLYBOKKEDSTORE AND HAIRCUT DID NOT UPDATE TO NEW VALUE
            customRecyclerViewAdapter=new CustomRecyclerViewAdapter(this, ShopsNames, ShopsAddresses, ShopsImages, ShopsImagesLinks, successfullyBookedShop, successfullyBookedHaircut);
            recyclerView.swapAdapter(customRecyclerViewAdapter, true);

        } catch (JSONException e) {
            Toast.makeText(this, "Problem in ServerResponseWith Shops Data function", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


void RequestImage(String Link){

    ImageRequest imageRequest=new ImageRequest(Link, new Response.Listener<Bitmap>() {
        @Override
        public void onResponse(Bitmap response) {
        ShopsImages.add(response);
        ShopsImagesAsStrings.add(BitmapToString(response));
        customRecyclerViewAdapter.notifyDataSetChanged();
        IndexOfImageToReceiveNext++;
        if(IndexOfImageToReceiveNext <ShopsImagesLinks.size()){
            RequestImage(ShopsImagesLinks.get(IndexOfImageToReceiveNext).get(0));
        }

        }
    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.v("VolleyErrors", "onErrorResponse: IN MAIN ACTIVITY IMAGES " + error.toString());
        }
    });
    requestQueue.add(imageRequest);
}

    void Search(String criteria){
//////////////////////////////////////////////////////////////////////
       /// USE CODE BELOW FOR LOCAL SEARCH ON AVAILBLE LIST
/////////////////////////////////////////////////////////////////////
/*
      for (int i = 0; i< ShopsNames.size(); i++){

            if(!ShopsNamesOriginal.contains(ShopsNames.get(i))){
                ShopsNamesOriginal.add(ShopsNames.get(i));
            }
        }

        ShopsNames.clear();
        String lowercaseCriteria=criteria.toLowerCase();
        for (int i = 0; i< ShopsNamesOriginal.size(); i++){

            String nameOriginalLowerCase= ShopsNamesOriginal.get(i).toLowerCase();
            if(nameOriginalLowerCase.contains(lowercaseCriteria)){
                ShopsNames.add(ShopsNamesOriginal.get(i));
            }
        }
        customRecyclerViewAdapter.notifyDataSetChanged();
*/

//////////////////////////////////////////////////////////////////////
        /// USE CODE BELOW FOR ONLINE SEARCH ON SERVER SIDE RESULT WILL BE DISPLAYED FROM FUNCTION ServerResponseWithListOfShopsMainDataOnly
        /// THIS MEANS THE RESULT SHOULD BE RETURNED IN JSONOBJECT WITH NAME ListOfShopsMainDataOnly
/////////////////////////////////////////////////////////////////////
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("Request", "Search");
            jsonObject.put("Criteria",criteria);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, jsonObject,volleyListener,volleyErrorListener );
        requestQueue.add(jsonObjectRequest);

    }

    public String BitmapToString(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
     //   Toast.makeText(this, "Bitmap Size is " + bitmap.getByteCount(), Toast.LENGTH_LONG).show();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 85, byteArrayOutputStream);
        byte[] byteImage = byteArrayOutputStream.toByteArray();
     //   Toast.makeText(this, "ByteImage Size is " + byteImage.length, Toast.LENGTH_LONG).show();
       // Toast.makeText(this, "Base64 encoded Size is " + Base64.encodeToString(byteImage, Base64.DEFAULT).getBytes().length, Toast.LENGTH_LONG).show();
        return Base64.encodeToString(byteImage, Base64.DEFAULT);
    }

    public  Bitmap ConvertStringToBitmap(String image){
        byte[] bytes;
        bytes= Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
