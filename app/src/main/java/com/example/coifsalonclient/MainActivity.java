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
    private static final String URL="http://192.168.43.139:81/SecondPage.php";
    Response.Listener<JSONObject> volleyListener;
    Response.ErrorListener volleyErrorListener;
    RequestQueue requestQueue;
    Context mContext;

    ArrayList<String> StoresNames =new ArrayList();
    ArrayList<String> StoresNamesOriginal =new ArrayList();
    ArrayList<String> StoresAddresses =new ArrayList<>();
    ArrayList<String> StoresImagesAsStrings=new ArrayList<>();
    ArrayList<Bitmap> StoresImages=new ArrayList<>();
    ArrayList<ArrayList<String>> StoresImagesLinks=new ArrayList<>();

    EditText searchEditText;
    CustomRecyclerViewAdapter customRecyclerViewAdapter;
    RecyclerView recyclerView;
    TextView BookedStoreMainActivityTextView;
    TextView BookedHairCutMainActivityTextView;
    Button GoToBookedStoreMainActivityButton;
    public  String successfullyBookedServicesHaircut=null;
    public  String successfullyBookedStore=null;


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

        if(response.has("ListOfStoresMainDataOnly")){
            try {
                ServerResponseWithListOfStoresMainDataOnly(response.getJSONObject("ListOfStoresMainDataOnly"));
                ServerResponseWithBookForMainActivity(response.getJSONObject("BookInfoForMainActivity"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
};

        StoresNames.add("Marouan");
        StoresNames.add("Anouar");
        StoresNames.add("Said");
        StoresNames.add("Saleh");
        StoresNames.add("Marzek");

        recyclerView=findViewById(R.id.searchResultRecyclerView);
        customRecyclerViewAdapter=new CustomRecyclerViewAdapter(this, StoresNames,StoresAddresses,StoresImages,StoresImagesLinks);
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

        BookedStoreMainActivityTextView=findViewById(R.id.BookedStoreMainActivityTextView);
        BookedStoreMainActivityTextView.setVisibility(View.GONE);
        BookedHairCutMainActivityTextView=findViewById(R.id.BookedHairCutMainActivityTextView);
        BookedHairCutMainActivityTextView.setVisibility(View.GONE);
        GoToBookedStoreMainActivityButton=findViewById(R.id.GoToBookedStoreFromMainActivityButton);
        GoToBookedStoreMainActivityButton.setVisibility(View.GONE);
       GoToBookedStoreMainActivityButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent goToShopDetailsActivity = new Intent(mContext, ShopDetailsActivity.class);
               goToShopDetailsActivity.putExtra("ShopName", successfullyBookedStore);

               mContext.startActivity(goToShopDetailsActivity);
           }
       });
        GetListOfStoresMainDataOnly();

    }
    void showToast(){
        Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
    }
    void showToast(String toshow){
        Toast.makeText(this, toshow, Toast.LENGTH_LONG).show();
    }



    void GetListOfStoresMainDataOnly(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("Request", "ListOfStoresMainDataOnly");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, jsonObject,volleyListener,volleyErrorListener );
        requestQueue.add(jsonObjectRequest);
    }
