package com.kevin.addressBook.bll;

import java.io.IOException;
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
	 * @throws IOException 
	 */
	public Boolean addUser(AddressInfo user) throws IOException;
	
	/**
	 * ͨ��IDɾ��һ���û�
	 * @param id
	 * @return
	 * @throws IOException 
	 */
	public Boolean deleteUser(String id) throws IOException;
	
	/**
	 * �޸��û���Ϣ
	 * @param user
	 * @return
	 * @throws IOException 
	 */
	public Boolean editUser(AddressInfo user) throws IOException;
}
