package com.jnu.finalapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Objects;

public class EditBookActivity extends AppCompatActivity {

    Intent intent;
    EditText editTitle,editAuthor,editTranslator,editPubdate,editPublisher,editISBN,editTag,editBookshelf,editNote;
    ImageView editPicture;
    int mresultcode;
    EditText tag,shelf;
    String[] taglist,shelflist;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        initData();

        // activate toolbar close button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbaredit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBookActivity.this.finish();
            }
        });

        tag=findViewById(R.id.editTextTag);
        tag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= (tag.getWidth() - tag
                            .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        tag.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrowup), null);
                        showListPopulWindow(taglist,tag);
                        return true;
                    }
                }
                return false;
            }
        });
        shelf=findViewById(R.id.editTextShelf);
        shelf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getX() >= (shelf.getWidth() - shelf
                            .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        shelf.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrowup), null);
                        showListPopulWindow(shelflist,shelf);
                        return true;
                    }
                }
                return false;
            }
        });

    }
    private void showListPopulWindow(String[] list,EditText edit){
        ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list));
        listPopupWindow.setAnchorView(tag);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edit.setText(list[i]);
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                edit.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrow), null);
            }
        });
        listPopupWindow.show();

    }
    protected void initData(){
        intent=getIntent();
        mresultcode =1;
        editTitle=(EditText)findViewById(R.id.editTexttitle);
        editAuthor=(EditText)findViewById(R.id.editTextauthor);
        editPublisher=(EditText)findViewById(R.id.editTextpublisher);
        editPubdate=(EditText) findViewById(R.id.editTextpubdate);
        editTranslator=(EditText)findViewById(R.id.editTexttranslator);
        editISBN=(EditText)findViewById(R.id.editTextTextisbn);
        editPicture=(ImageView) findViewById(R.id.imageViewcover);
        editTag=(EditText) findViewById(R.id.editTextTag);
        editBookshelf=(EditText) findViewById(R.id.editTextShelf);
        editNote=(EditText) findViewById(R.id.editTextNote);

        ArrayList<String> gettaglist=intent.getStringArrayListExtra("taglist");
        taglist = (String[])gettaglist.toArray(new String[gettaglist.size()]);

        ArrayList<String> getshelflist=intent.getStringArrayListExtra("shelflist");
        shelflist = (String[])gettaglist.toArray(new String[gettaglist.size()]);

        if(Objects.equals(intent.getStringExtra("Action"), "Edit")) {
            editTitle.setText(intent.getStringExtra("title"));
            editAuthor.setText(intent.getStringExtra("author"));
            editISBN.setText(intent.getStringExtra("isbn"));
            editPubdate.setText(intent.getStringExtra("pubdate"));
            editTranslator.setText(intent.getStringExtra("translator"));
            editPublisher.setText(intent.getStringExtra("publisher"));
            editPicture.setImageResource(intent.getIntExtra("picture",R.drawable.book_no_name));
            editTag.setText(intent.getStringExtra("tag"));
            editBookshelf.setText(intent.getStringExtra("bookshelf"));
            editNote.setText(intent.getStringExtra("note"));
            mresultcode =2;
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater inflater = getMenuInflater();

        // SAVE button in EditActivity
        inflater.inflate(R.menu.edit_save, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // message reponse for SAVE
            case R.id.save:
                save();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_save, menu);
        return true;
    }

    public void save(){
        // save renewed data into Intent
        intent.putExtra("title", editTitle.getText().toString());
        intent.putExtra("author", editAuthor.getText().toString());
        intent.putExtra("publisher", editPublisher.getText().toString());
        intent.putExtra("pubdate", editPubdate.getText().toString());
        intent.putExtra("isbn", editISBN.getText().toString());
        intent.putExtra("tag", editTag.getText().toString());
        intent.putExtra("bookshelf", editBookshelf.getText().toString());
        intent.putExtra("note", editNote.getText().toString());

        EditBookActivity.this.setResult(mresultcode, intent);
        EditBookActivity.this.finish();
    }
}