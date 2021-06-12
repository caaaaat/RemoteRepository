package com.test.firebasetast;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;

public class getWeatherInfo {

    public void getweatherInfo(View view,Context context){

        recyclerAdapter recyclerAdapter = new recyclerAdapter(context);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.setAdapter(recyclerAdapter);

    }

    public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder>{
        private Context context;
        String TAG = "recyclerAdapter";

        recyclerAdapter(Context context){
            this.context = context;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView textInfo;
            ImageView weatherInfo;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textInfo = itemView.findViewById(R.id.textInfo);
                weatherInfo = itemView.findViewById(R.id.weatherInfo);
            }
        }

        //創建recyclerView
        @NonNull
        @Override
        public recyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.cardview_weatherinfo,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        //設定 recyclerView 內容
        @Override
        public void onBindViewHolder(@NonNull recyclerAdapter.ViewHolder holder, int position) {
            int test = R.mipmap.radarrecycler;
            getDiffImageWithDiffIntent(context,position,holder);
        }

        @Override
        public int getItemCount() {
            try{
                return 5;
            }catch (NullPointerException e){
                return 0;
            }
        }
    }

    public void getDiffImageWithDiffIntent(Context context ,int position,recyclerAdapter.ViewHolder holder){

        String url ="";
        Intent intent=new Intent();
        String textInfo = null;
        int imageResource = 0;

        switch(position){
            case 0:
                textInfo = "溫度分部圖";
                imageResource = R.mipmap.temprecycler;
                url="https://www.cwb.gov.tw/V8/C/W/OBS_Temp.html";
                intent.putExtra("url",url);
                break;
            case 1:
                textInfo = "累積雨量圖";
                imageResource = R.mipmap.rainfullrecycler;
                url="https://www.cwb.gov.tw/V8/C/P/Rainfall/Rainfall_QZJ.html";
                intent.putExtra("url",url);
                break;
            case 2:
                textInfo = "紫外線觀測圖";
                imageResource = R.mipmap.urrecycler;
                url="https://www.cwb.gov.tw/V8/C/W/OBS_UVI.html";
                intent.putExtra("url",url);
                break;
            case 3:
                textInfo = "衛星雲圖圖";
                imageResource = R.mipmap.sateliterecycler;
                url="https://www.cwb.gov.tw/V8/C/W/OBS_Sat.html";
                intent.putExtra("url",url);
                break;
            case 4:
                textInfo = "雷達回波圖";
                imageResource = R.mipmap.radarrecycler;
                url="https://www.cwb.gov.tw/V8/C/W/OBS_Radar.html";
                intent.putExtra("url",url);
                break;
            
        }
        holder.textInfo.setText(textInfo);
        holder.weatherInfo.setImageResource(imageResource);
        holder.weatherInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(context,webViewActivity.class);
                context.startActivity(intent);
            }
        });
    }


    /*        new DownloadImageTask((ImageView) view.findViewById(R.id.tempInfo))
               .execute("Img url");*/
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }


        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
