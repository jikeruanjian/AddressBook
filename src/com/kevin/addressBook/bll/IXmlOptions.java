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
	 * 获取到所有用户
	 * 
	 * @return 所有用户数据的List
	 */
	public List<AddressInfo> getAllUsers();

	/**
	 * 获取某条数据的详细
	 * 
	 * @param id
	 * @return addressInfo
	 */
	public AddressInfo getUserDetails(String id);

	/**
	 * 添加一个用户
	 * 
	 * @param user
	 * @return
	 * @throws IOException
	 */
	public Boolean addUser(AddressInfo user) throws IOException;

	/**
	 * 删除一个用户
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public Boolean deleteUser(String id) throws IOException;

	/**
	 * 编辑用户信息
	 * 
	 * @param user
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public Boolean editUser(AddressInfo user) throws IOException, Exception;
}
