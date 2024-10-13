package aev1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe model per a generar els datos de l'aplicació
 */
public class Model {

	/**
	 * Atribut directori null fins que es seleccione un directori amb el que operar
	 */
	File directori = null;

	/**
	 * Métode constructor buit
	 */
	public Model() {

	}

	public File getDirectori() {
		return this.directori;
	}

	public void setDirectori(File nouDirectori) {
		this.directori = nouDirectori;
	}

	/**
	 * Mètode per a llistar els arxius d'un directori i subdirectoris de manera
	 * recursiva
	 * 
	 * @param directori File que conté el directori en qüestió
	 * @param indent    String amb la indentació actual per fer el mètode dinàmic
	 *                  amb la recursivitat
	 * @return String amb el resultat de l'estructura del directori en forma d'arbre
	 */
	public String llistarArchiusRecursius(File directori, String indent) {
		File[] archius = directori.listFiles();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String resultat = "";

		// Si el directori actual és el mateix que el directori principal, afegim la
		// seua ruta
		if (this.directori.getAbsolutePath() == directori.getAbsolutePath()) {
			resultat += directori.getAbsolutePath() + "\n";
		}

		if (archius != null) {
			for (File file : archius) {
				if (file.isDirectory()) {
					resultat += indent + "|-- \\" + file.getName() + "\n";
					resultat += llistarArchiusRecursius(file, indent + "|         ");
				} else {
					// Obtenir la mida en bytes
					long tamanoEnBytes = file.length();
					String mida;

					// Formatem la mida segons corresponga
					if (tamanoEnBytes < 1024) {
						// Menys de 1 KB
						mida = tamanoEnBytes + " B"; // Mostrar en bytes
					} else {
						// Convertir a KB
						double tamanoEnKB = tamanoEnBytes / 1024.0;
						mida = String.format("%.1f KB", tamanoEnKB);
					}

					// Obtenir la data de modificació
					Date fechaMod = new Date(file.lastModified());
					String fecha = sdf.format(fechaMod);

					// Afegim el nom del fitxer, la mida i la data de modificació al resultat
					resultat += indent + "|-- " + file.getName() + " (" + mida + " – " + fecha + ")\n";
				}
			}
		}
		return resultat;
	}

	/**
	 * Métode per a buscar les coincidències d'una paraula en un directori i
	 * subdirectoris
	 * 
	 * @param directori          File amb el directori en qüestió
	 * @param indent             String amb la indentació actual per a fer el mètode
	 *                           dinàmic amb la recursivitat
	 * @param paraula            String amb la paraula a buscar
	 * @param respectaMajuscules Boolean per a comptar coincidències amb majúscules
	 *                           o sense
	 * @param respectaAccents    Boolean per a comptar coincidències amb accents o
	 *                           sense
	 * @return String amb el resultat de la cerca de coincidències, incloent el nom
	 *         del fitxer i el nombre de coincidències
	 */
	public String buscaCoincidenciesILlistaDirectori(File directori, String indent, String paraula,
			boolean respectaMajuscules, boolean respectaAccents) {
		File[] archius = directori.listFiles();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String resultat = "";
		// Si el directori actual és el mateix que el directori principal, afegim la
		// seua ruta
		if (this.directori.getAbsolutePath() == directori.getAbsolutePath()) {
			resultat += directori.getAbsolutePath() + "\n";
		}

		if (archius != null) {
			BusquedaArxiu bA = new BusquedaArxiu();
			for (File file : archius) {
				if (file.isDirectory()) {
					resultat += indent + "|-- \\" + file.getName() + "\n";
					resultat += buscaCoincidenciesILlistaDirectori(file, indent + "|   ", paraula, respectaMajuscules,
							respectaAccents);

				} else {
					String tamaño = String.format("%.1f KB", file.length() / 1024.0);
					Date fechaMod = new Date(file.lastModified());
					String fecha = sdf.format(fechaMod);
					resultat += indent + "|-- " + file.getName() + " ("
							+ bA.CoincidenciesArxiu(file, paraula, respectaMajuscules, respectaAccents)
							+ " coincidències)" + "\n";
				}
			}
		}
		return resultat;
	}

