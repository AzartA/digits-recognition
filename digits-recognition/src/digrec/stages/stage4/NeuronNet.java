package digrec.stages.stage4;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

/**
 * @version 4.3
 *
 */
public class NeuronNet implements Serializable {

	private static final long serialVersionUID = 328778L;
	private final int LAYERS;
	private final int [] NEURONS_IN_LAYERS;
	private double [][][] weights;
	
	public NeuronNet() {
		NEURONS_IN_LAYERS = new int[]{15,10};
		LAYERS = NEURONS_IN_LAYERS.length;
		weights = new double [LAYERS-1][][];
		Random rd = new Random(328778L);
		for (int l=0;l<(LAYERS-1);l++) {
			weights[l] = new double[NEURONS_IN_LAYERS[l]][];
			for (int i=0;i<NEURONS_IN_LAYERS[l];i++) {
				weights[l][i] = new double[NEURONS_IN_LAYERS[l+1]];
				for (int j = 0; j<NEURONS_IN_LAYERS[l+1];j++){
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
			weights[l] = new double[NEURONS_IN_LAYERS[l]][];
			for (int i=0;i<NEURONS_IN_LAYERS[l];i++) {
				weights[l][i] = new double[NEURONS_IN_LAYERS[l+1]];
				for (int j = 0; j<NEURONS_IN_LAYERS[l+1];j++){
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
			wts[i] = weights[i].clone();
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
			this.weights[i]= weights[i].clone();
			for(int j = 0;j<l;j++) {
				this.weights[i][j] = weights[i][j].clone();
			}
		}
	}
	
	public void saveToF() {
		 
	    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("nnw.bin"))) {
			out.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	   // System.out.println("Saved successfully.");
	}
	
	public void loadFromF() {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("nnw.bin"))) {
			this.setWeights(((NeuronNet)in.readObject()).getWeights());
						
		} catch (ClassNotFoundException|IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Loaded successfully.");
	}
	
	
	public void learnNeuronNet() {
		final double nju = 0.5;
		
		int [][] idealInputNeurones = {
			{1,1,1,1,0,1,1,0,1,1,0,1,1,1,1,1}, //0
			{0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,1}, //1
			{1,1,1,0,0,1,1,1,1,1,0,0,1,1,1,1}, //2
			{1,1,1,0,0,1,1,1,1,0,0,1,1,1,1,1}, //3
			{1,0,1,1,0,1,1,1,1,0,0,1,0,0,1,1}, //4
			{1,1,1,1,0,0,1,1,1,0,0,1,1,1,1,1}, //5
			{1,1,1,1,0,0,1,1,1,1,0,1,1,1,1,1}, //6
			{1,1,1,0,0,1,0,0,1,0,0,1,0,0,1,1}, //7
			{1,1,1,1,0,1,1,1,1,1,0,1,1,1,1,1}, //8
			{1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1}  //9
			};
		double [][] deltaW = new double[10][16];
		double outNeuron;
		
		for(int n =0;n<10;n++) {
			for (int i=0;i<10;i++) {
				outNeuron = 0;
				for (int j = 0; j<16;j++){
					//outNeuron+= idealInputNeurones[n][j]*weights[i][j];
				}
				outNeuron = sigmoid(outNeuron);	
				outNeuron = (i==n?1:0)- outNeuron;
				for (int k = 0; k<16;k++){
					deltaW[i][k] += nju*idealInputNeurones[n][k]*outNeuron;
				}
			}
		
		}	
			
		for (int v = 0;v<10;v++)	{
			for (int k = 0; k<16;k++){
				//this.weights[v][k]+= deltaW[v][k] *0.1;
			}
		}
	}
	
	private double sigmoid(double weight) {
		return 1/(1+ Math.pow(Math.E, -weight));
	}
	
	public int takeDigit(int[] inNeurons) {
		int digit = -1;
		double [] outNeurons = new double[10];
		double bestRes = -1000.0;
		for (int i=0;i<10;i++) {
			for (int j = 0; j<16;j++){
				//outNeurons[i]+= inNeurons[j]*weights[i][j];
			}
			outNeurons[i] = sigmoid(outNeurons[i]);
			if(outNeurons[i]>bestRes) {
				bestRes = outNeurons[i];
				digit = i;
			}
		}
		return digit;
	}
	
	public void selfLearning () {
		System.out.println("Learning...");
		for(int i = 0;i<100;i++) {
			double[][] oldWts = new double[10][16];
			for(int j = 0;j<10;j++) {
				//oldWts[j] = weights[j].clone();
			}
			
			learnNeuronNet();
			double dif;
			double max = 0;
			for (int v = 0;v<10;v++)	{
				for (int k = 0; k<16;k++){
					// dif= Math.abs(weights[v][k] - oldWts[v][k]);
					// if(dif>max) max = dif;
				}
			}
			
			if(max<=0.02) {
				System.out.println("Done "+ i +" iteration.");
				break;		
			}
		}
		saveToF();
		System.out.println("Saved to a file.");
	}

}


