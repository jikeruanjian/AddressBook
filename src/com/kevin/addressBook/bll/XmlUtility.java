package com.kevin.addressBook.bll;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import com.kevin.addressBook.model.AddressInfo;
import com.kevin.addressBook.model.Const;

public class XmlUtility {

	/**
	 * 在一个xml文件中，遍历xml文件，根据Element的elementName，取出所有复合条件的Element元素，
	 * 把它们转换为javabean类型，并放入List中返回
	 */
	public static List<AddressInfo> parseDocToObject(Document doc) {
		List<AddressInfo> result = new ArrayList<AddressInfo>();
		Element root = doc.getRootElement();
		List<Element> addressInfos = root.elements();
		AddressInfo ai = null;
		for (Element element : addressInfos) {
			ai = new AddressInfo();
			List<Element> atts = element.elements();
			Iterator it = element.attributeIterator();
			if (it.hasNext()) {
				Attribute attribute = (Attribute) it.next();
				if (attribute.getName().equals(Const.AddressInfo.ID)) {
					String id = attribute.getValue();
					if (id == null || id.equals("")) {
						id = UUID.randomUUID().toString(); // 如果还没有ID的值，则需要生产一个
						attribute.setText(id);
					}
					ai.setId(id);
				}
			} else {
				String id = UUID.randomUUID().toString();
				element.addAttribute(Const.AddressInfo.ID, id);
				ai.setId(id);
			}
			for (Element att : atts) {
				if (att.getName().equals(Const.AddressInfo.NAME)) {
					ai.setName(att.getTextTrim());
				} else if (att.getName().equals(Const.AddressInfo.POST)) {
					ai.setPost(att.getTextTrim());
				} else if (att.getName().equals(Const.AddressInfo.COMPANY)) {
					ai.setCompany(att.getTextTrim());
				} else if (att.getName().equals(Const.AddressInfo.ADDRESS)) {
					ai.setAddress(att.getTextTrim());
				} else if (att.getName().equals(Const.AddressInfo.PHONENUM)) {
					ai.setPhoneNum(att.getTextTrim());
				} else if (att.getName().equals(Const.AddressInfo.SALEINFO)) {
					ai.setSaleInfo(att.getTextTrim());
				} else if (att.getName().equals(Const.AddressInfo.PURCHASEINFO)) {
					ai.setPurchaseInfo(att.getTextTrim());
				} else if (att.getName().equals(Const.AddressInfo.IMAGENAME)) {
					ai.setImageName(att.getTextTrim());
				}
			}
			result.add(ai);
		}
		return result;
	}

	/**
	 * 根据xml文件的文件名，把xml文件转换成Document对象并返回
	 */
	public static Document getDocument(String fileUrl) throws DocumentException {
		if (fileUrl == null || fileUrl.equals(""))
			return DocumentHelper.createDocument();
		File file = new File(fileUrl);
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		return document;
	}

	/**
	 * 根据Element元素的javabean对象以及Element元素的elementName在Element root下增加一个Element 。
	 */
	public static void addElement(Element root, AddressInfo addressInfo) {
		Element element = root.addElement(Const.AddressInfo.ROWNAME);
		element.addAttribute(Const.AddressInfo.ID, addressInfo.getId());
		element.addElement(Const.AddressInfo.NAME).setText(
				addressInfo.getName());
		element.addElement(Const.AddressInfo.POST).setText(
				addressInfo.getPost());
		element.addElement(Const.AddressInfo.ADDRESS).setText(
				addressInfo.getAddress());
		element.addElement(Const.AddressInfo.COMPANY).setText(
				addressInfo.getCompany());
		element.addElement(Const.AddressInfo.PHONENUM).setText(
				addressInfo.getPhoneNum());
		element.addElement(Const.AddressInfo.SALEINFO).setText(
				addressInfo.getSaleInfo());
		element.addElement(Const.AddressInfo.PURCHASEINFO).setText(
				addressInfo.getPurchaseInfo());
		element.addElement(Const.AddressInfo.IMAGENAME).setText(
				addressInfo.getImageName());
	}

	/**
	 * 根据Element元素的javabean对象以及Element元素的elementName在Element root下增加一个Element 。
	 */
	public static void editElement(Element root, AddressInfo addressInfo) {
		List<Element> elements = root.elements();
		for (Element element : elements) {
			Iterator it = element.attributeIterator();
			if (it.hasNext()) {
				Attribute attribute = (Attribute) it.next();
				if (attribute.getName().equals(Const.AddressInfo.ID)) {
					String id = attribute.getValue();
					if (id.equals(addressInfo.getId())) {
						List<Element> attrs = element.elements();
						for (Element att : attrs) {
							if (att.getName().equals(Const.AddressInfo.NAME)) {
								att.setText(addressInfo.getName());
							} else if (att.getName().equals(
									Const.AddressInfo.POST)) {
								att.setText(addressInfo.getPost());
							} else if (att.getName().equals(
									Const.AddressInfo.COMPANY)) {
								att.setText(addressInfo.getCompany());
							} else if (att.getName().equals(
									Const.AddressInfo.ADDRESS)) {
								att.setText(addressInfo.getAddress());
							} else if (att.getName().equals(
									Const.AddressInfo.PHONENUM)) {
								att.setText(addressInfo.getPhoneNum());
							} else if (att.getName().equals(
									Const.AddressInfo.SALEINFO)) {
								att.setText(addressInfo.getSaleInfo());
							} else if (att.getName().equals(
									Const.AddressInfo.PURCHASEINFO)) {
								att.setText(addressInfo.getPurchaseInfo());
							} else if (att.getName().equals(
									Const.AddressInfo.IMAGENAME)) {
								att.setText(addressInfo.getImageName());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 在Document对象中，以elementName，attributeName，attributeValue为参数删除对应的Element元素。
	 */
	public static boolean removeElementByAttribute(Document document, String id) {
		List<Element> list = document.getRootElement().elements(
				Const.AddressInfo.ROWNAME);
		for (Element element : list) {
			for (Iterator it = element.attributeIterator(); it.hasNext();) {
				Attribute attribute = (Attribute) it.next();
				if (attribute.getName().equals(Const.AddressInfo.ID)
						&& attribute.getValue().equals(id)) {
					element.getParent().remove(element);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 把Document对象与filePath对应的物理文件进行同步。
	 */
	public static void writeDocumentToFile(Document document, String filePath)
			throws IOException {
		XMLWriter writer = new XMLWriter(new FileWriter(filePath));
		writer.write(document);
		writer.flush();
		writer.close();
	}

}
