package com.example.cuoikytaisinh.Fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cuoikytaisinh.Model.Furniture;
import com.example.cuoikytaisinh.Adapter.FurnitureAdapter;
import com.example.cuoikytaisinh.R;
import com.example.cuoikytaisinh.Model.Utils;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    ListView listView;
    ArrayList<Furniture> arrayList;  // Danh sách các sản phẩm trong giỏ hàng
    FurnitureAdapter furnitureAdapter;
    Button btn_payment;
    SQLiteDatabase db; // Cơ sở dữ liệu AppDB

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy tham chiếu đến các view trong layout
        listView = view.findViewById(R.id.listView);
        btn_payment = view.findViewById(R.id.BTbuy);

        // Mở cơ sở dữ liệu AppDB
        db = getActivity().openOrCreateDatabase("AppDB", getContext().MODE_PRIVATE, null);

        // Khởi tạo danh sách sản phẩm và adapter
        arrayList = Utils.getFurnitureHistory();  // Danh sách các sản phẩm trong giỏ hàng, lấy từ Utils
        furnitureAdapter = new FurnitureAdapter(getContext(), arrayList);

        // Tạo bảng BILL nếu chưa có
        String sql = "CREATE TABLE IF NOT EXISTS Cart (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PRODUCT_NAME TEXT, " +
                "QUANTITY INTEGER, " +
                "TOTALPRICE FLOAT)";
        db.execSQL(sql);

        // Gắn adapter vào ListView
        listView.setAdapter(furnitureAdapter);

        // Sự kiện khi nhấn vào nút thanh toán
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });

        // Thiết lập sự kiện khi nhấn vào item trong ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Xử lý khi người dùng nhấn vào item trong giỏ hàng
                Furniture selectedFurniture = arrayList.get(position);
                // Có thể mở một màn hình chi tiết sản phẩm hoặc thực hiện hành động khác nếu cần
            }
        });
    }

    // Xử lý thanh toán và lưu các sản phẩm vào cơ sở dữ liệu
    private void processPayment() {
        if (arrayList.isEmpty()) {
            Toast.makeText(getContext(), "Giỏ hàng trống. Vui lòng thêm sản phẩm.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm tất cả sản phẩm trong giỏ hàng vào bảng Cart trong cơ sở dữ liệu
        for (Furniture furniture : arrayList) {
            addProductToCart(furniture);
        }

        // Thông báo thanh toán thành công
        Toast.makeText(getContext(), "Thanh toán thành công. Các sản phẩm đã được lưu vào giỏ hàng!", Toast.LENGTH_SHORT).show();

        // Sau khi thanh toán, có thể thực hiện các hành động khác như dọn giỏ hàng hoặc chuyển đến trang thanh toán
        arrayList.clear();  // Dọn giỏ hàng sau khi thanh toán
        furnitureAdapter.notifyDataSetChanged();  // Cập nhật giao diện ListView
    }

    // Thêm sản phẩm vào bảng Cart trong cơ sở dữ liệu
    private void addProductToCart(Furniture furniture) {
        // Lấy giá của sản phẩm (cần chỉnh sửa nếu chưa có)
        float price = furniture.getPrice();  // Chú ý: Đảm bảo rằng giá được trả về đúng kiểu float
        int quantity = furniture.getQuantity();  // Lấy số lượng sản phẩm
        float total = price * quantity;  // Tính tổng tiền

        // Tạo đối tượng ContentValues để lưu vào cơ sở dữ liệu
        ContentValues contentValues = new ContentValues();
        contentValues.put("PRODUCT_NAME", furniture.getName());  // Lưu tên sản phẩm
        contentValues.put("QUANTITY", furniture.getQuantity());  // Lưu số lượng sản phẩm
        contentValues.put("TOTALPRICE", total);  // Lưu tổng giá

        // Thực hiện thêm sản phẩm vào bảng Cart
        long result = db.insert("Cart", null, contentValues);
        if (result == -1) {
            Toast.makeText(getContext(), "Lỗi khi thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        } else {
            // Sản phẩm được thêm thành công vào bảng Cart
            Toast.makeText(getContext(), "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        }
    }
}
