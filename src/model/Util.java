package model;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import view.HeaderPanel;

public class Util {

	/**
	 * Sava file to given path
	 * 
	 * @param files               byte array that contains content of file
	 * @param absolutePathAndName name and path of file in string
	 */
	public static void saveFileToCertainPath(byte[] files, String absolutePathAndName) {
		try (FileOutputStream fout = new FileOutputStream(new File(absolutePathAndName))) {
			fout.write(files);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * copy headers to clipBoard
	 * 
	 * @param responseHeadersList arrayList HeaderPanel that contains name and value
	 *                            of header
	 */
	public static void saveHeadersToClipBoard(ArrayList<HeaderPanel> responseHeadersList) {
		StringBuilder result = new StringBuilder();
		for (HeaderPanel header : responseHeadersList) {
			result.append(header.getNameText() + ":");
			result.append(header.getValueText() + ";\n");
		}
		StringSelection selection = new StringSelection(result.toString());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, null);
	}

	/**
	 * Convert byte array to string
	 * 
	 * @param bytes bytes array
	 * @return returns converted string
	 */
	public static String byteToString(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			result.append((char) bytes[i]);
		}
		return result.toString();
	}

	/**
	 * Convert byteArray to image
	 * 
	 * @param bytes bytes array that contains contents of image
	 * @return returns image
	 * @throws IOException throws this exception when any problem comes with work
	 *                     with file
	 */
	public static Image byteToImage(byte[] bytes) throws IOException {
		ByteArrayInputStream byteout = new ByteArrayInputStream(bytes);
		Image image = ImageIO.read(byteout);
		return image;
	}

	/**
	 * Convert arrayList of Header Panel to map of objects
	 * 
	 * @param headers headers to convert
	 * @return converted map of objects
	 */
	public static Map<Object, Object> convertToMap(ArrayList<HeaderPanel> headers) {
		Map<Object, Object> mappedHeaders = new HashMap<Object, Object>();
		for (HeaderPanel header : headers) {
			if (!header.isEmpty() && header.isActive()) {
				mappedHeaders.put(header.getNameText(), header.getValueText());
			}
		}
		return mappedHeaders;
	}

	/**
	 * Copy passed text to clipBoard
	 * 
	 * @param text text to copy
	 */
	public static void copyTextToClipBoard(String text) {
		StringSelection selection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, null);

	}

	/**
	 * Check that string in absoulute path or not
	 * 
	 * @param str str to check
	 * @return returns true if passed string be path
	 */
	public static boolean isPath(String str) {
		if (str.contains(":\\")) {
			return true;
		}
		return false;
	}

	/**
	 * Convert ArrayList that contains query headers to string that likes query
	 * style
	 * 
	 * @param queryInfo array list of query headers
	 * @return returns string of query style
	 */
	public static String convertToQueryInfo(ArrayList<HeaderPanel> queryInfo) {
		String result = "";
		for (HeaderPanel query : queryInfo) {
			if (!query.isEmpty() && query.isActive()) {
				result = result + query.getNameText() + "=" + query.getValueText() + "&";
			}
		}

		// delete latest ampersand operand
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 1);
		}
		System.out.println(result);
		return (result.length() > 0) ?  result : "";
	}
}
