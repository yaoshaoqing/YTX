package ytx.app.main;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vi爱 on 2018/3/30.
 */

public abstract class IsLogin {
    public int login_type;
    public String sign_sn = null;
    public int uid;

    public static Boolean getLogin_type(Context context) {
        SharedPreferences sp = context.getSharedPreferences("login",0);
        if(sp.getInt("login_type",0) == 1){
            return true;
        }else{
            return false;
        }
    }
    public static String getSign_sn(Context context){
        SharedPreferences sp = context.getSharedPreferences("login",0);
        return sp.getString("sign_sn",null);
    }
    public static Map<String,String>  getone(Context context){
        SharedPreferences sp = context.getSharedPreferences("login",0);
        Map<String, String> map = new HashMap<>();
        map.put("uid",sp.getInt("uid",0)+"");
        map.put("sign_sn",sp.getString("sign_sn",null));
        map.put("login_type",sp.getInt("login_type",0)+"");
        map.put("mobile",sp.getString("mobile",null));
        return map;
    }
}
