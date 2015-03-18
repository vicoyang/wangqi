package fetchimg.activity;

public class MImage {
	private String name;
	private String path;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public MImage(String name, String path) {
		super();
		this.name = name;
		this.path = path;
	}

	@Override
	public String toString() {
		return "MImage [name=" + name + ", path=" + path + "]";
	}
}