void ServerResponseWithBookForMainActivity(JSONObject BookInfoForMainActivityJSONObject){
    try {
        successfullyBookedServicesHaircut=BookInfoForMainActivityJSONObject.getString("ServicesHairCutToReserved");
    } catch (JSONException e) {
        e.printStackTrace();
    }
    try {
        successfullyBookedStore=BookInfoForMainActivityJSONObject.getString("BookedStore");
    } catch (JSONException e) {
        e.printStackTrace();
    }
    if(successfullyBookedStore!=null && successfullyBookedServicesHaircut!=null){
        if(successfullyBookedStore.length()>0 && successfullyBookedServicesHaircut.length()>0){
            BookedStoreMainActivityTextView.setText(successfullyBookedStore);
            BookedStoreMainActivityTextView.setVisibility(View.VISIBLE);
            BookedHairCutMainActivityTextView.setText(successfullyBookedServicesHaircut);
            BookedHairCutMainActivityTextView.setVisibility(View.VISIBLE);
            GoToBookedStoreMainActivityButton.setVisibility(View.VISIBLE);
        }else{
            HideBookInfoVIEWS();
        }

    }else{

        HideBookInfoVIEWS();
    }

}  void HideBookInfoVIEWS(){
        BookedStoreMainActivityTextView.setVisibility(View.GONE);
        BookedHairCutMainActivityTextView.setVisibility(View.GONE);
        GoToBookedStoreMainActivityButton.setVisibility(View.GONE);
    }
    void  ServerResponseWithListOfStoresMainDataOnly(JSONObject ListOfStoresMainDataOnlyJSONObjectResponse){
        try {
            StoresNames.clear();
            for(int i=0;i<ListOfStoresMainDataOnlyJSONObjectResponse.getJSONArray("StoresNames").length();i++){
              StoresNames.add(ListOfStoresMainDataOnlyJSONObjectResponse.getJSONArray("StoresNames").getString(i));
            }

            StoresAddresses.clear();
            for(int i=0;i<ListOfStoresMainDataOnlyJSONObjectResponse.getJSONArray("StoresAddresses").length();i++){
                StoresAddresses.add(ListOfStoresMainDataOnlyJSONObjectResponse.getJSONArray("StoresAddresses").getString(i));
            }


            StoresImagesLinks.clear();
            for (int i=0;i<ListOfStoresMainDataOnlyJSONObjectResponse.getJSONArray("StoresImagesLinks").length();i++){
                ArrayList<String> ListOfLinksOfEachStore= new ArrayList<>();
                for(int j=0;j<ListOfStoresMainDataOnlyJSONObjectResponse.getJSONArray("StoresImagesLinks").getJSONArray(i).length();j++){
                    ListOfLinksOfEachStore.add(ListOfStoresMainDataOnlyJSONObjectResponse.getJSONArray("StoresImagesLinks").getJSONArray(i).getString(j));
                }
               StoresImagesLinks.add(ListOfLinksOfEachStore);
            }
            StoresImages.clear();
            for (int i=0;i<StoresImagesLinks.size();i++){
                RequestImage(StoresImagesLinks.get(i).get(0));
            }
            customRecyclerViewAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Toast.makeText(this, "Problem in ServerResponseWithSearchResult function", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


void RequestImage(String Link){

    ImageRequest imageRequest=new ImageRequest(Link, new Response.Listener<Bitmap>() {
        @Override
        public void onResponse(Bitmap response) {
        StoresImages.add(response);
        StoresImagesAsStrings.add(BitmapToString(response));
        customRecyclerViewAdapter.notifyDataSetChanged();
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
      for (int i = 0; i< StoresNames.size(); i++){

            if(!StoresNamesOriginal.contains(StoresNames.get(i))){
                StoresNamesOriginal.add(StoresNames.get(i));
            }
        }

        StoresNames.clear();
        String lowercaseCriteria=criteria.toLowerCase();
        for (int i = 0; i< StoresNamesOriginal.size(); i++){

            String nameOriginalLowerCase= StoresNamesOriginal.get(i).toLowerCase();
            if(nameOriginalLowerCase.contains(lowercaseCriteria)){
                StoresNames.add(StoresNamesOriginal.get(i));
            }
        }
        customRecyclerViewAdapter.notifyDataSetChanged();
*/

//////////////////////////////////////////////////////////////////////
        /// USE CODE BELOW FOR ONLINE SEARCH ON SERVER SIDE RESULT WILL BE DISPLAYED FROM FUNCTION ServerResponseWithListOfStoresMainDataOnly
        /// THIS MEANS THE RESULT SHOULD BE RETURNED IN JSONOBJECT WITH NAME ListOfStoresMainDataOnly
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
