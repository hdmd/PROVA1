package com.app.service;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.model.Element;
import com.app.model.Metadata;
import com.app.model.Stats;
import com.app.utils.Calculator;
import com.app.utils.Utils;

/**
 * Classe che implementa la logica di business, ovvero ha la funzione di
 * elaborare i dati e fornirli al Controller per essere esposti verso il client.
 * 
 * @author Giulia Temperini, Paolo Cacciatore
 * @version 1.0
 */
@Service
public class AppService {

	static ArrayList<Element> records = new ArrayList<Element>();
	static ArrayList<Metadata> header = new ArrayList<Metadata>();
	private FilterUtils<Element> filteredData = new FilterUtils<Element>();
	// Creo quest'oggetto per permettere di fare i calcoli in calculator
	private Calculator<Element> valori = new Calculator<Element>();

	/**
	 * Verifica se il dataset &egrave; presente nella directory salvato come file
	 * <em>dataset.csv</em>. Se &egrave; presente effettua direttamente il parsing
	 * dei dati. Se non &egrave; presente effettua la decodifica del JSON all'url
	 * specificato seguita dalla ricerca dell'url contenente il dataset ed infine il
	 * download di quest'ultimo.
	 * 
	 * @see com.app.utils.Utils
	 */
	@Autowired
	public AppService() {
		File f = new File("dataset.csv");
		if (!f.exists()) {
			Utils.jsonDecode("http://data.europa.eu/euodp/data/api/3/action/package_show?id=yGVKnIzbkC2ZHpT6jQouDg",
					"dataset.csv");
		}
		Utils.csvParse(records, header, "dataset.csv");
		System.out.println(records.getClass());
	}

	/**
	 * Restituisce l'i-esimo elemento/record del dataset.
	 * 
	 * @param i Numero elemento/record da restituire
	 * @return {@link Element}
	 */
	public Element printElement(int i) {
		if (i > records.size() || i < 0) {
			throw new RuntimeException("Indice non valido");
		}
		return records.get(i);
	}

	/**
	 * Restituisce l'intero dataset rappresentato da un ArrayList di oggetti di tipo
	 * {@link Element}.
	 * 
	 * @return tutti gli elementi/record del dataset
	 */
	public ArrayList<Element> printElement() {
		return records;
	}

	/**
	 * Restituisce la somma di tutti i valori assunti dall'attributo pc_schools.
	 * 
	 * @return indicatore matematico calcolato
	 * @see com.app.model.Stats
	 * @see com.app.utils.Calculator
	 */
	public Stats calcolo(String nome_param, String operazione) {

		if (!(operazione.equals("sum") || operazione.equals("min") || operazione.equals("max"))) {
			throw new RuntimeException("Operatore di calcolo non valido. Operatori validi: sum, min, max");
		}

		else
			return new Stats(operazione, operazione + " dei valori di " + nome_param, valori.calcolo(records, nome_param, operazione));

	}

	/**
	 * Restituisce il valore medio calcolato su tutti i valori assunti
	 * dall'attributo pc_schools.
	 * 
	 * @return valore medio di pc_schools
	 * @see com.app.model.Stats
	 * @see com.app.utils.Calculator
	 */
	public Stats avg(String nome_param) {
		return new Stats("Avg", "Valore medio per " + nome_param, valori.calcolo(records, nome_param, "sum") / records.size());
	}

	/**
	 * Restituisce la deviazione standard calcolata su tutti i valori assunti
	 * dall'attributo pc_schools.
	 * 
	 * @return deviazione standard di pc_schools
	 * @see com.app.model.Stats
	 * @see com.app.utils.Calculator
	 */
	public Stats stdDev(String nome_param) {
		float value = 0;
		int i;
		double avg = avg(nome_param).getValue();
		for (i = 0; i < records.size(); i++) {
			value += Math.pow(records.get(i).getValue() - avg, 2);
		}
		value /= records.size();
		return new Stats("StdDev", "Deviazione standard dei valori " + nome_param, value);
	}

	/**
	 * Restituisce l'elenco dei metadati del dataset.
	 * 
	 * @return elenco di tutti i metadati.
	 * @see com.app.model.Metadata
	 */
	public ArrayList<Metadata> printMetadata() {
		return header;
	}

	/**
	 * Restituisce l'elenco dei indicatori matematici/statistici relativi al
	 * dataset.
	 * 
	 * @return elenco di tutti gli indicatori matematici/statistici
	 * @see com.app.model.Stats
	 */
	public ArrayList<Stats> Stats(String nome_param) {
		ArrayList<Stats> statistics = new ArrayList<Stats>();
		statistics.add(calcolo(nome_param,"sum"));
		statistics.add(avg(nome_param));
		statistics.add(calcolo(nome_param,"min"));
		statistics.add(calcolo(nome_param,"max"));
		statistics.add(stdDev(nome_param));
		return statistics;
	}

