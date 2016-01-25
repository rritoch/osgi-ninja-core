package com.vnetpublishing.java.osgi.tools.protocol;

import javax.xml.bind.annotation.XmlSeeAlso;

import com.vnetpublishing.java.osgi.tools.impl.OsgiDependencies;
import com.vnetpublishing.java.osgi.tools.impl.OsgiLink;
import com.vnetpublishing.java.osgi.tools.impl.OsgiMavenBundle;
import com.vnetpublishing.java.osgi.tools.impl.OsgiMavenLibrary;
import com.vnetpublishing.java.osgi.tools.impl.OsgiResourceBundle;


@XmlSeeAlso({OsgiMavenBundle.class,OsgiMavenLibrary.class,OsgiResourceBundle.class,OsgiLink.class})
public interface OsgiDependency {

	String getDependencyType();
	
	void setOwner(OsgiDependencies owner);
	
	OsgiDependencies getOwner();
}
