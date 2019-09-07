package digrec.stages.stage1;

import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int [] neuWeight = {2,1,2,4,-4,4,2,-1,2,-5};
		int a10 = 0; 	
		System.out.println("enter matrix 3x3, where 1 is blue and 0 is white:");
		for (int i=0;i<9;i++) {
			a10	+= sc.nextInt()*neuWeight[i];
		}
		a10+=neuWeight[9];
		sc.close(); 
		System.out.println(a10<0?"It's a \"1\".":"It's a \"0\".");
		

	}

}
