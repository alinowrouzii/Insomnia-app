package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Util;

public class RightPanel extends JPanel implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1517754531577866816L;
	// rightUp panel
	private JPanel rightUpPanel;
	// code status label
	private JLabel codeStatusLabel;
	// response time label
	private JLabel responseTImeLabel;
	// consumed date label
	private JLabel consumedDateLabel;
	// right down panel
	private JPanel rightDownPanel;
	// origin tabbed pane of right down panel
	private JTabbedPane originTabbedPane;
	// message body tabbed pane
	private transient JTabbedPane messageBodyTabbedPane;
	// raw panel of message body
	private JPanel rawPanel;
	// message body panel
	private JPanel messageBodyPanel;
	// picture panel of message body
	private JPanel previewPanel;
	// textArea of message body
	private JPanel viewerPanel;
	// message body text area of raw panel
	private JEditorPane messageBodyTextArea;
	// header panel
	private JPanel headerPanel;
	// response headerPanel
	private JPanel responseHeadersPanel;
	// name label
	private JLabel nameLabel;
	// value label
	private JLabel valueLabel;
	// list of nameValue(s)
	private ArrayList<HeaderPanel> responseHeadersList;
	// button to copy response header property to clipboard
	private JButton copyHeadersBtton;
	// button to save picture with file chooser
	private JButton savePictureButton;
	//// button to save message body property
	private JButton saveMessageBodyTextBtn;
	// response body
	private byte[] responseBody;

	/**
	 * create a new rightPanel object and then initialize that
	 */
	public RightPanel() {
		responseBody = null;
		responseHeadersList = new ArrayList<HeaderPanel>();
		initialize();

	}

	/**
	 * initialize rightPanel
	 */
	private void initialize() {
		setPreferredSize(new Dimension(380, 600));
		setMaximumSize((new Dimension(380, 600)));
		setLayout(new BorderLayout());

		rightUpPanel = new JPanel();

		rightDownPanel = new JPanel();
		add(rightDownPanel, BorderLayout.CENTER);
		initializeRightUpPanel();
		initRIghtDownPanel();
	}

	/**
	 * initialize rightUp panel
	 */
	private void initializeRightUpPanel() {
		rightUpPanel.setPreferredSize(new Dimension(0, 45));
		rightUpPanel.setBorder(new EmptyBorder(7, 7, 0, 0));
		rightUpPanel.setBackground(Color.WHITE);
		add(rightUpPanel, BorderLayout.NORTH);

		codeStatusLabel = new JLabel("Status");
		codeStatusLabel.setBackground(Color.BLACK);
		codeStatusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		codeStatusLabel.setFont(new Font("Verdana", Font.BOLD, 17));
		codeStatusLabel.setBorder(new EmptyBorder(2, 2, 2, 2));
		responseTImeLabel = new JLabel("ResponseTime");
		responseTImeLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		responseTImeLabel.setBorder(new EmptyBorder(2, 2, 2, 2));
		consumedDateLabel = new JLabel("consumData");
		consumedDateLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		consumedDateLabel.setBorder(new EmptyBorder(2, 2, 2, 2));

		rightUpPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		rightUpPanel.add(codeStatusLabel);
		rightUpPanel.add(responseTImeLabel);
		rightUpPanel.add(consumedDateLabel);
	}

	/**
	 * Set status of code
	 *
	 * @param codeStatus status of code
	 */
	public void setCodeStatus(Integer codeStatus) {

		codeStatusLabel.setOpaque(true);

		if (codeStatus % 200 == 0 && codeStatus != 0) {
			String status = " " + codeStatus;
			if (codeStatus == 200) {
				status = status + " OK  ";
			} else if (codeStatus == 400) {
				status = status + " BadRequest";
			}
			codeStatusLabel.setBackground(Color.GREEN);
			codeStatusLabel.setText(status);
		} else if (codeStatus == 405) {
			codeStatusLabel.setBackground(Color.ORANGE);
			codeStatusLabel.setText("405 methodNotAllowed");
		} else {
			codeStatusLabel.setText(" " + codeStatus.toString() + " ");
			codeStatusLabel.setBackground(Color.RED);
		}
		codeStatusLabel.setForeground(Color.WHITE);
	}

	/**
	 * set time of response
	 *
	 * @param responseTimeMills response time
	 */
	public void setResponseTimeStatus(double responseTimeMills) {
		String formattedTimeAsSecond = String.format("%.3f", responseTimeMills / 1000.0);
		responseTImeLabel.setText(" " + formattedTimeAsSecond + " S ");
	}

	/**
	 * Set consumed Date of request
	 *
	 * @param consumedDate consumed date of request
	 */
	public void setConsumedDate(long consumedDate) {
		if (consumedDate < 1000) {
			consumedDateLabel.setText(" " + consumedDate + " B ");
		} else {
			consumedDateLabel.setText(" " + String.format("%.1f", consumedDate / 1000.0) + " KB ");
		}
	}

	/**
	 * initialize rightDownPanel
	 */
	private void initRIghtDownPanel() {
		rightDownPanel.setLayout(new BorderLayout());

		originTabbedPane = new JTabbedPane();
		rightDownPanel.add(originTabbedPane, BorderLayout.CENTER);

		messageBodyTabbedPane = new JTabbedPane();
		originTabbedPane.add(messageBodyTabbedPane, "BodyMessage");

		rawPanel = new JPanel();
		messageBodyTabbedPane.add(rawPanel, "source");

		previewPanel = new JPanel();
		initPreviewPanel();

		messageBodyTabbedPane.add(previewPanel, "preview");

		headerPanel = new JPanel();
		originTabbedPane.add(headerPanel, "Header");

		initRawPanel();
		initHeaderPanel();
	}

	/**
	 * initialize preview panel
	 */
	private void initPreviewPanel() {
		previewPanel.setLayout(new BorderLayout());

		viewerPanel = new JPanel();
		previewPanel.add(viewerPanel, BorderLayout.CENTER);

		savePictureButton = new JButton(" Save as a file ");
		savePictureButton.setEnabled(false);
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.add(savePictureButton);

		JFileChooser chooser = new JFileChooser();
		// add action listener to save picture
		// with file chooser
		savePictureButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int val = chooser.showSaveDialog(RightPanel.this);
				if (val == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String fileAddressAndName = file.toString();
					model.Util.saveFileToCertainPath(responseBody, fileAddressAndName);
				}
			}
		});
		previewPanel.add(panel, BorderLayout.SOUTH);
	}

	/**
	 * init message body Panel
	 */
	private void initRawPanel() {
		messageBodyTextArea = new JEditorPane();
		messageBodyTextArea.setEditable(false);

		messageBodyPanel = new JPanel(new BorderLayout());
		messageBodyPanel.add(messageBodyTextArea, BorderLayout.CENTER);
		rawPanel.setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane(messageBodyPanel);
		rawPanel.add(scrollPane, BorderLayout.CENTER);

		saveMessageBodyTextBtn = new JButton("Copy to clipBoard");
		saveMessageBodyTextBtn.setEnabled(false);
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.add(saveMessageBodyTextBtn);
		// add action listener to message body panel
		// to copy bodyTextArea text to clipboard
		saveMessageBodyTextBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Util.copyTextToClipBoard(messageBodyTextArea.getText());
			}
		});
		rawPanel.add(panel, BorderLayout.SOUTH);
	}

	/**
	 * init headerPanel
	 */
	private void initHeaderPanel() {
		headerPanel.setLayout(new BorderLayout());
	
		nameLabel = new JLabel("Name                       ");
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		valueLabel = new JLabel("               Value");
		valueLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		JPanel headerUpPanel = new JPanel();
		headerUpPanel.setLayout(new FlowLayout());
		headerPanel.add(headerUpPanel, BorderLayout.NORTH);
		headerUpPanel.add(nameLabel);
		headerUpPanel.add(valueLabel);

		responseHeadersPanel = new JPanel();
		responseHeadersPanel.setLayout(new BoxLayout(responseHeadersPanel, BoxLayout.Y_AXIS));

		JScrollPane scrollPane = new JScrollPane(responseHeadersPanel);
		headerPanel.add(scrollPane, BorderLayout.CENTER);
//		headerPanel.add(responseHeadersPanel, BorderLayout.CENTER);
		initCopyToClipBoardButton();

	}

	/**
	 * Initialize copy response headers to clipboard button
	 */
	private void initCopyToClipBoardButton() {
		copyHeadersBtton = new JButton("Copy To ClipBoard");
		copyHeadersBtton.setEnabled(false);
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		panel.add(copyHeadersBtton);
		headerPanel.add(panel, BorderLayout.SOUTH);
		copyHeadersBtton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Util.saveHeadersToClipBoard(responseHeadersList);
			}
		});
	}

	/**
	 * Add nameValuePanel to header
	 *
	 * @param responseHeaders headers of response
	 */
	public void addResponseHeaders(Map<String, List<String>> responseHeaders) {

		// first cleaning the responseHeaders panel
		// if responseHeader panel have a responseHeader
		if (responseHeadersList.size() > 0) {
			for (HeaderPanel header : responseHeadersList) {
				responseHeadersPanel.remove(header);
			}
			responseHeadersList.clear();
		}
		for (String headerName : responseHeaders.keySet()) {
			HeaderPanel headerPanel = new ResponseHeader(headerName, responseHeaders.get(headerName).toString());
			responseHeadersPanel.add(headerPanel);
			responseHeadersList.add(headerPanel);
		}
		copyHeadersBtton.setEnabled(true);

		updateUI();
	}

	/**
	 * Set picture as a response body and then set binary content of image as a
	 * source text
	 *
	 * @param body byte array that contains image
	 */
	public void setViewerAsAResponseBody(byte[] body, String type) {
		responseBody = body;
		messageBodyTextArea.setText(Util.byteToString(body));
		if (type.contains("image")) {
			try {
				Image image = Util.byteToImage(body);
				viewerPanel.removeAll();
				JLabel picLabel = new JLabel(new ImageIcon(image));
				JScrollPane scrolpane = new JScrollPane(picLabel);
				viewerPanel.setLayout(new BorderLayout());
				viewerPanel.add(scrolpane, BorderLayout.CENTER);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (type.contains("html")) {
			HtmlViewer htmlViewer = new HtmlViewer(body);
			viewerPanel.removeAll();
			viewerPanel.setLayout(new BorderLayout());
			JScrollPane scrolpane = new JScrollPane(htmlViewer);
			viewerPanel.add(scrolpane, BorderLayout.CENTER);

		}
		else if(type.contains("json")){
			JsonViewer jsonViewer = new JsonViewer();
			jsonViewer.setJsonText(Util.byteToString(body)) ;
			viewerPanel.removeAll();
			viewerPanel.setLayout(new BorderLayout());
			JScrollPane scrolpane = new JScrollPane(jsonViewer);
			viewerPanel.add(scrolpane, BorderLayout.CENTER);
		}
		saveMessageBodyTextBtn.setEnabled(true);
		savePictureButton.setEnabled(true);
		updateUI();
	}

	/**
	 * Set text of raw panel
	 *
	 * @param text text to set raw panel
	 */
	public void setRawText(String text) {
		// first clear the picture panel if viewerPanel have a picture for other
		// request
		viewerPanel.removeAll();
		savePictureButton.setEnabled(false);

		if (text != null) {
			messageBodyTextArea.setText(text);
			saveMessageBodyTextBtn.setEnabled(true);
		}
		updateUI();
	}

	/**
	 * Set text of raw text
	 *
	 * @param input byte array that contains raw text
	 */
	public void setRawText(byte[] input) {
		String text = Util.byteToString(input);
		setRawText(text);
	}

}
