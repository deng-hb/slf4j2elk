package com.denghb.slf4j2elk.utils;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

/**
 * Created by denghb on 16/8/29.
 */
public class HttpUtils {

    /**
     * 加签验签
     *
     * @param url
     * @param body
     * @return
     */
    public static void send(String url, String body) {
        HttpURLConnection connection = null;
        try {
            connection = getHttpConnection(url);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(body);
            bw.flush();
            out.close();
            bw.close();//使用完关闭

            //------------字节流读取服务端返回的数据------------
            InputStream in = connection.getInputStream();
            if (null == in) {
                in = connection.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                buffer.append(str);
            }
            System.out.println(buffer);
            in.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();//使用完关闭TCP连接，释放资源
        }
    }


    private static HttpURLConnection getHttpConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(3000);// 3秒
        connection.setReadTimeout(60000);// 1分钟
        connection.setUseCaches(false);
        connection.setDoInput(true);//设置这个连接是否可以写入数据
        connection.setDoOutput(true);//设置这个连接是否可以输出数据
        connection.setRequestMethod("POST");//设置请求的方式
        // 请求头
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", "com.denghb.httputils");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Content-type", "application/json");

        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(getTrustAllSSLSocketFactory());
        }
        return connection;
    }

    private static SSLSocketFactory getTrustAllSSLSocketFactory() {
        // 信任所有证书
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            return sslContext.getSocketFactory();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

}