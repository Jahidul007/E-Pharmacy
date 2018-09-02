package com.tutorial.authentication.drug;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tutorial.authentication.NavDrawerActivity;
import com.tutorial.authentication.R;
import com.tutorial.authentication.drug.Drug;
import com.tutorial.authentication.utils.SharedPrefManager;
import com.tutorial.authentication.utils.Utils;

public class AddDrugActivity extends AppCompatActivity {
    
    EditText brand,generic,company,strength,form;
    Button add;

    FirebaseDatabase database;
    DatabaseReference myRef;

    SharedPrefManager sharedPrefManager;

    Context mContext = this;

    private String mUsername, mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);
        
        brand = (EditText) findViewById(R.id.editTextBrandName) ;
        generic = (EditText) findViewById(R.id.editTextGenericName) ;
        company = (EditText) findViewById(R.id.editTextCompanyName) ;
        strength = (EditText) findViewById(R.id.editTextStrength) ;
        form = (EditText) findViewById(R.id.editTextForm) ;
        add = (Button) findViewById(R.id.buttonadd) ;
        
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("requestToAdd");

        sharedPrefManager = new SharedPrefManager(mContext);
        mUsername = sharedPrefManager.getName();
        mEmail = sharedPrefManager.getUserEmail();
        
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDrug();


            }
        });

    }

    private void addDrug() {

        sharedPrefManager = new SharedPrefManager(mContext);
        mUsername = sharedPrefManager.getName();
        mEmail = sharedPrefManager.getUserEmail();

        final String encodedEmail = Utils.encodeEmail(mEmail.toLowerCase());

        String brandName = brand.getText().toString().trim();
        String genericName = generic.getText().toString().trim();
        String companyName = company.getText().toString().trim();
        String strengthName = strength.getText().toString().trim();
        String formName = form.getText().toString().trim();

        if(!TextUtils.isEmpty(brandName) && !TextUtils.isEmpty(genericName)){

            Drug drug = new Drug(encodedEmail,brandName,genericName,companyName,strengthName,formName);
            myRef.child(brandName).setValue(drug);
            Toast.makeText(this,"Drug save successfully",Toast.LENGTH_LONG).show();
            brand.setText("");
            generic.setText("");
            company.setText("");
            strength.setText("");
            form.setText("");
            Intent intent = new Intent(getApplicationContext(),NavDrawerActivity.class);
            startActivity(intent);
            finish();

        } else{

            if (brandName.isEmpty()) {
                brand.setError("Brand name required");
                brand.requestFocus();

                generic.setText(genericName);
                company.setText(companyName);
                strength.setText(strengthName);
                form.setText(formName);

                return;
            } else {
                generic.setError("Generic name required");
                generic.requestFocus();

                brand.setText(brandName);
                company.setText(companyName);
                strength.setText(strengthName);
                form.setText(formName);
                return;
            }
            //Toast.makeText(this,"Drug name or generic name should not be empty", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onBackPressed() {
         Intent intent = new Intent(this, NavDrawerActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
         startActivity(intent);
         finish();
    }

}
