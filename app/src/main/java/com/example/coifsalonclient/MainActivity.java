package com.example.coifsalonclient;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String URL="http://192.168.43.139:81/SecondPage.php";
    Response.Listener<JSONObject> volleyListener;
    Response.ErrorListener volleyErrorListener;
    RequestQueue requestQueue;

    ArrayList<String> StoresNames =new ArrayList();
    ArrayList<String> StoresNamesOriginal =new ArrayList();
    ArrayList<String> StoresAddresses =new ArrayList<>();
    ArrayList<String> StoresImagesAsStrings=new ArrayList<>();
    ArrayList<Bitmap> StoresImages=new ArrayList<>();

    EditText searchEditText;
    CustomRecyclerViewAdapter customRecyclerViewAdapter;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      // getActionBar().hide();
volleyErrorListener=new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Log.v("VolleyErrors", "onErrorResponse: IN MAIN ACTIVITY "+error.toString());
      showToast(error.toString());

    }
};
volleyListener=new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(JSONObject response) {
        Log.v("VolleyReceived","IN MAIN ACTIVITY"+ response.toString());
        showToast(response.toString());
        if(response.has("ListOfStoresMainDataOnly")){
            try {
                ServerResponseWithListOfStoresMainDataOnly(response.getJSONObject("ListOfStoresMainDataOnly"));
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
        customRecyclerViewAdapter=new CustomRecyclerViewAdapter(this, StoresNames,StoresAddresses,StoresImages);
        recyclerView.setAdapter(customRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue= Volley.newRequestQueue(this);
        GetListOfStoresMainDataOnly();
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

            StoresImagesAsStrings.clear();
            StoresImages.clear();
            for(int i=0;i<ListOfStoresMainDataOnlyJSONObjectResponse.getJSONArray("StoresImages").length();i++){
                StoresImagesAsStrings.add(ListOfStoresMainDataOnlyJSONObjectResponse.getJSONArray("StoresImages").getString(i));
                StoresImages.add(ConvertStringToBitmap(ListOfStoresMainDataOnlyJSONObjectResponse.getJSONArray("StoresImages").getString(i)));
            }

            customRecyclerViewAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Toast.makeText(this, "Problem in ServerResponseWithSearchResult function", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }




    void Search(String criteria){

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
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("Search", criteria);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, jsonObject,volleyListener,volleyErrorListener );
        requestQueue.add(jsonObjectRequest);
       // showToast(criteria);
    }

    public  Bitmap ConvertStringToBitmap(String image){
        byte[] bytes;
        bytes= Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
