package com.example.ecoquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class SubmitActivity extends AppCompatActivity implements View.OnClickListener{

    private ConstraintLayout clSubmit;
    private Button btSelectImage;
    private Button btSend;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        clSubmit = findViewById(R.id.clSubmit);
        btSelectImage = findViewById(R.id.btSelectImage);
        btSend = findViewById(R.id.btSend);
        btSelectImage.setOnClickListener(this);
        btSend.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Commons.PICK_IMAGE && resultCode==RESULT_OK) {
            imageUri = data.getData();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
          /*  case R.id.btSelectImage: {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Commons.PICK_IMAGE);
                break;
            }*/
            case R.id.btSend: {
                String emailText = "New question to add!\n\n";
                for(int i = 1; i< clSubmit.getChildCount();i++) {
                    View view = clSubmit.getChildAt(i);
                    if (view instanceof TextView | view instanceof EditText) {
                        if(view instanceof EditText)emailText+=": ";
                        emailText+=((TextView) view).getText().toString();
                        if(view instanceof EditText)emailText+="\n";
                    }
                }

                // TODO: 08.04.20 Replace this crap with DB upload
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                //intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "new question");
                intent.putExtra(Intent.EXTRA_TEXT, emailText);
                if(imageUri!=null) {
                    intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                }
                startActivity(intent);
                break;
            }
        }
    }
}
