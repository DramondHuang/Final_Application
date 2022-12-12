package com.jnu.finalapplication;
import static android.content.Context.DOWNLOAD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class BookLoader {

    String download;
    Bitmap bitmap;
    Handler handler;

    public Book parsonJson(String json) {
        String cover="";
        String title = "", author = "", pubdate = "", translator = "", publisher = "", isbn = "", tag = "", bookshelf = "", note = "", coverurl = "";
        try {
            // 整个最大的JSON数组
            JSONObject jsonObjectALL = new JSONObject(json);
            // 通过标识，获取JSON对象data
            JSONObject jsonObject = jsonObjectALL.getJSONObject("data");

            title = jsonObject.optString("title");
            author = jsonObject.optString("author");
            translator = jsonObject.optString("translator");
            publisher = jsonObject.optString("publisher");
            pubdate = jsonObject.optString("pubdate");
            isbn = jsonObject.optString("isbn");
            tag = "";
            bookshelf = "";
            note = "";
            cover = jsonObject.optString("img");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Book(title, cover, author, pubdate, translator, publisher, isbn, tag, bookshelf, note);
    }

    public String downloadjson(String path) throws IOException, InterruptedException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String tempLine = null;
                StringBuffer resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
                download = resultBuffer.toString();
            }
        } catch (Exception exception) {
        }
        return download;
    }

    public void setCover(String path,ImageView imageView) {
        handler = new Handler() {
            public void handleMessage(@NonNull Message msg) {
                //主线程接受子线程发送的信息
                Bitmap cover=(Bitmap) msg.obj;
                imageView.setImageBitmap(cover);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        InputStream inputStream = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        //使用Handler向主线程发送消息
                        Message msg = new Message();
                        msg.obj = bitmap;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
}


