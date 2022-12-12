package com.jnu.finalapplication;

import java.io.Serializable;

public class Book implements Serializable {
    String cover,title,author,pubdate,translator,publisher,isbn,tag,bookshelf,note;

    public Book(String t,String co,String au,String ti,String tran,String pub, String is,String ta,String shelf,String n){
        title=t;
        cover=co;
        author=au;
        pubdate=ti;
        translator=tran;
        publisher=pub;
        isbn=is;
        tag=ta;
        bookshelf=shelf;
        note=n;
    }
}
