package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TableLayout;

import com.example.foodapp.Module.DetailRecipe;
import com.example.foodapp.Module.SearchEntry;
import com.example.foodapp.Presentor.InstructionAdapter;
import com.example.foodapp.Presentor.MyCustomAdaptor;
import com.example.foodapp.Presentor.SearchPresentor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    NonScrollListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String[] instruction = {"dfojfewof", "fjsiofjioewjeow","sfhewifheuiwhf"};
//        InstructionAdapter instructionAdapter = new InstructionAdapter(this, instruction);
//        listView = findViewById(R.id.istructionList);
//        listView.setAdapter(instructionAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this,  R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }



}
