package com.example.tourer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.Dialogs.AddTransactionDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView transactionRecView;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private TextView amount, welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setSupportActionBar(toolbar);
        initBottomNavView();
        clickables();

    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavView);
        //  amount = findViewById(R.id.txtAmount);
        //  welcome = findViewById(R.id.txtWelcome);
        // transactionRecView = findViewById(R.id.transactionRecView);
        floatingActionButton = findViewById(R.id.fbAddTransaction);
        toolbar = findViewById(R.id.toolbar);
    }

    private void clickables() {
        // welcome.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fbAddTransaction:
                AddTransactionDialog addTransactionDialog = new AddTransactionDialog();
                addTransactionDialog.show(getSupportFragmentManager(), "add tourist item");
                break;
            default:
        }
    }

    private void initBottomNavView() {
        bottomNavigationView.setSelectedItemId(R.id.item_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_home:
                        break;
                    case R.id.item_bookings:
                        break;
                    case R.id.item_accomodation:
                        break;
                    case R.id.item_activity:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tayebwa:
                break;
            case R.id.menu_tayebwa1:
                break;
            case R.id.menu_logout:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}