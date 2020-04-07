package com.example.ecoquiz;

import android.support.v7.app.AppCompatActivity;
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
        AboutSecret secretFrag = new AboutSecret();
    getSupportFragmentManager().beginTransaction().replace(R.id.aboutFragmentContainer, secretFrag).addToBackStack(null).commit();
    }
}
