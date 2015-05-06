package plantsInsects.ui;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.CardLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.border.EmptyBorder;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;

import plantsInsects.EnvironmentParams;
import plantsInsects.InsectParams;
import plantsInsects.PlantParams;
import plantsInsects.enums.PlantSpacialDistribution;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;

public class ModelUserPanel extends JPanel implements ActionListener{
	
	private static ModelUserPanel userPanel = null;
	
	public static ModelUserPanel getInstance(){
		if(userPanel == null)
			userPanel = new ModelUserPanel();
		
		return userPanel;
	}
	
	public static boolean hasInstance() {
		return userPanel != null;
	}
	
	private JPanel cardPanel;
	private JPanel envPanel;
	private JButton nextButton;
	private JLabel hintLabel;
	private JLabel stepLabel;
	private int step = 1;
	
	private JSpinner gridSizeSpinner;
	private JSpinner plantCountSpinner;
	private JComboBox plantDistCombo;
	private JSpinner insectCountSpinner;
	private JSpinner yearCountSpinner;
	
	private ArrayList<PlantParamsPanel> plantPanels;
	private ArrayList<InsectParamsPanel> insectPanels;
	private EnvironmentParams envParams;
	private JPanel buttonsPanel;
	private JButton prevButton;
	private JLabel lblPauseAtEach;
	private JCheckBox pauseCheck;
	
