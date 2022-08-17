package com.example.compose_download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;


public class DVAdapter extends RecyclerView.Adapter<DVAdapter.ViewHolder> {

    private ArrayList<Model> models;
    private Context context;

    public DVAdapter(ArrayList<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.description.setText(models.get(holder.getAdapterPosition()).getDescription());
        holder.url_text.setText(models.get(holder.getAdapterPosition()).getUrl());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(view.getContext(),  SimpleActivity.class);
               // view.getContext().startActivity(intent);

                if (holder.description.getText().equals("Powell")){
                    Toast.makeText(context, "Yes Powell Exist", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(view.getContext(),  SimpleActivity.class);
                    // view.getContext().startActivity(intent);


                }
            }
        });




        File extStore = Environment.getExternalStorageDirectory();
        File myFile = new File(extStore.getAbsolutePath() + "/First_Downloads/old/" + holder.description.getText());

        if (myFile.exists() ){
            holder.check_image.setImageResource(R.drawable.ic_check_box);
            holder.name.setText("Downloaded");

        }else {
            //holder.check_image.setImageResource(R.drawable.ic_download);
        }



        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               DownLoad_BottomSheet downLoad_bottomSheet = new DownLoad_BottomSheet();
                downLoad_bottomSheet.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), downLoad_bottomSheet.getTag());

               // downLoad_bottomSheet.setCancelable(false);

                Intent intent1 = new Intent("custom_msg");
              //  intent1.putExtra("name", models.get(position).getName());
                // becuse the nsm is constant , we pas description instead
                intent1.putExtra("name", models.get(position).getDescription());
                intent1.putExtra("key", models.get(position).getDescription());
                intent1.putExtra("url", models.get(position).getUrl());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

            }
        });




    }

    @Override
    public int getItemCount() {
        if (models!=null){
            return models.size();

        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, description, url_text, number;
        private ImageView imageView, option, check_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description_item);
            url_text = itemView.findViewById(R.id.url_txt_item);
            check_image = itemView.findViewById(R.id.check_image);
            option = itemView.findViewById(R.id.option_item);

        }
    }
}
