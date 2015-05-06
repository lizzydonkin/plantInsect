import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import plantsInsects.Insect;
import plantsInsects.InsectParams;
import plantsInsects.ParameterSerializationHelper;
import plantsInsects.Plant;
import plantsInsects.PlantInsectBuilder;
import plantsInsects.PlantParams;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.scenario.ScenarioUtils;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;


public class ContextBuilderTest {
	
	private Context context;
	private Grid<Object> grid;
	private Parameters params;

	@Before
	public void setUp() throws Exception {
		String scenarioDirString = "PlantsInsects.rs";
		ScenarioUtils.setScenarioDir(new File(scenarioDirString));
		File paramsFile = new File(ScenarioUtils.getScenarioDir(), "parameters.xml");
		ParametersParser pp = new ParametersParser(paramsFile);
		params = pp.getParameters();
		RunEnvironment.init(new Schedule(), null, params, true);
	}
	
	private void createPlantsAndInsectsSpecies(int plantCount, int insectCount) {
		ParameterSerializationHelper helper = new ParameterSerializationHelper();
		ArrayList<PlantParams> pp = new ArrayList<PlantParams>();
		
		for(int i = 1; i <= plantCount; i++) {
			PlantParams plant = new PlantParams();
			plant.setPlantId("plant" + i);
			helper.savePlant(plant, params, false);
			pp.add(plant);
			params = ParameterSerializationHelper.getSavedParams();
		}
		
		for(int i = 1; i <= insectCount; i++) {
			InsectParams ins = new InsectParams(pp);
			ins.setInsectId("insect" + i);
			helper.saveInsect(ins, params, false);
			params = ParameterSerializationHelper.getSavedParams();
		}
	}

	private void createContext() {
		context = new DefaultContext();
		PlantInsectBuilder builder = new PlantInsectBuilder();
		context = builder.build(context);
		grid = (Grid<Object>) context.getProjection("grid");
		RunState.init().setMasterContext(context);
	}
	
	private boolean leftRowHasOnly1PlantType() {
		int gridSize = params.getInteger("gridSize");
		boolean allSameLeft = true;
		String prevLeftId = "";
		for(int i = 0; i < gridSize; i++) {
			Plant p = getPlantAt(0, i);
			if(!prevLeftId.isEmpty()) {
				if(!prevLeftId.equals(p.getSpeciesParams().getPlantId())) {
					allSameLeft = false;
					break;
				}
			}
			prevLeftId = p.getSpeciesParams().getPlantId();
		}
		return allSameLeft;
	}
	
	private boolean bottomRowHasOnly1PlantType() {
		int gridSize = params.getInteger("gridSize");
		boolean allSameBottom = true;
		String prevBotId = "";
		for(int i = 0; i < gridSize; i++) {
			Plant p = getPlantAt(i, 0);
			if(!prevBotId.isEmpty()) {
				if(!prevBotId.equals(p.getSpeciesParams().getPlantId())) {
					allSameBottom = false;
					break;
				}
			}
			prevBotId = p.getSpeciesParams().getPlantId();
		}
		return allSameBottom;
	}
	
	private int getPlantCount(String plantId) {
		int count = 0;
		for(Object o: context.getObjects(Plant.class)) {
			Plant p = (Plant) o;
			if(p.getSpeciesParams().getPlantId().equals(plantId)) {
				count++;
			}
		}
		return count;
	}
	
	private Plant getPlantAt(int x, int y) {
		Plant result = null;
		for(Object o: grid.getObjectsAt(x, y)) {
			if(o.getClass() == Plant.class) {
				result = (Plant) o;
				break;
			}
		}
		
		return result;
	}
	
	@Test
	public void testGridSize() {
		Parameters params = RunEnvironment.getInstance().getParameters();
		
		for(int gridSize = 10; gridSize <= 100; gridSize += 10) {
			params.setValue("gridSize", gridSize);
			RunEnvironment.getInstance().setParameters(params);
			createContext();
			assertEquals("Error: grid height incorrect", gridSize, grid.getDimensions().getHeight());
			assertEquals("Error: grid width incorrect", gridSize, grid.getDimensions().getWidth());
		}
	}

