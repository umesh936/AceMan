package in.sminfo.tool.mgmt.common.utilities;

import java.util.HashMap;
import java.util.Map;

public class StaticDataHolder {

	private static Map<String, Object> internalStorage = new HashMap<>();

	public static Object getData(String key) {
		return internalStorage.get(key);
	}

	public static void putData(String key, Object value) {
		internalStorage.put(key, value);
	}
}
