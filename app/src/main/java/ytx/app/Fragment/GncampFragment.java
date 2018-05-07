package ytx.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ytx.app.Activity.Camp_detailActivity;
import ytx.app.ListAdapters.ListAdapter;
import ytx.app.Activity.MainActivity;
import ytx.app.R;
import ytx.app.View.TwoWayRattingBar;

import static ytx.app.Config.MyAppApiConfig.INTERFACE_URL;

/**
 * Created by vi爱 on 2018/1/10.
 */

public class GncampFragment extends BaseFragment implements AdapterView.OnItemClickListener,View.OnClickListener{
    protected View view;
    protected MainActivity activity;
    protected List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
    private boolean isPrepared;
    private boolean mHasLoadedOnce;
    protected RequestQueue mQueue;
    public ListAdapter listAdapter = null;
    public int page=1;
    public PullToRefreshListView PullTolistView;
    public ProgressDialog progressDialog;
    public LinearLayout search_popupwindow;
    public View search_layout;
    protected TextView date_all;
    protected TextView summer;//夏令營
    protected TextView national;//十一營
    protected TextView theme_all;//項目主題
    protected TextView outdoors;//户外拓展
    protected TextView natural;//自然探索
    protected TextView sports;//体育项目
    protected TextView art;//艺术人文
    protected TextView science;//科学技术
    protected TextView military;//军旅主题
    protected TextView language;//语言提升
    protected TextView international;//国际综合
    protected TextView termini_all;//目的地
    protected TextView anhui;//安徽
    protected TextView beijing;
    protected Integer theme = null;//项目主题
    protected Integer min_age = null;//最小年龄
    protected Integer max_age = null;//最大年龄
    protected Integer area = null;//地址
    protected Integer holiday = null;//活动时间
    protected Integer date_sort;//排序，按出发日期，默认为空，1为由近到远，2为由远到近
    protected Integer price_sort;//排序，按价格，默认为空，1为由低到高，2为由高到低
    protected TextView keyword;//搜索关键字
    public PopupWindow window;
    protected TwoWayRattingBar twoWayRattingBar;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(this.view == null){
            view = inflater.inflate(R.layout.gncamp_view,container,false);
            activity = (MainActivity) this.getActivity();
            isPrepared = true;
            mQueue = Volley.newRequestQueue(getContext());
            lazyLoad();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) this.getActivity();
        init();
        pullToRefresh();
        setHeightWidth();

    }

    public void pullToRefresh(){
        //设置可上拉刷新和下拉刷新
        PullTolistView.setMode(PullToRefreshBase.Mode.BOTH);

        //设置刷新时显示的文本
        ILoadingLayout startLayout = PullTolistView.getLoadingLayoutProxy(true,false);
        startLayout.setPullLabel("正在下拉刷新...");
        startLayout.setRefreshingLabel("正在玩命加载中...");
        startLayout.setReleaseLabel("放开以刷新");


        ILoadingLayout endLayout = PullTolistView.getLoadingLayoutProxy(false,true);
        endLayout.setPullLabel("正在上拉刷新...");
        endLayout.setRefreshingLabel("正在玩命加载中...");
        endLayout.setReleaseLabel("放开以刷新");

        //滚动事件
        PullTolistView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                list = new ArrayList<Map<String, Object>>();
                getDate();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page += 1;
                getDate();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * 设置宽高
     */
    public void setHeightWidth(){
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int DisplayWidth = dm.widthPixels;
        int DisplayHeight = dm.heightPixels;
        float BarHeight = activity.findViewById(R.id.button).getLayoutParams().height;
        float editHeight = this.view.findViewById(R.id.toolbar).getLayoutParams().height;
        float OrderBy = this.view.findViewById(R.id.OrderBy).getLayoutParams().height;
        //float jingping = this.view.findViewById(R.id.linearImg).getLayoutParams().height;
        ViewGroup.LayoutParams PullTolistViewParams = PullTolistView.getLayoutParams();
        int ListviewHeight = (int) (DisplayHeight-BarHeight-editHeight-OrderBy);
        PullTolistViewParams.height = ListviewHeight;
    }
    protected void init(){
        this.PullTolistView = view.findViewById(R.id.listview);
        this.search_popupwindow = view.findViewById(R.id.search);
        this.PullTolistView.setOnItemClickListener(this);
        this.search_popupwindow.setOnClickListener(this);
    }

    private Handler listData = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                String string = (String) msg.obj;
                JSONObject json = new JSONObject(string);
                JSONArray jsonArray = json.getJSONArray("data");
                if(list.size() > 0){
                    list = new ArrayList<Map<String, Object>>();
                }
                for(int i=0;i<=jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", jsonObject.getString("id"));
                    map.put("img", jsonObject.getString("cover"));
                    map.put("title", jsonObject.getString("title"));
                    map.put("age", jsonObject.getString("age"));
                    map.put("price",jsonObject.getString("ncost")+"元");
                    map.put("area",jsonObject.getString("camp_location"));
                    map.put("date",jsonObject.getString("nstart"));
                    list.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

//    private void getData() {
//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                try {
//                    String url = "http://test.51camp.cn/app_api/v1/list_camp.php";
//                    String str = GetPostUtil.Post(url,"page=1&camp_type=1");
//                    Message message = new Message();
//                    message.obj = str;
//                    listData.sendMessage(message);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        thread.start();
//    }
    protected void adaper(){
        listAdapter = new ListAdapter(activity,list);
        PullTolistView.setAdapter(listAdapter);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getDate();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this.activity,list.get(position).get("title").toString().trim(),Toast.LENGTH_SHORT).show();
        String campid = (String) list.get(position-1).get("id");
        String title = (String) list.get(position-1).get("title");
        Intent intent = new Intent();
        intent.putExtra("campid",campid);
        intent.putExtra("title",title);
        intent.setClass(this.activity,Camp_detailActivity.class);
        startActivity(intent);
    }

    /**
     * 数据加载
     */
    public void getDate(){
        if(this.page <= 1){
            progressDialog = new ProgressDialog(GncampFragment.this.activity);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER );
            //progressDialog.setMax(10);
            progressDialog.show();
        }
//        new AsyncTask<Void, String, String>(){
//            ProgressDialog progressDialog;
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                progressDialog = new ProgressDialog(CncampFragment.this.activity);
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER );
//                //progressDialog.setMax(10);
//                progressDialog.show();
//            }
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                String str = null;
//                try {
//                    String url = INTERFACE_URL+"list_camp.php";
//                    str = GetPostUtil.Post(url,"page=1&camp_type=0");
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                return str;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                try {
//                    String string = (String) s;
//                    JSONObject json = new JSONObject(string);
//                    JSONArray jsonArray = json.getJSONArray("data");
//                    if(list.size() > 0){
//                        list = new ArrayList<Map<String, Object>>();
//                    }
//                    for(int i=0;i<=jsonArray.length();i++){
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                        Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("id", jsonObject.getString("id"));
//                        map.put("img", jsonObject.getString("cover"));
//                        map.put("title", jsonObject.getString("title"));
//                        map.put("age", jsonObject.getString("age"));
//                        map.put("price",jsonObject.getString("ncost")+"元");
//                        map.put("area",jsonObject.getString("camp_location"));
//                        map.put("date",jsonObject.getString("nstart"));
//                        list.add(map);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                CncampFragment.this.adaper();
//                mHasLoadedOnce = true;
//                progressDialog.dismiss();
//            }
//        }.execute();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,INTERFACE_URL+"list_camp.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String string) {
                try {
                    JSONObject json = new JSONObject(string);
                    JSONArray jsonArray = json.getJSONArray("data");
//                    if (list.size() > 0) {
//                        list = new ArrayList<Map<String, Object>>();
//                    }
                    for (int i = 0; i <= jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", jsonObject.getString("id"));
                        map.put("img", jsonObject.getString("cover"));
                        map.put("title", jsonObject.getString("title"));
                        map.put("age", jsonObject.getString("age"));
                        map.put("price", jsonObject.getString("ncost") + "元");
                        map.put("area", jsonObject.getString("camp_location"));
                        map.put("date", jsonObject.getString("nstart"));
                        list.add(map);
                    }

                } catch (Exception e) {
                }
                if(listAdapter == null){
                    GncampFragment.this.adaper();
                }else{
                    listAdapter.updateView(list);
                    GncampFragment.this.PullTolistView.onRefreshComplete();
                }
                mHasLoadedOnce = true;
                if(GncampFragment.this.page <= 1){
                    progressDialog.dismiss();
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
                map.put("page", page+"");
                map.put("camp_type", "1");
                if(holiday  != null){
                    map.put("holiday", holiday+"");
                }
                if(min_age != null){
                    map.put("min_age",min_age+"");
                }
                if(max_age != null){
                    map.put("max_age",max_age+"");
                }
                if(area !=null) {
                    map.put("area", area + "");
                }
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.search:
                showPopwindow();
                break;

        }
    }

    /**
     * 显示popupWindow
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        search_layout = inflater.inflate(R.layout.search_popupwindow, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window = new PopupWindow(search_layout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(GncampFragment.this.view.findViewById(R.id.start),
                Gravity.BOTTOM, 0, 0);
        //确定监听事件
        search_layout.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                getDate();
            }
        });
        //重置监听事件
        search_layout.findViewById(R.id.initialize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
            }
        });
        //popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //System.out.println("popWindow消失");
            }
        });

        //设置监听事件
        click();
        //初始值
        initialize();
        //年龄双向选择
        setListener();
    }

    class Click implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.outdoors:
                    theme = 1;//户外拓展
                    setThemeAllColor();
                    setThemeClickColor(outdoors);
                    break;
                case R.id.natural:
                    theme = 2;//自然探索
                    setThemeAllColor();
                    setThemeClickColor(natural);
                    break;
                case R.id.sports:
                    theme = 3;//体育运动
                    setThemeAllColor();
                    setThemeClickColor(sports);
                    break;
                case R.id.art:
                    theme = 4;//艺术人文
                    setThemeAllColor();
                    setThemeClickColor(art);
                    break;
                case R.id.science:
                    theme = 6;//科学技术
                    setThemeAllColor();
                    setThemeClickColor(science);
                    break;
                case R.id.military:
                    theme = 12;//军旅主题
                    setThemeAllColor();
                    setThemeClickColor(military);
                    break;
                case R.id.language:
                    theme = 13;//语言提升
                    setThemeAllColor();
                    setThemeClickColor(language);
                    break;
                case R.id.international:
                    theme = 16;//国际综合
                    setThemeAllColor();
                    setThemeClickColor(international);
                    break;
                case R.id.theme_all:
                    theme = null;
                    setThemeAllColor();
                    setThemeClickColor(theme_all);
                    break;
                case R.id.summer://夏令营
                    setDateAllColor();
                    setDateClickColor(summer);
                    holiday = 11;
                    break;
                case R.id.national://十一营
                    setDateAllColor();
                    setDateClickColor(national);
                    holiday = 12;
                    break;
                case R.id.date_all://不限
                    holiday = null;
                    setDateAllColor();
                    setDateClickColor(date_all);
                    break;
                case R.id.termini_all://目的地
                    area = null;
                    setTerminiAllColor();
                    setTerminiClickColor(termini_all);
                    break;
                case R.id.anhui:
                    area = 202;
                    setTerminiAllColor();
                    setTerminiClickColor(anhui);
                    break;
                case R.id.beijing:
                    area = 1;
                    setTerminiAllColor();
                    setTerminiClickColor(beijing);
                    break;


            }
        }
    }
    //设置项目主题的颜色为白色
    public void setThemeAllColor(){
        //设置字体边框背景
//        this.theme_all.setBackgroundResource(R.drawable.search_text_border);//項目主題
//        this.theme_all.setTextAppearance(GncampFragment.this.activity,R.style.search_text);
//        this.outdoors.setBackgroundResource(R.drawable.search_text_border);//户外拓展
//        this.natural.setBackgroundResource(R.drawable.search_text_border);//自然探索
//        this.sports.setBackgroundResource(R.drawable.search_text_border);//体育项目
//        this.art.setBackgroundResource(R.drawable.search_text_border);//艺术人文
//        this.science.setBackgroundResource(R.drawable.search_text_border);//科学技术
//        this.military.setBackgroundResource(R.drawable.search_text_border);//军旅主题
//        this.language.setBackgroundResource(R.drawable.search_text_border);//语言提升
//        this.international.setBackgroundResource(R.drawable.search_text_border);//国际综合
        LinearLayout select_theme_layout_1 = search_layout.findViewById(R.id.select_theme_layout_1);
        int layout_1 = select_theme_layout_1.getChildCount();
        for(int i=0;i<layout_1;i++){
            View v = select_theme_layout_1.getChildAt(i);
            v.setBackgroundResource(R.drawable.search_text_border);
        }
        LinearLayout select_theme_layout_2 = search_layout.findViewById(R.id.select_theme_layout_2);
        int layout_2 = select_theme_layout_2.getChildCount();
        for(int i=0;i<layout_2;i++){
            View v = select_theme_layout_2.getChildAt(i);
            v.setBackgroundResource(R.drawable.search_text_border);
        }
        LinearLayout select_theme_layout_3 = search_layout.findViewById(R.id.select_theme_layout_3);
        int layout_3 = select_theme_layout_3.getChildCount();
        for(int i=0;i<layout_3;i++){
            View v = select_theme_layout_3.getChildAt(i);
            v.setBackgroundResource(R.drawable.search_text_border);
            //TextView textView = new TextView(getContext());
        }
        //设置字体颜色
        this.theme_all.setTextColor(Color.parseColor("#333333"));
        this.outdoors.setTextColor(Color.parseColor("#333333"));//户外拓展
        this.natural.setTextColor(Color.parseColor("#333333"));//自然探索
        this.sports.setTextColor(Color.parseColor("#333333"));//体育项目
        this.art.setTextColor(Color.parseColor("#333333"));//艺术人文
        this.science.setTextColor(Color.parseColor("#333333"));//科学技术
        this.military.setTextColor(Color.parseColor("#333333"));//军旅主题
        this.language.setTextColor(Color.parseColor("#333333"));//语言提升
        this.international.setTextColor(Color.parseColor("#333333"));//国际综合
    }


    //设置项目主题点击后的颜色
    public void setThemeClickColor(TextView v){
        v.setBackgroundResource(R.drawable.search_text_checked_border);
        v.setTextColor(Color.parseColor("#ffffff"));
    }


    //活动时间
    public void setDateAllColor(){
        LinearLayout select_date_layout = search_layout.findViewById(R.id.select_date_layout);
        int index = select_date_layout.getChildCount();
        for(int i=0;i<index;i++){
            View v = select_date_layout.getChildAt(i);
            v.setBackgroundResource(R.drawable.search_text_border);
        }
        this.date_all.setTextColor(Color.parseColor("#333333"));
        this.summer.setTextColor(Color.parseColor("#333333"));
        this.national.setTextColor(Color.parseColor("#333333"));
    }


    //设置活动时间点击后的颜色
    public void setDateClickColor(TextView v){
        v.setBackgroundResource(R.drawable.search_text_checked_border);
        v.setTextColor(Color.parseColor("#ffffff"));
    }


    //设置目的地颜色
    public void setTerminiAllColor(){
        LinearLayout select_termini_layout = search_layout.findViewById(R.id.select_termini);
        int index = select_termini_layout.getChildCount();
        for(int i=0;i<index;i++){
            View v = select_termini_layout.getChildAt(i);
            v.setBackgroundResource(R.drawable.search_text_border);
        }
        this.termini_all.setTextColor(Color.parseColor("#333333"));
        this.anhui.setTextColor(Color.parseColor("#333333"));
        this.beijing.setTextColor(Color.parseColor("#333333"));
    }


    //设置目的地点击后的颜色
    public void setTerminiClickColor(TextView v){
        v.setBackgroundResource(R.drawable.search_text_checked_border);
        v.setTextColor(Color.parseColor("#ffffff"));
    }


    //初始化
    public void initialize(){

        setDateAllColor();
        setThemeAllColor();
        setTerminiAllColor();
        //设置初始值
        this.date_all.setTextColor(Color.parseColor("#ffffff"));
        this.theme_all.setTextColor(Color.parseColor("#ffffff"));
        this.termini_all.setTextColor(Color.parseColor("#ffffff"));
        this.date_all.setBackgroundResource(R.drawable.search_text_checked_border);
        this.termini_all.setBackgroundResource(R.drawable.search_text_checked_border);
        this.theme_all.setBackgroundResource(R.drawable.search_text_checked_border);
        this.theme = null;
        this.holiday = null;
        this.area = null;

    }


    public void click(){
        //循环设置监听事件事件
        Click  click = new Click();
        LinearLayout select_theme_layout_1 = search_layout.findViewById(R.id.select_theme_layout_1);
        int layout_1 = select_theme_layout_1.getChildCount();
        for(int i=0;i<layout_1;i++){
            View v = select_theme_layout_1.getChildAt(i);
            v.setOnClickListener(click);
        }
        LinearLayout select_theme_layout_2 = search_layout.findViewById(R.id.select_theme_layout_2);
        int layout_2 = select_theme_layout_2.getChildCount();
        for(int i=0;i<layout_2;i++){
            View v = select_theme_layout_2.getChildAt(i);
            v.setOnClickListener(click);
        }
        LinearLayout select_theme_layout_3 = search_layout.findViewById(R.id.select_theme_layout_3);
        int layout_3 = select_theme_layout_3.getChildCount();
        for(int i=0;i<layout_3;i++){
            View v = select_theme_layout_3.getChildAt(i);
            v.setOnClickListener(click);
        }
        LinearLayout select_date_layout = search_layout.findViewById(R.id.select_date_layout);
        int index = select_date_layout.getChildCount();
        for(int i=0;i<index;i++){
            View v = select_date_layout.getChildAt(i);
            v.setOnClickListener(click);
        }
        LinearLayout select_termini_layout = search_layout.findViewById(R.id.select_termini);
        int termini_layout = select_termini_layout.getChildCount();
        for(int i=0;i<termini_layout;i++){
            View v = select_termini_layout.getChildAt(i);
            v.setOnClickListener(click);
        }

        //获取页面对像
        this.date_all = search_layout.findViewById(R.id.date_all);
        this.summer = search_layout.findViewById(R.id.summer);//夏令營
        this.national = search_layout.findViewById(R.id.national);//十一營
        this.theme_all = search_layout.findViewById(R.id.theme_all);//項目主題
        this.outdoors = search_layout.findViewById(R.id.outdoors);//户外拓展
        this.natural = search_layout.findViewById(R.id.natural);//自然探索
        this.sports = search_layout.findViewById(R.id.sports);//体育项目
        this.art = search_layout.findViewById(R.id.art);//艺术人文
        this.science = search_layout.findViewById(R.id.science);//科学技术
        this.military = search_layout.findViewById(R.id.military);//军旅主题
        this.language = search_layout.findViewById(R.id.language);//语言提升
        this.international = search_layout.findViewById(R.id.international);//国际综合
        this.termini_all = search_layout.findViewById(R.id.termini_all);//目的地  不限
        this.anhui = search_layout.findViewById(R.id.anhui);
        this.beijing = search_layout.findViewById(R.id.beijing);
        this.twoWayRattingBar = search_layout.findViewById(R.id.twoWayRattingBar);
    }

    private void setListener() {
        // 用法
       twoWayRattingBar.setOnProgressChangeListener(new TwoWayRattingBar.OnProgressChangeListener() {
           @Override
           public void onLeftProgressChange(float progress) {
               min_age = (int) Math.floor(progress*18);
           }

           @Override
           public void onRightProgressChange(float progress) {
               max_age = (int) Math.floor(progress*18);
           }
       });
    }

}




