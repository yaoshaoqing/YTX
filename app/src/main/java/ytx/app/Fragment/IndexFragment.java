package ytx.app.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import ytx.app.main.Camp_detailActivity;
import ytx.app.Http.GetPost.GetPostUtil;
import ytx.app.ListAdapters.ListAdapter;
import ytx.app.main.MainActivity;
import ytx.app.R;

import static ytx.app.Config.MyAppApiConfig.INTERFACE_URL;

/**
 * Created by vi爱 on 2018/1/10.
 */
public class IndexFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    protected View view = null;
    private ViewPager mViewPaper;
    private List<ImageView> images = new ArrayList<ImageView>();
    private List<View> dots;
    private  List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
    protected ListView listView;
    private int currentItem;
    protected MainActivity activity;
    protected RequestQueue mQueue;
    //记录上一次点的位置
    private int oldPosition = 0;
    //存放图片的id
    //存放图片的标题
    private String[]  titles = new String[]{
            "巩俐不低俗，我就不能低俗",
            "扑树又回来啦！再唱经典老歌引万人大合唱",
            "揭秘北京电影如何升级",
            "乐视网TV版大派送",
            "热血屌丝的反杀"
    };
    private TextView title;
    private ViewPagerAdapter adapter = null;
    private ScheduledExecutorService scheduledExecutorService;
    private boolean isPrepared;
    private boolean mHasLoadedOnce;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(this.view == null){
            this.view = inflater.inflate(R.layout.index_view,container,false);
            activity = (MainActivity) this.getActivity();
            isPrepared = true;
            mQueue = Volley.newRequestQueue(getContext());
            index();
            lazyLoad();
        }
        return this.view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        //setAdapter();
        setHeightWidth();
    }

    protected void init(){
        mViewPaper = (ViewPager) this.view.findViewById(R.id.vp);
        //title = (TextView) this.view.findViewById(R.id.title);
        listView = this.view.findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        EditText editText = this.view.findViewById(R.id.editext);
//        RelativeLayout.LayoutParams editTextHeight = (RelativeLayout.LayoutParams) editText.getLayoutParams();
//        LinearLayout.LayoutParams relativeHeight =(LinearLayout.LayoutParams) relative.getLayoutParams();

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
        float ImagesHeight = this.view.findViewById(R.id.framelayout).getLayoutParams().height;
        float jingping = this.view.findViewById(R.id.linearImg).getLayoutParams().height;
        LinearLayout.LayoutParams listviewLayoutParams = (LinearLayout.LayoutParams) this.view.findViewById(R.id.listview).getLayoutParams();
        int ListviewHeight = (int) (DisplayHeight-BarHeight-editHeight-ImagesHeight-jingping);
        listviewLayoutParams.height = ListviewHeight;
    }
    //存放图片的id
    private int[] imageIds = new int[]{
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e
    };

    private void setAdapter(){
        //显示的小点
        dots = new ArrayList<View>();
        dots.add(this.view.findViewById(R.id.dot_0));
        dots.add(this.view.findViewById(R.id.dot_1));
        dots.add(this.view.findViewById(R.id.dot_2));
        dots.add(this.view.findViewById(R.id.dot_3));
        dots.add(this.view.findViewById(R.id.dot_4));
        dots.add(this.view.findViewById(R.id.dot_5));

        //title.setText(titles[0]);
        //显示的图片
//        images = new ArrayList<ImageView>();
//        for(int i = 0; i < imageIds.length; i++){
//            ImageView imageView = new ImageView(this.activity);
//            imageView.setBackgroundResource(imageIds[i]);
//            images.add(imageView);
//        }
        adapter = new ViewPagerAdapter();
        mViewPaper.setAdapter(adapter);

        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //title.setText(titles[position]);
                dots.get(position).setBackgroundResource(R.drawable.dot1);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot);
                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }
    protected void index(){
        new AsyncTask<Void,String,List<ImageView>>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<ImageView> doInBackground(Void... voids) {
                try {
                    String join = GetPostUtil.Get(INTERFACE_URL+"list_slide.php");
                    JSONObject jsonObject = new JSONObject(join);
                    if(!jsonObject.get("status").equals(200)){
                        return null;
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(images.size() > 0){
                        images = new ArrayList<ImageView>();
                    }

                    for (int i= 0;i<jsonArray.length();i++){
                        final JSONObject object=(JSONObject)jsonArray.get(i);
                        final ImageView imageView = new ImageView(activity);
                        URL url = null;
                        try {
                            url = new URL(object.getString("src").toString());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        InputStream is = null;
                        try {
                            is = url.openStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bitmap BitmapStream = BitmapFactory.decodeStream(is);
                        BitmapDrawable   bitmapDrawable = new BitmapDrawable(BitmapStream);
                        imageView.setBackground(bitmapDrawable);
                        images.add(imageView);

                    }
//                    if(images.size() != 6) {
//                        Thread.sleep(1500);
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return images;
            }

            @Override
            protected void onPostExecute(List<ImageView> s) {
                super.onPostExecute(s);
                IndexFragment.this.setAdapter();
            }
        }.execute();

    }

    /**
     * 列表数据
     */
    public void adaper(){
        ListAdapter listAdapter = new ListAdapter(activity,list);
        listView.setAdapter(listAdapter);
    }
   // private Handler getImg = new Handler(){
//            try {
//                JSONObject jsonObject = new JSONObject(str);
//                if(!jsonObject.get("status").equals(200)){
//                    return;
//                }
//                JSONArray jsonArray = jsonObject.getJSONArray("data");
//                if(images.size() > 0){
//                    images = new ArrayList<ImageView>();
//                }
//                //new Handler().postDelayed();
//                for (int i= 0;i<jsonArray.length();i++){
//                    final JSONObject object=(JSONObject)jsonArray.get(i);
//                    final ImageView imageView = new ImageView(activity);
//                    new Thread() {
//                    @Override
//                    public void run() {
////                        URL url = null;
////                        try {
////                            url = new URL(object.getString("src").toString());
////                        } catch (MalformedURLException e) {
////                            e.printStackTrace();
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
////                        InputStream is = null;
////                        try {
////                            is = url.openStream();
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////                        Bitmap BitmapStream = BitmapFactory.decodeStream(is);
////                        imageView.setImageBitmap(BitmapStream);
////                        images.add(imageView);
//                    }
//                    }.start();
//
//                }
//                if(images.size() != 6) {
//                    Thread.sleep(1500);
//                }
//                setAdapter();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
       // }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        };
    };
    @Override
    public void onStart() {
        super.onStart();
        /**
         * 图片自动滚动
         */
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                //子线程
                new Runnable() {
                    @Override
                    public void run() {
                        currentItem = (currentItem + 1) % images.size();
                        mHandler.sendEmptyMessage(0);
                    }
                },2,5, TimeUnit.SECONDS);

    }

    @Override
    public void onStop() {
        super.onStop();
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 图片轮播功能
     * 接收子线程传递过来的数据
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        }
    };


    /**
     * 数据加载，只加载一次
     */
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(IndexFragment.this.activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER );
        progressDialog.show();
