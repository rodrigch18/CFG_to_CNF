import java.util.*;
import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * CFG_to_CNF
 * 
 * @author Christian
 */
public class CFG_to_CNF
{
	
	public static class Converter
	{
		ArrayList<String> CNF =  new ArrayList<String>();
		
		public Converter()
		{
			this.CNF = new ArrayList<String>();
		}
		
		public ArrayList<String> convert(ArrayList<String> original)
		{
			Line[] temp = new Line[original.size()+1];
			
			
			for(int i = 0; i < original.size(); i++)
			{
				if(!Character.isLetter(original.get(i).charAt(0))){
					return CNF = null;
				}
				else if(!Character.isUpperCase(original.get(i).charAt(0))){
					return CNF = null;
				}
				else if(original.get(i).charAt(1) != '-')
				{
					return CNF = null;
				}
				else if (original.get(i).charAt(2) != '>')
				{
					return CNF = null;
				}
				
				temp[i+1] = new Line(original.get(i));
				
				//CNF.add(original.get(i));
			}
			
			//special case: Start Variable
			int first = temp[1].str.indexOf('>') + 1;
			if (temp[1].indexesOfDelimiters == null)
			{
				temp[1].sectionsOfLine.add(new Section(temp[1].str.substring(first)));
				if(temp[1].sectionsOfLine.get(0) == null)
				{
					CNF.add("S0->");
				}
				else if(temp[1].sectionsOfLine.get(0).characters.size() < 2)
				{
					CNF.add("S0->" + temp[1].sectionsOfLine.get(0).characters.get(0));
					if(!temp[1].sectionsOfLine.get(0).characters.get(0).equals('\\'))
					{
						CNF.add(original.get(0));
					}
					else{
						CNF.add(original.get(0).substring(0,original.get(0).indexOf('>')+1));
					}
				}
				
			}
			
			for(int i = 1; i < original.size(); i++)
			{
//				int begin = temp[i+1].str.indexOf('>') + 1;
//				if (temp[i+1].indexesOfDelimiters == null)
//				{
//					temp[i+1].sectionsOfLine.add(new Section(temp[i+1].str.substring(first)));
//				}
				CNF.add(original.get(i));
				
			}
			
			
			
			return CNF;
		}
		
	}
	
	public static class Line{
	    public String str;
	    ArrayList<Integer> indexesOfDelimiters;
	    ArrayList<Section> sectionsOfLine;
	    
	    public Line(String str){
	        this.str = str;
	        this.indexesOfDelimiters = getIndexOf('|');
	        this.sectionsOfLine = new ArrayList<Section>();
	    }
	    
	    public ArrayList<Integer> getIndexOf(char s)
		{
			ArrayList<Integer> indexList = new ArrayList<Integer>();

			int index = str.indexOf(s);
			while (index >= 0)
			{
				indexList.add(index);

				index = str.indexOf(str, index + 1);
			}// while
			
			if(index < 0)
			{
				return null;
			}

			return indexList;

		}// getIndexOfSuffix
	    
	}
	
	public static class Section{
	    ArrayList<Character> characters;
	    String part;
	    
	    public Section(String str)
	    {
	        this.part = str;
	        this.characters = getChars();
	    }
	    
	    public ArrayList<Character> getChars()
		{
			ArrayList<Character> charList = new ArrayList<Character>();

			if (part.length() <= 0)
			{
				return null;
			}
			for(int j = 0; j < part.length(); j++)
			{
				charList.add(part.charAt(j));
			}

			return charList;

		}// getIndexOfSuffix
	    
	}
	
	public static File fileSelector()
	{
		JFileChooser fc = new JFileChooser();
		JFrame frame = new JFrame();
		File selectedFile = null;
		fc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
		int result = fc.showOpenDialog(frame);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			selectedFile = fc.getSelectedFile();
			System.out.println("Selected file: " + selectedFile.getAbsolutePath());
		}
		else 
		{
			return null;
		}
		
		return selectedFile;
	}

	public static void main(String[] args)
	{
		File aFile = fileSelector();
		
		if(aFile == null)
		{
			System.err.println("No File Selected");
			System.exit(1);
		}
		BufferedReader bf;
		ArrayList<String> orig = null;
		String message;
		int count = 0;
		
		try
		{
			bf = new BufferedReader(new FileReader(aFile));
			orig = new ArrayList<String>();
			while ((message = bf.readLine()) != null)
			{
			     orig.add(message);
			     count++;
			}
			
			bf.close();
			if (count == 0){
				JOptionPane.showMessageDialog(null, "String was not of correct format", "Error", JOptionPane.ERROR_MESSAGE);
				
				System.exit(1);
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		Converter aConverter = new Converter();
		
		ArrayList<String> finalCNF = null;
		
		finalCNF = aConverter.convert(orig);
		
		if(finalCNF == null){
			JOptionPane.showMessageDialog(null, "String was not of correct format", "Error", JOptionPane.ERROR_MESSAGE);
			
			System.exit(1);
		}
		
		String finalString = "";
		
		for(int i = 0; i < finalCNF.size(); i++)
		{
			finalString = finalString + finalCNF.get(i) + "\n";
			System.out.println(finalCNF.get(i));
		}
		
		JOptionPane.showMessageDialog(null, "FINAL: "+ "\n" + finalString);
		
		System.exit(0);

	}//main

}
