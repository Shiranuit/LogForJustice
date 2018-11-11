package fr.Shiranuit.LogForJustice.Utils;

import java.util.HashMap;

public class HashMapUtil {
	public static void copy(HashMap src, HashMap dst) {
		for (Object key : src.keySet()) {
			dst.put(key, src.get(key));
		}
	}
}
