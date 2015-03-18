package fetchimg.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils {
	public final static String TargetUrl = "http://www.duitang.com/album/58375208/";

	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
