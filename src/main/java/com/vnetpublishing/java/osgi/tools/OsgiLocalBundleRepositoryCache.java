package com.vnetpublishing.java.osgi.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.vnetpublishing.java.osgi.tools.impl.OsgiDependencies;
import com.vnetpublishing.java.osgi.tools.impl.OsgiLink;
import com.vnetpublishing.java.osgi.tools.impl.OsgiMavenLibrary;
import com.vnetpublishing.java.osgi.tools.model.OsgiArtifactRefName;
import com.vnetpublishing.java.osgi.tools.protocol.OsgiArtifact;

/**
 * OSGI Bundle Local Repository Cache
 * 
 * Provides a local cache of OSGI dependencies
 * @author Ralph Ritoch <rritoch@gmail.com>
 */

public class OsgiLocalBundleRepositoryCache {

	private static final Logger LOGGER = Logger.getLogger(OsgiLocalBundleRepositoryCache.class.getName());
	
	private static final int DOWNLOAD_BLOCK_SIZE = 4096;
	
	public static final OsgiLocalBundleRepositoryCache DEFAULT = new OsgiLocalBundleRepositoryCache();
	
	Path path;
	
	public OsgiLocalBundleRepositoryCache () {
		path = Paths.get(System.getProperty("user.home"),".osgir1");
	}
	
	public OsgiLocalBundleRepositoryCache (Path path) {
		this.path = path;
	}
	
	private static void download(URL inURL,File outfile) throws IOException {
		
		LOGGER.info(String.format("Downloading dependency %s",inURL.toString()));
		copyURLTo(inURL,outfile);
	}
	
	private static void copyURLTo(URL inURL,File outfile) throws IOException {
		
		
		URLConnection conn = null;
		InputStream inStream = null;
		FileOutputStream outStream = null;
		
		try {
			conn = inURL.openConnection();
			inStream = conn.getInputStream();
			outStream = new FileOutputStream(outfile);
			
			final byte[] buff = new byte[DOWNLOAD_BLOCK_SIZE];
			
			int len = 0;
			while(len >= 0) {
				len = inStream.read(buff);
				if (len > 0) {
					outStream.write(buff,0,len);
				}
			}
			
		} finally {
			
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException ex) {
					LOGGER.log(Level.FINE, "Unable to close input stream",ex);
				}
			}
			
