package ytx.app.main;
import ytx.app.Public.Comm;
import ytx.app.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import static ytx.app.Config.MyAppApiConfig.INTERFACE_URL;

public class Firm_orderActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView phone;
    private Button submit;
    private int camp_id;
    private int departure_id;
    private TextView title;
    private TextView camp_date;
    private TextView price;
    private TextView total;
    private TextView number;
    private ImageView jian;
    private ImageView jia;
    private TextView button_total;
    private String buy_price;
    private int buy_number;
    protected RequestQueue mQueue;
    private TextView name;
    private TextView mobile;
    private TextView remark;
    private TextView Coupon_id;
    private String camp_date_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_order);
        mQueue = Volley.newRequestQueue(this);
        init();
        setData();
    }
    public void init(){
        this.camp_date = findViewById(R.id.camp_date);
        this.title = findViewById(R.id.title);
        this.price = findViewById(R.id.price);
        this.total = findViewById(R.id.total);
        this.number = findViewById(R.id.number);
        this.jian = findViewById(R.id.jian);
        this.jia = findViewById(R.id.jia);
        this.phone = findViewById(R.id.phone);
        this.submit = findViewById(R.id.submit);
        this.button_total = findViewById(R.id.button_total);
        this.name = findViewById(R.id.name);
        this.mobile = findViewById(R.id.mobile);
        this.remark = findViewById(R.id.remark);
        this.mobile.setText(IsLogin.getone(this).get("mobile"));
        this.submit.setOnClickListener(this);
        this.phone.setOnClickListener(this);
        this.jia.setOnClickListener(this);
        this.jian.setOnClickListener(this);
        if(!IsLogin.getLogin_type(this)){
            return;
        }
    }
    /**
     * 给页面赋值
     */
    protected void setData(){
        Intent intent = getIntent();
        this.buy_number = Integer.parseInt(this.number.getText().toString());
        this.buy_price = intent.getStringExtra("price").substring(1);
        this.camp_id = Integer.parseInt(intent.getStringExtra("camp_id"));//产品ID
        this.camp_date_title = intent.getStringExtra("camp_date_title");
        this.departure_id = Integer.parseInt(intent.getStringExtra("departure_id"));
        this.title.setText(intent.getStringExtra("title"));
        this.camp_date.setText(intent.getStringExtra("camp_date_title"));
        this.price.setText(intent.getStringExtra("price"));
        this.total.setText(intent.getStringExtra("price"));
        this.button_total.setText(intent.getStringExtra("price"));
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.phone:
                break;
            case R.id.submit://跳转到支付页面
                redirection();
                break;
            case R.id.jia:
                int get_jia_num = Integer.parseInt((String) this.number.getText());
                get_jia_num +=1;
                this.number.setText(get_jia_num+"");
                this.total.setText("￥"+Integer.parseInt(this.price.getText().toString().substring(1))*get_jia_num);
                this.button_total.setText("￥"+Integer.parseInt(this.price.getText().toString().substring(1))*get_jia_num);
                this.buy_number = get_jia_num;
                //this.buy_price = this.price.getText().toString().substring(1);
                break;
            case R.id.jian:
                int get_jian_num;
                get_jian_num = Integer.parseInt((String) this.number.getText());
                if(get_jian_num > 1){
                    get_jian_num -=1;
                    this.number.setText(get_jian_num+"");
                    this.total.setText("￥"+Integer.parseInt(this.price.getText().toString().substring(1))*get_jian_num);
                    this.button_total.setText("￥"+Integer.parseInt(this.price.getText().toString().substring(1))*get_jian_num);
                    this.buy_number = get_jian_num;
                    //this.buy_price = this.price.getText().toString().substring(1);
                }
                break;
        }
    }
    /**
     *赋值\重定向
     */
    public void redirection(){
        if(TextUtils.isEmpty(this.name.getText().toString())){
            Toast.makeText(this,"联系人姓名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(this.mobile.getText().toString())){
            Toast.makeText(this,"联系人手机号不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Comm.isMobile(this.mobile.getText().toString())){
            Toast.makeText(this,"手机号格式不正确",Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,INTERFACE_URL+"camp_buy.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String string) {
                try {
                    JSONObject json = new JSONObject(string);
                    if(TextUtils.equals(json.getString("status"),"200")){
                        JSONObject jsonObject = json.getJSONObject("data");
                        Intent intent = new Intent();
                        intent.putExtra("orderid",jsonObject.getString("orderid"));
                        intent.putExtra("people",Integer.parseInt(jsonObject.getString("tourists").toString())+Integer.parseInt(jsonObject.getString("parent").toString()));
                        intent.putExtra("cdep_date",camp_date_title);
                        intent.putExtra("buy_price",buy_price);
                        intent.putExtra("buy_number",buy_number);
                        intent.setClass(Firm_orderActivity.this,PaymentActivity.class);
                        startActivity(intent);
                        return;
                    }else{
                        Toast.makeText(Firm_orderActivity.this,json.getString("r"),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(Firm_orderActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 0);
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(Firm_orderActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Firm_orderActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                //Log.e("TAG", error.getMessage(), error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("sign_sn",IsLogin.getone(Firm_orderActivity.this).get("sign_sn"));
                map.put("camp_id",camp_id+"");
                map.put("uid",IsLogin.getone(Firm_orderActivity.this).get("uid"));
                map.put("departure_id",departure_id+"");
                map.put("buy_name",name.getText().toString());
                map.put("buy_mobile",mobile.getText().toString());
                map.put("remark",remark.getText().toString());
                map.put("buy_num",buy_number+"");
                //map.put("Coupon_id",);
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    /**
     *跳转回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == 200){
            redirection();
        }
    }


}
