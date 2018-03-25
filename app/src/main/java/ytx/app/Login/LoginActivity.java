package ytx.app.Login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ytx.ytx.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Button login = findViewById(R.id.login);
        TextView forget = findViewById(R.id.forget);
        TextView reg = findViewById(R.id.reg);
        username = findViewById(R.id.user_name);
        password = findViewById(R.id.user_psw);
        login.setOnClickListener(this);
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
        int id = v.getId();
        Intent intent = new Intent();
        switch (id){
            case R.id.login:
                String name = username.getText().toString();
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
}
