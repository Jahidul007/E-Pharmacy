package com.tutorial.authentication.comment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tutorial.authentication.R;
import com.tutorial.authentication.orderPack.order;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommentList extends ArrayAdapter<Comment> {

    private Activity context;
    private List<Comment> orderList;


    public CommentList(Activity context, List<Comment> orderList) {
        super(context, R.layout.layout_comment_list, orderList);
        this.context = context;
        this.orderList = orderList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.layout_comment_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewUser);
        TextView textViewQty = (TextView) listViewItem.findViewById(R.id.textViewComment);
        TextView textViewdt = (TextView) listViewItem.findViewById(R.id.textViewDate);


        Comment mOrder = orderList.get(position);

        textViewName.setText(mOrder.getUser());
        textViewQty.setText(mOrder.getMessage());
        textViewdt.setText(mOrder.getDate());

        return listViewItem;
    }
}
