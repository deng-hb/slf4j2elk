package com.denghb.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

/**
 * Created by denghb on 16/8/29.
 */
public class AppHttpClient {

    /**
     * 加签验签
     *
     * @param url
     * @param parameters
     * @return
     */
    public void post(String url, TreeMap<String, Object> parameters) {
        if (null == parameters) {
            parameters = new TreeMap<>();
        }
        execute(url, "POST", parameters);
    }

    private void execute(String url, String method, Map<String, Object> parameters) {
        long startTime = System.currentTimeMillis();
        HttpURLConnection connection = null;
        DataOutputStream output = null;
        try {
            connection = getHttpConnection(url, method, parameters);
            if (null != parameters) {
                // 判断是否是文件流
                boolean isMultipart = false;
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    Object object = entry.getValue();
                    if (object instanceof Map || object instanceof List) {
                        isMultipart = true;
                        break;
                    }
                }
                if (isMultipart) {
                    String boundary = "AppHttpClinet-denghb-com";
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    output = new DataOutputStream(connection.getOutputStream());
                    boundary = "--" + boundary;
                    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (value instanceof List) {
                            List<Map<String, Object>> list = (List<Map<String, Object>>) value;
                            for (Map<String, Object> map : list) {
                                appendData(map, output, boundary, key);
                            }
                        } else if (value instanceof Map) {
                            Map<String, Object> map = (Map) value;
                            appendData(map, output, boundary, key);
                        } else {
                            StringBuffer sb = new StringBuffer();
                            sb.append(boundary);
                            sb.append("\r\nContent-Disposition: form-data; name=\"");
                            sb.append(key);
                            sb.append("\"\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Transfer-Encoding: 8bit\r\n\r\n");
                            sb.append(value);
                            sb.append("\r\n");
                            output.writeBytes(sb.toString());
                        }
                    }
                    output.writeBytes(boundary + "--\r\n");// 数据结束标志
                    output.flush();
                } else {
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    output = new DataOutputStream(connection.getOutputStream());
                    // 纯文本
                    StringBuffer sb = new StringBuffer();
                    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                        sb.append(entry.getKey());
                        sb.append("=");
                        sb.append(URLEncoder.encode((String) entry.getValue()));
                        sb.append("&");
                    }
                    output.writeBytes(sb.toString());
                    output.flush();
                }
            }
            connection.connect();
            InputStream inputStream = null;
            if (null != connection.getContentEncoding()) {
                String encode = connection.getContentEncoding().toLowerCase();
                if (null != encode && encode.indexOf("gzip") >= 0) {
                    inputStream = new GZIPInputStream(connection.getInputStream());
                }
            }
            if (null == inputStream) {
                inputStream = connection.getInputStream();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            // 同步的
            long ms = (System.currentTimeMillis() - startTime);// 请求毫秒
            System.out.println(String.format("\n#request:%s\n#time:%sms\n#parameters:%s\n#response:%s", url, ms, parameters, builder.toString()));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                connection.disconnect();
                connection = null;
            }
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private HttpURLConnection getHttpConnection(String urlString, String method, Map<String, Object> parameters) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(3000);// 3秒
        connection.setReadTimeout(60000);// 1分钟
        if (!"DOWNLOAD".equalsIgnoreCase(method)) {
            connection.setDoOutput(true);
            connection.setDoInput(true);
        }
        connection.setUseCaches(false);
        // 请求头
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", "riches/test 1.0");
        connection.setRequestProperty("Charset", "UTF-8");
        if ("POST".equalsIgnoreCase(method)) {
            connection.setRequestMethod("POST");
        }

        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(getTrustAllSSLSocketFactory());
        }
        return connection;
    }


    private void appendData(Map<String, Object> map, DataOutputStream output, String boundary, String name) throws Exception {
        try {
            byte[] bytes = (byte[]) map.get("file_data");
            String fileName = (String) map.get("file_name");
            if (null == bytes) {
                String filePath = (String) map.get("file_path");
                File file = new File(filePath);
                FileInputStream is = new FileInputStream(file);
                bytes = new byte[is.available()];
                is.read(bytes);
                is.close();
                if (null == fileName) {
                    fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                }
            }
            StringBuilder split = new StringBuilder();
            split.append(boundary);
            split.append(String.format("\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"", name, fileName));
            split.append("\r\nContent-Type: application/octet-stream\r\nContent-Transfer-Encoding: binary\r\n\r\n");
            output.writeBytes(split.toString());
            output.write(bytes);
            output.writeBytes("\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SSLSocketFactory getTrustAllSSLSocketFactory() {
        // 信任所有证书
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
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

    private static Map<String, String> getHttpResponseHeader(HttpURLConnection http) {
        Map<String, String> header = new LinkedHashMap<String, String>();
        for (int i = 0; ; i++) {
            String mine = http.getHeaderField(i);
            if (mine == null)
                break;
            header.put(http.getHeaderFieldKey(i), mine);
        }
        return header;
    }
}