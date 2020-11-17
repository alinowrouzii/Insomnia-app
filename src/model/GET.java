package model;

public class GET extends Request {

	/**
	 * Create a get request object
	 */
	public GET() {
		super();
	}

	/**
	 * initialize and preapape GET object to run
	 */
	@Override
	public void initializeAndBuild() {
		super.initialize();
		requestBuilder.GET();
		super.request = super.requestBuilder.build();
	}
}
