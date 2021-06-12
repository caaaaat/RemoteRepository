package com.test.firebasetast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
        setListen();


    }


    //初始化
    public void init(){
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        logIn = findViewById(R.id.logIn);
        clear = findViewById(R.id.clear);
        newAccount = findViewById(R.id.newAccount);

        //先設定好 >> 之後使用 setText + show(); 即可設定完成
        toast.makeText(context,"",Toast.LENGTH_SHORT);
    }

    //設定監聽器
    public void setListen(){

        //登入
        logIn.setOnClickListener(logInLinsters);
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
        Log.d(TAG,"test1 ");

        String accountValue = account.getText().toString();

        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference db = fbd.getReference();
        //目前還沒能檢察密碼是否正確
        db.child("users").addValueEventListener(new ValueEventListener() {
            String TAG = "usersData";
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map value =  (Map<String,Object>)snapshot.getValue();

                if(value.get(accountValue) == null){
                    Log.d(TAG,"login fail account not exists " );
                    Toast.makeText(getApplicationContext(),
                            "account not exists ",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),
                            accountValue +" login Sussess ",Toast.LENGTH_SHORT).show();

                    //使用者地址
                    String address = (String) snapshot.child(accountValue).child("address").getValue();
                    String subaddress = (String) snapshot.child(accountValue).child("subAddress").getValue();


                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,weatherActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("account",accountValue);
                    bundle.putString("address",address);
                    bundle.putString("subaddress",subaddress);

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

    };

    //創帳號
    private View.OnClickListener createNewAccount = view -> {

        Intent intent = new Intent();
        intent.setClass(MainActivity.this,createNewAccountActivity.class);
        Bundle bundle = new Bundle();

        intent.putExtras(bundle);
        startActivity(intent);
        startActivityForResult(intent,2);

    };


}