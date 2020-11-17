package view;

public class ResponseHeader extends HeaderPanel {

	private static final long serialVersionUID = 6315598143613591198L;

	/**
	 * create a new response header
	 * 
	 * @param name  name of header
	 * @param value value of header
	 */
	public ResponseHeader(String name, String value) {
		// add first call a super class constructor and
		// then specify and set options for this class
		super();
		super.showCheckBoxButton(false);
		super.showDeleteButton(false);
		super.setEditable(false);
		super.setHeaderName(name);
		super.setHeaderValue(value);
	}

}
