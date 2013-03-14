package com.kevin.addressBook.tools;

import java.util.Comparator;

import com.kevin.addressBook.model.AddressInfo;

public class ComparatorUser implements Comparator<AddressInfo> {
	@Override
	public int compare(AddressInfo lhs, AddressInfo rhs) {
		int flag = lhs.getName().compareTo(rhs.getName());
		if (flag == 0) {
			return 1;
		} else {
			return flag;
		}
	}
}