package com.vnetpublishing.java.osgi.tools.impl;


import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.osgi.framework.Version;

import com.vnetpublishing.java.osgi.tools.model.OsgiArtifactRefName;
import com.vnetpublishing.java.osgi.tools.model.OsgiResourceArtifactConfig;
import com.vnetpublishing.java.osgi.tools.protocol.OsgiArtifact;
import com.vnetpublishing.java.osgi.tools.protocol.OsgiArtifactConfig;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (
	name = "resourceBundle",  
	namespace = "urn:com:vnetpublishing:osgi:dependencies"
)
public class OsgiResourceBundle extends AOsgiDependency implements OsgiArtifact {
	
	public static final String DEPENDENCY_TYPE = "RESOURCE_BUNDLE";
	

	
    @XmlElement
    private String artifactId;
    
    @XmlElement
    private String version;
    
    @XmlElement
	private String resource;
	
	@XmlElement
	private OsgiResourceWrapConfiguration wrap = null;
	

	public OsgiResourceWrapConfiguration getWrap() {
		return wrap;
	}
	public void setWrap(OsgiResourceWrapConfiguration resourceWrap) {
		this.wrap = resourceWrap;
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
	
	public URL getURL() throws MalformedURLException, URISyntaxException {
		StringBuilder sb = new StringBuilder();
		
		if (wrap != null) {
			sb.append("wrap:");
		}
		
		
		
		sb.append(getOwner().resolve(resource).toString());
		
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
	
	public OsgiArtifactRefName getRefName() {
		
		String domain = "local";
		
		String proto = "";
		
		URL u;
		try {
			u = new URL(resource);
			proto = u.getProtocol();
			
			if ("http".equalsIgnoreCase(proto) || 
					"https".equalsIgnoreCase(proto) ||
					"ftp".equalsIgnoreCase(proto)
			) {
				domain = u.getHost();
			}
			
		} catch (MalformedURLException e) {
			//e.printStackTrace();
		}
		
		OsgiArtifactConfig cfg = new OsgiResourceArtifactConfig(this);
		return new OsgiArtifactRefName(
				"resource", 
				domain,
				artifactId,
				new Version(version),
				cfg
			);
		}
	
	public String getDependencyType() {
		return DEPENDENCY_TYPE;
	}
}

