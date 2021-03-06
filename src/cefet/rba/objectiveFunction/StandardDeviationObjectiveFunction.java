/*
 * Copyright (C) 2016 Renato Barros Arantes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cefet.rba.objectiveFunction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cefet.rba.chromossome.Chromosome;
import cefet.rba.chromossome.MagicSquareChromosome;

public class StandardDeviationObjectiveFunction implements ObjectiveFunction {

	private static final Logger log4j = LogManager.getLogger(StandardDeviationObjectiveFunction.class.getName());

	private final int MAGIC_CONSTANT;
	
	public StandardDeviationObjectiveFunction(int n) {
		MAGIC_CONSTANT = n*((n*n)+1)/2;
	}
	
	private double applyRestrictions(MagicSquareChromosome magicSquareChromosome, double low, double high, double multiplyFactor) {
		double r = 0.0;
		for (int i = 0, sz = magicSquareChromosome.getSummation().length; i < sz; i++) {
			double rl = -magicSquareChromosome.getSummation()[i] + (MAGIC_CONSTANT-low);
			double rh =  magicSquareChromosome.getSummation()[i] - (MAGIC_CONSTANT+high);
			if (rl > 0) {
				r+= multiplyFactor*rl;
			}
			if (rh > 0) {
				r+= multiplyFactor*rh;
			}			
		}
		return r;
	}
	
	@Override
	public double evaluate(Chromosome<?> chromosome) {
		double result = 0.0;
		MagicSquareChromosome magicSquareChromosome = (MagicSquareChromosome)chromosome;
		for (int i = 0, sz = magicSquareChromosome.getSummation().length; i < sz; i++) {
			result+= Math.pow(magicSquareChromosome.getSummation()[i] - MAGIC_CONSTANT, 2.0);
		}
		result = Math.sqrt(result/magicSquareChromosome.getSummation().length) /*+ applyRestrictions(magicSquareChromosome, 1.0, 1.0, 100)*/;
		if (log4j.isDebugEnabled()) log4j.debug(chromosome+" = "+result);
		return result;
	}
	
	public int getMagicConstant() {
		return MAGIC_CONSTANT;
	}
}
