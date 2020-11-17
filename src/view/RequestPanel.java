package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.DELETE;
import model.GET;
import model.POST;
import model.PUT;
import model.Request;
import model.Util;

import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

public class RequestPanel extends JPanel implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4634649471558166272L;
	// center panel of requestPanel
	private CenterPanel centerPanel;
	// right panel of requestPanel
	private RightPanel rightPanel;
	// do request in background and implements SwingWorkder
	private BackgroundRequestPanel backgroundRequestPanel;
	// request to work with that
	private Request request;
	// boolean to follow redirect or not
	private static boolean followRedirect;
	// private final static String PATH = "./requests/";
	// path of saved requests
	private final static String PATH = "./data/";

	/**
	 * Create a new RequestPanel object and then initialize that
	 */
	public RequestPanel() {
		centerPanel = new CenterPanel();
		rightPanel = new RightPanel();
		initialize();

	}

	/**
	 * initialize a center and right panel of requestPanel
	 */
	private void initialize() {
		// set follow redirect true as default
		followRedirect = true;

		// setPreferdSize of requestPanel
		setPreferredSize(new Dimension(980, 600));

		// Adding a gridBag layout to requestPanel
		// and then create a gridBagConstrains
		// for each other
		setBackground(SystemColor.activeCaptionBorder);
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 600, 380 };
		gbl.rowHeights = new int[] { 600 };
		gbl.columnWeights = new double[] { 1.0, 0.8 };
		gbl.rowWeights = new double[] { 1.0 };
		setLayout(gbl);

		centerPanel = new CenterPanel();
		centerPanel.setBorder(null);

		GridBagConstraints gbc_centerPanel = new GridBagConstraints();
		gbc_centerPanel.insets = new Insets(0, 1, 0, 0);
		gbc_centerPanel.fill = GridBagConstraints.BOTH;
		gbc_centerPanel.gridx = 0;
		gbc_centerPanel.gridy = 0;
		add(centerPanel, gbc_centerPanel);

		rightPanel = new RightPanel();
		rightPanel.setBorder(null);

		GridBagConstraints gbc_rightPanel = new GridBagConstraints();
		gbc_rightPanel.insets = new Insets(0, 1, 0, 0);
		gbc_rightPanel.fill = GridBagConstraints.BOTH;
		gbc_rightPanel.gridx = 1;
		gbc_rightPanel.gridy = 0;
		add(rightPanel, gbc_rightPanel);

		JButton sendBtn = centerPanel.getSendBtn();
		sendBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				request = Request.createRequest(centerPanel.getRequestMethod());

				CenterPanel.setRequestProperty(centerPanel, request, followRedirect);
				backgroundRequestPanel = new BackgroundRequestPanel(/* centerPanel, */ rightPanel, request);

				backgroundRequestPanel.execute();
			}
		});
	}

	/**
	 * Set follow redirect
	 *
	 * @param bool boolean to follow redirect or not
	 */
	public static void setFollowRedirect(boolean bool) {
		followRedirect = bool;
	}

	/**
	 * Save current reuqest to specify path
	 *
	 * @param requestName name of request
	 * @param fileName    name of file
	 */
	public void saveData(String requestName, String fileName) {

		try (FileOutputStream fos = new FileOutputStream(new File(PATH + fileName + ".bin"));
				BufferedOutputStream bos = new BufferedOutputStream(fos);) {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			if (request == null) {
				request = Request.createRequest(centerPanel.getRequestMethod());
			}

//			System.out.println("reqname" + requestName);
			// first save the requestName
			request.setRequestName(requestName);
			// then update the request info
			CenterPanel.setRequestProperty(centerPanel, request, followRedirect);
			// then save request
			oos.writeObject(request);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load request file and then update the requestPanel
	 *
	 * @param file file that contains request
	 * @return name of request
	 * @throws FileNotFoundException  when no file is found!
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public String loadData(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		try (FileInputStream fin = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fin);
				ObjectInputStream ois = new ObjectInputStream(bis);) {
			Request request = (Request) ois.readObject();
			setRequestPropertyToPanels(request);
//			System.out.println(request.getRequestName());
			return request.getRequestName();
		}
	}

	/**
	 * Set content of Request to center panel and rightPanel
	 *
	 * @param request request of Request Class
	 */
	public void setRequestPropertyToPanels(Request request) {
		this.request = request;
		centerPanel.setRequestUrl(request.getUrl());
		centerPanel.setRequestHeaders(request.getRequestHeaders());
		centerPanel.setQueryHeaders(request.getQueryInfo());
		if (request instanceof PUT) {
			centerPanel.setFormData(((PUT) request).getFormData());
			centerPanel.setJsonText(((PUT) request).getJsonContent());
		}
		if (request instanceof POST) {
			centerPanel.setSelectedMethod("POST");
		} else if (request instanceof PUT) {
			centerPanel.setSelectedMethod("PUT");

		} else if (request instanceof GET) {
			centerPanel.setSelectedMethod("GET");

		} else if (request instanceof DELETE) {
			centerPanel.setSelectedMethod("DELETE");
		}

		if (request.isDone()) {
			// then update the rightPanel
			try {
				BackgroundRequestPanel.setResponseProperty(rightPanel, request);
			} catch (ExecutionException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}