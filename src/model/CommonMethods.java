package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CommonMethods {

	public String getDateTime() {

		DateFormat dateFormat = new SimpleDateFormat("yyMMdd_hhmmss");
		Date date = new Date();
		String dateTime = dateFormat.format(date);
		return dateTime;

	}

	public String getSaveFile(String path, String defaultName, String message) {

		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setMultiSelectionEnabled(false);
		jFileChooser.setDialogTitle(message);
		jFileChooser.setFileSelectionMode(jFileChooser.FILES_ONLY);
		jFileChooser.setSelectedFile(new File(path + defaultName));
		
		int retVal = jFileChooser.showSaveDialog(null);

		if (retVal == JFileChooser.APPROVE_OPTION) {

//			JOptionPane.showMessageDialog(null, "Path: " + jFileChooser.getSelectedFile().getPath() + " Name: "
//					+ jFileChooser.getSelectedFile().getName());
			return jFileChooser.getSelectedFile().getPath();
			
		}else{
			
			return "";
		}
		
	}

	
	public String getFileName(String filePath){
		
		if(filePath.equals(""))
			return "";		
		String segments[] = filePath.split(Pattern.quote("\\"));
		int length = segments.length;
		
		if(length==0){
			return "";
		}else{
			
			return segments[length-1];
		}	
	}
	
	public boolean isIntParseable(String numString){
		
		try {	
			int t = Integer.parseInt(numString);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public int getInt(String numString){
		return Integer.parseInt(numString);
	}
	
	
	public boolean isDoubleParseable(String numString){
		
		try {	
			Double t = Double.parseDouble(numString);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public double getDouble(String numString){
		return Double.parseDouble(numString);
	}
	
	
	public String getFilePath(String msg) {

		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setDialogTitle(msg);
		fileChooser.setFileSelectionMode(fileChooser.FILES_ONLY);

		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();
			String path = file.getPath();
			return path;

		} else {
			return "";
		}

	}

	public String getDirectoryPath(String msg) {

		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setDialogTitle(msg);
		fileChooser.setFileSelectionMode(fileChooser.DIRECTORIES_ONLY);

		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();
			String path = file.getPath();
			return path;

		} else {
			return "";
		}
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

	public boolean hasEmptyString(ArrayList<String> stringList) {

		for (String string : stringList) {

			if (string.equals(""))
				return true;
		}
		return false;
	}

	public boolean writeFile(String filePathName, String data) {

		PrintWriter out;
		try {
			out = new PrintWriter(filePathName);
			out.println(data);
			out.close();
			return true;
		} catch (FileNotFoundException error) {
			JOptionPane.showMessageDialog(null, "File Saving Failed");
			return false;
		}
	}

	public String readFile(String filePath) {

		return null;
	}

}
