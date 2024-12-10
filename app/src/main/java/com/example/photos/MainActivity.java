package com.example.photos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ModelClass modelClass;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ActivityResultLauncher<Intent> activityResultLauncherForAddImage;

    private ActivityResultLauncher<Intent>activityResultLauncherForUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerActivityForAddImage();
        registerActivityForUpdate();
        recyclerView=findViewById(R.id.rv);
        floatingActionButton=findViewById(R.id.floatingActionButton);



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(layoutManager);
        AdapterClass adapterClass=new AdapterClass();
        recyclerView.setAdapter(adapterClass);


        modelClass=new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ModelClass.class);
        modelClass.getImages().observe(MainActivity.this, new Observer<List<Images>>() {
            @Override
            public void onChanged(List<Images> images)
            {
                adapterClass.setImagesList(images);

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this,AddImage_Activity.class);
                activityResultLauncherForAddImage.launch(intent);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                modelClass.delete(adapterClass.getPosition(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(recyclerView);

        adapterClass.setListner(new AdapterClass.OnImageClickListner() {
            @Override
            public void onImageClick(Images images) {
                Intent intent=new Intent(MainActivity.this, Update.class);
                intent.putExtra("ID",images.getId());
                intent.putExtra("title",images.getImage_title());
                intent.putExtra("description",images.getImage_description());
                intent.putExtra("image",images.getImage());
                activityResultLauncherForUpdate.launch(intent);

            }
        });


    }

    public void registerActivityForUpdate()
    {
        activityResultLauncherForUpdate=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o)
            {
                int resultcode=o.getResultCode();
                Intent data=o.getData();
                if( resultcode == RESULT_OK && data != null);
                {
                    String title=data.getStringExtra("updatedtitle");
                    String description=data.getStringExtra("updateddes");
                    if(title.isEmpty() )
                    {
                        title="";
                        description="";
                    }
                    else if(description.isEmpty())
                    {
                        description="";
                    }
                    byte[] image= data.getByteArrayExtra("image");
                    int id=data.getIntExtra("ID",-1);
                    Images images=new Images(title,description,image);
                    images.setId(id);
                    modelClass.update(images);
                }


            }
        }) ;

    }

    public void registerActivityForAddImage()
    {
        activityResultLauncherForAddImage=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o)
            {

                int resultcode=o.getResultCode();
                Intent data=o.getData();
                if(resultcode==RESULT_OK && data!=null)
                {
                    String title=data.getStringExtra("title");
                    String des=data.getStringExtra("des");
                    if(title.isEmpty())
                    {
                        title="";

                    }
                    else if(des.isEmpty())
                    {
                        des="";
                    }
                    byte[]imageinput=data.getByteArrayExtra("img");
                    Images images=new Images(title,des,imageinput);
                    modelClass.insert(images);

                }

            }
        });
    }

}