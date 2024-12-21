package com.example.cuoikytaisinh.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class HomeFragment extends Fragment {

    private ListView listView;
    private ArrayList<Furniture> arrayList;
    private FurnitureAdapter furnitureAdapter;
    private SQLiteDatabase db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        listView = view.findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        furnitureAdapter = new FurnitureAdapter(getContext(), arrayList);
        listView.setAdapter(furnitureAdapter);


        // Open the database and load products
        db = getActivity().openOrCreateDatabase("AppDB", getContext().MODE_PRIVATE, null);
        loadProductsFromDatabase();

        // Set item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Add clicked item to history and show a toast
                Utils.getFurnitureHistory().add(arrayList.get(position));
                Toast.makeText(getContext(), "Sản phẩm đã thêm vào giỏ: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProductsFromDatabase() {
        // Clear the existing data
        arrayList.clear();

        // Query the Products table to get all columns
        Cursor cursor = db.query("Products", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // Get data from the cursor for each column
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                float price = cursor.getFloat(cursor.getColumnIndexOrThrow("price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image")); // Get image name

                // Create a new Furniture object with all the data
                Furniture furniture = new Furniture(id, name, description,quantity,price, image);

                // Add the furniture object to the list
                arrayList.add(furniture);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Notify the adapter to refresh the list
        furnitureAdapter.notifyDataSetChanged();
    }
}
