package com.lemon.base.bean.user;

public class UserGroup {

	/**分组id*/
	private Integer id;
	/**分组名字*/
	private String name;
	/**分组内用户数量*/
	private int count;
	
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
