package plantsInsects;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import plantsInsects.enums.InsectInitialDistribution;
import plantsInsects.enums.InsectSensoryMode;
import plantsInsects.enums.PlantHeight;
import plantsInsects.enums.PlantSpacialDistribution;
import plantsInsects.ui.ModelUserPanel;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.ParameterSchema;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.Schema;
import repast.simphony.ui.GUIParametersManager;
import repast.simphony.ui.RSApplication;

public class ParameterSerializationHelper {
	
	private static Parameters savedParams = null;
	
	public static Parameters getSavedParams() {
		return savedParams;
	}
	
	private Parameters getGlobalParams() {
		
		if(savedParams != null)
			return savedParams;
		
		ControllerRegistry reg = RSApplication.getRSApplicationInstance().getController().getControllerRegistry();
		GUIParametersManager paramManager = null;
		for (ParameterSetter setter : reg.getParameterSetters()) {
			if (setter instanceof GUIParametersManager) {
				paramManager = (GUIParametersManager)setter;
				break;
			}
		}
		
		return paramManager.getParameters();
	}

	public void savePlant(PlantParams pp) {
		savePlant(pp, getGlobalParams(), true);
	}
	
	public void savePlant(PlantParams pp, Parameters params, boolean saveToFile) {		
		Schema schema = params.getSchema();
		
		String paramsPrefix = "p_" + pp.getPlantId() + "_";
		String colStr = String.format("#%02x%02x%02x", pp.getDisplayCol().getRed(), pp.getDisplayCol().getGreen(), pp.getDisplayCol().getBlue());
		
		if(schema.contains(paramsPrefix + "id")) {
			params.setValue(paramsPrefix + "colour", colStr);
			params.setValue(paramsPrefix + "percentage", pp.getPercentage());
			params.setValue(paramsPrefix + "dmg_threshold", pp.getDamageThreshold());
			params.setValue(paramsPrefix + "reprod_success", pp.getReproductiveSuccess());
			params.setValue(paramsPrefix + "height", pp.getHeight().toString());
			
			ParameterSchema details = schema.getDetails(paramsPrefix + "colour");
			details.setDefaultValue(colStr);
			details = schema.getDetails(paramsPrefix + "percentage");
			details.setDefaultValue(pp.getPercentage());
			details = schema.getDetails(paramsPrefix + "dmg_threshold");
			details.setDefaultValue(pp.getDamageThreshold());
			details = schema.getDetails(paramsPrefix + "reprod_success");
			details.setDefaultValue(pp.getReproductiveSuccess());
			details = schema.getDetails(paramsPrefix + "height");
			details.setDefaultValue(pp.getHeight().toString());
		} else {
			ParametersCreator creator = new ParametersCreator();
			creator.addParameters(params);
			creator.addParameter(paramsPrefix + "id", String.class, pp.getPlantId(), false);
			creator.addParameter(paramsPrefix + "colour", String.class, colStr, false);
			creator.addParameter(paramsPrefix + "percentage", Double.class, pp.getPercentage(), false);
			creator.addParameter(paramsPrefix + "dmg_threshold", Integer.class, pp.getDamageThreshold(), false);
			creator.addParameter(paramsPrefix + "reprod_success", Integer.class, pp.getReproductiveSuccess(), false);
			creator.addParameter(paramsPrefix + "height", String.class, pp.getHeight().toString(), false);
			params = creator.createParameters();
		}
		
		savedParams = params;
		if(saveToFile) {
			RSApplication.getRSApplicationInstance().updateGuiParamsManager(params, null);
			RSApplication.getRSApplicationInstance().saveCurrentParameters();
		}
	}
	
	public PlantParams loadPlant(String id) {
		return loadPlant(id, getGlobalParams());
	}
	
