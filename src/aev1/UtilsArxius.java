package aev1;

import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Classe estática amb metodes utils per a la gestió de arxius
 */
public class UtilsArxius {

	/**
	 * Métode per saber si un archiu es pot tractar
	 * 
	 * @param file Arxiu a comprobar
	 * @return boolean Retorna un booleà per saber si es pot tractar o no
	 */
	public static boolean esArxiuDeText(File file) {
		// Utilitza BufferedReader per llegir l'arxiu
		try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				for (char c : linea.toCharArray()) {
					// Verifica si el carácter es una letra, un digit o un carácter imprimible
					// comú
					if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) && !esCaracterImprimible(c)) {
						return false; // No es texto plano
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false; // En caso de excepción, asumim que no es text plà
		}

		return true; // Es text plà
	}

	//
	/**
	 * Mètode auxiliar per verificar si un caràcter és imprimible
	 * 
	 * @param c Caracter per comprobar
	 * @return Retorna un boolean dient si es imprimible o no
	 */
	private static boolean esCaracterImprimible(char c) {
		// Incloem caràcters comuns de puntuació i altres símbols que poden estar
		// en text pla
		String caractersImprimibles = ".,;:!?\"'()[]{}-_=+<>@#$%^&*~`|/\\";
		return (c >= 32 && c <= 126) || caractersImprimibles.indexOf(c) != -1 || Character.isISOControl(c) == false;
	}

	/**
	 * Métode per a eliminar els accents de un text
	 * 
	 * @param text String amb el text amb accents
	 * @return retorna el string donat sense accents
	 */
	public static String eliminaAccents(String text) {
		String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(normalized).replaceAll("");
	}

}
