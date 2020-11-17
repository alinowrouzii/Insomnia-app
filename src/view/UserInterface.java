package view;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemColor;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class UserInterface implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String ICON_PATH = "./icons/";
	// TrayIcon
	private TrayIcon trayIcon;
	// menu bar of app
	private MyMenuBar menuBar;
	// optionsDialog of App
	private OptionsDialog optionsDialog;
	// about dialog
	private AboutDialog aboutDialog;
	// frame dialog
	private JFrame frame;
	// panel for center and right side of App
	private JPanel requestPanel;
	// leftPanel of app
	private JPanel leftPanel;
	// left downPanel of app
	private JPanel leftDownPanel;
	// number of request
	private int numberOfRequest;
	// card layout for switch between
	// request panels
	private CardLayout cardLayout;
	// button for creating a new request
	private JButton newRequestBtn;
	// store requestKeys and value of keys
	// that used in cardLayout
	private LinkedHashMap<JButton, String> requestButtons;
	// Hashmap of request panels and store key of
	// each request panel
	private LinkedHashMap<RequestPanel, String> requestPanels;
	// private final static String PATH = "./requests/";
	private final static String PATH = "./data/";
	private final static String SETTING_PATH = "./setting/";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

//		for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//			System.out.println(info.getClassName());
//		}
//					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//					UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					UserInterface window = new UserInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * create this block to check that data direcotry is exist or not and if
	 * directory not exist then create directory
	 */
	static {
		new File(PATH).mkdirs();
		new File(SETTING_PATH).mkdirs();
	}

	/**
	 * Create the application.
	 */
	public UserInterface() {
		trayIcon = null;
		frame = new JFrame();
		leftPanel = new JPanel();
		menuBar = new MyMenuBar();
		optionsDialog = new OptionsDialog();
		aboutDialog = new AboutDialog();

		requestPanel = new JPanel();
		requestPanels = new LinkedHashMap<RequestPanel, String>();
		requestButtons = new LinkedHashMap<JButton, String>();
		numberOfRequest = 0;
		// intialize app
		initialize();
		// load data from app
		loadData();
		// then load existing setting
		loadSettings();
	}

	/**
	 * Load existing setting and set app with that setting
	 */
	public void loadSettings() {
		String setting = "";
		// read data to load
		try (FileInputStream fis = new FileInputStream(SETTING_PATH + "sett.bin")) {
			int read = 0;
			while ((read = fis.read()) != -1) {
				setting = setting + (char) read;
			}
			updateOptions(setting);
		} catch (FileNotFoundException e) {
			// set default options
			updateOptions("1 1 1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(frame);
	}

	/**
	 * Load exsiting request
	 */
	public void loadData() {

		File[] files = new File(PATH).listFiles();

		if (files.length == 0) {
			addRequestPanel(new RequestPanel(), "MyReq");
		} else {
			for (File file : files) {
				RequestPanel req = new RequestPanel();
				String requestName = "";
				try {
					requestName = req.loadData(file);
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				addRequestPanel(req, requestName);
			}
		}
	}

	/**
	 * Save current requests
	 */
	public void saveData() {
		int counter = 1;
		for (RequestPanel req : requestPanels.keySet()) {
			String val = requestPanels.get(req);
			for (JButton btn : requestButtons.keySet()) {
				if (requestButtons.get(btn).equals(val)) {
					val = btn.getText();
				}
			}
			req.saveData(val, "" + counter++);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		UIManager.getLookAndFeelDefaults().put("TextField.caretForeground", Color.red);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ICON_PATH + "appIcon.png"));
		frame.setBounds(100, 100, 1250, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		requestPanel.setBackground(SystemColor.activeCaptionBorder);
		frame.getContentPane().add(requestPanel, BorderLayout.CENTER);
		cardLayout = new CardLayout();
		requestPanel.setLayout(cardLayout);

		initMenuBar();
		initLeftPanel();
	}

	/**
	 * initialize menuBar
	 */
	private void initMenuBar() {
		frame.setJMenuBar(menuBar);

		initOptionsDialog();

		JMenuItem exitItem = menuBar.getExitItem();
		// add actionListener to exit Item
		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveData();
				if (optionsDialog.getOptionsInfo().split(" ")[1].equals("1")) {
					System.out.println("save in SystemTray");
					saveInSystemTray();
					frame.setVisible(false);
				} else {
					System.exit(0);
				}

			}
		});

		// get toggle sideBar from menuBar and set actionListener to that
		JMenuItem toggleSlideBar = menuBar.getToggleSideBarItem();
		toggleSlideBar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (leftPanel.isVisible()) {
					leftPanel.setVisible(false);
				} else {
					leftPanel.setVisible(true);
				}

				leftPanel.updateUI();
			}
		});
		// get toggle fullScreenItem from
		// menuBar and Then add ActionListener to that
		JMenuItem toggleFullScreen = menuBar.getToggleFullScreenItem();
		toggleFullScreen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// if frame size be maximized
				if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
					frame.setExtendedState(JFrame.NORMAL);
				} else {
					// if frame size be normal
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
				frame.setVisible(true);
			}
		});
		// Minimize app
		JMenuItem minimizeFrame = menuBar.getMinimizeItem();
		minimizeFrame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// minimize with --> frame.setState(Frame.ICONIFIED)
				// restore with --> frame.setState(Frame.NORMAL)
				if (frame.getState() == JFrame.NORMAL) {
					frame.setState(JFrame.ICONIFIED);
				} else {
					frame.setState(JFrame.NORMAL);
				}
			}
		});

		// helpMenu
		JMenuItem helpMenuItem = menuBar.getHelpMenuItem();
		helpMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HelpDialog hd = new HelpDialog();
				hd.setModalityType(ModalityType.APPLICATION_MODAL);
				hd.setLocationRelativeTo(frame);
				hd.setVisible(true);
			}
		});

		// AboutMenu item
		JMenuItem aboutMenuItem = menuBar.getAboutMenuItem();
		aboutDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		aboutMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				aboutDialog.setLocationRelativeTo(frame);
				aboutDialog.setVisible(true);
			}
		});
	}

	/**
	 * initialize leftPanel
	 */
	private void initLeftPanel() {

		int heightSize = frame.getContentPane().getPreferredSize().height;
		int widthSize = 220;

		leftPanel.setPreferredSize(new Dimension(widthSize, heightSize));
		frame.getContentPane().add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BorderLayout(0, 0));

		/*
		 * initialize leftDown panel first Add a RequestCreator Button And then Add
		 * action listener to that and then create a one request apnel as a default
		 */
		leftDownPanel = new JPanel();

		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.CENTER);

		leftDownPanel.setLayout(fl);
		leftPanel.add(leftDownPanel, BorderLayout.CENTER);

		newRequestBtn = new JButton("New Request");
		newRequestBtn.setBorder(new EmptyBorder(5, 5, 5, 5));
		newRequestBtn.setPreferredSize(new Dimension(leftPanel.getPreferredSize().width - 10, 35));
		leftPanel.add(newRequestBtn, BorderLayout.NORTH);

		newRequestBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				NewRequestDialog dialog = new NewRequestDialog();

				dialog.setModalityType(ModalityType.APPLICATION_MODAL);
				dialog.setLocationRelativeTo(frame);
				dialog.setVisible(true);
				if (dialog.getTextFieldInfo() != null) {
					String nameOfRequest = dialog.getTextFieldInfo();
					addRequestPanel(new RequestPanel(), nameOfRequest);
				}
			}
		});
	}

	/**
	 * Add new RequestPanel to requestPanel
	 *
	 * @param reQPanel      request panel that will be added
	 * @param nameOfRequest name of request
	 */
	private void addRequestPanel(RequestPanel reQPanel, String nameOfRequest) {
		numberOfRequest += 1;
		JButton requestbtn = new JButton(nameOfRequest);
		requestbtn.setPreferredSize(new Dimension(leftPanel.getPreferredSize().width - 10, 25));
		leftDownPanel.add(requestbtn);

		requestButtons.put(requestbtn, "" + (numberOfRequest));

		requestPanels.put(reQPanel, "" + (numberOfRequest));
		// add requestPanel to origin requestPanel as a card layout order
		requestPanel.add(reQPanel, "" + (numberOfRequest));

		requestbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String valueOfButtonToShowSpecificPanel = requestButtons.get(requestbtn);
				cardLayout.show(requestPanel, valueOfButtonToShowSpecificPanel);
			}
		});
		// add mouse listener to change name of request
		requestbtn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {

					JPopupMenu popUp = new JPopupMenu();
					JMenuItem renameItem = new JMenuItem("Rename");
					renameItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							NewRequestDialog dialog = new NewRequestDialog();
							dialog.setModalityType(ModalityType.APPLICATION_MODAL);
							dialog.setLocationRelativeTo(frame);
							dialog.setVisible(true);
							if (dialog.getTextFieldInfo() != null) {
								requestbtn.setText(dialog.getTextFieldInfo());
							}
						}
					});
					popUp.add(renameItem);
					popUp.addSeparator();
					JMenuItem deleteItem = new JMenuItem("Delete");
					deleteItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
						}
					});
					popUp.add(deleteItem);
					popUp.show(e.getComponent(), e.getX(), e.getY());
				}

			}
		});
		cardLayout.last(requestPanel);
		frame.setVisible(true);

	}

	/**
	 * initialize optionsDialog and then Add action listener to that
	 */
	private void initOptionsDialog() {
		optionsDialog.setSize(600, 400);
		optionsDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		// get from menuBar Field
		JMenuItem optionsItem = menuBar.getOptionsItem();
		optionsItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				optionsDialog.setLocationRelativeTo(frame);
				optionsDialog.setVisible(true);
				// TO DO : complete this
				updateOptions(optionsDialog.getOptionsInfo());
			}
		});

	}

	/**
	 * Update options of App
	 *
	 * @param option options of setting
	 */
	private void updateOptions(String option) {
		optionsDialog.setOptions(option);
		saveSettings(option);
		String[] options = option.split(" ");
		if (options[0].equals("1")) {
			RequestPanel.setFollowRedirect(true);
		} else {
			RequestPanel.setFollowRedirect(false);
		}
		try {
			if (options[2].equals("1")) {
				UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
			} else {
				UIManager.setLookAndFeel("com.formdev.flatlaf.FlatIntelliJLaf");
			}
			SwingUtilities.updateComponentTreeUI(frame);
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(System.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
	}

	/**
	 * Save setting as a file
	 *
	 * @param option options to save
	 */
	public void saveSettings(String option) {
		File file = new File("./setting/sett.bin");
		try (FileOutputStream fos = new FileOutputStream(file)) {

			fos.write(option.getBytes());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save app to system tray, Just call it! to save app in system tray
	 */
	private void saveInSystemTray() {
		if (SystemTray.isSupported()) {
			SystemTray systemTray = SystemTray.getSystemTray();

			PopupMenu trayMenu = new PopupMenu();
			MenuItem exitMenuItem = new MenuItem("Exit");
			exitMenuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			trayMenu.add(exitMenuItem);

			Image image = Toolkit.getDefaultToolkit().getImage(ICON_PATH + "trayIcon.png");
			trayIcon = new TrayIcon(image, "Insomnia", trayMenu);
			trayIcon.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					frame.setVisible(true);
				}
			});
			try {
				systemTray.add(trayIcon);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

}
