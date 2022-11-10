package com.jnu.finalapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Objects;

public class EditBookActivity extends AppCompatActivity {

    Intent intent;
    EditText editTitle,editAuthor,editTranslator,editPubdate,editPublisher,editISBN;
    ImageView editPicture;
    int mresultcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        intent=getIntent();
        mresultcode =1;
        editTitle=(EditText)findViewById(R.id.editTexttitle);
        editAuthor=(EditText)findViewById(R.id.editTextauthor);
        editPublisher=(EditText)findViewById(R.id.editTextpublisher);
        editPubdate=(EditText) findViewById(R.id.editTextpubdate);
        editTranslator=(EditText)findViewById(R.id.editTexttranslator);
        editISBN=(EditText)findViewById(R.id.editTextTextisbn);
        editPicture=(ImageView) findViewById(R.id.imageViewcover);

        if(Objects.equals(intent.getStringExtra("Action"), "Edit")) {
            editTitle.setText(intent.getStringExtra("title"));
            editAuthor.setText(intent.getStringExtra("author"));
            editISBN.setText(intent.getStringExtra("isbn"));
            editPubdate.setText(intent.getStringExtra("pubdate"));
            editTranslator.setText(intent.getStringExtra("translator"));
            editPublisher.setText(intent.getStringExtra("publisher"));
            editPicture.setImageResource(intent.getIntExtra("picture",R.drawable.book_no_name));
            mresultcode =2;
        }

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
        intent.putExtra("translator", editTranslator.getText().toString());

        EditBookActivity.this.setResult(mresultcode, intent);
        EditBookActivity.this.finish();
    }
}