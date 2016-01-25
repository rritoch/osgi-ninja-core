package com.vnetpublishing.java.osgi.tools.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (
	name = "resourceWrap",
	namespace = "urn:com:vnetpublishing:osgi:dependencies"
)
public class OsgiResourceWrapConfiguration extends AOsgiWrapConfiguration {

	@XmlElement(name="instr")
	List<OsgiWrapInstr> instructions = new ArrayList<OsgiWrapInstr>();
	
	
	public List<OsgiWrapInstr> getInstructions() {
		return instructions;
	}

	public void setInstructions(List<OsgiWrapInstr> items) {
		this.instructions = items;
	}
	
	public String toNormalURIString() {
		
		List<String> sortable = new ArrayList<String>();
		
		for(OsgiWrapInstr instr : instructions) {
			sortable.add(instr.toURIString());
		}
		
		Collections.sort(sortable);
		StringBuilder sb = new StringBuilder();
		
		boolean not_first = false;
		for(String item : sortable) {
			if (not_first) {
				sb.append("&");
			} else {
				not_first = true;
			}
			sb.append(item);
		}
		
		return sb.toString();
	}
	
	public String toURIString() {
		StringBuilder sb = new StringBuilder();
		
		boolean not_first = false;
		for(OsgiWrapInstr instr : instructions) {
			if (not_first) {
				sb.append("&");
			} else {
				not_first = true;
			}
			sb.append(instr.toURIString());
		}
		
		return sb.toString();
	}

}

