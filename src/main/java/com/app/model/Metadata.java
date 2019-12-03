package com.app.model;

/**
 * 
 * Classe implementata per rappresentare i metadati relativi al dataset. Sono
 * implementati i canonici getter e setter relativi a ciascun attributo.
 * 
 * @author Giulia Temperini, Paolo Cacciatore
 * @version 1.0
 */

public class Metadata {

	private String name;
	private String type;

	public Metadata(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}