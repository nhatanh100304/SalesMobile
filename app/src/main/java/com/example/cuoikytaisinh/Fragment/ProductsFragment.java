package com.example.cuoikytaisinh.Fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cuoikytaisinh.R;

import java.io.IOException;
import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    private EditText productNameEditText, productDescriptionEditText, productPriceEditText, productImageEditText, quantityEditText;
    private Button addProductButton, updateProductButton, deleteProductButton;
    private ListView productListView;
    private SQLiteDatabase db;
    private ArrayList<String> productList;
    private ArrayAdapter<String> adapter;
    private int selectedProductId = -1; // Track the selected product
    private ImageView productImageView; // ImageView to display the image

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        // Initialize the views
        productNameEditText = view.findViewById(R.id.productNameEditText);
        productDescriptionEditText = view.findViewById(R.id.productDescriptionEditText);
        productPriceEditText = view.findViewById(R.id.productPriceEditText);
        productImageEditText = view.findViewById(R.id.productImageEditText);
        quantityEditText = view.findViewById(R.id.quantityEditText);
        addProductButton = view.findViewById(R.id.addProductButton);
        updateProductButton = view.findViewById(R.id.updateProductButton);
        deleteProductButton = view.findViewById(R.id.deleteProductButton);
        productListView = view.findViewById(R.id.productListView);
        productImageView = view.findViewById(R.id.productImageView); // ImageView to display the image


        // Open or create the database
        db = getActivity().openOrCreateDatabase("AppDB", getContext().MODE_PRIVATE, null);

        // Create table if it doesn't exist, ensuring quantity is treated as INTEGER
        db.execSQL("CREATE TABLE IF NOT EXISTS Products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "description TEXT,"
                + "price REAL," // Ensure price is REAL
                + "quantity INTEGER," // Ensure quantity is INTEGER
                + "image TEXT);");

        // Initialize product list and adapter
        productList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);

        // Load products from the database
        loadProductsFromDatabase();

        // Handle product selection from the list
        productListView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedProduct = productList.get(position);
            String[] productDetails = selectedProduct.split(" - ");
            selectedProductId = Integer.parseInt(productDetails[0]);

            // Update input fields with selected product details
            productNameEditText.setText(productDetails[1]);
            productDescriptionEditText.setText(productDetails[2]);
            productPriceEditText.setText(productDetails[3]);
            quantityEditText.setText(productDetails[4]);
            productImageEditText.setText(productDetails[5]);

            // Load image if available
            String imageFileName = productDetails[5];
            loadImageFromAssets(imageFileName);
        });

        // Add new product
        addProductButton.setOnClickListener(v -> {
            String name = productNameEditText.getText().toString();
            String description = productDescriptionEditText.getText().toString();
            String priceStr = productPriceEditText.getText().toString();
            String quantityStr = quantityEditText.getText().toString();
            String image = productImageEditText.getText().toString();

            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty() || image.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // Parse price and quantity
                    float price = Float.parseFloat(priceStr);
                    int quantity = Integer.parseInt(quantityStr);
                    addProductToDatabase(name, description, price, quantity, image);
                    loadProductsFromDatabase(); // Reload product list
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Vui lòng nhập giá và số lượng hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Update existing product
        updateProductButton.setOnClickListener(v -> {
            if (selectedProductId != -1) {
                String name = productNameEditText.getText().toString();
                String description = productDescriptionEditText.getText().toString();
                String priceStr = productPriceEditText.getText().toString();
                String quantityStr = quantityEditText.getText().toString();
                String image = productImageEditText.getText().toString();

                if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty() || image.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // Parse price and quantity
                        float price = Float.parseFloat(priceStr);
                        int quantity = Integer.parseInt(quantityStr);
                        updateProductInDatabase(name, description, price, quantity, image);
                        loadProductsFromDatabase(); // Reload product list
                        selectedProductId = -1; // Reset selected product
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Vui lòng nhập giá và số lượng hợp lệ!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn sản phẩm cần sửa", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete selected product
        deleteProductButton.setOnClickListener(v -> {
            if (selectedProductId != -1) {
                deleteProductFromDatabase();
                loadProductsFromDatabase(); // Reload product list
                selectedProductId = -1; // Reset selected product
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn sản phẩm cần xóa", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Add new product to database
    private void addProductToDatabase(String name, String description, float price, int quantity, String image) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("price", price); // Use float for price
        contentValues.put("quantity", quantity); // Use int for quantity
        contentValues.put("image", image); // Save image name

        long result = db.insert("Products", null, contentValues);
        if (result != -1) {
            Toast.makeText(getContext(), "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }

    // Update product in the database
    private void updateProductInDatabase(String name, String description, float price, int quantity, String image) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("price", price); // Use float for price
        contentValues.put("quantity", quantity); // Use int for quantity
        contentValues.put("image", image);

        db.update("Products", contentValues, "id = ?", new String[]{String.valueOf(selectedProductId)});
    }

    // Delete selected product from database
    private void deleteProductFromDatabase() {
        db.delete("Products", "id = ?", new String[]{String.valueOf(selectedProductId)});
        Toast.makeText(getContext(), "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
    }

    // Load products from database
    private void loadProductsFromDatabase() {
        productList.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM Products", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            float price = cursor.getFloat(cursor.getColumnIndexOrThrow("price"));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
            productList.add(id + " - " + name + " - " + description + " - " + price + " - " + quantity + " - " + image);
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    // Load image from assets
    private void loadImageFromAssets(String imageFileName) {
        try {
            // Get the image from assets and display it
            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getAssets().open(imageFileName));
            productImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            productImageView.setImageResource(android.R.color.transparent); // In case of an error
        }
    }
}
