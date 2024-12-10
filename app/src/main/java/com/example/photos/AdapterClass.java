package com.example.photos;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.Myimagesholder>
{
    List<Images> imagesList =new ArrayList<>();
    private OnImageClickListner listner;

    public void setListner(OnImageClickListner listner) {
        this.listner = listner;
    }

    public void setImagesList(List<Images> imagesList)
    {
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }
    public Images getPosition(int positon)
    {

        return imagesList.get(positon);

    }
    public interface  OnImageClickListner
    {
        void onImageClick(Images images);
    }



    @NonNull
    @Override
    public Myimagesholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_design,parent,false);

        return new Myimagesholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myimagesholder holder, int position)
    {
        Images images=imagesList.get(position);
        holder.title.setText(images.getImage_title());
        holder.description.setText(images.getImage_description());
        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(images.getImage(),0,images.getImage().length));


    }

    @Override
    public int getItemCount()
    {
        return imagesList.size();
    }

    public class Myimagesholder extends RecyclerView.ViewHolder
    {
        ImageView image;
       TextView title,description;
        public Myimagesholder(@NonNull View itemView)
        {
            super(itemView);
            image=itemView.findViewById(R.id.imageView);
            title=itemView.findViewById(R.id.textViewTitleRV);
            description=itemView.findViewById(R.id.textViewDescriptionRV);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    int position=getAdapterPosition();
                    if(listner!=null && position != RecyclerView.NO_POSITION)
                    {
                        listner.onImageClick(imagesList.get(position));

                    }

                }
            });

        }
    }

}
