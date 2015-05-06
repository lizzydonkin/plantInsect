package plantsInsects;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import plantsInsects.enums.InsectInitialDistribution;
import plantsInsects.enums.InsectSensoryMode;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class InsectParams {

	private String insectId;
	private int initialCount;
	private int maxEggs;
	private double tradeOff;
	private int tolerance;
	private double mortalityRate;
	private int maxFlightLength;
	private int eggHatchTime;
	private InsectSensoryMode sensoryMode;
	private InsectInitialDistribution initialDist;
	private int memorySize;
	private double migrationOutRate;
	private double migrationInRate;
	private HashMap<String, Integer> eggsPerPlant;
	private Color displayCol;
	private ArrayList<String> preferredPlantIds;
	private int count;
	
	private int prevCount;
	
	public InsectParams(String insectId, int initialCount, int maxEggs,
			double tradeOff, int tolerance, double mortalityRate,
			int maxFlightLength, int eggHatchTime,
			InsectSensoryMode sensoryMode,
			InsectInitialDistribution initialDist, int memorySize,
			double migrationOutRate, double migrationInRate,
			HashMap<String, Integer> eggsPerPlant, Color col) {
		
		this.insectId = insectId;
		this.initialCount = initialCount;
		this.maxEggs = maxEggs;
		this.tradeOff = tradeOff;
		this.tolerance = tolerance;
		this.mortalityRate = mortalityRate;
		this.maxFlightLength = maxFlightLength;
		this.eggHatchTime = eggHatchTime;
		this.sensoryMode = sensoryMode;
		this.initialDist = initialDist;
		this.memorySize = memorySize;
		this.migrationOutRate = migrationOutRate;
		this.migrationInRate = migrationInRate;
		this.eggsPerPlant = eggsPerPlant;
		this.displayCol = col;
		this.preferredPlantIds = new ArrayList<String>();
		calculateMostPreferred();
		this.count = initialCount;
		prevCount = count;
	}
	
	public InsectParams(ArrayList<PlantParams> plantParams) {

		this.insectId = "";
		this.initialCount = 20;
		this.maxEggs = 5;
		this.tradeOff = 0.5;
		this.tolerance = 0;
		this.mortalityRate = 0.001;
		this.maxFlightLength = 5;
		this.eggHatchTime = 1;
		this.sensoryMode = InsectSensoryMode.Visual;
		this.initialDist = InsectInitialDistribution.Random;
		this.memorySize = 5;
		this.migrationOutRate = 0.001;
		this.migrationInRate = 0.005;
		this.eggsPerPlant = new HashMap<String, Integer>();
		for(PlantParams pp: plantParams){
			eggsPerPlant.put(pp.getPlantId(), 2);
		}
		this.displayCol = Color.RED;
		this.preferredPlantIds = new ArrayList<String>();
		calculateMostPreferred();
		this.count = initialCount;
		prevCount = count;
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 1.5)
	public void afterDeaths() {
		if(!SeasonHelper.isWinterSeason()) {
			int diff = prevCount - count;
			System.out.println(RunEnvironment.getInstance().getCurrentSchedule().getTickCount() + ": " + insectId + ": " + diff + " have died");
			prevCount = count;
		}
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 0.5)
	public void afterBirths() {
		if(!SeasonHelper.isWinterSeason()) {
			int diff = count - prevCount;
			System.out.println(RunEnvironment.getInstance().getCurrentSchedule().getTickCount() + ": " + insectId + ": " + diff + " have been hatched");
			prevCount = count;
		}
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 0)
	public void migrationIn() {
		if(SeasonHelper.isWinterSeason())
			return;
		
		if(RandomHelper.nextDoubleFromTo(0, 1) < migrationInRate) {
			Context<Object> ctx = ContextUtils.getContext(this);
			Grid<Object> grid = (Grid<Object>) ctx.getProjection("grid");
			int migInCount = RandomHelper.nextIntFromTo(initialCount / 2, initialCount);
			int direction = RandomHelper.nextIntFromTo(1, 4);
			int gridSize = grid.getDimensions().getWidth();
			
			for(int i = 0; i < migInCount; i++) {
				int eggCount = RandomHelper.nextIntFromTo(1, maxEggs);
				Insect ins = new Insect(this, eggCount, ctx);
				ctx.add(ins);
				
				int x, y;
				switch(direction) {
					case 1:
						x = 0;
						y = RandomHelper.nextIntFromTo(0, gridSize - 1);
						break;
					case 2:
						x = gridSize - 1;
						y = RandomHelper.nextIntFromTo(0, gridSize - 1);
						break;
					case 3:
						x = RandomHelper.nextIntFromTo(0, gridSize - 1);
						y = 0;
						break;
					case 4:
					default:
						x = RandomHelper.nextIntFromTo(0, gridSize - 1);;
						y = gridSize - 1;
						break;
				}
				grid.moveTo(ins, x, y);
			}
			
			count += migInCount;
		}
	}

	public String getInsectId() {
		return insectId;
	}

	public void setInsectId(String insectId) {
		this.insectId = insectId;
	}

	public int getInitialCount() {
		return initialCount;
	}

	public void setInitialCount(int initialCount) {
		this.initialCount = initialCount;
	}

	public int getMaxEggs() {
		return maxEggs;
	}

	public void setMaxEggs(int maxEggs) {
		this.maxEggs = maxEggs;
	}

	public double getTradeOff() {
		return tradeOff;
	}

	public void setTradeOff(double tradeOff) {
		this.tradeOff = tradeOff;
	}

	public int getTolerance() {
		return tolerance;
	}

	public void setTolerance(int tolerance) {
		this.tolerance = tolerance;
	}

	public double getMortalityRate() {
		return mortalityRate;
	}

	public void setMortalityRate(double mortalityRate) {
		this.mortalityRate = mortalityRate;
	}

	public int getMaxFlightLength() {
		return maxFlightLength;
	}

	public void setMaxFlightLength(int maxFlightLength) {
		this.maxFlightLength = maxFlightLength;
	}

	public int getEggHatchTime() {
		return eggHatchTime;
	}

	public void setEggHatchTime(int eggHatchTime) {
		this.eggHatchTime = eggHatchTime;
	}

	public InsectSensoryMode getSensoryMode() {
		return sensoryMode;
	}

	public void setSensoryMode(InsectSensoryMode sensoryMode) {
		this.sensoryMode = sensoryMode;
	}

	public InsectInitialDistribution getInitialDist() {
		return initialDist;
	}

	public void setInitialDist(InsectInitialDistribution initialDist) {
		this.initialDist = initialDist;
	}

	public int getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}

	public double getMigrationOutRate() {
		return migrationOutRate;
	}

	public void setMigrationOutRate(double migrationOutRate) {
		this.migrationOutRate = migrationOutRate;
	}

	public double getMigrationInRate() {
		return migrationInRate;
	}

	public void setMigrationInRate(double migrationInRate) {
		this.migrationInRate = migrationInRate;
	}

	public HashMap<String, Integer> getEggsPerPlant() {
		return eggsPerPlant;
	}

	public void setEggsPerPlant(HashMap<String, Integer> eggsPerPlant) {
		this.eggsPerPlant = eggsPerPlant;
	}

	public Color getDisplayCol() {
		return displayCol;
	}

	public void setDisplayCol(Color displayCol) {
		this.displayCol = displayCol;
	}

	public ArrayList<String> getPreferredPlantIds() {
		return preferredPlantIds;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int c) {
		count = c;
	}
	
	public void incrementCount() {
		count++;
	}
	
	public void decrementCount() {
		count--;
	}
	
	private void calculateMostPreferred() {
		int maxSoFar = 0;
		for(String key: eggsPerPlant.keySet()) {
			if(eggsPerPlant.get(key) > maxSoFar) {
				maxSoFar = eggsPerPlant.get(key);
				preferredPlantIds.clear();
				preferredPlantIds.add(key);
			}
			else if(eggsPerPlant.get(key) == maxSoFar) {
				preferredPlantIds.add(key);
			}
		}
	}
	
}
