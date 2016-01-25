package com.vnetpublishing.java.osgi.tools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.vnetpublishing.java.osgi.tools.impl.OsgiDependencies;
import com.vnetpublishing.java.osgi.tools.impl.OsgiLink;
import com.vnetpublishing.java.osgi.tools.impl.OsgiMavenBundle;
import com.vnetpublishing.java.osgi.tools.impl.OsgiMavenWrapConfiguration;
import com.vnetpublishing.java.osgi.tools.impl.OsgiWrapInstr;
import com.vnetpublishing.java.osgi.tools.impl.OsgiWrapInstrValueList;

public class ToolsTest {

	@Test
	public void mavenBundleGenTest() throws JAXBException {
		OsgiDependencies deps = new OsgiDependencies();
		OsgiMavenBundle bundle0 = new OsgiMavenBundle();
		
		bundle0.setGroupId("org.apache.servicemix.bundles");
		bundle0.setArtifactId("org.apache.servicemix.bundles.spring-core");
		bundle0.setVersion("4.2.4.RELEASE_1");
		
		OsgiWrapInstr instr0 = new OsgiWrapInstr();
		instr0.setName("Bundle-SymbolicName");
		instr0.setValue("my.bundle");
		
		OsgiWrapInstr instr1 = new OsgiWrapInstr();
		instr1.setName("Import-Package");
		
		OsgiWrapInstrValueList instr1values = new OsgiWrapInstrValueList();
		
		instr1values.getValues().add("clojure");
		instr1values.getValues().add("*;resolution:=optional");
		
		instr1.setValue(instr1values);
		
		
		OsgiMavenWrapConfiguration wrap0 = new OsgiMavenWrapConfiguration();
		wrap0.getInstructions().add(instr0);
		wrap0.getInstructions().add(instr1);
		bundle0.setWrap(wrap0);
		deps.getDeps().add(bundle0);
		
		OsgiLink repo0 = new OsgiLink();
		repo0.setHref("link_all.xml");
		deps.getDeps().add(repo0);
		
		//System.out.println(deps.save());
		deps.save();
	}
	
	
	@Test
	public void mavenBundleReadTest() throws JAXBException, MalformedURLException, IOException {
		OsgiDependencies deps = new OsgiDependencies();
		URL srcUrl = getClass().getClassLoader().getResource("basic_maven_bundle.xml");
		deps.loadResource(srcUrl);
		deps.save();
	}

	/*
	@Test
	public void mavenRepositoryTest() throws IOException, JAXBException, URISyntaxException {
		//String repo = Files.createTempDirectory("bundle").toString();
		String repo = "target" + File.separator + "bundles";
		
		try {
			OsgiUserBundleRespository repository = new OsgiUserBundleRespository(repo);
			URL srcUrl = getClass().getClassLoader().getResource("basic_maven_bundle.xml");
			repository.updateRepository(srcUrl, false);
		} finally {
			File f = new File(repo);
			if (!f.delete()) {
				f.deleteOnExit();
			}
		}
	}
	
	@Test
	public void mavenRepositoryWrapTest() throws IOException, JAXBException, URISyntaxException {
		//String repo = Files.createTempDirectory("bundle").toString();
		String repo = "target" + File.separator + "bundles";
		
		try {
			OsgiUserBundleRespository repository = new OsgiUserBundleRespository(repo);
			URL srcUrl = getClass().getClassLoader().getResource("basic_maven_wrap_bundle.xml");
			repository.updateRepository(srcUrl, false);
		} finally {
			File f = new File(repo);
			if (!f.delete()) {
				f.deleteOnExit();
			}
		}
	}
	*/
	
	@Test
	public void mavenRepositoryLinkTest() throws IOException, JAXBException, URISyntaxException {
		//String repo = Files.createTempDirectory("bundle").toString();
		String repo = "target" + File.separator + "bundles";
		
		try {
			OsgiUserBundleRespository repository = new OsgiUserBundleRespository(repo);
			URL srcUrl = getClass().getClassLoader().getResource("link_all.xml");
			repository.updateRepository(srcUrl, false);
		} finally {
			File f = new File(repo);
			if (!f.delete()) {
				f.deleteOnExit();
			}
		}
	}
	
	
}
