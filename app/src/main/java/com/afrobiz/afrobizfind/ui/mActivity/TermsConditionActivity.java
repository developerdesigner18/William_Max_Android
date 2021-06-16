package com.afrobiz.afrobizfind.ui.mActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.shockwave.pdfium.PdfDocument;

import com.afrobiz.afrobizfind.R;

import java.util.List;

public class TermsConditionActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener
{
    ImageView IvBack;
    WebView web_terms;
    PDFView pdfView;
    private static final String TAG = TermsConditionActivity.class.getSimpleName();
    Integer pageNumber = 0;
    public static final String SAMPLE_FILE = "terms.pdf";
    String pdfFileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terms_condition);

        web_terms = (WebView) findViewById(R.id.web_terms);
        pdfView = (PDFView) findViewById(R.id.pdfView);

        displayFromAsset(SAMPLE_FILE);

        IvBack = (ImageView) findViewById(R.id.Ivback);

        IvBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i_go = new Intent(TermsConditionActivity.this , SettingActivity.class);
                startActivity(i_go);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i_go = new Intent(TermsConditionActivity.this , SettingActivity.class);
        startActivity(i_go);
        finish();
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep)
    {
        for (PdfDocument.Bookmark b : tree)
        {
            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren())
            {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }


    @Override
    public void onPageChanged(int page, int pageCount)
    {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void onPageError(int page, Throwable t)
    {
        Log.e(TAG, "Cannot load page " + page);
    }
}
