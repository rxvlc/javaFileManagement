package aev1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Controlador que gestiona la interacció entre la vista i el model en 
 * l'aplicació de busqueda i substitució de coincidències en arxius.
 */
public class Controller {

    private View vista;
    private Model model;
    ActionListener actionListenerAttachFile;
    ActionListener actionListenerBuscarCoincidencia;
    ActionListener actionListenerReemplacarParaula;

    /**
     * Constructor del controlador. Inicialitza la vista i el model, i configura 
     * els event handlers per als diferents components de la interfície gràfica.
     * 
     * @param vista  La vista que representa la interfície gràfica.
     * @param model  El model que gestiona la lògica de l'aplicació.
     */
    public Controller(View vista, Model model) {
        this.vista = vista;
        this.model = model;
        initEventHandlers();
    }

    /**
     * Inicialitza els event handlers per a gestionar les accions de la vista, 
     * com la selecció d'arxius, la cerca de coincidències i la substitució de paraules.
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
                    vista.getTxtRutaActual().setText(directoriSeleccionat.getAbsolutePath());
                    vista.getTxtEstructuraDirectori()
                            .setText(model.llistarArchiusRecursius(directoriSeleccionat, ""));
                }
            }
        };

        // Afegeix el event handler al botó de selecció d'arxiu
        vista.getBtnSeleccionaArxiu().addActionListener(actionListenerAttachFile);

        // Gestiona l'acció de busqueda de coincidències en el directori
        actionListenerBuscarCoincidencia = new ActionListener() {

            /**
             * Accions a realitzar quan l'usuari polsa el botó de buscar coincidències.
             * 
             * @param e El event que ocorre quan es prem el botó.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica si el directori és vàlid i existeix
                if (model.directori != null && model.directori.exists()) {
                    if (!vista.getTxtBuscarCoincidencia().getText().isEmpty()) {
                        vista.getTxtEstructuraDirectori()
                                .setText(model.buscaCoincidenciesILlistaDirectori(model.directori, "",
                                        vista.getTxtBuscarCoincidencia().getText(),
                                        vista.getChkRespectarMajuscules().isSelected(),
                                        vista.getChkRespectarAccents().isSelected()));
                    } else {
                        // Mostra un missatge d'error si no s'introdueix una paraula
                        JOptionPane.showMessageDialog(vista,
                                "Has de ficar al menys un caracter per a buscar coincidències.", "Error",
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
             * Accions a realitzar quan l'usuari polsa el botó de substituir coincidències.
             * 
             * @param e El event que ocorre quan es polsa el botó.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica si el directori és vàlid i existeix
                if (model.directori != null && model.directori.exists()) {
                    if (!vista.getTxtBuscarCoincidencia().getText().isEmpty()) {
                        vista.getTxtEstructuraDirectori()
                                .setText(model.recorreDirectoriIReemplacaParaula(model.directori, "",
                                        vista.getTxtBuscarCoincidencia().getText(),
                                        vista.getTxtReemplacarCoincidencia().getText(),
                                        vista.getChkRespectarMajuscules().isSelected(),
                                        vista.getChkRespectarAccents().isSelected()));
                    } else {
                        // Mostra un missatge d'error si no s'introdueix una paraula per a buscar
                        JOptionPane.showMessageDialog(vista,
                                "Has de ficar al menys un caracter per a buscar coincidències.", "Error",
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
