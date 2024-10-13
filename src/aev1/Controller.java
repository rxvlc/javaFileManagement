package aev1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Controlador que gestiona la interacció entre la vista i el model en
 * l'aplicació de cerca i substitució de coincidències en arxius.
 */
public class Controller {

	private View vista;
	private Model model;
	ActionListener actionListenerAttachFile;
	ActionListener actionListenerBuscarCoincidencia;
	ActionListener actionListenerReemplacarParaula;

	/**
	 * Constructor del controlador. Inicialitza la vista i el model, i configura els
	 * event handlers per als diferents components de la interfície gràfica.
	 * 
	 * @param vista La vista que representa la interfície gràfica.
	 * @param model El model que gestiona la lògica de l'aplicació.
	 */
	public Controller(View vista, Model model) {
		this.vista = vista;
		this.model = model;
		initEventHandlers();
	}

	/**
	 * Inicialitza els event handlers per a gestionar les accions de la vista, com
	 * la selecció d'arxius, la cerca de coincidències i la substitució de paraules.
	 */
	public void initEventHandlers() {
		// Gestiona la selecció d'un directori des del JFileChooser
		actionListenerAttachFile = new ActionListener() {

			/**
			 * Accions a realitzar quan l'usuari selecciona un directori.
			 * 
			 * @param e El event que ocorre quan es selecciona l'arxiu.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser selectorArxius = new JFileChooser();

				// Configura el títol del diàleg
				selectorArxius.setDialogTitle("Selecciona un directori");

				// Estableix el mode de selecció a només directoris
				selectorArxius.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				// Mostra el diàleg per seleccionar un directori
				int seleccioUsuari = selectorArxius.showOpenDialog(null);

				// Si l'usuari selecciona un directori, es processa
				if (seleccioUsuari == JFileChooser.APPROVE_OPTION) {
					File directoriSeleccionat = selectorArxius.getSelectedFile();
					// Assigna el directori seleccionat al model
					model.directori = directoriSeleccionat;

					// Actualitza la vista amb el directori seleccionat
					String rutaDirectori = directoriSeleccionat.getAbsolutePath();
					vista.getTxtRutaActual().setText(rutaDirectori);

					// Mostra l'estructura del directori seleccionat a la vista
					String estructuraDirectori = model.llistarArchiusRecursius(directoriSeleccionat, "");
					vista.getTxtEstructuraDirectori().setText(estructuraDirectori);
				}
			}
		};

		// Afegeix el event handler al botó de selecció d'arxiu
		vista.getBtnSeleccionaArxiu().addActionListener(actionListenerAttachFile);

		// Gestiona l'acció de cerca de coincidències en el directori
		actionListenerBuscarCoincidencia = new ActionListener() {

			/**
			 * Accions a realitzar quan l'usuari prem el botó de buscar coincidències.
			 * 
			 * @param e El event que ocorre quan es prem el botó.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				// Verifica si el directori és vàlid i existeix
				if (model.directori != null && model.directori.exists()) {

					// Obtindre la paraula a buscar de la vista
					String paraulaBuscar = vista.getTxtBuscarCoincidencia().getText();

					if (!paraulaBuscar.isEmpty()) {

						// Comprova les opcions de majúscules i accents
						boolean respectarMajuscules = vista.getChkRespectarMajuscules().isSelected();
						boolean respectarAccents = vista.getChkRespectarAccents().isSelected();

						// Cerca les coincidències en el directori
						String resultat = model.buscaCoincidenciesILlistaDirectori(model.directori, "", paraulaBuscar,
								respectarMajuscules, respectarAccents);

						// Mostra el resultat en la vista
						vista.getTxtEstructuraDirectori().setText(resultat);
					} else {
						// Mostra un missatge d'error si no s'introdueix una paraula
						JOptionPane.showMessageDialog(vista,
								"Has de ficar almenys un caracter per a buscar coincidències.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					// Mostra un missatge d'error si no es selecciona un directori vàlid
					JOptionPane.showMessageDialog(vista, "El directori no existeix! Selecciona'n un de vàlid.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};

		// Afegeix el event handler al botó de buscar coincidències
		vista.getBtnBuscarCoincidencia().addActionListener(actionListenerBuscarCoincidencia);

		// Gestiona l'acció de substitució de paraules en el directori
		actionListenerReemplacarParaula = new ActionListener() {

			/**
			 * Accions a realitzar quan l'usuari prem el botó de substituir coincidències.
			 * 
			 * @param e El event que ocorre quan es prem el botó.
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				// Verifica si el directori és vàlid i existeix
				if (model.directori != null && model.directori.exists()) {

					// Obtindre la paraula a buscar de la vista
					String paraulaBuscar = vista.getTxtBuscarCoincidencia().getText();

					if (!paraulaBuscar.isEmpty()) {

						// Obtindre la paraula de reemplaçament de la vista
						String paraulaReemplacar = vista.getTxtReemplacarCoincidencia().getText();

						// Comprova les opcions de majúscules i accents
						boolean respectarMajuscules = vista.getChkRespectarMajuscules().isSelected();
						boolean respectarAccents = vista.getChkRespectarAccents().isSelected();

						// Realitza la substitució en el directori
						String resultat = model.recorreDirectoriIReemplacaParaula(model.directori, "", paraulaBuscar,
								paraulaReemplacar, respectarMajuscules, respectarAccents);

						// Mostra el resultat en la vista
						vista.getTxtEstructuraDirectori().setText(resultat);
					} else {
						// Mostra un missatge d'error si no s'introdueix una paraula per a buscar
						JOptionPane.showMessageDialog(vista,
								"Has de ficar almenys un caracter per a buscar coincidències.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					// Mostra un missatge d'error si no es selecciona un directori vàlid
					JOptionPane.showMessageDialog(vista, "El directori no existeix! Selecciona'n un de vàlid.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};

		// Afegeix el event handler al botó de substituir contingut
		vista.getBtnReemplacarContingut().addActionListener(actionListenerReemplacarParaula);

	}
}
