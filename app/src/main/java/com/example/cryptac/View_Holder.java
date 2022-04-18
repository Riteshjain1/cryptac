package com.example.cryptac;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class View_Holder extends RecyclerView.ViewHolder {

    TextView name;
    ImageView imageView;

    View_Holder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        imageView = (ImageView) itemView.findViewById(R.id.image);
    }
}
