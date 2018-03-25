package ytx.app.Helper;

import android.content.Context;
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
        String sql = "create table user(_id integer primary key autoincrement, name text, age integer)";
        String sql2 = "create table teacher...";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete from stu");
    }
}
