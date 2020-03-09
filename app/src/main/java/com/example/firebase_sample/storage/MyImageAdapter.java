package com.example.firebase_sample.storage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebase_sample.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.MyImageViewHolder> {
    private final ArrayList<String> valueList;
    private Context context;

    public MyImageAdapter(ArrayList<String> valueList) {
        this.valueList=valueList;
    }

    @NonNull
    @Override
    public MyImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_row_item,parent,false);
        return new MyImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyImageViewHolder holder, int position) {
        String value=valueList.get(position);
        holder.tvName.setText(value);

        FirebaseStorage mStorage=FirebaseStorage.getInstance();
        StorageReference mStorageRef = mStorage.getReference("images/" + value);
        Glide.with(context /* context */)
                .load(mStorageRef)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return valueList.size();
    }

    public class MyImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName;
        public MyImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            tvName=itemView.findViewById(R.id.tvName);
        }
    }
}
