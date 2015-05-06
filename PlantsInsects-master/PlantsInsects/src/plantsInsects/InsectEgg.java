package plantsInsects;

import repast.simphony.context.Context;

public class InsectEgg extends Insect {

	private int hatchTime;
	
	public InsectEgg(InsectParams params, Context<Object> context) {
		super(params, 0, context);
		this.hatchTime = params.getEggHatchTime();
	}

	public int getHatchTime() {
		return hatchTime;
	}

	public void decreaseHatchTime(){
		hatchTime--;
	}
}
