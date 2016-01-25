package com.vnetpublishing.java.osgi.tools.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (
	name = "valueList",
	namespace = "urn:com:vnetpublishing:osgi:dependencies"
)
public class OsgiWrapInstrValueList {

	@XmlElement(name="value")
	List<String> values = new ArrayList<String>();

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> value) {
		this.values = value;
	}
	
	@Override
	public String toString() {
		return String.join(",", values);
	}
	
}
