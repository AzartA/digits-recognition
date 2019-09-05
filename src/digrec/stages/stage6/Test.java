package digrec.stages.stage6;

import java.util.Arrays;
import java.util.Scanner;

public class Test {

	
	public static void main(String[] args) {
		int[] neurons = new int[100];
		NeuronNet wts;
		int i;
		Scanner sc = new Scanner(System.in);
		System.out.println("1. Learn the network\r\n" + 
				"2. Guess all numbers\r\nYour choice: ");
		switch (sc.nextInt()) {
		case 1:
			System.out.print("Enter the sizes of the layers: ");
			i = 0;
			while(sc.hasNextInt()) {
				neurons[i++]= sc.nextInt();
			}
			sc.close();
			System.out.println("Learning...");
			neurons = Arrays.copyOf(neurons, i);
			wts = new NeuronNet(neurons);
			wts.selfLearning(); 
			//wts.selfLearning(7000, 0, 100, 0.5, 10, 0.15, 0, 0); 
			System.out.println("Done. Saved to the file.");
			break;
		case 2:
			sc.close();
			System.out.println("Guessing...");
			wts = NeuronNet.loadFromF();
			int count = 700; // count of each number [0-9]
			//wts.loadInputNumbers(7000, 0);
			wts.loadInputNumbers(count, 1200);
			i=0;
			for(int u = 0; u<count*10;u++) {
				if((int)wts.inputNumbers[u][wts.inputNumbers[0].length-1]==wts.getDigit(wts.inputNumbers[u])) {
					i++;
				}
				//System.out.println("The number \"" +nn.inputNumbers[u][784] + "\" is \"" + nn.takeDigit(nn.inputNumbers[u]) + "\".");
			}
			System.out.printf("The network prediction accuracy: " + i + "/" + (count*10) + ", %1$.2f%1$1%", (double)i*100/(count*10));
			
			break;
		default:
			System.out.println("Unknown comand.");
		}
	}
}
