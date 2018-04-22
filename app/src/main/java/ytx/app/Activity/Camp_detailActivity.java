package ytx.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import ytx.app.*;
import static ytx.app.Config.MyAppApiConfig.INTERFACE_URL;

public class Camp_detailActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout enroll;
    protected TextView title;
    protected TextView camp_location;
    protected TextView desc;
    protected TextView ages;
    protected TextView camp_type;
    protected TextView price;
    protected WebView content;
    protected StringBuilder builder;
    protected WebView application_notes;
    protected WebView cost_description;
    protected WebView notes;
    protected WebView schedule;
    private RelativeLayout camp_date;
    protected ImageView imageView;
    protected RequestQueue mQueue;
    protected String[] camp_date_id;
    protected String[] camp_date_text;
    protected TextView select_camp_date_text;
    protected String select_camp_date_id = "";
    protected String campid = "";
    protected String camp_title = "";
    protected String camp_date_title = "";
    protected String camp_date_price = "";
    protected Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_detail);
        init();
        this.enroll = findViewById(R.id.enroll);
        this.enroll.setOnClickListener(this);
        this.camp_date.setOnClickListener(this);
        mQueue = Volley.newRequestQueue(this);
        setData();
        setFace();
    }

    /**
     * 获取对像
     */
    private void init(){
        this.title = findViewById(R.id.title);
        this.desc = findViewById(R.id.desc);
        this.ages = findViewById(R.id.ages);
        this.camp_location = findViewById(R.id.camp_lcation);
        this.camp_type = findViewById(R.id.camp_type);
        this.price = findViewById(R.id.price);
        this.content = findViewById(R.id.content);
        builder = new StringBuilder();
        this.application_notes = findViewById(R.id.application_notes);
        this.cost_description = findViewById(R.id.cost_description);
        this.notes = findViewById(R.id.notes);
        this.schedule = findViewById(R.id.schedule);
        this.camp_date = findViewById(R.id.camp_date);
        this.imageView = findViewById(R.id.Img);
        this.select_camp_date_text = findViewById(R.id.select_camp_date_text);
    }

    /**
     * 给页面赋值
     */
    private void setData(){

        //接收产品ID
        Intent intent = getIntent();
        campid = intent.getStringExtra("campid");
        camp_title = intent.getStringExtra("title");
//        new AsyncTask<Void,String,String>(){
//            ProgressDialog progressDialog;
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                progressDialog = new ProgressDialog(Camp_detailActivity.this);
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER );
//                //progressDialog.setMax(10);
//                progressDialog.show();
//            }
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                String data = null;
//                String post = "camp_id="+campid;
//                data = GetPostUtil.Post(INTERFACE_URL+"camp_detail.php",post);
//                return data;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                try {
//                    String string = s;
//                    JSONObject json = new JSONObject(string);
//                    if(json.getString("status").equals("200")){
//                        JSONObject jsonObject = json.getJSONObject("data");
//                        Camp_detailActivity.this.title.setText(title);
//                        //                        builder.append("<html>");
////                        builder.append("<head>");
////                        builder.append("</head>");
////                        builder.append("<body>");
////                        builder.append(jsonObject.getString("content"));
////                        builder.append("</body>");
////                        builder.append("</html>");
//                        //webView.loadDataWithBaseURL(null, builder.toString(), "text/html" , "utf-8", null);
//                        camp_location.setText(jsonObject.getString("camp_location"));
//                        camp_type.setText(jsonObject.getString("camp_type"));
//                        ages.setText(jsonObject.getString("ages"));
//                        price.setText(jsonObject.getString("ncost"));
//                        content.loadData(getHtmlData(jsonObject.getString("content")), "text/html; charset=UTF-8", null);
//                        application_notes.loadData(getHtmlData(jsonObject.getString("application_notes")),"text/html; charset=UTF-8", null);
//                        cost_description.loadData(getHtmlData(jsonObject.getString("cost_description")),"text/html; charset=UTF-8", null);
//                        notes.loadData(getHtmlData(jsonObject.getString("notes")),"text/html; charset=UTF-8", null);
//                        JSONArray jsonArray = jsonObject.getJSONArray("camp_schedule");
//                        Picasso.with(Camp_detailActivity.this).load(jsonObject.getString("cover")).placeholder(R.mipmap.ic_launcher).into(imageView);
//                        String camp_schedule = "";
//                        for(int i=0;i<=jsonArray.length();i++){
//                            JSONObject scheduleObject = jsonArray.getJSONObject(i);
//                            camp_schedule += scheduleObject.getString("name");
//                            camp_schedule += scheduleObject.getString("content");
//                            schedule.loadData(getHtmlData(camp_schedule),"text/html; charset=UTF-8", null);
//                        }
//                    }else{
//                        Toast.makeText(Camp_detailActivity.this,"操作异常",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                } catch (Exception e) {
//                    System.out.print("123");
//                }
//                progressDialog.dismiss();
//            }
//        }.execute();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,INTERFACE_URL+"camp_detail.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String string) {
                try {
                    JSONObject json = new JSONObject(string);
                    if(json.getString("status").equals("200")){
                        JSONObject jsonObject = json.getJSONObject("data");
                        Camp_detailActivity.this.title.setText(camp_title);
                        camp_location.setText(jsonObject.getString("camp_location"));
                        camp_type.setText(jsonObject.getString("camp_type"));
                        ages.setText(jsonObject.getString("ages"));
                        price.setText(jsonObject.getString("ncost"));
                        content.loadData(getHtmlData(jsonObject.getString("content")), "text/html; charset=UTF-8", null);
                        application_notes.loadData(getHtmlData(jsonObject.getString("application_notes")),"text/html; charset=UTF-8", null);
                        cost_description.loadData(getHtmlData(jsonObject.getString("cost_description")),"text/html; charset=UTF-8", null);
                        notes.loadData(getHtmlData(jsonObject.getString("notes")),"text/html; charset=UTF-8", null);
                        Picasso.with(Camp_detailActivity.this).load(jsonObject.getString("cover")).fit().into(imageView);
                        String camp_schedule = "";
                        camp_date_id = new String[jsonObject.getJSONArray("camp_date").length()];
                        camp_date_text = new String[jsonObject.getJSONArray("camp_date").length()];
                        for(int x=0;x<jsonObject.getJSONArray("camp_date").length();x++){
                            JSONObject camp_date_Object = jsonObject.getJSONArray("camp_date").getJSONObject(x);
                            camp_date_id[x] = camp_date_Object.getString("id");
                            camp_date_text[x] = camp_date_Object.getString("title")+camp_date_Object.getString("price");
                        }
                        JSONArray jsonArray = jsonObject.getJSONArray("camp_schedule");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject scheduleObject = jsonArray.getJSONObject(i);
                            camp_schedule += scheduleObject.getString("name");
                            camp_schedule += scheduleObject.getString("content");
                            schedule.loadData(getHtmlData(camp_schedule),"text/html; charset=UTF-8", null);
                        }

                    }else{
                        Toast.makeText(Camp_detailActivity.this,"操作异常",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Camp_detailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                //Log.e("TAG", error.getMessage(), error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("camp_id", campid);
                map.put("uid",IsLogin.getone(Camp_detailActivity.this).get("uid"));
                map.put("sign_sn",IsLogin.getone(Camp_detailActivity.this).get("sign_sn"));
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id) {
            case R.id.enroll:
                if("".equals(select_camp_date_id) ||  "".equals(camp_date_title) || "".equals(select_camp_date_id)){
                    Toast.makeText(this,"请选择出发期数",Toast.LENGTH_SHORT).show();
                    return;
                }
                if("".equals(campid)){
                    Toast.makeText(this,"操作异常",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!IsLogin.getLogin_type(this)){
                    Intent intent = new Intent();
                    intent.setClass(this, LoginActivity.class);
                    startActivityForResult(intent, 0);
                }else {
                    redirection();
                }

                break;
            case R.id.camp_date:
                //System.out.println(camp_date_text[1]);
                final String items[] = {"我是Item一", "我是Item二", "我是Item三", "我是Item四","我是Item5", "我是Item6", "我是Item7", "我是Item8"};
                AlertDialog dialog = new AlertDialog.Builder(this)
                        //.setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle("请选择日期")//设置对话框的标题
                        .setItems(camp_date_text, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                select_camp_date_text.setText(camp_date_text[which].toString());
                                select_camp_date_id = camp_date_id[which].toString();
                                camp_date_title = camp_date_text[which].toString();
                                int start = camp_date_title.indexOf("￥",0);
                                camp_date_price = camp_date_title.substring(start);
                            }
                        }).create();
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).create();
                dialog.show();
//                Dialog dialog = new Dialog(this, R.style.base_dialog);
//                View view = View.inflate(this, R.layout.dialog_bottom, null);
//                dialog.setContentView(view);
//                dialog.setCanceledOnTouchOutside(true);
//                view.setMinimumHeight((int) (300.0f));
//                Window dialogWindow = dialog.getWindow();
//                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                lp.width = (int) (768);
//                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                lp.gravity = Gravity.BOTTOM;
//                dialogWindow.setAttributes(lp);
//                dialog.show();
                break;

        }
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
    //设置字体
    protected void setFace(){
        this.typeface = Typeface.createFromAsset(getAssets(),"fonts/PingFang.ttf");
        this.title.setTypeface(typeface);
        this.price.setTypeface(typeface);
    }
    public void redirection(){
        Intent intent = new Intent();
        intent.putExtra("camp_id",campid);
        intent.putExtra("title",camp_title);
        intent.putExtra("departure_id",select_camp_date_id);
        intent.putExtra("camp_date_title",camp_date_title);
        intent.putExtra("price",camp_date_price);
        intent.setClass(this, Firm_orderActivity.class);
        startActivityForResult(intent,0);
    }
}
