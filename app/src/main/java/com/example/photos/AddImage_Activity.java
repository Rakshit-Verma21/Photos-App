package com.example.photos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddImage_Activity extends AppCompatActivity {
    private ImageView image;
    private Button save;
    private EditText title, description;

    private Bitmap selectedimage;
    private  Bitmap scaledimage;

    ActivityResultLauncher<Intent> activityResultLauncherForImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        getSupportActionBar().setTitle("ADD Image");
        registerActivityForSelectImage();
        save = findViewById(R.id.buttonAdd);
        image = findViewById(R.id.imageViewAdd);
        title = findViewById(R.id.editTextAddTitle);
        description = findViewById(R.id.editTextAddDescription);
        image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void onClick(View v) {
                String permission;
                if (Build.VERSION.SDK_INT >= 32) {
                    permission = Manifest.permission.READ_MEDIA_IMAGES;
                } else {
                    permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                }

                if (ContextCompat.checkSelfPermission(AddImage_Activity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddImage_Activity.this, new String[]{permission}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncherForImage.launch(intent);

                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedimage == null)
                {
                    Toast.makeText(AddImage_Activity.this,"No Image Selected",Toast.LENGTH_LONG).show();
                }
                String titlesave = title.getText().toString();
                String des = description.getText().toString();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                scaledimage = compress(selectedimage,300);
                selectedimage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                byte[] image=outputStream.toByteArray();


                Intent intent=new Intent();
                intent.putExtra("img",image);
                intent.putExtra("title",titlesave);
                intent.putExtra("des",des);
                setResult(RESULT_OK,intent);
                finish();




            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherForImage.launch(intent);

        }

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




}