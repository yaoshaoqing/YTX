package ytx.app.ListAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ytx.app.ImageLoader.ImageLoader;
import ytx.ytx.R;

/**
 * Created by vi爱 on 2018/1/22.
 */

public class ListAdapter extends BaseAdapter{
    private Context context;
    private List<Map<String,Object>> list;
    private HashMap<Integer, Bitmap> bitmapHashMap = new HashMap<Integer, Bitmap>();
    private ViewHolder holder;
    private HashMap<Integer,ImageView> imageViewHashMap = new HashMap<Integer,ImageView>();
    private RequestQueue mQueue;
    public ImageLoader imageLoader; //用来下载图片的类，后面有介绍
    public ListAdapter(Context context, List<Map<String,Object>> list){
        this.context = context;
         mQueue = Volley.newRequestQueue(this.context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        //Toast.makeText(this.context,list.get(position).get("id").toString().trim(),Toast.LENGTH_SHORT).show();
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final int index = position;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.main, null);
            holder = new ViewHolder();
            holder.image = view.findViewById(R.id.img);
            holder.title = view.findViewById(R.id.title);
            holder.age = view.findViewById(R.id.age);
            holder.area = view.findViewById(R.id.area);
            holder.date = view.findViewById(R.id.date);
            holder.price = view.findViewById(R.id.price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
//        Bitmap bitmap = bitmapHashMap.get(position);
//        if(bitmap != null){
//            holder.image.setImageBitmap(bitmap);
//        }else{
            //imageViewHashMap.put(index,holder.image);
//            new Thread(){
//                @Override
//                public void run() {
//                    try {
//                        URL url = new URL(list.get(index).get("img").toString());
//                        InputStream is = url.openStream();
//                        Bitmap BitmapStream = BitmapFactory.decodeStream(is);
//                        Message message = ListBitmap.obtainMessage();
//                        message.arg1 = index;
//                        bitmapHashMap.put(index,BitmapStream);
//                        ListBitmap.sendMessage(message);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//            if(bitmapHashMap.get(index) == null){
//
//                new AsyncTask<Void,String,Integer>(){
//
//                    @Override
//                    protected Integer doInBackground(Void... voids) {
//                        try {
//                            URL url = new URL(list.get(index).get("img").toString());
//                            InputStream is = url.openStream();
//                            Bitmap BitmapStream = BitmapFactory.decodeStream(is);
//                            bitmapHashMap.put(index,BitmapStream);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        return index;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Integer integer) {
//                        super.onPostExecute(integer);
//                        imageViewHashMap.get(integer).setImageBitmap(bitmapHashMap.get(integer));
//                    }
//                }.execute();
//                //Picasso.with(this.context).load(imageUrl).placeholder(R.mipmap.ic_launcher).into(imageView);
//            }else{
//                imageViewHashMap.get(index).setImageBitmap(bitmapHashMap.get(index));
//            }
//        }
        //holder.image.setImageBitmap((Bitmap) list.get(position).get("img"));
        //接口回调的方法，完成图片的读取;
//        ImageLoader downImage = new ImageLoader(list.get(position).get("img").toString().trim());
//        downImage.loadImage(new ImageLoader.ImageCallBack() {
//            @Override
//            public void getDrawable(Drawable drawable) {
//                holder.image.setImageDrawable(drawable);
//            }
//        });
        Picasso.with(this.context).load(list.get(index).get("img").toString()).tag("PICASSO_TAG").into(holder.image);
        holder.title.setText(list.get(position).get("title").toString().trim());
        holder.age.setText(list.get(position).get("age").toString().trim());
        holder.price.setText(list.get(position).get("price").toString().trim());
        holder.area.setText(list.get(position).get("area").toString().trim());
        holder.date.setText(list.get(position).get("date").toString().trim());
        return view;
    }

    static class ViewHolder{
        TextView title;
        TextView price;
        TextView date;
        TextView area;
        TextView age;
        ImageView image;

    }
}
