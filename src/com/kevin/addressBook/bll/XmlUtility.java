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
	 * ���xml�ļ����ļ����xml�ļ�ת����Document���󲢷���
	 */
	public static Document getDocument(String fileUrl) {
		if (fileUrl == null || fileUrl.equals(""))
			return null;
		File file = new File(fileUrl);
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(file);
		} catch (DocumentException e) {
			document = DocumentHelper.createDocument();
			document.addElement("list");// ��ڵ�
		}
		return document;
	}

	/**
	 * ��һ��xml�ļ��У�����xml�ļ������Element��elementName��ȡ�����и���������ElementԪ�أ�
	 * ������ת��Ϊjavabean���ͣ�������List�з���
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
						id = UUID.randomUUID().toString(); // ���û��ID��ֵ������Ҫ���һ��
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
	 * ���ElementԪ�ص�javabean�����Լ�ElementԪ�ص�elementName��Element root������һ��Element ��
	 */
	public static boolean addElement(Element root, AddressInfo addressInfo) {
		try {
			Element element = root.addElement(Const.AddressInfo.ROWNAME);
			addressInfo.setId(UUID.randomUUID().toString());
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
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * ���ElementԪ�ص�javabean�����Լ�ElementԪ�ص�elementName��Element root������һ��Element ��
	 */
	public static boolean editElement(Element root, AddressInfo addressInfo) {
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
				return true;
			}
		}
		return false;
	}

	/**
	 * ��Document�����У���elementName��attributeName��attributeValueΪ����ɾ���Ӧ��ElementԪ�ء�
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
	 * ��Document������filePath��Ӧ�������ļ�����ͬ����
	 */
	public static void writeDocumentToFile(Document document, String filePath)
			throws IOException {
		XMLWriter writer = new XMLWriter(new FileWriter(filePath));
		writer.write(document);
		writer.flush();
		writer.close();
	}

}
