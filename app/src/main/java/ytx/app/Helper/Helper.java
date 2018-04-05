package ytx.app.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vi爱 on 2018/3/3.
 */

public class Helper extends SQLiteOpenHelper{
    /**
     * @param context   上下文
     * @param name      数据库名
     * @param factory  一般传null
     * @param version  数据为版号 值为大于0的整形
     */
    public Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    protected SQLiteDatabase db;
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "" +
                "create table user(_id integer primary key autoincrement,uid text,nickname text,name text,telephone text,mobile text,remark text,sign_sn text,msg_unread_num integer(11),login_type integer(1))";
        //String sql2 = "create table teacher...";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete from stu");
    }

    public void UpdateAll() {
        db = this.getReadableDatabase();
        String sql = "update user set sign_sn='0'";
        db.execSQL(sql);
        db.close();
    }
    public Boolean islogin(){
        db = this.getReadableDatabase();
        String sql = "select _id,name from user where login_type = 1";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst() != false){
            cursor.close();
            db.close();
            return true;
        }else{
            cursor.close();
            db.close();
            return false;
        }
    }
    public Boolean Select(String[] str){
        db = this.getReadableDatabase();
        //Cursor cursor = db.query("user", new String[]{"_id"}, "uid = ?", str, null, null, null);
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
        String sql = "select _id,name from user where login_type>0";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        cursor.close();
        db.close();
        //return cursor.getString(cursor.getColumnIndex("nickname"));
        if(id > 0){
            return true;
        }
        return false;
    }

    public void LoginType(ContentValues values,String uid){
        db = this.getReadableDatabase();
        String sql = "select _id,name from user where uid = "+uid;
        Cursor cursor = db.rawQuery(sql, null);
        //Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
        if(cursor.moveToFirst() != false){
            db.update("user",values,"uid = ?",new String[]{uid});
        }else{
            db.insert("user",null,values);
        }
        cursor.close();
        db.close();
    }

}
