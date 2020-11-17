package model;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;;

public class POST extends PUT {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new POST request
	 */
	public POST() {
		super();
	}

	/**
	 * Initialize and build object to preapres object to call run method
	 */
	@Override
	public void initializeAndBuild() throws IOException {
		super.initialize();
		if (jsonContent.length() > 0 && jsonContent != null) {
			System.out.println("jsonnnnn");
			System.out.println(jsonContent);
			super.requestBuilder.header("Content-Type", "application/json; utf-8");
			super.requestBuilder.POST(BodyPublishers.ofString(jsonContent));
		} else {
			if (formData != null && formData.size() > 0) {
				super.requestBuilder.header("Content-Type", "multipart/form-data; boundary=" + boundary)
						.POST(ofMimeMultipartData(formData, boundary));
			} else {
				super.requestBuilder.header("Content-Type", "multipart/form-data; boundary=" + boundary)
						.POST(BodyPublishers.noBody());
			}
		}
		super.request = requestBuilder.build();
	}

}