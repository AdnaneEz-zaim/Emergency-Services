package com.example.emergencysystem;
public class VolleyUtils {

 public static void SendData(Map data, Context mContext, ProgressBar progressBar){
        RequestQueue requstQueue = Volley.newRequestQueue(mContext);
        JSONObject dataJson = new JSONObject(data);
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, "localhost:8000/predict",dataJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        
                       
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            progressBar.setVisibility(View.INVISIBLE);
                            
                            System.out.println(error);
                        } catch (Exception e) {}

                    }
                }
        );
        requstQueue.add(jsonobj);
    }
}