package aev1;

import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import aev1.*;

public class Controller {

	private View vista;
	private Model model;
	ActionListener actionListenerAttachFile;
	ActionListener actionListenerBuscarCoincidencia;
	ActionListener actionListenerReemplacarParaula;

	public Controller(View vista, Model model) {
		this.vista = vista;
		this.model = model;
		initEventHandlers();
	}

	public void initEventHandlers() {
		actionListenerAttachFile = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser selectorArxius = new JFileChooser();

				// Configura el títol del diàleg
				selectorArxius.setDialogTitle("Selecciona un directori");

				// Estableix el mode de selecció a només directoris
				selectorArxius.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				// Mostra el diàleg per seleccionar un directori
				int seleccioUsuari = selectorArxius.showOpenDialog(null);

				if (seleccioUsuari == JFileChooser.APPROVE_OPTION) {
					File directoriSeleccionat = selectorArxius.getSelectedFile();

					System.out.println("Directori seleccionat: " + directoriSeleccionat.getAbsolutePath());

					// Assigna el directori seleccionat al model (o realitza qualsevol altra acció
					// necessària)
					model.directori = directoriSeleccionat;
					vista.getTxtRutaActual().setText(directoriSeleccionat.getAbsolutePath());
					vista.getTxtEstructuraDirectori().setText(model.llistarArchiusRecursius(directoriSeleccionat, ""));
				}
			}

		};

		vista.getBtnSeleccionaArxiu().addActionListener(actionListenerAttachFile);

		actionListenerBuscarCoincidencia = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				vista.getTxtEstructuraDirectori().setText(model.buscaCoincidenciesILlistaDirectori(model.directori, "",
						vista.getTxtBuscarCoincidencia().getText(), vista.getChkRespectarMajuscules().isSelected(),
						vista.getChkRespectarAccents().isSelected()));
			}
		};

		vista.getBtnBuscarCoincidencia().addActionListener(actionListenerBuscarCoincidencia);

		actionListenerReemplacarParaula = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				vista.getTxtEstructuraDirectori().setText(model.recorreDirectoriIReemplacaParaula(model.directori, "",
						vista.getTxtBuscarCoincidencia().getText(), vista.getTxtReemplacarCoincidencia().getText(),
						vista.getChkRespectarMajuscules().isSelected(), vista.getChkRespectarAccents().isSelected()));
			}
		};

		vista.getBtnReemplacarContingut().addActionListener(actionListenerReemplacarParaula);

	}
}
