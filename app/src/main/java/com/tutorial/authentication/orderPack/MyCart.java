package com.tutorial.authentication.orderPack;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutorial.authentication.R;
import com.tutorial.authentication.utils.SharedPrefManager;
import com.tutorial.authentication.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MyCart extends AppCompatActivity {

    ListView listView;
    List<order> orderDetails;

    DatabaseReference mOrder;

    public SharedPrefManager sharedPrefManager;
    private final Context mContext = this;
    String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        listView = (ListView) findViewById(R.id.list);

        sharedPrefManager = new SharedPrefManager(mContext);

        mEmail = sharedPrefManager.getUserEmail();
        final String encodedEmail = Utils.encodeEmail(mEmail.toLowerCase());
        mOrder = FirebaseDatabase.getInstance().getReference("cart/"+encodedEmail);

        orderDetails = new ArrayList<>();


    }
    @Override
    protected void onStart() {
        super.onStart();

        mOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                orderDetails.clear();

                for(DataSnapshot artistSnapshot : dataSnapshot.getChildren()){

                    order mOrder = artistSnapshot.getValue(order.class);

                    orderDetails.add(mOrder);
                }

                OrderList adapter = new OrderList(MyCart.this,orderDetails);
                listView.setAdapter(adapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