	@Test
	public void testInsectDistributionNorth() {
		createPlantsAndInsectsSpecies(1, 1);
		params.setValue("insectIds", "insect1");
		params.setValue("i_insect1_initial_dist", "North");
		RunEnvironment.getInstance().setParameters(params);
		
		int gridSize = params.getInteger("gridSize");
		int northBorder = gridSize - (gridSize / 20);
		
		createContext();
		
		assertTrue("Error: insects not created", context.getObjects(Insect.class).size() > 0);

		for(Object o: context.getObjects(Insect.class)){
			assertTrue("Error: an insect is too far south", grid.getLocation(o).getY() >= northBorder);
		}
	}
	
	@Test
	public void testInsectDistributionWest() {
		createPlantsAndInsectsSpecies(1, 1);
		params.setValue("insectIds", "insect1");
		params.setValue("i_insect1_initial_dist", "West");
		RunEnvironment.getInstance().setParameters(params);
		
		int gridSize = params.getInteger("gridSize");
		int westBorder = gridSize / 20;
		
		createContext();
		
		assertTrue(context.getObjects(Insect.class).size() > 0);

		for(Object o: context.getObjects(Insect.class)){
			assertTrue("Error: an insect is too far east", grid.getLocation(o).getX() <= westBorder);
		}
	}
	
	@Test
	public void testInsectDistributionNorthWest() {
		createPlantsAndInsectsSpecies(1, 1);
		params.setValue("insectIds", "insect1");
		params.setValue("i_insect1_initial_dist", "NorthWest");
		RunEnvironment.getInstance().setParameters(params);
		
		int gridSize = params.getInteger("gridSize");
		int westBorder = gridSize / 20;
		int northBorder = gridSize - (gridSize / 20);
		
		createContext();
		
		assertTrue(context.getObjects(Insect.class).size() > 0);

		for(Object o: context.getObjects(Insect.class)){
			assertTrue("Error: an insect is too far east", grid.getLocation(o).getX() <= westBorder);
			assertTrue("Error: an insect is too far south", grid.getLocation(o).getY() >= northBorder);
		}
	}

	/* This only tests if insect is within grid bounds. It doesn't test the randomness. */
	@Test
	public void testInsectDistributionRandom() {
		Parameters params = RunEnvironment.getInstance().getParameters();
		// this assumes the 'gen' insect is saved
		params.setValue("insectIds", "gen");
		params.setValue("i_gen_initial_dist", "Random");
		RunEnvironment.getInstance().setParameters(params);
		
		int gridSize = params.getInteger("gridSize");
		int westBorder = gridSize / 20;
		int northBorder = gridSize - (gridSize / 20);
		
		createContext();
		
		assertTrue(context.getObjects(Insect.class).size() > 0);

		for(Object o: context.getObjects(Insect.class)){
			GridPoint p = grid.getLocation(o);
			assertTrue("Error: an insect is outside of bounds", p.getX() < gridSize && p.getX() >= 0);
			assertTrue("Error: an insect is outside of bounds", p.getY() < gridSize && p.getY() >= 0);
		}
	}

	@Test
	public void testPlantDistributionRandom2Plants() {
		createPlantsAndInsectsSpecies(2, 0);
		int gridSize = 100;
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2");
		params.setValue("plantDistribution", "Random");
		RunEnvironment.getInstance().setParameters(params);
		
		createContext();
		
		int p1s = getPlantCount("plant1");
		int p2s = getPlantCount("plant2");		
		boolean allSameBottom = bottomRowHasOnly1PlantType();
		boolean allSameLeft = leftRowHasOnly1PlantType();
		
		assertEquals("Error: total number of plants created is incorrect", context.getObjects(Plant.class).size(), gridSize * gridSize);
		assertEquals("Error: plant count should be equal for the 2 species", p1s, p2s);
		assertFalse("Error: plants should be distributed randomly", allSameBottom);
		assertFalse("Error: plants should be distributed randomly", allSameLeft);
	}

