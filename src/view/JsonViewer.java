package view;

import javax.swing.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.IOException;

public class JsonViewer extends JPanel {

	private static final long serialVersionUID = 1L;
	private JEditorPane textarea;

	/**
	 * Create new json viewer
	 */
	public JsonViewer() {
		textarea = new JEditorPane();
		initialize();
	}

	/**
	 * initialize jsonViewer panel
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(600, 300));

		JScrollPane pane = new JScrollPane(textarea);
		this.add(pane, BorderLayout.CENTER);
	}

	/**
	 * set Json Text to beautify
	 * 
	 * @param text json text
	 */
	public void setJsonText(String text) {
		textarea.setText(text);
		jsonbeautify(textarea);

	}

	/**
	 * Beautify json text
	 * 
	 * @param textarea textArea of jsonviewer
	 */
	private static void jsonbeautify(JEditorPane textarea) {
		String contents = textarea.getText();
		if (!contents.isEmpty() && contents != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				Object json = objectMapper.readValue(contents, Object.class);
				textarea.setText(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
			} catch (IOException e) {
				textarea.setText(e.toString());

			}
		}
	}

}