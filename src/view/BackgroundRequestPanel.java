package view;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import model.Request;
import model.Util;

public class BackgroundRequestPanel extends SwingWorker<Integer, Integer> implements Serializable {

	private static final long serialVersionUID = -7913672645185126010L;
	// right panel that is response panel too
	private RightPanel rightPanel;
	// right panel that is request panel too
//	private CenterPanel centerPanel;
	// request that send request wuth this object :)
	private Request request;

	/**
	 * Create a new backgroundRequestPanel object to handle and run request in
	 * background of GUI
	 *
	 * @param rightPanel responsePanel
	 * @param request    request of Request class!
	 */
	public BackgroundRequestPanel( /* CenterPanel centerPanel, */ RightPanel rightPanel, Request request) {
		this.rightPanel = rightPanel;
//		this.centerPanel = centerPanel;
		this.request = request;
	}

	/**
	 * Run a request in this method
	 */
	@Override
	protected Integer doInBackground() {
		try {
			request.initializeAndBuild();
		} catch (IOException e) {
			e.printStackTrace();
		}
		request.run();

		return request.getResponseCode();
	}

	/**
	 * Set property of rightPanel(responsePanel)
	 */
	@Override
	protected void done() {
		// getting type of responseBody
		if (request.isDone()) {
			try {
				setResponseProperty(rightPanel, request);
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set property of rightPanel(ResponsePanel)
	 * 
	 * @param rightPanel rightPanel(ResponsePanel)
	 * @param request    request of Request class
	 * @throws ExecutionException   throws this exception with any problem comes
	 *                              with execution of request
	 * @throws InterruptedException throws this exception when any problem comes
	 *                              with request
	 */
	public static void setResponseProperty(RightPanel rightPanel, Request request)
			throws ExecutionException, InterruptedException {
		rightPanel.setCodeStatus(request.getResponseCode());
		rightPanel.setResponseTimeStatus(request.getResponseTimeMills());
		// means request is succesful
		rightPanel.setConsumedDate(request.getResponseBodyLength());
		rightPanel.addResponseHeaders(request.getResponseHeaders());
		try {
			String contentType = request.getResponseHeaders().get("content-type").get(0);
			if (contentType.contains("json")) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						rightPanel.setViewerAsAResponseBody(request.getResponseBody(), contentType);
//						rightPanel.setRawText(request.getResponseBody());
					}
				});
				thread.start();

//				String text = Util.byteToString(request.getResponseBody());
//				rightPanel.setRawText(text);
			} else if (contentType.contains("image") || contentType.contains("html")) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						rightPanel.setViewerAsAResponseBody(request.getResponseBody(), contentType);
					}
				});
				thread.start();
			} else {
				rightPanel.setRawText(request.getResponseBody());
			}

		} catch (NullPointerException e) {
			rightPanel.setRawText(request.getResponseBody());
			e.printStackTrace();
		}
	}

}
