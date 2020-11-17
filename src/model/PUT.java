package model;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class PUT extends Request {

	private static final long serialVersionUID = -6723282738423903618L;
	// Map of form data information
	protected Map<Object, Object> formData;
	// string that store json content
	protected String jsonContent = "";
	// boundary to use in multipart form data
	protected String boundary;

	/**
	 * Create a new PUT request and then intialize some fields
	 */
	public PUT() {
		super();
		formData = new HashMap<Object, Object>();
		boundary = Long.toOctalString(System.currentTimeMillis());
	}

	/**
	 * Initialize object and then preapre it to call run method
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
						.PUT(ofMimeMultipartData(formData, boundary));
			} else {
				super.requestBuilder.header("Content-Type", "multipart/form-data; boundary=" + boundary)
						.PUT(BodyPublishers.noBody());
			}
		}
		super.request = requestBuilder.build();
	}

	/**
	 * Puplish body of form
	 * 
	 * @param formData map that contains form data info
	 * @return bodyPublisher of form data
	 */
	public BodyPublisher ofForm(Map<Object, Object> formData) {
		StringBuilder encodedText = new StringBuilder();

		Set<Entry<Object, Object>> setFormData = formData.entrySet();
		for (Entry<Object, Object> form : setFormData) {
			if (encodedText.length() > 0 && encodedText != null) {
				encodedText.append("&");
			}
			encodedText.append(URLEncoder.encode(form.getKey().toString(), StandardCharsets.UTF_8));
			encodedText.append("=");
			encodedText.append(URLEncoder.encode(form.getValue().toString(), StandardCharsets.UTF_8));

		}

		return BodyPublishers.ofString(encodedText.toString());
	}

	/**
	 * Set form data
	 * 
	 * @param formData map of form data info
	 * @throws IOException throws this exception when any problem come with work
	 *                     with file
	 */
	public void setFormData(Map<Object, Object> formData) throws IOException {
		this.formData = formData;

	}

	/**
	 * Publish body to multipart data
	 * 
	 * @param data     map that contains info of form data
	 * @param boundary boundary to use in multipart form data
	 * @return bodyPublisher of multiPart form data
	 * @throws IOException throws this exception when any problem comes with work
	 *                     with file
	 */
	public static BodyPublisher ofMimeMultipartData(Map<Object, Object> data, String boundary) throws IOException {

		var byteArrays = new ArrayList<byte[]>();
		byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=")
				.getBytes(StandardCharsets.UTF_8);

		if (data == null) {
		}
		for (Map.Entry<Object, Object> entry : data.entrySet()) {
			byteArrays.add(separator);

			if (Util.isPath(entry.getValue().toString())) {

				var path = (Paths.get(entry.getValue().toString()));
				String mimeType = Files.probeContentType(path);
				byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName() + "\"\r\nContent-Type: "
						+ mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
				try {
					byteArrays.add(Files.readAllBytes(path));
				} catch (IOException e) {
					System.out.println("there is a problem to read a file!!");
					e.printStackTrace();
				}
				byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
			} else {
				byteArrays.add(
						("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n").getBytes(StandardCharsets.UTF_8));
			}
		}
		byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
		return BodyPublishers.ofByteArrays(byteArrays);
	}

	/**
	 * Set json Content
	 * @param jsonContent content of json
	 */
	public void setJSONContent(String jsonContent) {
		if (jsonContent.length() > 0 && jsonContent != null) {
			this.jsonContent = jsonContent;
		}
	}

	/**
	 * Get form data map
	 * @return form data
	 */
	public Map<Object, Object> getFormData() {
		return formData;
	}

	/**
	 * Get json content
	 * @return json content
	 */
	public String getJsonContent() {
		return jsonContent;
	}
}