	@Test
	public void testPlantDistributionRandom3Plants() {
		createPlantsAndInsectsSpecies(3, 0);
		int gridSize = 99;
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2;plant3");
		params.setValue("plantDistribution", "Random");
		params.setValue("p_plant1_percentage", 0.33);
		params.setValue("p_plant2_percentage", 0.33);
		params.setValue("p_plant3_percentage", 0.33);
		RunEnvironment.getInstance().setParameters(params);
		
		createContext();
		
		int p1s = getPlantCount("plant1");
		int p2s = getPlantCount("plant2");
		int p3s = getPlantCount("plant3");
		boolean allSameBottom = bottomRowHasOnly1PlantType();
		boolean allSameLeft = leftRowHasOnly1PlantType();
		
		assertEquals("Error: total number of plants created is incorrect", context.getObjects(Plant.class).size(), gridSize * gridSize);
		assertEquals("Error: plant count should be equal for the 3 species", p1s, p2s);
		assertEquals("Error: plant count should be equal for the 3 species", p2s, p3s);
		assertFalse("Error: plants should be distributed randomly", allSameBottom);
		assertFalse("Error: plants should be distributed randomly", allSameLeft);
	}
	
	@Test
	public void testPlantDistributionRows2Plants() {
		createPlantsAndInsectsSpecies(2, 0);
		int gridSize = 100;
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2");
		params.setValue("plantDistribution", "Rows");
		RunEnvironment.getInstance().setParameters(params);
		
		createContext();
		
		int p1s = getPlantCount("plant1");
		int p2s = getPlantCount("plant2");
		boolean allSameBottom = bottomRowHasOnly1PlantType();
		boolean allSameLeft = leftRowHasOnly1PlantType();
		
		assertEquals("Error: total number of plants created is incorrect", context.getObjects(Plant.class).size(), gridSize * gridSize);
		assertEquals("Error: plant count should be equal for the 3 species (within 10% for Rows)", p1s, p2s, (gridSize * gridSize) / 10);
		assertFalse("Error: bottom row should have different plants", allSameBottom);
		assertTrue("Error: left row should have the same plant", allSameLeft);
	}
	
	@Test
	public void testPlantDistributionRows3Plants() {
		createPlantsAndInsectsSpecies(3, 0);
		int gridSize = 99;
		
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2;plant3");
		params.setValue("plantDistribution", "Rows");
		params.setValue("p_plant1_percentage", 0.33);
		params.setValue("p_plant2_percentage", 0.33);
		params.setValue("p_plant3_percentage", 0.33);
		RunEnvironment.getInstance().setParameters(params);
		
		createContext();
		
		int p1s = getPlantCount("plant1");
		int p2s = getPlantCount("plant2");
		int p3s = getPlantCount("plant3");
		boolean allSameBottom = bottomRowHasOnly1PlantType();
		boolean allSameLeft = leftRowHasOnly1PlantType();
		
		assertEquals("Error: total number of plants created is incorrect", context.getObjects(Plant.class).size(), gridSize * gridSize);
		assertEquals("Error: plant count should be equal for the 3 species (within 10% for Rows)", p1s, p2s, (gridSize * gridSize) / 10);
		assertEquals("Error: plant count should be equal for the 3 species (within 10% for Rows)", p2s, p3s, (gridSize * gridSize) / 10);
		assertFalse("Error: bottom row should have different plants", allSameBottom);
		assertTrue("Error: left row should have the same plant", allSameLeft);
	}

