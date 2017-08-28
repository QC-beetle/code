import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class TaxonomyID {
	
	public String taxonomyID;
	public String Accession;
	public String[] Source;
	public String[] taxonomy;
	public int[] SourceCount;//number of each source;
	public String[] SourcePrint;//the display version of source list
	public int NUM;//the size of count array;
	
	
	
	public TaxonomyID(String[] accessionNumber, String[] sourceName, int arraySize)
	{
		NUM = 0;
		//Accession = accessionNumbers;
		//searchTaxonomyID(Accession);
		sortAndcount(sourceName, arraySize, accessionNumber);
		
	}

	public void searchTaxonomyID(String accessionNumbers)
	{
		String s = null;
		try
		{
			
			Process p1 = Runtime.getRuntime().exec("./efetch -db sequences -format fasta -mode xml -id " 
					+ accessionNumbers);
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p1.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
			
			//create a FASTA file for storing a list of transcript sequences
			//PrintWriter sequences = new PrintWriter(fastaName + ".fa");
			
			//read the output from the command
			//print the output in the FASTA file
			//System.out.println("Here is the standard output of the command: \n");
			while((s = stdInput.readLine()) != null)
			{
				String query = "<TSeq_taxid>";
				String endQuery = "</TSeq_taxid>";
				int start = s.indexOf(query);
				int end = s.indexOf(endQuery);
				if(start > 0)
				{
					//System.out.println(s.substring(start+12, end));
					taxonomyID = s.substring(start+12, end);
					
				}
				
				//sequences.println(s);
				//System.out.println(s);
			}
			
			//read any errors from the command
			//System.out.println("Here is the standard error of the command: \n");
			while((s = stdError.readLine()) != null)
			{
				System.out.println(s);
			}
			
			//sequences.close();
			
			
		}
		catch (IOException ex)
		{
			System.out.println("exception happened");
			
			
		}
		
		
		
	}

	public void sortAndcount(String[] sourceName, int arraySize, String[] accessionNumber)
	{
		Source = new String[arraySize];
		for(int i = 0; i < arraySize; i ++)
			  if(sourceName[i] != null)
				  Source[i] = sourceName[i];

		Arrays.sort(Source);
		
		/*for(int i = 0; i < arraySize-1; i ++)
		{
			int minpos = i;
			for(int j = i + 1; j < arraySize; j ++)
				if(Source[i].compareTo(Source[j]) < 0)
					minpos = i;
			String temp = Source[i];
			String temp1 = accessionNumber[i];
			Source[i] = Source[minpos];
			accessionNumber[i] = accessionNumber[minpos];
			Source[minpos] = temp;
			accessionNumber[minpos] = temp1;
			
		}*/
		
		Set<String> set = new HashSet<String>();
		for(int i = 0; i < arraySize; i ++)
			set.add(Source[i]);
		 
		
		
		Iterator<String> it = set.iterator();
		Iterator<String> it1 = set.iterator();
		while(it.hasNext())
		{
			  //SourcePrint[NUM] = it.next();
			String s = it.next();
			  NUM ++;
		    //System.out.println(it.next());
		}
		
		SourceCount = new int [NUM];
		SourcePrint = new String [NUM];
		taxonomy = new String[NUM];
		
		
		int j = 0;
		while(it1.hasNext())
		{
			String s = it1.next();  
			SourcePrint[j] = s;
			j ++;
			 // NUM ++;
		    //System.out.println(it.next());
		}
		
		for(int i = 0; i < NUM; i ++)
			SourceCount[i] = 1;
		int num = 0; //start position of SourceCount;
		
		for(int i = 0; i < arraySize-1; i ++)
		  {
			 if(Source[i].compareTo(Source[i+1]) == 0)
			 {
				 SourceCount[num] += 1;
				 //Accession[num] = accessionNumber[i+1];
				 
			 }
			 if(Source[i].compareTo(Source[i+1]) < 0)
			 {
				 
				 num ++;
				 
			 }
			 //System.out.println(Source[i]);
		  }
		
		Arrays.sort(SourcePrint);
		
		for(int i = 0; i < NUM; i ++)
		{
		
			String access = searchAccession(SourcePrint[i], arraySize, accessionNumber, sourceName);
			searchTaxonomyID(access);
			taxonomy[i] = taxonomyID;
			
			
		}
	}
	
	public String searchAccession(String SourcePrint, int arraySize, String[] accessionNumber, 
			String[] sourceName)
	{
		
		for(int i = 0; i < sourceName.length; i ++)
		{
			if(sourceName[i] != null && sourceName[i].compareTo(SourcePrint) == 0)
			{
				Accession = accessionNumber[i];
				return Accession;
				
			}

		}
		return null;
		
	}
	
	
	public String getTaxonomy(int num)
	{
		return taxonomy[num];
		
	}
	
	public int getCount(int num)
	{
		
		return SourceCount[num];
		
	}
	
	public String getSource(int num)
	{
		return SourcePrint[num];
		
	}
	
	public int getCountSize()
	{
		return NUM;
	}
	
	
	
	
}
