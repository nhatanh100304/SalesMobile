package com.example.cuoikytaisinh.Fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cuoikytaisinh.R;
import com.example.cuoikytaisinh.Model.Users;

import java.util.ArrayList;

public class AccountFragment extends Fragment {

    private EditText usernameEditText, passwordEditText;
    private Button editButton, saveButton, deleteButton;
    private ListView userListView;
    private ArrayList<Users> userList;
    private ArrayAdapter<Users> userAdapter;
    private SQLiteDatabase db;
    private int selectedUserId = -1; // ID của người dùng đang được chọn để sửa hoặc xóa

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout fragment_account.xml
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Khởi tạo các trường nhập liệu và nút
        usernameEditText = view.findViewById(R.id.User);
        passwordEditText = view.findViewById(R.id.Pasword);
        editButton = view.findViewById(R.id.editButton);
        saveButton = view.findViewById(R.id.saveButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        userListView = view.findViewById(R.id.userListView);

        // Mở cơ sở dữ liệu SQLite
        db = getActivity().openOrCreateDatabase("AppDB", getContext().MODE_PRIVATE, null);

        // Load danh sách người dùng khi Fragment được tạo
        loadUserList();

        // Xử lý sự kiện khi người dùng bấm nút "Sửa"
        editButton.setOnClickListener(v -> editUser());

        // Xử lý sự kiện khi người dùng bấm nút "Lưu"
        saveButton.setOnClickListener(v -> saveUser());

        // Xử lý sự kiện khi người dùng bấm nút "Xóa"
        deleteButton.setOnClickListener(v -> deleteUser());

        return view;
    }

    private void loadUserList() {
        // Khởi tạo danh sách người dùng
        userList = new ArrayList<>();

        // Truy vấn dữ liệu từ bảng `users`
        Cursor cursor = db.query("users",null,null,null,null,null,null,null);

        if (cursor.moveToFirst()) {
            do {
                // Lấy dữ liệu từ mỗi hàng trong cursor
                int id = cursor.getInt(0);
                String username = cursor.getString(1);
                String password = cursor.getString(2);

                // Thêm user vào danh sách
                userList.add(new Users(id, username, password));
            } while (cursor.moveToNext());
        }
        cursor.close();


        // Thiết lập ArrayAdapter cho ListView
        userAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userList);
        userListView.setAdapter(userAdapter);

        // Xử lý khi người dùng chọn một item
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            Users selectedUser = userList.get(position);
            selectedUserId = selectedUser.getId();
            usernameEditText.setText(selectedUser.getUsername());
            passwordEditText.setText(selectedUser.getPassword());

            // Kích hoạt các nút
            editButton.setEnabled(true);
            saveButton.setEnabled(true);
            deleteButton.setEnabled(true);
        });
    }

    // Phương thức "Sửa" người dùng
    private void editUser() {
        // Bật chế độ sửa thông tin
        usernameEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
    }

    // Phương thức "Lưu" thông tin người dùng
    private void saveUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Kiểm tra nếu các trường không trống
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin người dùng trong cơ sở dữ liệu
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        int rowsUpdated = db.update("users", values, "id = ?", new String[]{String.valueOf(selectedUserId)});

        if (rowsUpdated > 0) {
            // Cập nhật thành công
            Toast.makeText(getContext(), "Thông tin người dùng đã được cập nhật!", Toast.LENGTH_SHORT).show();
            // Load lại danh sách người dùng
            loadUserList();
        } else {
            // Cập nhật thất bại
            Toast.makeText(getContext(), "Cập nhật thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }

        // Tắt chế độ sửa thông tin
        usernameEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        // Tắt nút Lưu
        saveButton.setEnabled(false);
    }

    // Phương thức "Xóa" người dùng
    private void deleteUser() {
        if (selectedUserId == -1) {
            Toast.makeText(getContext(), "Vui lòng chọn người dùng để xóa!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Xóa người dùng khỏi cơ sở dữ liệu
        int rowsDeleted = db.delete("users", "id = ?", new String[]{String.valueOf(selectedUserId)});

        if (rowsDeleted > 0) {
            // Xóa thành công
            Toast.makeText(getContext(), "Người dùng đã bị xóa!", Toast.LENGTH_SHORT).show();
            // Load lại danh sách người dùng
            loadUserList();
        } else {
            // Xóa thất bại
            Toast.makeText(getContext(), "Xóa thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }

        // Tắt các nút sửa, lưu, xóa
        usernameEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        editButton.setEnabled(false);
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}
