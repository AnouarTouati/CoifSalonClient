package com.example.coifsalonclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String URL="http://192.168.43.139:8888/Client.php";
    Response.Listener<JSONObject> volleyListener;
    Response.ErrorListener volleyErrorListener;
    RequestQueue requestQueue;
    Context mContext;

    ArrayList<String> shopsNames =new ArrayList<>();
    ArrayList<String> shopsNamesOriginal =new ArrayList<>();
    ArrayList<String> shopsAddresses =new ArrayList<>();
    ArrayList<String> shopsImagesAsStrings =new ArrayList<>();
    ArrayList<Bitmap> shopsImages =new ArrayList<>();
    ArrayList<ArrayList<String>> shopsImagesLinks =new ArrayList<>();
    Integer indexOfImageToReceiveNext =0;

    EditText searchEditText;
    CustomRecyclerViewAdapter customRecyclerViewAdapter;
    RecyclerView recyclerView;
    TextView bookedShopMainActivityTextView;
    TextView bookedHairCutMainActivityTextView;
    Button goToBookedShopMainActivityButton;
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
                serverResponseWithBookForMainActivity(response.getJSONObject("BookInfoForMainActivity"));
                serverResponseWithListOfShopsMainDataOnly(response.getJSONObject("ListOfShopsMainDataOnly"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
};

        shopsNames.add("Marouan");
        shopsNames.add("Anouar");
        shopsNames.add("Said");
        shopsNames.add("Saleh");
        shopsNames.add("Marzek");

        recyclerView=findViewById(R.id.searchResultRecyclerView);
        customRecyclerViewAdapter=new CustomRecyclerViewAdapter(this, shopsNames, shopsAddresses, shopsImages, shopsImagesLinks, successfullyBookedShop, successfullyBookedHaircut);
        recyclerView.setAdapter(customRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue= Volley.newRequestQueue(this);

     searchEditText=findViewById(R.id.searchEditText);
     searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
         @Override
         public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if(actionId== EditorInfo.IME_ACTION_DONE || actionId==EditorInfo.IME_ACTION_SEARCH ||keyEvent!=null && keyEvent.getAction()==KeyEvent.ACTION_DOWN && keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER ){
                    if(keyEvent==null || !keyEvent.isShiftPressed()){
                        search(searchEditText.getText().toString());
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

        bookedShopMainActivityTextView =findViewById(R.id.BookedShopMainActivityTextView);
        bookedShopMainActivityTextView.setVisibility(View.GONE);
        bookedHairCutMainActivityTextView =findViewById(R.id.BookedHairCutMainActivityTextView);
        bookedHairCutMainActivityTextView.setVisibility(View.GONE);
        goToBookedShopMainActivityButton =findViewById(R.id.GoToBookedShopFromMainActivityButton);
        goToBookedShopMainActivityButton.setVisibility(View.GONE);
       goToBookedShopMainActivityButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent goToShopDetailsActivity = new Intent(mContext, ShopDetailsActivity.class);
               goToShopDetailsActivity.putExtra("ShopName", successfullyBookedShop);
               goToShopDetailsActivity.putExtra("ImagesLinks", shopsImagesLinks.get(shopsNames.indexOf(successfullyBookedShop)));
               goToShopDetailsActivity.putExtra("SuccessfullyBookedShop", successfullyBookedShop);
               goToShopDetailsActivity.putExtra("SuccessfullyBookedHaircut", successfullyBookedHaircut);
               mContext.startActivity(goToShopDetailsActivity);
           }
       });
        getListOfShopsMainDataOnly();


    }



    void getListOfShopsMainDataOnly(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("Request", "ListOfShopsMainDataOnly");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, jsonObject,volleyListener,volleyErrorListener );
        requestQueue.add(jsonObjectRequest);
    }
