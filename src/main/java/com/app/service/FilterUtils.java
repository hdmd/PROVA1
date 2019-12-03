package com.app.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.app.model.Element;

/**
 * Libreria di metodi statici utilizzati per l'operazione di filtraggio sul
 * dataset.
 * 
 * @author Giulia Temperini, Paolo Cacciatore
 * @version 1.0
 */
public class FilterUtils<T> {
	/**
	 * Effettua il confronto tra i parametri value e th. Il criterio del confronto
	 * &egrave; definito da operator.
	 * 
	 * @param th       Valore di soglia/confronto
	 * @param operator Operatore condizionale per il confronto
	 * @param value    Valore di soglia/di confronto
	 * @return <strong>true</strong> se il confronto ha avuto esito positivo
	 *         <strong>false</strong> se il confronto ha avuto esito negativo
	 */
	public static boolean check(Object value, String operator, Object th) {
		if (value instanceof Number && th instanceof Number) {
			Double thC = ((Number) th).doubleValue();
			Double valuec = ((Number) value).doubleValue();
			if (operator.equals("eq"))
				return value.equals(th);
			else if (operator.equals("gt"))
				return valuec > thC;
			else if (operator.equals("lt"))
				return valuec < thC;
		} else if (th instanceof String && value instanceof String) {
			return value.equals(th);
		} else {
		}
		return false;
	}

	/**
	 * Data la collezione src in ingresso, per ogni suo elemento effettua il
	 * confronto tra il valore assunto dall'attributo identificato da fieldName e il
	 * parametro value. I criteri del confronto sono definiti da operator.
	 * 
	 * @param src       Collezione a cui applicare il filtraggio
	 * @param fieldName Nome dell'attributo il cui valore assunto viene confrontato
	 * @param operator  Operatore condizionale per il confronto
	 * @param value     Valore di soglia/di confronto
	 * @return Nuova collezione contenente gli elementi filtrati
	 */
	public Collection<T> select(Collection<T> src, String fieldName, String operator, Object value) {
		Collection<T> out = new ArrayList<T>();
		for (T item : src) {
			try {
				Method m = item.getClass()
						.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), null);
				try {
					Object tmp = m.invoke(item);
					if (FilterUtils.check(tmp, operator, value))
						out.add(item);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Causa di errore: " + e.getMessage());

				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Causa di errore: " + e.getMessage());
				} catch (InvocationTargetException e) {
					throw new RuntimeException("Causa di errore: " + e.getMessage());
				}
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Field errato");
			} catch (SecurityException e) {
				throw new RuntimeException("Causa di errore: " + e.getMessage());
			}
		}
		return out;
	}

	/**
	 * Effettua l'unione di due ArrayList di {@link Element} senza duplicati.
	 * 
	 * @param list1
	 * @param list2
	 * @return list1 unita a list2 senza l'aggiunta degli elementi gi&agrave;
	 *         presenti in list1
	 */
	public ArrayList<Element> merge(ArrayList<Element> list1, ArrayList<Element> list2) {
		int i;
		for (i = 0; i < list2.size(); i++) {
			if (!list1.contains((list2).get(i))) {
				list1.add(list2.get(i));
			}
		}
		return list1;
	}

	/**
	 * Controlla la validit&agrave; degli operatori di confronti inseriti per il
	 * filtraggio.
	 * 
	 * @param operator Operatori inseriti, <strong>NOTA</strong> Numero di operatori
	 *                 variabili
	 * @return <strong>true</strong> se tutti gli operatori sono validi,
	 *         <strong>false</strong> se almeno un operatore &egrave; errato
	 */
	public boolean rightOperator(String... operator) {
		for (String x : operator) {
			if (!("gt".equals(x) || "lt".equals(x) || "eq".equals(x)))
				return false;
		}
		return true;

	}

}
