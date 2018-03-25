package ytx.app.Cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by vi爱 on 2018/2/8.
 */

public class outputStreamSD {
    //文件保存的路径
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/pics";

    /**
     * 向本地SD卡写网络图片
     *
     * @param bitmap
     */
    public static void saveBitmapToLocal(String fileName, Bitmap bitmap) {
        try {
            // 创建文件流，指向该路径，文件名叫做fileName
            File file = new File(FILE_PATH, fileName);
            // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
            File fileParent = file.getParentFile();
            System.out.println(fileParent);
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            // 将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 从本地SD卡获取缓存的bitmap
     */
    public static Bitmap getBitmapFromLocal(String fileName) {
        try {
            File file = new File(FILE_PATH, fileName);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        file));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
