package digrec.stages.stage4;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * @version 4.5
 *
 */
public class NeuronNet implements Serializable {

	private static final long serialVersionUID = 328778L;
	private final int LAYERS;
	private final int [] NEURONS_IN_LAYERS;
	private double [][][] weights;
	public double [][] idealNeurons; 	// non-rectangle matrix!
	public double [][][] deltaW;	
	
	
	
		
	public double [][] idealInputNeurones = {
			{1,1,1,1,0,1,1,0,1,1,0,1,1,1,1}, //0
			{0,1,0,0,1,0,0,1,0,0,1,0,0,1,0}, //1
			{1,1,1,0,0,1,1,1,1,1,0,0,1,1,1}, //2
			{1,1,1,0,0,1,1,1,1,0,0,1,1,1,1}, //3
			{1,0,1,1,0,1,1,1,1,0,0,1,0,0,1}, //4
			{1,1,1,1,0,0,1,1,1,0,0,1,1,1,1}, //5
			{1,1,1,1,0,0,1,1,1,1,0,1,1,1,1}, //6
			{1,1,1,0,0,1,0,0,1,0,0,1,0,0,1}, //7
			{1,1,1,1,0,1,1,1,1,1,0,1,1,1,1}, //8
			{1,1,1,1,0,1,1,1,1,0,0,1,1,1,1}  //9
			};
	int iInLength = idealInputNeurones.length;
	
	
	public NeuronNet() {
		NEURONS_IN_LAYERS = new int[]{15,10};
		LAYERS = NEURONS_IN_LAYERS.length;
		weights = new double [LAYERS-1][][];
		Random rd = new Random(serialVersionUID);
		for (int l=0;l<(LAYERS-1);l++) {
			weights[l] = new double[NEURONS_IN_LAYERS[l+1]][];
			for (int i=0;i<NEURONS_IN_LAYERS[l+1];i++) {
				weights[l][i] = new double[NEURONS_IN_LAYERS[l]+1];
				for (int j = 0; j<NEURONS_IN_LAYERS[l]+1;j++){
					weights[l][i][j]= rd.nextGaussian();
				}
			}
		}
		
	}
	
	public NeuronNet(int... neuronsInLayers) {
		LAYERS = neuronsInLayers.length;
		NEURONS_IN_LAYERS = neuronsInLayers.clone();
		weights = new double [LAYERS-1][][];
		Random rd = new Random(328778L);
		for (int l=0;l<(LAYERS-1);l++) {
			weights[l] = new double[NEURONS_IN_LAYERS[l+1]][];
			for (int i=0;i<NEURONS_IN_LAYERS[l+1];i++) {
				weights[l][i] = new double[NEURONS_IN_LAYERS[l]+1];
				for (int j = 0; j<NEURONS_IN_LAYERS[l]+1;j++){
					weights[l][i][j]= rd.nextGaussian();
				}
			}
		}
	}

	
	/**
	 * @return	double [][][] new array, don't return reference
	 */
	public double[][][] getWeights() {
		double[][][] wts = new double [LAYERS-1][][];
		int l;
		for(int i = 0;i<LAYERS-1;i++) {
			l = weights[i].length;
			wts[i] = new double [l][];
			for(int j = 0;j<l;j++) {
				wts[i][j] = weights[i][j].clone();
			}
		}
		return wts;
	}

	/**
	 *	Method don't change the array passed as an argument.
	 * @param weights double [][][] array isn't changing in method 
	 */
	public void setWeights(double[][][] weights) {
		int l;
		for(int i = 0;i<LAYERS-1;i++) {
			l = weights[i].length;
			this.weights[i]= new double [l][];
			for(int j = 0;j<l;j++) {
				this.weights[i][j] = weights[i][j].clone();
			}
		}
	}
	
