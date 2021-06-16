package com.afrobiz.afrobizfind;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.afrobiz.afrobizfind.ui.mActivity.SettingActivity;

import java.io.File;
import java.util.Date;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ManageStorageActivity extends AppCompatActivity
{
    TextView tv_path;
    TextView tv_size;
    TextView tv_date;
    TextView tv_folder_name;
    TextView tv_clean , tv_empty;

    ImageView IvBack;

    LinearLayout layout_clean , layout_details;

    File mainFolder;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_storage);

        IvBack = (ImageView) findViewById(R.id.IvBack);

        layout_clean = (LinearLayout) findViewById(R.id.layout_clean);
        layout_details = (LinearLayout) findViewById(R.id.layout_details);

        tv_empty = (TextView) findViewById(R.id.tv_empty);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_size = (TextView) findViewById(R.id.tv_size);
        tv_folder_name = (TextView) findViewById(R.id.tv_folder_name);
        tv_path = (TextView) findViewById(R.id.tv_path);
        tv_clean = (TextView) findViewById(R.id.tv_clean);

        mainFolder = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_folder));
        if(mainFolder.exists())
        {
            layout_clean.setVisibility(View.VISIBLE);
            layout_details.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);

            tv_folder_name.setText("Folder Name :- "+mainFolder.getName());
            tv_path.setText("Path :- "+mainFolder.getAbsolutePath());

            Date lastModDate = new Date(mainFolder.lastModified());
            Log.e("File last modified : ",  lastModDate.toString());

            tv_date.setText("File last modified :- "+lastModDate.toString());

            long size = 0;

            size = getFileFolderSize(mainFolder);

            double sizeMB = (double) size / 1024 / 1024;

            String s = " MB";

            if (sizeMB < 1)
            {
                sizeMB = (double) size / 1024;
                s = " KB";
            }
            System.out.println(mainFolder.getName() + " : " + sizeMB + s);

            tv_size.setText("Size :- "+sizeMB+ s );
        }
        else
        {
            layout_clean.setVisibility(View.GONE);
            layout_details.setVisibility(View.GONE);
            tv_empty.setVisibility(View.VISIBLE);
        }

        IvBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_main = new Intent(ManageStorageActivity.this , SettingActivity.class);
                startActivity(i_main);
                finish();
            }
        });

        tv_clean.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deleteRecursive(mainFolder);
            }
        });



    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_main = new Intent(ManageStorageActivity.this , SettingActivity.class);
        startActivity(i_main);
        finish();
    }

    public static long getFileFolderSize(File dir)
    {
        long size = 0;
        if (dir.isDirectory())
        {
            for (File file : dir.listFiles())
            {
                if (file.isFile())
                {
                    size += file.length();
                }
                else
                    size += getFileFolderSize(file);
            }
        }
        else if (dir.isFile())
        {
            size += dir.length();
        }
        return size;
    }
    public void deleteRecursive(File fileOrDirectory)
    {
        if (fileOrDirectory.isDirectory())
        {
            for (File child : fileOrDirectory.listFiles())
            {
                deleteRecursive(child);
            }
        }

        boolean b = fileOrDirectory.delete();

        if(b == true)
        {
            Toast.makeText(ManageStorageActivity.this , "All Images Deleted" , Toast.LENGTH_SHORT).show();
            if(!mainFolder.exists())
            {
                layout_clean.setVisibility(View.GONE);
                layout_details.setVisibility(View.GONE);
                tv_empty.setVisibility(View.VISIBLE);
            }
        }
    }
}
