package com.vnetpublishing.java.osgi.tools.impl;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
//import java.util.logging.Logger;

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
	name = "mavenBundle",
	namespace = "urn:com:vnetpublishing:osgi:dependencies"
)
public class OsgiMavenBundle extends AOsgiDependency implements OsgiArtifact  {

	//public static final Logger LOGGER = Logger.getLogger(OsgiMavenBundle.class.getName());
	
	public static final String DEPENDENCY_TYPE = "MAVEN_BUNDLE";
	
	@XmlElement
	private String groupId;
	
	@XmlElement
	private String artifactId;
	
	@XmlElement
	private String version;
	
	@XmlElement
	private String bundleVersion;
	
	@XmlElement
	private boolean isWar = false;
	
	@XmlElement
	private OsgiMavenWrapConfiguration wrap = null;
	
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
	
	public String getBundleVersion() {
		return bundleVersion;
	}
	public void setBudleVersion(String bundleVersion) {
		this.bundleVersion = bundleVersion;
	}
	
	
	public OsgiMavenWrapConfiguration getWrap() {
		return wrap;
	}
	public void setWrap(OsgiMavenWrapConfiguration wrap) {
		this.wrap = wrap;
	}
	public URL getURL() throws MalformedURLException, URISyntaxException {
		
		StringBuilder sb = new StringBuilder();
		
		if (wrap != null) {
			sb.append("wrap:");
		}
		
		if (isWar) {
			sb.append("war:");
		}
		sb.append("mvn:");
		sb.append(String.valueOf(groupId));
		sb.append("/");
		sb.append(String.valueOf(artifactId));
		sb.append("/");
		sb.append(String.valueOf(version));
		
		if (isWar) {
			sb.append("/war");
		}
		//TODO: Handle bnd file
		
		if (wrap != null) {
			String wrapCfg = wrap.toURIString();
			if (wrapCfg.length() > 0) {
				sb.append("$");
				sb.append(wrapCfg);
			}
		}
		
		final String oldProp = System.getProperty("java.protocol.handler.pkgs");
		
		try {
			System.setProperty("java.protocol.handler.pkgs", "org.ops4j.pax.url");
			
			String uri = sb.toString();
			try {
				URL url = new URL(uri);
				return url;
			} catch (final MalformedURLException e) {
				String err = String.format("Problem with URL \"%s\"",uri);
				throw new MalformedURLException(err) {
					private static final long serialVersionUID = 292834072479134516L;
					@Override
					public Throwable getCause() {
						return e;
					}
				};
			}
			
		
		} finally {
			if (oldProp == null) {
				System.clearProperty("java.protocol.handler.pkgs");
			} else {
				System.setProperty("java.protocol.handler.pkgs",oldProp);
			}
		}
		
	}
	
	public boolean isWar() {
		return isWar;
	}
	
	public void setWar(boolean isWar) {
		this.isWar = isWar;
	}
	
	public OsgiArtifactRefName getRefName() {
		
		OsgiArtifactConfig cfg = new OsgiMavenArtifactConfig(this);
		
		
		Version v = new Version(bundleVersion == null ? version : bundleVersion);
		
		return new OsgiArtifactRefName(
				"mvn", 
				groupId,
				artifactId,
				v,
				cfg
			);
	}
	public String getDependencyType() {
		return DEPENDENCY_TYPE;
	}
}
