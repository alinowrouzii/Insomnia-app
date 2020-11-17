package model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import view.CenterPanel;

public abstract class Request implements Runnable, Serializable {

	private static final long serialVersionUID = 5306069467430103747L;
	// request client that send request
	private transient HttpClient client;
	// client builder that build client
	private transient HttpClient.Builder clientBuilder;
	// request of HttpRequest
	protected transient HttpRequest request;
	// request builder that build request
	protected transient HttpRequest.Builder requestBuilder;
	// response of request
	private transient HttpResponse<InputStream> response;
	// url of request
	private String url;
	// headers of request
	private String[] requestHeaders;
	// name of request response file that will be saved
	private String responseFileName;
	// name of request
	private String requestName = "";
	// boolean to show info about request after response
	private boolean getInfoOrNot;
	// boolean to save request or not
	private boolean saveRequest;
	// boolean to save responseBody as a file
	private boolean saveResponseAsFile;
	// boolean to follow redirect or not
	private boolean followRedirect;
	// double number to store time of response
	private double responseTime;
	// boolean to figure out that request is built and runned or not
	private boolean isDone;
	// byte array to store response Body
	private byte[] responseBody;
	// map of string and list of string to store headers of response
	private Map<String, List<String>> resPonseHeaders;
	// int number to store status code of response
	private int statusCode;
	private String queryInfo = "";
	// path of response files
	private final static String FILE_PATH = "./files/";
	// path of requests object
	protected final static String REQ_PATH = "./requests/";
	private boolean isActive ;
	/**
	 * default constructor and create a new Request object
	 */
	public Request() {
		this.isActive = true ;
	}

	/**
	 * initialize some fields and then build client
	 */
	protected void initialize() {
		clientBuilder = HttpClient.newBuilder();
		requestBuilder = HttpRequest.newBuilder();
		if (requestHeaders != null && requestHeaders.length > 0) {
			requestBuilder.headers(requestHeaders);
		}
		if (followRedirect) {
			clientBuilder.followRedirects(Redirect.ALWAYS);
		} else {
			clientBuilder.followRedirects(Redirect.NEVER);
		}

		// temporary string to append question operand to first of query info string
		// NOTE : queryInfo string filed stored a queryInfo without question operand
		String query = "";
		if (queryInfo.length() > 0) {
			query = "?" + queryInfo;
		}
		requestBuilder.uri(URI.create(url.trim() + query));
		client = clientBuilder.build();
	}

	/**
	 * Set url of reuqest
	 * 
	 * @param url url of request
	 */
	public void setUrl(String url) {
		if (url != null) {
			this.url = url;
		}

	}

	/**
	 * abstract method to implements by Request subclasses
	 * 
	 * @throws IOException throws IOException when a problem come with work with
	 *                     file
	 */
	public abstract void initializeAndBuild() throws IOException;

	/**
	 * run a Runanable Request class
	 */
	@Override
	public void run() {
		long time1;
		try {
			time1 = System.currentTimeMillis();
			response = client.send(request, BodyHandlers.ofInputStream());

		} catch (IOException | InterruptedException e) {
			System.out.println("there is a problem in getting response from server!");
			e.printStackTrace();
			return;
		}
		// initialize body field
		initResponseBodyField();
		resPonseHeaders = response.headers().map();
		statusCode = response.statusCode();
		System.out.println(Util.byteToString(getResponseBody()));
		long time2 = System.currentTimeMillis();
		responseTime = time2 - time1;

		// then change the isDone field to true
		isDone = true;

		if (saveRequest) {
			saveRequest();
		}
		if (saveResponseAsFile) {
			saveResponseAsFile();
		}
		if (getInfoOrNot) {
			Map<String, List<String>> headers = getResponseHeaders();
			Set<String> allKeys = headers.keySet();
			for (String key : allKeys) {
				System.out.println(key + "= " + headers.get(key).toString());
			}
		}
	}

