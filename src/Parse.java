import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;





public class Parse {
	 public static void main(String[] args) {
		  
		  String fileName="/Users/shilei1025/Documents/workspace/marchal/data/nmea/5.txt";//GPS data path
		  File file=new File(fileName);
		  BufferedReader br = null;
		  String name="";
		  float[] Latitude;
		  Latitude=new float[1000];
		  float[] Longitude;
		  Longitude=new float[1000];
		  int count=0;
		  try {
		   br=new BufferedReader(new FileReader(file));
		  } catch (FileNotFoundException e) {
		   e.printStackTrace();
		   System.out.println("error");
		  }
		 
		  String line=null;
		  String[] rec=null;
		  try {
		   while((line=br.readLine())!=null){
		    rec=line.split(",");
		    name=rec[0];
		    if(name.equals("$GPRMC"))
		    {
		    	if(rec[4].equals("N"))
		    	{
		    	Latitude[count]=Float.parseFloat(rec[3])/100+(Float.parseFloat(rec[3])%100)/60;
		    	}
		    	else
		    	{
		    		Latitude[count]=(Float.parseFloat(rec[3])/100+(Float.parseFloat(rec[3])%100)/60)*(-1);
		    	}
		    	if(rec[6].equals("E"))
		    	{
		    	Longitude[count]=Float.parseFloat(rec[5])/100+(Float.parseFloat(rec[5])%100)/60;
		    	}
		    	else
		    	{
		    		Longitude[count]=(Float.parseFloat(rec[5])/100+(Float.parseFloat(rec[5])%100)/60)*(-1);
		    	}
		    	System.out.println("GPS:\n latitude "+Latitude[count]+"\t"+"longitude "+Longitude[count]+"\n");//parse latitude&longitude data
		    	count++;
		    }
		    else
		    	continue;
		  
		    
		   }
		  } catch (IOException e1) {
		   e1.printStackTrace();
		  }
		  
		  //parse the map's link
		  String mapName="/Users/shilei1025/Documents/workspace/marchal/data/map.txt";//Map data path
		  File Mapfile=new File(mapName);
		  BufferedReader br1 = null;
		  float[] PointLatitude;
		  PointLatitude=new float[1000];
		  float[] PointLongitude;
		  PointLongitude=new float[1000];
		  int[] Linkstart;
		  Linkstart=new int[1000];
		  int[] Linkend;
		  Linkend=new int[1000];
		  int Pointcount=0;
		  int Pointcal=0;
		  int Linkcount=0;
		  int Linkcal=0;
		  int flag=0;
		  int flag1=0;
		  try {
		   br1=new BufferedReader(new FileReader(Mapfile));
		  } catch (FileNotFoundException e) {
		   e.printStackTrace();
		   System.out.println("error");
		  }
		 
		  String line1=null;
		  String[] rec1=null;
		  try {
		    while((line1=br1.readLine())!=null){
		    rec1=line1.split("\\s+");
		   if(flag==0)
		   {
			   Pointcount=Integer.parseInt(rec1[0]);
			   flag=1;
			   continue;
		   }
		   else if(Pointcal<Pointcount)
		    {
		    	PointLatitude[Pointcal]=Float.parseFloat(rec1[2]);
		    	PointLongitude[Pointcal]=Float.parseFloat(rec1[1]);
		    	System.out.println("Map Points:\n latitude "+PointLatitude[Pointcal]+"\t"+"longitude "+PointLongitude[Pointcal]+"\n");//parse map points latitude&longitude data
		    	Pointcal++;
		    	continue;
		    }
		   else if(flag1==0)
		   {
			   Linkcount=Integer.parseInt(rec1[0]);
			   flag1=1;
			   continue;
		   }
		   else
		   {
			    Linkstart[Linkcal]=Integer.parseInt(rec1[0]);
		    	Linkend[Linkcal]=Integer.parseInt(rec1[1]);
		    	System.out.println("Map Links:\n start "+Linkstart[Linkcal]+"\t"+"end "+Linkend[Linkcal]+"\n");//parse map points latitude&longitude data
		    	Linkcal++;
		    	
		   }
		    
		   }
		  } catch (IOException e1) {
		   e1.printStackTrace();
		  }
		  
		  //use HashMap to store map information
		  HashMap<Integer,List<Integer>> map=new HashMap<Integer,List<Integer>>();
		  for(int g=0;g<Pointcount+3;g++)
		  {
			 
			  map.put(g+1,new ArrayList<Integer>());
		  }
		  for(int i=0;i<Linkcount;i++)
		  {
			  if(map.get(Linkstart[i]).isEmpty())
			  {
				 List<Integer> set=new ArrayList<Integer>();
				 set.add(Linkend[i]);
				 map.put(Linkstart[i],set); 
				 
			  }
			  else{
				  
				  map.get(Linkstart[i]).add(Linkend[i]);
				 
				  
			  }
		  }
		  
	     Marchal instance=new Marchal();
	     Path example=new Path();
	     Path[] path=new Path[1000];
	    
	     float[] distance;
	     distance=new float[Linkcount];
	     int[] disIndex;
		 disIndex=new int[Linkcount];
		 for(int t=0;t<Linkcount;t++)
		    {
		    	disIndex[t]=t;
		    }//initial disIndex
	     final int THRESHOLD=23;
	     final int N=30;
	     float record=0;
	     int i=0;
	     int k=0;
	   
	     int s=0;
	     float temp=0;
	     int pointer=0;
	     boolean signal=false;
	     boolean indicater=false;
	     boolean fla=false;
	     List<Integer> reach=new ArrayList<Integer>();
	     List<Integer> inter=new ArrayList<Integer>();
	     
	     while(i<count)
	     {
	    	 signal=false;
	    	 for(int j=0;j<Linkcount;j++)
	    	 {
	    		 distance[j]=instance.getLength(PointLatitude[Linkstart[j]-1], PointLongitude[Linkstart[j]-1], PointLatitude[Linkend[j]-1], PointLongitude[Linkend[j]-1], Latitude[i], Longitude[i]);
	    		
	    			 
	    	 }
	    	 for(int t=0;t<Linkcount;t++)
			    {
			    	disIndex[t]=t;
			    }//initial disIndex
	    	 instance.shellsort(distance,disIndex);//sort the current point to every link's distance
	    	 
	    	 //find out the nearest N links
	    	 k=0;
	    	 while(k<N)
	    	 {
	    		if(distance[k]<=THRESHOLD)
	    		{
	    			path[pointer]=new Path();
	    			
	    			example.createPath(path[pointer],disIndex[k],distance[k]);
	    			pointer++;
	    			signal=true;
	    		}
	           k++;
	    			
	    	 }
	    	 
	    	 //if not matched,it is a new road
	    	 if(!signal)
	    	 {
	    		 System.out.println("New Road(Point "+(i+1)+" ):\nLatitute: "+Latitude[i]+"\tLongitude: "+Longitude[i]+"\n");
	    		 i++;
	    	 }
	    	 
	    	else
	    		 cycling: {
	    		 
	    		reach=new ArrayList<Integer>();
		    	 inter=new ArrayList<Integer>();
		    	 indicater=false;
	    			fla=false;
	    		while(s<pointer)
	    		{
	    			path[s].p=i+1; 
	    			int size=path[s].list.size();
	    			int indi=path[s].list.get(size-1);
	    			record=0;
	    			
	    			
	    			while(path[s].p<count)
	    			{
	    			temp=instance.getLength(PointLatitude[Linkstart[indi]-1], PointLongitude[Linkstart[indi]-1], PointLatitude[Linkend[indi]-1], PointLongitude[Linkend[indi]-1], Latitude[path[s].p], Longitude[path[s].p]);
	    			if(temp<=THRESHOLD)
	    			{
	    				record+=instance.getLenWithPoints(Latitude[path[s].p-1], Longitude[path[s].p-1], Latitude[path[s].p], Longitude[path[s].p]);
	    				path[s].score+=temp;
	    				if(inter.size()==0)
	    				inter.add(s);
	    				else{
	    					if(s!=inter.get(inter.size()-1))
	    						inter.add(s);	
	    				}
	    				
	    				fla=true;
	    				path[s].p++;
	    			}
	    			
	    			else 
	    				break;
	    			
	    			}
	    			
	    			if(fla&&record>=0.5*instance.getLenWithPoints(PointLatitude[Linkstart[indi]-1], PointLongitude[Linkstart[indi]-1], PointLatitude[Linkend[indi]-1], PointLatitude[Linkend[indi]-1]))
	    			{
	    				indicater=true;
	    				reach.add(s);
	    			}
	    			s++;
	    		}
	    		 
	    		if(!indicater&&fla)
	    		{
	    			int temporary=0;
	    			float mem=path[inter.get(0)].score;
	    			for(int h=0;h<inter.size();h++)
	    			{
	    				if(mem>path[inter.get(h)].score)
	    				{
	    					mem=path[inter.get(h)].score;
	    					temporary=inter.get(h);
	    				}
	    			}
	    			
	    			System.out.println("Matched link without reaching intersection:\n");
	    			for(int a=0;a<path[temporary].list.size();a++)
	    			{
	    				System.out.println("start: "+Linkstart[path[temporary].list.get(a)]+"\tend: "+Linkend[path[temporary].list.get(a)]+"\n");
	    				
	    				
	    			}
	    			    System.out.println("From Point "+(i+1)+" to Point "+path[temporary].p);
	    			i=path[temporary].p;
	    			for(int b=0;b<pointer;b++)
	    			{
	    				example.deletePath(path[b]);
	    			}
	    			s=0;
	    			pointer=0;
	    			
	    			
	    		}
	    		else if(!fla&&!indicater)
	    		{
	    			int temporary=0;
	    			float mem=path[0].score;
	    			for(int h=0;h<pointer;h++)
	    			{
	    				if(mem>path[h].score)
	    				{
	    					mem=path[h].score;
	    					temporary=h;
	    				}
	    			}
	    			
	    			System.out.println("Matched link without reaching intersection:\n");
	    			for(int a=0;a<path[temporary].list.size();a++)
	    			{
	    				System.out.println("start: "+Linkstart[path[temporary].list.get(a)]+"\tend: "+Linkend[path[temporary].list.get(a)]+"\n");
	    				
	    			}
	    			System.out.println("From Point "+(i+1)+" to Point "+path[temporary].p);
	    			i=path[temporary].p;
	    			for(int b=0;b<pointer;b++)
	    			{
	    				example.deletePath(path[b]);
	    			}
	    			s=0;
	    			pointer=0;
	    			
	    			
	    			
	    			
	    		}
	    		else 
	    		{
	    			s=0;
	    			int d=0;
	    			List<Integer> ne=new ArrayList<Integer>();
	    			boolean tag=false;
	    			int e=0;
	    			float rest=0;
	    			while(s<reach.size())
	    			{
	    				int sta=path[reach.get(s)].list.get(path[reach.get(s)].list.size()-1);
	    				List<Integer> group=new ArrayList<Integer>();
	    				group=map.get(Linkend[sta]);
	    				int z=0;
	    				rest=instance.getLength(PointLatitude[Linkend[sta]-1], PointLongitude[Linkend[sta]-1] , PointLatitude[group.get(z)-1], PointLongitude[group.get(z)-1], Latitude[path[reach.get(s)].p-1], Longitude[path[reach.get(s)].p-1]);
	    				while(z<group.size())
	    				{
	    					temp=instance.getLength(PointLatitude[Linkend[sta]-1], PointLongitude[Linkend[sta]-1] , PointLatitude[group.get(z)-1], PointLongitude[group.get(z)-1], Latitude[path[reach.get(s)].p-1], Longitude[path[reach.get(s)].p-1]);
	    					
	    					
	    					if(temp<rest)
	    					{
	    						rest=temp;
	    						e=z;
	    					}
	    					
	    					
	    					
	    					z++;
	    				}
	    				
	    				if(rest<=THRESHOLD)
    					{
    						
    						tag=true;
    						for(d=0;d<Linkcount;d++)
    						{
    							if(Linkstart[d]==Linkend[sta]&&Linkend[d]==group.get(e))
    								break;
    						}
    						
    						path[reach.get(s)].list.add(d);
    						path[reach.get(s)].score+=rest;
    						path[reach.get(s)].p++;
    						ne.add(reach.get(s));
    						}
    					s++;	
    					}
	    		
	    				if(!tag)
	    				{
	    					
	    					int temporary=0;
	    					float mem=path[reach.get(0)].score;
	    	    			for(int h=0;h<reach.size();h++)
	    	    			{
	    	    				if(mem>path[reach.get(h)].score)
	    	    				{
	    	    					mem=path[reach.get(h)].score;
	    	    					temporary=reach.get(h);
	    	    				}
	    	    			}
	    	    			
	    	    			System.out.println("Matched link with reaching intersection:\n");
	    	    			for(int a=0;a<path[temporary].list.size();a++)
	    	    			{
	    	    				System.out.println("start: "+Linkstart[path[temporary].list.get(a)]+"\tend: "+Linkend[path[temporary].list.get(a)]+"\n");
	    	    				
	    	    			}
	    	    			i=path[temporary].p;
	    	    			for(int b=0;b<pointer;b++)
	    	    			{
	    	    				example.deletePath(path[b]);
	    	    			}
	    	    			s=0;
	    	    			pointer=0;	
	    				
	    			}
	    			
	    				else
	    				{
	    					int temporary=0;
	    					float mem=path[ne.get(0)].score;
	    					for(int f=0;f<ne.size();f++)
	    					{
	    						if(mem>path[ne.get(f)].score)
	    						{
	    							mem=path[ne.get(f)].score;
	    							temporary=ne.get(f);
	    						}
	    					}
	    					s=temporary;
	    					pointer=s+1;
	    					i=path[temporary].p;
	    					break cycling;
	    				}
	    			
	    			
	    			
	    		}
	    		
	    	
	    		 
	    	 }
	    	 
	    	 
	    	 
	    	 
	    	 
	    	 
	     }
	     
	    /* float[] distance;
	     distance=new float[Linkcount];
	     
	     int[] disIndex;
	    disIndex=new int[Linkcount];
	    for(int t=0;t<Linkcount;t++)
	    {
	    	disIndex[t]=t;
	    }//initial disIndex
	     int[] scoIndex;
	     scoIndex=new int[30];
	     for(int t=0;t<30;t++)
		    {
		    	scoIndex[t]=t;
		    }//initial scoIndex
	     float[] score;
	     score=new float[100];
	     int p=0;
	     int i=0;
	     final int THRESHOLD=50;
	     float temp;
	     boolean signal=false;
	     boolean[] indicate;
	     float length=0;
	     indicate=new boolean[Linkcount];
	     for(int t=0;t<Linkcount;t++)
		    {
		    	indicate[t]=false;
		    }
	     int k=0;
	     int q=0;
	     int pointer=0;
	   
	   
		   while(i<count)
	     {
	    	 p=i+1;
	    	 for(int j=0;j<Linkcount;j++)
	    	 {
	    		 distance[j]=instance.getLength(PointLatitude[Linkstart[j]-1], PointLongitude[Linkstart[j]-1], PointLatitude[Linkend[j]-1], PointLongitude[Linkend[j]-1], Latitude[i], Longitude[i]);
	    		
	    			 
	    	 }
	    	 instance.shellsort(distance,disIndex);//sort the current point to every link's distance
	    	
	    		
	    		 while(k<30)
	    		 {
	    			 if(distance[k]<=THRESHOLD)
	    			 {
	    		        score[pointer]=distance[k];
	    		        path[pointer][i]=disIndex[k];
	    		     for(p=i+1;p<count;p++)
	    		    {
	    			 temp=instance.getLength(PointLatitude[Linkstart[disIndex[k]]-1], PointLongitude[Linkstart[disIndex[k]]-1], PointLatitude[Linkend[disIndex[k]]-1], PointLongitude[Linkend[disIndex[k]]-1], Latitude[p], Longitude[p]);
	    			 if(temp<=THRESHOLD)
	    			 {
	    				 score[pointer]+=temp;
	    				 path[pointer][p]=path[pointer][p-1];
	    				 length+=instance.getLenWithPoints(Latitude[p-1], Longitude[p-1], Latitude[p], Longitude[p]);
	    				//calculate the distance between the points(matched the same link)
	    				 signal=true;
	    			 }
	    			 else
	    				 break;
	    		    }
	    		 
	        		 
	        		
	        		 if(signal&&length<0.5*instance.getLenWithPoints(PointLatitude[Linkstart[path[pointer][p-1]]-1], PointLongitude[Linkstart[path[pointer][p-1]]-1], PointLatitude[Linkend[path[pointer][p-1]]-1], PointLatitude[Linkend[path[pointer][p-1]]-1]))
	        		 {
	        			
	        			 indicate[path[pointer][p-1]]=true;
	        			 //check whether the points reach the intersection
	        	    	for(q=0;q<Linkcount;q++)	
	        	    	{
	        	    		if(Linkend[path[pointer][p-1]]==Linkstart[q])
	        	    		{
	        	    			 temp=instance.getLength(PointLatitude[Linkstart[q]-1], PointLongitude[Linkstart[q]-1], PointLatitude[Linkend[q]-1], PointLongitude[Linkend[q]-1], Latitude[p], Longitude[p]);
	        	    			 if(temp<=THRESHOLD)
	        	    			 {
	        	    				 
	        	    			 }
	        	    		}
	        	    	}
	        			 
	        			 
	        		     }
	    	    	 }
	    		 
	    		 else
	    		 {
	    			 score[k]=0;
	    			 path[k][i]=-1;
	    			 
	    		 }
	    		
	    	//record the first link of path
	    	 
	    	 
	    	
    		 i=p;
	     }
	   k++;
	   i=0;
	   }
	     
	   
	   //figure out the lowest score path
	   
	   instance.shellsort(score, scoIndex);
	   
	   int lowest=scoIndex[0];
	   int temporary=-2;
	   int[] matched;
	   matched=new int[count];
	   for(int t=0;t<count;t++)
	    {
	    	matched[t]=-1;
	    }//initial matched
	   
	   //check whether the matched links are continuous
	   
	   int q=0;
   	   for(int s=0;s<count&&q<count;s++)
   	   {
   		   if(path[lowest][s]==-1)
   		   {
   			   System.out.println("New road:\n"+"Latitude: "+Latitude[s]+"\tLongitude: "+Longitude[s]+"\n");
   		   }
   		   else
   		   {
   			   if(temporary==path[lowest][s])
   				   continue;
   			   else
   			   {
   			   temporary=path[lowest][s];
   			   matched[q]=temporary;
   			   q++;
   			   System.out.println("Matched link:\n"+"Start point: "+Linkstart[temporary]+"\tEnd point: "+Linkend[temporary]+"\n");
   			   if(indicate[temporary])
   			   {
   				   System.out.println("This link's intersection isn't reached\n");
   				   
   			   }
   			   else
   			   {
   				System.out.println("This link's intersection is reached\n");
   			   }
   			    
   			   }
   			   
   		   }
   		  
  
   		   
   	   }
	   
	   //check the tolerant links
   	   q=0;
   	   boolean newflag=true;
   	   while(q<count&&matched[q]!=-1&&q+1<count&&matched[q+1]!=-1)
   	   {
   		  if(Linkend[matched[q]]!=Linkstart[matched[q+1]])
   		  {
   			  for(int b=0;b<Linkcount;b++)
   			  {
   				  if(Linkend[matched[q]]==Linkstart[b]&&Linkend[b]==Linkstart[matched[q+1]])
   				  {
   					  System.out.println("Exist one tolerant link:\n"+"Start point: "+Linkstart[b]+"\tEnd point: "+Linkend[b]+"\n");
   					  newflag=false;
   				  }
   				  else
   				  {
   					  for(int c=0;c<Linkcount;c++)
   					  {
   						  if(Linkend[matched[q]]==Linkstart[b]&&Linkend[b]==Linkstart[c]&&Linkend[c]==Linkstart[matched[q+1]])
   						  {
   							 System.out.println("Exist two tolerant links:\n"+"Start point: "+Linkstart[b]+"\tEnd point: "+Linkend[b]+"\n"+"Start point: "+Linkstart[c]+"\tEnd point: "+Linkend[c]+"\n");
   							 newflag=false;
   						  }
   						  
   					  }
   				  }
   			  }
   			  if(newflag)
   			  {
   				System.out.println("The matched links are not continuous");  
   			  }
   		  }
   			  q++;
   	   }*/
   		   
   		   
	 
	   
	
	   
	   
	    
	 }

}
