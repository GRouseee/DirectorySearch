/* * * * * * * * * * * * * * * * *\
 * 						   		 *
 * 		 	Gavin Rouse	  		 *
 * 		   CSCD211 - 9am		 *
 * I attempted the extra credit  *
 * 						   		 *
\* * * * * * * * * * * * * * * * */

import java.io.File; 
import java.io.FileNotFoundException;
import java.text.*;
import java.util.*;

public class MySearch    
{
	private final static int PATH = 0;
	private final static int NAME = 1;
	private final static int EXT = 2;
	private final static int CONTENT = 3;
	private final static int DATE = 4;

	public static void main(String[] args) throws ParseException
	{
		Scanner input = new Scanner(System.in);
		String s = "Y";
		
		while(s.equalsIgnoreCase("Y"))
		{
			ArrayList<String> results = new ArrayList<String>();
			
			search(new File(options(PATH)), options(NAME), options(EXT), options(CONTENT), toDate((options(DATE))), results);
			MySearch.toString(results);
			System.out.print("\nRun again (Y/N): ");
			s = input.nextLine();
		}
	}
	
	public static String options(int choice)
	{
		Scanner input = new Scanner(System.in);
		String s = "";
		File file = new File(""); //Source: http://stackoverflow.com/questions/468789/is-there-a-way-in-java-to-determine-if-a-path-is-valid-without-attempting-to-cre
		
		if (choice == 0)
		{
			System.out.println("Directory search by path, name, extension, and content.\n");
			
			while (!file.isDirectory()) //see source above
			{
				System.out.print("Enter starting directory for the search (like c:\\temp): ");
				s = input.nextLine();
				file = new File(s); //see source above
				System.out.println();
				
				if (!file.isDirectory()) //see source above
				{
					System.out.println("The directory entered does not exist. Please enter again.\n");
				}
			}
		}
		
		else if (choice == 1)
		{
			System.out.print("Enter the file name (like myFile or enter for all): ");
			s = input.nextLine();
			System.out.println();
		}
		
		else if (choice == 2)
		{
			System.out.print("Enter the file extension (like txt or enter for all): ");
			s = input.nextLine();
			System.out.println();
		}
		
		else if (choice == 3)
		{
			System.out.print("Enter content to search for (like cscd211 or enter for any): ");
			s = input.nextLine();
			System.out.println();
		}
		
		else if (choice == 4)
		{
			System.out.print("Enter the last known modification date (like 1/1/1970 or enter for any): ");
			s = input.nextLine();		
			System.out.println("\nSearching.........\n");
		}
		
		return s;
	}


	public static void search(File item, String fileName, String extension, String content, long modDate, ArrayList<String> results)
	{
	   String base ="";
	   String ext = "";
	   
	   File[] names = item.listFiles();
	   
	   if (names != null)
	   {
		   for (File name : names)
		   {
		      search(name, fileName, extension, content, modDate, results);
		   }
	   }
	   
	   else
	   {
		  String sWork = null;
	      String fNameFull = item.toString();
	      String fNameShort = item.getName();
	      
	      int loc = fNameShort.lastIndexOf('.');
	      
	      
	      if (loc > 0)
	      {
	    	  base = fNameShort.substring(0, loc); 
	    	  ext = fNameShort.substring(loc + 1);   
	      }	      
	      
	      if (extension.equals("") || ext.equalsIgnoreCase(extension))
	      {
	    	  if (modDate == 0 || modDate <= item.lastModified())
	    	  {
		    	  if (fileName.equals("") || fileName.equalsIgnoreCase(base))
		    	  {
		    		  if (content.equals(""))
		    	      {
		    			  results.add(fNameFull);
		    	      }
		    		  
		    		  else
		    		  {
		    			  try
		    			  {
		    				  Scanner fin = new Scanner(item);
		    				  sWork = fin.findWithinHorizon(content, 0);
		    				  if (sWork != null)
		    				  {
		    					  results.add(fNameFull);
		    				  }
		    			  }
		    			  
		    			  catch (Error e)
		    			  {
		    				  System.out.println("Error with " + fNameFull);
		    			  }
		    			  
		    			  catch (FileNotFoundException e)
		    			  {
		    				  System.out.println("Error with " + fNameFull);
		    			  }
		    		  }
		    	  }
	    	  }
	      }
	   }
	}

	public static void toString(ArrayList<String> results)
	{
		int number = results.size();
		
		System.out.println("\nResults - " + number + " entries found: ");
		
		for (String fName : results)
		{
			System.out.println(fName);
		}
	}
	
	public static long toDate(String sDate) throws ParseException 				//Sources: http://stackoverflow.com/questions/18803675/get-last-modified-date-of-files-in-a-directory
	{																			//			and https://docs.oracle.com/javase/7/docs/api/java/util/GregorianCalendar.html
		if (sDate.equals(""))
		{
			return 0;
		}
		
		else
		{
			String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
			SimpleTimeZone pdt = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);
			DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
			Date date = formatter.parse(sDate);
			Calendar calendar = new GregorianCalendar(pdt);
			calendar.setTime(date);
			return calendar.getTimeInMillis();
		}
	}
}
