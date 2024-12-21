package com.example.cuoikytaisinh;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class register  extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button registerButton;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);  // Đảm bảo bạn đã tạo layout activity_register.xml

        // Khởi tạo các trường nhập liệu và nút
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        // Mở cơ sở dữ liệu SQLite
        db = openOrCreateDatabase("AppDB", MODE_PRIVATE, null);

        // Tạo bảng users nếu chưa có
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)";
        db.execSQL(createUsersTable);

        // Xử lý sự kiện khi người dùng bấm nút đăng ký
        registerButton.setOnClickListener(v -> registerUser());
    }

    // Phương thức đăng ký người dùng
    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Kiểm tra nếu các trường không trống
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(register .this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra nếu người dùng đã tồn tại
        if (isUserExists(username)) {
            Toast.makeText(register .this, "Tên người dùng đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu thông tin người dùng vào cơ sở dữ liệu
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);

        if (result != -1) {
            // Đăng ký thành công
            Toast.makeText(register .this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            // Chuyển sang màn hình đăng nhập
            Intent intent = new Intent(register.this, Login.class);
            startActivity(intent);

            finish();  // Đóng màn hình đăng ký và quay lại màn hình trước
        } else {
            // Đăng ký thất bại
            Toast.makeText(register .this, "Đăng ký thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }

    // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu chưa
    private boolean isUserExists(String username) {
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
