package com.example.compose_download;

import static android.content.Context.MODE_PRIVATE;
import static com.example.compose_download.MainActivity.key;
import static com.example.compose_download.MainActivity.name;
import static com.example.compose_download.MainActivity.url_per;


import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;


public class DownLoad_BottomSheet extends BottomSheetDialogFragment {


    public DownLoad_BottomSheet() {
        // Required empty public constructor
    }

    private Button  btn_offline_center;
    private ImageView  btn_cancel, btn_pause, btn_resume;
    private TextView  btn_download;
    private TextView txt_bytes, txt_progress, text_play;
    private ProgressBar progressBar;
    public final String SHARED_PREFS = "sharedPrefs";
    private long downloadReference = -1L;





    public DownloadManager manager;

    private static final String imageUrl = "https://th..is.jpg";
    private static String imageName = "mito.png";
    private Handler handler;
    private Handler myHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        handler = new Handler(Looper.getMainLooper());
        myHandler = new Handler(Looper.getMainLooper());
        btn_download = view.findViewById(R.id.btn_download_pref);
        btn_offline_center = view.findViewById(R.id.btn_offline_center);
        btn_pause = view.findViewById(R.id.btn_pause);
        btn_resume = view.findViewById(R.id.btn_resume);
        btn_cancel = view.findViewById(R.id.btn_cancell);
        txt_bytes = view.findViewById(R.id.txt_bytes);
        text_play = view.findViewById(R.id.text_play);
        txt_progress = view.findViewById(R.id.txt_progress);
        progressBar = view.findViewById(R.id.progressBar_pref);

        manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        getContext().registerReceiver(onComplete, new IntentFilter((DownloadManager.ACTION_DOWNLOAD_COMPLETE)));





        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDownloadStatus();

            }
        },100);

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage();
                savedata();
                btn_download.setVisibility(View.GONE);
                txt_progress.setVisibility(View.VISIBLE);
                txt_bytes.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.VISIBLE);
                btn_resume.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
            }
        });

        btn_offline_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                long new_download_ref = sharedPreferences.getLong(key, -19L);
               // Toast.makeText(getContext(), new_download_ref+"  ...  " + key, Toast.LENGTH_SHORT).show();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                long new_download_ref = sharedPreferences.getLong(key, -19L);
                SharedPreferences.Editor editor = sharedPreferences.edit();
               // Toast.makeText(getContext(), key + "...." + new_download_ref, Toast.LENGTH_SHORT).show();


                DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(new_download_ref);
                    try {

                        Cursor c = ((DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE)).query(query);
                        if (c.moveToFirst()) {
                            manager.remove(new_download_ref);
                            txt_progress.setText("Dsiplay");
                            txt_bytes.setText("0");
                            progressBar.setProgress(0);
                            dismiss();

                        }

                    } catch (Exception e) {

                        Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();

                    }}
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseDownload();


                Toast.makeText(getContext(), "Please wait for pause", Toast.LENGTH_SHORT).show();
            }
        });

        btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resumeDownload();
                Toast.makeText(getContext(), "Please wait for Resume", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getDownloadStatus();
            handler.postDelayed(runnable, 1000);
            ///Toast.makeText(getContext(), "upDating", Toast.LENGTH_SHORT).show();

        }
    };


    public void downloadImage() {


        manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        File direct = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/First_Downloads/old");
        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url_per));
        request.setTitle(name);
        request.setDescription("Downloading " + name);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir("/First_Downloads/old", name);
        downloadReference = manager.enqueue(request);


    }

private void savedata(){

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(key, downloadReference);
        editor.apply();


    }



    @SuppressLint("SetTextI18n")
    public void getDownloadStatus() {


        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        long new_download_ref = sharedPreferences.getLong(key, -19L);

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(new_download_ref);


        try {
            Cursor c = ((DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE)).query(query);

            if (c.moveToFirst()) {
                @SuppressLint("Range") long bytes_downloaded = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                @SuppressLint("Range") long bytes_total = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                final int dl_progress = (int) ((double) bytes_downloaded / (double) bytes_total * 100f);

                 progressBar.setProgress((int) dl_progress);
               /// txt_bytes.setText(bytes_downloaded + "/" + bytes_total);
                txt_bytes.setText(bytesIntoHumanReadable(Long.parseLong(String.valueOf(bytes_total)))+" / "+bytesIntoHumanReadable(Long.parseLong(String.valueOf(bytes_downloaded))));


            }

        } catch (Exception e) {

            Toast.makeText(getContext(), "cursor failed to move to next", Toast.LENGTH_LONG).show();
        }


        try {

            Cursor c = ((DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE)).query(query);

            if (c == null) {
                txt_progress.setText(statusMessage(c));


            } else {
                c.moveToFirst();
                txt_progress.setText(statusMessage(c));
            }

        } catch (Exception e) {

        }


    }


    @SuppressLint({"Range", "SetTextI18n"})
    private  String statusMessage(Cursor c) {
        String msg = "???";

        switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_PENDING:
                msg = "Pending..";
                btn_download.setVisibility(View.INVISIBLE);
                txt_progress.setVisibility(View.VISIBLE);
                txt_bytes.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                btn_resume.setVisibility(View.INVISIBLE);
                btn_pause.setVisibility(View.INVISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                break;

            case DownloadManager.STATUS_RUNNING:
                msg = "Downloading..";
                btn_download.setVisibility(View.INVISIBLE);
                txt_progress.setVisibility(View.VISIBLE);
                txt_bytes.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.VISIBLE);
                btn_resume.setVisibility(View.INVISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                break;

            case DownloadManager.STATUS_PAUSED:
                msg = "Paused";
                btn_download.setVisibility(View.INVISIBLE);
                txt_progress.setVisibility(View.VISIBLE);
                txt_bytes.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.INVISIBLE);
                btn_resume.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Complete";
                btn_download.setVisibility(View.INVISIBLE);
                txt_progress.setVisibility(View.VISIBLE);
                txt_bytes.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                btn_pause.setVisibility(View.INVISIBLE);
                btn_resume.setVisibility(View.INVISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                text_play.setVisibility(View.VISIBLE);
                break;
            case DownloadManager.STATUS_FAILED:
                msg = "Failed!";
                btn_download.setVisibility(View.VISIBLE);
                break;

            default:
                msg = "Nothing";
                break;
        }

        return (msg);
    }



    private  boolean pauseDownload() {
        int updatedRow = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("control", 1);

        try {
            updatedRow = getContext().getContentResolver().update(Uri.parse("content://downloads/my_downloads"), contentValues, "title=?", new String[]{name});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0 < updatedRow;
    }



    private boolean resumeDownload() {
        int updatedRow = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("control", 0);

        try {
            updatedRow = getContext().getContentResolver().update(Uri.parse("content://downloads/my_downloads"), contentValues, "title=?", new String[]{name});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0 < updatedRow;
    }




    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @SuppressLint("Range")
        public void onReceive(Context ctxt, Intent intent) {

           // Toast.makeText(ctxt, "Broad_cast", Toast.LENGTH_SHORT).show();


        }
    };


    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);

    }


    private String bytesIntoHumanReadable(long bytes) {
        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " Bytes";
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}



