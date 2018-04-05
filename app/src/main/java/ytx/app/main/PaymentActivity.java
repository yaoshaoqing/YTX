package ytx.app.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.Volley;
import ytx.app.R;

public class PaymentActivity extends AppCompatActivity implements OnClickListener {

    protected RequestQueue mQueue;
    protected TextView cdep_date;
    protected TextView orderid;
    protected TextView people;
    protected TextView money;
    protected TextView price;
    private RelativeLayout wxpay;
    private RelativeLayout alipay;
    private RelativeLayout payment;
    protected ImageView wxpay_img;
    protected ImageView alipay_img;
    protected ImageView payment_img;
    protected TextView payType;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mQueue = Volley.newRequestQueue(this);
        init();
        setData();
    }
    protected void init(){
        this.cdep_date = findViewById(R.id.cdep_date);
        this.orderid = findViewById(R.id.order_id);
        this.people = findViewById(R.id.people);
        this.money = findViewById(R.id.money);
        this.price = findViewById(R.id.buy_price);
        this.wxpay = findViewById(R.id.wxpay);
        this.alipay = findViewById(R.id.alipay);
        this.payment = findViewById(R.id.payment);
        this.wxpay_img = findViewById(R.id.wxpay_img);
        this.alipay_img = findViewById(R.id.alipay_img);
        this.payment_img = findViewById(R.id.payment_img);
        this.payType = findViewById(R.id.pay_type);
        this.alipay.setOnClickListener(this);
        this.wxpay.setOnClickListener(this);
        this.payment.setOnClickListener(this);
        if(!IsLogin.getLogin_type(this)){
            return;
        }
    }
    protected void setData(){
        Intent intent = getIntent();
        this.orderid.setText(intent.getStringExtra("orderid").toString());
        this.people.setText(intent.getIntExtra("people",0)+"人");
        this.cdep_date.setText(intent.getStringExtra("cdep_date").toString());
        String price = intent.getStringExtra("buy_price");
        int num = intent.getIntExtra("buy_number",0);
        this.total = Integer.parseInt(price)*num;
        this.price.setText("￥"+this.total);
        this.money.setText("￥"+this.total);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Bitmap paymentOff = BitmapFactory.decodeResource(getResources(),R.drawable.payment0 );
        Bitmap paymentOn = BitmapFactory.decodeResource(getResources(),R.drawable.payment1 );
        this.wxpay_img.setImageBitmap(paymentOff);
        this.alipay_img.setImageBitmap(paymentOff);
        this.payment_img.setImageBitmap(paymentOff);
        String payTypeValue = "";
        switch (id){
            case R.id.wxpay:
                this.wxpay_img.setImageBitmap(paymentOn);
                payTypeValue = "1";
                break;
            case R.id.alipay:
                this.alipay_img.setImageBitmap(paymentOn);
                payTypeValue = "2";
                break;
            case R.id.payment:
                this.payment_img.setImageBitmap(paymentOn);
                payTypeValue = "1";
                break;
        }
        this.payType.setText(payTypeValue);
    }

}
