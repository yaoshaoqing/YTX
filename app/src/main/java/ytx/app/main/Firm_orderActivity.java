package ytx.app.main;
import ytx.app.R;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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
        this.submit.setOnClickListener(this);
        this.phone.setOnClickListener(this);
        this.jia.setOnClickListener(this);
        this.jian.setOnClickListener(this);
    }
    /**
     * 给页面赋值
     */
    protected void setData(){
        Intent intent = getIntent();
        this.camp_id = Integer.parseInt(intent.getStringExtra("camp_id"));//产品ID
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
                Intent intent = new Intent();
                intent.putExtra("buy_price",this.buy_price);
                intent.putExtra("camp_id",this.camp_id);
                intent.putExtra("number",this.buy_number);
                intent.putExtra("departure_id",this.departure_id);
                intent.putExtra("camp_date_title",camp_date.getText().toString());
                intent.putExtra("departure_id",this.departure_id);
                intent.putExtra("mobile",this.mobile.getText().toString());
                intent.putExtra("name",this.name.getText().toString());
                intent.putExtra("remark",this.remark.getText().toString());
                intent.setClass(this,PaymentActivity.class);
                startActivity(intent);
                break;
            case R.id.jia:
                int get_jia_num = Integer.parseInt((String) this.number.getText());
                get_jia_num +=1;
                this.number.setText(get_jia_num+"");
                this.total.setText("￥"+Integer.parseInt(this.price.getText().toString().substring(1))*get_jia_num);
                this.button_total.setText("￥"+Integer.parseInt(this.price.getText().toString().substring(1))*get_jia_num);
                this.buy_number = get_jia_num;
                this.buy_price = this.price.getText().toString().substring(1);
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
                    this.buy_price = this.price.getText().toString().substring(1);
                }
                break;
        }
    }

}