	public PlantParams loadPlant(String id, Parameters params) {
		
		String paramsPrefix = "p_" + id + "_";
		if(!params.getSchema().contains(paramsPrefix + "id")) {
			return null;
		}
		
		//String id = (String) params.getValue(paramsPrefix + "id");
		Color col = Color.decode((String)params.getValue(paramsPrefix + "colour"));
		double perc = (Double) params.getValue(paramsPrefix + "percentage");
		int threshold = (Integer) params.getValue(paramsPrefix + "dmg_threshold");
		int reprod = (Integer) params.getValue(paramsPrefix + "reprod_success");
		PlantHeight height;
		try {          
			height = PlantHeight.valueOf((String) params.getValue(paramsPrefix + "height"));
		} catch (IllegalArgumentException e) {
			height = PlantHeight.Medium;
		}
		
		return new PlantParams(id, perc, threshold, reprod, height, col);
	}
	
	public void saveInsect(InsectParams ip) {
		saveInsect(ip, getGlobalParams(), true);
	}
	
	public void saveInsect(InsectParams ip, Parameters params, boolean saveToFile) {
		Schema schema = params.getSchema();
		
		String paramsPrefix = "i_" + ip.getInsectId() + "_";
		String colStr = String.format("#%02x%02x%02x", ip.getDisplayCol().getRed(), ip.getDisplayCol().getGreen(), ip.getDisplayCol().getBlue());
		String plantMapString = "";
		for (Map.Entry<String, Integer> entry : ip.getEggsPerPlant().entrySet()) {
		    plantMapString += entry.getKey() + "-" + entry.getValue() + ";";
		}
		plantMapString = plantMapString.substring(0, plantMapString.length() - 1);
		
		if(schema.contains(paramsPrefix + "id")) {
			params.setValue(paramsPrefix + "colour", colStr);
			params.setValue(paramsPrefix + "initial_count", ip.getInitialCount());
			params.setValue(paramsPrefix + "max_eggs", ip.getMaxEggs());
			params.setValue(paramsPrefix + "tradeoff", ip.getTradeOff());
			params.setValue(paramsPrefix + "tolerance", ip.getTolerance());
			params.setValue(paramsPrefix + "mortality", ip.getMortalityRate());
			params.setValue(paramsPrefix + "max_flight_len", ip.getMaxFlightLength());
			params.setValue(paramsPrefix + "hatch_time", ip.getEggHatchTime());
			params.setValue(paramsPrefix + "sensory_mode", ip.getSensoryMode().toString());
			params.setValue(paramsPrefix + "initial_dist", ip.getInitialDist().toString());
			params.setValue(paramsPrefix + "memory_size", ip.getMemorySize());
			params.setValue(paramsPrefix + "mig_out", ip.getMigrationOutRate());
			params.setValue(paramsPrefix + "mig_in", ip.getMigrationInRate());
			params.setValue(paramsPrefix + "eggs_on_plants", plantMapString);
			
			ParameterSchema details = schema.getDetails(paramsPrefix + "colour");
			details.setDefaultValue(colStr);
			details = schema.getDetails(paramsPrefix + "initial_count");
			details.setDefaultValue(ip.getInitialCount());
			details = schema.getDetails(paramsPrefix + "max_eggs");
			details.setDefaultValue(ip.getMaxEggs());
			details = schema.getDetails(paramsPrefix + "tradeoff");
			details.setDefaultValue(ip.getTradeOff());
			details = schema.getDetails(paramsPrefix + "tolerance");
			details.setDefaultValue(ip.getTolerance());
			details = schema.getDetails(paramsPrefix + "mortality");
			details.setDefaultValue(ip.getMortalityRate());
			details = schema.getDetails(paramsPrefix + "max_flight_len");
			details.setDefaultValue(ip.getMaxFlightLength());
			details = schema.getDetails(paramsPrefix + "hatch_time");
			details.setDefaultValue(ip.getEggHatchTime());
			details = schema.getDetails(paramsPrefix + "sensory_mode");
			details.setDefaultValue(ip.getSensoryMode().toString());
			details = schema.getDetails(paramsPrefix + "initial_dist");
			details.setDefaultValue(ip.getInitialDist().toString());
			details = schema.getDetails(paramsPrefix + "memory_size");
			details.setDefaultValue(ip.getMemorySize());
			details = schema.getDetails(paramsPrefix + "mig_out");
			details.setDefaultValue(ip.getMigrationOutRate());
			details = schema.getDetails(paramsPrefix + "mig_in");
			details.setDefaultValue(ip.getMigrationInRate());
			details = schema.getDetails(paramsPrefix + "eggs_on_plants");
			details.setDefaultValue(plantMapString);
		} else {
			ParametersCreator creator = new ParametersCreator();
			creator.addParameters(params);
			creator.addParameter(paramsPrefix + "id", String.class, ip.getInsectId(), false);
			creator.addParameter(paramsPrefix + "colour", String.class, colStr, false);
			creator.addParameter(paramsPrefix + "initial_count", Integer.class, ip.getInitialCount(), false);
			creator.addParameter(paramsPrefix + "max_eggs", Integer.class, ip.getMaxEggs(), false);
			creator.addParameter(paramsPrefix + "tradeoff", Double.class, ip.getTradeOff(), false);
			creator.addParameter(paramsPrefix + "tolerance", Integer.class, ip.getTolerance(), false);
			creator.addParameter(paramsPrefix + "mortality", Double.class, ip.getMortalityRate(), false);
			creator.addParameter(paramsPrefix + "max_flight_len", Integer.class, ip.getMaxFlightLength(), false);
			creator.addParameter(paramsPrefix + "hatch_time", Integer.class, ip.getEggHatchTime(), false);
			creator.addParameter(paramsPrefix + "sensory_mode", String.class, ip.getSensoryMode().toString(), false);
			creator.addParameter(paramsPrefix + "initial_dist", String.class, ip.getInitialDist().toString(), false);
			creator.addParameter(paramsPrefix + "memory_size", Integer.class, ip.getMemorySize(), false);
			creator.addParameter(paramsPrefix + "mig_out", Double.class, ip.getMigrationOutRate(), false);
			creator.addParameter(paramsPrefix + "mig_in", Double.class, ip.getMigrationInRate(), false);
			creator.addParameter(paramsPrefix + "eggs_on_plants", String.class, plantMapString, false);
			params = creator.createParameters();
		}

		savedParams = params;
		if(saveToFile) {
			RSApplication.getRSApplicationInstance().updateGuiParamsManager(params, null);
			RSApplication.getRSApplicationInstance().saveCurrentParameters();
		}
	}
	
