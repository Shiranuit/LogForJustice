package fr.Shiranuit.LogForJustice.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class ArrayConverter {
	

	public static ArrayList<String> convert(String[] lst) {
		ArrayList<String> nlst = new ArrayList<String>(Arrays.asList(lst));
		return nlst;
	}
	
	public static  String[] convert(ArrayList<String> lst) {
		String[] stockArr = new String[lst.size()];
		stockArr = lst.toArray(stockArr);
		return stockArr;
	}
	
	public static  String[] convert(Set<String> lst) {
		String[] stockArr = new String[lst.size()];
		stockArr = lst.toArray(stockArr);
		return stockArr;
	}
}
