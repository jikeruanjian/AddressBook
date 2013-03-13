package com.kevin.addressBook.model;

/**
 * �û���Ϣ
 * 
 * @author kevin
 * 
 */
public class AddressInfo {

	private String id = ""; // Ψһ�ı��
	private String name = ""; // ����
	private String post = ""; // ְ��
	private String company = ""; // ��λ
	private String address = ""; // ��ַ
	private String phoneNum = ""; // �绰
	private String saleInfo = ""; // ������Ϣ
	private String purchaseInfo = ""; // ����Ϣ
	private String imageName = ""; // ͷ��

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getSaleInfo() {
		return saleInfo;
	}

	public void setSaleInfo(String saleInfo) {
		this.saleInfo = saleInfo;
	}

	public String getPurchaseInfo() {
		return purchaseInfo;
	}

	public void setPurchaseInfo(String purchaseInfo) {
		this.purchaseInfo = purchaseInfo;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}