void serverResponseWithBookForMainActivity(JSONObject BookInfoForMainActivityJSONObject){
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
            bookedShopMainActivityTextView.setText(successfullyBookedShop);
            bookedShopMainActivityTextView.setVisibility(View.VISIBLE);
            bookedHairCutMainActivityTextView.setText(successfullyBookedHaircut);
            bookedHairCutMainActivityTextView.setVisibility(View.VISIBLE);
            goToBookedShopMainActivityButton.setVisibility(View.VISIBLE);
        }else{
            hideBookInfoVIEWS();
        }

    }else{

        hideBookInfoVIEWS();
    }

}  void hideBookInfoVIEWS(){
        bookedShopMainActivityTextView.setVisibility(View.GONE);
        bookedHairCutMainActivityTextView.setVisibility(View.GONE);
        goToBookedShopMainActivityButton.setVisibility(View.GONE);
    }
    void serverResponseWithListOfShopsMainDataOnly(JSONObject ListOfShopsMainDataOnlyJSONObjectResponse){
        try {
            shopsNames.clear();
            for(int i=0;i<ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsNames").length();i++){
              shopsNames.add(ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsNames").getString(i));
            }

            shopsAddresses.clear();
            for(int i=0;i<ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsAddresses").length();i++){
                shopsAddresses.add(ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsAddresses").getString(i));
            }


            shopsImagesLinks.clear();
            for (int i=0;i<ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsImagesLinks").length();i++){
                ArrayList<String> ListOfLinksOfEachShop= new ArrayList<>();
                for(int j=0;j<ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsImagesLinks").getJSONArray(i).length();j++){
                    ListOfLinksOfEachShop.add(ListOfShopsMainDataOnlyJSONObjectResponse.getJSONArray("ShopsImagesLinks").getJSONArray(i).getString(j));
                }
               shopsImagesLinks.add(ListOfLinksOfEachShop);
            }
            shopsImages.clear();
            indexOfImageToReceiveNext =0;
            requestImage(shopsImagesLinks.get(indexOfImageToReceiveNext).get(0));


           // customRecyclerViewAdapter.notifyDataSetChanged(); NOTTIFYING DID NOT WORK PROPERLY SINCE SUCCESSFULLYBOKKEDSTORE AND HAIRCUT DID NOT UPDATE TO NEW VALUE
            customRecyclerViewAdapter=new CustomRecyclerViewAdapter(this, shopsNames, shopsAddresses, shopsImages, shopsImagesLinks, successfullyBookedShop, successfullyBookedHaircut);
            recyclerView.swapAdapter(customRecyclerViewAdapter, true);

        } catch (JSONException e) {
            Toast.makeText(this, "Problem in ServerResponseWith Shops Data function", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


void requestImage(String Link){

    ImageRequest imageRequest=new ImageRequest(Link, new Response.Listener<Bitmap>() {
        @Override
        public void onResponse(Bitmap response) {
        shopsImages.add(response);
        shopsImagesAsStrings.add(CommonMehods.bitmapToString(response));
        customRecyclerViewAdapter.notifyDataSetChanged();
        indexOfImageToReceiveNext++;
        if(indexOfImageToReceiveNext < shopsImagesLinks.size()){
            requestImage(shopsImagesLinks.get(indexOfImageToReceiveNext).get(0));
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

    void search(String criteria){
//////////////////////////////////////////////////////////////////////
       /// USE CODE BELOW FOR LOCAL SEARCH ON AVAILBLE LIST
/////////////////////////////////////////////////////////////////////
/*
      for (int i = 0; i< shopsNames.size(); i++){

            if(!shopsNamesOriginal.contains(shopsNames.get(i))){
                shopsNamesOriginal.add(shopsNames.get(i));
            }
        }

        shopsNames.clear();
        String lowercaseCriteria=criteria.toLowerCase();
        for (int i = 0; i< shopsNamesOriginal.size(); i++){

            String nameOriginalLowerCase= shopsNamesOriginal.get(i).toLowerCase();
            if(nameOriginalLowerCase.contains(lowercaseCriteria)){
                shopsNames.add(shopsNamesOriginal.get(i));
            }
        }
        customRecyclerViewAdapter.notifyDataSetChanged();
*/

//////////////////////////////////////////////////////////////////////
        /// USE CODE BELOW FOR ONLINE SEARCH ON SERVER SIDE RESULT WILL BE DISPLAYED FROM FUNCTION serverResponseWithListOfShopsMainDataOnly
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

}
