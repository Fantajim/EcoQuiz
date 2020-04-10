package com.example.ecoquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutMenu extends Fragment implements Switch.OnCheckedChangeListener {

    private Switch swAnnoy;
    private Switch swBootcamp;
    private Switch swBonfire;
    private Switch swNuclear;
    private LinearLayout llSecret;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AboutMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_secret, container, false);
        swAnnoy = view.findViewById(R.id.swAnnoy);
        swBootcamp = view.findViewById(R.id.swBootcamp);
        swBonfire = view.findViewById(R.id.swBonfire);
        swNuclear = view.findViewById(R.id.swNuclear);
        llSecret = view.findViewById(R.id.llSecret);
        sharedPreferences = getActivity().getSharedPreferences(Commons.SHAREDPREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(Commons.MOD)){
           String temp = sharedPreferences.getString(Commons.MOD, "");
            if (temp.equals(Commons.ANNOY)) swAnnoy.setChecked(true);
            if (temp.equals(Commons.BONFIRE)) swBonfire.setChecked(true);
            if (temp.equals(Commons.BOOTCAMP)) swBootcamp.setChecked(true);
            if (temp.equals(Commons.NUCLEAR)) swNuclear.setChecked(true);

        }


        for(int i = 0;i < llSecret.getChildCount(); i++) {
            Switch sw = (Switch)llSecret.getChildAt(i);
            sw.setOnCheckedChangeListener(this);
        }


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int counter = 0;
        for(int i = 0; i < llSecret.getChildCount(); i++) {
            Switch sw = (Switch) llSecret.getChildAt(i);
            if(sw.getId() != buttonView.getId() && !isChecked)counter++;
            if (sw.getId()!=buttonView.getId() && isChecked ) {
                ((Switch) llSecret.getChildAt(i)).setChecked(false);
                editor = sharedPreferences.edit();
            }
            else {
                switch(buttonView.getId()) {
                    case R.id.swAnnoy: {
                        editor = sharedPreferences.edit();
                        editor.putString(Commons.MOD, Commons.ANNOY).apply();
                        break;
                    }
                    case R.id.swBootcamp: {
                        editor = sharedPreferences.edit();
                        editor.putString(Commons.MOD, Commons.BOOTCAMP).apply();
                        break;
                    }
                    case R.id.swBonfire: {
                        editor = sharedPreferences.edit();
                        editor.putString(Commons.MOD, Commons.BONFIRE).apply();
                        break;
                    }
                    case R.id.swNuclear: {
                        editor = sharedPreferences.edit();
                        editor.putString(Commons.MOD, Commons.NUCLEAR).apply();
                        break;
                    }
                }
            }
            if(counter == llSecret.getChildCount()-1) {
                editor = sharedPreferences.edit();
                editor.remove(Commons.MOD).apply();
                MainActivity.getMediaPlayer().stop();
            }
        }
    }
}
