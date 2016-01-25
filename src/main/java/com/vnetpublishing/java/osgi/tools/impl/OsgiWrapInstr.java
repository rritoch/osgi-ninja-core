package com.vnetpublishing.java.osgi.tools.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (
	name = "instr",  
	namespace = "urn:com:vnetpublishing:osgi:dependencies"
)

public class OsgiWrapInstr {

	@XmlElement
	private String name;
	
	@XmlElements({
		 @XmlElement(name="value",type=String.class),
		 @XmlElement(name="valueList",type=OsgiWrapInstrValueList.class)
	})
	private Object value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String toURIString() {
		try {
			return String.format("%s=%s", 
				URLEncoder.encode(name,"UTF-8").replace("+", "%20"),
				URLEncoder.encode(String.valueOf(value),"UTF-8")).replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
