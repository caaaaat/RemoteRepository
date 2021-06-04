package com.test.firebasetast;

import com.google.firebase.database.annotations.NotNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    /*最主要是用註記來判斷要執行的動作，常用註記如下：
        @BeforeClass：方法首先被執行，只執行一次。
        @AfterClass：方法最後被執行，只執行一次。
        @Before：每執行 Test 前執行一次。
        @After：每執行 Test 後執行一次。
        @Test：測試的功能內容。
        @BeforeClass/@AfterClass 都要皆為 static。
    */
    String TAG = "test";

//    @BeforeClass
//    public static void testBeforeClass() throws Exception {
//        System.out.println("testBeforeClass");
//    }
//
//    @Before
//    public void testBefore() throws Exception {
//        System.out.println("testBefore");
//    }

//    @Test
//    public void testCase() {
//        System.out.println("--------");
//        System.out.println("testCase");
//        System.out.println("--------");
//    }


//    @Test
//    public void test(){
//        String str11 = "你好123abc";
//        String test = "%E9%A0%AD%E5%9F%8E%E9%8E%AE";
//        try {
//            String result = URLEncoder.encode(str11, "utf-8");
////            Log.e(TAG,"URL编码:"+result);// 输出 URL编码:%E4%BD%A0%E5%A5%BD123abc
//            System.out.println(result);
//            result = URLDecoder.decode(test,"utf-8");
//            System.out.println(result);
////            Log.e(TAG, "test: " + result );
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

//    @Test
//    public void test(){
//        System.out.println("onResponse start " );
//        String jsonData = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/" +
//                "F-D0047-003?Authorization=CWB-1804447F-FDE6-44B0-9CE8-FCDA0022B460&format=JSON&locationName=%E9%A0%AD%E5%9F%8E%E9%8E%AE";
//
//        /**建立連線*/
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
//                .build();
//
//        Request request = new Request.Builder()
//                .url(jsonData)
//                .build();
//
//        /**設置回傳*/
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                /**如果傳送過程有發生錯誤*/
////                Log.d(TAG, "onFailure: sendGET 出錯 ");
//                System.out.println("onFailure onFailure");
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                /**取得回傳*/
////                tvRes.setText("GET回傳：\n" + response.body().string());
////                String TAG = "onResponse";
////                Log.d(TAG, "onResponse: " + "test start"  );
////                Log.d(TAG, "onResponse:  目前線程 " + Thread.currentThread().getId() );
//                try {
//
//                    JSONObject test = new JSONObject(response.body().string());
//                    String locationName = test.getJSONObject("records").getJSONArray("locations").getJSONObject(0).getJSONArray("location").getJSONObject(0)
//                            .getString("locationName");
//
//                    System.out.println("onResponse "  + locationName);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    @Test
    public void test(){
//        long time = System.curretTimeMillis();
//        Date date = new Date(time);
//        SimpleDateFormat formate = new SimpleDateFormat("E");
//        String test = formate.format(date);
//        System.out.println("time " + test);


        List list = new ArrayList();
        for(int i = 0;i<7;i++){
            Calendar calender = Calendar.getInstance();
            calender.add(Calendar.DAY_OF_WEEK,i);
            SimpleDateFormat formate = new SimpleDateFormat("E");
            String weekDay = formate.format(calender.getTime());
            System.out.println("time " + weekDay.replace("星期",""));
            list.add(weekDay.replace("星期",""));
        }
    }

//    @After
//    public void testAfter() throws Exception {
//        System.out.println("testAfter");
//    }
//
//    @AfterClass
//    public static void testAfterClass() throws Exception {
//        System.out.println("testAfterClass");
//    }

}