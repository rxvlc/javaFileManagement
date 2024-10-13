package aev1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Model {

	File directori = null;

	public Model() {

	}

	public File getDirectori() {
		return this.directori;
	}

	public void setDirectori(File nouDirectori) {
		this.directori = nouDirectori;
	}

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
					resultat += llistarArchiusRecursius(file, indent + "|   ");
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
	 * Métode per a buscar les coincidències de un terme en tot un directori i
	 * subdirectoris
	 * 
	 * @param directori
	 * @param indent
	 * @param paraula
	 * @param respectaMajuscules
	 * @param respectaAccents
	 * @return ficar algo
	 */
	public String buscaCoincidenciesILlistaDirectori(File directori, String indent, String paraula,
			boolean respectaMajuscules, boolean respectaAccents) {
		File[] archius = directori.listFiles();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String resultat = "";

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

	public String recorreDirectoriIReemplacaParaula(File directori, String indent, String paraulaVella,
			String paraulaNova, boolean respectaMajuscules, boolean respectaAccents) {
		File[] archius = directori.listFiles();
		String res = "";
		if (archius != null) {
			for (File file : archius) {
				if (file.isDirectory()) {
					// Si es directori
					res += indent + "|-- " + file.getName() + "\n";
					res += recorreDirectoriIReemplacaParaula(file, indent + "|   ", paraulaVella, paraulaNova,
							respectaMajuscules, respectaAccents);
				} else {
					// Si es arxiu, i comprova si es de text
					if (UtilsArxius.esArxiuDeText(file)) {
						res += indent + "|-- " + file.getName() + " (" + recorreArxiuIRemplaca(file, paraulaVella,
								paraulaNova, respectaMajuscules, respectaAccents) + ") \n";
					} else {
						res += indent + "|-- " + file.getName() + " (0) \n";
					}
				}
			}
		}
		return res;
	}

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
			while (linea != null) {
				if (!respectaAccents) {
					linea = UtilsArxius.eliminaAccents(linea);
					paraulaNova = UtilsArxius.eliminaAccents(paraulaNova);
				}

				if (!respectaMajuscules) {
					linea = linea.toLowerCase();
					paraulaNova = paraulaNova.toLowerCase();
				}

				novaLinea = linea.replace(paraulaVella, paraulaNova);
				int index = 0;
				while ((index = linea.indexOf(paraulaVella, index)) != -1) {
					reemplacos++;
					index += paraulaVella.length(); // Meneja el index per seguir buscant
				}

				linea = br.readLine();
				escriuLineaEnArxiu(nouArxiu, novaLinea);
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(reemplacos);
		return reemplacos;
	}

	private boolean escriuLineaEnArxiu(File arxiu, String text) {
		try {
			FileWriter fw = new FileWriter(arxiu, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.newLine();
			bw.close();
			fw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
