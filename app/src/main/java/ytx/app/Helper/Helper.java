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
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "" +
                "create table user(_id integer primary key autoincrement," +
                "uid text ," +
                "nickname text ," +
                "name text ," +
                "telephone text ," +
                "mobile text ," +
                "remark text ," +
                "sign_sn text ," +
                "msg_unread_num integer(11) ," +
                "login_type integer(1))";
        //String sql2 = "create table teacher...";
        db.execSQL(sql);
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete from stu");
    }

    public void insert() {//增加一条学生记录
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "insert into user(username, password,sign_sn,login_type)values('yaoshaoqing','123456','1234567','1')";
        //String sql = "DROP TABLE user;";
        db.execSQL(sql);
        db.close();
    }

    public void updateAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "update user set sign_sn='0'";
        db.execSQL(sql);
        db.close();
    }

    public void update(ContentValues values,String[] str) {//修改学生记录
        SQLiteDatabase db = this.getReadableDatabase();
//        String sql = "update user set sign_sn='0'";
//        db.execSQL(sql);
//        ContentValues values = new ContentValues();
//        values.put("name","王五");
//        values.put("age","12");
        db.update("user", values, "uid?", str);
        db.close();;
    }

    public Boolean select(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("user", new String[]{"_id","username","password","sign_sn","login_type"}, "login_type = ?", new String[]{"1"}, null, null, null);
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
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        cursor.close();
        db.close();
        if(id > 0){
            return true;
        }
        return false;
    }
}
