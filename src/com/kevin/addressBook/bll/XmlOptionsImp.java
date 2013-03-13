package com.kevin.addressBook.bll;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;

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
			try { //这里的方式还需要该动
				XmlOptionsImp.doc = XmlUtility.getDocument(XmlOptionsImp.path);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
