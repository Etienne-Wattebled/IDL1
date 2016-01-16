package utils;

public class PathUtils {
	public static String getPathAfter(String path, String flag) {
		return path.substring(path.lastIndexOf(flag)+flag.length());
	}
	
	public static String getPathWithoutExtension(String path) {
		return path.substring(0,path.lastIndexOf("."));
	}
}
