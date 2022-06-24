package com.example.emergencysystem.VolleyUtils;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.example.emergencysystem.MainActivity;

import org.json.JSONObject;

import java.util.Map;

public class VolleyUtils {

 public static void SendData(Map data, Context mContext){
        RequestQueue requstQueue = Volley.newRequestQueue((Context) mContext);

        JSONObject dataJson = new JSONObject(data);
//        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, "http://10.1.12.209:8000/predict",dataJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, "Send", Toast.LENGTH_SHORT).show();
                        System.out.println("========> envoyer");
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                          //  progressBar.setVisibility(View.INVISIBLE);
                            System.out.println("=====> erreur");
                            System.out.println(error);
                        } catch (Exception e) {}

                    }
                }
        );
     jsonobj.setRetryPolicy(new DefaultRetryPolicy(
             0,
             DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
             DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requstQueue.add(jsonobj);
    }
}