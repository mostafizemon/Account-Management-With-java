package com.mostafiz.loginregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mostafiz.loginregister.databinding.ActivitySignupBinding;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);




        binding.signupactivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.signupactivitySignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname=binding.signupactivityFullname.getText().toString();
                String address=binding.signupactivityAddress.getText().toString();
                String email=binding.signupactivityEmail.getText().toString();
                String password=binding.signupactivityPassword.getText().toString();

                if (email.isEmpty() || !email.matches(emailPattern)){
                    binding.signupactivityEmail.setError("Please Enter Valid Email");
                    binding.signupactivityEmail.requestFocus();
                }
                if (password.isEmpty() || password.length()<6){
                    binding.signupactivityPassword.setError("Password must be 6 digit");
                    binding.signupactivityPassword.requestFocus();
                }

                else {
                    stringrequest(fullname,address,email,password);
                }
            }
        });





    }







//---------------------------------------------------------------------------------------------------------------------------------------------------------
    private void stringrequest(String fullname,String address,String email,String password){
        String url="http://192.168.0.118/AccountManagement/signup.php";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("success")){

                    SharedPreferences sharedPreferences=getSharedPreferences("information",MODE_PRIVATE);
                    SharedPreferences.Editor edit=sharedPreferences.edit();
                    edit.putString("email",email);
                    edit.apply();

                    Intent intent=new Intent(SignupActivity.this,UploadimageActivity.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                    finish();

                }
                if (response.equals("failed")){
                    Toast.makeText(SignupActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignupActivity.this,""+response,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map=new HashMap<String,String>();
                try {
                    map.put("email",EncryptMethod.encrypData(email));
                    map.put("password",EncryptMethod.encrypData(password));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                map.put("fullname",fullname);
                map.put("address",address);


                return map;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(SignupActivity.this);
        requestQueue.add(stringRequest);


    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------














}