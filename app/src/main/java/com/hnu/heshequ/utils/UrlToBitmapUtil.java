package com.hnu.heshequ.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;



public class UrlToBitmapUtil {

    private CompleteListener listener;

    private static volatile UrlToBitmapUtil instance;

    private UrlToBitmapUtil() {
    }

    public static UrlToBitmapUtil getInstance() {
        if (instance == null) {
            synchronized (UrlToBitmapUtil.class) {
                if (instance == null) {
                    instance = new UrlToBitmapUtil();
                }
            }
        }
        return instance;
    }

    public void getBitMap(final String imgurl, final CompleteListener listener) {
        this.listener = listener;
        new Thread(new Runnable() {
            Bitmap bitmap = null;

            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(imgurl);
                    InputStream is = null;
                    BufferedInputStream bis = null;
                    try {
                        is = url.openConnection().getInputStream();
                        bis = new BufferedInputStream(is);
                        bitmap = BitmapFactory.decodeStream(bis);
                        listener.onComplete(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    listener.onFailure();
                }
            }
        }).start();
    }

    public interface CompleteListener {
        void onComplete(Bitmap bitmap);

        void onFailure();
    }
}
