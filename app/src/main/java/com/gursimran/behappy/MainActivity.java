package com.gursimran.behappy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
Button next_button,share_button;
ProgressDialog progressDialog;
    String currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.image);
        next_button=findViewById(R.id.next_btn);
        share_button=findViewById(R.id.share_btn);
        loadMeme();
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMeme();
            }
        });
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMe();
            }
        });

    }

    private void loadMeme() {
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Your meme is loading");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
      String  url ="https://meme-api.herokuapp.com/gimme";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                          currentImage=response.getString("url");
                            Glide.with(MainActivity.this).load(currentImage).into(imageView);
                            progressDialog.dismiss();


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Check you network and Try again", Toast.LENGTH_SHORT).show();
                    }
                });


        queue.add(jsonObjectRequest);

    }
    private void shareMe(){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this new meme i got from Reditt "+" "+currentImage);
        startActivity(Intent.createChooser(intent,"Share this meme using...."));
    }
}
