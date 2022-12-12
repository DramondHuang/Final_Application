package com.jnu.finalapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    Intent intent;
    TextView editTitle,editAuthor,editTranslator,editPubdate,editPublisher,editISBN,editTag,editBookshelf,editNote;
    ImageView editPicture;
    String[] taglist,shelflist;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initData();

        // activate toolbar close button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbaredit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.finish();
            }
        });

    }
    protected void initData(){
        intent=getIntent();
        editTitle=findViewById(R.id.editTexttitle);
        editAuthor=findViewById(R.id.editTextauthor);
        editPublisher=findViewById(R.id.editTextpublisher);
        editPubdate=findViewById(R.id.editTextpubdate);
        editTranslator=findViewById(R.id.editTexttranslator);
        editISBN=findViewById(R.id.editTextTextisbn);
        editPicture=findViewById(R.id.imageViewcover);
        editTag=findViewById(R.id.editTextTag);
        editBookshelf=findViewById(R.id.editTextShelf);
        editNote=findViewById(R.id.editTextNote);

        editTitle.setFocusableInTouchMode(false);//不可编辑
        editTitle.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框
        editAuthor.setFocusableInTouchMode(false);//不可编辑
        editAuthor.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框
        editPublisher.setFocusableInTouchMode(false);//不可编辑
        editPublisher.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框
        editPubdate.setFocusableInTouchMode(false);//不可编辑
        editPubdate.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框
        editTranslator.setFocusableInTouchMode(false);//不可编辑
        editTranslator.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框
        editISBN.setFocusableInTouchMode(false);//不可编辑
        editISBN.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框
        editTag.setFocusableInTouchMode(false);//不可编辑
        editTag.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框
        editBookshelf.setFocusableInTouchMode(false);//不可编辑
        editBookshelf.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框
        editNote.setFocusableInTouchMode(false);//不可编辑
        editNote.setKeyListener(null);//不可粘贴，长按不会弹出粘贴框

        ArrayList<String> gettaglist=intent.getStringArrayListExtra("taglist");
        taglist = (String[])gettaglist.toArray(new String[gettaglist.size()]);

        ArrayList<String> getshelflist=intent.getStringArrayListExtra("shelflist");
        shelflist = (String[])getshelflist.toArray(new String[getshelflist.size()]);


        editTitle.setText(intent.getStringExtra("title"));
        editAuthor.setText(intent.getStringExtra("author"));
        editISBN.setText(intent.getStringExtra("isbn"));
        editPubdate.setText(intent.getStringExtra("pubdate"));
        editTranslator.setText(intent.getStringExtra("translator"));
        editPublisher.setText(intent.getStringExtra("publisher"));
        new BookLoader().setCover(intent.getStringExtra("picture"),editPicture);
        editTag.setText(intent.getStringExtra("tag"));
        editBookshelf.setText(intent.getStringExtra("bookshelf"));
        editNote.setText(intent.getStringExtra("note"));
    }
}