package ytx.app.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ytx.app.Public.Comm;
import ytx.app.R;

import static ytx.app.Config.MyAppApiConfig.INTERFACE_URL;

public class RegActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText username;
    private EditText code;
    private EditText password;
    private EditText conf_password;
    private Button getcode;
    protected RequestQueue mQueue;
    public String mobile;
    protected Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        mQueue = Volley.newRequestQueue(this);
        init();
    }
    protected void init(){
        this.username = findViewById(R.id.user_name);
        this.code = findViewById(R.id.code);
        this.password = findViewById(R.id.password);
        this.conf_password = findViewById(R.id.Conf_password);
        this.submit = findViewById(R.id.submit);
        Drawable username_drawable = getResources().getDrawable(R.drawable.yonghu);
        Drawable password_drawable = getResources().getDrawable(R.drawable.mima);
        Drawable code_drawable = getResources().getDrawable(R.drawable.code);
        //四个参数分别是设置图片的左、上、右、下的尺寸
        username_drawable.setBounds(0,0,40,40);
        password_drawable.setBounds(0,0,35,40);
        code_drawable.setBounds(0,0,40,27);
        //这个是选择将图片绘制在EditText的位置，参数对应的是：左、上、右、下
        username.setCompoundDrawables(username_drawable,null,null,null);
        password.setCompoundDrawables(password_drawable,null,null,null);
        conf_password.setCompoundDrawables(password_drawable,null,null,null);
        code.setCompoundDrawables(code_drawable,null,null,null);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.getcode:
                this.mobile = username.getText().toString();
                sms();
                break;
            case R.id.submit:
                reg();
                break;
        }
    }

    public void sms(){
        if(TextUtils.isEmpty(this.username.getText().toString())){
            Toast.makeText(this,"手机号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Comm.isMobile(this.username.getText().toString())){
            Toast.makeText(this,"手机号格式不正确",Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,INTERFACE_URL+"sms.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String string) {
                try {
                    JSONObject json = new JSONObject(string);
                    if(TextUtils.equals(json.getString("status"),"200")){
                        new Thread(){
                            @Override
                            public void run() {
                                int x=60;
                                while (0 <= x){
                                    Message msg = handler.obtainMessage();
                                    msg.arg1=x;
                                    x--;

                                    handler.sendMessage(msg);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }.start();
                    }else{
                        Toast.makeText(RegActivity.this,json.getString("r"),Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(RegActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RegActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                //Log.e("TAG", error.getMessage(), error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("mobile",mobile);
                map.put("type","1");
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    public void reg(){
        if(!Comm.isMobile(this.username.getText().toString())){
            Toast.makeText(this,"手机号格式不正确",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(this.password.getText().toString())){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(this.conf_password.getText().toString())){
            Toast.makeText(this,"确认密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.equals(this.conf_password.getText().toString(),this.password.getText().toString())){
            Toast.makeText(this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,INTERFACE_URL+"reg.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String string) {
                try {
                    JSONObject json = new JSONObject(string);
                    if(TextUtils.equals(json.getString("status"),"200")){
                        Intent intent = new Intent();
                        intent.setClass(RegActivity.this,LoginActivity.class);
                        startActivity(intent);
                        return;
                    }else{
                        Toast.makeText(RegActivity.this,json.getString("r"),Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(RegActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RegActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                //Log.e("TAG", error.getMessage(), error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("mobile",username.getText().toString());
                map.put("pass",conf_password.getText().toString());
                map.put("code",code.getText().toString());
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    protected Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1 > 0) {
                getcode.setText(msg.arg1+"秒");
                getcode.setEnabled(false);
                //getcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.onclick_code));
            }else{
                getcode.setText("获取验证码");
                getcode.setEnabled(true);
                //getcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.resource_code));
            }

        }
    };
}
