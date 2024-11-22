package com.tayebmahmud.mobilegateway;

import android.app.ProgressDialog;
import android.content.Context;


import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InitializeGateway {
    private Context context;
    private String BASE_URL;
    private String API_KEY;
    private String VERIFY_URL;
    private ProgressDialog dialog;

    public InitializeGateway(Context context, String BASE_URL, String API_KEY) {
        this.context = context;
        this.BASE_URL = BASE_URL;
        this.API_KEY = API_KEY;

        //init dialog
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    public InitializeGateway(Context context, String BASE_URL, String API_KEY, String VERIFY_URL) {
        this.context = context;
        this.BASE_URL = BASE_URL;
        this.API_KEY = API_KEY;
        this.VERIFY_URL = VERIFY_URL;

        //init dialog
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    public interface SMSCallBack {
        void onResult(boolean success);
    }

    public interface VerifySMS {
        void onResult(boolean success);
    }

    public void sendSMS(HashMap<String, String> map, SMSCallBack callBack) {
        try {
            dialog.show();
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(context);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                    response -> {
                        // Display the first 500 characters of the response string.
                        if (response != null) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                String error = jsonResponse.getString("error");
                                String message = jsonResponse.getString("message");
                                callBack.onResult(error.equals("false") && message.equals("Data added successfully."));
                                dialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                callBack.onResult(false);
                                dialog.dismiss();
                            }
                        } else {
                            callBack.onResult(false);
                            dialog.dismiss();
                        }


                    }, error -> {
                callBack.onResult(false);
                dialog.dismiss();
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> stringStringHashMap = new HashMap<>();
                    stringStringHashMap.put("api-key", API_KEY);
                    return stringStringHashMap;
                }

                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    return map;
                }
            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onResult(false);
        }
    }

    public void verifySMS(HashMap<String, String> map, VerifySMS callBack) {
        try {
            try {
                dialog.show();
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(context);

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, VERIFY_URL,
                        response -> {
                            // Display the first 500 characters of the response string.
                            if (response != null) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String error = jsonResponse.getString("error");
                                    String message = jsonResponse.getString("message");
                                    callBack.onResult(error.equals("false") && message.equals("Data added successfully."));
                                    dialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    callBack.onResult(false);
                                    dialog.dismiss();
                                }
                            } else {
                                callBack.onResult(false);
                                dialog.dismiss();
                            }


                        }, error -> {
                    callBack.onResult(false);
                    dialog.dismiss();
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                        stringStringHashMap.put("api-key", API_KEY);
                        return stringStringHashMap;
                    }

                    @Nullable
                    @Override
                    protected Map<String, String> getParams() {
                        return map;
                    }
                };

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            } catch (Exception e) {
                e.printStackTrace();
                callBack.onResult(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
