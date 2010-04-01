package net.kfkx.phone;

public class Blacklist {
	public static int HAVED = 1;
	public static int HAVE_NO = 0;
	public static int TYPE_ONESOUND = 0;
	public static int TYPE_OVERCHARGE = 1;
	public static int TYPE_PROMOTION = 2;
	public static int TYPE_OTHER = 3;
	public static int TYPE_MESSAGE =4;
	private Integer blackid;
	private String number;
	private int type;
	private String remark;
	private String timehappen;
	private Integer timelength;
	private int uptype;
	public Blacklist(){}
	public Blacklist(Integer blackid, String number, int type,
			String remark, String timehappen, Integer timelength, int uptype) {
		this.blackid = blackid;
		this.number = number;
		this.type = type;
		this.remark = remark;
		this.timehappen = timehappen;
		this.timelength = timelength;
		this.uptype = uptype;
	}
	public Blacklist(String number, int type, String remark,
			String timehappen, Integer timelength, int uptype) {
		this.number = number;
		this.type = type;
		this.remark = remark;
		this.timehappen = timehappen;
		this.timelength = timelength;
		this.uptype = uptype;
	}
	public Blacklist(String number, int type, String remark, int uptype) {
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
