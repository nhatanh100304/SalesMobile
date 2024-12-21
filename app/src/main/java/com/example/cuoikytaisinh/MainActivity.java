package com.example.cuoikytaisinh;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cuoikytaisinh.Fragment.AccountFragment;
import com.example.cuoikytaisinh.Fragment.BillFragment;
import com.example.cuoikytaisinh.Fragment.CartFragment;
import com.example.cuoikytaisinh.Fragment.HomeFragment;
import com.example.cuoikytaisinh.Fragment.ProductsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    Toolbar toolbar;  // Khai báo toolbar
    DrawerLayout drawerLayout;
    NavigationView navigationView; // Khai báo NavigationView cho Drawer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        // Khởi tạo Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  // Đặt Toolbar làm ActionBar

        // Khởi tạo BottomNavigationView
        navView = findViewById(R.id.nav_view);

        // Khởi tạo NavigationView để xử lý các mục trong DrawerLayout
        navigationView = findViewById(R.id.nav_view_drawer);

        // Thiết lập sự kiện cho BottomNavigationView
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Thiết lập sự kiện cho NavigationView (Menu Drawer)
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Sử dụng if-else thay cho switch-case để xử lý các lựa chọn từ Drawer
                if (menuItem.getItemId() == R.id.nav_account) {

                    loadFragment(new AccountFragment());  // Thay thế bằng fragment phù hợp
                }   // Thay thế bằng fragment phù hợp
                 else if (menuItem.getItemId() == R.id.nav_logout) {

                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);

                    finish();  // Thay thế bằng fragment phù hợp
                } else {
                    return false;
                }
                drawerLayout.closeDrawer(GravityCompat.START);  // Đóng Drawer sau khi chọn mục
                return true;
            }
        });

        // Mặc định khi vừa vào ứng dụng sẽ hiển thị HomeFragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());  // Hiển thị HomeFragment đầu tiên
        }

        // Đảm bảo set item mặc định
        navView.setSelectedItemId(R.id.home);  // Đảm bảo ID trùng với mục trong menu.xml
    }

    // Listener khi người dùng chọn item trong BottomNavigationView
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            // Sử dụng if-else để xử lý các item được chọn trong BottomNavigationView
            if (item.getItemId() == R.id.home) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Trang Chủ");  // Thiết lập title cho ActionBar
                }
                fragment = new HomeFragment();
            } else if (item.getItemId() == R.id.dashboard) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Thêm Sản Phẩm");
                }
                fragment = new ProductsFragment();
            } else if (item.getItemId() == R.id.cart) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Giỏ");
                }
                fragment = new CartFragment();
            }else if (item.getItemId() == R.id.bill) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Bill");
                }
                fragment = new BillFragment();
            }

            // Nếu fragment không null, thay thế fragment hiện tại
            if (fragment != null) {
                loadFragment(fragment);
            }
            return true;  // Trả về true để xác nhận item đã được chọn
        }
    };

    // Phương thức để load fragment vào container
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);  // Thay thế fragment trong FrameLayout
        transaction.addToBackStack(null);  // Optional, nếu bạn muốn quản lý stack của fragment
        transaction.commit();
    }
}
