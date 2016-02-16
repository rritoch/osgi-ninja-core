package com.vnetpublishing.java.osgi.tools;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.vnetpublishing.java.osgi.tools.impl.AOsgiDependency;
import com.vnetpublishing.java.osgi.tools.impl.OsgiDependencies;
import com.vnetpublishing.java.osgi.tools.impl.OsgiLink;
import com.vnetpublishing.java.osgi.tools.impl.OsgiMavenBundle;
import com.vnetpublishing.java.osgi.tools.impl.OsgiMavenLibrary;
import com.vnetpublishing.java.osgi.tools.impl.OsgiResourceBundle;
import com.vnetpublishing.java.osgi.tools.protocol.OsgiDependency;

/**
 * OSGI User Bundle Repository
 * 
 * Manages an applications bundle repository
 * 
 * @author Ralph Ritoch <rritoch@gmail.com>
 */

public class OsgiUserBundleRespository {
	
	Path path;
	
	OsgiLocalBundleRepositoryCache cache;
	
	public OsgiUserBundleRespository(String path) 
	{
		this.path = Paths.get(path);
		cache = OsgiLocalBundleRepositoryCache.DEFAULT;
	}

	public OsgiUserBundleRespository(File path) 
	{
		this.path = path.toPath();
		cache = OsgiLocalBundleRepositoryCache.DEFAULT;
	}
	
	public OsgiUserBundleRespository(String path,OsgiLocalBundleRepositoryCache cache) 
	{
		this.path = Paths.get(path);
		this.cache = cache;
	}
	
	public void updateRepository(String depsfile, boolean force) throws IOException, JAXBException, URISyntaxException {
		OsgiDependencies deps = cache.getDependencies(depsfile);
		updateRepository(deps,force);
	}

	public void updateRepository(OsgiDependencies deps, boolean force) throws IOException, JAXBException, URISyntaxException {
		List<List<AOsgiDependency>> nextItems = new ArrayList<List<AOsgiDependency>>();
		
		deps.takeOwnershipOfChildren();
		nextItems.add(deps.getDeps());
		
		while(nextItems.size() > 0) {
			List<AOsgiDependency> items = nextItems.remove(0);
			for(OsgiDependency dep : items) {
				String depType = dep.getDependencyType();
				if (OsgiMavenBundle.DEPENDENCY_TYPE.equals(depType)) {
					OsgiMavenBundle bundleDep = (OsgiMavenBundle)dep;
					
					cache.update(bundleDep, force || bundleDep.getVersion().endsWith("-SNAPSHOT"));
					cache.copy(bundleDep,path);
				} else if (OsgiResourceBundle.DEPENDENCY_TYPE.equals(depType)) {
					OsgiResourceBundle bundleDep = (OsgiResourceBundle)dep;
					cache.update((OsgiResourceBundle)dep, force);
					cache.copy(bundleDep, path);
				} else if (OsgiMavenLibrary.DEPENDENCY_TYPE.equals(depType)) {
					OsgiMavenLibrary libDep = (OsgiMavenLibrary)dep;
					cache.update(libDep, force);
					
					List<URL> resources = cache.resolveResources(libDep);
					
					for(URL r : resources) {
						OsgiDependencies rdep = new OsgiDependencies();
						rdep.loadResource(r);
						rdep.takeOwnershipOfChildren();
						nextItems.add(rdep.getDeps());
					}
					
				} else if (OsgiLink.DEPENDENCY_TYPE.equals(depType)) {
					
					OsgiLink curLink = (OsgiLink)dep;
					OsgiDependencies linkDep = new OsgiDependencies();
					linkDep.loadResource(curLink.getOwner().resolve(curLink.getHref()));
					linkDep.takeOwnershipOfChildren();
					nextItems.add(linkDep.getDeps());
					
				} else {
					throw new RuntimeException("Unsupported Dependency Type");
				}
			}
		}
	}
	
	public void updateRepository(URL resource, boolean force) throws JAXBException, IOException, URISyntaxException {
		OsgiDependencies deps = cache.getDependencies(resource);
		updateRepository(deps,force);
	}
}
