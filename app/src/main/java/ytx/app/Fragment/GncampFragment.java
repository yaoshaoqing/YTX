package ytx.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import ytx.app.Http.GetPost.GetPostUtil;
import ytx.app.ListAdapters.ListAdapter;
import ytx.app.Activity.MainActivity;
import ytx.app.R;

import static ytx.app.Config.MyAppApiConfig.INTERFACE_URL;

/**
 * Created by vi爱 on 2018/1/10.
 */

public class GncampFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    protected View view;
    protected MainActivity activity;
    protected ListView listView;
    protected List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
    private boolean isPrepared;
    private boolean mHasLoadedOnce;
    protected RequestQueue mQueue;
    public ListAdapter listAdapter = null;
    public int page=1;
    public PullToRefreshListView PullTolistView;
    public ProgressDialog progressDialog;
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
        this.PullTolistView.setOnItemClickListener(this);
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
        String campid = (String) list.get(position).get("id");
        String title = (String) list.get(position).get("title");
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
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

}




