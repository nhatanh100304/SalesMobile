package com.example.cuoikytaisinh;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Mở hoặc tạo cơ sở dữ liệu SQLite
        db = openOrCreateDatabase("AppDB", MODE_PRIVATE, null);

        // Khởi tạo bảng users nếu chưa có
        String createTableQuery = "CREATE TABLE IF NOT EXISTS users (username TEXT, email TEXT, password TEXT)";
        db.execSQL(createTableQuery);

        // Xử lý sự kiện click nút đăng nhập
        loginButton.setOnClickListener(v -> loginUser());

        // Xử lý sự kiện click nút đăng ký (chuyển sang RegisterActivity)
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, register.class);
            startActivity(intent);
            finish();  // Kết thúc LoginActivity để không quay lại trang đăng nhập khi nhấn nút back
        });
    }

    // Phương thức đăng nhập người dùng
    private void loginUser() {

//        // Đoạn mã trong hàm hoặc nơi cần chạy câu lệnh DROP TABLE
//        try {
//            // Câu lệnh SQL để xóa bảng nếu nó tồn tại
//            db.execSQL("Drop TABLE IF  EXISTS Cart ");
//
//            Toast.makeText(this, "Bảng Products đã được xóa thành công.", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            // Nếu có lỗi xảy ra, in ra lỗi và thông báo cho người dùng
//            e.printStackTrace();
//            Toast.makeText(this, "Có lỗi khi xóa bảng Products.", Toast.LENGTH_SHORT).show();
//        }
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Kiểm tra nếu các trường không trống
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Vui lòng nhập tên đăng nhập và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra tên đăng nhập và mật khẩu trong cơ sở dữ liệu
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});

        // Nếu tìm thấy người dùng, chuyển sang MainActivity
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();  // Kết thúc LoginActivity để không quay lại trang đăng nhập khi nhấn nút back
        } else {
            // Nếu không tìm thấy, thông báo lỗi
            Toast.makeText(Login.this, "Tên đăng nhập hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
        }
    }

}
