package com.vnetpublishing.java.osgi.tools.impl;

import com.vnetpublishing.java.osgi.tools.protocol.OsgiDependency;

public abstract class AOsgiDependency implements OsgiDependency {

	private OsgiDependencies owner;
	
	
	public void setOwner(OsgiDependencies owner) {
		this.owner = owner;
	}
	
	public OsgiDependencies getOwner() {
		return owner;
	}
}
