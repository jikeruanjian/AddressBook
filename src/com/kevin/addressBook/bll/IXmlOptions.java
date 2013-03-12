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
	 * 获取所有用户
	 * @return 所有有效用户的List集合
	 */
	public List<AddressInfo> getAllUsers();
	
	/**
	 * 通过用户ID获取一个用户的详细信息
	 * @param id
	 * @return addressInfo
	 */
	public AddressInfo getUserDetails(String id); 
	
	/**
	 * 增加一个用户
	 * @param user
	 * @return
	 */
	public Boolean addUser(AddressInfo user);
	
	/**
	 * 通过ID删除一个用户
	 * @param id
	 * @return
	 */
	public Boolean deleteUser(String id);
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	public Boolean editUser(AddressInfo user);
}
