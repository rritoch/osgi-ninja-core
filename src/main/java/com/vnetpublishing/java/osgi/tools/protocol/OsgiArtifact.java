package com.vnetpublishing.java.osgi.tools.protocol;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.vnetpublishing.java.osgi.tools.model.OsgiArtifactRefName;

public interface OsgiArtifact {

	URL getURL() throws MalformedURLException, URISyntaxException;
	
	OsgiArtifactRefName getRefName();
	
}
