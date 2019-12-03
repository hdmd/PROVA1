package com.app.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import com.app.model.Element;
import com.app.model.Metadata;

/**
 * Libreria di metodi statici utilizzati all'avvio per inizializzare la
 * Collection contenente i dati.
 * 
 * @author Giulia Temperini, Paolo Cacciatore
 * @version 1.0
 */
public class Utils {

	/**
	 * Dato un indirizzo ad una pagina codificata in JSON, ne effettua la
	 * decodifica, per poi eseguire il download del dataset dall'URL ricavato.
	 * 
	 * @param url      l'url assoluto della pagina JSON da decodificare
	 * @param fileName nome del file contenente il dataset
	 */

	public static void jsonDecode(String url, String fileName) {
		/*
		 * Nel JSON che contiene il dataset da scaricare sono presenti due differenti
		 * url con
		 * format="http://publications.europa.eu/resource/authority/file-type/CSV". A
		 * tal proposito è stata definita la variabile booleana a, di modo che il
		 * download viene effettuato solo la prima volta che si incontra quel
		 * determinato format.
		 * 
		 */
		boolean a = true;

		try {

			URLConnection openConnection = new URL(url).openConnection();
			InputStream in = openConnection.getInputStream();

			String data = "";
			String line = "";
			try {
				InputStreamReader inR = new InputStreamReader(in);
				BufferedReader buf = new BufferedReader(inR);

				while ((line = buf.readLine()) != null) {
					data += line;
				}
			} finally {
				in.close();
			}
			JSONObject obj = (JSONObject) JSONValue.parseWithException(data);
			JSONObject objI = (JSONObject) (obj.get("result"));
			JSONArray objA = (JSONArray) (objI.get("resources"));

			for (Object o : objA) {
				if (o instanceof JSONObject) {
					JSONObject o1 = (JSONObject) o;
					String format = (String) o1.get("format");
					String urlD = (String) o1.get("url");
					if (format.equals("http://publications.europa.eu/resource/authority/file-type/CSV") && a) {
						download(urlD, fileName);
						a = false;
					}
				}
			}

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo privato utilizzato da {@link jsonDecode} per effettuare il download e
	 * salvataggio nella directory.
	 * 
	 * @param url
	 * @param fileName
	 * @throws Exception
	 */
	private static void download(String url, String fileName) throws Exception {
		try (InputStream in = URI.create(url).toURL().openStream()) {
			Files.copy(in, Paths.get(fileName));
		}
	}

	/**
	 * Metodo utilizzato per effettuare il parsing del dataset creando un ArrayList
	 * di oggetti della classe {@link Element}}
	 * 
	 * @param v        ArrayList che conterrà i record del dataset
	 * @param header   ArrayList che conterrà i metadati del dataset
	 * @param filename File contenente il dataset
	 */
	public static void csvParse(ArrayList<Element> v, ArrayList<Metadata> header, String filename) {
		boolean flag = false;

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

			String line;

			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");

				if (!flag) {
					for (int p = 0; p < values.length; p++) {
						String attribute = values[p].substring(1, values[p].length() - 1);
						String type = Element.type(attribute);
						header.add(new Metadata(attribute, type));
					}
				}
				if (flag) {
					v.add(new Element(Integer.parseInt(values[0].substring(1, values[0].length() - 1)),
							values[1].substring(1, values[1].length() - 1),
							values[2].substring(1, values[2].length() - 1),
							values[3].substring(1, values[3].length() - 1),
							values[4].substring(1, values[4].length() - 1),
							Float.parseFloat(values[5].substring(1, values[5].length() - 1))));
				}
				flag = true;
			}

			br.close();

		} catch (IOException j) {
			j.printStackTrace();
			return;
		}
	}
}
