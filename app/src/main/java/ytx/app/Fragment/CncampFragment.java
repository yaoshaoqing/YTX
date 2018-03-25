package ytx.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ytx.app.main.Camp_detailActivity;
import ytx.app.Http.GetPost.GetPostUtil;
import ytx.app.ListAdapters.ListAdapter;
import ytx.app.main.MainActivity;
import ytx.ytx.R;

import static ytx.app.Config.MyAppApiConfig.INTERFACE_URL;

/**
 * Created by vi爱 on 2018/1/10.
 */

public class CncampFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    protected View view;
    protected MainActivity activity;
    protected ListView listView;
    protected List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
    private boolean isPrepared;
    private boolean mHasLoadedOnce;
    protected RequestQueue mQueue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(this.view == null){
            this.view = inflater.inflate(R.layout.cncamp_view,container,false);
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
        init();
        setHeightWidth();
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
        LinearLayout.LayoutParams listviewLayoutParams = (LinearLayout.LayoutParams) this.view.findViewById(R.id.listview).getLayoutParams();
        int ListviewHeight = (int) (DisplayHeight-BarHeight-editHeight-OrderBy);
        listviewLayoutParams.height = ListviewHeight;
    }
    protected void init(){
        listView = view.findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
    }

    protected void adaper(){
        ListAdapter listAdapter = new ListAdapter(activity,list);
        listView.setAdapter(listAdapter);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(CncampFragment.this.activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER );
        //progressDialog.setMax(10);
        progressDialog.show();
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
                System.out.println(string);
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
                CncampFragment.this.adaper();
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
                map.put("page", "1");
                map.put("camp_type", "0");
                return map;
            }
        };
        mQueue.add(stringRequest);
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

}




