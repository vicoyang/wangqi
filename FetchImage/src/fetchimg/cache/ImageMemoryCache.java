package fetchimg.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class ImageMemoryCache {
	public String getImagePath(Context context, String url) {
		if (url == null)
			return "";
		String imagePath = "";
		String fileName = "";

		if (url != null && url.length() != 0) {
			fileName = url.substring(url.lastIndexOf("/") + 1);
		}

		imagePath = context.getCacheDir() + "/" + fileName;
		File file = new File(context.getCacheDir(), fileName);
		if (!file.exists()) {
			try {
				byte[] data = readInputStream(getRequest(url));
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
						data.length);

				bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(
						file));

				imagePath = file.getAbsolutePath();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return imagePath;
	} 

	public InputStream getRequest(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000); 
		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		return null;

	}

	public byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
}
