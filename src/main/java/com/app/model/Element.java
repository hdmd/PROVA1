package com.app.model;

/**
 * Classe che definisce il singolo record del dataset creando oggetti i cui
 * attributi rappresentano le singole colonne di dati del file .csv utilizzato
 * nel progetto. Sono implementati i canonici setter e getter, il metodo
 * toString e un metodo che fornisce come stringa il tipo di ogni attributo.
 * 
 * @author Giulia Temperini, Paolo Cacciatore
 * @version 1.0
 */

public class Element {
	private int time_period;
	private String ref_area;
	private String indicator;
	private String breakdown;
	private String unit_measure;
	private float value;

	public Element(int time_period, String ref_area, String indicator, String breakdown, String unit_measure,
			float value)

	{
		this.time_period = time_period;
		this.ref_area = ref_area;
		this.indicator = indicator;
		this.breakdown = breakdown;
		this.unit_measure = unit_measure;
		this.value = value;

	}

	public int getTime_period() {
		return time_period;
	}

	public void setTime_period(int time_period) {
		this.time_period = time_period;
	}

	public String getRef_area() {
		return ref_area;
	}

	public void setRef_area(String ref_area) {
		this.ref_area = ref_area;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getBreakdown() {
		return breakdown;
	}

	public void setBreakdown(String breakdown) {
		this.breakdown = breakdown;
	}

	public String getUnit_measure() {
		return unit_measure;
	}

	public void setUnit_measure(String unit_measure) {
		this.unit_measure = unit_measure;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format(
				"Element [time_period:%d, ref_area=%s, indicator=%s, value=%f, breakdown=%s, unit_measure=%s]",
				time_period, ref_area, indicator, value, breakdown, unit_measure);
	}

	/**
	 * Restituisce come Stringa il tipo dell'attributo il cui nome viene dato come
	 * parametro in ingresso.
	 * 
	 * @param x Nome dell'attributo
	 * @return <strong>tipo dell'attributo inserito</strong> se tale attributo
	 *         esiste, <strong>null</strong> se l'attributo inserito non esiste
	 */
	public static String type(String x) {
		if (x.equals("time_period"))
			return "Int";
		if (x.equals("ref_area"))
			return "String";
		if (x.equals("indicator"))
			return "String";
		if (x.equals("breakdown"))
			return "String";
		if (x.equals("value"))
			return "Float";
		if (x.equals("unit_measure"))
			return "String";
		return null;
	}

}
