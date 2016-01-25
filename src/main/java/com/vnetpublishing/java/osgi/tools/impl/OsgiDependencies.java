package com.vnetpublishing.java.osgi.tools.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.helpers.DefaultValidationEventHandler;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (
	name = "deps", 
	namespace = "urn:com:vnetpublishing:osgi:dependencies" 
)

public class OsgiDependencies {

	public static final String OSGIDEPS_PACKAGE = "com.vnetpublishing.java.osgi.tools.impl";
	
	private URL resource;
	
	@XmlElements({
		 @XmlElement(name="mavenBundle",type=OsgiMavenBundle.class),
		 @XmlElement(name="resourceBundle",type=OsgiResourceBundle.class),
		 @XmlElement(name="mavenLibrary",type=OsgiMavenLibrary.class),
		 @XmlElement(name="link",type=OsgiLink.class)
	})
	List<AOsgiDependency> deps = new ArrayList<AOsgiDependency>();

	public List<AOsgiDependency> getDeps() {
		return deps;
	}

	public void setDeps(List<AOsgiDependency> deps) {
		this.deps = deps;
	}
	
	public String save() throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance( OSGIDEPS_PACKAGE );
		Marshaller m = ctx.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter sw = new StringWriter();
		m.marshal(this, sw);
		return sw.toString();
	}
	
	public void loadResource(URL resource) throws JAXBException, IOException {
		JAXBContext ctx = JAXBContext.newInstance( OSGIDEPS_PACKAGE );
		InputStream is = resource.openStream();
		Unmarshaller u = ctx.createUnmarshaller();
		u.setEventHandler(new DefaultValidationEventHandler());
		OsgiDependencies deps = (OsgiDependencies)u.unmarshal(is);
		this.deps = deps.getDeps();
		this.resource = resource;
	}
	
	public URL resolve(String href) throws MalformedURLException, URISyntaxException {
		URL r = resource.toURI().resolve(href).toURL();
		return r;
	}

	public void takeOwnershipOfChildren() {
		for(AOsgiDependency dep : deps) {
			dep.setOwner(this);
		}
	}
	
}
