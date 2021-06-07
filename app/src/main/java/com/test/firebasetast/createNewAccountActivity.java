package com.test.firebasetast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class createNewAccountActivity extends AppCompatActivity {

    Spinner address,subAddress;
    EditText account,password;
    Button create_newAccount,create_back;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createactivity_layout);

        //目前沒有傳值進來
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        init();
        setListener();
        setSpinnerData();
    }

    public void init(){
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);

        address = findViewById(R.id.address);
        subAddress = findViewById(R.id.subAddress);
//        addressSearch = findViewById(R.id.addressSearch);

        create_newAccount = findViewById(R.id.create_newAccount);
        create_back = findViewById(R.id.create_back);



    }

    public void setListener(){
        String TAG = "setListener";
        Log.d(TAG,"setListener start");
        //創帳號
        create_newAccount.setOnClickListener(createAccount);
        //上一頁
        create_back.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(createNewAccountActivity.this,MainActivity.class);
            Bundle bundle = new Bundle();

            intent.putExtras(bundle);
            startActivityForResult(intent,3);
        });


    }

    public View.OnClickListener createAccount = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String TAG = "createAccount";
            String account_put =account.getText().toString();
            String password_put = password.getText().toString();
            String address_put = (String)address.getSelectedItem();
            String subAddress_put = (String)subAddress.getSelectedItem();

            FirebaseDatabase fbd = FirebaseDatabase.getInstance();
            DatabaseReference db = fbd.getReference();

            Log.d(TAG,"FirebaseDataBase set Value userId " + account_put + "  start ");
            db.child("users").child(account_put).child("userId").setValue(account_put);
            db.child("users").child(account_put).child("password").setValue(password_put);
            db.child("users").child(account_put).child("address").setValue(address_put);
            db.child("users").child(account_put).child("subAddress").setValue(subAddress_put);
            Log.d(TAG,"FirebaseDataBase set Value end ");

            Toast.makeText(getApplicationContext(),"createAccount sussess",Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "onClick: "+ "accountVlaue = "+ account_put +  " password Value = " +password_put +
//                    "address_put + subAddress_put " + address_put + "  " + subAddress_put);

            account.setText("");
            password.setText("");
//            address.setAdapter();

        }
    };



    //設定 下拉選單 選項
    public void setSpinnerData(){
        String TAG = "setSpinnerData";
        FirebaseDatabase fbd = FirebaseDatabase.getInstance();
        DatabaseReference db = fbd.getReference();


        db.child("location").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            //跟ui同線程
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: is successful");
                    HashMap<String, ArrayList> sublocation = (HashMap) task.getResult().getValue();

                    //主下拉選單
                    ArrayList<String> location = new ArrayList<>();
                    for(String sublocationKey : sublocation.keySet()){
                        location.add(sublocationKey);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),
                                    android.R.layout.simple_dropdown_item_1line,location);
                            address.setAdapter(arrayAdapter);

                            ArrayAdapter arrayAdapter2 = new ArrayAdapter(getApplicationContext(),
                                    android.R.layout.simple_dropdown_item_1line,sublocation.get(location.get(0)).toArray());
                            subAddress.setAdapter(arrayAdapter2);


                            address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    int position = address.getSelectedItemPosition();
                                    ArrayAdapter arrayAdapter2 = new ArrayAdapter(getApplicationContext(),
                                            android.R.layout.simple_dropdown_item_1line,sublocation.get(location.get(position)).toArray());
                                    subAddress.setAdapter(arrayAdapter2);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    Log.d(TAG, "onNothingSelected: error ");
                                }
                            });
                        }
                    });
                }else {
                    Log.d(TAG, "onComplete: is not successful");
                }
            }
        });




//        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.address_array,
//                android.R.layout.simple_expandable_list_item_1);
//        address.setAdapter(arrayAdapter);
    }


}
