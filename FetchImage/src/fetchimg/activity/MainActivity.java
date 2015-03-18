package fetchimg.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.fetchimage.R;

import fetchimg.adapter.HorizontalScrollViewAdapter;
import fetchimg.common.ImageNameComparator;
import fetchimg.common.Utils;
import fetchimg.control.MyHorizontalScrollView;
import fetchimg.control.MyHorizontalScrollView.OnItemClickListener;
import fetchimg.fetcher.ImageFetcher;

public class MainActivity extends Activity {
	private ArrayList<MImage> images = new ArrayList<MImage>();
	private ImageFetcher fetcher = new ImageFetcher(this);
	private MyHorizontalScrollView mScrollView;
	private HorizontalScrollViewAdapter mAdapter;
	private Handler mHandler = new MHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
		initView();
		loadData();

	}

	private void findView() {
		mScrollView = (MyHorizontalScrollView) findViewById(R.id.myHorizontalScrollView);
	}

	private void initView() {
		mAdapter = new HorizontalScrollViewAdapter(this, images);
		mScrollView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onClick(View view, int pos) {
				ContentResolver cr = MainActivity.this.getContentResolver();
				Bitmap bmp = Utils.getLoacalBitmap(images.get(pos).getPath());
				String url = MediaStore.Images.Media.insertImage(cr, bmp,
						images.get(pos).getName(), "");
				if (TextUtils.isEmpty(url)) {
					Toast.makeText(MainActivity.this, "保存失败!",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "保存成功!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private void loadData() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				extracLinks(Utils.TargetUrl);
				ImageNameComparator comparator = new ImageNameComparator();
				Collections.sort(images, comparator);
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
		});
		thread.start();
	}

	private class MHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				if (images.size() > 0) {
					mScrollView.initDatas(mAdapter);
				}
			}

		}
	};

	public void extracLinks(String url) {
		try {
			Parser parser = new Parser(url);
			parser.setEncoding("UTF-8");
			NodeFilter frameFilter = new NodeClassFilter(ImageTag.class);
			NodeList list = parser.extractAllNodesThatMatch(frameFilter);
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if (tag instanceof ImageTag) {
					ImageTag image = (ImageTag) list.elementAt(i);
					MImage timage = fetcher.fetchImage(image.getText());
					if (timage != null) {
						images.add(timage);
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
