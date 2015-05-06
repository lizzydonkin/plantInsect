package plantsInsects.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import plantsInsects.ParameterSerializationHelper;
import plantsInsects.PlantParams;
import plantsInsects.enums.PlantHeight;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.ParametersWriter;
import repast.simphony.parameter.Schema;
import repast.simphony.scenario.ScenarioLoader;
import repast.simphony.ui.GUIParametersManager;
import repast.simphony.ui.RSApplication;
import repast.simphony.ui.plugin.UIActionExtensions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class PlantParamsPanel extends JPanel implements ActionListener {
	
	private PlantParams params;
	private ParameterSerializationHelper serializationHelper;
	
	private JTextField plantIdEdit;
	private JSpinner percentSpinner;
	private JSpinner thresholdSpinner;
	private JSpinner reprodSpinner;
	private JComboBox heightCombo;
	private JLabel colourLabel;
	private JButton setColourButton;
	private JButton saveButton;
	private JButton loadButton;

	/**
	 * Create the panel.
	 */
	public PlantParamsPanel() {
		
		params = new PlantParams();
		serializationHelper = new ParameterSerializationHelper();

		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl_panel_2);
		
		JLabel lblPlantId = new JLabel("Plant Id");
		GridBagConstraints gbc_lblPlantId = new GridBagConstraints();
		gbc_lblPlantId.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlantId.anchor = GridBagConstraints.EAST;
		gbc_lblPlantId.gridx = 0;
		gbc_lblPlantId.gridy = 0;
		add(lblPlantId, gbc_lblPlantId);
		
		plantIdEdit = new JTextField();
		plantIdEdit.setToolTipText("The name of the plant. If a plant with this name has been saved its parameters can quickly be loaded.");
		GridBagConstraints gbc_plantIdEdit = new GridBagConstraints();
		gbc_plantIdEdit.insets = new Insets(0, 0, 5, 5);
		gbc_plantIdEdit.fill = GridBagConstraints.HORIZONTAL;
		gbc_plantIdEdit.gridx = 1;
		gbc_plantIdEdit.gridy = 0;
		add(plantIdEdit, gbc_plantIdEdit);
		plantIdEdit.setColumns(10);
		plantIdEdit.addActionListener(this);
		
		loadButton = new JButton("Try Load");
		GridBagConstraints gbc_loadButton = new GridBagConstraints();
		gbc_loadButton.fill = GridBagConstraints.BOTH;
		gbc_loadButton.insets = new Insets(0, 0, 5, 0);
		gbc_loadButton.gridx = 2;
		gbc_loadButton.gridy = 0;
		add(loadButton, gbc_loadButton);
		loadButton.addActionListener(this);
		
		JLabel lblDisplayColour = new JLabel("Display Colour");
		GridBagConstraints gbc_lblDisplayColour = new GridBagConstraints();
		gbc_lblDisplayColour.anchor = GridBagConstraints.EAST;
		gbc_lblDisplayColour.insets = new Insets(0, 0, 5, 5);
		gbc_lblDisplayColour.gridx = 0;
		gbc_lblDisplayColour.gridy = 1;
		add(lblDisplayColour, gbc_lblDisplayColour);
		
		colourLabel = new JLabel("");
		colourLabel.setToolTipText("Plant's colour.");
		colourLabel.setOpaque(true);
		colourLabel.setBackground(Color.YELLOW);
		GridBagConstraints gbc_colourLabel = new GridBagConstraints();
		gbc_colourLabel.fill = GridBagConstraints.BOTH;
		gbc_colourLabel.insets = new Insets(0, 0, 5, 5);
		gbc_colourLabel.gridx = 1;
		gbc_colourLabel.gridy = 1;
		add(colourLabel, gbc_colourLabel);
		
		setColourButton = new JButton("Set Colour");
		GridBagConstraints gbc_setColourButton = new GridBagConstraints();
		gbc_setColourButton.fill = GridBagConstraints.BOTH;
		gbc_setColourButton.insets = new Insets(0, 0, 5, 0);
		gbc_setColourButton.gridx = 2;
		gbc_setColourButton.gridy = 1;
		add(setColourButton, gbc_setColourButton);
		setColourButton.addActionListener(this);
		
		JLabel lblPlantPercentage = new JLabel("Plant Percentage");
		GridBagConstraints gbc_lblPlantPercentage = new GridBagConstraints();
		gbc_lblPlantPercentage.anchor = GridBagConstraints.EAST;
		gbc_lblPlantPercentage.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlantPercentage.gridx = 0;
		gbc_lblPlantPercentage.gridy = 2;
		add(lblPlantPercentage, gbc_lblPlantPercentage);
		
		percentSpinner = new JSpinner();
		percentSpinner.setToolTipText("The percentage of the field that this plant species will take.");
		percentSpinner.setModel(new SpinnerNumberModel(0.25, 0.0, 1.0, 0.01));
		GridBagConstraints gbc_spinnerPlantPerc = new GridBagConstraints();
		gbc_spinnerPlantPerc.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPlantPerc.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerPlantPerc.gridx = 1;
		gbc_spinnerPlantPerc.gridy = 2;
		add(percentSpinner, gbc_spinnerPlantPerc);
		
		JLabel lblDamageThreshold = new JLabel("Damage Threshold");
		GridBagConstraints gbc_lblDamageThreshold = new GridBagConstraints();
		gbc_lblDamageThreshold.anchor = GridBagConstraints.EAST;
		gbc_lblDamageThreshold.insets = new Insets(0, 0, 5, 5);
		gbc_lblDamageThreshold.gridx = 0;
		gbc_lblDamageThreshold.gridy = 3;
		add(lblDamageThreshold, gbc_lblDamageThreshold);
		
		thresholdSpinner = new JSpinner();
		thresholdSpinner.setToolTipText("The maximum amount of damage (hatched eggs) a plant can take.");
		thresholdSpinner.setModel(new SpinnerNumberModel(new Integer(1000), new Integer(50), null, new Integer(10)));
		GridBagConstraints gbc_spinnerThreshold = new GridBagConstraints();
		gbc_spinnerThreshold.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerThreshold.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerThreshold.gridx = 1;
		gbc_spinnerThreshold.gridy = 3;
		add(thresholdSpinner, gbc_spinnerThreshold);
		
		JLabel lblReproductiveSuccess = new JLabel("Reproductive Success");
		GridBagConstraints gbc_lblReproductiveSuccess = new GridBagConstraints();
		gbc_lblReproductiveSuccess.anchor = GridBagConstraints.EAST;
		gbc_lblReproductiveSuccess.insets = new Insets(0, 0, 5, 5);
		gbc_lblReproductiveSuccess.gridx = 0;
		gbc_lblReproductiveSuccess.gridy = 4;
		add(lblReproductiveSuccess, gbc_lblReproductiveSuccess);
		
		reprodSpinner = new JSpinner();
		reprodSpinner.setToolTipText("Maximum eggs that can hatch on a plant in a day. The actual amount of eggs that hatch may be lower depending on the total number of eggs on the plant and the damage it has taken.");
		reprodSpinner.setModel(new SpinnerNumberModel(new Integer(20), new Integer(1), null, new Integer(1)));
		GridBagConstraints gbc_spinnerReprod = new GridBagConstraints();
		gbc_spinnerReprod.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerReprod.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerReprod.gridx = 1;
		gbc_spinnerReprod.gridy = 4;
		add(reprodSpinner, gbc_spinnerReprod);
		
		JLabel lblPlantHeigth = new JLabel("Plant Height");
		GridBagConstraints gbc_lblPlantHeigth = new GridBagConstraints();
		gbc_lblPlantHeigth.anchor = GridBagConstraints.EAST;
		gbc_lblPlantHeigth.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlantHeigth.gridx = 0;
		gbc_lblPlantHeigth.gridy = 5;
		add(lblPlantHeigth, gbc_lblPlantHeigth);
		
		heightCombo = new JComboBox(PlantHeight.values());
		heightCombo.setToolTipText("Used by insects that use vision as their sensory mode to determine if they can see a certain plant.");
		GridBagConstraints gbc_heightCombo = new GridBagConstraints();
		gbc_heightCombo.insets = new Insets(0, 0, 5, 0);
		gbc_heightCombo.gridwidth = 2;
		gbc_heightCombo.fill = GridBagConstraints.HORIZONTAL;
		gbc_heightCombo.gridx = 1;
		gbc_heightCombo.gridy = 5;
		add(heightCombo, gbc_heightCombo);
		
		saveButton = new JButton("Save Plant");
		GridBagConstraints gbc_saveButton = new GridBagConstraints();
		gbc_saveButton.insets = new Insets(0, 0, 0, 5);
		gbc_saveButton.gridx = 1;
		gbc_saveButton.gridy = 6;
		add(saveButton, gbc_saveButton);
		saveButton.addActionListener(this);
		
		setParams(params);
	}

	public PlantParams getParams() {
		params = new PlantParams(plantIdEdit.getText(), (double)percentSpinner.getValue(), (int)thresholdSpinner.getValue(),
				(int)reprodSpinner.getValue(), (PlantHeight)heightCombo.getSelectedItem(), colourLabel.getBackground());
		
		return params;
	}

	public void setParams(PlantParams params) {
		this.params = params;
		
		plantIdEdit.setText(params.getPlantId());
		double maxPerc = (Double) ((SpinnerNumberModel) percentSpinner.getModel()).getMaximum();
		percentSpinner.setValue(Math.min(params.getPercentage(), maxPerc));
		thresholdSpinner.setValue(params.getDamageThreshold());
		reprodSpinner.setValue(params.getReproductiveSuccess());
		heightCombo.setSelectedItem(params.getHeight());
		colourLabel.setBackground(params.getDisplayCol());
	}
	
	public double getPercentageLeft() {
		SpinnerNumberModel model = (SpinnerNumberModel) percentSpinner.getModel();
		return ((Double) model.getMaximum()) - ((double) model.getValue());
	}
	
	public void updatePercentage(double max, double preffered) {
		SpinnerNumberModel model = (SpinnerNumberModel) percentSpinner.getModel();
		model.setMaximum(max);
		model.setValue(preffered);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == setColourButton) {
			Color newCol = JColorChooser.showDialog(this, "Choose Display Color", colourLabel.getBackground());
			if(newCol != null)
				colourLabel.setBackground(newCol);
		} else if(arg0.getSource() == saveButton) {
			serializationHelper.savePlant(getParams());
		} else if(arg0.getSource() == loadButton || arg0.getSource() == plantIdEdit) {
			PlantParams pp = serializationHelper.loadPlant(plantIdEdit.getText());
			if(pp == null) {
				JOptionPane.showMessageDialog(this, "Could not find a saved plant with that Id.");
			} else {
				setParams(pp);
			}
		}
	}

}
