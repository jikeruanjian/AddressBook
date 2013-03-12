package com.kevin.addressBook.bll;

import java.util.List;

import org.dom4j.Document;

import com.kevin.addressBook.model.AddressInfo;

public class XmlOptionsImp implements IXmlOptions {

	private static XmlOptionsImp instance = null;

	private static String path = "";
	private static Document doc = null;

	private XmlOptionsImp() {
		// Exists only to defeat instantiation.
	}

	public static XmlOptionsImp getInstance(String path) {
		if (instance == null) {
			XmlOptionsImp.path = path;
			instance = new XmlOptionsImp();
		}
		return instance;
	}

	@Override
	public List<AddressInfo> getAllUsers() {

		return null;
	}

	@Override
	public AddressInfo getUserDetails(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean addUser(AddressInfo user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteUser(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean editUser(AddressInfo user) {
		// TODO Auto-generated method stub
		return null;
	}

}
