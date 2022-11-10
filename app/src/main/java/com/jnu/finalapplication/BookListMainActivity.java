package com.jnu.finalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import java.util.List;


public class BookListMainActivity extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private RecyclerAdapter myAdapter;
    private int mPosition;

    ArrayList<Book> Booklist=new ArrayList<Book>();
    Book Book1= new Book("软件项目管理案例教程（第4版）", R.drawable.book_2,"hzj","2002-06","translator","publisher","ISBN123");
    Book Book2= new Book("创新工程实践", R.drawable.book_no_name,"hzj","2002-06","translator","publisher","ISBN123");
    Book Book3= new Book("信息安全数学基础（第2版）", R.drawable.book_1,"hzj","2002-06","translator","publisher","ISBN123");

    public int getPosition() {

        return mPosition;

    }
    public class Book{
        int cover_id;
        String title,author,pubdate,translator,publisher,isbn;

        public Book(String t,int id,String au,String ti,String tran,String pub, String is){
            title=t;
            cover_id=id;
            author=au;
            pubdate=ti;
            translator=tran;
            publisher=pub;
            isbn=is;
        }

        public int getCoverResource(){
            return cover_id;
        }

        public String getTitle(){
            return title;
        }
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

            switch (resultCode) {
                case 1:         // add new book
                    Book addedbook = new Book(title, R.drawable.book_no_name, author, pubdate,translator,publisher,isbn);
                    Booklist.add(addedbook);
                    myAdapter.notifyDataSetChanged();
                    break;

                case 2:         // edit existed book
                    Booklist.get(mPosition).title = title;
                    Booklist.get(mPosition).pubdate=pubdate;
                    Booklist.get(mPosition).publisher=publisher;
                    Booklist.get(mPosition).isbn=isbn;
                    Booklist.get(mPosition).translator=translator;
                    Booklist.get(mPosition).author=author;
                    myAdapter.notifyDataSetChanged();
                default:
                    //其它窗口的回传数据
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);
        initData();
        myRecyclerView=(RecyclerView) findViewById(R.id.recycle_view_books);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView.setAdapter(myAdapter=new RecyclerAdapter());

        registerForContextMenu(findViewById(R.id.recycle_view_books));
    }
    protected void initData(){

        Booklist.add(Book1);
        Booklist.add(Book2);
        Booklist.add(Book3);
    }
    //For add button
    public void Add(View view){
        Intent intent=new Intent(this,EditBookActivity.class);
        Toast.makeText(this,"add",Toast.LENGTH_SHORT).show();
        intent.putExtra("Action","Add");
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
//            case R.id.add:
//                Intent intent=new Intent(this,EditBookActivity.class);
//                Toast.makeText(this,"add",Toast.LENGTH_SHORT).show();
//                intent.putExtra("Action","Add");
//                startActivityForResult(intent,0);
//                break;

            case R.id.delete:
                Toast.makeText(this,"delete",Toast.LENGTH_SHORT).show();
                Booklist.remove(mPosition);
                myAdapter.notifyDataSetChanged();
                break;

            case R.id.edit:
                Intent intent2=new Intent(this,EditBookActivity.class);
                Toast.makeText(this,"Edit",Toast.LENGTH_SHORT).show();
                intent2.putExtra("Action","Edit");
                intent2.putExtra("title",Booklist.get(mPosition).title);
                intent2.putExtra("author",Booklist.get(mPosition).author);
                intent2.putExtra("translator",Booklist.get(mPosition).translator);
                intent2.putExtra("pubdate",Booklist.get(mPosition).pubdate);
                intent2.putExtra("publisher",Booklist.get(mPosition).publisher);
                intent2.putExtra("isbn",Booklist.get(mPosition).isbn);
                intent2.putExtra("picture",Booklist.get(mPosition).cover_id);
                startActivityForResult(intent2,0);
                break;
        }
        return super.onContextItemSelected(item);
    }


    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
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
            holder.tv.setText(Booklist.get(position).title);
            holder.iv.setImageResource(Booklist.get(position).cover_id);
            holder.au.setText(Booklist.get(position).author);
            holder.ti.setText(Booklist.get(position).pubdate);

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
            return Booklist.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv,au,ti;
            ImageView iv;

            public MyViewHolder(View inflate) {
                super(inflate);
                tv=(TextView) inflate.findViewById(R.id.text_view_book_title);
                iv=(ImageView) inflate.findViewById(R.id.image_view_book_cover);
                au=(TextView) inflate.findViewById(R.id.text_view_book_author);
                ti=(TextView) inflate.findViewById(R.id.text_view_book_time);
            }
        }
    }
}