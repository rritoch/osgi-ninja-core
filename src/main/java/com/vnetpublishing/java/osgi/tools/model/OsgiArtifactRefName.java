package com.vnetpublishing.java.osgi.tools.model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.osgi.framework.Version;

import com.vnetpublishing.java.osgi.tools.protocol.OsgiArtifactConfig;

public class OsgiArtifactRefName {

	private String _normalConfigName;
	private String protocol;
	private String authority;
	private String artifactId;
	private Version version;
	private String signature;
	
	public OsgiArtifactRefName(
			String protocol, 
			String authority,
			String artifactId,
			Version version,
			OsgiArtifactConfig artifactConfig) {
		this.protocol = protocol;
		this.authority = authority;
		this.artifactId = artifactId;
		this.version = version;
		this._normalConfigName = artifactConfig.getNormalConfigName();
		this.signature = null;
	}
	

	
	public String getProtocol() {
		return protocol;
	}

	public String getAuthority() {
		return authority;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public Version getVersion() {
		return version;
	}

	public String getSignature() {
		if (signature == null) {
			MessageDigest digest = null;
			
			try {
				digest = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				throw new SecurityException("Unable to create OSGI signature due to missing SHA-256 Algorithm",e);
			}
			
			byte[] hash = digest.digest(_normalConfigName.getBytes(StandardCharsets.UTF_8));
		
			final StringBuilder sb = new StringBuilder();
			for(byte b : hash) {
				sb.append(String.format("%02x", b));
			}

			BigInteger big = new BigInteger(sb.toString(), 16);
			signature = big.toString(36).substring(0,8);
		}
		return signature;
	}
	
	public String getFilename() {
		return 
			String.format(
				"%s_%s_%s_%s_%s.jar",
				protocol,
				authority,
				artifactId,
				version.toString(),
				getSignature()).toLowerCase();
	}
	
	public Path getPath() {
		return Paths.get(protocol.toLowerCase(),
				  authority.toLowerCase(),
				  artifactId.toLowerCase(),
				  version.toString().toLowerCase());
	}
	
	public Path getPath(Path base) {
		return base.resolve(getPath());
	}
}