	/**
	 * get response body and save that in responseBodyFiled to access to use
	 * response body again and again
	 */
	private void initResponseBodyField() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (InputStream in = response.body()) {
			int read = 0;
			while ((read = in.read()) != -1) {
				baos.write(read);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		responseBody = baos.toByteArray();
	}

	/**
	 * Get response Headers
	 * 
	 * @return response headers
	 */
	public Map<String, List<String>> getResponseHeaders() {
		return resPonseHeaders;
	}

	/**
	 * save current request in specify path
	 */
	public void saveRequest() {
		try (FileOutputStream fos = new FileOutputStream(REQ_PATH + System.currentTimeMillis() / 100000000000.0 + ".bin");
				ObjectOutputStream objOut = new ObjectOutputStream(fos);) {
			objOut.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set requestHeaders
	 * 
	 * @param headers requestHeaders
	 */
	public void setHeaders(String[] headers) {
		this.requestHeaders = headers;
	}

	/**
	 * Set query info
	 * 
	 * @param query info of query
	 */
	public void setQueryInfo(String query) {
		if (query != null) {
			queryInfo = query;
		}
	}

	/**
	 * Get query info
	 * 
	 * @return returns String of query info
	 */
	public String getQueryInfo() {
		return queryInfo;
	}

	/**
	 * Get list of Requests
	 * 
	 * @return arrayList of requests
	 */
	public static ArrayList<Request> getRequestsList() {
		File[] requests = new File(REQ_PATH).listFiles();
		ArrayList<Request> res = new ArrayList<Request>();
		for (File file : requests) {
			try (FileInputStream fin = new FileInputStream(file); ObjectInputStream objIn = new ObjectInputStream(fin)) {
				Request temp = (Request) objIn.readObject();
				res.add(temp);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * Show all current request in console by methods
	 */
	public static void showRequests() {
		ArrayList<Request> exsitingRequests = getRequestsList();
		ArrayList<Request> postReq = new ArrayList<Request>();
		ArrayList<Request> getReq = new ArrayList<Request>();
		ArrayList<Request> putReq = new ArrayList<Request>();
		ArrayList<Request> deleteReq = new ArrayList<Request>();
		for (Request request : exsitingRequests) {
			if (request instanceof POST) {
				postReq.add(request);
			} else if (request instanceof GET) {
				getReq.add(request);
			} else if (request instanceof PUT) {
				putReq.add(request);
			} else if (request instanceof DELETE) {
				deleteReq.add(request);
			}
		}
		showRequests("GET", getReq);
		showRequests("POST", postReq);
		showRequests("PUT", putReq);
		showRequests("DELETE", deleteReq);
	}

	/**
	 * Show requests in console by specify method
	 */
	public static void showRequests(String method, ArrayList<Request> requests) {
		System.out.println(method + " requests: ");
		int counter = 1;
		for (Request req : requests) {
			System.out.print(counter++ + " . Request Name: " + req.getRequestName() + " | url: " + req.url);
			System.out.print(" | method: " + method);
			System.out.print(" | headers: ");
			if (req.requestHeaders != null) {
				int counterr = 0;
				for (String header : req.requestHeaders) {
					System.out.print(header);
					if (counterr++ % 2 == 0) {
						System.out.print(":");
					} else {
						System.out.print("; ");

					}
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * save Response as a file
	 */
	public void saveResponseAsFile() {
		// if responsefileName not settled yet, Set it now...
		setResponseFileName(responseFileName);

		try (FileOutputStream fos = new FileOutputStream(FILE_PATH + responseFileName)) {

			fos.write(responseBody);
			System.out.println("download completed!");
		} catch (FileNotFoundException e) {
			System.out.println("fileNot found!");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set response file name, if pass the null or empty String to this method then
	 * create a random name for response file
	 * 
	 * @param name name of file to save
	 */
	public void setResponseFileName(String name) {
		if (name != null && name.length() > 0) {
			this.responseFileName = name;
		} else {
			responseFileName = "output_" + System.currentTimeMillis() / 1000000000000.0;
		}
	}

	/**
	 * Get reponse code(Status code)
	 * 
	 * @return status code of response
	 */
	public int getResponseCode() {
		return statusCode;
	}

	/**
	 * Help user to work currently with this app
	 */
	public static void help() {
		System.out.println("use -M method or --m method to set the method of your request\r\n"
				+ "use -O or --output to save output with a default name\r\n"
				+ "use -O filename.type or --output filename.type to save output with your specify name\r\n"
				+ "use -S or --save to save your current request\r\n"
				+ "use -i to show headers of your request Response\r\n"
				+ "use -U or --upload to upload file with multipart form\r\n" + "use -D or --data to use form data\r\n"
				+ "use -f to set up yuor request to follow Redirect\r\n"
				+ "use -H or --header to set your request header\r\n" + "use --list to see your all saved request\r\n"
				+ "use --fire to fire your request\r\n"
				+ "at the first you should write your request name and then write url in console\r\n" + "\r\n"
				+ "Example request:\r\n"
				+ "myreq http://httpbin.org/post -M POST -U myfile=F:\\test.txt&ali=nowrouzi -i -S -H User-Agent:GOD -O ali.txt\r\n"
				+ "\r\n" + "Example request:\r\n"
				+ "putrequest http://httpbin.org/put -M PUT -U myfile=F:\\test.txt&ali=nowrouzi -i -S -H User-Agent:Ali -O\r\n"
				+ "\r\n" + "Example request:\r\n" + "req http://httpbin.org/get -M GET -i -S\r\n" + "\r\n"
				+ "Example request:\r\n"
				+ "myRequest http://httpbin.org/put -M PUT -D user=myusername&pass=myPassword -i -S -H User-Agent:GOD;myNameIs:Ali -O\r\n"
				+ "\r\n");
	}

	/**
	 * Get length of response body
	 * 
	 * @return length of response body
	 */
	public long getResponseBodyLength() {
		try {
			return responseBody.length;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Return true if the run method of this request called before
	 * 
	 * @return true if run method called before
	 */
	public boolean isDone() {
		return isDone;
	}

	/**
	 * Get response time mills
	 * 
	 * @return response time mills
	 */
	public double getResponseTimeMills() {
		return responseTime;
	}

	/**
	 * Get response body as a byte array
	 * 
	 * @return response body
	 */
	public byte[] getResponseBody() {
		return responseBody;
	}

	/**
	 * Set following redirect
	 * 
	 * @param followRedirect follow Redirect
	 */
	public void setFollowRedirect(boolean followRedirect) {
		this.followRedirect = followRedirect;
	}

	/**
	 * Set to get info in console or not
	 * 
	 * @param bool boolean to get info
	 */
	public void setToGetInfo(boolean bool) {
		getInfoOrNot = bool;
	}

	/**
	 * Set to save Request or not
	 * 
	 * @param bool boolean to save request
	 */
	public void toSaveRequest(boolean bool) {
		saveRequest = bool;
	}

	/**
	 * Set to save response as a file or not
	 * 
	 * @param bool boolean to save response as a file
	 */
	public void toSaveResponseAsFile(boolean bool) {
		saveResponseAsFile = bool;
	}

	/**
	 * Static method to create Request instannce with specify method
	 * 
	 * @param requestMethod method of request
	 * @return Object of Request subclasses with specify method
	 */
	public static Request createRequest(String requestMethod) {
		if (requestMethod.equals("POST")) {
			return new POST();
		} else if (requestMethod.equals("GET")) {
			return new GET();
		} else if (requestMethod.equals("PUT")) {
			return new PUT();
		} else if (requestMethod.equals("DELETE")) {
			return new DELETE();
		}
		return new GET();
	}

	/**
	 * Set name of request
	 * 
	 * @param requestName name of request
	 */
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	/**
	 * get name of request
	 * 
	 * @return name of request
	 */
	public String getRequestName() {
		return requestName;
	}

	/**
	 * Get url
	 * 
	 * @return url of request
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Get request headers
	 * 
	 * @return request headers
	 */
	public String[] getRequestHeaders() {
		return requestHeaders;
	}

	public boolean isActive() {
		return isActive;
	}
	public void setActivity(boolean bool) {
		this.isActive = bool ;
	}
}
