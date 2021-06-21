package com.test.firebasetast;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.test.firebasetast.areaTemp.common;
import com.test.firebasetast.areaTemp.weatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class getJsonData {

//    private String loaction;
//    private String address;
//    private  String jsonData;


    public String locationURLDecode(String location){
        String result = "";
        try{
            String TAG = "locationURLDecode";
            result = URLEncoder.encode(location,"utf-8");
            Log.e(TAG, "locationURLDecode: string  " + location);
            Log.e(TAG, "locationURLDecode: after decode " + result);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return result;
    }

    public String getJsonDataURL(String address,String subaddress){
        common common = new common();
        String TAG = "getJsonDataURL";

        String jsonData = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-" +
                common.areaNumber().get(address) +
                "?Authorization=CWB-1804447F-FDE6-44B0-9CE8-FCDA0022B460&format=JSON&locationName="
                + locationURLDecode(subaddress);

        Log.e(TAG, "getJsonDataURL: address "+ address + "subaddress "  + subaddress );
        Log.e(TAG, "getJsonDataURL: jsonData "  + jsonData );

        return jsonData;
    }


    public void  sendGET(View view, Bundle bundle){
        String account = bundle.getString("account");
        String address = bundle.getString("address");
        String subaddress = bundle.getString("subaddress");
        String TAG = "sendGET";
        //json資料
        String jsonData = getJsonDataURL(address,subaddress);


        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference db = fbd.getReference();
        db.child("location").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Spinner addressSpinner = view.findViewById(R.id.address);
                Spinner subAddressSpinner = view.findViewById(R.id.subAddress);
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: is successful");
                    HashMap<String, ArrayList> sublocation = (HashMap) task.getResult().getValue();

                    //主下拉選單
                    ArrayList<String> location = new ArrayList<>();
                    for(String sublocationKey : sublocation.keySet()){
                        location.add(sublocationKey);
                    }

                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter arrayAdapter = new ArrayAdapter(view.getContext(),
                                    android.R.layout.simple_dropdown_item_1line,location);

                            int addressPosition = arrayAdapter.getPosition(address);
                            addressSpinner.setAdapter(arrayAdapter);
                            addressSpinner.setSelection(addressPosition);

                            ArrayAdapter arrayAdapter2 = new ArrayAdapter(view.getContext(),
                                    android.R.layout.simple_dropdown_item_1line,sublocation.get(location.get(0)).toArray());
                            subAddressSpinner.setAdapter(arrayAdapter2);


                            addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    int position = addressSpinner.getSelectedItemPosition();
                                    ArrayAdapter arrayAdapter2 = new ArrayAdapter(view.getContext(),
                                            android.R.layout.simple_dropdown_item_1line,sublocation.get(location.get(position)).toArray());
                                    subAddressSpinner.setAdapter(arrayAdapter2);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    Log.d(TAG, "onNothingSelected: error ");
                                }
                            });



                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                                    .build();

                            Request request = new Request.Builder()
                                    .url(jsonData)
                                    .build();

                            /**設置回傳*/
                            Call call = client.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                    /**如果傳送過程有發生錯誤*/
                                    Log.d(TAG, "onFailure: sendGET 出錯 ");
                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                    /**取得回傳*/
                                    String TAG = "onResponse";
                                    Log.d(TAG, "onResponse: respones sussess " );
                                    try {

                                        JSONObject test = new JSONObject(response.body().string());

                                        JSONObject location = test.getJSONObject("records").getJSONArray("locations").getJSONObject(0).getJSONArray("location").getJSONObject(0);
                                        String minTemp = location.getJSONArray("weatherElement").getJSONObject(8).getString("elementName");
                                        JSONArray minTimeArray = location.getJSONArray("weatherElement").getJSONObject(8).getJSONArray("time");
                                        String maxTemp = location.getJSONArray("weatherElement").getJSONObject(12).getString("elementName");
                                        JSONArray maxTimeArray = location.getJSONArray("weatherElement").getJSONObject(12).getJSONArray("time");
                                        String WeatherDescription = location.getJSONArray("weatherElement").getJSONObject(10).getString("elementName");
                                        JSONArray descriptionTimeArray = location.getJSONArray("weatherElement").getJSONObject(10).getJSONArray("time");

                                        HashMap<String,JSONArray> hashMap = new HashMap<>();
                                        hashMap.put(minTemp,minTimeArray);
                                        hashMap.put(maxTemp,maxTimeArray);
                                        hashMap.put(WeatherDescription,descriptionTimeArray);
                                        weatherData result = new weatherData(hashMap);

                                        view.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                TextView maxTemp,minTemp,weatherSituation,weekDay,rainfallChance;
                                                ImageView weatherImage;

                                                maxTemp = view.findViewById(R.id.maxTemp);
                                                minTemp = view.findViewById(R.id.minTemp);
                                                weekDay = view.findViewById(R.id.weekDay);
                                                weatherImage = view.findViewById(R.id.weatherImage);

                                                weatherSituation = view.findViewById(R.id.weatherSituation);
                                                rainfallChance = view.findViewById(R.id.rainfallChance);

                                                Button selectedAddress = view.findViewById(R.id.selectedAddress);

                                                maxTemp.setText(result.getMaxTempTextView());
                                                minTemp.setText(result.getMinTempTextView());
                                                weekDay.setText(result.getWeekDay());
                                                weatherSituation.setText((String)result.getWeatherDescription().get("description"));
                                                rainfallChance.setText((String)result.getWeatherDescription().get("rainfallChance"));

                                                Calendar calender = Calendar.getInstance();
                                                int hourNow = calender.get(Calendar.HOUR_OF_DAY);
                                                String weather = weatherSituation.getText().toString();


                                                if(weather.contains("雷")){
                                                    weatherImage.setImageResource(R.mipmap.cloudlightning);
                                                }else if (weather.contains("雨")){
                                                    weatherImage.setImageResource(R.mipmap.heavyrain);
                                                }else if (weather.contains("雲")){
                                                    weatherImage.setImageResource(R.mipmap.cloud);
                                                }else if (hourNow <18){
                                                    weatherImage.setImageResource(R.mipmap.sun);
                                                }else if (hourNow >17){
                                                    weatherImage.setImageResource(R.mipmap.moon);
                                                }



                                                selectedAddress.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View viewtest) {
                                                        getItemSelected(view);
                                                    }
                                                });



                                            }
                                        });


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        }
                    });
                }else{
                    Log.d(TAG,account + " 抓取地區下拉選單錯誤 ");
                    Toast.makeText(view.getContext(),
                            account + " address spinner error  ",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void getItemSelected(View view ){
        Spinner addressSpinner =view.findViewById(R.id.address);
        Spinner subaddressSpinner = view.findViewById(R.id.subAddress);
        String address = (String)addressSpinner.getSelectedItem();
        String subaddress = (String)subaddressSpinner.getSelectedItem();
        String TAG = "getItemSelected";
        Log.e(TAG, "getItemSelected: address "+ address +" subadd " +subaddress  );

        String jsonData =  getJsonDataURL(address,subaddress);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        Request request = new Request.Builder()
                .url(jsonData)
                .build();

        /**設置回傳*/
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /**如果傳送過程有發生錯誤*/
                Log.d(TAG, "onFailure: sendGET 出錯 ");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                /**取得回傳*/
                String TAG = "onResponse";
                Log.d(TAG, "onResponse: respones sussess " );
                try {

                    JSONObject test = new JSONObject(response.body().string());

                    JSONObject location = test.getJSONObject("records").getJSONArray("locations").getJSONObject(0).getJSONArray("location").getJSONObject(0);
                    //elementName + timeArray(MinT MaxT WeatherDescription)
                    Log.e(TAG, "onResponse: location " + location );
                    String minTemp = location.getJSONArray("weatherElement").getJSONObject(8).getString("elementName");
                    JSONArray minTimeArray = location.getJSONArray("weatherElement").getJSONObject(8).getJSONArray("time");
                    String maxTemp = location.getJSONArray("weatherElement").getJSONObject(12).getString("elementName");
                    JSONArray maxTimeArray = location.getJSONArray("weatherElement").getJSONObject(12).getJSONArray("time");
                    String WeatherDescription = location.getJSONArray("weatherElement").getJSONObject(10).getString("elementName");
                    JSONArray descriptionTimeArray = location.getJSONArray("weatherElement").getJSONObject(10).getJSONArray("time");

                    HashMap<String,JSONArray> hashMap = new HashMap<>();
                    hashMap.put(minTemp,minTimeArray);
                    hashMap.put(maxTemp,maxTimeArray);
                    hashMap.put(WeatherDescription,descriptionTimeArray);
                    weatherData result = new weatherData(hashMap);

                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView maxTemp,minTemp,weatherSituation,weekDay,rainfallChance;
                            ImageView weatherImage;

                            maxTemp = view.findViewById(R.id.maxTemp);
                            minTemp = view.findViewById(R.id.minTemp);
                            weekDay = view.findViewById(R.id.weekDay);
                            weatherImage = view.findViewById(R.id.weatherImage);

                            weatherSituation = view.findViewById(R.id.weatherSituation);
                            rainfallChance = view.findViewById(R.id.rainfallChance);

                            maxTemp.setText(result.getMaxTempTextView());
                            minTemp.setText(result.getMinTempTextView());
                            weekDay.setText(result.getWeekDay());
                            weatherSituation.setText((String)result.getWeatherDescription().get("description"));
                            rainfallChance.setText((String)result.getWeatherDescription().get("rainfallChance"));

                            Calendar calender = Calendar.getInstance();
                            int hourNow = calender.get(Calendar.HOUR_OF_DAY);
                            String weather = weatherSituation.getText().toString();


                            if(weather.contains("雷")){
                                weatherImage.setImageResource(R.mipmap.cloudlightning);
                            }else if (weather.contains("雨")){
                                weatherImage.setImageResource(R.mipmap.heavyrain);
                            }else if (weather.contains("雲")){
                                weatherImage.setImageResource(R.mipmap.cloud);
                            }else if (hourNow <18){
                                weatherImage.setImageResource(R.mipmap.sun);
                            }else if (hourNow >17){
                                weatherImage.setImageResource(R.mipmap.moon);
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });





    }
}
