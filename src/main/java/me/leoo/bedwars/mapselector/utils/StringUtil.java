package me.leoo.bedwars.mapselector.utils;

public class StringUtil {

	public static String firstLetterUpperCase(String s){
		if(s == null){
			return null;
		}
		char[] l = s.toCharArray();
		l[0] = Character.toUpperCase(l[0]);
		return new String(l);
	}
}
