package booleanquery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public  class IndexBuild {
	
    
    
    //method to find AND for two postingslist (TAAT)
    public static LinkedList<Integer> AND_TAAT(LinkedList<Integer> p1,LinkedList<Integer> p2,int cnt) {
    	
    	LinkedList<Integer> AND_post = new LinkedList<Integer>();
    	int i=0,j=0;
    	while((i<p1.size()) && (j<p2.size())){
    		cnt++;
    		if (p1.get(i)< p2.get(j)){
   
                i++;
                
            }
    		else if (p1.get(i)>p2.get(j)){
	            
	    		     j++;
	    		     
	    	         }
    		else{
    			AND_post.add(p1.get(i));
    			i++;
    			j++;
    		}
    	
    	}
         AND_post.addLast(cnt);
        return AND_post;
    	}
    
  //method to find AND for two postingslist(TAAT)
 public static LinkedList<Integer> OR_TAAT(LinkedList<Integer> p1,LinkedList<Integer> p2, int cnt) {
    	
	 LinkedList<Integer> OR_post = new LinkedList<Integer>();
	 
	 int i=0 ,j=0 ;
	 
	 
	    while((i<p1.size()) && (j<p2.size())){
	    	    cnt++;
	            if (p1.get(i)< p2.get(j)){
	            	OR_post.add(p1.get(i));
	                i++;
	                
	            }
	    		else if (p1.get(i)>p2.get(j)){
	            
	    		    OR_post.add(p2.get(j));  
	    		     j++;
	    		     
	    	         }
	    	 else{ 
	    		    OR_post.add(p2.get(j));   
	    		    i++;
	    		    j++;
	    		    
	    	      }
	    	   
	 }
	    while(i < p1.size())
	      {   OR_post.add(p1.get(i));
	           i++;
	           
	      }
	    
	    while( j < p2.size())
	      {   OR_post.add(p2.get(j));
	          j++;
	      }
	    
	    OR_post.addLast(cnt);
        return OR_post;
    	
	 }  
	 
    
    
    
   
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
	
		 
		int temp_doc_id;
		int m;
		int count_ANDTAAT;
		int count_ORTAAT;
		String term_arr[] =new String[10];
		
		
		String outputfile = args[1]; //output file creation and writing
		//String outputfile = "/Users/arunchandrapendyala/Desktop/output.txt"; 
		FileWriter filew = new FileWriter(outputfile,true);
		
		
		BufferedReader br = new BufferedReader (new FileReader (args[2]));
		//BufferedReader br = new BufferedReader (new FileReader ("/Users/arunchandrapendyala/Desktop/input_2.txt"));
		String line; //reading input file using buffered reader
		
		
		
		HashMap<String, LinkedList<Integer>> postMap = new HashMap<String , LinkedList<Integer>>(); //hashmap to store terms and postingslist
		//Path ind = Paths.get("/Users/arunchandrapendyala/Desktop/IR/Project_2/index") ;
		Path ind = Paths.get(args[0]) ;
	    
	    
		IndexReader indexReader = DirectoryReader.open(FSDirectory.open(ind));
		   
	    Iterator<String> fields = MultiFields.getFields(indexReader).iterator();
	    while(fields.hasNext()){
	           
	    	String field_value = fields.next();
	    
	    	if(!field_value.equals("_version_")&&!field_value.equals("id")){
	    
	    		TermsEnum termsEnum = MultiFields.getTerms(indexReader,field_value).iterator();
	             
	    		while(termsEnum.next()!=null){
					BytesRef term = termsEnum.term();
					PostingsEnum postings = MultiFields.getTermDocsEnum(indexReader, field_value, term);
					LinkedList<Integer> pos = new LinkedList<Integer>();
					while(postings.nextDoc()!=postings.NO_MORE_DOCS){
						pos.add(postings.docID());
					}
					postMap.put(term.utf8ToString(), pos);
				}
	    	}
	    } //end of formation of postings list
	    
	  //  System.out.println(postMap);
	    
	  
	     
	    while( (line = br.readLine() ) != null) {  //reading one line at a time
	    	String t_arr = line.trim();
	    	term_arr = t_arr.split("\\s");
	    	String[] term_arr_print = new String[term_arr.length];
	    	for(int i=0; i<term_arr.length ; i++){
	    		term_arr_print[i] = term_arr[i];
	    		
	    	}
	    	
	    	count_ANDTAAT = 0 ;
        	count_ORTAAT = 0;
	    	
	    	
	        for (int x=0; x<term_arr.length; x++) {
	        	
	            if (!(term_arr[x].equals("\\s\\s+"))){ //ignoring spaces between terms
	           LinkedList<Integer> val = postMap.get(term_arr[x]);
	         
		    	System.out.println("GetPostings\n" + term_arr[x]);
		    	filew.write("GetPostings\n" + term_arr[x]);
		    	System.out.print("Postings list: ");
		    	filew.write("\nPostings list: ");
		    	for(int i=0;i<val.size();i++){
			        System.out.print(val.get(i)+" ");
			        filew.write(val.get(i)+" ");
			        }
		    	System.out.print("\n ");	
		    	filew.write("\n");
		
	    }
      }         	
	    	
	    
	
	        
	     //TAAT implementations 
		    
		 //   boolean[] b_AND = new boolean[20];
		    
		    LinkedList<Integer> intAND_post = new LinkedList<Integer>();
		    LinkedList<Integer> intOR_post = new LinkedList<Integer>();
		    LinkedList<Integer> tempOR_post = new LinkedList<Integer>();
		    LinkedList<Integer> post_t2 = new LinkedList<Integer>();
		    String t;
		    int d;
		    for (int p=1; p<term_arr.length ; p++){  //sort the terms by doc freqs using insertion sort
		    	
		    	for(d=p; d>0 ; d--){
		    		if((postMap.get(term_arr[d])).size() < (postMap.get(term_arr[d-1])).size()){
		           t = term_arr[d]; 
		    	   term_arr[d] = term_arr[d-1];
		    	   term_arr[d-1] = t ;
		    		}
		    	}
		    }
		    
		    
	        for (int k=0; k<term_arr.length; k++) {
	        	
	        	
	        	
	        	if(k==0){ //first take the first two postings lists and then perform AND , OR (TAAT) and form the 1st intermediate list
	        		
	        		LinkedList<Integer> post_t1 = postMap.get(term_arr[k]); 
	            	m=k+1;
	            	if(m<term_arr.length){
	            	post_t2 = postMap.get(term_arr[k+1]) ;
	            	}


	            intOR_post = OR_TAAT(post_t1,post_t2,count_ORTAAT);	
	            count_ORTAAT = count_ORTAAT + intOR_post.getLast();
 	            intOR_post.removeLast();
	        	intAND_post = AND_TAAT(post_t1,post_t2,count_ANDTAAT);
	        	count_ANDTAAT = count_ANDTAAT + intAND_post.getLast();
 	           intAND_post.removeLast();
	        	   } 
	        	
	        
	        	else {     //Perform AND , OR on intermediate result and the next queryterm's postings list
	        		
	        		m=k+1;
	            	if(m<term_arr.length){
	            	post_t2 = postMap.get(term_arr[k+1]) ;
	            	             }
	        	           intAND_post = AND_TAAT(post_t2 ,intAND_post,count_ANDTAAT);
	        	           count_ANDTAAT = count_ANDTAAT + intAND_post.getLast();
	        	           intAND_post.removeLast();
	        	           intOR_post = OR_TAAT(post_t2 ,intOR_post,count_ORTAAT);
	        	           count_ORTAAT = count_ORTAAT + intOR_post.getLast();
	        	           intOR_post.removeLast();
	        	          
	        	           
	        	        }
	    	
	        	
		     
	        	
	        	
	    }
	        
	      //DAAT implementation
	        
	        int count_DAATAND=0;
	        int size_DAATAND=0;
	        HashMap<Integer, Integer> scoremap = new HashMap<Integer, Integer>();
        	int temp;
        	for(int h=0;h<postMap.get(term_arr[0]).size();h++){      //document scoring
                scoremap.put(postMap.get(term_arr[0]).get(h), 1);
        	
        	
        	}
        	
        	for(int i=1;i<term_arr.length;i++){
        		for(int j=0;j<postMap.get(term_arr[i]).size();j++){
        			for(int k=0;k<postMap.get(term_arr[0]).size();k++){
        				count_DAATAND++;
        	        if(postMap.get(term_arr[0]).get(k)==postMap.get(term_arr[i]).get(j)){
	           
        	        	
        		        scoremap.put(postMap.get(term_arr[0]).get(k),(scoremap.get(postMap.get(term_arr[0]).get(k)))+1); //increase doc score if found
        		        
        	        }
        	        }
        	  }
        	}
        	
        	
        	
        	
	        //Print the results and write the results to output file
	        System.out.print("TaatAnd \n ");
	        filew.write("TaatAnd\n");
	        for(int i=0;i<term_arr_print.length;i++){
	        filew.write(term_arr_print[i]+" ");
	        }
	        filew.write("\nResults: ");
	        if(intAND_post.size()==0){
	        	filew.write("empty");
	        }
	        else{
	        for(int i=0;i<intAND_post.size();i++){	
	        System.out.print(intAND_post.get(i)+" ");
	        filew.write(intAND_post.get(i)+" ");
	        }
	        }
	        System.out.println("Number of documents in results: " +intAND_post.size());
	        filew.write("\nNumber of documents in results: " +intAND_post.size());
	        
	        System.out.println("\nNumber of comparisons: " + count_ANDTAAT);
	        filew.write("\nNumber of comparisons: " + count_ANDTAAT);
	        filew.write("\n");
	        System.out.print("TaatOr \n");
	        filew.write("TaatOr\n");
	        for(int i=0;i<term_arr_print.length;i++){
		        filew.write(term_arr_print[i]+" ");
		        }
		        filew.write("\nResults: ");
	        for(int i=0;i<intOR_post.size();i++){
	        System.out.print(intOR_post.get(i)+" ");
	        filew.write(intOR_post.get(i)+" ");
	        }
	        System.out.println("\nNumber of documents in results: " +intOR_post.size());
	        filew.write("\nNumber of documents in results: " +intOR_post.size());
	        System.out.println("Number of comparisons: " + count_ORTAAT);
	        filew.write("\nNumber of comparisons: " + count_ORTAAT);
	        filew.write("\n");
	        System.out.print("DaatAnd \n ");
	        filew.write("DaatAnd\n");
	        for(int i=0;i<term_arr_print.length;i++){
		        filew.write(term_arr_print[i]+" ");
		        }
		        filew.write("\nResults: ");
	        if(intAND_post.size()==0){
	        	filew.write("empty");
	        }
	        else{
	        for(int i=0;i<term_arr.length;i++){
	        	if(scoremap.get(postMap.get(term_arr[0]).get(i))==term_arr.length){
	        		size_DAATAND++;
	        		System.out.print(postMap.get(term_arr[0]).get(i));
	        		filew.write(postMap.get(term_arr[0]).get(i)+"");
	        	     }	
	        	}
	        	}
	        System.out.println("Number of documents in results: " +size_DAATAND);
	        filew.write("\nNumber of documents in results: " +size_DAATAND);
	        
	        System.out.println("\nNumber of comparisons: " + count_DAATAND);
	        filew.write("\nNumber of comparisons: " + count_DAATAND);
	        filew.write("\n");
	        
	        System.out.print("DaatOr \n");
	        filew.write("DaatOr\n");                  
	        for(int i=0;i<term_arr_print.length;i++){
		        filew.write(term_arr_print[i]+" ");
		        }
		        filew.write("\nResults: ");   
	        for(int i=0;i<intOR_post.size();i++){
	        System.out.print(intOR_post.get(i)+" ");     
	        filew.write(intOR_post.get(i)+" ");
	        }
	        System.out.println("\nNumber of documents in results: " +intOR_post.size()); 
	        filew.write("\nNumber of documents in results: " +intOR_post.size());
	        System.out.println("Number of comparisons: " );
	        filew.write("\nNumber of comparisons: " );
	        filew.write("\n");
	        
	        
	        
	        count_DAATAND=0;
	        count_ORTAAT =0;
	        count_ANDTAAT=0;
	        
        }
	    
	        
	    filew.close();    
	
	    
	    
	    
	    
	    
	    //References:https://lucene.apache.org/core/6_2_1/core/org/apache/lucene/index/package-summary.html ; http://www.geeksforgeeks.org/union-and-intersection-of-two-linked-lists/
	    
}
}


