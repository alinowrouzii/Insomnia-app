package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;



public class HeaderPanel extends JPanel implements Serializable {

	private static final long serialVersionUID = 4443992928678372315L;
	// check box of nameValue
	private transient JCheckBox checkbox;
	// textField for name
	private JTextField nameTextField;
	// textField for value
	private JTextField valueTextField;
	// button for delete request
	private transient JButton deleteBtn;

	/**
	 * Create a new HeaderPanel object adn then initialize that
	 */
	public HeaderPanel() {
		checkbox = new JCheckBox();
		nameTextField = new JTextField();
		valueTextField = new JTextField();
		deleteBtn = new JButton("Delete");
		initialize();
	}

	/**
	 * Create new Header Panel with given Info
	 * 
	 * @param name  name of header
	 * @param value value of header
	 */
	public HeaderPanel(String name, String value) {
		checkbox = new JCheckBox();
		nameTextField = new JTextField();
		valueTextField = new JTextField();
		deleteBtn = new JButton("Delete");
		initialize();
		nameTextField.setText(name);
		valueTextField.setText(value);
	}

	/**
	 * initialize Header panel
	 */
	private void initialize() {
		setBorder(new EmptyBorder(4, 4, 4, 4));
		BoxLayout bl = new BoxLayout(this, BoxLayout.X_AXIS);

		setLayout(bl);

		// setPreferredSize(new Dimension(580, 40));
		setSize(580, 40);
		setMaximumSize(new Dimension(1500, 40));

		nameTextField.setPreferredSize(new Dimension(200, 35));
		nameTextField.setFont(new Font("Dialog", Font.PLAIN, 16));
		nameTextField.setText("Name...");
		add(nameTextField);
		add(valueTextField);
		valueTextField.setPreferredSize(new Dimension(240, 35));
		valueTextField.setFont(new Font("Dialog", Font.PLAIN, 16));
		valueTextField.setText("Value...");
		checkbox.setSelected(true);
		add(checkbox);
		add(deleteBtn);
	}

	/**
	 * Get check box of nameValue
	 * 
	 * @return check box of nameValue
	 */
	public JCheckBox getCheckbox() {
		return checkbox;
	}

	/**
	 * Get textField for name
	 * 
	 * @return ctextField for name
	 */
	public String getNameText() {
		return nameTextField.getText();
	}

	/**
	 * Get textField for value
	 * 
	 * @return ctextField for value
	 */
	public String getValueText() {
		return valueTextField.getText();
	}

	/**
	 * Show or hide delete button
	 * 
	 * @param bool boolean to show or hide delete button
	 */
	protected void showDeleteButton(boolean bool) {
		deleteBtn.setVisible(bool);
	}

	/**
	 * Show or hide checkBox button
	 * 
	 * @param bool boolean to show or hide checkBox button
	 */
	protected void showCheckBoxButton(boolean bool) {
		checkbox.setVisible(bool);
	}

	/**
	 * Set editable HeaderPanel or not
	 * 
	 * @param bool boolean to set editable HeaderPanel or not
	 */
	protected void setEditable(boolean bool) {
		nameTextField.setEditable(bool);
		valueTextField.setEditable(bool);
	}

	/**
	 * Get button for delete object
	 * 
	 * @return cbutton for delete object
	 */
	public JButton getDeleteBtn() {
		return deleteBtn;
	}

	/**
	 * Set header name
	 * 
	 * @param name name of header
	 */
	protected void setHeaderName(String name) {
		nameTextField.setText(name);
	}

	/**
	 * Set header value
	 * 
	 * @param value value of header
	 */
	protected void setHeaderValue(String value) {
		valueTextField.setText(value);
	}

	/**
	 * Check that heade is Empty or not
	 * 
	 * @return true if headerPanel is empty
	 */
	public boolean isEmpty() {
		if (nameTextField.getText().length() == 0 || nameTextField.getText().equals("Name...")) {
			return true;
		}
		if (valueTextField.getText().length() == 0 || valueTextField.getText().equals("Value...")) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (nameTextField.getText().hashCode());
		result = prime * result + (valueTextField.getText().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		HeaderPanel other = (HeaderPanel) obj;
		if (!other.getNameText().equals(this.getNameText())) {
			return false;
		}
		if (!other.getValueText().equals(this.getValueText())) {
			return false;
		}
		return true;
	}

	/**
	 * Check that headerPanel is active or not
	 * 
	 * @return returns true if check box of headerPanel is selected
	 */
	public boolean isActive() {
		if (checkbox.isSelected()) {
			return true;
		}
		return false;
	}

}
