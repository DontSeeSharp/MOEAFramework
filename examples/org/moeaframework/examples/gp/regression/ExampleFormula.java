/* Copyright of original 2009-2019 David Hadka
 *
 * This is a modified version of QuatricExample.java, which is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.moeaframework.examples.gp.regression;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * The function is
 * {@code f(x) = sin(x) + cos(x).
 * 
 * References:
 * <ol>
 *   <li>Koza, J.R.  "Genetic Programming: On the Programming of Computers by
 *       Means of Natural Selection."  MIT Press, Cambridge, MA, USA, 1992.
 * </ol>
 */
public class ExampleFormula implements UnivariateFunction {
	
	/**
	 * Runs the Quartic demo problem.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SymbolicRegressionGUI.runDemo(new SymbolicRegression(
				new ExampleFormula(), -Math.PI, Math.PI, 50));
	}

	@Override
	public double value(double x) {
		return Math.sin(x) + Math.cos(x);
	}

}
