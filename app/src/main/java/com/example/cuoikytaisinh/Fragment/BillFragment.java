package com.example.cuoikytaisinh.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cuoikytaisinh.Adapter.BillAdapter;
import com.example.cuoikytaisinh.Model.Bill;
import com.example.cuoikytaisinh.R;

import java.util.ArrayList;

public class BillFragment extends Fragment {

    private ListView listViewBill;
    private TextView tvTotalAmount;  // Tham chiếu đến TextView hiển thị tổng tiền
    private SQLiteDatabase db;  // Cơ sở dữ liệu AppDB
    private ArrayList<Bill> array = new ArrayList<>();

    public BillFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout XML cho Fragment
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        // Lấy tham chiếu đến ListView và TextView
        listViewBill = view.findViewById(R.id.listViewBill);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);

        // Mở cơ sở dữ liệu AppDB
        db = getActivity().openOrCreateDatabase("AppDB", getContext().MODE_PRIVATE, null);

        // Gọi phương thức để lấy dữ liệu từ Cart và tính tổng tiền
        loadCartData();

        // Sự kiện cho nút thanh toán
        view.findViewById(R.id.btnPayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa tất cả các mục trong ListView và dọn dẹp cơ sở dữ liệu (nếu cần)
                array.clear();
                BillAdapter billAdapter = (BillAdapter) listViewBill.getAdapter();
                billAdapter.notifyDataSetChanged();  // Cập nhật lại giao diện ListView

                // Thông báo thanh toán thành công
                Toast.makeText(getContext(), "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

                // Xóa dữ liệu giỏ hàng trong cơ sở dữ liệu (bảng Cart)
                db.delete("Cart", null, null);

                // Cập nhật lại tổng tiền sau khi thanh toán (đặt lại về 0)
                tvTotalAmount.setText("Tổng tiền: 0 VND");
            }
        });

        return view;  // Trả về view đã inflate
    }

    // Phương thức lấy dữ liệu từ Cart và tính tổng tiền
    private void loadCartData() {
        array.clear();  // Xóa dữ liệu cũ trong array

        // Truy vấn dữ liệu từ bảng Cart
        Cursor cursor = db.query("Cart", null, null, null, null, null, null);

        // Biến để tính tổng tiền
        float totalAmount = 0;

        // Xử lý kết quả truy vấn
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY"));
                float totalPrice = cursor.getFloat(cursor.getColumnIndexOrThrow("TOTALPRICE"));

                // Tạo đối tượng Bill và thêm vào danh sách
                Bill bill = new Bill(id, quantity, totalPrice);
                array.add(bill);

                // Cộng dồn tổng tiền
                totalAmount += totalPrice;
            }
        } else {
            Log.d("BillFragment", "Không có dữ liệu trong bảng Cart");
        }

        // Đóng cursor sau khi sử dụng
        cursor.close();

        // Cập nhật tổng tiền vào TextView
        tvTotalAmount.setText("Tổng tiền: " + totalAmount + " VND");

        // Tạo BillAdapter và gắn vào ListView
        BillAdapter billAdapter = new BillAdapter(getContext(), R.layout.item_bill, array);
        listViewBill.setAdapter(billAdapter);
    }
}



