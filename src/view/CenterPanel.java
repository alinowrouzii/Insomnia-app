package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import model.PUT;
import model.Request;
import model.Util;

public class CenterPanel extends JPanel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// centerUp panel
	private JPanel centerUpPanel;
	// send button
	private JButton sendBtn;
	// center downPanel
	private JPanel centerDownPanel;
	// httpTextFiled
	private JTextField httpTextField;
	// center up combobox
	private JComboBox<Object> centerUpComboBox;
	// origin tabbed pane
	private JTabbedPane originTabbedPane;
	// bodyTab of App
	private JTabbedPane bodyTab;
	// nobodyPanel
	private JPanel nobodyPanel;
	// formPanel
	private JPanel formPanel;
	// create new name value button
	private JButton createNewFormData;
	// list of name value(S)
	private ArrayList<HeaderPanel> formDataList;
	// json panel
	private JPanel jSONPanel;
	// json textEditor
	private JEditorPane jSONTextEditor;
	// headerPanel
	private JPanel requestHeaderPanel;
	// create a new headerValue button
	private JButton createNewRequestHeader;
	// list of header value(S)
	private ArrayList<HeaderPanel> requestHeadersList;

	private JPanel queryPanel;
	private ArrayList<HeaderPanel> queryHeadersList;
	private JButton createNewQueryHeaderBtn;

	/**
	 * Create a new Center panel and then initialize all components
	 */
	public CenterPanel() {
		centerUpPanel = new JPanel();
		sendBtn = new JButton("   Send   ");
		httpTextField = new JTextField("http://");
		centerDownPanel = new JPanel();
		originTabbedPane = new JTabbedPane();

		bodyTab = new JTabbedPane();
		nobodyPanel = new JPanel();
		formPanel = new JPanel();
		createNewFormData = new JButton();
		formDataList = new ArrayList<HeaderPanel>();
		jSONPanel = new JPanel();

		createNewQueryHeaderBtn = new JButton("Create new queryHeader");
		queryHeadersList = new ArrayList<HeaderPanel>();
		queryPanel = new JPanel();

		requestHeaderPanel = new JPanel();
		createNewRequestHeader = new JButton();
		requestHeadersList = new ArrayList<HeaderPanel>();

		jSONTextEditor = new JEditorPane();
		initialize();
	}

	// initialize all components
	private void initialize() {
		setPreferredSize(new Dimension(600, 600));
		setLayout(new BorderLayout());

		// init centerUp panel
		initCenterUpPanel();
		// init centerDown panel
		initCenterDownPanel();

	}

	/**
	 * initialize center downPanel
	 */
	private void initCenterDownPanel() {
		add(centerDownPanel, BorderLayout.CENTER);
		centerDownPanel.setLayout(new BorderLayout(0, 0));
		centerDownPanel.add(originTabbedPane, BorderLayout.CENTER);
		initOriginTabbedPane();
	}

	/**
	 * initialize originTabbed pane
	 */
	private void initOriginTabbedPane() {

		originTabbedPane.setBorder(null);
		originTabbedPane.add("Body", bodyTab);
		initBodyTab();

		JScrollPane queryScrollPane = new JScrollPane(queryPanel);
		originTabbedPane.add("query", queryScrollPane);
		initQueryPanel();

		JScrollPane scrollPane = new JScrollPane(requestHeaderPanel);
		originTabbedPane.add("Header", scrollPane);
		initHeaderPanel();

	}

	/**
	 * Initialize query panel and add createQueryHeaders to query panel
	 */
	public void initQueryPanel() {
		queryPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
		queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.Y_AXIS));

		createNewQueryHeaderBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

		queryPanel.add(createNewQueryHeaderBtn);
		createNewQueryHeaderBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HeaderPanel header = new HeaderPanel();
				// add first nameValuePanel to list
				queryHeadersList.add(header);
				queryPanel.add(header);
				updateUI();

				header.getDeleteBtn().addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						queryPanel.remove(header);
						queryHeadersList.remove(header);
						updateUI();

					}
				});
			}
		});
		HeaderPanel firstQueryHeader = new HeaderPanel();
		firstQueryHeader.setPreferredSize(new Dimension(580, 45));
		// add first query header to list
		queryPanel.add(firstQueryHeader);
		queryHeadersList.add(firstQueryHeader);
	}

	/**
	 * initialize center up panel
	 */
	private void initCenterUpPanel() {
		centerUpPanel.setBackground(Color.WHITE);

		centerUpPanel.setPreferredSize(new Dimension(getPreferredSize().width, 45));
		add(centerUpPanel, BorderLayout.NORTH);
		centerUpPanel.setLayout(new BorderLayout(0, 0));

		sendBtn.setFont(new Font("Tahoma", Font.BOLD, 13));
		sendBtn.setBackground(null);
		sendBtn.setBorder(null);
		centerUpPanel.add(sendBtn, BorderLayout.EAST);

		String[] comboBoxOptions = { "GET", "POST", "PUT", "DELETE"/* , "Custom Mehode" */ };
		centerUpComboBox = new JComboBox<Object>(comboBoxOptions);
		centerUpComboBox.setFont(new Font("Verdana", Font.PLAIN, 15));
		centerUpComboBox.setBorder(null);
		centerUpPanel.add(centerUpComboBox, BorderLayout.WEST);

		httpTextField.setToolTipText("https://api.myproduct.com/v1/users");
		httpTextField.setBorder(null);
		httpTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		centerUpPanel.add(httpTextField, BorderLayout.CENTER);
		httpTextField.setColumns(10);
	}

	/**
	 * initilaize body TabbedPane
	 */
	private void initBodyTab() {

		nobodyPanel.setLayout(new BorderLayout());
		Image image = Toolkit.getDefaultToolkit().getImage("./icons/" + "pic.png");

		JLabel label = new JLabel(new ImageIcon(image));
		nobodyPanel.add(label, BorderLayout.CENTER);
		bodyTab.add(nobodyPanel, "NoBody");

		JScrollPane scrollPane = new JScrollPane(formPanel);
		bodyTab.add(scrollPane, " Form ");
		initFormPanel();
		
		JScrollPane jSonScrollPane = new JScrollPane(jSONPanel) ;
		bodyTab.add(jSonScrollPane, " JSON ");
		initJSONPanel();
	}

	/**
	 * initialize formPanel and then add actionListener to create a new formData
	 * button and then create a one nameValue object as a default and then add a
	 * file chooser to upload a file with multipart form data
	 */
	private void initFormPanel() {

		formPanel.setBorder(new EmptyBorder(4, 4, 4, 4));

		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

		// panel to file chooser and create new form button
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.setPreferredSize(new Dimension(getPreferredSize().width - 50, 50));
		panel.setMaximumSize(new Dimension(getPreferredSize().width, 50));
		formPanel.add(panel);
		createNewFormData.setText("Create new Form");
		panel.add(createNewFormData);
		createNewFormData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				HeaderPanel nameValue = new HeaderPanel();
				// add first nameValuePanel to list
				formDataList.add(nameValue);
				formPanel.add(nameValue);
				updateUI();

				nameValue.getDeleteBtn().addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						formPanel.remove(nameValue);
						formDataList.remove(nameValue);
						updateUI();

					}
				});
			}
		});
		// then add file chooser as a action to filechooser btn to
		// choose a file and then upload file
		JFileChooser filechooser = new JFileChooser();
		JButton filechooserBtn = new JButton("Choose File To Upload");
		panel.add(filechooserBtn);
		filechooserBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int val = filechooser.showDialog(CenterPanel.this, "Choose");
				if (val == JFileChooser.APPROVE_OPTION) {
					String path = filechooser.getSelectedFile().getPath();
					HeaderPanel header = new HeaderPanel("myfile", path);
					formDataList.add(header);
					System.out.println(formDataList.size());
				}
			}
		});

		HeaderPanel firstNameValue = new HeaderPanel();
		firstNameValue.setPreferredSize(new Dimension(580, 45));
		// add first nameValuePanel to list
		formDataList.add(firstNameValue);
		formPanel.add(firstNameValue);

	}

	/**
	 * initialize json panel
	 */
	private void initJSONPanel() {
		jSONPanel.setBorder(new EmptyBorder(2,2,4,15));
		jSONPanel.setLayout(new BorderLayout());
		jSONPanel.add(jSONTextEditor, BorderLayout.CENTER);
		jSONTextEditor.setEditable(true);
		jSONTextEditor.setBorder(new LineBorder(Color.WHITE));
		jSONTextEditor.setFont(new Font("Thohama", Font.PLAIN, 18));
	}

	/**
	 * initialize a header panel and then add action listener to create a new
	 * headerPanel and then create a one headerPanel object as a default
	 */
	private void initHeaderPanel() {

		requestHeaderPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
		requestHeaderPanel.setLayout(new BoxLayout(requestHeaderPanel, BoxLayout.Y_AXIS));

		createNewRequestHeader.setText("Create new Header");
		createNewRequestHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

		requestHeaderPanel.add(createNewRequestHeader);
		createNewRequestHeader.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				HeaderPanel headerValue = new HeaderPanel();
				// add first nameValuePanel to list
				requestHeadersList.add(headerValue);
				requestHeaderPanel.add(headerValue);
				updateUI();

				headerValue.getDeleteBtn().addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						requestHeaderPanel.remove(headerValue);
						requestHeadersList.remove(headerValue);
						updateUI();

					}
				});
			}
		});
		HeaderPanel firstHeaderValue = new HeaderPanel();
		firstHeaderValue.setPreferredSize(new Dimension(580, 45));
		// add first nameValuePanel to list
		requestHeadersList.add(firstHeaderValue);
		requestHeaderPanel.add(firstHeaderValue);
	}

	/**
	 * Get send button
	 * 
	 * @return send button
	 */
	public JButton getSendBtn() {
		return sendBtn;
	}

	/**
	 * Get url of request
	 * 
	 * @return url that store in center up textField
	 */
	public String getUrl() {
		return httpTextField.getText();
	}

	/**
	 * Get response headers
	 * 
	 * @return response headers
	 */
	public ArrayList<HeaderPanel> getRequestHeaders() {
		return requestHeadersList;
	}

	/**
	 * Get form data list
	 * 
	 * @return arrayList of HeaderPanel that store form data info
	 */
	public ArrayList<HeaderPanel> getFormDataList() {
		return formDataList;
	}

	/**
	 * Get query headers list
	 * 
	 * @return arrayList of HeaderPanel that store query headers info
	 */
	public ArrayList<HeaderPanel> getQueryHeadersList() {
		return queryHeadersList;
	}

	/**
	 * Get request method
	 * 
	 * @return request method that is selected by centerUp comboBox
	 */
	public String getRequestMethod() {
		return centerUpComboBox.getSelectedItem().toString();
	}

	/**
	 * Get json textEditor text
	 * 
	 * @return text of json textEditor
	 */
	public String getJSONTextEditorText() {
		return jSONTextEditor.getText();
	}

	/**
	 * Set headers of request
	 * 
	 * @param headers request headers
	 */
	public void setRequestHeaders(String[] headers) {
		// first clearing the existing headerRequest
		if (headers.length > 0) {
			for (HeaderPanel header : requestHeadersList) {
				requestHeaderPanel.remove(header);
			}
			requestHeadersList.clear();
		}

		for (int i = 0; i < headers.length; i += 2) {
			HeaderPanel headerPanel = new HeaderPanel(headers[i], headers[i + 1]);
			// then add requests Header to requestHeaderPanel
			requestHeaderPanel.add(headerPanel);
			requestHeadersList.add(headerPanel);
		}
	}

	/**
	 * Set form data info
	 * 
	 * @param formData formData info
	 */
	public void setFormData(Map<Object, Object> formData) {
		// first clearing the existing formData
		if (formData.size() > 0) {
			for (HeaderPanel header : formDataList) {
				formPanel.remove(header);
			}
			formDataList.clear();
		}
		for (Object obj : formData.keySet()) {
			HeaderPanel headerPanel = new HeaderPanel(obj.toString(), formData.get(obj).toString());
			// then add requests Header to requestHeaderPanel
			formPanel.add(headerPanel);
			formDataList.add(headerPanel);
		}
	}

	public void setQueryHeaders(String queryInfo) {
		if (queryInfo.length() > 0) {
			//first clear the query panel
			for(HeaderPanel header : queryHeadersList) {
				queryPanel.remove(header);
			}
			queryHeadersList.clear();
			
			String[] headers = queryInfo.trim().split("=|\\&");
			for (int i = 0; i < headers.length; i += 2) {
				HeaderPanel headerPanel = new HeaderPanel(headers[i], headers[i + 1]);
				// then add requests Header to requestHeaderPanel
				queryPanel.add(headerPanel);
				queryHeadersList.add(headerPanel);
			}
		}
	}

	/**
	 * Set request url
	 * 
	 * @param url url to set to httpTextField
	 */
	public void setRequestUrl(String url) {
		httpTextField.setText(url);
	}

	/**
	 * Set json text
	 * 
	 * @param text text to set to jsonTextEditor
	 */
	public void setJsonText(String text) {
		jSONTextEditor.setText(text);
	}

	/**
	 * Set selected method
	 * 
	 * @param method method to set selected item, for centerUp combobox
	 */
	public void setSelectedMethod(String method) {
		int index = 0;
		if (method.equals("GET")) {
			index = 0;
		} else if (method.equals("POST")) {
			index = 1;
		} else if (method.equals("PUT")) {
			index = 2;
		} else if (method.equals("DELETE")) {
			index = 3;
		}
		centerUpComboBox.setSelectedIndex(index);
	}

	/**
	 * Set property of center panel to Request object
	 * 
	 * @param centerPanel    center panel of app
	 * @param request        request of Request class to send request with that
	 * @param followRedirect following redirect or not
	 */
	public static void setRequestProperty(CenterPanel centerPanel, Request request, boolean followRedirect) {
		String url = centerPanel.getUrl();

		Map<Object, Object> formData = Util.convertToMap(centerPanel.getFormDataList());
		String[] reuqestHeaders = convertToStringArray(centerPanel.getRequestHeaders());
		String jsontext = centerPanel.getJSONTextEditorText();

		request.setUrl(url);
		if (request instanceof PUT) {
			try {
				((PUT) request).setFormData(formData);
				if (jsontext.length() > 0 && jsontext != null) {
					((PUT) request).setJSONContent(jsontext);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		request.setQueryInfo(Util.convertToQueryInfo(centerPanel.getQueryHeadersList()));
		request.setHeaders(reuqestHeaders);
		request.setFollowRedirect(followRedirect);
	}

	/**
	 * convert arrayList of HeaderPanel to array of string
	 * 
	 * @param reqHeaders array list of HeaderPanel
	 * @return array of string that each element is text of one textFiled in
	 *         HeaderPanel
	 */
	public static String[] convertToStringArray(ArrayList<HeaderPanel> reqHeaders) {
		ArrayList<String> allheaders = new ArrayList<String>();
		for (HeaderPanel header : reqHeaders) {
			if (!header.isEmpty() && header.isActive()) {
				allheaders.add(header.getNameText());
				allheaders.add(header.getValueText());
			}
		}
		String[] res = new String[allheaders.size()];
		res = allheaders.toArray(res);
		return res;
	}
}
