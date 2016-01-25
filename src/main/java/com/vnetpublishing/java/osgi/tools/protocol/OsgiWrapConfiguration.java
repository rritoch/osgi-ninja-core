package com.vnetpublishing.java.osgi.tools.protocol;

import javax.xml.bind.annotation.XmlSeeAlso;

import com.vnetpublishing.java.osgi.tools.impl.OsgiMavenWrapConfiguration;
import com.vnetpublishing.java.osgi.tools.impl.OsgiResourceWrapConfiguration;


@XmlSeeAlso({OsgiMavenWrapConfiguration.class,OsgiResourceWrapConfiguration.class})
public interface OsgiWrapConfiguration {

	String toURIString();
	String toNormalURIString();
}
