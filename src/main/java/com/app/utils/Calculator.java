package com.app.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.app.model.Element;
import com.app.service.FilterUtils;

/**
 * Libreria di metodi statici ciascuno dei quali calcola un determinato
 * indicatore matematico relativo al dataset. Ciascun metodo ha come unico
 * parametro L'ArrayList rappresentante il dataset.
 * <p>
 * Indicatori implementati: somma, valore minimo, valore massimo.
 * 
 * @author Giulia Temperini, Paolo Cacciatore
 * @version 1.0
 */
public class Calculator<T> {

	private float value;

	public float calcolo(Collection<T> data, String nome_param, String operazione) {
		int i=0;
		
		for (T item : data) {
			try {
				Method m = item.getClass().getMethod("get" + nome_param, null);
				try { 
					if (i==0) 
						{value=(Float) m.invoke(item);
						 i++;
						}
					
					else {
					if (operazione.equals("sum"))
						value += (Float) m.invoke(item);
						

					if (operazione.equals("min")) {
						if ((Float) m.invoke(item) < value)

							value = (Float) m.invoke(item);
						else {
						}
					}
					if (operazione.equals("max"))

					{
						if ((Float) m.invoke(item) > value)

							value = (Float) m.invoke(item);
						else {
						}
					}

				}} catch (IllegalAccessException e) {
					throw new RuntimeException("Causa di errore: " + e.getMessage());

				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Causa di errore: " + e.getMessage());
				} catch (InvocationTargetException e) {
					throw new RuntimeException("Causa di errore: " + e.getMessage());
				}
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Parametro errato o non presente nel dataset.");
			} catch (SecurityException e) {
				throw new RuntimeException("Causa di errore: " + e.getMessage());
			}
		}
		return value;
	}

	public static HashMap<String, Integer> counter(ArrayList<String> column) {
		int i;
		HashMap<String, Integer> uniqueElements = new HashMap<String, Integer>();
		for (i = 0; i < column.size(); i++) {
			if (!uniqueElements.containsKey(column.get(i))) {
				uniqueElements.put(column.get(i), 1);
			} else {
				uniqueElements.replace(column.get(i), uniqueElements.get(column.get(i)),
						uniqueElements.get(column.get(i)) + 1);
			}
		}
		return uniqueElements;
	}
}
