package aev1;

import java.io.*;
import java.text.Normalizer;
import java.util.regex.Pattern;
import org.apache.pdfbox.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.nio.charset.StandardCharsets;

/**
 * Classe per a la busqueda dins de un arxiu
 */
public class BusquedaArxiu {

	/**
	 * Mètode principal per comptar coincidències en un fitxer
	 * 
	 * @param f                  Arxiu donat per a treballar amb ell
	 * @param paraula            La paraula que volen contar
	 * @param respectaMajuscules Booleà per a comptar coincidencies amb majuscules
	 *                           corresponens o sense ells
	 * @param respectaAccents    Booleà per a comptar coincidencies amb accents
	 *                           corresponents o sense ells
	 * @return Retorna el contador de paraules en el fitxer
	 */
	public int CoincidenciesArxiu(File f, String paraula, boolean respectaMajuscules, boolean respectaAccents) {
		int cont = 0;

		// Processar fitxers de text (amb qualsevol extensió)
		if (UtilsArxius.esArxiuDeText(f) && !f.getName().toLowerCase().endsWith(".pdf")) {
			cont = ContarEnTextPla(f, paraula, respectaMajuscules, respectaAccents);
		}
		// Processar fitxers PDF (.pdf)
		else if (f.getName().toLowerCase().endsWith(".pdf")) {
			cont = ContarEnPDF(f, paraula, respectaMajuscules, respectaAccents);
		}
		// Si no és text pla ni PDF, assumim 0 coincidències
		else {
			return 0;
		}

		return cont;
	}

	/**
	 * Métode per a contar coincidències en un fitxer de text plà
	 * 
	 * @param f                  El fitxer a tractar
	 * @param paraula            La paraula que volem contar
	 * @param respectaMajuscules Booleà per a tindre en conter si hem de respectar
	 *                           les majúscules o no.
	 * @param respectaAccents    Booleà per a tindre en conter si hem de respectar
	 *                           els accents o no.
	 * @return int Torna un numero amb les coincidències
	 */
	private int ContarEnTextPla(File f, String paraula, boolean respectaMajuscules, boolean respectaAccents) {
		int cont = 0;
		try {
			FileReader fr = new FileReader(f.getAbsolutePath(), StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(fr);
			String linea = br.readLine();
			while (linea != null) {
				cont += ContaCoincidencies(linea, paraula, respectaMajuscules, respectaAccents);
				linea = br.readLine();
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cont;
	}

	/**
	 * Métode per a contar coincidències en un pdf
	 * 
	 * @param f                  File fitxer en cuestio a tractar
	 * @param paraula            Paraula que hem de trobar
	 * @param respectaMajuscules Booleà per a tindre en conter si hem de respectar
	 *                           les majúscules o no.
	 * @param respectaAccents    Booleà per a tindre en conter si hem de respectar
	 *                           els accents o no.
	 * @return int Torna un enter amb les coincidències
	 */
	private int ContarEnPDF(File f, String paraula, boolean respectaMajuscules, boolean respectaAccents) {
		int cont = 0;

		// Inicialitzem el document
		PDDocument pdDocument = null;
		try { // Utilitzar el fitxer directament
			pdDocument = Loader.loadPDF(f);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			int numPages = pdDocument.getNumberOfPages(); // Obtenir el nombre total de pàgines

			// Processar cada pàgina una a una
			for (int i = 1; i <= numPages; i++) {
				pdfStripper.setStartPage(i);
				pdfStripper.setEndPage(i); // Processar només la pàgina actual
				String text = pdfStripper.getText(pdDocument);

				// Comptar coincidències en el text extret de la pàgina
				cont += ContaCoincidencies(text, paraula, respectaMajuscules, respectaAccents);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return cont;
	}

	/**
	 * Métode per a contar coincidències de una paraula en una linea de text
	 * 
	 * @param linea              String amb la Linea en cuestió per a el contament
	 *                           de les coincidències
	 * @param paraula            String la paraula amb la que es vol trobar
	 *                           coincidències
	 * @param respectaMajuscules Booleà per a tindre en conter si hem de respectar
	 *                           les majúscules o no.
	 * @param respectaAccents    Booleà per a tindre en conter si hem de respectar
	 *                           els accents o no.
	 * @return int torna el contador de paraules en el text
	 */
	private int ContaCoincidencies(String linea, String paraula, boolean respectaMajuscules, boolean respectaAccents) {
		if (linea == null)
			return 0;
		// Si no es respecten els accents els eliminem per a comparar cadenes amb eixe
		// handicap
		if (!respectaAccents) {
			linea = UtilsArxius.eliminaAccents(linea);
			paraula = UtilsArxius.eliminaAccents(paraula);
		}

		// Ignorar majuscules/minuscules si no es van a respectar
		if (!respectaMajuscules) {
			linea = linea.toLowerCase();
			paraula = paraula.toLowerCase();
		}

		// Contar coincidencies
		int index = 0;
		int count = 0;
		while ((index = linea.indexOf(paraula, index)) != -1) {
			count++;
			index += paraula.length();
		}

		return count;
	}

}