	@Test
	public void testPlantDistributionBorders2Plants() {
		createPlantsAndInsectsSpecies(2, 0);
		int gridSize = 100;
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2");
		params.setValue("plantDistribution", "Borders");
		RunEnvironment.getInstance().setParameters(params);
		
		createContext();
		
		int p1s = getPlantCount("plant1");
		int p2s = getPlantCount("plant2");		
		boolean allSameBottom = bottomRowHasOnly1PlantType();
		boolean allSameLeft = leftRowHasOnly1PlantType();
		
		assertEquals("Error: total number of plants created is incorrect", context.getObjects(Plant.class).size(), gridSize * gridSize);
		assertEquals("Error: plant count should be equal for the 3 species (within 10% for Borders)", p1s, p2s, (gridSize * gridSize) / 10);
		assertTrue("Error: bottom row should have the same plant", allSameBottom);
		assertTrue("Error: left row should have the same plant", allSameLeft);
	}

	@Test
	public void testPlantDistributionBorders3Plants() {
		createPlantsAndInsectsSpecies(3, 0);
		int gridSize = 99;
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2;plant3");
		params.setValue("plantDistribution", "Borders");
		params.setValue("p_plant1_percentage", 0.33);
		params.setValue("p_plant2_percentage", 0.33);
		params.setValue("p_plant3_percentage", 0.33);
		RunEnvironment.getInstance().setParameters(params);
		
		createContext();
		
		int p1s = getPlantCount("plant1");
		int p2s = getPlantCount("plant2");
		int p3s = getPlantCount("plant3");		
		boolean allSameBottom = bottomRowHasOnly1PlantType();
		boolean allSameLeft = leftRowHasOnly1PlantType();
		
		assertEquals("Error: total number of plants created is incorrect", context.getObjects(Plant.class).size(), gridSize * gridSize);
		assertEquals("Error: plant count should be equal for the 3 species (within 10% for Borders)", p1s, p2s, (gridSize * gridSize) / 10);
		assertEquals("Error: plant count should be equal for the 3 species (within 10% for Borders)", p2s, p3s, (gridSize * gridSize) / 10);
		assertTrue("Error: bottom row should have the same plant", allSameBottom);
		assertTrue("Error: left row should have the same plant", allSameLeft);
	}
	
	@Test
	public void testPlantDistributionBlocks2Plants() {
		createPlantsAndInsectsSpecies(2, 0);
		int gridSize = 100;
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2");
		params.setValue("plantDistribution", "Blocks");
		RunEnvironment.getInstance().setParameters(params);
		
		createContext();
		
		int p1s = getPlantCount("plant1");
		int p2s = getPlantCount("plant2");		
		boolean allSameBottom = bottomRowHasOnly1PlantType();
		boolean allSameLeft = leftRowHasOnly1PlantType();
		
		assertEquals("Error: total number of plants created is incorrect", context.getObjects(Plant.class).size(), gridSize * gridSize);
		assertEquals("Error: plant count should be equal for the 3 species (within 10% for Blocks)", p1s, p2s, (gridSize * gridSize) / 10);
		assertFalse("Error: bottom row should not have the same plant", allSameBottom);
		assertFalse("Error: left row should not have the same plant", allSameLeft);
	}

	@Test
	public void testPlantDistributionBlocks3Plants() {
		createPlantsAndInsectsSpecies(3, 0);
		int gridSize = 99;
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2;plant3");
		params.setValue("plantDistribution", "Blocks");
		params.setValue("p_plant1_percentage", 0.33);
		params.setValue("p_plant2_percentage", 0.33);
		params.setValue("p_plant3_percentage", 0.33);
		RunEnvironment.getInstance().setParameters(params);
		
		createContext();
		
		int p1s = getPlantCount("plant1");
		int p2s = getPlantCount("plant2");
		int p3s = getPlantCount("plant3");
		boolean allSameBottom = bottomRowHasOnly1PlantType();
		boolean allSameLeft = leftRowHasOnly1PlantType();
		
		assertEquals("Error: total number of plants created is incorrect", context.getObjects(Plant.class).size(), gridSize * gridSize);
		assertEquals("Error: plant count should be equal for the 3 species (within 10% for Blocks)", p1s, p2s, (gridSize * gridSize) / 10);
		assertEquals("Error: plant count should be equal for the 3 species (within 10% for Blocks)", p2s, p3s, (gridSize * gridSize) / 10);
		assertFalse("Error: bottom row should not have the same plant", allSameBottom);
		assertFalse("Error: left row should not have the same plant", allSameLeft);
	}
	
