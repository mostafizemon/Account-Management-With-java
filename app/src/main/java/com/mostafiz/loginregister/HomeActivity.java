package com.mostafiz.loginregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mostafiz.loginregister.databinding.ActivityHomeBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    String email;

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize SharedPreferences, Editor, and email inside onCreate()
        sharedPreferences = getSharedPreferences("information", MODE_PRIVATE);
        edit = sharedPreferences.edit();
        email = sharedPreferences.getString("email", "");


        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                edit.putString("email","");
                edit.apply();
                finish();
            }
        });


        if (email.length() <= 0) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        } else {
            objectRequest();
        }
    }

    private void objectRequest() {
        String url = "http://192.168.0.118/AccountManagement/getinformation.php"; // Make sure to provide a valid URL
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = response.getString("name");
                    String image = response.getString("image");

                    Picasso.get().load(image).into(binding.imageView);


                    binding.email.setText(email);
                    binding.name.setText(name);

                    // Load image into ImageView using an image loading library like Glide or Picasso

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(objectRequest);
    }
}
