package common;

public class Common {
	public static String newMess(String type,String cmd,String mess){
		return "{%["+type+"]<"+cmd+">%}(%"+mess+"%)";
	}
}
