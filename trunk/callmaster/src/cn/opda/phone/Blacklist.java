package cn.opda.phone;

public class Blacklist {
	public static int HAVED = 1;
	public static int HAVE_NO = 0;
	public static String TYPE_ONESOUND = "一声响";
	public static String TYPE_OVERCHARGE = "高额收费";
	public static String TYPE_PROMOTION = "推销";
	public static String TYPE_OTHER = "其他";
	public static String TYPE_MESSAGE ="电话骚扰";
	private Integer blackid;
	private String number;
	private String type;
	private String remark;
	private String timehappen;
	private Integer timelength;
	private int uptype;
	public Blacklist(){}
	public Blacklist(Integer blackid, String number, String type,
			String remark, String timehappen, Integer timelength, int uptype) {
		this.blackid = blackid;
		this.number = number;
		this.type = type;
		this.remark = remark;
		this.timehappen = timehappen;
		this.timelength = timelength;
		this.uptype = uptype;
	}
	public Blacklist(String number, String type, String remark,
			String timehappen, Integer timelength, int uptype) {
		this.number = number;
		this.type = type;
		this.remark = remark;
		this.timehappen = timehappen;
		this.timelength = timelength;
		this.uptype = uptype;
	}
	public Blacklist(String number, String type, String remark, int uptype) {
		this.number = number;
		this.type = type;
		this.remark = remark;
		this.uptype = uptype;
	}
	public int getUptype() {
		return uptype;
	}
	public void setUptype(int uptype) {
		this.uptype = uptype;
	}
	public Integer getTimelength() {
		return timelength;
	}
	public void setTimelength(Integer timelength) {
		this.timelength = timelength;
	}
	public String getTimehappen() {
		return timehappen;
	}
	public void setTimehappen(String timehappen) {
		this.timehappen = timehappen;
	}
	public Integer getBlackid() {
		return blackid;
	}
	public void setBlackid(Integer blackid) {
		this.blackid = blackid;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
