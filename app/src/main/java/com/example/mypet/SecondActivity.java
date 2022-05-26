package com.example.mypet;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class SecondActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment= new HomeFragment();
    OthersFragment othersFragment = new OthersFragment();
    PetsFragment petsFragment = new PetsFragment();
    EditText a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        a= findViewById(R.id.edtNombre);
        bottomNavigationView = findViewById(R.id.bottom_navitagion);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menuHome:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                        case R.id.menuPets:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,petsFragment).commit();
                            return true;
                                case R.id.menuOthers:
                                    Toast.makeText(SecondActivity.this,"Proximamente",Toast.LENGTH_LONG).show();
                                //getSupportFragmentManager().beginTransaction().replace(R.id.container,othersFragment).commit();
                               // return true;
                }
                return false;
            }
        });



    }

}