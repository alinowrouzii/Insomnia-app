package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class HtmlViewer extends JPanel {

	private static final long serialVersionUID = 1L;
	private JEditorPane webPanel;
	private HTMLEditorKit htmlEditorKit;
	private HTMLDocument htmlDocument;
	private ByteArrayInputStream htmlBody;

	public HtmlViewer(byte[] htmlBody) {
		webPanel = new JEditorPane();
		htmlEditorKit = new HTMLEditorKit();
		
		addHtmlBody(htmlBody);
		initialize();
	}

	private void addHtmlBody(byte[] htmlBody) {
		this.htmlBody = new ByteArrayInputStream(htmlBody);
	}

	private void initialize() {
		setLayout(new BorderLayout());

		webPanel.setEditable(false);
		webPanel.setOpaque(true);
		webPanel.setContentType("text/html;charset=\"UTF-8\"");

		add(webPanel, BorderLayout.CENTER);
		fillPanel();
	}

	public void fillPanel() {
		setTheStyleSheet(htmlEditorKit.getStyleSheet());
		htmlDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();
		BufferedReader br = new BufferedReader(new InputStreamReader(htmlBody));
		try {
			webPanel.read(br, htmlDocument);
		} catch (IOException e) {
			e.printStackTrace();
		}
		webPanel.repaint();

	}
	
	public void setTheStyleSheet(StyleSheet s) {
		s.addRule("body {color:#000; background-color:#ffdddd; font-family:times; font-size:20px; margin: 4px; }");
		s.addRule("h1 {color: blue;}");
		s.addRule("h2 {color: #ff0000;}");
		s.addRule("h3 {color: #ff0000;}");
		s.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");
	}
}
