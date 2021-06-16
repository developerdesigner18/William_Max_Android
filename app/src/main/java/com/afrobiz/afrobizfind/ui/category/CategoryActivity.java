package com.afrobiz.afrobizfind.ui.category;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afrobiz.afrobizfind.R;
import com.afrobiz.afrobizfind.ui.modal.Category;
import com.afrobiz.afrobizfind.ui.widgets.RevealAnimation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class CategoryActivity extends AppCompatActivity
{
    RevealAnimation mRevealAnimation;
    LinearLayout rootLayout;

    ImageView IvBack;

    @BindView(R.id.rcv_category)
    RecyclerView rcv_category;
    List<Category> categoryList = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            categoryList = (List<Category>) bundle.getSerializable("list_category");
        }

        Intent intent = this.getIntent();

        rootLayout = (LinearLayout) findViewById(R.id.root_layout);

        rcv_category = (RecyclerView) findViewById(R.id.rcv_category);

        mRevealAnimation = new RevealAnimation(rootLayout, intent, this);
        IvBack = (ImageView) findViewById(R.id.IvBack);

        IvBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                rootLayout.setBackgroundColor(Color.WHITE);
                if(categoryList != null && categoryList.size() > 0)
                {
                    CategoryAdapter catAdapter = new CategoryAdapter(CategoryActivity.this , categoryList);
                    rcv_category.setAdapter(catAdapter);
                    GridLayoutManager manager = new GridLayoutManager(CategoryActivity.this , 2);
                    rcv_category.setLayoutManager(manager);

                    runLayoutAnimation(rcv_category);
                }
            }
        },1000);

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        rootLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mRevealAnimation.unRevealActivity();
    }

    private void runLayoutAnimation(final RecyclerView recyclerView)
    {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom_scale);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