	@Test
	public void testInsectSensoryModeVisual() {
		createPlantsAndInsectsSpecies(2, 1);
		int gridSize = 2;
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2");
		params.setValue("insectIds", "insect1");
		params.setValue("plantDistribution", "Blocks");
		params.setValue("i_insect1_eggs_on_plants", "plant1-1;plant2-2");
		params.setValue("i_insect1_max_eggs", 100);
		params.setValue("i_insect1_initial_count", 1);
		params.setValue("i_insect1_memory_size", 1);
		params.setValue("i_insect1_mortality", 0);
		params.setValue("i_insect1_mig_out", 0);
		RunEnvironment.getInstance().setParameters(params);		
		createContext();
		
		Insect ins = (Insect) context.getObjects(Insect.class).get(0);

		for (int i = 0; i < 10; ++i) {
			ins.step();
			GridPoint newPt = grid.getLocation(ins);
			if(newPt == null) {
				System.out.println("testInsectSensoryModeVisual: Test invalid because insect died / migrated out during test");
				break;
			}
			
			Plant p = getPlantAt(newPt.getX(), newPt.getY());
			assertEquals("Error: insect should only visit preferred plant in Visual mode", p.getSpeciesParams().getPlantId(), "plant2");
		}
	}
	
	@Test
	public void testInsectSensoryModeVisualWithHeight() {
		createPlantsAndInsectsSpecies(2, 1);
		int gridSize = 8;
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2");
		params.setValue("insectIds", "insect1");
		params.setValue("plantDistribution", "Rows");
		params.setValue("i_insect1_eggs_on_plants", "plant1-1;plant2-2");
		params.setValue("i_insect1_initial_count", 1);
		params.setValue("i_insect1_max_flight_len", 8);
		params.setValue("i_insect1_max_eggs", 100);
		params.setValue("i_insect1_memory_size", 20);
		params.setValue("i_insect1_mortality", 0);
		params.setValue("i_insect1_mig_out", 0);
		params.setValue("p_plant1_height", "High");
		params.setValue("p_plant2_height", "Short");
		RunEnvironment.getInstance().setParameters(params);		
		createContext();
		
		Insect ins = (Insect) context.getObjects(Insect.class).get(0);
		
		int maxX = -1, minX = -1;
		for(int i = 0; i < gridSize; i++) {
			if(minX < 0 && getPlantAt(i, 0).getSpeciesParams().getPlantId().equals("plant2")) {
				minX = i;
				continue;
			}
			
			if(minX >= 0 && getPlantAt(i, 0).getSpeciesParams().getPlantId().equals("plant1")) {
				maxX = i - 1;
				break;
			}
		}
		
		grid.moveTo(ins, minX, 0);

		for (int i = 0; i < 16; ++i) {
			ins.step();
			GridPoint newPt = grid.getLocation(ins);
			if(newPt == null) {
				System.out.println("testInsectSensoryModeVisualWithHeight: Test invalid because insect died / migrated out during test");
				break;
			}
			
			Plant p = getPlantAt(newPt.getX(), newPt.getY());
			
			//System.out.println("min: " + minX + " max: " + maxX + " act: " + newPt);
			if(i < 15) {
				assertEquals("Error: insect should only visit preferred plant in Visual mode", p.getSpeciesParams().getPlantId(), "plant2");
				assertTrue("Error: insect shoud not leave the row because it shouldn't see other rows", newPt.getX() >= minX);
				assertTrue("Error: insect shoud not leave the row because it shouldn't see other rows", newPt.getX() <= maxX);
			} else {
				assertEquals("Error: insect should have visited all preferred plants in row at this point, moving to non-preferred because it can't see next preferred row",
						p.getSpeciesParams().getPlantId(), "plant1");
			}
			grid.moveTo(ins, minX, 0);
		}
	}

