package fr.Shiranuit.LogForJustice.Utils;

public class ObjectUtil {
	
	   public static int Int(String val) {
		   try {
			   return Integer.valueOf(val).intValue();  
		   } catch (Exception e) {
			   return 0;
		   }
	   }
	   
	   public static long Long(String val) {
		   try {
			   return Long.valueOf(val).longValue();  
		   } catch (Exception e) {
			   return 0;
		   }
	   }
	   
	   public static double Double(String val) {
		   try {
			   return Double.valueOf(val).doubleValue();  
		   } catch (Exception e) {
			   return 0;
		   }
	   }
	   
	   public static boolean Boolean(String val) {
		   try {
			   return Boolean.valueOf(val).booleanValue();  
		   } catch (Exception e) {
			   return false;
		   }
	   }
}
