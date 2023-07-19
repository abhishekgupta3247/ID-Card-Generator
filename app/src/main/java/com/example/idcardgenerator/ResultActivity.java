package com.example.idcardgenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle bundle = getIntent().getBundleExtra("Main");

        //Extract the data…
        String file = bundle.getString("file");
        String name = bundle.getString("name");
        String id = bundle.getString("id");
        String branch = bundle.getString("branch");
        String father = bundle.getString("father");
        String cn = bundle.getString("cn");
        String add = bundle.getString("adrs");

        Bitmap bmp = null;
        try {
            FileInputStream inputStream = this.openFileInput(file);
            bmp = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView nameTextview = findViewById(R.id.nameText);
        ImageView imageView =(ImageView) findViewById(R.id.studImage);
        TextView idTextview= findViewById(R.id.idText);
        TextView branchTextview= findViewById(R.id.branchText);
        TextView fatherTextview= findViewById(R.id.fatherText);
        TextView contactTextview= findViewById(R.id.contactText);
        TextView addTextview= findViewById(R.id.adrsText);
        Button shareBtn= (Button) findViewById(R.id.shareBtn);
        CardView cardView=(CardView) findViewById(R.id.cardView);

        nameTextview.setText(name);
        imageView.setImageBitmap(bmp);
        idTextview.setText("ID : "+id);
        branchTextview.setText("Branch : "+branch);
        fatherTextview.setText("Father's Name : "+father);
        contactTextview.setText("Contact No: "+cn);
        addTextview.setText("Address : "+add);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap screenshot = ViewShot(cardView);
                Intent intent =new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                screenshot.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(),screenshot, "Title", null);
                Uri imageUri =  Uri.parse(path);
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(intent, "Select"));
            }
        });
    }
    public Bitmap ViewShot(View v) {
        int height = v.getHeight();
        int width = v.getWidth();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;


    }
}