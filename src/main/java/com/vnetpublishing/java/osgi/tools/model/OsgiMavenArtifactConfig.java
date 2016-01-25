package com.vnetpublishing.java.osgi.tools.model;

import com.vnetpublishing.java.osgi.tools.impl.OsgiMavenBundle;
import com.vnetpublishing.java.osgi.tools.impl.OsgiMavenLibrary;
import com.vnetpublishing.java.osgi.tools.protocol.OsgiArtifactConfig;

public class OsgiMavenArtifactConfig implements OsgiArtifactConfig {

	OsgiMavenBundle osgiMavenArtifact;
	public OsgiMavenArtifactConfig(OsgiMavenBundle osgiMavenArtifact) {
		this.osgiMavenArtifact = osgiMavenArtifact;
	}

	public OsgiMavenArtifactConfig(OsgiMavenLibrary osgiMavenLibrary) {
		this.osgiMavenArtifact = null;
	}

	public String getNormalConfigName() {
		if (osgiMavenArtifact == null || osgiMavenArtifact.getWrap() == null) {
			return "NOWRAP";
		}
		return String.format(
			"WRAP:%s",	
			osgiMavenArtifact.getWrap().toNormalURIString()
		);
	}
}
