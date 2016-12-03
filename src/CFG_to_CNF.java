import java.util.*;
import java.io.*;
import java.lang.reflect.Array;

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
		char[] Alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		ArrayList<Character> currectChar = new ArrayList<Character>();
		ArrayList<String> CNF =  new ArrayList<String>();
		
		public Converter()
		{
			this.CNF = new ArrayList<String>();
		}
		
		public ArrayList<String> convert(ArrayList<String> original)
		{
			Line[] temp = new Line[original.size()];
			
			
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
				
				temp[i] = new Line(original.get(i));
				
				for(int l = 0; l < original.get(i).length(); i++){
					if(Character.isUpperCase(original.get(i).charAt(l))){
						if(!currectChar.contains(original.get(i).charAt(l))){
							currectChar.add(original.get(i).charAt(l));
						}
						
					}
				}
				
				//CNF.add(original.get(i));
			}
			
			//special case: Start Variable
			int first = temp[0].str.indexOf('>') + 1;
			if (temp[0].indexesOfDelimiters == null)// without delimiters
			{
				temp[0].sectionsOfLine.add(new Section(temp[0].str.substring(first)));
				
				if(temp[0].sectionsOfLine.get(0) == null)
				{
					CNF.add("S->");
				}
				else if(temp[0].sectionsOfLine.get(0).characters.size() < 2)
				{
					CNF.add("S->" + temp[0].sectionsOfLine.get(0).characters.get(0));
					
					if(!temp[0].sectionsOfLine.get(0).characters.get(0).equals('\\'))
					{
						CNF.add(original.get(0));
					}
					else{
						CNF.add(original.get(0).substring(0,original.get(0).indexOf('>')+1));
					}
				}
				else if(temp[0].sectionsOfLine.get(0).characters.size() > 2 && temp[0].sectionsOfLine.get(0).characters.size() < 4)
				{
					
				}
				
			}
			else// with delimiters
			{
				for(int k = 0; k < temp[0].indexesOfDelimiters.size(); k++)
				{
					if(k == 0)
					{
						temp[0].sectionsOfLine.add(new Section(temp[0].str.substring(first, temp[0].indexesOfDelimiters.get(k))));
					}
					else
					{
						temp[0].sectionsOfLine.add(new Section(temp[0].str.substring(temp[0].indexesOfDelimiters.get(k-1), temp[0].indexesOfDelimiters.get(k))));
					}
				}
			}
			
			char tempChar = '0';
			
			for(int i = original.size()-1; i > 0; i--)
			{
				int begin = temp[i].str.indexOf('>') + 1;
				for(int k = 0; k < temp[0].indexesOfDelimiters.size(); k++)
				{
					if(k == 0)
					{
						temp[i].sectionsOfLine.add(new Section(temp[i].str.substring(begin, temp[i].indexesOfDelimiters.get(k))));
					}
					else
					{
						temp[i].sectionsOfLine.add(new Section(temp[i].str.substring(temp[i].indexesOfDelimiters.get(k-1), temp[i].indexesOfDelimiters.get(k))));
					}
				}
				
				for(int j = 0; j < temp[i].sectionsOfLine.size(); j++)
				{
					for(int p = 0; p < temp[i].sectionsOfLine.get(j).characters.size(); p++)
					{
						if(temp[i].sectionsOfLine.get(j).characters.get(p) == '\\')
						{
							tempChar = temp[i].str.charAt(0);
							
						}
					}
				}
				if(tempChar != '0')
				{
					for(int k = 0; k < original.size(); k++)
					{
						for(int j = 0; j < temp[k].sectionsOfLine.size(); j++)
						{
							if(temp[k].sectionsOfLine.get(j).characters.contains(tempChar))
							{
								int tempIndex = temp[k].sectionsOfLine.get(j).characters.indexOf(tempChar);
								
								if(tempIndex == 0 && temp[k].sectionsOfLine.get(j).characters.size() == 1){
									temp[k].sectionsOfLine.add(new Section("\\"));
								}
								else if(tempIndex == 0 && temp[k].sectionsOfLine.get(j).characters.size() == 2)
								{
									String tempString = temp[k].sectionsOfLine.get(j).characters.toString().replaceAll("[,\\s\\[\\]]", "");
									temp[k].sectionsOfLine.add(new Section("" + tempString.substring(1)));
								}
								else if(tempIndex != 0 && temp[k].sectionsOfLine.get(j).characters.size() == 2)
								{
									String tempString = temp[k].sectionsOfLine.get(j).characters.toString().replaceAll("[,\\s\\[\\]]", "");
									temp[k].sectionsOfLine.add(new Section("" + tempString.substring(0, 1)));
								}
								else if(tempIndex != 0 && temp[k].sectionsOfLine.get(j).characters.size() > 2)
								{
									String tempString = temp[k].sectionsOfLine.get(j).characters.toString().replaceAll("[,\\s\\[\\]]", "");
									temp[k].sectionsOfLine.add(new Section("" + tempString.substring(0, tempIndex) + tempString.substring(tempIndex+1)));
								}
							}
						}
					}
				}	
			}
			
			
			//CNF.add(original.get(i));
			
			return CNF;
		}// Convert
		
		public char getNextChar()
		{
			char nextChar = 'A';
			int count = 0;
			
			while(count < Alphabet.length){
				if(currectChar.contains(nextChar)){
					nextChar = Alphabet[count];
					count++;
				}
				else
				{
					return nextChar;
				}
			}
			
			
			return '0';
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
