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
import androidx.viewbinding.ViewBinding;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mostafiz.loginregister.databinding.ActivityLoginBinding;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        binding.loginactivitySignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));

            }
        });



        binding.loginactivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=binding.loginactivityEmail.getText().toString();
                String password=binding.loginactivityPassword.getText().toString();

                if (email.isEmpty() || !email.matches(emailPattern)){
                    binding.loginactivityEmail.setError("Please Enter Valid Email");
                    binding.loginactivityEmail.requestFocus();
                }

                if (password.length()<6){
                    binding.loginactivityPassword.setError("Password Must be 6 Digits");
                    binding.loginactivityPassword.requestFocus();
                }

                else {
                    stringrequest(email,password);

                }




            }
        });





    }


    private void stringrequest(String email,String password){
        String url="http://192.168.0.118/AccountManagement/login.php";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("success")){

                    SharedPreferences sharedPreferences=getSharedPreferences("information",MODE_PRIVATE);
                    SharedPreferences.Editor edit=sharedPreferences.edit();
                    edit.putString("email",email);
                    edit.apply();

                    Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
                if (response.equals("failed")){

                }
                else {

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

                return map;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);


    }


}