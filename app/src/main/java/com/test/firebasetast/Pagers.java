package com.test.firebasetast;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;
import com.test.firebasetast.areaTemp.weatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class Pagers extends LinearLayout {//繼承別的Layout亦可


    Map<String,List<weatherData>> result = new HashMap();

    List<weatherData> list_mint = new ArrayList();
    List<weatherData> list_maxt = new ArrayList();
    List<weatherData> list_wx = new ArrayList();

    View view = null;

    //ui 主線程
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    //天氣 recyclerView 畫面
                    setCardViewDataAndRecyclerView();
                case 2 :
            }
        }
    };

    //頁面須在此顯示
    public Pagers(Context context, int pageNumber, Bundle bundle) {//pageNumber是由ＭainActivity.java那邊傳入頁碼
        super(context);


        //使用 View.post 方式代入ui線程 or 使用handler代入主畫面都可以
        LayoutInflater inflater = LayoutInflater.from(context);
        if(pageNumber == 1 ) {
            //一周天氣
            view = inflater.inflate(R.layout.weekly_weather_recyclerview, null);//連接頁面
            getJsonData getJsonData = new getJsonData();
            getJsonData.sendGET(view,bundle);
//            getJsonData.getItemSelected(view);

        }else if(pageNumber == 2 ){
            //天氣 recyclerView 畫面
            view = inflater.inflate(R.layout.weather_recyclerview, null);//連接頁面
            sendGET();
        }else {
            view = inflater.inflate(R.layout.my_pagers, null);//連接頁面
            TextView textView = view.findViewById(R.id.textView);//取得頁面元件
            textView.setText("第"+pageNumber+"頁");
        }

        addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }




    public void sendGET(){
        String TAG = "sendGET";
        //json資料
        String jsonData = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-1804447F-FDE6-44B0-9CE8-FCDA0022B460&format=JSON";

        /**建立連線*/
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        Request request1 = new Request.Builder()
                .url(jsonData)
                .build();

        /**設置回傳*/
        Call call = client.newCall(request1);
        //與 onRespone現程不同 >> 一個是主現程 一個是副現程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /**如果傳送過程有發生錯誤*/
                Log.d(TAG, "onFailure: sendGET 出錯 ");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String TAG = "onResponse";
                Log.d(TAG, "onResponse: respones sussess "  );
//                Log.d(TAG, "onResponse:  目前線程 " + Thread.currentThread().getId() );
                try {

                    JSONObject test = new JSONObject(response.body().string());
                    JSONObject recordsObj = test.getJSONObject("records");
                    JSONArray locationArray = recordsObj.getJSONArray("location");

                    for(int i =0 ;i <locationArray.length(); i++){
                        String locationName = locationArray.getJSONObject(i).getString("locationName");
                        JSONArray weatherElement = locationArray.getJSONObject(i).getJSONArray("weatherElement");
                        for(int j = 0 ;j <weatherElement.length();j++){
                            String elementName= weatherElement.getJSONObject(j).getString("elementName");
                            JSONArray timeArray = weatherElement.getJSONObject(j).getJSONArray("time");

                            String parameterName = timeArray.getJSONObject(0)
                                    .getJSONObject("parameter").getString("parameterName");

                            if(elementName.equals("Wx")){
//                                Log.e(TAG, "onResponse: wx locationName " + locationName +" parameterName " + parameterName );
                                list_wx.add(new weatherData(locationName,"Wx",parameterName));
                            }else if(elementName.equals("MinT")){
//                                Log.e(TAG, "onResponse: minT locationName " + locationName +" parameterName " + parameterName );
                                list_mint.add(new weatherData(locationName,"MinT",parameterName));
                            }else if(elementName.equals("MaxT")){
//                                Log.e(TAG, "onResponse: maxT locationName " + locationName +" parameterName " + parameterName );
                                list_maxt.add(new weatherData(locationName,"MaxT",parameterName));
                            }
                        }
                    }

                    result.put("MinT",list_mint);
                    result.put("MaxT",list_maxt);
                    result.put("Wx",list_wx);

                    Message message = new Message();
                    message.what = 1;
                    mhandler.sendMessage(message);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //recyclerView 設定
    public void setCardViewDataAndRecyclerView(){
        String TAG = "RecyclerView";

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
//        new GridView()
        recyclerAdapter recyclerAdapter = new recyclerAdapter(getContext());
        recyclerView.setAdapter(recyclerAdapter);
    }

    public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder>{
        private Context context;
        String TAG = "recyclerAdapter";
        //建構子
        recyclerAdapter(Context context){
            this.context = context;
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            TextView address,tempMax,tempMin;
            ImageView weather;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                address = itemView.findViewById(R.id.address_textView);
                tempMax = itemView.findViewById(R.id.tempMax_textView);
                tempMin = itemView.findViewById(R.id.tempMin_textView);
                weather = itemView.findViewById(R.id.weatherImage);


            }
        }

        //創建recyclerView
        @NonNull
        @Override
        public recyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.cardview,parent,false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        //設定 recyclerView 內容
        @Override
        public void onBindViewHolder(@NonNull recyclerAdapter.ViewHolder holder, int position) {
            List<weatherData> listMinT =  result.get("MinT");
            List<weatherData> listMaxT = result.get("MaxT");
            List<weatherData> listWx = result.get("Wx");


            holder.address.setText(listMinT.get(position).getLocationName());
            holder.tempMax.setText(listMaxT.get(position).getParameterName()+"\u00b0 ");
            holder.tempMin.setText(listMinT.get(position).getParameterName()+"\u00b0 ");

            String Wx= listWx.get(position).getParameterName();
            Log.e(TAG, "onBindViewHolder: " + Wx +" address " + listMinT.get(position).getLocationName() );
            Calendar calender = Calendar.getInstance();
            int hourNow = calender.get(Calendar.HOUR_OF_DAY);
            if(Wx.contains("雷")){
                holder.weather.setImageResource(R.mipmap.cloudlightning);
            }else if (Wx.contains("雨")){
                holder.weather.setImageResource(R.mipmap.heavyrain);
            }else if (Wx.contains("雲")){
                holder.weather.setImageResource(R.mipmap.cloud);
            }else if (hourNow <18){
                holder.weather.setImageResource(R.mipmap.sun);
            }else if (hourNow >17){
                holder.weather.setImageResource(R.mipmap.moon);
            }

        }

        @Override
        public int getItemCount() {
            try{
                return result.get("MinT").size();
            }catch (NullPointerException e){
                return 0;
            }
        }
    }
}
