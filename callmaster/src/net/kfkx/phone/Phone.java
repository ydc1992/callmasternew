package net.kfkx.phone;

public class Phone {
	private Integer phoneid;
	private String province;
	private String city;
	private String areaCode;
	public String getProvince() {
		return province;
	}
	public Phone(){}
	
	public Phone(String province, String city, String areaCode) {
		this.province = province;
		this.city = city;
		this.areaCode = areaCode;
	}
	public Phone(Integer phoneid, String province, String city,
			String areaCode) {
		this.phoneid = phoneid;
		this.province = province;
		this.city = city;
		this.areaCode = areaCode;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public Integer getPhoneid() {
		return phoneid;
	}
	public void setPhoneid(Integer phoneid) {
		this.phoneid = phoneid;
	}
}
