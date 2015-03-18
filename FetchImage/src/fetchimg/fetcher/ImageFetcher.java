package fetchimg.fetcher;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fetchimg.activity.MImage;
import fetchimg.cache.ImageMemoryCache;

import android.content.Context;
import android.util.Log;

public class ImageFetcher {
	private Context mContext;

	public ImageFetcher(Context context) {
		this.mContext = context;
	}

	public MImage fetchImage(String content) throws IOException {
		String imgMod1 = "alt=\"([^\"]*)\"";
		String imgMod2 = "src=\"([^\"]*)\"";
		Pattern pattern1 = Pattern.compile(imgMod1);
		Matcher matcher1 = pattern1.matcher(content);

		MImage image = null;
		while (matcher1.find()) {
			ImageMemoryCache cache = new ImageMemoryCache();
			Pattern pattern2 = Pattern.compile(imgMod2);
			Matcher matcher2 = pattern2.matcher(content);
			if (matcher2.find()) {
				image = new MImage(matcher1.group(1), cache.getImagePath(
						mContext, matcher2.group(1)));
			}
		}
		return image;
	}
}
