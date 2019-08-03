package com.example.coifsalonclient;

import android.content.Context;
import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
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

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String URL="";
    Response.Listener<JSONObject> volleyListener;
    Response.ErrorListener volleyErrorListener;
    RequestQueue requestQueue;

    ArrayList<String> names=new ArrayList();
    ArrayList<String> namesOriginal=new ArrayList();

    EditText searchEditText;
    CustomRecyclerViewAdapter customRecyclerViewAdapter;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
volleyErrorListener=new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {

    }
};
volleyListener=new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(JSONObject response) {

    }
};

        names.add("Marouan");
        names.add("Anouar");
        names.add("Said");
        names.add("Saleh");
        names.add("Marzek");

        recyclerView=findViewById(R.id.searchResultRecyclerView);
        customRecyclerViewAdapter=new CustomRecyclerViewAdapter(this,names);
        recyclerView.setAdapter(customRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue= Volley.newRequestQueue(this);
        Search();
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



    void Search(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("Search", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, jsonObject,volleyListener,volleyErrorListener );
        requestQueue.add(jsonObjectRequest);
    }






    void Search(String criteria){

       for (int i=0;i<names.size();i++){

            if(!namesOriginal.contains(names.get(i))){
                namesOriginal.add(names.get(i));
            }
        }

        names.clear();
        String lowercaseCriteria=criteria.toLowerCase();
        for (int i=0;i<namesOriginal.size();i++){

            String nameOriginalLowerCase=namesOriginal.get(i).toLowerCase();
            if(nameOriginalLowerCase.contains(lowercaseCriteria)){
                names.add(namesOriginal.get(i));
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
        showToast(criteria);
    }

}
