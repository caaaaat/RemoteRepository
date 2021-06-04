package com.test.firebasetast;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.annotations.NotNull;
import com.test.firebasetast.areaTemp.weatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class getJsonData {

    private String loaction;
    private Handler mhandler;

//    public void sendGET(String location, Handler mhandler){
//        this.loaction = location;
//        this.mhandler = mhandler;
//    }

    public String locationURLDecode(String location){
        String result = "";
        try{
            String TAG = "locationURLDecode";
            result = URLDecoder.decode(location,"utf-8");
            Log.e(TAG, "locationURLDecode: string  " + location);
            Log.e(TAG, "locationURLDecode: after decode " + result);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return result;
    }


    public void  sendGET(String location, Handler mhandler,View view){
        this.loaction = location;
//        this.mhandler = mhandler;

        String TAG = "sendGET";
        //json資料
        String jsonData = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-003?Authorization=CWB-1804447F-FDE6-44B0-9CE8-FCDA0022B460&format=JSON&locationName="
                + locationURLDecode(loaction);

        /**建立連線*/
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

//                    Message message = new Message();
//                    message.what = 2;
//                    message.obj = result;
//                    mhandler.sendMessage(message);

                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView maxTemp,minTemp,weatherSituation,weekDay,rainfallChance;
                            ImageView weatherImage;

                            maxTemp = view.findViewById(R.id.maxTemp);
                            minTemp = view.findViewById(R.id.minTemp);
                            weekDay = view.findViewById(R.id.weekDay);

                            weatherSituation = view.findViewById(R.id.weatherSituation);
                            rainfallChance = view.findViewById(R.id.rainfallChance);

                            maxTemp.setText(result.getMaxTempTextView());
                            minTemp.setText(result.getMinTempTextView());
                            weekDay.setText(result.getWeekDay());
                            weatherSituation.setText((String)result.getWeatherDescription().get("description"));
                            rainfallChance.setText((String)result.getWeatherDescription().get("rainfallChance"));

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
