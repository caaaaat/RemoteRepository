package com.test.firebasetast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    EditText account,password;
    Button logIn,clear,newAccount;
//    Toolbar toolbar;

//    public static final int SIGN_IN_REQUEST = 1;

    private Toast toast;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;


        //CWB-1804447F-FDE6-44B0-9CE8-FCDA0022B460



        init();
        //設置toolbar
        setToolBar();
        setListen();


    }



    //初始化
    public void init(){
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        logIn = findViewById(R.id.logIn);
        clear = findViewById(R.id.clear);
        newAccount = findViewById(R.id.newAccount);
//        logInByEmail = findViewById(R.id.logIn_email);
//        toolbar = findViewById(R.id.toolbar);

        //先設定好 >> 之後使用 setText + show(); 即可設定完成
        toast.makeText(context,"",Toast.LENGTH_SHORT);
    }

    private void setToolBar(){

        getSupportActionBar().setTitle("weatherForecast");

//        /**將Toolbar綁定到setSupportActionBar*/
//        setSupportActionBar(toolbar);
//        /**設置大標題*/
//        getSupportActionBar().setTitle("主標題");
//        /**設置大標題字體顏色*/
//        toolbar.setTitleTextColor(Color.WHITE);
//        /**設置副標題*/
//        toolbar.setSubtitle("副標題");
//        /**設置副標題字體顏色*/
//        toolbar.setSubtitleTextColor(Color.WHITE);
//        /**設置標題前方的Icon圖樣*/
////        toolbar.setNavigationIcon(getDrawable(R.drawable.ic_baseline_arrow_back_ios_24));
//        /**設置前方Icon與Title之距離為0(預設的很遠...)*/
//        toolbar.setContentInsetStartWithNavigation(0);
//
//        /**設置Icon圖樣的點擊事件*/
//        toolbar.setNavigationOnClickListener(v->{
//            Toast.makeText(this, "結束", Toast.LENGTH_SHORT).show();
//        });
    }

    //設定監聽器
    public void setListen(){

        //登入
        logIn.setOnClickListener(logInLinsters);
//        logInByEmail.setOnClickListener();
        //清除
        clear.setOnClickListener(view -> {
            account.setText("");
            password.setText("");
        });
        //創帳號
        newAccount.setOnClickListener(createNewAccount);


    }


    public View.OnClickListener logInLinsters = view -> {
        String TAG = "logInLinsters ";
//                GenericTypeIndicator<Map<String,String>> test =new GenericTypeIndicator<HashMap<String,String>>();

        Log.d(TAG,"test1 ");
        String accountValue = account.getText().toString();
        String passwordValue = password.getText().toString();
        Toast.makeText(getApplicationContext(),"accountVlaue = "+ accountValue +  " password Value = " +passwordValue ,Toast.LENGTH_SHORT).show();


        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference db = fbd.getReference();

        //check user account
//        db.child("users").child(accountValue).child("userId").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                String account_DB = "";
//                Boolean login_flag = false;
//                if(task.isSuccessful()){
////                    task.getResult()
//                    login_flag = true;
//                    Log.d(TAG, String.valueOf(task.getResult().getValue()) + " logIn sussess");
//                    task.getResult(memberData.class);
//                    task.getResult()
//                }else{
//                    Log.e(TAG, "Error getting data", task.getException());
//                }
//            }
//        });

        db.child("users").addValueEventListener(new ValueEventListener() {
            String TAG = "usersData";
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map value =  (Map<String,Object>)snapshot.getValue();
//                memberData test = snapshot.getValue(memberData.class);
//                Log.d(TAG,"account is " + test.getAccount());
//                Log.d(TAG,"account is " + test.getAddress());
//                Log.d(TAG,"account is " + test.getPassword());


                if(value.get(accountValue) == null){
                    Log.d(TAG,"login fail account not exists " );
                    Toast.makeText(getApplicationContext(),
                            "account not exists ",Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(TAG,"Value is " + value.get(accountValue));

                    //目前還沒能檢察密碼是否正確
//                    for(DataSnapshot test:snapshot.getChildren()){
//                        test.
//                    }


                    Toast.makeText(getApplicationContext(),
                            accountValue +" login Sussess ",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,weatherActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("account",accountValue);

                    account.setText("");
                    password.setText("");

                    intent.putExtras(bundle);
                    startActivityForResult(intent,2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"have no this account");
                Log.d(TAG, "onCancelled: " + error);
                Toast.makeText(getApplicationContext(),
                        "have no account ",Toast.LENGTH_SHORT).show();
            }
        });


//        ValueEventListener dataListener = new ValueEventListener() {
//            String TAG = "dataListener";
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
////                memberData md= snapshot.getValue(memberData.class);
////                memberData test= snapshot.getValue(memberData.class);
////                snapshot.getKey();
//
//                 Map<String,Object> map = (Map<String,Object>)snapshot.getValue();
//
////                if(accountValue == md.getAccount()) test11 = true;
//                Log.d(TAG, "onDataChange: " + map.get("account") );
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//        db.addValueEventListener(dataListener);






//        db.child("users").child(accountValue).child("userId").setValue(accountValue);
//        db.child("users").child(accountValue).child("userPassword").setValue(passwordValue);
////        db.child("users").child(accountValue).child("userName").setValue("userName");
//
//        Log.d(TAG, "set Value database ");
//
//        Intent intent = new Intent();
//        intent.setClass(MainActivity.this,weatherActivity.class);
//        Bundle bundle = new Bundle();
//        //目前userName寫死
//        bundle.putString("userName","userName");
//        bundle.putString("userId",accountValue);
//        intent.putExtras(bundle);
//        startActivityForResult(intent,1);

    };




    //創帳號
    private View.OnClickListener createNewAccount = view -> {

        Intent intent = new Intent();
        intent.setClass(MainActivity.this,createNewAccountActivity.class);
        Bundle bundle = new Bundle();

        intent.putExtras(bundle);
        startActivityForResult(intent,2);
//        onNewIntent();

//        startActivity(new Intent(MainActivity.this,createNewAccountActivity.class));

    };


}