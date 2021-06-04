package com.test.firebasetast.areaTemp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class weatherData {


    private String locationName;
    private String elementName;
    private String parameterName;
    private String parameterValue;



    private HashMap<String, JSONArray> elementArray;
    private String minTempTextView;
    private String maxTempTextView;
    private String weekDay;
    private HashMap<String,String > weatherDescription;


    public weatherData() {
    }

    //抓出一周天氣的text字串
    public weatherData(HashMap<String, JSONArray> elementArray) {
        try{
            String TAG = "weatherData ";
            this.elementArray = elementArray;
            minTempTextView = "";
            maxTempTextView = "";
            weekDay ="";

            //最低氣溫
            JSONArray mintArray = elementArray.get("MinT");
            for(int i =0 ;i <mintArray.length(); i++){
                //
                if(i%2 == 0){
                    minTempTextView = minTempTextView + mintArray.getJSONObject(i).getJSONArray("elementValue")
                            .getJSONObject(0).getString("value") + "\u00b0 " ;
//                    Log.e(TAG, "weatherData:  add i =  " + i );
//                Log.e(TAG, "weatherData: minPutData " + mintArray.getJSONObject(i).getJSONArray("elementValue")
//                        .getJSONObject(0).getString("value") + "\u00b0"   );
                }
            }

            //最高氣溫
            JSONArray maxtArray = elementArray.get("MaxT");
            for(int i =0 ;i <maxtArray.length(); i++){
                if(i%2 == 0 ){
                    maxTempTextView = maxTempTextView + maxtArray.getJSONObject(i).getJSONArray("elementValue")
                            .getJSONObject(0).getString("value") + "\u00b0 " ;
//                Log.e(TAG, "weatherData: maxTempTextView " + maxTempTextView   );
                }
            }

            //目前禮拜幾+後面六天
            weekDay = weekDayString();

            //天氣預報綜合描述
            JSONArray descriptionArray = elementArray.get("WeatherDescription");
            String description = descriptionArray.getJSONObject(0).getJSONArray("elementValue").getJSONObject(0).getString("value");
            String[] array = description.split("。");
            weatherDescription.put("description",array[0]);
            weatherDescription.put("rainfallChance",array[1]);
            weatherDescription.put("temp",array[2]);



        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    //抓出目前禮拜幾+後面六天
    public String weekDayString(){
        String result ="";
        for(int i = 0;i<7;i++){
            Calendar calender = Calendar.getInstance();
            calender.add(Calendar.DAY_OF_WEEK,i);
            SimpleDateFormat formate = new SimpleDateFormat("E");
            String weekDay = formate.format(calender.getTime());
            result = result + weekDay.replace("週","") + "   ";
        }
        return result;
    }


    public weatherData(String locationName, String elementName, String parameterName) {
        this.locationName = locationName;
        this.elementName = elementName;
        this.parameterName = parameterName;
    }

    public weatherData(String locationName, String elementName, String parameterName, String parameterValue) {
        this.locationName = locationName;
        this.elementName = elementName;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }


    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }


    public HashMap<String, JSONArray> getElementArray() {
        return elementArray;
    }

    public void setElementArray(HashMap<String, JSONArray> elementArray) {
        this.elementArray = elementArray;
    }


    public String getMinTempTextView() {
        return minTempTextView;
    }

    public void setMinTempTextView(String minTempTextView) {
        this.minTempTextView = minTempTextView;
    }

    public String getMaxTempTextView() {
        return maxTempTextView;
    }

    public void setMaxTempTextView(String maxTempTextView) {
        this.maxTempTextView = maxTempTextView;
    }


    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }


    public HashMap getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(HashMap weatherDescription) {
        this.weatherDescription = weatherDescription;
    }


}
