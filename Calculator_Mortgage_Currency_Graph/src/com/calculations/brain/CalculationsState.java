package com.calculations.brain;

import java.util.ArrayList;

import android.widget.ArrayAdapter;

import com.humby.fragments.GraphView;
import com.support.calculations.CalcItem;
import com.support.calculations.ComplexNumber;
import com.support.calculations.ComplexNumberStr;

public class CalculationsState {

	GraphView graph;
	Calculator graphCalcs = null;
	float[] zeros;
	public int traceLoc, fnState;
	Calculator copyCalc = new Calculator ();;
	String shared;

	public CalculationsState(){
		graphCalcs=new Calculator ();
		zeros = new float[128];
		fnState = 0;
	}


	public void setGlobalRounding(int round) {
		graphCalcs.round = round;

	}

	public void setGlobalAngleMode(boolean rad) {
		graphCalcs.angleMode = rad;

	}

	public Calculator getGraphCalcs () {
		return graphCalcs;
	}

	public void setGraphView (GraphView graph) {
		this.graph = graph;
	}

	void clearArr (float[] arr) {
		for (int i=0;i<arr.length;i+=1) {
			arr[i] = 0;
		}
	}

	public void plotFns () {
		plotFns (getFnPts ());
	}

	public void initFns () {

		graphCalcs.initForGraph ();        
	}

	public void resetCalcs () {

		if (!graphCalcs.empty ()) {
			graphCalcs.initForFnEntry ();
		}

	}

	public void plotFns (float []fnPts) {

		if (!graphCalcs.empty ()) {
			/*
	                Log.v ("plotFns",Float.toString(graph.getXMax())+" "+
	                       Float.toString(graph.getYMax())+" "+Float.toString(graph.getXLeft ())+
	                       " "+Float.toString(graph.getXRight ())+" "+
	                       Float.toString(graph.getYBot ())+" "+Float.toString(graph.getYTop ()));
			 */
			graphCalcs.graphFn (fnPts, graph.getXLeft (),graph.getXRight(),
					graph.getYBot (),graph.getYTop (),
					graph.getXMin (),graph.getXMax (),
					graph.getYMin (),graph.getYMax (),
					graph.getXUnitLen());
			/*
	                for (int j=0;j<N_FN_PTS*4;j+=4) {
	                    Log.v ("plotFns", Float.toString(fnPts[i][j])+","+Float.toString(fnPts[i][j+1])+
	                           "   "+ Float.toString(fnPts[i][j+2])+","+Float.toString(fnPts[i][j+3]));
	                }
			 */
		} else {
			clearArr (fnPts);
		}

	}

	public void calcZeros (ArrayAdapter<String> zerosArr) {
		int n;

		if (!graphCalcs.empty ()) {

			n = graphCalcs.calcZeros (zeros, graph.getXLeft (),graph.getXRight(),
					graph.getYBot (),graph.getYTop (),
					graph.getXMin (),graph.getXMax (),
					graph.getYMin (),graph.getYMax (),
					graph.getXUnitLen());

			// Convert x values of zeros to strings
			for (int k=0;k<n;k+=1) {
				float num=zeros[k];
				if ((num > -0.001 && num < 0) ||
						(num < 0.001 && num > 0))
					num = 0;
				String numStr = ComplexNumber.roundStr (num, 3);
				zerosArr.add ("   x = "+numStr);
			}
		}

	}

	public void initTrace () {

		traceLoc = (int)((graph.getXMin () + graph.getXMax ()) / 2);

	}

	public void updateTrace (int dx) {

		traceLoc += dx;
		if (traceLoc > graph.getXMax ())
			traceLoc = (int)graph.getXMax ();
		else if (traceLoc < graph.getXMin ())
			traceLoc = (int)graph.getXMin ();

	}

	public float getYTracePt (float xval) {
		return graphCalcs.getTracePt (xval);
	}

	public int getYGraphPt (float val) {
		return (int)graph.getYMax () -
				graphCalcs.realToGraph (val, graph.getYBot (), graph.getYTop (),
						graph.getYMin (), graph.getYMax());
	}

	public Calculator getGraphCalc () {
		return graphCalcs;
	}

	public boolean isCalcEmpty () {
		return graphCalcs.empty ();
	}


	/*
	    public int getNumFnPts () {
	        return N_FN_PTS;
	    }
	 */
	public float[] getFnPts () {
		return graph.fnPts;
	}

	public void getCopy (Calculator calc) {
		calc.viewStr = copyCalc.viewStr;
		calc.tokens = new ArrayList<CalcItem>(copyCalc.tokens);
		calc.tokenLens = new ArrayList<Integer>(copyCalc.tokenLens);
		calc.viewIndex = copyCalc.viewIndex;
	}

	public void setCopy (Calculator calc) {
		copyCalc.viewStr = calc.viewStr;
		copyCalc.tokens = new ArrayList<CalcItem>(calc.tokens);
		copyCalc.tokenLens = new ArrayList<Integer>(calc.tokenLens);
		copyCalc.viewIndex = calc.viewIndex;
	}

	public String getConvCopyString () {
		return copyCalc.calcOnTheFly();
	}

	public void setCopy (String val) {
		copyCalc.viewStr = val;
		copyCalc.tokens = new ArrayList<CalcItem>();
		copyCalc.tokenLens = new ArrayList<Integer>();
		for (int i=0;i<val.length ();i+=1) {
			copyCalc.tokens.add (new ComplexNumberStr (val.substring (i,i+1)));
			copyCalc.tokenLens.add (1);
		}
		copyCalc.viewIndex = val.length ();
	}






}
