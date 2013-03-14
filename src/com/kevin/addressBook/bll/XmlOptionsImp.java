package com.kevin.addressBook.bll;

import java.io.IOException;
import java.util.List;

import org.dom4j.Document;

import android.util.Log;

import com.kevin.addressBook.model.AddressInfo;

public class XmlOptionsImp implements IXmlOptions {

	private static XmlOptionsImp instance = null;

	private static String path = "";

	public static String getPath() {
		return path;
	}

	private static Document doc = null;

	private XmlOptionsImp() {
		// Exists only to defeat instantiation.
	}

	public static XmlOptionsImp getInstance() {
		if (instance == null || !XmlOptionsImp.path.equals(path)) {
			XmlOptionsImp.doc = XmlUtility.getDocument(XmlOptionsImp.path);
			instance = new XmlOptionsImp();
		}
		return instance;
	}

	/**
	 * 
	 * @param path
	 */
	public static void setPath(String path) {
		XmlOptionsImp.path = path;
		XmlOptionsImp.doc = XmlUtility.getDocument(XmlOptionsImp.path);
		instance = new XmlOptionsImp();
	}

	@Override
	public List<AddressInfo> getAllUsers() {
		return XmlUtility.parseDocToObject(doc);
	}

	@Override
	public AddressInfo getUserDetails(String id) {
		AddressInfo ai = null;
		List<AddressInfo> aiList = XmlUtility.parseDocToObject(doc);
		for (AddressInfo addressInfo : aiList) {
			if (addressInfo.getId().equals(id)) {
				ai = addressInfo;
				break;
			}
		}
		return ai;
	}

	@Override
	public Boolean addUser(AddressInfo user) throws IOException {
		Log.i("write", "准备添加写");
		if (XmlUtility.addElement(doc.getRootElement(), user)) {
			XmlUtility.writeDocumentToFile(doc, path);
			Log.i("write", "写完了:" + doc.asXML());
			return true;
		} else {
			Log.i("write", "添加失败");
			return false;
		}
	}

	@Override
	public Boolean deleteUser(String id) throws IOException {
		if (XmlUtility.removeElementByAttribute(doc, id)) {
			XmlUtility.writeDocumentToFile(doc, path);
			return true;
		} else
			return false;
	}

	@Override
	public Boolean editUser(AddressInfo user) throws IOException {
		// TODO Auto-generated method stub
		if (XmlUtility.editElement(doc.getRootElement(), user)) {
			XmlUtility.writeDocumentToFile(doc, path);
			return true;
		} else
			return false;
	}

}
