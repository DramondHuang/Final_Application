package com.jnu.finalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class BookListMainActivity extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private RecyclerAdapter myAdapter;
    private int mPosition;
    ArrayList<Book> Booklist=new ArrayList<Book>();
    ArrayList<Book> tempBooklist=new ArrayList<Book>();
    DataSaver dataSaver=new DataSaver();
    public static Context con;
    public DrawerLayout mDrawerLayout;
    ArrayList<String>taglist,shelflist;

    static public Context getContext(){
        return con;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);
        con=this.getApplicationContext();

        initData();

        myRecyclerView=(RecyclerView) findViewById(R.id.recycle_view_books);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter=new RecyclerAdapter();
        myAdapter.displayBooklist=dataSaver.LoadBook(getApplicationContext());
        myRecyclerView.setAdapter(myAdapter);
        registerForContextMenu(findViewById(R.id.recycle_view_books));

        // activate toolbar menu button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // activate drawer
        mDrawerLayout=findViewById(R.id.drawerLayout);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // activate drawer items
        NavigationView navview=(NavigationView)findViewById(R.id.nav_view);
        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_tag:
                        showTaglist();
                        break;
                    case R.id.nav_shelf:
                        showShelflist();
                        break;
                    case R.id.nav_search:
                        showInputDialog();
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });
    }
    protected void initData(){

        SharedPreferences settings = getSharedPreferences("name", 0);
        boolean firstStart = settings.getBoolean("firstStart", true);

        if(firstStart) {
            Book Book1= new Book("???????????????", "https://s1.ax1x.com/2022/12/12/z4Ggkq.jpg","?????????","2019-01",""," ???????????????","9787100166874","??????","??????","?????????????????????????????????");
            Book Book2= new Book("????????????", "https://s1.ax1x.com/2022/12/12/z4G2t0.jpg","[???]??????????????????(Stan Weinstein)???","2021-03","??????","???????????????????????????","9787300288987","??????","??????","????????????????????????????????????????????????????????????");
            Book Book3= new Book("????????????","https://s1.ax1x.com/2022/12/12/z4GfpT.jpg","?????????/?????????","2014-08","","???????????????????????????","9787300195780","??????","??????","???????????????????????????????????????");
            Book Book4= new Book("??????????????????","https://s1.ax1x.com/2022/12/12/z4Gh1U.jpg","??????]???????????????????????(Anthony Crescenzi)","2016-01","??????","?????????????????????","9787111524434","??????","??????","?????????????????????????????????????????????--??????????????????????????????????????????");
            Book Book5= new Book("????????????????????????","https://s1.ax1x.com/2022/12/12/z4G4cF.jpg","[???]??????????????????(Hobson Lane)/[???]??????????????????(Cole Howard)/[???]?????????????????????????????????(Hannes Max Hapke)","2020-10","??????/??????/?????????/??????","?????????????????????","9787115540232","??????????????????","???????????????","???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????Python???NLP???????????????????????????????????????????????????");
            Book Book6= new Book("???????????????????????????","https://s1.ax1x.com/2022/12/12/z4Gy0s.jpg"," ?????????","2005-04","","?????????????????????","9787302100898","??????????????????","???????????????","???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
            Book Book7= new Book("????????????4??????","https://s1.ax1x.com/2022/12/12/z4G67n.jpg","[???]Robert Sedgewick/[???]Kevin Wayne","2012-10","?????????","?????????????????????","9787115293800","??????","???????????????","????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
            Book Book8= new Book("????????????","https://s1.ax1x.com/2022/12/12/z4GRhV.jpg"," ?????????","2016-12","","?????????????????????","9787115440358","??????","???????????????","?????????4 ???????????????????????????????????????????????????????????????????????????????????????");

            Booklist.add(Book1);
            Booklist.add(Book2);
            Booklist.add(Book3);
            Booklist.add(Book4);
            Booklist.add(Book5);
            Booklist.add(Book6);
            Booklist.add(Book7);
            Booklist.add(Book8);

            dataSaver.SaveBook(getApplicationContext(),Booklist);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstStart", false);
            editor.commit();

                taglist = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.taglist)));
                shelflist = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.shelflist)));

            }

    }
    //For add button
    public void Add(View view){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(BookListMainActivity.this);
        normalDialog.setIcon(R.drawable.bookshelf);
        normalDialog.setTitle("??????????????????");
        normalDialog.setMessage("?????????????????????");
        normalDialog.setPositiveButton("????????????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IntentIntegrator intentIntegrator = new IntentIntegrator(BookListMainActivity.this);
                        intentIntegrator.setBeepEnabled(true);
                        /*?????????????????????????????????????????????????????????????????????????????????*/
                        intentIntegrator.setCaptureActivity(ScanActivity.class);
                        intentIntegrator.initiateScan();
                    }
                });
        normalDialog.setNegativeButton("????????????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(getApplicationContext(),EditBookActivity.class);
                        intent.putExtra("Action","Add");
                        intent.putExtra("taglist",getTaglist());
                        intent.putExtra("shelflist",getShelflist());
                        startActivityForResult(intent,0);
                    }
                });
        // ??????
        normalDialog.show();

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater inflater = getMenuInflater();
        // Context menu for edit and delete
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                AlertDialog.Builder builder=new AlertDialog.Builder(BookListMainActivity.this);
                builder.setMessage("?????????????");
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(BookListMainActivity.this,"????????????",Toast.LENGTH_SHORT).show();
                        tempBooklist=myAdapter.displayBooklist;
                        Book deleted=tempBooklist.get(mPosition);

                        //modify adapter
                        tempBooklist.remove(mPosition);
                        myAdapter.displayBooklist=tempBooklist;
                        myAdapter.notifyDataSetChanged();

                        //modify dataset
                        tempBooklist=new DataSaver().LoadBook(getApplicationContext());
                        for (int i=0;i<tempBooklist.size();i++){
                            if (tempBooklist.get(i).isbn.equals(deleted.isbn)||(tempBooklist.get(i).cover.equals(deleted.cover))){
                                tempBooklist.remove(i);
                            }
                        }
                        dataSaver.SaveBook(getApplicationContext(),tempBooklist);

                        dialog.dismiss();
                    }

                });
                builder.setNegativeButton("??????", new  DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;

            case R.id.edit:
                Intent intent2=new Intent(this,EditBookActivity.class);
                tempBooklist=myAdapter.displayBooklist;
                Book book=tempBooklist.get(mPosition);
                intent2.putExtra("Action","Edit");
                intent2.putExtra("title",book.title);
                intent2.putExtra("author",book.author);
                intent2.putExtra("translator",book.translator);
                intent2.putExtra("pubdate",book.pubdate);
                intent2.putExtra("publisher",book.publisher);
                intent2.putExtra("isbn",book.isbn);
                intent2.putExtra("picture",book.cover);
                intent2.putExtra("tag",book.tag);
                intent2.putExtra("bookshelf",book.bookshelf);
                intent2.putExtra("note",book.note);

                intent2.putExtra("taglist",getTaglist());
                intent2.putExtra("shelflist",getShelflist());

                startActivityForResult(intent2,0);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "????????????", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "????????????: " + result.getContents(), Toast.LENGTH_LONG).show();
                String isbn=result.getContents();
                String apiurl="https://api.ibook.tech/v1/book/isbn?isbn="+isbn+"&uKey=83317b5e85aa4a5d942f4bfa0ad116f4";
                Intent intent2=new Intent(getApplicationContext(),EditBookActivity.class);
                intent2.putExtra("Action","Scan");
                intent2.putExtra("apiurl",apiurl);
                intent2.putExtra("taglist",getTaglist());
                intent2.putExtra("shelflist",getShelflist());
                startActivityForResult(intent2,0);

            }
        }

        if(data!=null) {
            String title = data.getExtras().getString("title");
            String pubdate = data.getExtras().getString("pubdate");
            String publisher = data.getExtras().getString("publisher");
            String isbn = data.getExtras().getString("isbn");
            String translator = data.getExtras().getString("translator");
            String author = data.getExtras().getString("author");
            String tag = data.getExtras().getString("tag");
            String bookshelf = data.getExtras().getString("bookshelf");
            String note = data.getExtras().getString("note");
            String cover=data.getExtras().getString("cover");

            switch (resultCode) {
                case 1:         // add new book
                    Book addedbook = new Book(title,"https://s1.ax1x.com/2022/12/12/z41pFK.jpg" , author, pubdate,translator,publisher,isbn,tag,bookshelf,note);
                    //modify adapter
                    tempBooklist.add(addedbook);
                    myAdapter.displayBooklist=tempBooklist;
                    myAdapter.notifyDataSetChanged();

                    //modify dataset
                    tempBooklist=dataSaver.LoadBook(BookListMainActivity.getContext());
                    tempBooklist.add(addedbook);
                    dataSaver.SaveBook(getApplicationContext(),tempBooklist);
                    break;

                case 2:         // edit existed book
                    Book book=myAdapter.displayBooklist.get(mPosition);
                    tempBooklist=dataSaver.LoadBook(getApplicationContext());
                    //modify dataset
                    for (int i=0;i<tempBooklist.size();i++){
                        if(tempBooklist.get(i).isbn.equals(book.isbn)||(tempBooklist.get(i).cover.equals(book.cover))){
                            Book ebook=tempBooklist.get(i);
                            ebook.title = title;
                            ebook.pubdate=pubdate;
                            ebook.publisher=publisher;
                            ebook.isbn=isbn;
                            ebook.translator=translator;
                            ebook.author=author;
                            ebook.tag=tag;
                            ebook.bookshelf=bookshelf;
                            ebook.note=note;
                        }
                    }
                    dataSaver.SaveBook(getApplicationContext(),tempBooklist);

                    //modify adapter
                    book.title = title;
                    book.pubdate=pubdate;
                    book.publisher=publisher;
                    book.isbn=isbn;
                    book.translator=translator;
                    book.author=author;
                    book.tag=tag;
                    book.bookshelf=bookshelf;
                    book.note=note;
                    myAdapter.notifyDataSetChanged();
                    break;
                case 3:  //add from scanning
                    Book newbook = new Book(title,cover , author, pubdate,translator,publisher,isbn,tag,bookshelf,note);
                    tempBooklist=dataSaver.LoadBook(BookListMainActivity.getContext());
                    tempBooklist.add(newbook);
                    dataSaver.SaveBook(getApplicationContext(),tempBooklist);
                    myAdapter.displayBooklist=tempBooklist;
                    myAdapter.notifyDataSetChanged();
                    break;
                default:
                    //???????????????????????????
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

        ArrayList<Book> displayBooklist;
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(
                    BookListMainActivity.this).inflate(R.layout.recycler_items,parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position){
            Book book=displayBooklist.get(position);
            holder.tv.setText(book.title);
            new BookLoader().setCover(book.cover,holder.iv);
            holder.au.setText(book.author);
            holder.ti.setText(book.pubdate);
            holder.pb.setText(book.publisher);

            //Long press on a book to edit or delete
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mPosition = holder.getAdapterPosition();//?????????position????????????
                    return false;
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = holder.getAdapterPosition();//?????????position????????????
                    Intent intent2=new Intent(getApplicationContext(),DetailActivity.class);
                    tempBooklist=myAdapter.displayBooklist;
                    Book book=tempBooklist.get(mPosition);
                    intent2.putExtra("Action","Edit");
                    intent2.putExtra("title",book.title);
                    intent2.putExtra("author",book.author);
                    intent2.putExtra("translator",book.translator);
                    intent2.putExtra("pubdate",book.pubdate);
                    intent2.putExtra("publisher",book.publisher);
                    intent2.putExtra("isbn",book.isbn);
                    intent2.putExtra("picture",book.cover);
                    intent2.putExtra("tag",book.tag);
                    intent2.putExtra("bookshelf",book.bookshelf);
                    intent2.putExtra("note",book.note);
                    intent2.putExtra("taglist",getTaglist());
                    intent2.putExtra("shelflist",getShelflist());

                    startActivityForResult(intent2,0);
                }
            });
        }

        @Override
        public int getItemCount(){
            return displayBooklist.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv,au,ti,pb;
            ImageView iv;

            public MyViewHolder(View inflate) {
                super(inflate);
                tv=(TextView) inflate.findViewById(R.id.text_view_book_title);
                iv=(ImageView) inflate.findViewById(R.id.image_view_book_cover);
                au=(TextView) inflate.findViewById(R.id.text_view_book_author);
                ti=(TextView) inflate.findViewById(R.id.text_view_book_time);
                pb=(TextView) inflate.findViewById(R.id.text_view_book_publisher);
            }
        }
    }

    public ArrayList<String> getTaglist(){
        ArrayList<Book> booklist=new DataSaver().LoadBook(this.getApplicationContext());
        ArrayList<String> taglist=new ArrayList<>();
        for(Book book:booklist){
            if (book.tag==""){
                continue;
            }
            if (!taglist.contains(book.tag)){
                taglist.add(book.tag);
            }
        }
        return taglist;
    }
    public ArrayList<String> getShelflist(){
        ArrayList<Book> booklist=new DataSaver().LoadBook(this.getApplicationContext());
        ArrayList<String> shelflist=new ArrayList<>();
        for(Book book:booklist){
            if (book.bookshelf==""){
                continue;
            }
            if (!shelflist.contains(book.bookshelf)){
                shelflist.add(book.bookshelf);
            }
        }
        return shelflist;
    }
    private void showTaglist() {
        ArrayList<String> gettaglist=getTaglist();
        gettaglist.add(0,"????????????");
        String[] list=(String[])gettaglist.toArray(new String[gettaglist.size()]);
        View view=findViewById(R.id.nav_tag);
        ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list));
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listPopupWindow.dismiss();
                String displayTag=list[i];
                ArrayList<Book> booklist=new DataSaver().LoadBook(getApplicationContext());
                ArrayList<Book> displaybooklist= new ArrayList<>();
                if (displayTag.equals("????????????")) displaybooklist=booklist;
                else {
                    for (Book book : booklist) {
                        if (book.tag.equals(displayTag)) displaybooklist.add(book);
                    }
                }
                myAdapter.displayBooklist=displaybooklist;
                myAdapter.notifyDataSetChanged();
                listPopupWindow.dismiss();
                mDrawerLayout.closeDrawers();
            }
        });
        listPopupWindow.show();
    }
    private void showShelflist() {
        ArrayList<String> getshelflist=getShelflist();
        getshelflist.add(0,"????????????");
        String[] list=(String[])getshelflist.toArray(new String[getshelflist.size()]);
        View view=findViewById(R.id.nav_shelf);
        ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list));
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listPopupWindow.dismiss();
                String displayShelf=list[i];
                ArrayList<Book> displaybooklist= new ArrayList<>();
                ArrayList<Book> booklist=new DataSaver().LoadBook(getApplicationContext());
                if (displayShelf.equals("????????????")) displaybooklist=booklist;
                else{
                    for (Book book : booklist){
                        if (book.bookshelf.equals(displayShelf)) displaybooklist.add(book);
                    }
                }
                myAdapter.displayBooklist=displaybooklist;
                myAdapter.notifyDataSetChanged();
                listPopupWindow.dismiss();
                mDrawerLayout.closeDrawers();
            }
        });
        listPopupWindow.show();
    }
    private void showInputDialog(){
        final EditText inputServer = new EditText(BookListMainActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(BookListMainActivity.this);
        builder.setTitle("???????????????????????????").setIcon(R.drawable.search).setView(inputServer)
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String message = inputServer.getText().toString();
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG);
                tempBooklist=SearchBook(message,new DataSaver().LoadBook(getApplicationContext()));
                myAdapter.displayBooklist=tempBooklist;
                myAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    ArrayList<Book> SearchBook(String message,ArrayList<Book> booklist){
        ArrayList<Book> temp=new ArrayList<>();
        for (Book book:booklist) {
            if (book.title.equals(message) || (book.author.equals(message)) || book.translator.equals(message) || book.publisher.equals(message) || book.pubdate.equals(message) || book.isbn.equals(message)) {
                temp.add(book);
            }
        }
        return temp;
    }
}