	/**
	 * Effettua il filtraggio del dataset i cui criteri sono descritti dai parametri
	 * dati in ingresso e restituisce tutti elementi filtrati. Il parametro
	 * fieldName identifica l'attributo su cui applicare il filtro.
	 * <p>
	 * Per gli attributi numerici, per ciascun elemento del dataset viene effettuato
	 * il confronto, definito da operator, tra il valore dell'attributo considerato
	 * e value. Se l'esito del confronto &egrave; positivo l'elemento viene aggiunto
	 * all'ArrayList contenente gli elementi filtrati.
	 * <p>
	 * Per gli attributi di tipo Stringa l'operator &egrave; sempre <em>eq</em>: se
	 * un elemento del dataset ha come valore dell'attributo considerato lo stesso
	 * di value allora tale elemento viene aggiunto all'ArrayList contenente gli
	 * elementi filtrati.
	 * 
	 * @param fieldName Identificatore dell'attributo su cui applicare il filtro
	 * @param operator  Operatore di confronto
	 * @param value     Valore di soglia/confronto
	 * @return elenco degli elementi filtrati
	 * @throws RuntimeException se l'operatore condizionale inserito &egrave; errato
	 */
	public ArrayList<Element> filter(String fieldName, String operator, Object value) {
		if (!filteredData.rightOperator(operator)) {
			throw new RuntimeException("Operatore di confronto non valido. Operatori validi: gt, lt, equ");
		}
		return (ArrayList<Element>) filteredData.select(records, fieldName, operator, value);

	}

	/**
	 * Effettua l'<strong>and</strong> o <strong>or</strong> di due richieste
	 * condizionali.
	 * <p>
	 * Ciascuna richiesta condizionale è definita da operatorX e valueX.
	 * logicOperator specifica l'operatore d'insieme. L'attributo al quale applicare
	 * la computazione è solo value, per ciò non necessita essere specificato. Per
	 * l'operazione di and si applicano i filtri separatamente e successivamente si
	 * uniscono gli ArrayList risultanti. Per l'operazione di intersezione il
	 * secondo filtro viene applicato all'ArrayList risultante dal primo filtro e
	 * non all'intero dataset. <strong>NOTA</strong> -Tale filtro viene applicato
	 * solamente agli attributi numerici di {@link Element}
	 * 
	 * @param logicOperator Operatore di insieme
	 * @param value1        Valore di soglia/confronto per il primo filtro
	 * @param value2        Valore di soglia/confronto per il secondo filtro
	 * @param operator1     Operatore di confronto per il primo filtro
	 * @param operator2     Operatore di confronto per il secondo filtro
	 * @return Elenco degli elementi filtrati
	 * @throws RuntimeException se l'operatore di confronto inserito &egrave; errato
	 * @throws RuntimeException se l'operatore logico inserito &egrave; errato
	 */
	public ArrayList<Element> multifilter(String logicOperator, String operator1, Object value1, String operator2,
			Object value2) {
		// check dell'operatore
		if (!filteredData.rightOperator(operator1, operator2)) {
			throw new RuntimeException("Operatore di confronto non valido. Operatori validi: gt, lt, equ");
		}
		ArrayList<Element> list1 = new ArrayList<Element>();
		list1 = (ArrayList<Element>) filteredData.select(records, "value", operator1, value1);
		if (logicOperator.equals("and")) {
			return (ArrayList<Element>) filteredData.select(list1, "value", operator2, value2);
		} else if (logicOperator.equals("or")) {
			ArrayList<Element> list2 = new ArrayList<Element>();
			list2 = (ArrayList<Element>) filteredData.select(records, "value", operator2, value2);
			return filteredData.merge(list1, list2);
		}

		else {
			throw new RuntimeException("Operatore logico non valido");
		}
	}

	/**
	 * Effettua un filtro combinato dato dalla serie di due filtri. Il risultato del
	 * primo &egrave; un elenco di {@link Element} caratterizzati da
	 * ref_area=value1. A tale elenco viene applicato un secondo filtro
	 * sull'attributo value. Il criterio del filtro &egrave; definito da operator2.
	 * 
	 * @param value1    Valore di confronto per il primo filtro
	 * @param operator2 Operatore condizionale
	 * @param value2    Valore di soglia/confronto per il secondo filtro
	 * @return Elenco degli elementi filtrati
	 * @throws RuntimeException Se l'operatore condizionale inserito &egrave; errato
	 */
	public ArrayList<Element> multifilter(Object value1, String operator2, Object value2) {
		if (!filteredData.rightOperator(operator2)) {
			throw new RuntimeException("Operatore di confronto non valido. Operatori validi: gt, lt, eq");
		}
		ArrayList<Element> list1 = new ArrayList<Element>();
		list1 = (ArrayList<Element>) filteredData.select(records, "ref_area", "eq", value1);
		ArrayList<Element> list2 = new ArrayList<Element>();
		list2 = (ArrayList<Element>) filteredData.select(list1, "value", operator2, value2);
		return list2;
	}

	/**
	 * Conteggia i valori unici assunti da un determinato attributo, per ogni valore
	 * unico indica il numero di occorrenze.
	 * 
	 * @param fieldName Attributo da considerare
	 * @return Elenco dei valori unici con le rispettive occorrenze
	 * @throws NoSuchMethodException
	 * @throws RuntimeException
	 * @throws IllegalAccessException
	 * @throws ReflectiveOperationException
	 */
	public HashMap<String, Integer> counter(String fieldName)
			throws NoSuchMethodException, RuntimeException, IllegalAccessException, ReflectiveOperationException {
		ArrayList<String> inputColumn = new ArrayList<String>();
		int i;
		for (i = 0; i < records.size(); i++) {
			Method m = records.get(i).getClass()
					.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), null);
			inputColumn.add((String) m.invoke(records.get(i)));
		}
		return Calculator.counter(inputColumn);
	}

}
