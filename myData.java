import java.util.Scanner;
public class myData
{
	public static int [] Fib(int x)
	{
		int [] fib = new int [x];
		fib[0] = 1;
		fib[1] = 1;
		int i = 2,Befor1 = 0,Befor2 = 0;
		while(i<x)
		{
			Befor1 = i-1;
			Befor2 = i-2;
			fib[i] = fib[Befor1]+fib[Befor2 ];
			i = i+1;
		}
		return fib;
	}
	public static void Sort()
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("enter int for length of array: ");
		int [] arr = new int [10];
		int i = 0;
		int j = 0;
		int temp = 0;
		System.out.println("Enter 10 integers: ");
		while(i<10)
		{
			arr[i] = scan.nextInt();
			i = i+1;
		}
		i = 0;
		while(i<10)
		{
			j = i;
			while(j<10)
			{
				if(arr[j]>arr[i])
				{
					temp = arr[i];
           	 			arr[i] = arr[j];
            	  			arr[j] = temp;
				}
				j = j+1;	
			}
			i = i+1;
		}
		i = 0;
		System.out.println("Sort of them are : "); 
		while(i<10)
		{
			System.out.println(arr[i]);
			i = i+1;
		}
	}
    	public static int countArray(int[] x,int [] y,int LenX,int Select)
   	{
		int i = 0,idx = 0;
      		while(i<LenX) 
      		{
      			idx = x[i];
           		y[idx] = y[idx]+1;
           		i = i+1;
           	} 
		return y[Select];
    	 }  
	 public static void fac(int num)
   	{
      		int x = 1;
      		int y = num;
 		while(y!=0) 
 		{
	            x = x*y;
        	    y = y-1;
      		}
	  System.out.println(x);
  	 } 
	public static void main(String[] args)
	{
		int [] arr1 = new int [2];
		int [] arr2 = new int [10];
		int i = 1,j = 0;
		arr1[0] = 1;
		while(i<2)
		{
			arr1[i] = i;
			if(arr1[j]!=arr1[i])
			{
				arr1[j] = arr1[j]+1;
			}
			else if(arr1[j]<arr1[i])
			{
				arr1[j] = arr1[j]-1;
			}
			else 
			{
				arr1[j] = arr1[j]+2;
			}
			i = i+1;
		}
		int temp1 = 3;
		int [] F = new int [temp1];
		F = Fib(temp1);
		int k = 0;
		System.out.println("3 elements of fibonaci function : ");
		while(k<temp1)
		{
			System.out.println(F[k]);
			k = k+1;
		}
		System.out.println("Sort of some integers: ");	
		Sort();
		System.out.println("factoril of 4 is : ");
		fac(4);
		System.out.println("CountArray : ");
		System.out.println(countArray(arr1,arr2,2,1));
	}
}

