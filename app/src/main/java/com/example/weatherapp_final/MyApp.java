package com.example.weatherapp_final;

import android.app.Application;

public class MyApp extends Application {

    NetworkingManager networkingManager = new NetworkingManager();
    DatabaseManager databaseManager = new DatabaseManager();
    MultithreadingManager multithreadingManager = new MultithreadingManager();

}
