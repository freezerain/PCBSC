package com.freeze.pcbsc;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import com.freeze.pcbsc.asyncTasks.GenerateTableAsyncTask;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public NavController navController;
    public NavigationView navigationView;
    public AppBarConfiguration appBarConfiguration;
    public ProgressBar progressBar;
    private static final String TAG = "MainActivity.java";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        setupNavigation();


    }

    // Setting Up One Time Navigation
    private void setupNavigation() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerLayout = findViewById(R.id.drawer_layout);
        Set<Integer> topLevels = new HashSet<>();
        topLevels.add(R.id.landing);
        topLevels.add(R.id.testFragment);
        topLevels.add(R.id.buildsFragment);
        appBarConfiguration = new AppBarConfiguration.
                Builder(topLevels).setDrawerLayout(drawerLayout).build();
        navigationView = findViewById(R.id.navigationView);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        navController.navigate(R.id.buildsFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(navController.getCurrentDestination().getId(), true).build();
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.testFragment:
                navController.navigate(R.id.testFragment, new Bundle(), navOptions);
                Log.d("MainActivity", "Navigation item selected: Test fragment");
                break;
            case R.id.buildsFragment:
                Log.d("MainActivity", "Navigation item selected: builds Fragment");
                navController.navigate(R.id.buildsFragment, new Bundle(), navOptions);
                break;
        }
        return true;
    }

    public void getData(){
        Log.d(TAG, "getData() called");
        TableLayout tb = (TableLayout)findViewById(R.id.table);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        GenerateTableAsyncTask generateTableAsyncTask = new GenerateTableAsyncTask(this, progressBar);
        generateTableAsyncTask.execute();
    }

}