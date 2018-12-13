package com.flashdin.fastandroidnetworking.fast;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.flashdin.fastandroidnetworking.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by flashdin on 10/11/17.
 */

public class FastMethods {

    private ProgressDialog progressDialog;
    private static final String url = new Config().URL;

    public FastMethods(Context ctx) {
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Inserting");
        progressDialog.setMessage("Please wait ....");
        progressDialog.show();
    }

    public List<TUsers> viewData(Context ctx) {
        final List<TUsers> tUsersList = new ArrayList<>();
        AndroidNetworking.get(url + "users/{user_name}")
                .addPathParameter("user_name", "jkl")
                .setTag(this)
                .setPriority(Priority.LOW)
                .setUserAgent("result")
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        TUsers tUsers = new TUsers();
                        try {
                            tUsers.setUserId(Integer.valueOf(response.get("user_id").toString()));
                            tUsers.setUserName(response.get("user_name").toString());
//                            SimpleDateFormat date = new SimpleDateFormat("yyyy-mm-dd");
//                            Date date1 = date.parse(response.get("user_date").toString());
                            tUsers.setUserDate(new Date());
                            tUsers.setUserPhoto(response.get("user_photo").toString());
                            tUsers.setUserPhotopath(response.get("user_photopath").toString());
                            tUsersList.add(tUsers);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        anError.getMessage();
                    }
                });
        return tUsersList;
    }

    public void saveData(TUsers tUsers, final Context ctx) {
        JSONObject params = new JSONObject();
        if (tUsers.getUserId() <= 0) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("user_name", tUsers.getUserName());
                params.put("user_date", simpleDateFormat.format(new Date()));
                params.put("user_photo", "");
                params.put("user_photopath", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking.post(url + "users")
                    .addJSONObjectBody(params)
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                        @Override
                        public void onResponse(Response okHttpResponse, JSONObject response) {
                            progressDialog.dismiss();
                            Log.d(TAG, "onResponse object : " + response.toString());
                            Log.d(TAG, "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
                            if (okHttpResponse.isSuccessful()) {
                                Log.d(TAG, "onResponse success headers : " + okHttpResponse.headers().toString());
                            } else {
                                Log.d(TAG, "onResponse not success headers : " + okHttpResponse.headers().toString());
                            }
                            try {
                                Toast.makeText(ctx, response.get("result").toString(), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            progressDialog.dismiss();
                            Toast.makeText(ctx, anError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("user_id", String.valueOf(tUsers.getUserId()));
                params.put("user_name", tUsers.getUserName());
                params.put("user_date", simpleDateFormat.format(new Date()));
                params.put("user_photo", "");
                params.put("user_photopath", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking.put(url + "users")
                    .addJSONObjectBody(params)
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .setAnalyticsListener(new AnalyticsListener() {
                        @Override
                        public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                            Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                            Log.d(TAG, " bytesSent : " + bytesSent);
                            Log.d(TAG, " bytesReceived : " + bytesReceived);
                            Log.d(TAG, " isFromCache : " + isFromCache);
                        }
                    })
                    .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                        @Override
                        public void onResponse(Response okHttpResponse, JSONObject response) {
                            progressDialog.dismiss();
                            Log.d(TAG, "onResponse object : " + response.toString());
                            Log.d(TAG, "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
                            if (okHttpResponse.isSuccessful()) {
                                Log.d(TAG, "onResponse success headers : " + okHttpResponse.headers().toString());
                            } else {
                                Log.d(TAG, "onResponse not success headers : " + okHttpResponse.headers().toString());
                            }
                            try {
                                Toast.makeText(ctx, response.get("result").toString(), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            progressDialog.dismiss();
                            Toast.makeText(ctx, anError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    public void deleteData(TUsers tUsers, final Context ctx) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", String.valueOf(tUsers.getUserId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.delete(url + "users")
                .addJSONObjectBody(params)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, JSONObject response) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onResponse object : " + response.toString());
                        Log.d(TAG, "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
                        if (okHttpResponse.isSuccessful()) {
                            Log.d(TAG, "onResponse success headers : " + okHttpResponse.headers().toString());
                        } else {
                            Log.d(TAG, "onResponse not success headers : " + okHttpResponse.headers().toString());
                        }
                        try {
                            Toast.makeText(ctx, response.get("result").toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(ctx, anError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
