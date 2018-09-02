package com.tutorial.authentication.orderPack;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tutorial.authentication.R;

import java.util.List;

public class OrderList extends ArrayAdapter<order> {
    private Activity context;
    private List<order> orderList;


    public OrderList(Activity context, List<order> orderList) {
        super(context, R.layout.layout_order_list,orderList);
        this.context = context;
        this.orderList = orderList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.layout_order_list,null,true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewQty = (TextView) listViewItem.findViewById(R.id.textViewQuant);

        order mOrder = orderList.get(position);

        textViewName.setText(mOrder.getName());
        textViewQty.setText(mOrder.getQty());

        return listViewItem;
    }
}
