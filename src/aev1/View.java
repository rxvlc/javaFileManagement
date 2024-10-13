package aev1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel cPContDirectori;
	JButton btnSeleccionaDirectori;
	private JLabel lblTitulEstructuraDirectori;
	JTextArea txtEstructuraDirectori;
	private JTextField txtBuscarCoincidencia;
	JButton btnBuscarCoincidencia;
	private JTextField txtRutaActual;
	private JTextField txtReemplacContingut;
	private JButton btnReemplacarContingut;
	JCheckBox chkRespectarMajuscules;
	JCheckBox chkRespectarAccents;
	private JScrollPane scrollPane;

	/**
	 * Constructor de vista
	 */
	public View() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 901, 495);
		cPContDirectori = new JPanel();
		cPContDirectori.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(cPContDirectori);
		cPContDirectori.setLayout(null);

		btnSeleccionaDirectori = new JButton("Seleccionar carpeta");
		btnSeleccionaDirectori.setBounds(10, 15, 160, 23);
		cPContDirectori.add(btnSeleccionaDirectori);

		JLabel lblTitulCarpetaActual = new JLabel("Carpeta actual: ");
		lblTitulCarpetaActual.setBounds(180, 19, 102, 14);
		cPContDirectori.add(lblTitulCarpetaActual);

		lblTitulEstructuraDirectori = new JLabel("Estructura Directori:");
		lblTitulEstructuraDirectori.setBounds(10, 165, 160, 14);
		cPContDirectori.add(lblTitulEstructuraDirectori);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 183, 867, 263);
		cPContDirectori.add(scrollPane);

		txtEstructuraDirectori = new JTextArea();
		scrollPane.setViewportView(txtEstructuraDirectori);
		txtEstructuraDirectori.setEditable(false);

		btnBuscarCoincidencia = new JButton("Buscar Coincidència");
		btnBuscarCoincidencia.setBounds(709, 48, 168, 23);
		cPContDirectori.add(btnBuscarCoincidencia);

		txtBuscarCoincidencia = new JTextField();
		txtBuscarCoincidencia.setBounds(20, 49, 679, 20);
		cPContDirectori.add(txtBuscarCoincidencia);
		txtBuscarCoincidencia.setColumns(10);

		txtRutaActual = new JTextField();
		txtRutaActual.setEditable(false);
		txtRutaActual.setBounds(268, 16, 609, 20);
		cPContDirectori.add(txtRutaActual);
		txtRutaActual.setColumns(10);

		txtReemplacContingut = new JTextField();
		txtReemplacContingut.setBounds(20, 80, 679, 20);
		cPContDirectori.add(txtReemplacContingut);
		txtReemplacContingut.setColumns(10);

		btnReemplacarContingut = new JButton("Reemplaçar Contingut");
		btnReemplacarContingut.setBounds(709, 80, 168, 23);
		cPContDirectori.add(btnReemplacarContingut);

		chkRespectarMajuscules = new JCheckBox("Respectar Majúscules");
		chkRespectarMajuscules.setBounds(20, 107, 220, 23);
		cPContDirectori.add(chkRespectarMajuscules);

		chkRespectarAccents = new JCheckBox("Respectar accents");
		chkRespectarAccents.setBounds(20, 135, 147, 23);
		cPContDirectori.add(chkRespectarAccents);
		this.setVisible(true);
	}

	public JButton getBtnSeleccionaArxiu() {
		return this.btnSeleccionaDirectori;
	}

	public JTextField getTxtRutaActual() {
		return this.txtRutaActual;
	}

	public JTextArea getTxtEstructuraDirectori() {
		return this.txtEstructuraDirectori;
	}

	public JButton getBtnBuscarCoincidencia() {
		return this.btnBuscarCoincidencia;
	}

	public JTextField getTxtBuscarCoincidencia() {
		return this.txtBuscarCoincidencia;
	}

	public JCheckBox getChkRespectarMajuscules() {
		return this.chkRespectarMajuscules;
	}

	public JCheckBox getChkRespectarAccents() {
		return this.chkRespectarAccents;
	}

	public JTextField getTxtReemplacarCoincidencia() {
		return this.txtReemplacContingut;
	}

	public JButton getBtnReemplacarContingut() {
		return this.btnReemplacarContingut;
	}

}