	@Test
	public void testInsectSensoryModeContact() {
		createPlantsAndInsectsSpecies(2, 1);
		int gridSize = 2;
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2");
		params.setValue("insectIds", "insect1");
		params.setValue("plantDistribution", "Blocks");
		params.setValue("i_insect1_eggs_on_plants", "plant1-1;plant2-2");
		params.setValue("i_insect1_max_eggs", 100);
		params.setValue("i_insect1_initial_count", 1);
		params.setValue("i_insect1_memory_size", 0);
		params.setValue("i_insect1_mortality", 0);
		params.setValue("i_insect1_mig_out", 0);
		params.setValue("i_insect1_sensory_mode", "Contact");
		RunEnvironment.getInstance().setParameters(params);		
		createContext();
		
		Insect ins = (Insect) context.getObjects(Insect.class).get(0);
		int p1Times = 0, p2Times = 0;

		for (int i = 0; i < 10; ++i) {
			ins.step();
			GridPoint newPt = grid.getLocation(ins);
			if(newPt == null) {
				System.out.println("testInsectSensoryModeContact: Test invalid because insect died / migrated out during test");
				break;
			}
			
			Plant p = getPlantAt(newPt.getX(), newPt.getY());
			if(p.getSpeciesParams().getPlantId().equals("plant1"))
				p1Times++;
			else
				p2Times++;
		}
		
		assertTrue("Error: should have visited both plants", p1Times > 0);
		assertTrue("Error: should have visited both plants", p2Times > 0);
	}
	
	@Test
	public void testInsectSensoryModeOlfactory() {
		createPlantsAndInsectsSpecies(2, 1);
		int gridSize = 5;
		params.setValue("gridSize", gridSize);
		params.setValue("plantIds", "plant1;plant2");
		params.setValue("insectIds", "insect1");
		params.setValue("plantDistribution", "Blocks");
		params.setValue("i_insect1_eggs_on_plants", "plant1-1;plant2-2");
		params.setValue("p_plant1_percentage", 0.4);
		params.setValue("p_plant2_percentage", 0.6);
		params.setValue("i_insect1_max_eggs", 100);
		params.setValue("i_insect1_initial_count", 1);
		params.setValue("i_insect1_memory_size", 0);
		params.setValue("i_insect1_mortality", 0);
		params.setValue("i_insect1_mig_out", 0);
		params.setValue("i_insect1_sensory_mode", "Olfactory");
		params.setValue("i_insect1_max_flight_len", 3);
		RunEnvironment.getInstance().setParameters(params);		
		createContext();
		
		Insect ins = (Insect) context.getObjects(Insect.class).get(0);
		grid.moveTo(ins, 0, 0);
		ArrayList<GridPoint> allPoints = new ArrayList<GridPoint>();

		for (int i = 0; i < 10; ++i) {
			ins.step();
			GridPoint newPt = grid.getLocation(ins);
			if(newPt == null) {
				System.out.println("testInsectSensoryModeOlfactory: Test invalid because insect died / migrated out during test");
				break;
			}
			
			Plant p = getPlantAt(newPt.getX(), newPt.getY());
			assertEquals("Error: should fly over all unpreferred and land on preferred", p.getSpeciesParams().getPlantId(), "plant2");
			assertTrue("Error: should land on closest preferred", newPt.getX() < 3 && newPt.getY() < 3);
			
			allPoints.add(newPt);
			grid.moveTo(ins, 0, 0);
		}
		
		Set<GridPoint> set = new HashSet<GridPoint>(allPoints);
		assertTrue("Error: should not have visited the same plant over and over again thanks to randomness", set.size() > 1);
	}
}
