package cn.kfkx.phone;

public class Contact {
	private Integer id;
	private String name;
	private String phone;
	private String area;
	public Contact(){}
	public Contact(String name, String phone, String area) {
		this.name = name;
		this.phone = phone;
		this.area = area;
	}
	public Contact(Integer id, String name, String phone, String area) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.area = area;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
}
