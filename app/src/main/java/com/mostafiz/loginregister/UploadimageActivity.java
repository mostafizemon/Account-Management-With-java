package com.mostafiz.loginregister;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.mostafiz.loginregister.databinding.ActivityUploadimageBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class UploadimageActivity extends AppCompatActivity {

    ActivityUploadimageBinding binding;
    RequestQueue requestQueue;
    boolean selectediamge=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadimageBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        requestQueue = Volley.newRequestQueue(this);

        binding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadimageActivity.this,HomeActivity.class));
                finish();
            }
        });



        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()== Activity.RESULT_OK){
                    Intent intent=result.getData();
                    Uri uri=intent.getData();
                    try {
                        Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        binding.imageview.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        });



        binding.chosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(UploadimageActivity.this)
                        .maxResultSize(1000,1000)
                        .compress(1024)
                        .cropSquare()
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                activityResultLauncher.launch(intent);
                                return null;
                            }
                        });
            }
        });





    binding.uploadphoto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.imageview.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

            byte[] imagebytes = byteArrayOutputStream.toByteArray();
            String image64 = Base64.encodeToString(imagebytes, Base64.DEFAULT);


            stringRequest(image64,email);

        }
    });






        }


    private void stringRequest(String image,String email) {
        String url = "http://192.168.0.118/AccountManagement/uploadimage.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(UploadimageActivity.this, response, Toast.LENGTH_SHORT).show();
                if(response.equals("success")){
                    Toast.makeText(UploadimageActivity.this, "Upload Image Succesfull", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadimageActivity.this,HomeActivity.class));
                    finish();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UploadimageActivity.this, "Failed to upload image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("image", image);
                try {
                    map.put("email",EncryptMethod.encrypData(email));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return map;
            }
        };

        // Add the request to the request queue
        requestQueue.add(stringRequest);
    }
}