package ytx.app.main;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ytx.app.Fragment.CncampFragment;
import ytx.app.Fragment.GncampFragment;
import ytx.app.Fragment.IndexFragment;
import ytx.app.Fragment.PersonalFragment;
import ytx.app.Helper.Helper;
import ytx.app.R;
import ytx.app.Login.*;

public class MainActivity extends AppCompatActivity {
    protected ImageView img_index,img_cncamp,img_gncamp,img_personal;
    protected TextView text_index,text_cncamp,text_gncamp,text_personal;
    protected ViewPager viewPager;
    private int view;
    private List<Fragment> list = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        viewpage();
        getDB();
        //insertToStu();
    }

    /**
     * 数据库
     * @return
     */
    private SQLiteDatabase getDB() {
        //Context context, String name, SQLiteDatabase.CursorFactory factory, int version
        //发果版本号不变，调用构造方法时，会创建数据库,数据库的名字是nexa,版本号为BB
        Helper helper = new Helper(this,"ytx",null,1);
        //第一次执行时，会触发helper对象调用onCreate方法创建表
        SQLiteDatabase db = helper.getReadableDatabase();
        return db;
    }
    public void insertToStu() {//增加一条学生记录
        SQLiteDatabase db = this.getDB();
        //String sql = "insert into user(username, password,sign_sn,login_type)values('yaoshaoqing','123456','1234567','1')";
        String sql = "DROP TABLE user;";
        db.execSQL(sql);
        db.close();
    }
    /**
     * 动太设置底部导航栏高度、宽度
     */
//    protected void ButtonHeight(){
//        RelativeLayout.LayoutParams lineareHeight =(RelativeLayout.LayoutParams) this.findViewById(R.id.button).getLayoutParams();
//        DisplayMetrics dm = this.getResources().getDisplayMetrics();
//        int DisplayWidth = dm.widthPixels;
//        int DisplayHeight = dm.heightPixels;
//        //Toast.makeText(this,"宽"+width+",高"+height,Toast.LENGTH_LONG).show();
//        Double setHeight = DisplayWidth * 0.081;
//        lineareHeight.height = setHeight.intValue();
//        float Heigth = this.findViewById(R.id.button).getLayoutParams().height;
//        Toast.makeText(this,Heigth+"",Toast.LENGTH_LONG).show();
//    }
    /**
     * 设置点击事件
     * @param v
     */
    protected void onclick(View v){
        int id = v.getId();
        //首页
        if(R.id.index == id){
            seticon(0);
            viewPager.setCurrentItem(0,false);
        //国内营
        }else if(R.id.cncamp == id){
            seticon(1);
            viewPager.setCurrentItem(1,false);
        //国际营
        }else if(R.id.gncamp == id){
            seticon(2);
            viewPager.setCurrentItem(2,false);
        //个人中心
        }else if(R.id.personal == id){
//            SQLiteDatabase db = this.getDB();
//            Cursor cursor = db.query("user", new String[]{"_id","username","password","sign_sn","login_type"}, "login_type = ?", new String[]{"1"}, null, null, null);
//            List<String> list = new ArrayList<String>();
//            while (cursor.moveToNext()) {
//                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
//                String username = cursor.getString(cursor.getColumnIndex("username"));
//                String password = cursor.getString(cursor.getColumnIndex("password"));
//                String sign_sn = cursor.getString(cursor.getColumnIndex("sign_sn"));
//                String login_type = cursor.getString(cursor.getColumnIndex("login_type"));
//                //list.add(new Stu(_id, name,age));
//                System.out.println(username);
//                System.out.println(_id);
//            }
//            cursor.close();
//            db.close();
            Intent intent = new Intent();
            intent.putExtra("name","123");
            intent.setClass(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            //seticon(3);
            //viewPager.setCurrentItem(3,false);
        }
//        BaseFragment baseFragment = new BaseFragment(this.view);
//        this.fragmentManager = this.getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout,baseFragment);
//        fragmentTransaction.commit();
    }

    /**
     * 获取页面标签
     */
    protected void init(){
        img_index = (ImageView) findViewById(R.id.index_img);
        img_cncamp = (ImageView) findViewById(R.id.cncamp_img);
        img_gncamp = (ImageView) findViewById(R.id.gncamp_img);
        img_personal = (ImageView) findViewById(R.id.personal_img);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        text_index = (TextView) findViewById(R.id.index_text);
        text_cncamp = (TextView) findViewById(R.id.cncamp_text);
        text_gncamp = (TextView) findViewById(R.id.gncamp_text);
        text_personal = (TextView) findViewById(R.id.personal_text);

    }

//    protected void fragment(){
//        this.fragmentManager = this.getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.frameLayout,baseFragment);
//        fragmentTransaction.commit();
//    }

    /**
     * 设置显示页面
     */
    public void viewpage(){

        list.add(new IndexFragment());
        list.add(new CncampFragment());
        list.add(new GncampFragment());
        list.add(new PersonalFragment());
        viewPager.setOffscreenPageLimit(1);
        BaseFragmentPager pager = new BaseFragmentPager(this.getSupportFragmentManager(),list);
        viewPager.setAdapter(pager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                seticon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置显示图标
     * @param position
     */
    public void seticon(int position){
        Bitmap bmp;
        Bitmap index_bmp= BitmapFactory.decodeResource(getResources(),R.drawable.index_wxz2x );
        img_index.setImageBitmap(index_bmp);
        Bitmap cncamp_bmp = BitmapFactory.decodeResource(getResources(),R.drawable.cncamp_wxz2x );
        img_cncamp.setImageBitmap(cncamp_bmp);
        Bitmap gncamp_bmp = BitmapFactory.decodeResource(getResources(),R.drawable.gncamp_wxz2x );
        img_gncamp.setImageBitmap(gncamp_bmp);
        Bitmap personal_bmp = BitmapFactory.decodeResource(getResources(),R.drawable.personal_wxz2x );
        img_personal.setImageBitmap(personal_bmp);
        text_index.setTextColor(Color.parseColor("#b2b2b2"));
        text_gncamp.setTextColor(Color.parseColor("#b2b2b2"));
        text_cncamp.setTextColor(Color.parseColor("#b2b2b2"));
        text_personal.setTextColor(Color.parseColor("#b2b2b2"));
        switch (position){
            case 0:
                bmp = BitmapFactory.decodeResource(getResources(),R.drawable.index_xz3x );
                img_index.setImageBitmap(bmp);
                text_index.setTextColor(Color.parseColor("#4cb434"));
                break;
            case 1:
                bmp = BitmapFactory.decodeResource(getResources(),R.drawable.cncamp_xz2x );
                img_cncamp.setImageBitmap(bmp);
                text_cncamp.setTextColor(Color.parseColor("#4cb434"));
                break;
            case 2:
                bmp = BitmapFactory.decodeResource(getResources(),R.drawable.gncamp_xz2x );
                img_gncamp.setImageBitmap(bmp);
                text_gncamp.setTextColor(Color.parseColor("#4cb434"));
                break;
            case 3:
                bmp = BitmapFactory.decodeResource(getResources(),R.drawable.personal_xz2x );
                img_personal.setImageBitmap(bmp);
                text_personal.setTextColor(Color.parseColor("#4cb434"));
                break;
        }
    }




}

class BaseFragmentPager extends FragmentPagerAdapter{
    protected List<Fragment> list;
    public BaseFragmentPager(FragmentManager fx, List<Fragment> list){
        super(fx);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }


}


