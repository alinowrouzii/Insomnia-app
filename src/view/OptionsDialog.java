package view;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.lang.ProcessHandle.Info;
import java.util.concurrent.Flow;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.Font;
import java.awt.Color;
import java.awt.FlowLayout;

public class OptionsDialog extends JDialog implements Serializable {
	// label of follow redirect
	private JLabel followRedirectLabel;
	// check box for active or deactive following redirect
	private JCheckBox activeFollowRedirectOrDeActive;
	// After Exit Label
	private JLabel afterExitLabel;
	// close or Save in SystemTray after Exit
	private JCheckBox closeAppAfterExitOrSaveInSystemTray;
	// darkTheme label
	private JLabel darkThemeLabel;
	// lightTheme label
	private JLabel lightThemeLabel;
	// dark theme button
	private JRadioButton darkThemebtn;
	// light theme button
	private JRadioButton lightThemebtn;
	// originPanel of dialog
	private JPanel originPanel;
	// ok button of dialog
	private JButton okbtn;
	// cancel button of dialog
	private JButton cancelBtn;
	// options info
	private String optionsInfo;

	/**
	 * Create a new options Dialog and then initialize that
	 */
	public OptionsDialog() {
		followRedirectLabel = new JLabel("             Follow Redirect");

		activeFollowRedirectOrDeActive = new JCheckBox();

		afterExitLabel = new JLabel("Save in SystemTray After ExitApp: ");
		closeAppAfterExitOrSaveInSystemTray = new JCheckBox();

		darkThemeLabel = new JLabel("              Dark Theme ");
		lightThemeLabel = new JLabel("              Light Theme ");
		darkThemebtn = new JRadioButton();
		lightThemebtn = new JRadioButton();

		okbtn = new JButton("Ok");
		cancelBtn = new JButton("Cancel");
		initialize();
	}

	/**
	 * initialize optionsDialog
	 */
	private void initialize() {
		// initialize optionsInfo
		// followRedirect --> Active
		// AfterExit --> Save in SystemTray
		// ThemeOfProgram --> DarkTheme
		optionsInfo = "1 1 1";

		getContentPane().setLayout(new BorderLayout());
		setSize(600, 400);
		setResizable(false);

		originPanel = new JPanel(new GridLayout(4, 2));
		add(originPanel, BorderLayout.CENTER);

		followRedirectLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		originPanel.add(followRedirectLabel);
		originPanel.add(activeFollowRedirectOrDeActive);
		activeFollowRedirectOrDeActive.setSelected(true);

		afterExitLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		originPanel.add(afterExitLabel);
		originPanel.add(closeAppAfterExitOrSaveInSystemTray);
		closeAppAfterExitOrSaveInSystemTray.setSelected(true);

		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(darkThemebtn);
		btnGroup.add(lightThemebtn);
		darkThemebtn.setSelected(true);

		lightThemeLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

		originPanel.add(lightThemeLabel);
		originPanel.add(lightThemebtn);
		originPanel.add(darkThemeLabel);
		originPanel.add(darkThemebtn);

		darkThemeLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

		FlowLayout fl = new FlowLayout();
		JPanel downPanel = new JPanel(fl);
//		downPanel.setBackground(null);
		getContentPane().add(downPanel, BorderLayout.SOUTH);
		MyActionListener myActionListener = new MyActionListener();
		downPanel.add(okbtn);
		okbtn.addActionListener(myActionListener);
		downPanel.add(cancelBtn);
		cancelBtn.addActionListener(myActionListener);

	}

	/**
	 * Inner class for actionListenr of options Dialog
	 */
	public class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okbtn) {
				saveInfo();
				setVisible(false);
			} else if (e.getSource() == cancelBtn) {
				setVisible(false);
			}
		}
	}

	/**
	 * save info of optionsDialog
	 */
	private void saveInfo() {
		optionsInfo = "";
		if (activeFollowRedirectOrDeActive.isSelected()) {
			optionsInfo = "1 ";
		} else {
			optionsInfo = "0 ";
		}

		if (closeAppAfterExitOrSaveInSystemTray.isSelected()) {
			optionsInfo = optionsInfo + "1 ";
		} else {
			optionsInfo = optionsInfo + "0 ";
		}

		if (darkThemebtn.isSelected()) {
			optionsInfo = optionsInfo + "1";
		} else {
			optionsInfo = optionsInfo + "0";
		}
	}

	/**
	 * Get a String that have a three number that specifies followingRedirect, close
	 * or save in systemTray after exiting app, and specifies theme of program
	 * 
	 * @return options info string
	 */
	public String getOptionsInfo() {
		return optionsInfo;
	}

	/**
	 * Set option to optionsDialog
	 * 
	 * @param option string that contains options to set
	 */
	public void setOptions(String option) {
		this.optionsInfo = option;
		String[] options = option.split(" ");
		if (options[0].equals("0")) {
			activeFollowRedirectOrDeActive.setSelected(false);
		} else {
			activeFollowRedirectOrDeActive.setSelected(true);
		}
		if (options[1].equals("0")) {
			closeAppAfterExitOrSaveInSystemTray.setSelected(false);
		} else {
			closeAppAfterExitOrSaveInSystemTray.setSelected(true);
		}
		if (options[2].equals("0")) {
			lightThemebtn.setSelected(true);
		} else {
			darkThemebtn.setSelected(true);

		}
	}
}
