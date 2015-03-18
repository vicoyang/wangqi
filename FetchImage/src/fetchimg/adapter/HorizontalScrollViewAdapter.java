package fetchimg.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fetchimage.R;

import fetchimg.activity.MImage;
import fetchimg.common.Utils;

public class HorizontalScrollViewAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<MImage> mDatas;

	public HorizontalScrollViewAdapter(Context context, List<MImage> mDatas) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
	}

	public int getCount() {
		return mDatas.size();
	}

	public Object getItem(int position) {
		return mDatas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.activity_index_gallery_item, parent, false);
			viewHolder.mImg = (ImageView) convertView
					.findViewById(R.id.item_image);
			viewHolder.mText = (TextView) convertView
					.findViewById(R.id.item_text);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (android.os.Build.VERSION.SDK_INT >= 14) {
			viewHolder.mImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		MImage mImage = mDatas.get(position);
		Bitmap bmp = Utils.getLoacalBitmap(mImage.getPath());
		viewHolder.mImg.setImageBitmap(bmp);
		viewHolder.mText.setText(mImage.getName());

		return convertView;
	}

	private class ViewHolder {
		ImageView mImg;
		TextView mText;
	}
}
