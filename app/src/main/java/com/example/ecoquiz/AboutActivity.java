package com.example.ecoquiz;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity implements AboutFragment.BroListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        AboutFragment aboutFrag = new AboutFragment();
        aboutFrag.setBroListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.aboutFragmentContainer, aboutFrag).commit();
    }

    @Override
    public void onLongClick() {
        AboutMenu secretFrag = new AboutMenu();
    getSupportFragmentManager().beginTransaction().replace(R.id.aboutFragmentContainer, secretFrag).addToBackStack(null).commit();
    }
}
