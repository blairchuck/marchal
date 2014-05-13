
public class Marchal {
	public  float getLenWithPoints(float p1x, float p1y, float p2x, float p2y) {
		float R = (float)6378.137;
		float dlat = (float)((p2x - p1x) * Math.PI / 180);
		float dlon = (float)((p2y - p1y) * Math.PI / 180);
		float aDouble = (float)(Math.sin(dlat / 2) * Math.sin(dlat / 2)
				+ Math.cos(p1x * Math.PI / 180)
				* Math.cos(p2x * Math.PI / 180) * Math.sin(dlon / 2)
				* Math.sin(dlon / 2));
		float cDouble = (float)(2 * Math.atan2(Math.sqrt(aDouble), Math
				.sqrt(1 - aDouble)));
		float d = Math.round((R * cDouble) * 1000) / 1000;
		return d;
		
		
	}
	
	
	public  float getLength(float lx1, float ly1, float lx2,
			float ly2, float px, float py) {
		float length = 0;
		float b = getLenWithPoints(lx1, ly1, px, py);
		float c = getLenWithPoints(lx2, ly2, px, py);
		float a = getLenWithPoints(lx1, ly1, lx2, ly2);

		if (c + b == a) {// point is on the link
			length = (float) 0;
		} else if (c * c >= a * a + b * b) { //projection on the extension cord of point1
			length = b;
		} else if (b * b >= a * a + c * c) {// projection on the extension cord of point2
			length = c;
		} else {
			// height of acute triangle
			float p = (a + b + c) / 2;// semi-perimeter
			float s = (float)(Math.sqrt(p * (p - a) * (p - b) * (p - c)));// Heron's formula
			length = (float)(2 * s / c);// height
		}

		return length;
	}
	
	public void shellsort(float[] Data,int[] index)
	{
		int k;
		float temp;
		for(int gap=Data.length/2;gap>0;gap/=2)
		{
			for(int j=gap;j<Data.length;j++)
			{
				if(Data[j]<Data[j-gap])
				{
					temp=Data[j];
					k=j-gap;
					while(k>=0&&Data[k]>temp)
					{
						Data[k+gap]=Data[k];
						index[k+gap]=k;
						k-=gap;
					}
					Data[k+gap]=temp;
					index[k+gap]=j;
					
					
				}
			}
		}
	}
}
