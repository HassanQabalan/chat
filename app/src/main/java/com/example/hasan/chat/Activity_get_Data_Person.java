package com.example.hasan.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Activity_get_Data_Person extends AppCompatActivity {
EditText name,number;
Button save;
ImageButton getImage;
Bitmap bitmap;
RequestQueue requestQueue;
String url=  "http://najarallkuwait.com/hasaan/saveImage.php";
private final int IMG_REQUST = 1;
    static final private String TAG = "get_Data_Person";
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data__p);
     requestQueue=Volley.newRequestQueue(getApplicationContext());
        name = findViewById(R.id.user_name);
        number = findViewById(R.id.user_number);
        save = findViewById(R.id.btn_save);
     getImage = findViewById(R.id.getImage);
     Random rnd = new Random();
     final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

     getImage.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             selectImage();


    }
     });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences =getSharedPreferences("user_info",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("name",name.getText().toString());
                editor.putString("number",number.getText().toString());
                editor.putInt("color",color);
                editor.apply();
                Intent intent = new Intent(Activity_get_Data_Person.this,Chat.class);
                startActivity(intent);
               // Log.i(TAG, "onClick: "+sharedPreferences.getString("name",null)+sharedPreferences.getString("number",null));
            }
        });

    }
    private String image_to_String (Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQUST && resultCode==RESULT_OK && data !=null) {

            Uri path = data.getData();
             try {
                bitmap = MediaStore.Images.Media.getBitmap(Activity_get_Data_Person.this.getContentResolver(), path);
            } catch (IOException e) {
                e.printStackTrace();

                //Toast.makeText(Activity_get_Data_Person.this, ""+s, Toast.LENGTH_SHORT).show();
                loadImageToServer();
            }
        }


}

    private void loadImageToServer() {
       final String s = image_to_String(bitmap);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: " + response);
            }
            ;
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressBar.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
             @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("image", s);
                params.put("number", number.getText().toString());
                Log.i(TAG, "getParams: " + params);
                return params;
            }
        };
        requestQueue.add(request);

    }

}