package com.example.idcardgenerator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Bitmap image = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView pic = findViewById(R.id.pic);

        if (resultCode == RESULT_OK && requestCode == 122) {
            Uri imageUri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = this.getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            image = selectedImage;
            pic.setImageBitmap(selectedImage);

        } else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();


        EditText edname = findViewById(R.id.name);
        EditText edid = findViewById(R.id.id);
        EditText edfather = findViewById(R.id.father);
        EditText edcon = findViewById(R.id.con);
        EditText edbranch =findViewById(R.id.branch);
        EditText edadrs = findViewById(R.id.adrs);

        Button generate = findViewById(R.id.generate);
        Button upload = findViewById(R.id.upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 122);
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edname.getText().toString();
                String id = edid.getText().toString();
                String father = edfather.getText().toString();
                String branch = edbranch.getText().toString();
                String cn = edcon.getText().toString();
                String adrs = edadrs.getText().toString();


                if (name.isEmpty() || id.isEmpty() || father.isEmpty() || cn.isEmpty() ||
                        adrs.isEmpty() || image == null) {
                    Toast.makeText(MainActivity.this, "Fill all the fields", Toast.LENGTH_LONG).show();
                } else {

                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("id", id);
                    bundle.putString("branch",branch);
                    bundle.putString("father", father);
                    bundle.putString("cn", cn);
                    bundle.putString("adrs", adrs);

                    String filename = "bitmap.png";

                    try {
                        FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        //Cleanup
                        stream.close();
                        image.recycle();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    bundle.putString("file",filename);


                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("Main", bundle);
                    startActivity(intent);
                }


            }
        });

    }
}