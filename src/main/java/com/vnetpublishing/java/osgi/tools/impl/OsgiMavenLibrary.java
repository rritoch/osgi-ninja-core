package com.vnetpublishing.java.osgi.tools.impl;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.osgi.framework.Version;

import com.vnetpublishing.java.osgi.tools.model.OsgiArtifactRefName;
import com.vnetpublishing.java.osgi.tools.model.OsgiMavenArtifactConfig;
import com.vnetpublishing.java.osgi.tools.protocol.OsgiArtifact;
import com.vnetpublishing.java.osgi.tools.protocol.OsgiArtifactConfig;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (
	name = "mavenLibrary",
	namespace = "urn:com:vnetpublishing:osgi:dependencies"
)
public class OsgiMavenLibrary extends AOsgiDependency implements OsgiArtifact {
	
	public static final String DEPENDENCY_TYPE = "MAVEN_LIBRARY";
	
	@XmlElement
	private String groupId;
	
	@XmlElement
	private String artifactId;
	
	@XmlElement
	private String version;
	
	@XmlElement
	private String resources;
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getDependencyType() {
		return DEPENDENCY_TYPE;
	}

	public URL getURL() throws MalformedURLException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("mvn:");
		sb.append(String.valueOf(groupId));
		sb.append("/");
		sb.append(String.valueOf(artifactId));
		sb.append("/");
		sb.append(String.valueOf(version));
		
		final String oldProp = System.getProperty("java.protocol.handler.pkgs");
		
		try {
			System.setProperty("java.protocol.handler.pkgs", "org.ops4j.pax.url");
			URL url = new URL(sb.toString());		
			return url;
		
		} finally {
			if (oldProp == null) {
				System.clearProperty("java.protocol.handler.pkgs");
			} else {
				System.setProperty("java.protocol.handler.pkgs",oldProp);
			}
		}
	}

	public final String cleanVersion(String version) {
		if (version.indexOf("-") < 0) {
			return version;
		}
		return version.substring(0,version.indexOf("-"));
	}
	
	public OsgiArtifactRefName getRefName() {
		OsgiArtifactConfig cfg = new OsgiMavenArtifactConfig(this);
		
		return new OsgiArtifactRefName(
				"mvn", 
				groupId,
				artifactId,
				new Version(cleanVersion(version)),
				cfg
			);
	}
	

	
}
