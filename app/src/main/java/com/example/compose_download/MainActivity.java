package com.example.compose_download;


import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private String url1 = "https://png.pngtree.com/png-vector/20190927/ourmid/pngtree-lovely-bat-clipart-vector-png-element-png-image_1749074.jpg";
   private String url2 = "https://firebasestorage.googleapis.com/v0/b/lazy-loading-fa843.appspot.com/o/What%20is%20Physics_.mp4?alt=media&token=ba2cca42-4d6d-4cd9-9e7e-d017995c2ece";

   private RecyclerView recyclerView;

   public static String name;
   public static String key;
   public static String url_per;




    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // for storage runtime pwrmsion
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

            }
        }


        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessagereciever, new IntentFilter("custom_msg"));


        ArrayList<Model> models = new ArrayList<>();

        models.add(new Model(url1,"solving the wave equation of light and energy per time in real space of force filed, getting used to power derivstion system in life " ));
        models.add(new Model(url1,"How the life of humans chages with time, fight the ukranie war and cliomates changes affect food supply in the global scale of the earth system " ));
        models.add(new Model(url1,"Ligrt, souind and eneger of mater and states of the ststyem of places" ));
        models.add(new Model(url1,"Ligrt, souind and eneger of mater and states of the ststyem of places" ));
        models.add(new Model(url2,"suply everthig ro evrything in life" ));
        models.add(new Model(url2,"Powell" ));



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        DVAdapter adapter = new DVAdapter(models, getApplicationContext());
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        adapter.notifyItemChanged(getChangingConfigurations());
        adapter.notifyItemChanged(adapter.getItemCount());


    }



    public BroadcastReceiver mMessagereciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            name = intent.getStringExtra("name");
            key = intent.getStringExtra("key");
            url_per = intent.getStringExtra("url");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);

    }

    @Override
    protected void onStop() {
        super.onStop();


    }
}