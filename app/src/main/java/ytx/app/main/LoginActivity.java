package ytx.app.main;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ytx.app.Helper.Helper;
import ytx.app.R;

import static ytx.app.Config.MyAppApiConfig.INTERFACE_URL;
import static ytx.app.Config.MyAppApiConfig.SQLITE_VERSION;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private Button button;
    private TextView forget;
    private TextView reg;
    protected RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mQueue = Volley.newRequestQueue(this);
        init();
    }

    private void init(){
        button = findViewById(R.id.button);
        forget = findViewById(R.id.forget);
        reg = findViewById(R.id.reg);
        username = findViewById(R.id.user_name);
        password = findViewById(R.id.user_psw);
        button.setOnClickListener(this);
        forget.setOnClickListener(this);
        reg.setOnClickListener(this);
        Drawable username_drawable = getResources().getDrawable(R.drawable.yonghu);
        Drawable password_drawable = getResources().getDrawable(R.drawable.mima);
        //四个参数分别是设置图片的左、上、右、下的尺寸
        username_drawable.setBounds(0,0,40,40);
        password_drawable.setBounds(0,0,35,40);
        //这个是选择将图片绘制在EditText的位置，参数对应的是：左、上、右、下
        username.setCompoundDrawables(username_drawable,null,null,null);
        password.setCompoundDrawables(password_drawable,null,null,null);
    }
    @Override
    public void onClick(View v) {
        final int id = v.getId();
        final Intent intent = new Intent();
        switch (id){
            case R.id.button:
                final String name = username.getText().toString();
                final String pass = password.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isMobile(name)){
                    Toast.makeText(this,"手机号格式不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST,INTERFACE_URL+"login.php",new Response.Listener<String>(){
                    @Override
                    public void onResponse(String string) {
                        try {
                            isLogin(string);
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Log.e("TAG", error.getMessage(), error);
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("mobile",name );
                        map.put("pass",pass);
                        return map;
                    }
                };
                mQueue.add(stringRequest);

                break;
            case R.id.forget:
                intent.setClass(this,ForgetActivity.class);
                startActivity(intent);
                break;
            case R.id.reg:
                intent.setClass(this,RegActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    private void isLogin(String string){
        JSONObject json = null;
        try {
            json = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(TextUtils.equals(json.getString("status").toString(),"200")){
                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER );
                progressDialog.show();
                JSONObject jsonObject = json.getJSONObject("data");
                ContentValues val = new ContentValues();
                val.put("uid",jsonObject.getString("uid"));
                val.put("nickname",jsonObject.getString("nickname"));
                val.put("name",jsonObject.getString("name"));
                val.put("telephone",jsonObject.getString("telephone"));
                val.put("mobile",jsonObject.getString("mobile"));
                val.put("remark",jsonObject.getString("remark"));
                val.put("sign_sn",jsonObject.getString("sign_sn"));
                val.put("login_type",1);
                val.put("msg_unread_num",jsonObject.getString("msg_unread_num"));
                Helper helper = new Helper(LoginActivity.this,"ytx",null,SQLITE_VERSION);
                helper.LoginType(val,jsonObject.getString("uid").toString());
                SharedPreferences sp = this.getSharedPreferences("login",0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("uid", Integer.parseInt(jsonObject.getString("uid")));
                editor.putInt("login_type",1);
                editor.putString("sign_sn",jsonObject.getString("sign_sn"));
                editor.putString("mobile",jsonObject.getString("mobile"));
                editor.commit();
                progressDialog.dismiss();
                Intent intent  = new Intent();
                setResult(200, intent);
                finish();
            }else{
                Toast.makeText(LoginActivity.this,json.getString("r").toString(),Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
