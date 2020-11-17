package model;

public class DELETE extends Request {

	private static final long serialVersionUID = -8722293668078709001L;

	/**
	 * Create anew delete request object
	 */
	public DELETE() {
		super();
	}

	/**
	 * initialize and build object and preapare object to run
	 */
	public void initializeAndBuild() {
		super.initialize();
		super.requestBuilder.header("Content-Type", "application/x-www-form-urlencoded").DELETE();
		super.request = super.requestBuilder.build();
	}
}
