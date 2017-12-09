package test;

import java.awt.JobAttributes;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileChooserTest {
	
	public static void main(String[] args) {
		JFileChooser jFileChooser = new JFileChooser();
		
		String optionalPath = "";
		jFileChooser.setSelectedFile(new File(optionalPath+"fileToSave.txt"));
		int retVal = jFileChooser.showSaveDialog(null);
		
		if(retVal == JFileChooser.APPROVE_OPTION){
			
			JOptionPane.showMessageDialog(null, "Path: " + jFileChooser.getSelectedFile().getPath()+" Name: "+ jFileChooser.getSelectedFile().getName());
		}
		
		
	}

}
