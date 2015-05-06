package plantsInsects;

import plantsInsects.enums.PlantSpacialDistribution;

public class EnvironmentParams {
	
	private int gridSize;
	private int numInsects;
	private int numPlants;
	private int numYears;
	private PlantSpacialDistribution distribution;
	private boolean pause;
	
	public EnvironmentParams(){
		this.gridSize = 25;
		this.numYears = 10;
		this.numInsects = 2;
		this.numPlants = 2;
		this.distribution = PlantSpacialDistribution.Random;
		this.pause = false;
	}
	
	public EnvironmentParams(int gridSize, int numYears, int numInsects, int numPlants, PlantSpacialDistribution dist, boolean pause){
		this.gridSize = gridSize;
		this.numYears = numYears;
		this.numInsects = numInsects;
		this.numPlants = numPlants;
		this.distribution = dist;
		this.pause = pause;
	}
	
	public int getGridSize() {
		return gridSize;
	}
	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}
	public int getNumInsects() {
		return numInsects;
	}
	public void setNumInsects(int numInsects) {
		this.numInsects = numInsects;
	}
	public int getNumPlants() {
		return numPlants;
	}
	public void setNumPlants(int numPlants) {
		this.numPlants = numPlants;
	}
	public int getNumYears() {
		return numYears;
	}
	public void setNumYears(int numYears) {
		this.numYears = numYears;
	}
	public PlantSpacialDistribution getDistribution() {
		return distribution;
	}
	public void setDistribution(PlantSpacialDistribution distribution) {
		this.distribution = distribution;
	}

	public boolean getPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

}
