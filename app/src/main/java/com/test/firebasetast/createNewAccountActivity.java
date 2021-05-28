package com.test.firebasetast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class createNewAccountActivity extends AppCompatActivity {

    Spinner address;
    EditText addressSearch;
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

            FirebaseDatabase fbd = FirebaseDatabase.getInstance();
            DatabaseReference db = fbd.getReference();

            Log.d(TAG,"FirebaseDataBase set Value userId " + account_put + "  start ");
            db.child("users").child(account_put).child("userId").setValue(account_put);
            db.child("users").child(account_put).child("password").setValue(password_put);
            db.child("users").child(account_put).child("address").setValue(address_put);
            Log.d(TAG,"FirebaseDataBase set Value end ");

            Toast.makeText(getApplicationContext(),"createAccount sussess",Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(),"accountVlaue = "+ accountValue +  " password Value = " +passwordValue ,Toast.LENGTH_SHORT).show();

            account.setText("");
            password.setText("");
//            address.setAdapter();

        }
    };



    //設定 下拉選單 選項
    public void setSpinnerData(){
//        List<String> addressList = new ArrayList<>();
//        addressList.add("中和區");
//        addressList.add("永");
//        addressList.add("和");
//        addressList.add("區");
//        addressList.add("和區");
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
//                android.R.layout.simple_dropdown_item_1line,addressList);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.address_array,
                android.R.layout.simple_expandable_list_item_1);
        address.setAdapter(arrayAdapter);
    }


}
