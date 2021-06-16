package com.afrobiz.afrobizfind;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class CropImageActivity extends AppCompatActivity
{
    String path;
    public static Bitmap cropBmp , croppedBMP;
    CropImageView crop_img;
    ImageView img_crop_done , img_vertical ,img_horizontal ,img_back;
    public static  Bitmap bmp;
    ProgressDialog pd;
    String imagepath;
    Bitmap galleryBmp;
    boolean logo = false;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crop);

        crop_img = (CropImageView) findViewById(R.id.crop_img);

        img_back = (ImageView) findViewById(R.id.img_back);
        img_crop_done = (ImageView) findViewById(R.id.img_crop_done);
        img_horizontal = (ImageView) findViewById(R.id.img_crop_horizontal);
        img_vertical = (ImageView) findViewById(R.id.img_crop_vertical);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            imagepath = bundle.getString("imagepath");
            logo = bundle.getBoolean("logo");

            if(imagepath != null)
            {
                galleryBmp = BitmapFactory.decodeFile(imagepath);
                if(galleryBmp != null)
                {
                    pd = new ProgressDialog(CropImageActivity.this);
                    pd.setMessage("Please wait");
                    pd.setCancelable(false);
                    pd.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            bmp = bitmapResize(crop_img , galleryBmp);
                            crop_img.setImageBitmap(bmp);
                            pd.dismiss();
                        }
                    },2000);
                }
            }
        }
        img_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if(pd != null && pd.isShowing())
                {
                    pd.dismiss();
                }
                Intent i_blend = new Intent();
                i_blend.putExtra("imagepath",imagepath);
                i_blend.putExtra("logo",logo);
                setResult(RESULT_CANCELED , i_blend );
                finish();
            }
        });


//        crop_img.setImageBitmap(AddProductActivity.galleryBmp);

        img_crop_done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                croppedBMP = crop_img.getCroppedImage();
                if(croppedBMP != null)
                {
                    String savedpath = saveImage(croppedBMP);
//                    Bundle b = new Bundle();
//                    b.putString("imagepath",savedpath);
                    Intent i_blend = new Intent();
                    i_blend.putExtra("imagepath",savedpath);
                    i_blend.putExtra("logo",logo);
                    setResult(RESULT_OK , i_blend);
                    finish();
                }
            }
        });

        img_horizontal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Matrix matrix = new Matrix();
                matrix.preScale(-1.0f, 1.0f);
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                crop_img.setImageBitmap(bmp);
            }
        });
        img_vertical.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Matrix matrix = new Matrix();
                matrix.preScale(1.0f, -1.0f);
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                crop_img.setImageBitmap(bmp);
            }
        });
    }

    public String saveImage(Bitmap myBitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory()
                , getResources().getString(R.string.app_folder));

        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh",wallpaperDirectory.toString());
        }

        try
        {
            File f = new File(wallpaperDirectory, System.currentTimeMillis()+ ".jpeg");

            if(f.exists())
            {
                f.delete();
            }
            else
            {
                f.createNewFile();
            }

            FileOutputStream fo = new FileOutputStream(f);

            fo.write(bytes.toByteArray());

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(f)));

            MediaScannerConnection.scanFile(CropImageActivity.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());


            return f.getAbsolutePath();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return "";

    }
    @Override
    public void onBackPressed()
    {
        if(pd != null && pd.isShowing())
        {
            pd.dismiss();
        }
        Intent i_blend = new Intent();
        i_blend.putExtra("imagepath",imagepath);
        i_blend.putExtra("logo",logo);
        setResult(RESULT_CANCELED , i_blend );
        finish();
    }

    public Bitmap bitmapResize(View v , Bitmap bmp)
    {

        int layoutwidth = v.getWidth();

        int layoutheight = v.getHeight();

        int imagewidth = bmp.getWidth();
        int imageheight = bmp.getHeight();
        int newwidth = 0;
        int newheight = 0;
        if (imagewidth >= imageheight)
        {
            newwidth = layoutwidth;
            newheight = (newwidth * imageheight) / imagewidth;
            if (newheight > layoutheight)
            {
                newwidth = (layoutheight * newwidth) / newheight;
                newheight = layoutheight;

            }
        }
        else
        {
            newheight = layoutheight;
            newwidth = (newheight * imagewidth) / imageheight;
            if (newwidth > layoutwidth)
            {
                newheight = (newheight * layoutwidth) / newwidth;
                newwidth = layoutwidth;
            }
        }
        Bitmap b56 = Bitmap.createScaledBitmap(bmp, newwidth, newheight, true);
        return b56;
    }
}
