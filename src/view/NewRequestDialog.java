package view;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;

public class NewRequestDialog extends JDialog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8583753444366367360L;
	//textField of newRequestDialg
	private JTextField textField;
	//ok button of newRequestDialg
	private JButton okbtn;
	//String of textField
	private String infoOfTextField ;

	/**
	 * Create a new RequestDialog object
	 */
	public NewRequestDialog() {
		textField = new JTextField();
		okbtn = new JButton("Ok");
		initialize();
	}

	/**
	 * initialize newRequestDialog object
	 */
	private void initialize() {

		setSize(new Dimension(250, 250));
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);

		JLabel myLabel = new JLabel("Enter Name for your Request!");
		myLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		myLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
		myLabel.setBackground(java.awt.Color.ORANGE);
		panel.add(myLabel);

		textField.setText("NewRequest");
		textField.setPreferredSize(new Dimension(200, 45));
		textField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		panel.add(textField);

		JPanel downPanel = new JPanel();
		getContentPane().add(downPanel, BorderLayout.SOUTH);

		okbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				infoOfTextField = textField.getText() ;
				setVisible(false);
			}
		});
		downPanel.add(okbtn);

	}

	/**
	 * Get Text of textField
	 * @return text of textField
	 */
	public String getTextFieldInfo() {
		return infoOfTextField ;
	}
}
