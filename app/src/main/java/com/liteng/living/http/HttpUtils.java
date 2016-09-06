package com.liteng.living.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by liteng on 16/9/6.
 */
public class HttpUtils {

    private static final int TIMEOUT_MS = 15000;


    private HttpUtils() {
    }

    private final static class Inner {
        static HttpUtils mHttpUtils = new HttpUtils();
    }

    public static HttpUtils getInstance() {
        return Inner.mHttpUtils;
    }


    public String get(String requestUrl) throws IOException {
        StringBuilder builder = new StringBuilder();
        //1.准备URL
        URL url = new URL(requestUrl);
        //2.获取HttpURLConnection 对象
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //3.设置请求方式和超时时间等
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        //设置请求方式为GET请求
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        //4.获取到请求返回的输入流
        InputStream inputStream = connection.getInputStream();
        //5.读取输入流里的具体数据
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            builder.append(new String(buffer, 0, len));
        }
        //6.关闭连接
        connection.disconnect();
        return builder.toString();
    }


    private HttpURLConnection openConnection(String url) throws IOException {
        //获得URL对象
        URL parseURL = new URL(url);
        //获得HttpURLConnection对象
        HttpURLConnection connection = (HttpURLConnection) parseURL.openConnection();
        //设置连接超时
        connection.setConnectTimeout(TIMEOUT_MS);
        //设置读取超时时间
        connection.setReadTimeout(TIMEOUT_MS);
        //不使用缓存
        connection.setUseCaches(false);
        //设置是否从httpUrlConnection读入，默认情况下是true;
        connection.setDoInput(true);
        //设置httpUrlConnection是否可以输出数据，默认情况下是false;
        connection.setDoOutput(true);
        return connection;
    }
}
