package digrec.tests;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;

import digrec.stages.stage3.Weights;
import digrec.stages.stage6.MatrixMath;
import digrec.stages.stage6.NeuronNet;
import digrec.stages.stage6.Assets;
public class Test  {

		  
	public static void main(String[] args) {
		
		Timer timer =  new Timer();
		long time = 0;
		//for(int i = 0; i<n; i++) {
			time += timer.meTime(new Func());
		//}
		System.out.println(time);
		
		  
	    //System.out.println(Arrays.deepToString(arr3));
	    
	}

}

class Func implements Runnable {

	@Override
	public void run() {
		//Assets newInput = new Assets();
		
		//newInput.fillTrainingSamples();
		//newInput.getinputSample(7000*8+2);
		//newInput = newInput.loadFromF();
		
		/*for(int i = 0; i<785; i++) {
			System.out.print(newInput.trainingSamples[6][15][i]+ "\t");
			if((i+1)%28==0 || i==784) {
				System.out.println();
			}
		}*/
		
		//System.out.println(newInput.inputSample[784]);
		
		

		
		
		NeuronNet nn = new NeuronNet (784,15,15,10);//(15,3,4,10);
		//Weights wts = new Weights();
		//nn.CountIdealNeurons();
		//for(int i=0; i<10;i++) {
		//System.out.println(nn.getWeights().length + "\t"+nn.getWeights()[0].length+ "\t"+nn.getWeights()[0][0].length);	
		//}
		/*nn.loadInputNumbers(1, 5);
		nn.learnNeuronNet(0.5);*/

		//nn.selfLearning();
		//nn.selfLearning(100,6000,0);
		
		//nn.printArray(nn.getWeights());
		//wts.selfLearning();
		//wts.learnNeuNet();
		//nn.printArray(wts.getWeights());
		//System.out.println();
		//nn.selfLearning(100);
		//nn.printArray(nn.getWeights());
		
		nn = NeuronNet.loadFromF();
		nn.loadInputNumbers(1000, 60001);
		int i=0;
		for(int u = 0; u<10000;u++) {
			if((int)nn.inputNumbers[u][784]==nn.getDigit(nn.inputNumbers[u])) {
				i++;
			}
			//System.out.println("The number \"" +nn.inputNumbers[u][784] + "\" is \"" + nn.takeDigit(nn.inputNumbers[u]) + "\".");
		}
		System.out.println((double)i/100 + "%");
		//for(int u = 0; u<10;u++) {
		//System.out.println("It's a \"" + nn.takeDigit(nn.idealInputNeurones[u]) + "\".");
		//}
		
	}
	
	public void saveToF(Assets as) {
		 
	    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("nnw5.bin"))) {
			out.writeObject(as);
			System.out.println("Saved successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	public Assets loadFromF() {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("nnw5.bin"))) {
			Assets as = (Assets)in.readObject();
			System.out.println("Loaded successfully.");	
			return as;
		} catch (ClassNotFoundException|IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
}

class Timer{
	public long meTime(Runnable run) {
		long start = System.currentTimeMillis();
		run.run();
		return System.currentTimeMillis()-start;
	}
}
