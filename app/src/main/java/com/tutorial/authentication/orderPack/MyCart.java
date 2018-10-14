package com.tutorial.authentication.orderPack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tutorial.authentication.NavDrawerActivity;
import com.tutorial.authentication.R;
import com.tutorial.authentication.brands.BrandActivity;
import com.tutorial.authentication.shipping.ShippingActivity;
import com.tutorial.authentication.utils.SharedPrefManager;
import com.tutorial.authentication.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyCart extends AppCompatActivity {

    EditText editTextQuantity;
    ImageView img;
    private static final int CHOOSE_IMAGE = 111;


    FirebaseAuth mAuth;
    Uri uriProfileImage;
    TextView textView, textViewMedicine;

    ProgressBar progressBar;

    String profileImageUrl;

    ListView listView;
    List<order> orderDetails;

    DatabaseReference mOrder, mCheckOut;

    public SharedPrefManager sharedPrefManager;
    private final Context mContext = this;
    String mEmail;

    Button checkOut;

    double price = 0, totalPrice = 0;
    String tolPrice;

    public static final String mPRICE = "price";
    public static final String mTitle = "name";
    public static final String PRICE = "price";
    public static final String IMAGEURL = "img";

    RelativeLayout layout;

    Button update, delete, ok;

    String encodedEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        textView = (TextView) findViewById(R.id.textViewTotalPrice);

        listView = (ListView) findViewById(R.id.list);

        sharedPrefManager = new SharedPrefManager(mContext);

        checkOut = (Button) findViewById(R.id.buttonCheckOut);

        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        ok = (Button) findViewById(R.id.buttonOk);

        mEmail = sharedPrefManager.getUserEmail();
        encodedEmail = Utils.encodeEmail(mEmail.toLowerCase());
        mOrder = FirebaseDatabase.getInstance().getReference("cart/" + encodedEmail);
        mCheckOut = FirebaseDatabase.getInstance().getReference("order/" + encodedEmail);

        orderDetails = new ArrayList<>();

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        img = (ImageView) findViewById(R.id.camera);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();

            }
        });

        layout = (RelativeLayout) findViewById(R.id.updateDelete);

        textViewMedicine = (TextView) findViewById(R.id.textViewMedicine);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final order order = orderDetails.get(i);

                layout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);

                final String name = order.getName();
                final String id = order.getId();
                final String price = order.getPrice();
                final String mqty = order.getQty();

                Double value = Double.parseDouble(price);
                Double qty = Double.parseDouble(mqty);

                double newPrice = value / qty;
                final String mPrice = String.valueOf(newPrice);

                textViewMedicine.setText("Update or Delete " + name);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteCart(name);
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editTextQuantity.setVisibility(View.VISIBLE);
                        ok.setVisibility(View.VISIBLE);
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        updateCart(id, name, mPrice);

                    }
                });

                return false;
            }

        });

    }

    private void updateCart(String id, String name, String price) {

        DatabaseReference cartdelete = FirebaseDatabase.getInstance().getReference("cart/" + encodedEmail).child(name);

        String qty = editTextQuantity.getText().toString().trim();

        if (!qty.isEmpty()) {
            Double value = Double.parseDouble(price);
            Double mqty = Double.parseDouble(qty);
            String total = String.valueOf(value * mqty);

            order order = new order(id, name, qty, total);

            cartdelete.setValue(order);

            listView.setVisibility(View.VISIBLE);
            layout.setVisibility(View.INVISIBLE);
            editTextQuantity.setText("");
        } else {
            editTextQuantity.setError("Input Quanty");
            editTextQuantity.requestFocus();
        }
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();


        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private void deleteCart(String name) {

        order order = new order();

        DatabaseReference cartdelete = FirebaseDatabase.getInstance().getReference("cart/" + encodedEmail).child(name);

        System.out.println("Cartdelete " + cartdelete);
        cartdelete.removeValue();
        listView.setVisibility(View.VISIBLE);
        layout.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();

        mOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                orderDetails.clear();

                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()) {

                    order mOrder = artistSnapshot.getValue(order.class);

                    String getPrice = mOrder.getPrice();


                    System.out.println(getPrice);

                    price = Double.parseDouble(getPrice);

                    orderDetails.add(mOrder);

                    totalPrice += price;
                }

                System.out.println("Total Price: " + totalPrice);

                tolPrice = String.valueOf(totalPrice);
                textView.setText("৳ " + tolPrice);

                totalPrice -= totalPrice;

                OrderList adapter = new OrderList(MyCart.this, orderDetails);
                listView.setAdapter(adapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (profileImageUrl != null && !tolPrice.equals("0.0")) {
                    Intent intent = new Intent(MyCart.this, ShippingActivity.class);

                    intent.putExtra(PRICE, tolPrice);
                    intent.putExtra(IMAGEURL, profileImageUrl);

                    startActivity(intent);

                } else {
                    if (tolPrice.equals("0.0")) {
                        Toast.makeText(MyCart.this, "No item available", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MyCart.this, "Upload a Image!!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                img.setImageBitmap(bitmap);

                uploadImageToFireBase();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void uploadImageToFireBase() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("prescriptionPics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);

                            profileImageUrl = taskSnapshot.getDownloadUrl().toString();

                            System.out.println("profileImageUrl: " + profileImageUrl);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MyCart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select profile image"), CHOOSE_IMAGE);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        layout.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
