package cn.opda.phone;

public class WebBlack {
	private Integer blackid;
	private String number;
	private String type;
	private String remark;
	private String timehappen;
	private Integer timelength;
	public WebBlack(){}
	
	public WebBlack(Integer blackid, String number, String type, String remark,
			String timehappen, Integer timelength) {
		this.blackid = blackid;
		this.number = number;
		this.type = type;
		this.remark = remark;
		this.timehappen = timehappen;
		this.timelength = timelength;
	}

	public WebBlack(String number, String type, String remark,
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
