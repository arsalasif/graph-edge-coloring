package project.graph.theory;

public class Util {
	// Find maximum value
	public static int findMax(int[] arr)
	{
		int max = 0;
		for(int i = 0; i < arr.length; i++)
		{
			if(max < arr[i])
				max = arr[i];
		}
		return max;
	}
	
	// Find minimum value 
	public static int findMin(int[] arr)
	{
		int min = arr[1];
		for(int i = 0; i < arr.length; i++)
		{
			if(min > arr[i] && arr[i] != 0)
				min = arr[i];
		}
		return min;
	}
	
	// Find index of minimum value
	public static int findIndexOfMin(int[] arr)
	{
		int min = arr[1];
		int index = 1;
		for(int i = 1; i < arr.length; i++)
		{
			if(min > arr[i] && arr[i] != 0)
			{
				min = arr[i];
				index = i;
			}
		}
		return index;
	}
	
	// Clear array, i.e reset all values to 0
	public static void clearArray(int[] arr)
	{
		for(int i = 0; i < arr.length; i++)
		{
			arr[i] = 0;
		}
	}
	
	// Print 1d array
	public static void printArray(String s, int[] arr)
	{
		System.out.println(s);

		for(int j = 1; j < arr.length; j++)
		{
			System.out.printf("%3d ", j);
		}
		
		System.out.println();
		for(int j = 1; j < arr.length; j++)
		{
			System.out.print("___");
		}
		System.out.println();
		for(int i = 1; i < arr.length; i++)
		{
			System.out.printf("%3d ", arr[i]);
		}
		System.out.println();
		System.out.println();
	}
	
	// print 2d array
	public static void print2dArray(int[][] arr)
	{
		for(int j = 1; j < arr.length; j++)
		{
			System.out.printf("%2d ", j);
		}
		System.out.println();
		for(int j = 1; j < arr.length; j++)
		{
			System.out.print("___");
		}
		System.out.println();
		for(int i = 1; i < arr.length; i++)
		{
			for(int j = 1; j < arr[i].length; j++)
			{
				System.out.printf("%2d ", arr[i][j]);
			}
			System.out.print("  |" + i);
			System.out.println();
		}
		System.out.println();
	}
	
	// Initialized 2d array to 0
	public static void init2dArray(int[][] arr)
	{
		for(int i = 1; i < arr.length; i++)
			for(int j = 1; j < arr.length; j++)
				if(i != j)
					arr[i][j] = 0;
	}
	
	
	// Find indices with value other than 0
	public static int nonEmptyIndices(int[] arr)
	{
		int count = 0;
		for(int i = 1; i < arr.length; i++)
		{
			if(arr[i] != 0)
				count++;
		}
		return count;
	}
	
	// Generate Generalized Petersen graph (n,k)
	public static void Petersen(int n, int k)
	{
		System.out.println("G(" + n +", " + k+ ")");
		System.out.println(n*2);
		// Generate outer polygon/cycle
		for(int i = 1; i <= n; i++)
		{
			System.out.print("-");
			System.out.print(i + " ");
			if(i == 1)
			{
				System.out.print(i+1 + " ");
				System.out.print(n + " ");
				System.out.print(n+i);
			}
			else if(i == n)
			{
				System.out.print(i-1 + " ");
				System.out.print(1 + " ");
				System.out.print(n+i);
			}
			else
			{
				System.out.print(i+1 + " ");
				System.out.print(i-1 + " ");
				System.out.print(n+i);
			}
			System.out.println();
		}
		for(int i = n+1; i <= n*2; i++)
		{
			System.out.print("-");
			System.out.print(i + " ");

			int val = i - k;
			int p = (n*2 - (n+1) + 1);
			int prevEdge = (i - k - (n+1)) % p + (n+1);
			if(prevEdge < n+1)
				prevEdge += p;
			int nextEdge = (i + k - (n+1)) % p + (n+1);
			if(nextEdge < n+1)
				nextEdge += p;
			System.out.print(nextEdge + " ");
			System.out.print(prevEdge);
			System.out.println();
		}
		System.out.println("0");
	}
}
