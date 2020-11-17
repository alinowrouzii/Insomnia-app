package view;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MyMenuBar extends JMenuBar implements Serializable{

	//app menu
	private JMenu applicationMenu;
	//options menuItem
	private JMenuItem optionsItem;
	//exit menuItem
	private JMenuItem exitItem;
	//editMenu
	private JMenu editMenu;
	//view Menu
	private JMenu viewMenu;
	//toggle FullScreen Item
	private JMenuItem toggleFullScreenItem;
	//toggle Slide Bar Item
	private JMenuItem toggleSideBarItem;

	//windowMenu
	private JMenu windowMenu;
	//minimize menuItem
	private JMenuItem minimizeItem;

	//toolsMenu
	private JMenu toolsMenu;

	//helpMenu
	private JMenu helpMenu;
	//aboutItem
	private JMenuItem aboutItem;
	//help menuItem
	private JMenuItem helpItem;

	//AboutDialog
	private JDialog aboutDialog;

	/**
	 * Create a new menu bar and
	 * then initialize that
	 */
	public MyMenuBar() {
		applicationMenu = new JMenu("Application");
		optionsItem = new JMenuItem("Options");
		exitItem = new JMenuItem("Exit");

		editMenu = new JMenu("Edit");

		viewMenu = new JMenu("View");
		toggleFullScreenItem = new JMenuItem("Toggle Full Screen");
		toggleSideBarItem = new JMenuItem("Toggle SideBar");

		windowMenu = new JMenu("Window");
		minimizeItem = new JMenuItem("Minimize");

		toolsMenu = new JMenu("Tools");

		helpMenu = new JMenu("Help");
		aboutItem = new JMenuItem("About");
		helpItem = new JMenuItem("Help");

		aboutDialog = new JDialog();
		initialize();
	}

	/**
	 * initialize menuBar
	 */
	private void initialize() {

		add(applicationMenu);
		applicationMenu.add(optionsItem);
		applicationMenu.add(exitItem);
		add(editMenu);

		add(viewMenu);
		viewMenu.add(toggleFullScreenItem);
		viewMenu.add(toggleSideBarItem);

		add(windowMenu);
		windowMenu.add(minimizeItem);

		add(toolsMenu);

		add(helpMenu);
		helpMenu.add(aboutItem);
		helpMenu.add(helpItem);

		setMNemonicForMenu();
		setAcceleratorForItems();
		initAboutDialog();
	}

	/**
	 * init about dialog
	 */
	private void initAboutDialog() {
		aboutDialog.setSize(200, 200);
		aboutDialog.setModalityType(ModalityType.DOCUMENT_MODAL);

	}

	/**
	 * Set MNemonic key to all
	 * Menu
	 */
	private void setMNemonicForMenu() {
		// set Mnemonic to all menu and
		// then set toolTipText to that
		applicationMenu.setMnemonic('A');
		applicationMenu.setToolTipText("Press ALT-A to select ApplicationMenu");
		editMenu.setMnemonic('E');
		editMenu.setToolTipText("Press ALT-E to select EditMenu");
		viewMenu.setMnemonic('V');
		viewMenu.setToolTipText("Press ALT-V to select ViewMenu");
		windowMenu.setMnemonic('W');
		windowMenu.setToolTipText("Press ALT-W to select windowMenu");
		toolsMenu.setMnemonic('T');
		toolsMenu.setToolTipText("Press ALT-T to select toolsMenu");
		helpMenu.setMnemonic('H');
		helpMenu.setToolTipText("Press ALT-H to select HelpMenu");
	}

	/**
	 * SetAccelerator to all manuItem
	 */
	private void setAcceleratorForItems() {
		// set accelerator to items of MyMenuBar
		optionsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		toggleFullScreenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		toggleSideBarItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		minimizeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
		aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		minimizeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));

	}

	/**
	 * @return optionsItem
	 */
	public JMenuItem getOptionsItem() {
		return optionsItem;
	}
	/**
	 * 
	 * @return ExitItem
	 */
	public JMenuItem getExitItem() {
		return exitItem;
	}
	/**
	 * toggle fullScreen item
	 * @return
	 */
	public JMenuItem getToggleFullScreenItem() {
		return toggleFullScreenItem;
	}

	/**
	 * 
	 * @return toggle slideBar item
	 */
	public JMenuItem getToggleSideBarItem() {
		return toggleSideBarItem;
	}
	
	/**
	 * 
	 * @return minimize item
	 */
	public JMenuItem getMinimizeItem() {
		return minimizeItem;
	}

	/**
	 * toolsMenu
	 * @return
	 */
	public JMenu getToolsMenu() {
		return toolsMenu;
	}

	/**
	 * 
	 * @return helpMenuItem
	 */
	public JMenuItem getHelpMenuItem() {
		return helpItem;
	}

	/**
	 * 
	 * @return about menuItem
	 */
	public JMenuItem getAboutMenuItem() {
		return aboutItem;
	}
}
