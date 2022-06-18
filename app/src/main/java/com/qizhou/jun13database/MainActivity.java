package com.qizhou.jun13database;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String BD_NAME = "Restaurant Database";
    private static final String TABLE_NAME = "restaurant";
    private ImageView imageView;
    private TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.main_image);
        nameView = findViewById(R.id.tv_name);

        DatabaseHelper databaseHelper = new DatabaseHelper(this, BD_NAME, null, 1);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want submit")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (query.equalsIgnoreCase("diplomate")) {
                                    ContentValues restaurant = new ContentValues();
                                    restaurant.put("name", "Le Diplomate");
                                    restaurant.put("rating", 4.5);
                                    restaurant.put("price", "$$");
                                    restaurant.put("image", "https://s3-media4.fl.yelpcdn.com/bphoto/Uu59wlwXORjfmYekCiM16Q/o.jpg");
                                    databaseHelper.getWritableDatabase().insert(TABLE_NAME, null, restaurant);
                                } else if (query.equalsIgnoreCase("aa")) {
                                    ContentValues restaurant = new ContentValues();
                                    restaurant.put("name", "Schwartz's");
                                    restaurant.put("rating", 3.5);
                                    restaurant.put("price", "$$$$");
                                    restaurant.put("image", "https://s3-media4.fl.yelpcdn.com/bphoto/x2LT3Xe670JCXJBxkuIBPg/o.jpg");
                                    databaseHelper.getWritableDatabase().insert(TABLE_NAME, null, restaurant);
                                } else {
                                    Cursor cursor = databaseHelper.getReadableDatabase() // select * from table
                                            .query(TABLE_NAME, new String[]{"name", "image", "rating"},
                                                    null, null, null, null, null, null);

                                    Cursor cursor1 = databaseHelper.getReadableDatabase()
                                            .rawQuery("select * from " + BD_NAME, null);

                                    ArrayList<String> names = new ArrayList<>();
                                    if (cursor.moveToFirst()) {
                                        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                                        String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                                        double rating = cursor.getDouble(2);
//                                        names.add(name);
                                        names.add(image);
                                        while (cursor.moveToNext()) {
//                                            names.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                                            names.add(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                                        }
//                                        nameView.setText(name);
//                                        Glide.with(MainActivity.this).load(image).into(imageView);

                                        ListFragment listFragment = new ListFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putStringArrayList("key", names);
                                        listFragment.setArguments(bundle);
                                        getSupportFragmentManager().beginTransaction().replace(R.id.container1, listFragment).commit();

                                    }

                                }


                                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want submit")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();
                return false;
            }
        });
    }
}