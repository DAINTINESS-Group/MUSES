package algorithms;

import java.util.ArrayList;

import parameters.Dataset;
import parameters.SupportType;
import datastructures.TableHistorySequence;

public  class SupportCounterFactory {

	public static SupportCounter createSupportCounter(SupportType supportType,ArrayList<TableHistorySequence> sequences,Dataset ds){
		if(supportType == SupportType.COBJ){
			return new COBJCounter(sequences,ds);
		}
		else{
			return new CDISTCounter(sequences,ds);
		}
	}
}
