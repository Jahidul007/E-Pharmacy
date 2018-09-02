package com.tutorial.authentication.comment;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutorial.authentication.R;
import com.tutorial.authentication.orderPack.MyCart;
import com.tutorial.authentication.orderPack.OrderList;
import com.tutorial.authentication.orderPack.order;
import com.tutorial.authentication.utils.SharedPrefManager;
import com.tutorial.authentication.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {


    EditText editText;
    ImageView img;

    ListView recyclerView;

    public SharedPrefManager sharedPrefManager;
    private final Context mContext = this;
    String mEmail;

    List<Comment> commentList;

    DatabaseReference mComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);



        editText =(EditText) findViewById(R.id.commenttext);
        img = (ImageView) findViewById(R.id.send);
        recyclerView = (ListView) findViewById(R.id.my_recycler_view);



//        final String encodedEmail = Utils.encodeEmail(mEmail.toLowerCase());
        mComment = FirebaseDatabase.getInstance().getReference("comment");

        commentList = new ArrayList<>();
        
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();

                editText.setText("");
            }
        });


    }

    private void addComment() {

        sharedPrefManager = new SharedPrefManager(mContext);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String mName = firebaseUser.getDisplayName();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        String id  = mComment.push().getKey();

        String message = editText.getText().toString().trim();

        if(!TextUtils.isEmpty(message)){

            Comment comment = new Comment(id,mName,message,currentDateandTime);

            mComment.child(currentDateandTime).setValue(comment);



            System.out.println("currentDateandTime:"+currentDateandTime);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mComment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                commentList.clear();

                for(DataSnapshot artistSnapshot : dataSnapshot.getChildren()){

                    Comment mOrder = artistSnapshot.getValue(Comment.class);

                    commentList.add(mOrder);
                }

                CommentList adapter = new CommentList(CommentActivity.this,commentList);
                recyclerView.setAdapter(adapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
