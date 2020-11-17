package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestManager {
	// request
	private static Request request;
	// method of request
	private static String method;
	// headers of request
	private static String[] requestHeaders;
	// url of request
	private static String url;
	// form data for put and post request
	private static Map<Object, Object> formData;
	// boolean to get info about request or not
	private static boolean getInfoOrNot;
	// boolean to save response or not
	private static boolean saveResponseOrNot;
	// boolean to save request or not
	private static boolean saveRequestOrNot;
	// boolean to follow redirect or not
	private static boolean followRedirect;
	// name of response file
	private static String responseFileName = "";
	// path to uploading file
	private static String pathToUploadFile;
	// name of request
	private static String requestName;

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		ExecutorService exe = Executors.newCachedThreadPool();
		while (true) {
			formData = null;
			getInfoOrNot = false;
			saveResponseOrNot = false;
			saveRequestOrNot = false;
			followRedirect = false;
			requestHeaders = null;
			url = "";
			method = "GET";
			responseFileName = "";
			pathToUploadFile = "";
			requestName = "";
			System.out.println("Enter Info: (Type help to get help about this App)");
			String info = scanner.nextLine();
			if (info.equals("exit")) {
				break;
			}
			if (info.equals("help")) {
				Request.help();
				continue;
			}
			if (info.contains("--list")) {
				Request.showRequests();
			} else if (info.contains("--fire")) {
				ArrayList<Request> existingReq = Request.getRequestsList();
				// split a info String that user entered
				// and then store a number of requests that user wants to fire :)
				String[] numberOfRequests = info.split("--fire")[1].trim().split(" ");

				for (int i = 0; i < numberOfRequests.length; i++) {
					int numberOfRequest = Integer.parseInt(numberOfRequests[i]) - 1;
					Request currentReq = existingReq.get(numberOfRequest);
					try {
						currentReq.initializeAndBuild();
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}

					exe.execute(currentReq);
				}
			} else {

				initializeFields(info);

				if (method.equals("POST")) {
					request = new POST();
				} else if (method.equals("GET")) {
					request = new GET();
				} else if (method.equals("DELETE")) {
					request = new DELETE();
				} else if (method.equals("PUT")) {
					request = new PUT();
				}

				if (request instanceof PUT) {
					try {
						((PUT) request).setFormData(formData);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				request.setRequestName(requestName);
				request.setUrl(url);
				request.setToGetInfo(getInfoOrNot);
				request.toSaveRequest(saveRequestOrNot);
				request.toSaveResponseAsFile(saveResponseOrNot);
				request.setFollowRedirect(followRedirect);
				request.setResponseFileName(responseFileName);
				request.setHeaders(requestHeaders);
				try {
					request.initializeAndBuild();
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
				exe.execute(request);

			}
		}
		scanner.close();
	}

	/**
	 * initialize request manager field
	 * 
	 * @param userInfo info that user entered
	 */
	public static void initializeFields(String userInfo) {
		if (userInfo.contains("-M") || userInfo.contains("--method")) {
			String[] temp = userInfo.split("-M|\\--method");
			String methodd = temp[1].trim().split(" ")[0];
			method = methodd;
			System.out.println("method: " + method);
		}
		if (userInfo.contains("-H") || userInfo.contains("--header")) {
			String[] temp = userInfo.split("-H|\\--header");
			String headers = temp[1].trim().split(" ")[0];
			String[] splitedHeaders = headers.split(":|\\;");
			requestHeaders = splitedHeaders;
		}
		if (userInfo.contains("-i")) {
			getInfoOrNot = true;
		}
		if (userInfo.contains("-O") || userInfo.contains("--output")) {
			saveResponseOrNot = true;
			String[] temp = userInfo.split("-O|\\--output");
			if (temp.length > 1) {
				if (!temp[1].trim().split(" ")[0].contains("-")) {
					responseFileName = temp[1].trim().split(" ")[0];
					System.out.println("responseName: " + responseFileName);
				}
			}
		}

		if (userInfo.contains("-D") || userInfo.contains("--data") || userInfo.contains("-U")
				|| userInfo.contains("--Upload")) {
			String[] splitedDataByAmpersand = userInfo.split("-D|\\--data|\\-U|\\\\--upload")[1].trim().split(" ")[0]
					.split("&");
			formData = new HashMap<Object, Object>();
			for (String keyValuePair : splitedDataByAmpersand) {
				String key = keyValuePair.split("=")[0];
				String value = keyValuePair.split("=")[1];
				formData.put(key, value);
			}
		}

		if (userInfo.contains("-f")) {
			followRedirect = true;
		}

		if (userInfo.contains("-U") || userInfo.contains("--upload")) {
			pathToUploadFile = userInfo.split("-U|\\--upload")[1].trim().split(" ")[0];
			System.out.println(pathToUploadFile);
		}

		if (userInfo.contains("-S") || userInfo.contains("--save")) {
			saveRequestOrNot = true;
		}

		url = userInfo.trim().split(" ")[1];
		requestName = userInfo.trim().split(" ")[0];
	}

}
