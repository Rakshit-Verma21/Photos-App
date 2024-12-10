package com.example.photos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Update extends AppCompatActivity {

    private ImageView image;
    private Button update;
    private EditText title;
    private EditText description;

    private String head,descrip;
    private int id;
    private byte[] img;

    private Bitmap selectedimage;
    private  Bitmap scaledimage;

    ActivityResultLauncher<Intent> activityResultLauncherForImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    getSupportActionBar().setTitle("Update");
        setContentView(R.layout.activity_update);
        registerActivityForSelectImage();
        image=findViewById(R.id.imageViewUpdate);
        update=findViewById(R.id.buttonupdate);
        title=findViewById(R.id.editTextUpdateTitle);
        description=findViewById(R.id.editTextUpdateDescription);
        id=getIntent().getIntExtra("ID",-1);
        head=getIntent().getStringExtra("title");
        descrip=getIntent().getStringExtra("description");
        img=getIntent().getByteArrayExtra("image");

        title.setText(head);
        description.setText(descrip);
        image.setImageBitmap(BitmapFactory.decodeByteArray(img,0, img.length));
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForImage.launch(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateData();


            }
        });
    }
    public void registerActivityForSelectImage() {
        activityResultLauncherForImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                int resultcode = o.getResultCode();
                Intent data = o.getData();
                if (resultcode == RESULT_OK && data != null) {
                    try {
                        selectedimage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        image.setImageBitmap(selectedimage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }
    public Bitmap compress(Bitmap image, int maxsize)
    {
        int width=image.getWidth();
        int height=image.getHeight();
        float ratio=(float)width/(float)height;
        if(ratio>1)
        {
            width=maxsize;
            height=(int)(width/ratio);

        }
        else {
            height=maxsize;
            width=(int)(height*ratio);
        }
        return Bitmap.createScaledBitmap(image ,width,height,true);

    }

    public void updateData()
    {
        if(id==-1)
        {
            Toast.makeText(Update.this,"Error",Toast.LENGTH_LONG).show();
        }
        else
        {
            String updatetitle = title.getText().toString();
            String updatedes = description.getText().toString();

            Intent intent=new Intent();
            intent.putExtra("ID",id);
            intent.putExtra("updatedtitle",updatetitle);
            intent.putExtra("updateddes",updatedes);

            if (selectedimage == null)
            {
                intent.putExtra("image",img);
            }
            else
            {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                scaledimage = compress(selectedimage,300);
                selectedimage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                byte[] image=outputStream.toByteArray();
                intent.putExtra("image",image);
            }



            setResult(RESULT_OK,intent);
            finish();




        }


    }

}