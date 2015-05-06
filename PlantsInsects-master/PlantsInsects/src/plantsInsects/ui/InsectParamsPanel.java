package plantsInsects.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import plantsInsects.InsectParams;
import plantsInsects.ParameterSerializationHelper;
import plantsInsects.PlantParams;
import plantsInsects.enums.InsectInitialDistribution;
import plantsInsects.enums.InsectSensoryMode;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.Schema;
import repast.simphony.ui.GUIParametersManager;
import repast.simphony.ui.RSApplication;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsectParamsPanel extends JPanel implements ActionListener {

	private InsectParams params;
	private ArrayList<PlantParams> plantParams;
	private ParameterSerializationHelper serializationHelper;
	
	private JTextField insectIdEdit;
	private JSpinner maxEggsSpinner;
	private JSpinner initialCountSpinner;
	private JTable eggCountTable;
	private JSpinner tradeOffSpinner;
	private JSpinner toleranceSpinner;
	private JSpinner mortalitySpinner;
	private JSpinner maxFlightSpinner;
	private JSpinner eggHatchTimeSpinner;
	private JComboBox sensoryModeCombo;
	private JComboBox distribCombo;
	private JSpinner memSizeSpinner;
	private JSpinner migOutSpinner;
	private JSpinner migInSpinner;
	private JButton setColourButton;
	private JLabel colourLabel;
	private JButton loadButton;
	private JButton saveButton;
	
	public InsectParamsPanel(ArrayList<PlantParams> plantParams) {
		
		params = new InsectParams(plantParams);
		this.plantParams = plantParams;
		serializationHelper = new ParameterSerializationHelper();

		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_3.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
		setLayout(gbl_panel_3);
		
		JLabel lblInsectId = new JLabel("Insect Id");
		GridBagConstraints gbc_lblInsectId = new GridBagConstraints();
		gbc_lblInsectId.anchor = GridBagConstraints.EAST;
		gbc_lblInsectId.insets = new Insets(0, 0, 5, 5);
		gbc_lblInsectId.gridx = 0;
		gbc_lblInsectId.gridy = 0;
		add(lblInsectId, gbc_lblInsectId);
		
		insectIdEdit = new JTextField();
		insectIdEdit.setToolTipText("The name of the insect. If an insect with this name has been saved its parameters can quickly be loaded.");
		GridBagConstraints gbc_insectIdEdit = new GridBagConstraints();
		gbc_insectIdEdit.insets = new Insets(0, 0, 5, 5);
		gbc_insectIdEdit.fill = GridBagConstraints.HORIZONTAL;
		gbc_insectIdEdit.gridx = 1;
		gbc_insectIdEdit.gridy = 0;
		add(insectIdEdit, gbc_insectIdEdit);
		insectIdEdit.setColumns(10);
		insectIdEdit.addActionListener(this);
		
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
		colourLabel.setToolTipText("Insect's colour");
		colourLabel.setBackground(Color.RED);
		colourLabel.setOpaque(true);
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
		
		JLabel lblInitialCount = new JLabel("Initial Count");
		GridBagConstraints gbc_lblInitialCount = new GridBagConstraints();
		gbc_lblInitialCount.anchor = GridBagConstraints.EAST;
		gbc_lblInitialCount.insets = new Insets(0, 0, 5, 5);
		gbc_lblInitialCount.gridx = 0;
		gbc_lblInitialCount.gridy = 2;
		add(lblInitialCount, gbc_lblInitialCount);
		
		initialCountSpinner = new JSpinner();
		initialCountSpinner.setToolTipText("Initial number of insects at the start of the simulation.");
		initialCountSpinner.setModel(new SpinnerNumberModel(20, 0, 200, 1));
		GridBagConstraints gbc_spinnerInsectCount = new GridBagConstraints();
		gbc_spinnerInsectCount.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerInsectCount.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerInsectCount.gridx = 1;
		gbc_spinnerInsectCount.gridy = 2;
		add(initialCountSpinner, gbc_spinnerInsectCount);
		
		JLabel lblMaximumEggs = new JLabel("Maximum Eggs");
		GridBagConstraints gbc_lblMaximumEggs = new GridBagConstraints();
		gbc_lblMaximumEggs.anchor = GridBagConstraints.EAST;
		gbc_lblMaximumEggs.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaximumEggs.gridx = 0;
		gbc_lblMaximumEggs.gridy = 3;
		add(lblMaximumEggs, gbc_lblMaximumEggs);
		
		maxEggsSpinner = new JSpinner();
		maxEggsSpinner.setToolTipText("The maximum number of eggs an insect is born with.");
		maxEggsSpinner.setModel(new SpinnerNumberModel(5, 1, 50, 1));
		GridBagConstraints gbc_spinnerMaxEggs = new GridBagConstraints();
		gbc_spinnerMaxEggs.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerMaxEggs.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerMaxEggs.gridx = 1;
		gbc_spinnerMaxEggs.gridy = 3;
		add(maxEggsSpinner, gbc_spinnerMaxEggs);
		
		JLabel lblTradeoff = new JLabel("Trade-off");
		GridBagConstraints gbc_lblTradeoff = new GridBagConstraints();
		gbc_lblTradeoff.anchor = GridBagConstraints.EAST;
		gbc_lblTradeoff.insets = new Insets(0, 0, 5, 5);
		gbc_lblTradeoff.gridx = 0;
		gbc_lblTradeoff.gridy = 4;
		add(lblTradeoff, gbc_lblTradeoff);
		
		tradeOffSpinner = new JSpinner();
		tradeOffSpinner.setToolTipText("Percentage of eggs that die if laid on non-host plants (for specialists) and percentage of eggs that die on all plants (for generalists).");
		tradeOffSpinner.setModel(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));
		GridBagConstraints gbc_spinnerTradeOff = new GridBagConstraints();
		gbc_spinnerTradeOff.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerTradeOff.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerTradeOff.gridx = 1;
		gbc_spinnerTradeOff.gridy = 4;
		add(tradeOffSpinner, gbc_spinnerTradeOff);
		
		JLabel lblTolerance = new JLabel("Tolerance");
		GridBagConstraints gbc_lblTolerance = new GridBagConstraints();
		gbc_lblTolerance.anchor = GridBagConstraints.EAST;
		gbc_lblTolerance.insets = new Insets(0, 0, 5, 5);
		gbc_lblTolerance.gridx = 0;
		gbc_lblTolerance.gridy = 5;
		add(lblTolerance, gbc_lblTolerance);
		
		toleranceSpinner = new JSpinner();
		toleranceSpinner.setToolTipText("How long (in days) a specialist will persist in not laying eggs on non-host plants.");
		toleranceSpinner.setModel(new SpinnerNumberModel(0, 0, 10, 1));
		GridBagConstraints gbc_spinnerTolerance = new GridBagConstraints();
		gbc_spinnerTolerance.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerTolerance.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerTolerance.gridx = 1;
		gbc_spinnerTolerance.gridy = 5;
		add(toleranceSpinner, gbc_spinnerTolerance);
		
		JLabel lblDailyMortalityRate = new JLabel("Daily Mortality Rate");
		GridBagConstraints gbc_lblDailyMortalityRate = new GridBagConstraints();
		gbc_lblDailyMortalityRate.anchor = GridBagConstraints.EAST;
		gbc_lblDailyMortalityRate.insets = new Insets(0, 0, 5, 5);
		gbc_lblDailyMortalityRate.gridx = 0;
		gbc_lblDailyMortalityRate.gridy = 6;
		add(lblDailyMortalityRate, gbc_lblDailyMortalityRate);
		
		mortalitySpinner = new JSpinner();
		mortalitySpinner.setToolTipText("Percent of insects that die daily for reasons outside of laying all their eggs.");
		mortalitySpinner.setModel(new SpinnerNumberModel(0.001, 0.0, 1.0, 0.001));
		GridBagConstraints gbc_spinnerMortality = new GridBagConstraints();
		gbc_spinnerMortality.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerMortality.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerMortality.gridx = 1;
		gbc_spinnerMortality.gridy = 6;
		add(mortalitySpinner, gbc_spinnerMortality);
		
		JLabel lblMaximumFlightLength = new JLabel("Maximum Flight Length");
		GridBagConstraints gbc_lblMaximumFlightLength = new GridBagConstraints();
		gbc_lblMaximumFlightLength.anchor = GridBagConstraints.EAST;
		gbc_lblMaximumFlightLength.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaximumFlightLength.gridx = 0;
		gbc_lblMaximumFlightLength.gridy = 7;
		add(lblMaximumFlightLength, gbc_lblMaximumFlightLength);
		
		maxFlightSpinner = new JSpinner();
		maxFlightSpinner.setToolTipText("The maximum distance (in number of plants) an insect can fly. Also used for maximum range of vision for visual insects.");
		maxFlightSpinner.setModel(new SpinnerNumberModel(10, 1, 20, 1));
		GridBagConstraints gbc_spinnerMaxFlight = new GridBagConstraints();
		gbc_spinnerMaxFlight.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerMaxFlight.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerMaxFlight.gridx = 1;
		gbc_spinnerMaxFlight.gridy = 7;
		add(maxFlightSpinner, gbc_spinnerMaxFlight);
		
		JLabel lblEggHatchTime = new JLabel("Egg Hatch Time");
		GridBagConstraints gbc_lblEggHatchTime = new GridBagConstraints();
		gbc_lblEggHatchTime.anchor = GridBagConstraints.EAST;
		gbc_lblEggHatchTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblEggHatchTime.gridx = 0;
		gbc_lblEggHatchTime.gridy = 8;
		add(lblEggHatchTime, gbc_lblEggHatchTime);
		
		eggHatchTimeSpinner = new JSpinner();
		eggHatchTimeSpinner.setToolTipText("The number of days an egg takes to hatch after being laid.");
		eggHatchTimeSpinner.setModel(new SpinnerNumberModel(2, 1, 10, 1));
		GridBagConstraints gbc_spinnerEggHatch = new GridBagConstraints();
		gbc_spinnerEggHatch.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerEggHatch.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerEggHatch.gridx = 1;
		gbc_spinnerEggHatch.gridy = 8;
		add(eggHatchTimeSpinner, gbc_spinnerEggHatch);
		
		JLabel lblSensoryMode = new JLabel("Sensory Mode");
		GridBagConstraints gbc_lblSensoryMode = new GridBagConstraints();
		gbc_lblSensoryMode.anchor = GridBagConstraints.EAST;
		gbc_lblSensoryMode.insets = new Insets(0, 0, 5, 5);
		gbc_lblSensoryMode.gridx = 0;
		gbc_lblSensoryMode.gridy = 9;
		add(lblSensoryMode, gbc_lblSensoryMode);
		
		sensoryModeCombo = new JComboBox(InsectSensoryMode.values());
		sensoryModeCombo.setToolTipText("The way an insect finds the next plant to visit.");
		GridBagConstraints gbc_sensoryModeCombo = new GridBagConstraints();
		gbc_sensoryModeCombo.insets = new Insets(0, 0, 5, 0);
		gbc_sensoryModeCombo.gridwidth = 2;
		gbc_sensoryModeCombo.fill = GridBagConstraints.HORIZONTAL;
		gbc_sensoryModeCombo.gridx = 1;
		gbc_sensoryModeCombo.gridy = 9;
		add(sensoryModeCombo, gbc_sensoryModeCombo);
		
		JLabel lblInitialDistribution = new JLabel("Initial Distribution");
		GridBagConstraints gbc_lblInitialDistribution = new GridBagConstraints();
		gbc_lblInitialDistribution.anchor = GridBagConstraints.EAST;
		gbc_lblInitialDistribution.insets = new Insets(0, 0, 5, 5);
		gbc_lblInitialDistribution.gridx = 0;
		gbc_lblInitialDistribution.gridy = 10;
		add(lblInitialDistribution, gbc_lblInitialDistribution);
		
		distribCombo = new JComboBox(InsectInitialDistribution.values());
		distribCombo.setToolTipText("Initial spacial distribution of the insects.");
		GridBagConstraints gbc_distribCombo = new GridBagConstraints();
		gbc_distribCombo.insets = new Insets(0, 0, 5, 0);
		gbc_distribCombo.gridwidth = 2;
		gbc_distribCombo.fill = GridBagConstraints.HORIZONTAL;
		gbc_distribCombo.gridx = 1;
		gbc_distribCombo.gridy = 10;
		add(distribCombo, gbc_distribCombo);
		
		JLabel lblMemorySize = new JLabel("Memory Size");
		GridBagConstraints gbc_lblMemorySize = new GridBagConstraints();
		gbc_lblMemorySize.anchor = GridBagConstraints.EAST;
		gbc_lblMemorySize.insets = new Insets(0, 0, 5, 5);
		gbc_lblMemorySize.gridx = 0;
		gbc_lblMemorySize.gridy = 11;
		add(lblMemorySize, gbc_lblMemorySize);
		
		memSizeSpinner = new JSpinner();
		memSizeSpinner.setToolTipText("The number of plants that an insect remembers visiting.");
		memSizeSpinner.setModel(new SpinnerNumberModel(5, 0, 20, 1));
		GridBagConstraints gbc_spinnerMemSize = new GridBagConstraints();
		gbc_spinnerMemSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerMemSize.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerMemSize.gridx = 1;
		gbc_spinnerMemSize.gridy = 11;
		add(memSizeSpinner, gbc_spinnerMemSize);
		
		JLabel lblMigrationoutChance = new JLabel("Migration-out Chance");
		GridBagConstraints gbc_lblMigrationoutChance = new GridBagConstraints();
		gbc_lblMigrationoutChance.anchor = GridBagConstraints.EAST;
		gbc_lblMigrationoutChance.insets = new Insets(0, 0, 5, 5);
		gbc_lblMigrationoutChance.gridx = 0;
		gbc_lblMigrationoutChance.gridy = 12;
		add(lblMigrationoutChance, gbc_lblMigrationoutChance);
		
		migOutSpinner = new JSpinner();
		migOutSpinner.setToolTipText("Base chance that an insect decides to migrate. The actual chance will increase based on the contents of the memory and on whether the insect is at the border of the field.");
		migOutSpinner.setModel(new SpinnerNumberModel(0.001, 0.0, 1.0, 0.001));
		GridBagConstraints gbc_spinnerMigOut = new GridBagConstraints();
		gbc_spinnerMigOut.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerMigOut.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerMigOut.gridx = 1;
		gbc_spinnerMigOut.gridy = 12;
		add(migOutSpinner, gbc_spinnerMigOut);
		
		JLabel lblMigrationinChance = new JLabel("Migration-in Chance");
		GridBagConstraints gbc_lblMigrationinChance = new GridBagConstraints();
		gbc_lblMigrationinChance.anchor = GridBagConstraints.EAST;
		gbc_lblMigrationinChance.insets = new Insets(0, 0, 5, 5);
		gbc_lblMigrationinChance.gridx = 0;
		gbc_lblMigrationinChance.gridy = 13;
		add(lblMigrationinChance, gbc_lblMigrationinChance);
		
		migInSpinner = new JSpinner();
		migInSpinner.setToolTipText("Daily chance of insects migrating into the field from outside.");
		migInSpinner.setModel(new SpinnerNumberModel(0.005, 0.0, 1.0, 0.001));
		GridBagConstraints gbc_spinnerMigIn = new GridBagConstraints();
		gbc_spinnerMigIn.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerMigIn.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerMigIn.gridx = 1;
		gbc_spinnerMigIn.gridy = 13;
		add(migInSpinner, gbc_spinnerMigIn);
		
		JLabel lblPlantPreference = new JLabel("Eggs Laid on Plants");
		GridBagConstraints gbc_lblPlantPreference = new GridBagConstraints();
		gbc_lblPlantPreference.anchor = GridBagConstraints.EAST;
		gbc_lblPlantPreference.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlantPreference.gridx = 0;
		gbc_lblPlantPreference.gridy = 14;
		add(lblPlantPreference, gbc_lblPlantPreference);
		
		eggCountTable = new JTable();
		eggCountTable.setToolTipText("Number of eggs laid on each plant species -a higher number indicates a more preferred plant species.");
		eggCountTable.setModel(new DefaultTableModel() {
			boolean[] columnEditables = new boolean[] {
				false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		GridBagConstraints gbc_eggCountTable = new GridBagConstraints();
		gbc_eggCountTable.insets = new Insets(0, 0, 5, 0);
		gbc_eggCountTable.gridwidth = 2;
		gbc_eggCountTable.fill = GridBagConstraints.HORIZONTAL;
		gbc_eggCountTable.gridx = 1;
		gbc_eggCountTable.gridy = 14;
		add(eggCountTable, gbc_eggCountTable);
		
		saveButton = new JButton("Save Insect");
		GridBagConstraints gbc_saveButton = new GridBagConstraints();
		gbc_saveButton.insets = new Insets(0, 0, 5, 5);
		gbc_saveButton.gridx = 1;
		gbc_saveButton.gridy = 15;
		add(saveButton, gbc_saveButton);
		saveButton.addActionListener(this);
		
		setParams(params);
	}

	public InsectParams getParams() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(int i = 0; i < eggCountTable.getModel().getRowCount(); i++){
			String plant = (String) eggCountTable.getModel().getValueAt(i, 0);
			int eggCount = Integer.parseInt(eggCountTable.getModel().getValueAt(i, 1).toString());
			map.put(plant, eggCount);
		}
		
		params = new InsectParams(insectIdEdit.getText(), (int)initialCountSpinner.getValue(), (int)maxEggsSpinner.getValue(),
				(double)tradeOffSpinner.getValue(), (int)toleranceSpinner.getValue(), (double)mortalitySpinner.getValue(),
				(int)maxFlightSpinner.getValue(), (int)eggHatchTimeSpinner.getValue(), (InsectSensoryMode)sensoryModeCombo.getSelectedItem(),
				(InsectInitialDistribution)distribCombo.getSelectedItem(), (int)memSizeSpinner.getValue(), (double)migOutSpinner.getValue(),
				(double)migInSpinner.getValue(), map, colourLabel.getBackground());
		
		return params;
	}

	public void setParams(InsectParams params) {
		this.params = params;
		
		insectIdEdit.setText(params.getInsectId());
		maxEggsSpinner.setValue(params.getMaxEggs());
		initialCountSpinner.setValue(params.getInitialCount());
		tradeOffSpinner.setValue(params.getTradeOff());
		toleranceSpinner.setValue(params.getTolerance());
		mortalitySpinner.setValue(params.getMortalityRate());
		maxFlightSpinner.setValue(params.getMaxFlightLength());
		eggHatchTimeSpinner.setValue(params.getEggHatchTime());
		sensoryModeCombo.setSelectedItem(params.getSensoryMode());
		distribCombo.setSelectedItem(params.getInitialDist());
		memSizeSpinner.setValue(params.getMemorySize());
		migOutSpinner.setValue(params.getMigrationOutRate());
		migInSpinner.setValue(params.getMigrationInRate());
		colourLabel.setBackground(params.getDisplayCol());
		
		Object[][] tableData = new Object[plantParams.size()][2];
		int index = 0;
		
		for(PlantParams pp: plantParams) {			
			tableData[index][0] = pp.getPlantId();
			Integer eggNum = params.getEggsPerPlant().get(pp.getPlantId());
			if(eggNum == null) {
				eggNum = 1;
			}
		    tableData[index][1] = eggNum;
		    index++;
		}

		String[] colNames = new String[] { "Plant Name", "Egg Count" };
		
		DefaultTableModel model = (DefaultTableModel) eggCountTable.getModel();
		model.setDataVector(tableData, colNames);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == setColourButton) {
			Color newCol = JColorChooser.showDialog(this, "Choose Display Color", colourLabel.getBackground());
			if(newCol != null)
				colourLabel.setBackground(newCol);
		} else if(arg0.getSource() == saveButton) {
			serializationHelper.saveInsect(getParams());
		} else if(arg0.getSource() == loadButton || arg0.getSource() == insectIdEdit) {
			InsectParams ip = serializationHelper.loadInsect(insectIdEdit.getText());
			if(ip == null) {
				JOptionPane.showMessageDialog(this, "Could not find a saved insect with that Id.");
			} else {
				setParams(ip);
			}
		}
		
	}
}
