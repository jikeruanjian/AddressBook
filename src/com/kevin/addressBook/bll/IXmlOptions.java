package com.kevin.addressBook.bll;

import java.util.List;
import com.kevin.addressBook.model.AddressInfo;

/**
 * 
 * @author kevin
 *
 */
public interface IXmlOptions {
	
	/**
	 * ��ȡ�����û�
	 * @return ������Ч�û���List����
	 */
	public List<AddressInfo> getAllUsers();
	
	/**
	 * ͨ���û�ID��ȡһ���û�����ϸ��Ϣ
	 * @param id
	 * @return addressInfo
	 */
	public AddressInfo getUserDetails(String id); 
	
	/**
	 * ����һ���û�
	 * @param user
	 * @return
	 */
	public Boolean addUser(AddressInfo user);
	
	/**
	 * ͨ��IDɾ��һ���û�
	 * @param id
	 * @return
	 */
	public Boolean deleteUser(String id);
	
	/**
	 * �޸��û���Ϣ
	 * @param user
	 * @return
	 */
	public Boolean editUser(AddressInfo user);
}
