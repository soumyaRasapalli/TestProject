package com.pro;

public class Header {

	private String Id;
	private String Name;
	private String Age;
	private String Profession;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getAge() {
		return Age;
	}
	public void setAge(String age) {
		Age = age;
	}
	public String getProfession() {
		return Profession;
	}
	public void setProfession(String profession) {
		Profession = profession;
	}
	@Override
	public String toString() {
		return "Header [Id=" + Id + ", Name=" + Name + ", Age=" + Age + ", Profession=" + Profession + "]";
	}
	
	
	
}
