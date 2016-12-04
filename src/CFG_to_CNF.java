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
		char[] Alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		ArrayList<Character> currectChar = new ArrayList<Character>();
		ArrayList<String> CNF = new ArrayList<String>();
		int size;

		public Converter()
		{
			this.CNF = new ArrayList<String>();
		}

		public ArrayList<String> convert(ArrayList<String> original)
		{
			ArrayList<Line> temp = new ArrayList<Line>();
			size = original.size();

			for (int i = 0; i < original.size(); i++)
			{
				if (!Character.isLetter(original.get(i).charAt(0)))
				{
					return CNF = null;
				} 
				else if (!Character.isUpperCase(original.get(i).charAt(0)))
				{
					return CNF = null;
				} 
				else if (original.get(i).charAt(1) != '-')
				{
					return CNF = null;
				} 
				else if (original.get(i).charAt(2) != '>')
				{
					return CNF = null;
				}
				else if (original.get(0).charAt(3) == '\\')
				{
					CNF.add("S0->\\");
					String One = original.get(0).substring(0, original.get(0).indexOf('>') + 1);
					CNF.add(One);
					return CNF;
				}

				temp.add(new Line(original.get(i)));

				for (int l = 0; l < original.get(i).length(); l++)
				{
					if (Character.isUpperCase(original.get(i).charAt(l)))
					{
						if (!currectChar.contains(original.get(i).charAt(l)))
						{
							currectChar.add(original.get(i).charAt(l));
						}

					}
				}
			}

			// Populate Section of Line array with each lines respective sections
			for (int i = temp.size() - 1; i >= 0; i--)
			{
				int begin = temp.get(i).str.indexOf('>') + 1;

				if (temp.get(i).indexesOfDelimiters.size() == 0)
				{
					temp.get(i).sectionsOfLine.add(new Section(temp.get(i).str.substring(begin)));
				} 
				else if (temp.get(i).indexesOfDelimiters.size() == 1)
				{
					temp.get(i).sectionsOfLine
					.add(new Section(temp.get(i).str.substring(begin, temp.get(i).indexesOfDelimiters.get(0))));
					temp.get(i).sectionsOfLine
					.add(new Section(temp.get(i).str.substring(temp.get(i).indexesOfDelimiters.get(0) + 1)));
				} 
				else if (temp.get(i).indexesOfDelimiters.size() > 1)
				{
					temp.get(i).sectionsOfLine
					.add(new Section(temp.get(i).str.substring(begin, temp.get(i).indexesOfDelimiters.get(0))));
					temp.get(i).sectionsOfLine
					.add(new Section(temp.get(i).str.substring(temp.get(i).indexesOfDelimiters.get(0) + 1)));
					for (int k = 1; k < temp.get(i).indexesOfDelimiters.size(); k++)
					{
						temp.get(i).sectionsOfLine
						.add(new Section(temp.get(i).str.substring(temp.get(i).indexesOfDelimiters.get(k - 1) + 1,
								temp.get(i).indexesOfDelimiters.get(k))));
					}
				}
			}


			//Rule for no Epsilons
			char tempChar = '0';
			boolean flag = false;
			boolean check = true;
			while (checkForEpsilon(temp) && check)
			{
				for (int i = temp.size() - 1; i >= 0; i--)
				{
					for (int j = 0; j < temp.get(i).sectionsOfLine.size(); j++)
					{
						int tempIndex = temp.get(i).sectionsOfLine.get(j).characters.indexOf('\\');

						if(i == 0)
						{
							temp.get(i).sectionsOfLine.remove(j);
							check = false;
						}

						if (tempIndex != -1)
						{
							tempChar = temp.get(i).str.charAt(0);
							temp.get(i).sectionsOfLine.remove(j);
							size = temp.size();
							flag = true;
							break;
						}
					}
					if (flag || !check)
					{
						flag = false;
						break;
					}
				}

				if (tempChar != '0')
				{
					for (int k = 0; k < temp.size(); k++)
					{
						for (int j = 0; j < temp.get(k).sectionsOfLine.size(); j++)
						{
							if (temp.get(k).sectionsOfLine.get(j).characters.contains(tempChar))
							{
								int tempIndex = temp.get(k).sectionsOfLine.get(j).characters.indexOf(tempChar);
								if (tempIndex != -1)
								{
									if (tempIndex == 0 && temp.get(k).sectionsOfLine.get(j).characters.size() == 1)
									{
										if(k != 0){
											temp.get(k).sectionsOfLine.add(new Section("\\"));
										}

									} 
									else if (tempIndex == 0 && temp.get(k).sectionsOfLine.get(j).characters.size() == 2)
									{
										String tempString = temp.get(k).sectionsOfLine.get(j).characters.toString()
												.replaceAll("[,\\s\\[\\]]", "");
										temp.get(k).sectionsOfLine.add(new Section("" + tempString.substring(1)));

									} 
									else if (tempIndex != 0 && temp.get(k).sectionsOfLine.get(j).characters.size() == 2)
									{
										String tempString = temp.get(k).sectionsOfLine.get(j).characters.toString()
												.replaceAll("[,\\s\\[\\]]", "");
										temp.get(k).sectionsOfLine.add(new Section("" + tempString.substring(0, 1)));

									} 
									else if (temp.get(k).sectionsOfLine.get(j).characters.size() > 2)
									{
										String tempString = temp.get(k).sectionsOfLine.get(j).characters.toString()
												.replaceAll("[,\\s\\[\\]]", "");

										ArrayList<Integer> indexListOfTempChar = new ArrayList<Integer>();

										int index = tempString.indexOf(tempChar);
										while (index >= 0)
										{
											indexListOfTempChar.add(index);

											index = tempString.indexOf(tempChar, index + 1);


										} // while

										if(indexListOfTempChar.size() == 1)
										{
											if(tempIndex == 0)
											{
												temp.get(k).sectionsOfLine.add(new Section("" + tempString.substring(1)));
											}
											else if(tempIndex != 0 && tempIndex == tempString.length())
											{
												temp.get(k).sectionsOfLine.add(new Section("" + tempString.substring(0, tempIndex)));
											}
											else if(tempIndex != 0)
											{
												temp.get(k).sectionsOfLine.add(new Section("" + tempString.substring(0, tempIndex) + tempString.substring(tempIndex+1)));
											}
										}

										if(indexListOfTempChar.size() > 1)
										{
											if(indexListOfTempChar.get(0) != tempString.charAt(0))
											{
												temp.get(k).sectionsOfLine.add(new Section("" + tempString.substring(0, indexListOfTempChar.get(1))));
											}

											for(int t = 0; t < indexListOfTempChar.size(); t++)
											{
												if(t+1 < indexListOfTempChar.size())
												{
													temp.get(k).sectionsOfLine.add(new Section("" + tempString.substring(indexListOfTempChar.get(t), indexListOfTempChar.get(t+1))));
													temp.get(k).sectionsOfLine.add(new Section("" + tempString.substring(indexListOfTempChar.get(t)+1, indexListOfTempChar.get(t+1)+1)));
												}
												else
												{
													if(tempString.charAt(indexListOfTempChar.get(t)) != tempString.charAt(tempString.length()-1))
													{
														temp.get(k).sectionsOfLine.add(new Section("" + tempString.substring(indexListOfTempChar.get(t))));
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			//Remove duplicates
			for (int i = 0; i < temp.size(); i++)
			{
				for (int j = 0; j < temp.get(i).sectionsOfLine.size(); j++)
				{
					String sti = temp.get(i).sectionsOfLine.get(j).part;

					for (int k = j+1; k < temp.get(i).sectionsOfLine.size(); k++)
					{
						if(sti.equals(temp.get(i).sectionsOfLine.get(k).part))
						{
							temp.get(i).sectionsOfLine.remove(k);
						}
					}
				}
				if(temp.get(i).sectionsOfLine.size() > 1){
					if(temp.get(i).sectionsOfLine.get(temp.get(i).sectionsOfLine.size()-1).part.equals(temp.get(i).sectionsOfLine.get(temp.get(i).sectionsOfLine.size()-2).part))
					{
						temp.get(i).sectionsOfLine.remove(temp.get(i).sectionsOfLine.size()-1);
					}
				}
			}

			//Remove Start Variable from Variables to lead to it
			for (int i = 0; i < temp.size(); i++)
			{
				for (int j = 0; j < temp.get(i).sectionsOfLine.size(); j++)
				{
					if (i == 0){
						if(temp.get(i).sectionsOfLine.get(j).part.length() == 1)
						{
							if(temp.get(i).sectionsOfLine.get(j).part.charAt(0) == temp.get(0).str.charAt(0))
							{
								temp.get(i).sectionsOfLine.remove(j);
							}
						}
					}
					else
					{
						if(temp.get(i).sectionsOfLine.get(j).part.length() == 1)
						{
							if(temp.get(i).sectionsOfLine.get(j).part.charAt(0) == temp.get(0).str.charAt(0))
							{
								temp.get(i).sectionsOfLine.remove(j);
								temp.get(i).sectionsOfLine.addAll(temp.get(0).sectionsOfLine);
							}
						}
					}
				}
			}

			//Remove Sections of 3 Characters or More
			for (int i = 0; i < temp.size(); i++)
			{
				for (int j = 0; j < temp.get(i).sectionsOfLine.size(); j++)
				{
					if(temp.get(i).sectionsOfLine.get(j).part.length() > 2)
					{
						int math = temp.get(i).sectionsOfLine.get(j).part.length()/2;
						int count = 0;
						while(count < math)
						{
							char newCharLHS = getNextChar();

							temp.add(new Line("" + newCharLHS + "->" + temp.get(i).sectionsOfLine.get(j).part.substring(temp.get(i).sectionsOfLine.get(j).part.length()-2 - (count*2), temp.get(i).sectionsOfLine.get(j).part.length() - (count*2))));

							String sample = temp.get(i).sectionsOfLine.get(j).part.substring(temp.get(i).sectionsOfLine.get(j).part.length()-2 - (count*2), temp.get(i).sectionsOfLine.get(j).part.length() - (count*2));


							temp.get(i).sectionsOfLine.set(j, new Section("" + temp.get(i).sectionsOfLine.get(j).part.substring(0, math*2 -1) + newCharLHS));

							for (int q = 0; q < temp.size(); q++)
							{
								for (int k = 0; k < temp.get(q).sectionsOfLine.size(); k++)
								{
									if(temp.get(q).sectionsOfLine.get(k).part.length() > 2)
									{

										if(temp.get(q).sectionsOfLine.get(k).part.contains(sample))
										{
											temp.get(q).sectionsOfLine.set(k, new Section("" + temp.get(q).sectionsOfLine.get(k).part.substring(0, math*2 -1) + newCharLHS));
										}

									}
								}
							}



							for (int b = 0; b < temp.size(); b++)
							{
								for (int l = 0; l < temp.get(b).str.length(); l++)
								{
									if (Character.isUpperCase(temp.get(b).str.charAt(l)))
									{
										if (!currectChar.contains(temp.get(b).str.charAt(l)))
										{
											currectChar.add(temp.get(b).str.charAt(l));
										}

									}
								}
							}

							int begin = temp.get(temp.size()-1).str.indexOf('>') + 1;


							if (temp.get(temp.size()-1).indexesOfDelimiters.size() == 0)
							{
								temp.get(temp.size()-1).sectionsOfLine.add(new Section(temp.get(temp.size()-1).str.substring(begin)));
							} 
							else if (temp.get(temp.size()-1).indexesOfDelimiters.size() == 1)
							{
								temp.get(temp.size()-1).sectionsOfLine
								.add(new Section(temp.get(temp.size()-1).str.substring(begin, temp.get(temp.size()-1).indexesOfDelimiters.get(0))));
								temp.get(temp.size()-1).sectionsOfLine
								.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(0) + 1)));
							} 
							else if (temp.get(temp.size()-1).indexesOfDelimiters.size() > 1)
							{
								temp.get(temp.size()-1).sectionsOfLine
								.add(new Section(temp.get(temp.size()-1).str.substring(begin, temp.get(temp.size()-1).indexesOfDelimiters.get(0))));
								temp.get(temp.size()-1).sectionsOfLine
								.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(0) + 1)));
								for (int k = 1; k < temp.get(temp.size()-1).indexesOfDelimiters.size(); k++)
								{
									temp.get(temp.size()-1).sectionsOfLine
									.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(k - 1) + 1,
											temp.get(temp.size()-1).indexesOfDelimiters.get(k))));
								}
							}
							count++;
						}
					}
				}
			}


			for (int i = 0; i < temp.size(); i++)
			{
				for (int j = 0; j < temp.get(i).sectionsOfLine.size(); j++)
				{
					if(temp.get(i).sectionsOfLine.get(j).part.length() == 1)
					{
						if(Character.isUpperCase(temp.get(i).sectionsOfLine.get(j).characters.get(0)))
						{
							for (int h = 0; h < temp.size(); h++)
							{
								if(temp.get(h).str.charAt(0) == temp.get(i).sectionsOfLine.get(j).characters.get(0))
								{
									if(temp.get(h).sectionsOfLine.size() == 1)
									{
										if (temp.get(h).sectionsOfLine.get(0).part.length() == 1)
										{
											if (Character.isLowerCase(temp.get(h).sectionsOfLine.get(0).part.charAt(0)))
											{
												temp.get(i).sectionsOfLine.set(j, new Section("" + temp.get(h).sectionsOfLine.get(0).part.charAt(0)));
											}
										}
									}
								}
							}
						}
					}
					if(temp.get(i).sectionsOfLine.get(j).part.length() == 2)
					{
						boolean upper = false;
						boolean lower = false;
						int lowerIndex = -1;
						for (int k = 0; k < temp.get(i).sectionsOfLine.get(j).characters.size(); k++)
						{
							if(Character.isUpperCase(temp.get(i).sectionsOfLine.get(j).characters.get(k)))
							{
								upper = true;

							}
							if(Character.isLowerCase(temp.get(i).sectionsOfLine.get(j).characters.get(k)))
							{
								lower = true;
								lowerIndex = k;
							}
						}
						if(upper && lower)
						{
							char newCharLHS = getNextChar();

							char sample = '0';


							temp.add(new Line("" + newCharLHS + "->" + temp.get(i).sectionsOfLine.get(j).characters.get(lowerIndex)));
							sample = temp.get(i).sectionsOfLine.get(j).characters.get(lowerIndex);



							if(lowerIndex == 0)
							{

								temp.get(i).sectionsOfLine.set(j, new Section("" + newCharLHS + temp.get(i).sectionsOfLine.get(j).part.substring(1)));
							}
							else
							{
								temp.get(i).sectionsOfLine.set(j, new Section("" + temp.get(i).sectionsOfLine.get(j).part.substring(0, 1) + newCharLHS));
							}

							upper = false;
							lower = false;

							for (int m = 0; m < temp.size(); m++)
							{
								for (int n = 0; n < temp.get(m).sectionsOfLine.size(); n++)
								{
									if(temp.get(m).sectionsOfLine.get(n).part.length() == 2 && (temp.get(m).sectionsOfLine.get(n).characters.get(0) == sample || temp.get(m).sectionsOfLine.get(n).characters.get(1) == sample))
									{

										boolean upper1 = false;
										boolean lower1 = false;
										int lowerIndex1 = -1;
										for (int k = 0; k < temp.get(m).sectionsOfLine.get(n).characters.size(); k++)
										{

											if(Character.isUpperCase(temp.get(m).sectionsOfLine.get(n).characters.get(k)))
											{

												upper1 = true;

											}
											if(Character.isLowerCase(temp.get(m).sectionsOfLine.get(n).characters.get(k)))
											{

												lower1 = true;
												lowerIndex1 = k;

											}
										}
										if(upper1 && lower1)
										{

											if(lowerIndex1 == 0)
											{

												temp.get(m).sectionsOfLine.set(n, new Section("" + newCharLHS + temp.get(m).sectionsOfLine.get(n).part.substring(1)));
											}
											else
											{
												temp.get(m).sectionsOfLine.set(n, new Section("" + temp.get(m).sectionsOfLine.get(n).part.substring(0, 1) + newCharLHS));
											}

										}

									}
									upper = false;
									lower = false;
								}
							}



							for (int b = 0; b < temp.size(); b++)
							{
								for (int l = 0; l < temp.get(b).str.length(); l++)
								{
									if (Character.isUpperCase(temp.get(b).str.charAt(l)))
									{
										if (!currectChar.contains(temp.get(b).str.charAt(l)))
										{
											currectChar.add(temp.get(b).str.charAt(l));
										}

									}
								}
							}

							int begin = temp.get(temp.size()-1).str.indexOf('>') + 1;


							if (temp.get(temp.size()-1).indexesOfDelimiters.size() == 0)
							{
								temp.get(temp.size()-1).sectionsOfLine.add(new Section(temp.get(temp.size()-1).str.substring(begin)));
							} 
							else if (temp.get(temp.size()-1).indexesOfDelimiters.size() == 1)
							{
								temp.get(temp.size()-1).sectionsOfLine
								.add(new Section(temp.get(temp.size()-1).str.substring(begin, temp.get(temp.size()-1).indexesOfDelimiters.get(0))));
								temp.get(temp.size()-1).sectionsOfLine
								.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(0) + 1)));
							} 
							else if (temp.get(temp.size()-1).indexesOfDelimiters.size() > 1)
							{
								temp.get(temp.size()-1).sectionsOfLine
								.add(new Section(temp.get(temp.size()-1).str.substring(begin, temp.get(temp.size()-1).indexesOfDelimiters.get(0))));
								temp.get(temp.size()-1).sectionsOfLine
								.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(0) + 1)));
								for (int k = 1; k < temp.get(temp.size()-1).indexesOfDelimiters.size(); k++)
								{
									temp.get(temp.size()-1).sectionsOfLine
									.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(k - 1) + 1,
											temp.get(temp.size()-1).indexesOfDelimiters.get(k))));
								}
							}
						}
						
						int found0m = -1;
						int found0n = -1;
						int found1m = -1;
						int found1n = -1;
						if(!upper && lower)
						{
							for (int m = 0; m < temp.size(); m++)
							{
								for (int n = 0; n < temp.get(m).sectionsOfLine.size(); n++)
								{
									if(temp.get(m).sectionsOfLine.get(n).part.length() == 1 && temp.get(m).sectionsOfLine.size() == 1)
									{
										if(temp.get(m).sectionsOfLine.get(n).characters.get(0) == temp.get(i).sectionsOfLine.get(j).characters.get(0))
										{
											found0m = m;
											found0n = n;
										}
										if (temp.get(m).sectionsOfLine.get(n).characters.get(0) == temp.get(i).sectionsOfLine.get(j).characters.get(1))
										{
											found1m = m;
											found1n = n;
										}

									}
								}
							}
							
							if(found0m != -1)
							{
								
								temp.get(i).sectionsOfLine.set(j, new Section("" + temp.get(found0m).str.charAt(0) + temp.get(i).sectionsOfLine.get(j).part.substring(1)));
								
							}
							else
							{
								char newChar = getNextChar();
								temp.get(i).sectionsOfLine.set(j, new Section("" + newChar + temp.get(i).sectionsOfLine.get(j).part.substring(1)));
								
								for (int b = 0; b < temp.size(); b++)
								{
									for (int l = 0; l < temp.get(b).sectionsOfLine.size(); l++)
									{
										for (int g = 0; g < temp.get(b).sectionsOfLine.get(l).characters.size(); l++)
										{
											if (Character.isUpperCase(temp.get(b).sectionsOfLine.get(l).characters.get(g)))
											{
												if (!currectChar.contains(temp.get(b).sectionsOfLine.get(l).characters.get(g)))
												{
													currectChar.add(temp.get(b).sectionsOfLine.get(l).characters.get(g));
												}
	
											}
										}
									}
								}

								int begin = temp.get(temp.size()-1).str.indexOf('>') + 1;


								if (temp.get(temp.size()-1).indexesOfDelimiters.size() == 0)
								{
									temp.get(temp.size()-1).sectionsOfLine.add(new Section(temp.get(temp.size()-1).str.substring(begin)));
								} 
								else if (temp.get(temp.size()-1).indexesOfDelimiters.size() == 1)
								{
									temp.get(temp.size()-1).sectionsOfLine
									.add(new Section(temp.get(temp.size()-1).str.substring(begin, temp.get(temp.size()-1).indexesOfDelimiters.get(0))));
									temp.get(temp.size()-1).sectionsOfLine
									.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(0) + 1)));
								} 
								else if (temp.get(temp.size()-1).indexesOfDelimiters.size() > 1)
								{
									temp.get(temp.size()-1).sectionsOfLine
									.add(new Section(temp.get(temp.size()-1).str.substring(begin, temp.get(temp.size()-1).indexesOfDelimiters.get(0))));
									temp.get(temp.size()-1).sectionsOfLine
									.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(0) + 1)));
									for (int k = 1; k < temp.get(temp.size()-1).indexesOfDelimiters.size(); k++)
									{
										temp.get(temp.size()-1).sectionsOfLine
										.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(k - 1) + 1,
												temp.get(temp.size()-1).indexesOfDelimiters.get(k))));
									}
								}
							}
							if(found1m != -1)
							{
								
								temp.get(i).sectionsOfLine.set(j, new Section("" + temp.get(i).sectionsOfLine.get(j).part.substring(0, 1) + temp.get(found1m).str.charAt(0)));
							}
							else
							{
								char newChar = getNextChar();
								temp.get(i).sectionsOfLine.set(j, new Section("" + temp.get(i).sectionsOfLine.get(j).part.substring(0, 1) + newChar));
								
								for (int b = 0; b < temp.size(); b++)
								{
									for (int l = 0; l < temp.get(b).sectionsOfLine.size(); l++)
									{
										for (int g = 0; g < temp.get(b).sectionsOfLine.get(l).characters.size(); l++)
										{
											if (Character.isUpperCase(temp.get(b).sectionsOfLine.get(l).characters.get(g)))
											{
												if (!currectChar.contains(temp.get(b).sectionsOfLine.get(l).characters.get(g)))
												{
													currectChar.add(temp.get(b).sectionsOfLine.get(l).characters.get(g));
												}
	
											}
										}
									}
								}

								int begin = temp.get(temp.size()-1).str.indexOf('>') + 1;


								if (temp.get(temp.size()-1).indexesOfDelimiters.size() == 0)
								{
									temp.get(temp.size()-1).sectionsOfLine.add(new Section(temp.get(temp.size()-1).str.substring(begin)));
								} 
								else if (temp.get(temp.size()-1).indexesOfDelimiters.size() == 1)
								{
									temp.get(temp.size()-1).sectionsOfLine
									.add(new Section(temp.get(temp.size()-1).str.substring(begin, temp.get(temp.size()-1).indexesOfDelimiters.get(0))));
									temp.get(temp.size()-1).sectionsOfLine
									.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(0) + 1)));
								} 
								else if (temp.get(temp.size()-1).indexesOfDelimiters.size() > 1)
								{
									temp.get(temp.size()-1).sectionsOfLine
									.add(new Section(temp.get(temp.size()-1).str.substring(begin, temp.get(temp.size()-1).indexesOfDelimiters.get(0))));
									temp.get(temp.size()-1).sectionsOfLine
									.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(0) + 1)));
									for (int k = 1; k < temp.get(temp.size()-1).indexesOfDelimiters.size(); k++)
									{
										temp.get(temp.size()-1).sectionsOfLine
										.add(new Section(temp.get(temp.size()-1).str.substring(temp.get(temp.size()-1).indexesOfDelimiters.get(k - 1) + 1,
												temp.get(temp.size()-1).indexesOfDelimiters.get(k))));
									}
								}
							}
						}
					}
				}
			}


			//Make start new Start Variable
			String start = "" + "S0->";
			for (int j = 0; j < temp.get(0).sectionsOfLine.size(); j++)
			{
				if (temp.get(0).sectionsOfLine.size() == 1)
				{
					start = start + temp.get(0).sectionsOfLine.get(j).part;

				} 
				else if (temp.get(0).sectionsOfLine.size() > 1 && j == temp.get(0).sectionsOfLine.size() - 1)
				{
					start = start + temp.get(0).sectionsOfLine.get(j).part;

				} 
				else if (temp.get(0).sectionsOfLine.size() > 1)
				{
					start = start + temp.get(0).sectionsOfLine.get(j).part + "|";

				}
			}

			CNF.add(start);



			//Put the Lines Back together from sections and add to final product
			for (int i = 0; i < temp.size(); i++)
			{
				String st = "" + temp.get(i).str.substring(0, temp.get(i).str.indexOf('>') + 1);
				for (int j = 0; j < temp.get(i).sectionsOfLine.size(); j++)
				{
					if (temp.get(i).sectionsOfLine.size() == 1)
					{
						st = st + temp.get(i).sectionsOfLine.get(j).part;
					} 
					else if (temp.get(i).sectionsOfLine.size() > 1 && j == temp.get(i).sectionsOfLine.size() - 1)
					{
						st = st + temp.get(i).sectionsOfLine.get(j).part;
					} 
					else if (temp.get(i).sectionsOfLine.size() > 1)
					{
						st = st + temp.get(i).sectionsOfLine.get(j).part + "|";
					}
				}
				CNF.add(st);
			}

			return CNF;
		}// Convert

		public char getNextChar()
		{
			char nextChar = 'A';
			int count = 0;

			while (count < Alphabet.length)
			{
				if (currectChar.contains(nextChar))
				{
					nextChar = Alphabet[count];
					count++;
				} else
				{
					return nextChar;
				}
			}

			return '0';
		}

		public boolean checkForEpsilon(ArrayList<Line> temp)
		{
			for (int i = 0; i < size; i++)
			{
				for (int j = 0; j < temp.get(i).sectionsOfLine.size(); j++)
				{
					int tempIndex = temp.get(i).sectionsOfLine.get(j).characters.indexOf('\\');

					if (tempIndex != -1)
					{
						return true;
					}
				}
			}
			return false;
		}

	}

	public static class Line
	{
		public String str;
		ArrayList<Integer> indexesOfDelimiters;
		ArrayList<Section> sectionsOfLine;

		public Line(String str)
		{
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

				index = str.indexOf(s, index + 1);
			} // while

			return indexList;

		}// getIndexOfSuffix

	}

	public static class Section
	{
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
			for (int j = 0; j < part.length(); j++)
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
		if (result == JFileChooser.APPROVE_OPTION)
		{
			selectedFile = fc.getSelectedFile();
			System.out.println("Selected file: " + selectedFile.getAbsolutePath());
		} else
		{
			return null;
		}

		return selectedFile;
	}

	public static void main(String[] args)
	{
		File aFile = fileSelector();

		if (aFile == null)
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
			if (count == 0)
			{
				JOptionPane.showMessageDialog(null, "String was not of correct format", "Error",
						JOptionPane.ERROR_MESSAGE);

				System.exit(1);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		Converter aConverter = new Converter();

		ArrayList<String> finalCNF = null;

		finalCNF = aConverter.convert(orig);

		if (finalCNF == null)
		{
			JOptionPane.showMessageDialog(null, "String was not of correct format", "Error", JOptionPane.ERROR_MESSAGE);

			System.exit(1);
		}

		String finalString = "";

		for (int i = 0; i < finalCNF.size(); i++)
		{
			finalString = finalString + finalCNF.get(i) + "\n";
			System.out.println(finalCNF.get(i));
		}

		JOptionPane.showMessageDialog(null, "FINAL: " + "\n" + finalString);

		System.exit(0);

	}// main

}
