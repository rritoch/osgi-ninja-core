package com.vnetpublishing.java.osgi.tools.model;

import com.vnetpublishing.java.osgi.tools.impl.OsgiResourceBundle;
import com.vnetpublishing.java.osgi.tools.protocol.OsgiArtifactConfig;

public class OsgiResourceArtifactConfig implements OsgiArtifactConfig {

	OsgiResourceBundle osgiResourceBundle;
	public OsgiResourceArtifactConfig(OsgiResourceBundle osgiResourceBundle) {
		this.osgiResourceBundle = osgiResourceBundle;
	}

	public String getNormalConfigName() {
		if (osgiResourceBundle.getWrap() == null) {
			return "NOWRAP";
		}
		return String.format(
			"WRAP:%s",	
			osgiResourceBundle.getWrap().toNormalURIString()
		);
	}
}