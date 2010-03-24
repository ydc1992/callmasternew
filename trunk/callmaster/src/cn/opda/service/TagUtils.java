package cn.opda.service;
public class TagUtils {
	public static int yishengxiang = 0x000F;
	public static int saorao = 0x000F0;
	public static int tuixiao = 0x00F00;
	public static int gaoe = 0x0F000;
	public static int message = 0xF0000;
	static boolean CheackTags(int atags, int atag) {
		return ((atags & atag) == atag);
	}

	static int SetTags(int atags, int atag) {
		atags |= atag;
		return atags;
	}

	static int UnsetTags(int atags, int atag) {
		atags &= (~atag);
		return atags;
	}
	//转化字符串为十六进制编码
	public static String toHexString(String s)   
	{   
	String str="";   
	for (int i=0;i<s.length();i++)   
	{   
	int ch = (int)s.charAt(i);   
	String s4 = Integer.toHexString(ch);   
	str = str + s4; 
	}   
	return str;   
	} 
}