	private ModelUserPanel() {
		plantPanels = new ArrayList<PlantParamsPanel>();
		insectPanels = new ArrayList<InsectParamsPanel>();
		envParams = new EnvironmentParams();
		
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		cardPanel = new JPanel();
		cardPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
		cardPanel.setLayout(new CardLayout(0, 0));
		
		envPanel = new JPanel();
		cardPanel.add(envPanel, "name_1128622329011721");
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		envPanel.setLayout(gbl_panel_1);
		
		JLabel lblGridSize = new JLabel("Grid Size");
		GridBagConstraints gbc_lblGridSize = new GridBagConstraints();
		gbc_lblGridSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblGridSize.anchor = GridBagConstraints.EAST;
		gbc_lblGridSize.gridx = 0;
		gbc_lblGridSize.gridy = 0;
		envPanel.add(lblGridSize, gbc_lblGridSize);
		
		gridSizeSpinner = new JSpinner();
		gridSizeSpinner.setToolTipText("Width and length of the plant field - it will always be square.");
		gridSizeSpinner.setModel(new SpinnerNumberModel(new Integer(100), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_spinnerGridSize = new GridBagConstraints();
		gbc_spinnerGridSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerGridSize.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerGridSize.gridx = 1;
		gbc_spinnerGridSize.gridy = 0;
		envPanel.add(gridSizeSpinner, gbc_spinnerGridSize);
		
		JLabel lblPlantCount = new JLabel("Plant Species Count");
		GridBagConstraints gbc_lblPlantCount = new GridBagConstraints();
		gbc_lblPlantCount.anchor = GridBagConstraints.EAST;
		gbc_lblPlantCount.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlantCount.gridx = 0;
		gbc_lblPlantCount.gridy = 1;
		envPanel.add(lblPlantCount, gbc_lblPlantCount);
		
		plantCountSpinner = new JSpinner();
		plantCountSpinner.setToolTipText("Number of plant species for this simulation run.");
		plantCountSpinner.setModel(new SpinnerNumberModel(2, 0, 10, 1));
		GridBagConstraints gbc_spinnerPlantNum = new GridBagConstraints();
		gbc_spinnerPlantNum.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPlantNum.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerPlantNum.gridx = 1;
		gbc_spinnerPlantNum.gridy = 1;
		envPanel.add(plantCountSpinner, gbc_spinnerPlantNum);
		
		JLabel lblPlantSpatialDistribution = new JLabel("Plant Spatial Distribution");
		GridBagConstraints gbc_lblPlantSpacialDistribution = new GridBagConstraints();
		gbc_lblPlantSpacialDistribution.anchor = GridBagConstraints.EAST;
		gbc_lblPlantSpacialDistribution.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlantSpacialDistribution.gridx = 0;
		gbc_lblPlantSpacialDistribution.gridy = 2;
		envPanel.add(lblPlantSpatialDistribution, gbc_lblPlantSpacialDistribution);
		
		plantDistCombo = new JComboBox(PlantSpacialDistribution.values());
		plantDistCombo.setToolTipText("The pattern plants are aranged in.");
		GridBagConstraints gbc_plantDistCombo = new GridBagConstraints();
		gbc_plantDistCombo.insets = new Insets(0, 0, 5, 0);
		gbc_plantDistCombo.fill = GridBagConstraints.HORIZONTAL;
		gbc_plantDistCombo.gridx = 1;
		gbc_plantDistCombo.gridy = 2;
		envPanel.add(plantDistCombo, gbc_plantDistCombo);
		
		JLabel lblInsectSpeciesCount = new JLabel("Insect Species Count");
		GridBagConstraints gbc_lblInsectSpeciesCount = new GridBagConstraints();
		gbc_lblInsectSpeciesCount.anchor = GridBagConstraints.EAST;
		gbc_lblInsectSpeciesCount.insets = new Insets(0, 0, 5, 5);
		gbc_lblInsectSpeciesCount.gridx = 0;
		gbc_lblInsectSpeciesCount.gridy = 3;
		envPanel.add(lblInsectSpeciesCount, gbc_lblInsectSpeciesCount);
		
		insectCountSpinner = new JSpinner();
		insectCountSpinner.setToolTipText("Number of insect species for this simulation run.");
		insectCountSpinner.setModel(new SpinnerNumberModel(2, 0, 10, 1));
		GridBagConstraints gbc_spinnerInsectNum = new GridBagConstraints();
		gbc_spinnerInsectNum.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerInsectNum.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerInsectNum.gridx = 1;
		gbc_spinnerInsectNum.gridy = 3;
		envPanel.add(insectCountSpinner, gbc_spinnerInsectNum);
		
		JLabel lblSimulationRunin = new JLabel("Simulation Run (in years)");
		GridBagConstraints gbc_lblSimulationRunin = new GridBagConstraints();
		gbc_lblSimulationRunin.anchor = GridBagConstraints.EAST;
		gbc_lblSimulationRunin.insets = new Insets(0, 0, 5, 5);
		gbc_lblSimulationRunin.gridx = 0;
		gbc_lblSimulationRunin.gridy = 4;
		envPanel.add(lblSimulationRunin, gbc_lblSimulationRunin);
		
		yearCountSpinner = new JSpinner();
		yearCountSpinner.setToolTipText("How long the simulation will run. The simulation can also be stop at any time from the Stop button in the toolbar.");
		yearCountSpinner.setModel(new SpinnerNumberModel(new Integer(50), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_spinnerYearsNum = new GridBagConstraints();
		gbc_spinnerYearsNum.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerYearsNum.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerYearsNum.gridx = 1;
		gbc_spinnerYearsNum.gridy = 4;
		envPanel.add(yearCountSpinner, gbc_spinnerYearsNum);
		
		lblPauseAtEach = new JLabel("Pause at each year");
		GridBagConstraints gbc_lblPauseAtEach = new GridBagConstraints();
		gbc_lblPauseAtEach.insets = new Insets(0, 0, 0, 5);
		gbc_lblPauseAtEach.gridx = 0;
		gbc_lblPauseAtEach.gridy = 5;
		envPanel.add(lblPauseAtEach, gbc_lblPauseAtEach);
		
		pauseCheck = new JCheckBox("");
		GridBagConstraints gbc_pauseCheck = new GridBagConstraints();
		gbc_pauseCheck.anchor = GridBagConstraints.WEST;
		gbc_pauseCheck.gridx = 1;
		gbc_pauseCheck.gridy = 5;
		envPanel.add(pauseCheck, gbc_pauseCheck);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		stepLabel = new JLabel("Step: 1");
		stepLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		stepLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(stepLabel);
		add(cardPanel);
		
		hintLabel = new JLabel("Initialize model when done");
		hintLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		hintLabel.setVisible(false);
		add(hintLabel);
		
		buttonsPanel = new JPanel();
		add(buttonsPanel);
		buttonsPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		prevButton = new JButton("Previous");
		prevButton.setEnabled(false);
		prevButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonsPanel.add(prevButton);
		prevButton.addActionListener(this);
		
		nextButton = new JButton("Next");
		buttonsPanel.add(nextButton);
		nextButton.addActionListener(this);
		nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		setEnvironmentParams(envParams);
	}
	
	public void reset() {
		CardLayout cl = (CardLayout)(cardPanel.getLayout());
		cl.first(cardPanel);
		
		for(PlantParamsPanel ppp: plantPanels) {
			cardPanel.remove(ppp);
		}
		for(InsectParamsPanel ipp: insectPanels) {
			cardPanel.remove(ipp);
		}
		plantPanels.clear();
		insectPanels.clear();
		cl.invalidateLayout(cardPanel);
		
		step = 1;
		stepLabel.setText("Step 1");
		hintLabel.setVisible(false);
		nextButton.setEnabled(true);
		prevButton.setEnabled(false);
	}
	
	private void recreateInsectPanels() {
		CardLayout cl = (CardLayout)(cardPanel.getLayout());
		for(InsectParamsPanel panel: insectPanels){
			cl.removeLayoutComponent(panel);
		}
		
		insectPanels.clear();
		
		for(int j = 0; j < (int) insectCountSpinner.getValue(); j++) {
			InsectParamsPanel ipp = new InsectParamsPanel(getPlantParams());
			insectPanels.add(ipp);
			cardPanel.add(ipp, "Insect" + j);
		}
	}
	
	public EnvironmentParams getEnvironmentParams() {
		envParams = new EnvironmentParams((int)gridSizeSpinner.getValue(), (int)yearCountSpinner.getValue(),
				(int)insectCountSpinner.getValue(), (int)plantCountSpinner.getValue(), 
				(PlantSpacialDistribution)plantDistCombo.getSelectedItem(), pauseCheck.isSelected());
		
		return envParams;
	}
	
	public void setEnvironmentParams(EnvironmentParams env) {
		envParams = env;
		
		gridSizeSpinner.setValue(env.getGridSize());
		plantCountSpinner.setValue(env.getNumPlants());
		plantDistCombo.setSelectedItem(env.getDistribution());
		insectCountSpinner.setValue(env.getNumInsects());
		yearCountSpinner.setValue(env.getNumYears());
	}
	
	public ArrayList<PlantParams> getPlantParams() {
		ArrayList<PlantParams> result = new ArrayList<PlantParams>();
		
		for(PlantParamsPanel ppp: plantPanels){
			result.add(ppp.getParams());
		}
		
		return result;
	}
	
	public ArrayList<InsectParams> getInsectParams() {
		ArrayList<InsectParams> result = new ArrayList<InsectParams>();
		
		for(InsectParamsPanel ipp: insectPanels){
			result.add(ipp.getParams());
		}
		
		return result;
	}
	
	private void nextClicked() {
		CardLayout cl = (CardLayout)(cardPanel.getLayout());
		
		// If on environment panel add plant panels
		if(envPanel.isVisible()){
			for(PlantParamsPanel panel: plantPanels){
				cl.removeLayoutComponent(panel);
			}
			
			plantPanels.clear();
						
			for(int i = 0; i < (int) plantCountSpinner.getValue(); i++) {
				PlantParamsPanel ppp = new PlantParamsPanel();
				plantPanels.add(ppp);
				cardPanel.add(ppp, "Plant" + i);
			}
		}
		
		for(int i = 0; i < plantPanels.size(); i++) {
			if(plantPanels.get(i).isVisible()) {
				// If on last plant panel add insect panels
				if(i == plantPanels.size() - 1) {
					for(InsectParamsPanel panel: insectPanels){
						cardPanel.remove(panel);
					}
					
					insectPanels.clear();
					
					for(int j = 0; j < (int) insectCountSpinner.getValue(); j++) {
						InsectParamsPanel ipp = new InsectParamsPanel(getPlantParams());
						insectPanels.add(ipp);
						cardPanel.add(ipp, "Insect" + j);
					}
				} else {
					double percLeft = plantPanels.get(i).getPercentageLeft();
					int plantsLeft = plantPanels.size() - i - 1;
					double preffered = percLeft / plantsLeft;
					plantPanels.get(i + 1).updatePercentage(percLeft, preffered);
				}
			}
		}
		
	    cl.next(cardPanel);
	    step++;
	    stepLabel.setText("Step: " + step + " of " + (1 + (int) plantCountSpinner.getValue() +(int) insectCountSpinner.getValue()));
	    prevButton.setEnabled(true);
	    
	    // If on last insect panel remove Next button
	    if(!insectPanels.isEmpty() && insectPanels.get(insectPanels.size() - 1).isVisible()) {
	    	nextButton.setEnabled(false);
	    	hintLabel.setVisible(true);
		}
	}
	
	private void prevClicked() {
		CardLayout cl = (CardLayout)(cardPanel.getLayout());
		cl.previous(cardPanel);
	    step--;
	    stepLabel.setText("Step: " + step + " of " + (1 + (int) plantCountSpinner.getValue() +(int) insectCountSpinner.getValue()));
	    nextButton.setEnabled(true);
	    hintLabel.setVisible(false);
	    
	    // If gone back to last plant panel recreate insect panels
	    if(!insectPanels.isEmpty() && insectPanels.get(insectPanels.size() - 1).isVisible()) {
	    	//recreateInsectPanels();
	    }
	    // If on first panel remove Previous button
	    else if(envPanel.isVisible()) {
	    	reset();
	    	prevButton.setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {		
		if(arg0.getSource() == nextButton) {
			nextClicked();
		} else if(arg0.getSource() == prevButton) {
			prevClicked();
		}
		
	}

}
