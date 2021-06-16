package com.afrobiz.afrobizfind.ui.inventory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.adapter.ProductDeatilsImagesAdapter;
import com.afrobiz.afrobizfind.ui.mActivity.Company_productlist.CompanyProductListActivity;
import com.afrobiz.afrobizfind.ui.mActivity.orderHistory.Inventory.InventoryActivity;
import com.afrobiz.afrobizfind.ui.modal.Image;
import com.afrobiz.afrobizfind.ui.modal.Product;
import com.afrobiz.afrobizfind.ui.scanner.FullScannerActivity;
import com.google.gson.Gson;

import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class InentorySetupDetailsActivity extends AppCompatActivity
{
    TextView tv_barcode1, tv_add_barcode, tv_product_name, tv_wholesale_price1, tv_retails_price1, tv_retails_price, tv_wholesale_price,
            tv_qty_per_box, tv_product_number, tv_qty, tv_barcodes, tv_last_updated, tv_restrict_stock,tv_empty;

    public static final int SCAN = 923;
    private static final int ZXING_CAMERA_PERMISSION = 110;
    Product currentInventory;
    List<Image> listimages;
    RecyclerView rcv_product_images;
    ImageView Ivback, img_edit, img_delete;
    boolean isEdit = false;
    int company_id = 0;

    LinearLayout layout_update_buttons;
    TextView tv_update, tv_cancel;

    EditText et_barcode1, et_product_name, et_wholesale_price1, et_retails_price1, et_retails_price, et_wholesale_price,
            et_qty_per_box, et_product_number, et_qty, et_barcodes, et_last_updated, et_restrict_stock;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_setup_details);

        img_delete = (ImageView) findViewById(R.id.img_delete);
        img_edit = (ImageView) findViewById(R.id.img_edit);

        Ivback = (ImageView) findViewById(R.id.Ivback);

        layout_update_buttons = (LinearLayout) findViewById(R.id.layout_update_buttons);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_update = (TextView) findViewById(R.id.tv_update);

        tv_product_name = (TextView) findViewById(R.id.tv_product_name);
        tv_product_number = (TextView) findViewById(R.id.tv_product_number);
        tv_retails_price = (TextView) findViewById(R.id.tv_retails_price);
        tv_wholesale_price = (TextView) findViewById(R.id.tv_wholesale_price);
        tv_qty = (TextView) findViewById(R.id.tv_qty);
        tv_barcodes = (TextView) findViewById(R.id.tv_barcodes);
        tv_last_updated = (TextView) findViewById(R.id.tv_last_update);
        tv_restrict_stock = (TextView) findViewById(R.id.tv_restrict_stock);
        tv_add_barcode = (TextView) findViewById(R.id.tv_add_barcode);
        tv_barcode1 = (TextView) findViewById(R.id.tv_barcode1);
        tv_qty_per_box = (TextView) findViewById(R.id.tv_qty_per_box);
        tv_retails_price1 = (TextView) findViewById(R.id.tv_retail_price1);
        tv_wholesale_price1 = (TextView) findViewById(R.id.tv_wholesale_price1);
        tv_barcodes = (TextView) findViewById(R.id.tv_barcodes);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        rcv_product_images = (RecyclerView) findViewById(R.id.rcv_product_images);

        et_product_name = (EditText) findViewById(R.id.et_product_name);
        et_product_number = (EditText) findViewById(R.id.et_product_number);
        et_retails_price = (EditText) findViewById(R.id.et_retails_price);
        et_wholesale_price = (EditText) findViewById(R.id.et_wholesale_price);
        et_qty = (EditText) findViewById(R.id.et_qty);
        et_barcodes = (EditText) findViewById(R.id.et_barcodes);
        et_last_updated = (EditText) findViewById(R.id.et_last_update);
        et_restrict_stock = (EditText) findViewById(R.id.et_restrict_stock);
