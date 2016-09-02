package com.redlichee.uwinmes.model;

/**
 * 任务单
 */
public class jobOrder {

	private String department;// 部门
	private String serialNumber;// 流水号
	private String inventoryCoding;// 存货编码
	private String inventoryName;// 存货名称
	private String specifications;// 规格型号
	private String process;// 工序
	private String quantity;// 报检数量
	private String units;// 单位


	private String type;// 类型
	private String date;// 时间
	private String name;// 姓名

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getInventoryCoding() {
		return inventoryCoding;
	}

	public void setInventoryCoding(String inventoryCoding) {
		this.inventoryCoding = inventoryCoding;
	}

	public String getInventoryName() {
		return inventoryName;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public jobOrder(String type, String date, String name) {
		this.type = type;
		this.date = date;
		this.name = name;
	}

	public jobOrder(String department, String serialNumber, String inventoryCoding, String inventoryName, String specifications, String process, String quantity, String units) {
		this.department = department;
		this.serialNumber = serialNumber;
		this.inventoryCoding = inventoryCoding;
		this.inventoryName = inventoryName;
		this.specifications = specifications;
		this.process = process;
		this.quantity = quantity;
		this.units = units;
	}
}