	/**
	 * Mètode per a recórrer un directori i reemplaçar una paraula per una altra en
	 * tots els fitxers de text
	 * 
	 * @param directori          File amb el directori en qüestió
	 * @param indent             String amb la indentació actual per a fer el mètode
	 *                           dinàmic amb la recursivitat
	 * @param paraulaVella       String amb la paraula a substituir
	 * @param paraulaNova        String amb la paraula nova que substituirà a la
	 *                           vella
	 * @param respectaMajuscules Boolean per a respectar majúscules durant la
	 *                           substitució
	 * @param respectaAccents    Boolean per a respectar accents durant la
	 *                           substitució
	 * @return String amb el resultat de la substitució i l'estructura del directori
	 */
	public String recorreDirectoriIReemplacaParaula(File directori, String indent, String paraulaVella,
			String paraulaNova, boolean respectaMajuscules, boolean respectaAccents) {
		File[] archius = directori.listFiles();
		BusquedaArxiu bA = new BusquedaArxiu();
		String res = "";
		// Si el directori actual és el mateix que el directori principal, afegim la
		// seua ruta
		if (this.directori.getAbsolutePath() == directori.getAbsolutePath()) {
			res += directori.getAbsolutePath() + "\n";
		}
		if (archius != null) {
			for (File file : archius) {
				if (file.isDirectory()) {
					// Si és un directori
					res += indent + "|-- " + file.getName() + "\n";
					res += recorreDirectoriIReemplacaParaula(file, indent + "|   ", paraulaVella, paraulaNova,
							respectaMajuscules, respectaAccents);
				} else {
					// Si és un arxiu de text
					if (UtilsArxius.esArxiuDeText(file)) {
						if (bA.CoincidenciesArxiu(file, paraulaVella, respectaMajuscules, respectaAccents) > 0) {
							res += indent + "|-- " + file.getName() + " (" + recorreArxiuIRemplaca(file, paraulaVella,
									paraulaNova, respectaMajuscules, respectaAccents) + " reemplaços) \n";
						}
					} else {
						res += indent + "|-- " + file.getName() + " (Arxiu no accesible) \n";
					}
				}
			}
		}
		return res;
	}

	/**
	 * Mètode per a recórrer un arxiu i reemplaçar una paraula per una altra
	 * 
	 * @param arxiu              File amb l'arxiu a recórrer
	 * @param paraulaVella       String amb la paraula a substituir
	 * @param paraulaNova        String amb la paraula nova que substituirà a la
	 *                           vella
	 * @param respectaMajuscules Boolean per a respectar majúscules durant la
	 *                           substitució
	 * @param respectaAccents    Boolean per a respectar accents durant la
	 *                           substitució
	 * @return int amb el nombre de vegades que s'ha substituït la paraula
	 */
	private int recorreArxiuIRemplaca(File arxiu, String paraulaVella, String paraulaNova, boolean respectaMajuscules,
			boolean respectaAccents) {
		int cont = 0;
		int reemplacos = 0;
		try {
			FileReader fr = new FileReader(arxiu);
			BufferedReader br = new BufferedReader(fr);
			String linea = br.readLine();
			String novaLinea;
			File nouArxiu = new File(arxiu.getParent() + "\\MOD_" + arxiu.getName());

			// Si el arxiu existix el eliminem per no tindre varios MOD en uno
			if (nouArxiu.exists()) {
				nouArxiu.delete();
			}

			while (linea != null) {
				if (!respectaAccents) {
					linea = UtilsArxius.eliminaAccents(linea);
					paraulaVella = UtilsArxius.eliminaAccents(paraulaVella);
				}

				if (!respectaMajuscules) {
					linea = linea.toLowerCase();
					paraulaVella = paraulaVella.toLowerCase();
				}

				novaLinea = linea.replace(paraulaVella, paraulaNova);
				int index = 0;
				while ((index = linea.indexOf(paraulaVella, index)) != -1) {
					reemplacos++;
					index += paraulaVella.length(); // Moure l'índex per seguir buscant
				}

				linea = br.readLine();
				escriuLineaEnArxiu(nouArxiu, novaLinea);
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			System.out.println("No s'ha pogut processar l'arxiu " + arxiu.getName());
		}
		return reemplacos;
	}

	/**
	 * Mètode per a escriure una línia en un fitxer
	 * 
	 * @param arxiu File amb l'arxiu en el qual es vol escriure
	 * @param linea String amb la línia a escriure
	 */
	private void escriuLineaEnArxiu(File arxiu, String linea) {
		try (FileOutputStream fos = new FileOutputStream(arxiu, true);
				OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
				BufferedWriter bw = new BufferedWriter(osw);) {
			bw.write(linea);
			bw.newLine();
		} catch (IOException e) {
			System.out.println("Error escrivint la línia: " + e.getMessage());
		}
	}
}
