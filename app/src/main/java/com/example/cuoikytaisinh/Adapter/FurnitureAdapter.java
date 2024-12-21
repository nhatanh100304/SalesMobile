package com.example.cuoikytaisinh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuoikytaisinh.Model.Furniture;
import com.example.cuoikytaisinh.R;

import java.util.ArrayList;

public class FurnitureAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Furniture> furnitureList;

    // Constructor cho FurnitureAdapter
    public FurnitureAdapter(Context context, ArrayList<Furniture> furnitureList) {
        this.context = context;
        this.furnitureList = furnitureList;
    }

    @Override
    public int getCount() {
        return furnitureList.size();
    }

    @Override
    public Object getItem(int position) {
        return furnitureList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Sử dụng ViewHolder để tối ưu hóa hiệu suất ListView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_furniture, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.textViewName = convertView.findViewById(R.id.textViewName);
            holder.textViewDescription = convertView.findViewById(R.id.textViewDescription);
            holder.textViewPrice = convertView.findViewById(R.id.textViewPrice);
            holder.textViewQuantity = convertView.findViewById(R.id.textQuantity);
            // Thêm TextView cho giá
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy đối tượng Furniture tại vị trí hiện tại
        Furniture furniture = furnitureList.get(position);

        // Gán dữ liệu vào các TextView
        holder.textViewName.setText(furniture.getName());
        holder.textViewDescription.setText(furniture.getDescription());
        holder.textViewPrice.setText(String.valueOf(furniture.getPrice()));
        holder.textViewQuantity.setText(String.valueOf(furniture.getQuantity()));
        // Gán giá sản phẩm

        // Lấy tên ảnh từ đối tượng Furniture
        String imageName = furniture.getImage(); // Lấy tên file ảnh

        // Xử lý hiển thị ảnh từ thư mục drawable
        if (imageName != null && !imageName.isEmpty()) {
            int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            if (imageId != 0) {
                holder.imageView.setImageResource(imageId); // Gán hình ảnh từ drawable
            } else {
                holder.imageView.setImageResource(R.drawable.tra1); // Hình ảnh mặc định nếu không tìm thấy
            }
        } else {
            holder.imageView.setImageResource(R.drawable.tra1); // Hình ảnh mặc định nếu không có tên ảnh
        }

        return convertView;
    }

    // ViewHolder để giữ các view con
    private static class ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewDescription;
        TextView textViewPrice;
        TextView textViewQuantity;// Thêm textView cho giá
    }
}