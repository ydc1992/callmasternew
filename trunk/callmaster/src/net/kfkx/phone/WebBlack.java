package net.kfkx.phone;

public class WebBlack {
	public static int TYPE_ONESOUND = 0;
	public static int TYPE_OVERCHARGE = 1;
	public static int TYPE_PROMOTION = 2;
	public static int TYPE_OTHER = 3;
	public static int TYPE_MESSAGE =4;
	private Integer blackid;
	private String number;
	private Integer type;
	private String remark;
	private String timehappen;
	private Integer timelength;
	private Integer version;
	
	public WebBlack(){}
	

	public WebBlack(Integer blackid, String number, Integer type, String remark,
			String timehappen, Integer timelength, Integer version) {
		this.blackid = blackid;
		this.number = number;
		this.type = type;
		this.remark = remark;
		this.timehappen = timehappen;
		this.timelength = timelength;
		this.version = version;
	}

	public WebBlack(String number, Integer type, String remark,
			String timehappen, Integer timelength) {
		this.number = number;
		this.type = type;
		this.remark = remark;
		this.timehappen = timehappen;
		this.timelength = timelength;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTimehappen() {
		return timehappen;
	}

	public void setTimehappen(String timehappen) {
		this.timehappen = timehappen;
	}

	public Integer getTimelength() {
		return timelength;
	}

	public void setTimelength(Integer timelength) {
		this.timelength = timelength;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
