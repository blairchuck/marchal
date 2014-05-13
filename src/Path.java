import java.util.ArrayList;
import java.util.List;
//define the class of path

public class Path {

	public List<Integer> list=new ArrayList<Integer>();
	public float score;
	public int p;
	 public void createPath(Path a,int b,float c)
	{
		a.list.add(b);
		a.score+=c;
	}
	public void copyPath(Path a,Path b)
	{
		b.list=a.list;
		b.score=a.score;
		b.p=a.p;
	}
	public void deletePath(Path a)
	{
		a.list=new ArrayList<Integer>();
		a.score=0;
		a.p=0;
	}
}