	public InsectParams loadInsect(String id) {
		return loadInsect(id, getGlobalParams());
	}
	
	public InsectParams loadInsect(String id, Parameters params) {
		
		String paramsPrefix = "i_" + id + "_";
		if(!params.getSchema().contains(paramsPrefix + "id")) {
			return null;
		}
		
		//String id = (String) params.getValue(paramsPrefix + "id");
		Color col = Color.decode((String)params.getValue(paramsPrefix + "colour"));
		int initialCount = (Integer) params.getValue(paramsPrefix + "initial_count");
		int maxEggs = (Integer) params.getValue(paramsPrefix + "max_eggs");
		double tradeoff = (Double) params.getValue(paramsPrefix + "tradeoff");
		int tolerance = (Integer) params.getValue(paramsPrefix + "tolerance");
		double mortality = (Double) params.getValue(paramsPrefix + "mortality");
		int maxFlightLen = (Integer) params.getValue(paramsPrefix + "max_flight_len");
		int hatchTime = (Integer) params.getValue(paramsPrefix + "hatch_time");
		InsectSensoryMode sensoryMode;
		try {          
			sensoryMode = InsectSensoryMode.valueOf((String) params.getValue(paramsPrefix + "sensory_mode"));
		} catch (IllegalArgumentException e) {
		    sensoryMode = InsectSensoryMode.Visual;
		}
		InsectInitialDistribution initialDist;
		try {          
			initialDist = InsectInitialDistribution.valueOf((String) params.getValue(paramsPrefix + "initial_dist"));
		} catch (IllegalArgumentException e) {
		    initialDist = InsectInitialDistribution.Random;
		}
		int memSize = (Integer) params.getValue(paramsPrefix + "memory_size");
		double migOut = (Double) params.getValue(paramsPrefix + "mig_out");
		double migIn = (Double) params.getValue(paramsPrefix + "mig_in");
		String plantMapString = (String) params.getValue(paramsPrefix + "eggs_on_plants");
		
		HashMap<String, Integer> plantMap = new HashMap<String, Integer>();
		for(String pair: plantMapString.split(";")) {
			String[] parts = pair.split("-");
			if(parts.length > 1) {
				plantMap.put(parts[0], Integer.parseInt(parts[1]));
			}
		}
		
		InsectParams result = new InsectParams(id, initialCount, maxEggs, tradeoff, tolerance, mortality, maxFlightLen, hatchTime, sensoryMode, initialDist, memSize, migOut, migIn, plantMap, col);
		return result;
	}