			if (outStream != null) {
				try {
					outStream.flush();
				} catch (IOException ex) {
					LOGGER.log(Level.FINE, "Unable to flush output stream",ex);
				}
				
				try {
					outStream.close();
				} catch (IOException ex) {
					LOGGER.log(Level.FINE, "Unable to close output stream",ex);
				}
			}
		}
	}
	
	
	public Path getPath() {
		return path;
	}
	
	public void update(OsgiArtifact artifact, boolean force) throws IOException, URISyntaxException {
		
		OsgiArtifactRefName refName = artifact.getRefName();
		File outfile = new File(refName.getPath(getPath().resolve(Paths.get("repo","default"))).toFile(),refName.getFilename());
		
		if (force || !outfile.exists()) {
			
			File dir = outfile.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
				dir.mkdir();
			}
			
			URL artifactURL = artifact.getURL();
			
			
			String oldProp = System.getProperty("java.protocol.handler.pkgs");
			try {
				System.setProperty("java.protocol.handler.pkgs", "org.ops4j.pax.url");
				download(artifactURL,outfile);
			} finally {
				if (oldProp == null) {
					System.clearProperty("java.protocol.handler.pkgs");
				} else {
					System.setProperty("java.protocol.handler.pkgs",oldProp);
				}
			}
			
		}
	}
	
	public URL getDependencyLocalURL(OsgiArtifact artifact) 
		throws MalformedURLException {
		OsgiArtifactRefName refName = artifact.getRefName();
		File outfile = new File(refName.getPath(getPath().resolve(Paths.get("repo","default"))).toFile(),refName.getFilename());
		return outfile.toURI().toURL();
	}
	
	public OsgiDependencies getDependencies(String depsfile) throws FileNotFoundException {
		
		JAXBContext jc = null;
		
		try {
			jc = JAXBContext.newInstance( "com.vnetpublishing.java.osgi.tools.impl");
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		
		Unmarshaller u = null;
		try {
			u = jc.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		
		
		OsgiDependencies deps = null;
		try {
			deps = (OsgiDependencies) u.unmarshal(new FileInputStream(depsfile));
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		
		
		return deps;
	}
	
	
	public List<OsgiDependencies> getDependencies(OsgiMavenLibrary library, boolean force) throws IOException {
		
		List<OsgiDependencies> depsList = new ArrayList<OsgiDependencies>();
		
		String filename = String.format("%s-%s",library.getArtifactId(),library.getVersion());
		File outfile = new File(getPath().resolve(Paths.get("cache","libraries","maven",library.getGroupId(),library.getArtifactId())).toFile(),filename);
		
		if (force || !outfile.exists()) {
			
			File dir = outfile.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
				dir.mkdir();
			}
			
			URL artifactURL = library.getURL();
			download(artifactURL,outfile);
		}
		
		
		final String coreURL = outfile.toURI().toString();
		
		if (library.getResources().trim().length() > 0) {
		
			String[] resources = library.getResources().trim().split("\\s+");
		
			for(String resource : resources) {
			
				JAXBContext jc = null;
		
				try {
					jc = JAXBContext.newInstance( "com.vnetpublishing.java.osgi.tools.impl");
				} catch (JAXBException e) {
					throw new RuntimeException(e);
				}
		
				Unmarshaller u = null;
				try {
					u = jc.createUnmarshaller();
				} catch (JAXBException e) {
					throw new RuntimeException(e);
				}
		
				OsgiDependencies deps = null;
		
				try {
					deps = (OsgiDependencies) u.unmarshal(new URL(String.format("jar:%s!/%s",coreURL,resource)));
				} catch (JAXBException e) {
					throw new RuntimeException(e);
				}
		
				depsList.add(deps);
			}
		}
		return depsList;
	}
	
	
	public OsgiDependencies getDependencies(OsgiLink link) throws MalformedURLException {
		
		JAXBContext jc = null;
		
		try {
			jc = JAXBContext.newInstance( "com.vnetpublishing.java.osgi.tools.impl");
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		
		Unmarshaller u = null;
		try {
			u = jc.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		
		OsgiDependencies deps = null;
		
		try {
			deps = (OsgiDependencies) u.unmarshal(new URL(link.getHref()));
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		
		return deps;
	}

	public OsgiDependencies getDependencies(URL resource) throws JAXBException, IOException {
		OsgiDependencies deps = new OsgiDependencies();
		deps.loadResource(resource);
		return deps;
	}
	
	public void copy(OsgiArtifact artifact,Path path) throws MalformedURLException, IOException {
		File outfile = path.resolve(artifact.getRefName().getFilename()).toFile();
		if (!outfile.getParentFile().isDirectory()) {
			outfile.getParentFile().mkdirs();
		}
		copyURLTo(getDependencyLocalURL(artifact),outfile);
	}

	public List<URL> resolveResources(OsgiMavenLibrary library) throws IOException {
		
		OsgiArtifactRefName refName = library.getRefName();
		List<URL> resources = new ArrayList<URL>();
		
		File outfile = new File(refName.getPath(getPath().resolve(Paths.get("repo","default"))).toFile(),refName.getFilename());
		String jarfile = outfile.toURI().toString();
		String[] r = library.getResources().replace(",", " ").replaceAll("\\s+", " ").trim().split(" ");
		for(String src : r) {
			StringBuilder sb = new StringBuilder();
			sb.append("jar:");
			sb.append(jarfile);
			sb.append("!");
			if (!src.startsWith("/")) {
				sb.append("/");
			}
			sb.append(src);
			resources.add(new URL(sb.toString()));
		}
		return resources;
	}
	
	
}
