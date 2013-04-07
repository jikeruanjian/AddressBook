package com.kevin.addressBook.bll;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import com.kevin.addressBook.model.AddressInfo;

public class XmlOptionsImp implements IXmlOptions {

	private static XmlOptionsImp instance = null;

	private static String path = "";
	public static boolean isLogin = false;
	private static Document doc = null;
	private static List<AddressInfo> allAddressInfos = null; // 存放所有的数据
	public static String getPath() {
		return path;
	}

	private XmlOptionsImp() {
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
		if (doc != null) {
			allAddressInfos = XmlUtility.parseDocToObject(doc);
		}
		instance = new XmlOptionsImp();
	}

	@Override
	public List<AddressInfo> getAllUsers() {
		if (allAddressInfos != null && allAddressInfos.size() > 0) {
			return allAddressInfos;
		}
		if (doc != null) {
			allAddressInfos = XmlUtility.parseDocToObject(doc);
			return allAddressInfos;
		}
		return null;
	}

	@Override
	public AddressInfo getUserDetails(String id) {
		AddressInfo ai = null;
		for (AddressInfo addressInfo : allAddressInfos) {
			if (addressInfo.getId().equals(id)) {
				ai = addressInfo;
				break;
			}
		}
		return ai;
	}

	@Override
	public Boolean addUser(AddressInfo user) throws IOException {
		if (XmlUtility.addElement(doc.getRootElement(), user)) {
			allAddressInfos.add(user);
			XmlUtility.writeDocumentToFile(doc, path);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean deleteUser(String id) throws IOException {
		if (XmlUtility.removeElementByAttribute(doc, id)) {
			XmlUtility.writeDocumentToFile(doc, path);
			for (Iterator<AddressInfo> it = allAddressInfos.iterator(); it
					.hasNext();) {
				if (it.next().getId().equals(id)) {
					it.remove();
				}
			}
			return true;
		} else
			return false;
	}

	@Override
	public Boolean editUser(AddressInfo user) throws Exception {
		if (user.getId() == null || user.getId().equals("")) {
			throw new Exception("未指定要修改信息的ID");
		}
		if (XmlUtility.editElement(doc.getRootElement(), user)) {
			AddressInfo ai = null;
			for (Iterator<AddressInfo> it = allAddressInfos.iterator(); it
					.hasNext();) {
				ai = it.next();
				if (ai.getId().equals(user.getId())) {
					ai = user;
				}
			}
			XmlUtility.writeDocumentToFile(doc, path);
			return true;
		} else
			return false;
	}

}
