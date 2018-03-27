package ytx.app.Login;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import ytx.app.R;
public class ForgetActivity extends AppCompatActivity {
    private EditText username;
    private EditText code;
    private EditText password;
    private EditText conf_password;
    private Button getcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        username = findViewById(R.id.user_name);
        code = findViewById(R.id.code);
        password = findViewById(R.id.password);
        conf_password = findViewById(R.id.Conf_password);
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
}
