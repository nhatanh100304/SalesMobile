package com.example.cuoikytaisinh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cuoikytaisinh.Model.Bill;
import com.example.cuoikytaisinh.R;

import java.util.ArrayList;

public class BillAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Bill> arrayList;  // Dữ liệu từ cơ sở dữ liệu (Cart table)

    public BillAdapter(Context context, int layout, ArrayList<Bill> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_bill, parent, false);
        }

        // Lấy các TextView để hiển thị dữ liệu
        TextView quantity = convertView.findViewById(R.id.quantity);
        TextView id = convertView.findViewById(R.id.id);
        TextView totalPrice = convertView.findViewById(R.id.totalPrice);

        // Lấy dữ liệu từ danh sách arrayList
        Bill bill = arrayList.get(position);

        // Hiển thị các dữ liệu của Bill
        id.setText("ID: " + bill.getId());
        quantity.setText("Quantity: " + bill.getQuantity());
        totalPrice.setText("Total: " + bill.getPrice());

        return convertView;
    }
}