	public EnvironmentParams getEnvironmentParams() {
		if(ModelUserPanel.hasInstance()) {
			return ModelUserPanel.getInstance().getEnvironmentParams();
		} else {
			if(RunEnvironment.getInstance() == null) {
				return null;
			} else {
				Parameters params = RunEnvironment.getInstance().getParameters();
				int gridSize = (Integer) params.getValue("gridSize");
				int plantCount = getPlantIds().size();
				int insCount = getInsectIds().size();
				PlantSpacialDistribution dist;
				try {          
					dist = PlantSpacialDistribution.valueOf((String) params.getValue("plantDistribution"));
				} catch (IllegalArgumentException e) {
					dist = PlantSpacialDistribution.Random;
				}
				int simRun = (Integer) params.getValue("simRun");
				
				return new EnvironmentParams(gridSize, simRun, insCount, plantCount, dist, false);
			}
		}
	}
	
	public ArrayList<PlantParams> getPlantParams() {
		if(ModelUserPanel.hasInstance()) {
			return ModelUserPanel.getInstance().getPlantParams();
		} else {
			if(RunEnvironment.getInstance() == null) {
				return null;
			} else {
				ArrayList<PlantParams> result = new ArrayList<PlantParams>();
				for(String id: getPlantIds()) {
					result.add(loadPlant(id, RunEnvironment.getInstance().getParameters()));
				}
				return result;
			}
		}
	}
	
	public ArrayList<InsectParams> getInsectParams() {
		if(ModelUserPanel.hasInstance()) {
			return ModelUserPanel.getInstance().getInsectParams();
		} else {
			if(RunEnvironment.getInstance() == null) {
				return null;
			} else {
				ArrayList<InsectParams> result = new ArrayList<InsectParams>();
				for(String id: getInsectIds()) {
					result.add(loadInsect(id, RunEnvironment.getInstance().getParameters()));
				}
				return result;
			}
		}
	}
	
	private ArrayList<String> getPlantIds() {
		if(RunEnvironment.getInstance() != null) {
			Parameters params = RunEnvironment.getInstance().getParameters();
			String idsStr = (String) params.getValue("plantIds");
			String[] split = idsStr.split(";");
			return new ArrayList<String>(Arrays.asList(split));
		}
		
		return new ArrayList<String>();
	}
	
	private ArrayList<String> getInsectIds() {
		if(RunEnvironment.getInstance() != null) {
			Parameters params = RunEnvironment.getInstance().getParameters();
			String idsStr = (String) params.getValue("insectIds");
			String[] split = idsStr.split(";");
			return new ArrayList<String>(Arrays.asList(split));
		}
		
		return new ArrayList<String>();
	}
}
