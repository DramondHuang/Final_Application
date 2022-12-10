package com.jnu.finalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BookListMainActivity extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private RecyclerAdapter myAdapter;
    private int mPosition;
    ArrayList<Book> Booklist=new ArrayList<Book>();
    ArrayList<Book> tempBooklist=new ArrayList<Book>();
    DataSaver dataSaver=new DataSaver();
    public static Context con;

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookListMainActivity.this,"MENU",Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void initData(){

        SharedPreferences settings = getSharedPreferences("name", 0);
        boolean firstStart = settings.getBoolean("firstStart", true);

        //if(firstStart) {
            //display your Message here
            Book Book1= new Book("软件项目管理案例教程（第4版）", R.drawable.book_2,"hzj","2002-06","translator","publisher","ISBN123","tag1","bookshelf1","note");
            Book Book2= new Book("创新工程实践", R.drawable.book_no_name,"hzj","2002-06","translator","publisher","ISBN123","tag2","bookshelf2","note");
            Book Book3= new Book("信息安全数学基础（第2版）", R.drawable.book_1,"hzj","2002-06","translator","publisher","ISBN123","tag3","bookshelf3","note");

            Booklist.add(Book1);
            Booklist.add(Book2);
            Booklist.add(Book3);

            dataSaver.SaveBook(getApplicationContext(),Booklist);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstStart", false);
            editor.commit();

//            taglist = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.taglist)));
//            shelflist = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.shelflist)));

        //}

    }
    //For add button
    public void Add(View view){
        Intent intent=new Intent(this,EditBookActivity.class);
        Toast.makeText(this,"add",Toast.LENGTH_SHORT).show();
        intent.putExtra("Action","Add");
        intent.putExtra("taglist",getTaglist());
        intent.putExtra("shelflist",getShelflist());
        startActivityForResult(intent,0);
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
                builder.setMessage("确认删除?");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(BookListMainActivity.this,"delete",Toast.LENGTH_SHORT).show();
                        tempBooklist=dataSaver.LoadBook(getApplicationContext());
                        tempBooklist.remove(mPosition);
                        dataSaver.SaveBook(getApplicationContext(),tempBooklist);
                        myAdapter.displayBooklist=tempBooklist;
                        myAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                });
                builder.setNegativeButton("取消", new  DialogInterface.OnClickListener(){

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
                Toast.makeText(this,"Edit",Toast.LENGTH_SHORT).show();
                tempBooklist=dataSaver.LoadBook(getApplicationContext());
                Book book=tempBooklist.get(mPosition);
                intent2.putExtra("Action","Edit");
                intent2.putExtra("title",book.title);
                intent2.putExtra("author",book.author);
                intent2.putExtra("translator",book.translator);
                intent2.putExtra("pubdate",book.pubdate);
                intent2.putExtra("publisher",book.publisher);
                intent2.putExtra("isbn",book.isbn);
                intent2.putExtra("picture",book.cover_id);
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

            switch (resultCode) {
                case 1:         // add new book
                    Book addedbook = new Book(title, R.drawable.book_no_name, author, pubdate,translator,publisher,isbn,tag,bookshelf,note);
                    tempBooklist=dataSaver.LoadBook(BookListMainActivity.getContext());
                    tempBooklist.add(addedbook);
                    dataSaver.SaveBook(getApplicationContext(),tempBooklist);
                    myAdapter.displayBooklist=tempBooklist;
                    myAdapter.notifyDataSetChanged();
                    break;

                case 2:         // edit existed book
                    tempBooklist=dataSaver.LoadBook(getApplicationContext());
                    Book book=tempBooklist.get(mPosition);
                    book.title = title;
                    book.pubdate=pubdate;
                    book.publisher=publisher;
                    book.isbn=isbn;
                    book.translator=translator;
                    book.author=author;
                    book.tag=tag;
                    book.bookshelf=bookshelf;
                    book.note=note;
                    dataSaver.SaveBook(getApplicationContext(),tempBooklist);
                    myAdapter.displayBooklist=tempBooklist;
                    myAdapter.notifyDataSetChanged();
                default:
                    //其它窗口的回传数据
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

        ArrayList<Book> displayBooklist;
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(
                    BookListMainActivity.this).inflate(R.layout.recycler_items,parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position){
            Book book=displayBooklist.get(position);
            holder.tv.setText(book.title);
            holder.iv.setImageResource(book.cover_id);
            holder.au.setText(book.author);
            holder.ti.setText(book.pubdate);
            holder.pb.setText(book.publisher);

            //Long press on a book to edit or delete
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mPosition = holder.getAdapterPosition();//将当前position赋值保存
                    return false;
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
}