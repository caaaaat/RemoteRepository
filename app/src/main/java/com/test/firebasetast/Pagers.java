package com.test.firebasetast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.test.firebasetast.areaTemp.cardView;
import com.test.firebasetast.areaTemp.memberData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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


    Map<String,List<cardView>> result = new HashMap();

    List<cardView> list_mint = new ArrayList();
    List<cardView> list_maxt = new ArrayList();
    List<cardView> list_wx = new ArrayList();

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
            }
        }
    };

    public Pagers(Context context, int pageNumber) {//pageNumber是由ＭainActivity.java那邊傳入頁碼
        super(context);

        //頁面須在此顯示
        //使用 View.post 方式代入ui線程 or 使用handler代入主畫面都可以

//        View view = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(pageNumber == 1 ){
            //天氣 recyclerView 畫面

            view = inflater.inflate(R.layout.weather_recyclerview, null);//連接頁面
            sendGET();

        }else if(pageNumber ==2 ){
//
//            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
//            recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
//            recyclerAdapter recyclerAdapter = new recyclerAdapter(context);
//            recyclerView.setAdapter(recyclerAdapter);

            view = inflater.inflate(R.layout.my_pagers, null);//連接頁面
            TextView textView = view.findViewById(R.id.textView);//取得頁面元件
            textView.setText("第"+pageNumber+"頁");
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
//                .connectTimeout()
                .build();

        /**設置傳送需求*/
//        Request request = new Request.Builder()
//                .url("https://jsonplaceholder.typicode.com/posts/1")
//                .header("Cookie","")//有Cookie需求的話則可用此發送
//                .addHeader("","")//如果API有需要header的則可使用此發送
//                .build();

        Request request1 = new Request.Builder()
                .url(jsonData)
                .build();

        /**設置回傳*/
        Call call = client.newCall(request1);
        //與 onRespone現程不同 >> 一個是主現程 一個是副現程
//        Log.d(TAG, "sendGET:  目前線程 " + Thread.currentThread().getId() );
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                /**如果傳送過程有發生錯誤*/
                Log.d(TAG, "onFailure: sendGET 出錯 ");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                /**取得回傳*/
//                tvRes.setText("GET回傳：\n" + response.body().string());
                String TAG = "onResponse";
                Log.d(TAG, "onResponse: " + "test start"  );
//                Log.d(TAG, "onResponse:  目前線程 " + Thread.currentThread().getId() );
                try {

                    JSONObject test = new JSONObject(response.body().string());
                    JSONObject recordsObj = test.getJSONObject("records");
                    JSONArray locationArray = recordsObj.getJSONArray("location");
                    String parameterValue = "";

                    for(int i =0 ;i <locationArray.length(); i++){
                        String locationName = locationArray.getJSONObject(i).getString("locationName");
                        JSONArray weatherElement = locationArray.getJSONObject(i).getJSONArray("weatherElement");
                        for(int j = 0 ;j <weatherElement.length();j++){
                            String elementName= weatherElement.getJSONObject(j).getString("elementName");
                            JSONArray timeArray = weatherElement.getJSONObject(j).getJSONArray("time");

//                           for(int k = 0;k<timeArray.length();k++){
                            String parameterName = timeArray.getJSONObject(1)
                                    .getJSONObject("parameter").getString("parameterName");
                            switch(elementName){
                                case "Wx":
                                    parameterValue = timeArray.getJSONObject(1)
                                            .getJSONObject("parameter").getString("parameterValue");
                                case "MinT":
                                    parameterValue ="";
                                case "MaxT":
                                    parameterValue = "";
                            }
//                           }

                            switch(elementName){
                                case "MinT":
                                    list_mint.add(new cardView(locationName,"MinT",parameterName));
//                                    Log.d(TAG, "onResponse: MinT ");
                                case "MaxT":
                                    list_maxt.add(new cardView(locationName,"MaxT",parameterName));
//                                    Log.d(TAG, "onResponse: MaxT ");
                                case "Wx":
                                    list_wx.add(new cardView(locationName,"Wx",parameterName,parameterValue));
//                                    Log.d(TAG, "onResponse: Wx ");
                            }


                        }
                    }


                    result.put("MinT",list_mint);
                    result.put("MaxT",list_maxt);
                    result.put("Wx",list_wx);

                    for(cardView test11 :list_mint){
                        Log.d(TAG, "onResponse: weatherData " + test11.getElementName());
                        Log.d(TAG, "onResponse: weatherData " + test11.getLocationName());
                        Log.d(TAG, "onResponse: weatherData " + test11.getParameterName());
                    }


//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            setCardViewDataAndRecyclerView();
//                        }
//                    });

                    Message message = new Message();
                    message.what = 1;
                    mhandler.sendMessage(message);

//                    view.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            setCardViewDataAndRecyclerView();
//                        }
//                    });



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    //抓使用者資料回傳
//    public memberData getUserData(){
//        String TAG = "getUserData";
//        memberData memberData = new memberData();
//        Intent intent = this.getIntent();
//        Bundle bundle = intent.getExtras();
//        String accountValue = bundle.getString("account");
//        memberData.setAccount(accountValue);
//
//        //找地區出來
//        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
//        DatabaseReference db = fbd.getReference();
//        db.child("users").child(accountValue).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                memberData userData= snapshot.getValue(memberData.class);
//                Log.d(TAG, "onDataChange: userData.getAddress() " + userData.getAddress());
//                memberData.setAddress(userData.getAddress());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "userData is null");
//            }
//        });
//
//
//        return memberData;
//    }


    //recyclerView 設定
    public void setCardViewDataAndRecyclerView(){
        String TAG = "RecyclerView";

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerAdapter recyclerAdapter = new recyclerAdapter(getContext());
        recyclerView.setAdapter(recyclerAdapter);

//        int userAddressLocate = addressList.indexOf(new cardView(memberData.getAddress()));
//        Log.d(TAG, "test1:  memberData.getAddress() " + memberData.getAddress());
//        Log.d(TAG, "test2: userAddressLocate " + userAddressLocate);
//        recyclerView.scrollToPosition(userAddressLocate);
    }

    public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder>{
        private Context context;

        //建構子
        recyclerAdapter(Context context){
            this.context = context;
        }


        class ViewHolder extends RecyclerView.ViewHolder{
            TextView address,tempMax,tempMin;
            Button button1,button2;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                address = itemView.findViewById(R.id.address_textView);
                tempMax = itemView.findViewById(R.id.tempMax_textView);
                tempMin = itemView.findViewById(R.id.tempMin_textView);

                button1 = itemView.findViewById(R.id.cardView_button1);
                button2 = itemView.findViewById(R.id.cardView_button2);

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
            List<cardView> listMinT =  result.get("MinT");
            List<cardView> listMaxT = result.get("MaxT");
            List<cardView> listWx = result.get("Wx");


            holder.address.setText(listMinT.get(position).getLocationName());
            holder.tempMax.setText(listMaxT.get(position).getParameterName()+"C");
            holder.tempMin.setText(listMinT.get(position).getParameterName()+"C");

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
