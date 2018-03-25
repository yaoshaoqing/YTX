package ytx.app.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import ytx.ytx.R;

import static ytx.app.Config.MyAppApiConfig.INTERFACE_URL;

public class PaymentActivity extends AppCompatActivity {

    protected RequestQueue mQueue;
    protected TextView cdep_date;
    protected TextView order_id;
    protected TextView people;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mQueue = Volley.newRequestQueue(this);
        setData();
    }
    protected void init(){
        this.cdep_date = findViewById(R.id.cdep_date);
        this.order_id = findViewById(R.id.order_id);
        this.people = findViewById(R.id.people);
    }
    protected void setData(){
        Intent intent = getIntent();
        final String camp_id = intent.getStringExtra("camp_id");
        final String buy_price = intent.getStringExtra("buy_price");
        final String buy_number = intent.getStringExtra("number");
        final String departure_id = intent.getStringExtra("departure_id");
        final String camp_date_title = intent.getStringExtra("camp_date_title");
        final String name = intent.getStringExtra("name");
        final String mobile = intent.getStringExtra("mobile");
        final String remark = intent.getStringExtra("remark");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,INTERFACE_URL+"camp_buy.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String string) {
                try {
                    JSONObject json = new JSONObject(string);
                    if(json.getString("status").equals("200")){
                        order_id.setText(json.getString("order_id"));
                        people.setText(json.getString("tourists").toString()+json.getString("parent").toString());
                        cdep_date.setText(camp_date_title);

                    }else{
                        System.out.println(string);
                        Toast.makeText(PaymentActivity.this,"操作异常",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PaymentActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                //Log.e("TAG", error.getMessage(), error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("sign_sn","1");
                map.put("camp_id",camp_id+"");
                map.put("uid","680");
                map.put("departure_id",departure_id+"");
                map.put("buy_name",name);
                map.put("buy_mobile",mobile);
                map.put("remark",remark);
                map.put("buy_num",buy_number+"");
                //map.put("Coupon_id",);
                return map;
            }
        };
        mQueue.add(stringRequest);
    }
}