	public void saveToF() {
		 
	    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("nnw4.bin"))) {
			out.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	   // System.out.println("Saved successfully.");
	}
	
	public void loadFromF() {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("nnw4.bin"))) {
			this.setWeights(((NeuronNet)in.readObject()).getWeights());
						
		} catch (ClassNotFoundException|IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Loaded successfully.");
	}
	
	
	public void learnNeuronNet() {
		final double eta = 0.5;
		double [][] neurons = new double[2][]; 		

		deltaW = new double[weights.length][][];	
		
		idealNeurons = new double [LAYERS-1][]; 	// non-rectangle matrix!
		for (int l=0;l<LAYERS-1;l++) { 		//layer
			idealNeurons[l] = new double [NEURONS_IN_LAYERS[l+1]];
			deltaW[l] = new double[weights[l].length][];
		}
		if(LAYERS >2) {
			CountIdealNeurons();
		}

		for(int n =0;n<iInLength;n++) {									// input number
			neurons[1] = idealInputNeurones[n].clone();

			for (int l=0;l<LAYERS-1;l++) { 								//layer
				neurons[0] = new double[neurons[1].length+1];
				for(int b = 0; b<neurons[1].length; b++) {
					neurons[0][b] = neurons[1][b];
				}
				neurons[0][neurons[0].length-1] = 1.0;					// bias neuron for previous layer
				neurons[1] = MatrixMath.ActivateNeuron(neurons[0], weights[l]);
				
				for(int i = 0;i<weights[l].length;i++) {
					if (deltaW[l][i] == null) {
						deltaW[l][i] = new double[weights[l][i].length];
					}
					if (l ==LAYERS-2) {
						idealNeurons[l][i] = (i==n?1:0);
					}
					for(int j = 0; j<weights[l][i].length; j++) {
						deltaW[l][i][j] += eta*neurons[0][j]*(idealNeurons[l][i]-neurons[1][i]);
					}
				}	
			}
			
		}	
		for (int l = 0;l<LAYERS-1;l++)	{
			for (int v = 0;v<NEURONS_IN_LAYERS[l+1];v++)	{
				for (int k = 0; k<NEURONS_IN_LAYERS[l];k++){
					weights[l][v][k]+= deltaW[l][v][k] /iInLength;
				}
				
			}	
			
		}
		
	}
	
	
	public void CountIdealNeurons() {
		double iIL = 1/(double)iInLength;
		
		for(int l = LAYERS-3;l>=0;l--) {								// layer
			for(int n =0;n<iInLength;n++) {								// input number
				if(l == LAYERS-3) {										// only for the second to the last layer
					for(int ln = 0;ln<NEURONS_IN_LAYERS[LAYERS-1];ln++) { 
						idealNeurons[LAYERS-2][ln] = (ln==n)?1:0;		// Ideal Output neurons
					}
				}
				for (int i=0;i<NEURONS_IN_LAYERS[l+1];i++) {
					for (int j = 0; j<NEURONS_IN_LAYERS[l+2];j++){
						/*if(idealNeurons[l+1][j]==0.0) {
								continue;
						}*/
						idealNeurons[l][i] += idealNeurons[l+1][j]/weights[l+1][j][i] *iIL;
					}
					
				}
				
			}
			/*for(int m = 0; m<idealNeurons[l].length; m++) {
				idealNeurons[l][m] = idealNeurons[l][m]*iIL;
				idealNeurons[l][m] = MatrixMath.Sigmoid(idealNeurons[l][m]);
				
				//idealNeurons[l][m] =(idealNeurons[l][m] < 0)?0:idealNeurons[l][m];
				//idealNeurons[l][m] =(idealNeurons[l][m]>1?1:idealNeurons[l][m]);
			}*/
		}
	}

public void printArray(double [][] array) {
	String a = Arrays.deepToString(array);
	a = a.replace("],", "]\n");
	System.out.println(a);
	System.out.println();
}

public void printArray(double [][][] array) {
	String a = Arrays.deepToString(array);
	a = a.replace("],", "]\n");
	a = a.replace("]]", "]]\n");
	System.out.println(a);
	System.out.println();
}
	
public int takeDigit(double [] inNeurons) {
	int digit = -1;
	double bestRes = -1000.0;
	double [][] neurons = new double[2][]; 	
	neurons[1] = inNeurons.clone();
	
	for (int l=0;l<LAYERS-1;l++) { 								//layer
		neurons[0] = new double[neurons[1].length+1];
		for(int b = 0; b<neurons[1].length; b++) {
			neurons[0][b] = neurons[1][b];
		}
		neurons[0][neurons[0].length-1] = 1.0;					// bias neuron for previous layer
		neurons[1] = MatrixMath.ActivateNeuron(neurons[0], weights[l]);
	}
	for (int i=0;i<NEURONS_IN_LAYERS[LAYERS-1];i++) {
		if(neurons[1][i]>bestRes) {
			bestRes = neurons[1][i];
			digit = i;
		}	
	}
		return digit;
}
	
	
	public void selfLearning (int iteratons) {
		System.out.println("Learning...");
		double max = -1;
		for(int i = 0;i<iteratons;i++) {
			double[][][] oldWts = getWeights();
			learnNeuronNet();
			double dif;
			max = 0;
			for (int l = 0;l<LAYERS-1;l++)	{
				for (int v = 0;v<NEURONS_IN_LAYERS[l+1];v++)	{
					for (int k = 0; k<NEURONS_IN_LAYERS[l];k++){
						 dif= Math.abs(weights[l][v][k] - oldWts[l][v][k]);
						 if(dif>max) max = dif;
					}
				}
			}

			//if(max<=0.02) {
				//System.out.println("Done "+ i +" iteration.  ");
				//System.out.println(max);
				//break;		
			//}

		}
		saveToF();
		System.out.println("Saved to a file.");
		System.out.println(max);
	}

}


