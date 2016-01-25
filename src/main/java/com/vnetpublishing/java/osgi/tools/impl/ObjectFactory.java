package com.vnetpublishing.java.osgi.tools.impl;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

	public OsgiDependencies createDeps() {
		return new OsgiDependencies();
	}
	
	public OsgiMavenBundle createMavenBundle() {
		return new OsgiMavenBundle();
	}
	
}
