package com.tablecross.api.model;

import java.math.BigDecimal;
import java.util.Date;

public class UsersDTO {
	private Integer id;
	private String email;
	private String password;
	private String mobile;
	private BigDecimal point;
	private Integer userType;
	private Integer parentId;
	private Integer level;
	private String createBy;
	private Date createdDate;
	private Long orderCount;
	private Integer notifyOrder;
	private Integer notifyRestaurant;
	private Integer notifyBeforeDate;
	private Date birthday;
	private Integer areaId;

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Long orderCount) {
		this.orderCount = orderCount;
	}

	public Integer getNotifyOrder() {
		return notifyOrder;
	}

	public void setNotifyOrder(Integer notifyOrder) {
		this.notifyOrder = notifyOrder;
	}

	public Integer getNotifyRestaurant() {
		return notifyRestaurant;
	}

	public void setNotifyRestaurant(Integer notifyRestaurant) {
		this.notifyRestaurant = notifyRestaurant;
	}

	public Integer getNotifyBeforeDate() {
		return notifyBeforeDate;
	}

	public void setNotifyBeforeDate(Integer notifyBeforeDate) {
		this.notifyBeforeDate = notifyBeforeDate;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public BigDecimal getPoint() {
		return point;
	}

	public void setPoint(BigDecimal point) {
		this.point = point;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