//        new AsyncTask<Void, String, String>(){
//            ProgressDialog progressDialog;
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                progressDialog = new ProgressDialog(IndexFragment.this.activity);
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER );
//                //progressDialog.setMax(10);
//                progressDialog.show();
//            }
//
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                String str = null;
//                try {
//                    String url = INTERFACE_URL+"list_top.php";
//                    str = GetPostUtil.Get(url);
//                    //Thread.sleep(2000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return str;
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                try {
//                    String string = s;
//                    JSONObject json = new JSONObject(string);
//                    JSONArray jsonArray = json.getJSONArray("data");
//                    if (list.size() > 0) {
//                         list = new ArrayList<Map<String, Object>>();
//                    }
//                    for (int i = 0; i <= jsonArray.length(); i++) {
//                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                        Map<String, Object> map = new HashMap<String, Object>();
//                        map.put("id", jsonObject.getString("id"));
//                        map.put("img", jsonObject.getString("cover"));
//                        map.put("title", jsonObject.getString("title"));
//                        map.put("age", jsonObject.getString("age"));
//                        map.put("price", jsonObject.getString("ncost") + "元");
//                        map.put("area", jsonObject.getString("camp_location"));
//                        map.put("date", jsonObject.getString("nstart"));
//                        list.add(map);
//                    }
//
//                } catch (Exception e) {
//                }
//                IndexFragment.this.adaper();
//                mHasLoadedOnce = true;
//                progressDialog.dismiss();
//            }
//        }.execute();
        StringRequest stringRequest = new StringRequest(INTERFACE_URL+"list_top.php",new Response.Listener<String>(){
            @Override
            public void onResponse(String string) {
                try {
                    JSONObject json = new JSONObject(string);
                    JSONArray jsonArray = json.getJSONArray("data");
                    if (list.size() > 0) {
                         list = new ArrayList<Map<String, Object>>();
                    }
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
                IndexFragment.this.adaper();
                mHasLoadedOnce = true;
                progressDialog.dismiss();
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
                map.put("params1", "value1");
                map.put("params2", "value2");
                return map;
            }
        };
        mQueue.add(stringRequest);

    }

    /**
     * listview点击事件及跳转
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this.activity,list.get(position).get("title").toString().trim(),Toast.LENGTH_SHORT).show();
        try{
            String campid = (String) list.get(position).get("id");
            String title = (String) list.get(position).get("title");
            Intent intent = new Intent();
            intent.putExtra("campid",campid);
            intent.putExtra("title",title);
            intent.setClass(this.activity,Camp_detailActivity.class);
            startActivity(intent);
        }catch (Exception e){

        }

    }

    /**
     * 图片轮播功能
      */
    class ViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        //添加
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(images.get(position));
            return images.get(position);
        }
        //删除
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView(images.get(position));
        }
    }
}
