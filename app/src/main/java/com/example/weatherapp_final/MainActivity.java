package com.example.weatherapp_final;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.weatherapp_final.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
       CitiesRecyclerViewAdapter.CitiesClickListener,
        NetworkingManager.NetworkingCallBackInterface
{
    ArrayList<City> cities = new ArrayList<>(0);
    RecyclerView rv;
    CitiesRecyclerViewAdapter adapter;
    NetworkingManager networkingManager;
    DatabaseManager databaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.citiesList);
        adapter = new CitiesRecyclerViewAdapter(cities,this);
        adapter.listener = this;
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        networkingManager = ((MyApp)getApplication()).networkingManager;
        databaseManager = ((MyApp)getApplication()).databaseManager;
        DatabaseManager.getDB(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_menu,menu);
        MenuItem searchViewmenue = menu.findItem(R.id.searchbar);
        SearchView searchView = (SearchView) searchViewmenue.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("weather app",  "Query submit " + query);
                /// Search for the city in the networking class
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("weather app", "Query changes " + newText);
                if (newText.length() >= 3){
                    // I need to call my networking function to get all cities
                    networkingManager.getCities(newText);

                }
                else {
                    adapter.list = new ArrayList<>(0);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });


        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        networkingManager.listener = this;
    }

    @Override
    public void onCityClicked(City selectedCity) {

        databaseManager.insertNewCity(selectedCity);
        finish();

    }


    @Override
    public void networkingManagerCompleteWithJsonString(String jsonString) {
        // parse json string
        adapter.list = JsonManager.fromJsonStringToList(jsonString);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void newtworkingMangerCompleteWithWeatherIcon(Bitmap image) {

    }
}