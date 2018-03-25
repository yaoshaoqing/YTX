package ytx.app.Http.GetPost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by vi爱 on 2017/11/14.
 */

public class GetPostUtil {
    /**
     *get的方式请求
     */
    public static String Get(String path){
        //get的方式提交就是url拼接的方式
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            //获得结果码
            int responseCode = connection.getResponseCode();

            if(responseCode ==200){
                //请求成功 获得返回的流
                InputStream is = connection.getInputStream();
                return GetPostUtil.InputStream2String(is);
            }else {
                //请求失败
                return Integer.toString(responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String Post(String path,String str){
        //String path = "http://172.16.168.111:1010/login.php";
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            //数据准备
            //String data = "username="+username+"&password="+password;
            String data = str;
            //至少要设置的两个请求头
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", data.length()+"");

            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            //获得结果码
            int responseCode = connection.getResponseCode();
            if(responseCode ==200){
                //请求成功
                InputStream is = connection.getInputStream();
                return GetPostUtil.InputStream2String(is);
            }else {
                //请求失败
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String InputStream2String(InputStream in) {

//        try {
//            InputStreamReader input =  new InputStreamReader(in);
//            byte[] b=new byte[10];
//            input.read(b);
//            String str = new String(b);
//            input.close();
//            return str;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(in,"utf8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
