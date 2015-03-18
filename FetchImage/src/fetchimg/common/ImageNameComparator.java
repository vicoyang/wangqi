package fetchimg.common;

import java.util.Comparator;

import fetchimg.activity.MImage;

public class ImageNameComparator implements Comparator<MImage> {

	@Override
	public int compare(MImage lhs, MImage rhs) {
		if (lhs.getName().length() > rhs.getName().length()) {
			return 1;
		} else if (lhs.getName().length() < rhs.getName().length()) {
			return -1;
		} else {
			return 0;
		}
	}

}