//        et_add_barcode = (EditText) findViewById(R.id.et_add_barcode);
        et_barcode1 = (EditText) findViewById(R.id.et_barcode1);
        et_qty_per_box = (EditText) findViewById(R.id.et_qty_per_box);
        et_retails_price1 = (EditText) findViewById(R.id.et_retail_price1);
        et_wholesale_price1 = (EditText) findViewById(R.id.et_wholesale_price1);
        et_barcodes = (EditText) findViewById(R.id.et_barcodes);

        textFields(isEdit);
        editFields(isEdit);

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            company_id = b.getInt("company_id");
            Log.e("com",""+company_id);

            currentInventory =  (Product) b.getSerializable("inventorysetup");
            Log.e("currentInventorySetup",""+new Gson().toJson(currentInventory));
        }
        if(currentInventory != null)
        {
            listimages = currentInventory.getImages();
            Log.e("listImages",""+new Gson().toJson(listimages));
            if(listimages != null && listimages.size() > 0)
            {
                rcv_product_images.setVisibility(View.VISIBLE);
                tv_empty.setVisibility(View.GONE);

                ProductDeatilsImagesAdapter imagesAdapter = new ProductDeatilsImagesAdapter(InentorySetupDetailsActivity.this , listimages);
                rcv_product_images.setAdapter(imagesAdapter);
                LinearLayoutManager manager = new LinearLayoutManager(InentorySetupDetailsActivity.this , LinearLayoutManager.HORIZONTAL , false);
                rcv_product_images.setLayoutManager(manager);
            }
            else
            {
                tv_empty.setVisibility(View.VISIBLE);
                rcv_product_images.setVisibility(View.GONE);
            }

            tv_product_number.setText(currentInventory.getProduct_number());
            tv_product_name.setText(currentInventory.getProductName());
            tv_retails_price.setText(currentInventory.getPrice());
            tv_wholesale_price.setText(currentInventory.getWholesalePrice());

            if(currentInventory.getInventory() != null && currentInventory.getInventory().size() > 0 )
            {
                tv_qty.setText(""+currentInventory.getInventory().get(0).getQuantity());
                tv_last_updated.setText(currentInventory.getInventory().get(0).getUpdatedAt());
                tv_barcodes.setText(currentInventory.getInventory().get(0).getBarcode());
            }

            et_product_number.setText(currentInventory.getProduct_number());
            et_product_name.setText(currentInventory.getProductName());
            et_retails_price.setText(currentInventory.getPrice());
            et_wholesale_price.setText(currentInventory.getWholesalePrice());

            if(currentInventory.getInventory() != null && currentInventory.getInventory().size() > 0 )
            {
                et_qty.setText(""+ currentInventory.getInventory().get(0).getQuantity());
                et_last_updated.setText(currentInventory.getInventory().get(0).getUpdatedAt());
                et_barcodes.setText(currentInventory.getInventory().get(0).getBarcode());
            }
        }

        tv_add_barcode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchActivity();
            }
        });
        img_edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isEdit == false)
                {
                    isEdit = true;
                    layout_update_buttons.setVisibility(View.VISIBLE);
                }
                else
                {
                    isEdit = false;
                    layout_update_buttons.setVisibility(View.GONE);
                }
                textFields(isEdit);
                editFields(isEdit);
            }
        });
        Ivback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_inventory = new Intent(InentorySetupDetailsActivity.this, InventorySetupActivity.class);
                i_inventory.putExtra("company_id", company_id);
                startActivity(i_inventory);
                finish();
            }
        });
    }
    public void launchActivity()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        }
        else
        {
            Intent intent = new Intent(this, FullScannerActivity.class);
            startActivityForResult(intent, SCAN);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(this, FullScannerActivity.class);
                    startActivityForResult(intent, SCAN);
                }
                else
                {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case SCAN :
                if(resultCode == RESULT_OK)
                {
                    String productName = data.getStringExtra("scan_result");
                    if(productName != null)
                    {
                        tv_barcode1.setText(productName);
                        et_barcode1.setText(productName);
                    }
                }
                break;
        }
    }
    public void editFields(boolean isEdit)
    {
        if(isEdit == true)
        {
            et_barcode1.setVisibility(View.VISIBLE);
            et_barcodes.setVisibility(View.VISIBLE);
            et_last_updated.setVisibility(View.VISIBLE);
            et_product_name.setVisibility(View.VISIBLE);
            et_product_number.setVisibility(View.VISIBLE);
            et_qty.setVisibility(View.VISIBLE);
            et_qty_per_box.setVisibility(View.VISIBLE);
            et_restrict_stock.setVisibility(View.VISIBLE);
            et_retails_price.setVisibility(View.VISIBLE);
            et_retails_price1.setVisibility(View.VISIBLE);
            et_wholesale_price.setVisibility(View.VISIBLE);
            et_wholesale_price1.setVisibility(View.VISIBLE);

        }
       else
        {
            et_barcode1.setVisibility(View.GONE);
            et_barcodes.setVisibility(View.GONE);
            et_last_updated.setVisibility(View.GONE);
            et_product_name.setVisibility(View.GONE);
            et_product_number.setVisibility(View.GONE);
            et_qty.setVisibility(View.GONE);
            et_qty_per_box.setVisibility(View.GONE);
            et_restrict_stock.setVisibility(View.GONE);
            et_retails_price.setVisibility(View.GONE);
            et_retails_price1.setVisibility(View.GONE);
            et_wholesale_price.setVisibility(View.GONE);
            et_wholesale_price1.setVisibility(View.GONE);
        }
    }

    public void textFields(boolean isEdit)
    {
        if (isEdit == true)
        {
            tv_barcode1.setVisibility(View.GONE);
            tv_barcodes.setVisibility(View.GONE);
            tv_last_updated.setVisibility(View.GONE);
            tv_product_name.setVisibility(View.GONE);
            tv_product_number.setVisibility(View.GONE);

            tv_qty.setVisibility(View.GONE);
            tv_qty_per_box.setVisibility(View.GONE);
            tv_restrict_stock.setVisibility(View.GONE);
            tv_retails_price.setVisibility(View.GONE);
            tv_retails_price1.setVisibility(View.GONE);
            tv_wholesale_price.setVisibility(View.GONE);
            tv_wholesale_price1.setVisibility(View.GONE);
        }
        else
        {
            tv_barcode1.setVisibility(View.VISIBLE);
            tv_barcodes.setVisibility(View.VISIBLE);
            tv_last_updated.setVisibility(View.VISIBLE);
            tv_product_name.setVisibility(View.VISIBLE);
            tv_product_number.setVisibility(View.VISIBLE);
            tv_qty.setVisibility(View.VISIBLE);
            tv_qty_per_box.setVisibility(View.VISIBLE);
            tv_restrict_stock.setVisibility(View.VISIBLE);
            tv_retails_price.setVisibility(View.VISIBLE);
            tv_retails_price1.setVisibility(View.VISIBLE);
            tv_wholesale_price.setVisibility(View.VISIBLE);
            tv_wholesale_price1.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_inventory = new Intent(InentorySetupDetailsActivity.this, InventorySetupActivity.class);
        i_inventory.putExtra("company_id", company_id);
        startActivity(i_inventory);
        finish();
    }
}
