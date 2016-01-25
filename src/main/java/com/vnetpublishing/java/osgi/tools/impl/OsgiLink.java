package com.vnetpublishing.java.osgi.tools.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (
	name = "link",
	namespace = "urn:com:vnetpublishing:osgi:dependencies"
)
public class OsgiLink extends AOsgiDependency {
	public static final String DEPENDENCY_TYPE = "LINK";

	public String getDependencyType() {
		return DEPENDENCY_TYPE;
	}
	
	@XmlAttribute(name="href", required = true)
	private String href;
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
}

