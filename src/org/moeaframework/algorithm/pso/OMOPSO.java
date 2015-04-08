package org.moeaframework.algorithm.pso;

import org.moeaframework.core.EpsilonBoxDominanceArchive;
import org.moeaframework.core.PRNG;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.variable.RealVariable;

// NOTE: This implementation is derived from the original manuscripts and the
// JMetal implementation.

/**
 * Implementation of OMOPSO, a multi-objective particle swarm optimizer (MOPSO).
 * According to [2], OMOPSO is one of the top-performing PSO algorithms.
 * <p>
 * References:
 * <ol>
 *   <li>Sierra, M. R. and C. A. Coello Coello (2005).  Improving PSO-based
 *       Multi-Objective Optimization using Crowding, Mutation and
 *       &epsilon;-Dominance.  Evolutionary Multi-Criterion Optimization,
 *       pp. 505-519.
 *   <li>Durillo, J. J., J. Garc�a-Nieto, A. J. Nebro, C. A. Coello Coello,
 *       F. Luna, and E. Alba (2009).  Multi-Objective Particle Swarm
 *       Optimizers: An Experimental Comparison.  Evolutionary Multi-Criterion
 *       Optimization, pp. 495-509.
 * </ol>
 */
public class OMOPSO extends AbstractPSOAlgorithm {

	private final Variation uniformMutation;
	
	private final Variation nonUniformMutation;
	
	public OMOPSO(Problem problem, int swarmSize, int leaderSize,
			double[] epsilons, double mutationProbability,
			double mutationPerturbation, int maxIterations) {
		super(problem, swarmSize, leaderSize, new CrowdingComparator(),
				new ParetoDominanceComparator(),
				new CrowdingPopulation(leaderSize),
				new EpsilonBoxDominanceArchive(epsilons),
				null);
		this.uniformMutation = new UniformMutation(mutationProbability,
				mutationPerturbation);
		this.nonUniformMutation = new NonUniformMutation(mutationProbability,
				mutationPerturbation, maxIterations);
	}
	
	@Override
	protected void mutate(int i) {
		if (i % 3 == 0) {
			particles[i] = nonUniformMutation.evolve(new Solution[] {
					particles[i] })[0];
		} else if (i % 3 == 1) {
			particles[i] = uniformMutation.evolve(new Solution[] {
					particles[i] })[0];
		}
	}
	
	public class NonUniformMutation implements Variation {
		
		private double probability = 1.0 / problem.getNumberOfVariables();
		
		private double perturbation = 0.5;
		
		private int maxIterations = 250;
		
		public NonUniformMutation(double probability, double perturbation,
				int maxIterations) {
			super();
			this.probability = probability;
			this.perturbation = perturbation;
			this.maxIterations = maxIterations;
		}

		@Override
		public int getArity() {
			return 1;
		}

		@Override
		public Solution[] evolve(Solution[] parents) {
			Solution offspring = parents[0].copy();
			
			for (int i = 0; i < offspring.getNumberOfVariables(); i++) {
				if (PRNG.nextDouble() < probability) {
					RealVariable variable = (RealVariable)offspring.getVariable(i);
					double value = variable.getValue();
					
					if (PRNG.nextBoolean()) {
						value += getDelta(variable.getUpperBound() - value);
					} else {
						value += getDelta(variable.getLowerBound() - value);
					}

					if (value < variable.getLowerBound()) {
						value = variable.getLowerBound();
					} else if (value > variable.getUpperBound()) {
						value = variable.getUpperBound();
					}
					
					variable.setValue(value);
				}
			}
			
			return new Solution[] { offspring };
		}
		
		public double getDelta(double difference) {
			int currentIteration = getNumberOfEvaluations() / swarmSize;
			double fraction = currentIteration / (double)maxIterations;
			
			return difference * (1.0 - Math.pow(PRNG.nextDouble(), 
					Math.pow(1.0 - fraction, perturbation)));
		}

	}
	
	public class UniformMutation implements Variation {
		
		private double probability = 1.0 / problem.getNumberOfVariables();
		
		private double perturbation = 0.5;
		
		public UniformMutation(double probability, double perturbation) {
			super();
			this.probability = probability;
			this.perturbation = perturbation;
		}

		@Override
		public int getArity() {
			return 1;
		}

		@Override
		public Solution[] evolve(Solution[] parents) {
			Solution offspring = parents[0].copy();
			
			for (int i = 0; i < offspring.getNumberOfVariables(); i++) {
				if (PRNG.nextDouble() < probability) {
					RealVariable variable = (RealVariable)offspring.getVariable(i);
					double value = variable.getValue();
					
					value += (PRNG.nextDouble() - 0.5) * perturbation;
					
					if (value < variable.getLowerBound()) {
						value = variable.getLowerBound();
					} else if (value > variable.getUpperBound()) {
						value = variable.getUpperBound();
					}
					
					variable.setValue(value);
				}
			}
			
			return new Solution[] { offspring };
		}
		
	}

}
