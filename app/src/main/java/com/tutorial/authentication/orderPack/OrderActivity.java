package com.tutorial.authentication.orderPack;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tutorial.authentication.NavDrawerActivity;
import com.tutorial.authentication.R;
import com.tutorial.authentication.brands.BrandActivity;
import com.tutorial.authentication.brands.BrandDetailActivity;
import com.tutorial.authentication.shipping.ShippingActivity;
import com.tutorial.authentication.utils.SharedPrefManager;
import com.tutorial.authentication.utils.Utils;

import java.io.IOException;

public class OrderActivity extends AppCompatActivity {

    Button addOrder, decrease, increase;
    EditText editTextMed;
    ImageView img;

    public SharedPrefManager sharedPrefManager;
    private final Context mContext = this;

    private static final int CHOOSE_IMAGE = 111;

    String mEmail;

    FirebaseAuth mAuth;
    Uri uriProfileImage;
    TextView textView;

    ProgressBar progressBar;

    String profileImageUrl;

    DatabaseReference mDatabaseReference;
    int x = 1;
    int in;
    String stringVal;

    TextView textViewPrice;

    double value;

    String priceValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        addOrder = (Button) findViewById(R.id.buttonAddToCart);
        decrease = (Button) findViewById(R.id.buttonDecrese);
        increase = (Button) findViewById(R.id.buttonIncrease);

        editTextMed = (EditText) findViewById(R.id.editTextMedicine);
        textView = (TextView) findViewById(R.id.textViewQuantity);

        textViewPrice = (TextView) findViewById(R.id.textViewPrice);

        //img = (ImageView) findViewById(R.id.camera);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        Intent intent = getIntent();
        String brandName = intent.getStringExtra(BrandDetailActivity.BRAND_NAME);
        String price = intent.getStringExtra(BrandDetailActivity.BRAND_PRICE);

        value = Double.parseDouble(price);

        priceValue = price;

        editTextMed.setText(brandName);
        textViewPrice.setText(price);

        disableEditText(editTextMed);


        System.out.println("bradname" + brandName);
        System.out.println("bradname" + price);
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                x = Integer.parseInt(textView.getText().toString());
                String stringVal;
                Log.d("src", "Decreasing value...");

                if (x > 1) {
                    x = x - 1;
                    stringVal = String.valueOf(x);

                    double price = value * x;

                    priceValue = String.valueOf(price);
                    textViewPrice.setText(priceValue);

                    System.out.println(priceValue);

                    textView.setText(stringVal);
                } else {
                    Log.d("src", "Value can't be less than 1");
                }
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                x = Integer.parseInt(textView.getText().toString());

                Log.d("src", "Increasing value...");
                if (x <= 9) {
                    x = x + 1;
                    stringVal = String.valueOf(x);
                    textView.setText(stringVal);

                    double price = value * x;

                    priceValue = String.valueOf(price);
                    textViewPrice.setText(priceValue);

                    System.out.println(priceValue);
                } else {
                    Log.d("src", "Value can't be greater than 10");
                }
            }
        });


        mAuth = FirebaseAuth.getInstance();

        //loadUserInformation();

        /**img.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
        showImageChooser();

        }
        });**/


        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
                finish();
                //Toast.makeText(getApplicationContext(), "Add to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, NavDrawerActivity.class));
        }
    }

    private void loadUserInformation() {

        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(img);
            }
        }

    }

    private void addToCart() {

        sharedPrefManager = new SharedPrefManager(mContext);

        mEmail = sharedPrefManager.getUserEmail();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        //mEmail = firebaseUser.getEmail();
        final String encodedEmail = Utils.encodeEmail(mEmail.toLowerCase());
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("cart/" + encodedEmail);
        System.out.println("Email: " + mDatabaseReference);
        System.out.println("profileImageUrl: " + profileImageUrl);
        System.out.println("profileImageUrl: " + firebaseUser);
        String medicine = editTextMed.getText().toString().trim();

        String price = String.valueOf(priceValue);
        System.out.println(priceValue);

        if (firebaseUser != null && medicine != null && x != 0 && priceValue != null) {


            String qty = String.valueOf(x);

            String id = mDatabaseReference.push().getKey();

            order mOrder = new order(id, medicine, qty, priceValue);

            mDatabaseReference.child(medicine).setValue(mOrder);

            System.out.println("qty: " + qty);
            System.out.println("price: " + priceValue);

            Toast.makeText(this, "Add to cart List", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(OrderActivity.this, MyCart.class);
            startActivity(intent);
            finish();


        } else {

            if (medicine.isEmpty()) {

                editTextMed.setError("Medicine Name Required");
                editTextMed.requestFocus();

                Toast.makeText(OrderActivity.this, "Medicine name is Empty", Toast.LENGTH_SHORT).show();

            }
        }
    }

    /**
     * @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     * super.onActivityResult(requestCode, resultCode, data);
     * <p>
     * if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
     * <p>
     * uriProfileImage = data.getData();
     * <p>
     * try {
     * Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
     * img.setImageBitmap(bitmap);
     * <p>
     * uploadImageToFireBase();
     * <p>
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * }
     * <p>
     * <p>
     * }
     * <p>
     * private void uploadImageToFireBase() {
     * StorageReference profileImageRef =
     * FirebaseStorage.getInstance().getReference("prescriptionPics/" + System.currentTimeMillis() + ".jpg");
     * <p>
     * if (uriProfileImage != null) {
     * progressBar.setVisibility(View.VISIBLE);
     * profileImageRef.putFile(uriProfileImage)
     * .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
     * @Override public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
     * progressBar.setVisibility(View.GONE);
     * <p>
     * profileImageUrl = taskSnapshot.getDownloadUrl().toString();
     * <p>
     * System.out.println("profileImageUrl: " + profileImageUrl);
     * }
     * })
     * .addOnFailureListener(new OnFailureListener() {
     * @Override public void onFailure(@NonNull Exception e) {
     * progressBar.setVisibility(View.GONE);
     * Toast.makeText(OrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
     * }
     * });
     * }
     * }
     * <p>
     * private void showImageChooser() {
     * Intent intent = new Intent();
     * intent.setType("image/*");
     * intent.setAction(Intent.ACTION_GET_CONTENT);
     * startActivityForResult(Intent.createChooser(intent, "Select profile image"), CHOOSE_IMAGE);
     * }
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.cart) {
            /**if (in == 0)
             Toast.makeText(this, "No items in cart yet", Toast.LENGTH_SHORT).show();
             else {**/
            Intent i = new Intent(this, MyCart.class);
            //i.putExtra("Mednames", medinames);
            //i.putIntegerArrayListExtra("qty", qty);
            //i.putExtra("num", in);
            startActivity(i);


        }
        return super.onOptionsItemSelected(item);
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        //editText.setBackgroundColor(Color.TRANSPARENT);
    }
}
