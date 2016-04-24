package burlap.assignment4.util;

import burlap.assignment4.BasicGridWorld;
import burlap.assignment4.thread.ValueIterationRunner;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.auxiliary.StateReachability;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.singleagent.SADomain;
import burlap.oomdp.singleagent.environment.SimulatedEnvironment;
import burlap.oomdp.statehashing.HashableStateFactory;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalysisRunner {

    final SimpleHashableStateFactory hashingFactory = new SimpleHashableStateFactory();

    private int MAX_ITERATIONS;
    private int NUM_INTERVALS;
    private int REPEATS;

    public AnalysisRunner(int MAX_ITERATIONS, int NUM_INTERVALS, int REPEATS) {
        this.MAX_ITERATIONS = MAX_ITERATIONS;
        this.NUM_INTERVALS = NUM_INTERVALS;
        this.REPEATS = REPEATS;

        int increment = MAX_ITERATIONS / NUM_INTERVALS;
        for (int numIterations = increment; numIterations <= MAX_ITERATIONS; numIterations += increment) {
            AnalysisAggregator.addNumberOfIterations(numIterations);

        }

    }

//    public void runValueIterationMultiThread(BasicGridWorld gen, Domain domain,
//                                             State initialState, RewardFunction rf, TerminalFunction tf, boolean showPolicyMap) {
//        System.out.println("//Value Iteration Analysis//");
//
//        int increment = MAX_ITERATIONS / NUM_INTERVALS;
//
//        List<ValueIterationRunner> threads = new ArrayList<ValueIterationRunner>();
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//
//        for (int numIterations = increment; numIterations <= MAX_ITERATIONS; numIterations += increment) {
//            ValueIterationRunner vir = new ValueIterationRunner(domain, initialState, rf, tf, hashingFactory, numIterations);
//            threads.add(vir);
//            executor.execute(vir);
//        }
//        executor.shutdown();
//        while (!executor.isTerminated()) {
//        }
//        System.out.println("Finished value iteration threads");
//
//        for (int i = 0; i < threads.size(); i++) {
//            AnalysisAggregator.addMillisecondsToFinishValueIteration(threads.get(i).getAnalysisResult().getElapsedTime());
//            AnalysisAggregator.addValueIterationReward(calcRewardInEpisode(threads.get(i).getAnalysisResult().getEpisodeAnalysis()));
//            AnalysisAggregator.addStepsToFinishValueIteration(threads.get(i).getAnalysisResult().getEpisodeAnalysis().numTimeSteps());
//        }
//
////		Visualizer v = gen.getVisualizer();
////		new EpisodeSequenceVisualizer(v, domain, Arrays.asList(ea));
//        ValueIteration vi = threads.get(threads.size() - 1).getAnalysisResult().getValueIteration();
//        Policy p = threads.get(threads.size() - 1).getAnalysisResult().getPolicy();
//
//        AnalysisAggregator.printValueIterationResults();
//        MapPrinter.printPolicyMap(vi.getAllStates(), p, gen.getMap());
//        System.out.println("\n\n");
//        if (showPolicyMap) {
//            simpleValueFunctionVis((ValueFunction) vi, p, initialState, domain, hashingFactory);
//        }
//    }

    public void runValueIteration(BasicGridWorld gen, Domain domain,
                                  State initialState, RewardFunction rf, TerminalFunction tf, boolean showPolicyMap) {
        double[] sumMillis = new double[NUM_INTERVALS];
        double[] sumReward = new double[NUM_INTERVALS];
        double[] sumSteps = new double[NUM_INTERVALS];

        System.out.println("//Value Iteration Analysis//");
        ValueIteration vi = null;
        Policy p = null;
        EpisodeAnalysis ea = null;
        int increment = MAX_ITERATIONS / NUM_INTERVALS;
        for (int i = 0; i < REPEATS; i++) {
            int counter = 0;
            for (int numIterations = increment; numIterations <= MAX_ITERATIONS; numIterations += increment) {
                long startTime = System.nanoTime();
                vi = new ValueIteration(
                        domain,
                        rf,
                        tf,
                        0.99,
                        hashingFactory,
                        -1, numIterations); //Added a very high delta number in order to guarantee that value iteration occurs the max number of iterations
                //for comparison with the other algorithms.

                // run planning from our initial state
                p = vi.planFromState(initialState);

                // evaluate the policy with one roll out visualize the trajectory
                ea = p.evaluateBehavior(initialState, rf, tf);

                // add to sums
                sumMillis[counter] += (int) (System.nanoTime() - startTime) / 1000000;
                sumReward[counter] += calcRewardInEpisode(ea);
                sumSteps[counter++] += ea.numTimeSteps();
            }
        }

        // Process averages
        for (int i = 0; i < NUM_INTERVALS; i++) {
            sumMillis[i] = sumMillis[i] / REPEATS;
            sumReward[i] = sumReward[i] / REPEATS;
            sumSteps[i] = sumSteps[i] / REPEATS;

            AnalysisAggregator.addMillisecondsToFinishValueIteration(sumMillis[i]);
            AnalysisAggregator.addValueIterationReward(sumReward[i]);
            AnalysisAggregator.addStepsToFinishValueIteration(sumSteps[i]);
        }

//		Visualizer v = gen.getVisualizer();
//		new EpisodeSequenceVisualizer(v, domain, Arrays.asList(ea));
        AnalysisAggregator.printValueIterationResults();
        MapPrinter.printPolicyMap(vi.getAllStates(), p, gen.getMap());
        System.out.println("\n\n");
        if (showPolicyMap) {
            simpleValueFunctionVis((ValueFunction) vi, p, initialState, domain, hashingFactory);
        }
    }

    public void runPolicyIteration(BasicGridWorld gen, Domain domain,
                                   State initialState, RewardFunction rf, TerminalFunction tf, boolean showPolicyMap) {
        double[] sumMillis = new double[NUM_INTERVALS];
        double[] sumReward = new double[NUM_INTERVALS];
        double[] sumSteps = new double[NUM_INTERVALS];

        System.out.println("//Policy Iteration Analysis//");
        PolicyIteration pi = null;
        Policy p = null;
        EpisodeAnalysis ea = null;
        int increment = MAX_ITERATIONS / NUM_INTERVALS;
        for (int i = 0; i < REPEATS; i++) {
            int counter = 0;
            for (int numIterations = increment; numIterations <= MAX_ITERATIONS; numIterations += increment) {
                long startTime = System.nanoTime();
                pi = new PolicyIteration(
                        domain,
                        rf,
                        tf,
                        0.99,
                        hashingFactory,
                        -1, 1, numIterations);

                // run planning from our initial state
                p = pi.planFromState(initialState);

                // evaluate the policy with one roll out visualize the trajectory
                ea = p.evaluateBehavior(initialState, rf, tf);

                // add to sums
                sumMillis[counter] += (int) (System.nanoTime() - startTime) / 1000000;
                sumReward[counter] += calcRewardInEpisode(ea);
                sumSteps[counter++] += ea.numTimeSteps();
            }
        }

        // Process averages
        for (int i = 0; i < NUM_INTERVALS; i++) {
            sumMillis[i] = sumMillis[i] / REPEATS;
            sumReward[i] = sumReward[i] / REPEATS;
            sumSteps[i] = sumSteps[i] / REPEATS;

            AnalysisAggregator.addMillisecondsToFinishPolicyIteration(sumMillis[i]);
            AnalysisAggregator.addPolicyIterationReward(sumReward[i]);
            AnalysisAggregator.addStepsToFinishPolicyIteration(sumSteps[i]);
        }

//		Visualizer v = gen.getVisualizer();
//		new EpisodeSequenceVisualizer(v, domain, Arrays.asList(ea));
        AnalysisAggregator.printPolicyIterationResults();

        MapPrinter.printPolicyMap(getAllStates(domain, rf, tf, initialState), p, gen.getMap());
        System.out.println("\n\n");

        //visualize the value function and policy.
        if (showPolicyMap) {
            simpleValueFunctionVis(pi, p, initialState, domain, hashingFactory);
        }
    }

    public void simpleValueFunctionVis(ValueFunction valueFunction, Policy p,
                                       State initialState, Domain domain, HashableStateFactory hashingFactory) {

        List<State> allStates = StateReachability.getReachableStates(initialState,
                (SADomain) domain, hashingFactory);
        ValueFunctionVisualizerGUI gui = GridWorldDomain.getGridWorldValueFunctionVisualization(
                allStates, valueFunction, p);
        gui.initGUI();

    }

    public void runQLearning(BasicGridWorld gen, Domain domain,
                             State initialState, RewardFunction rf, TerminalFunction tf,
                             SimulatedEnvironment env, boolean showPolicyMap) {
        double[] sumMillis = new double[NUM_INTERVALS];
        double[] sumReward = new double[NUM_INTERVALS];
        double[] sumSteps = new double[NUM_INTERVALS];

        System.out.println("//Q Learning Analysis//");

        QLearning agent = null;
        Policy p = null;
        EpisodeAnalysis ea = null;
        int increment = MAX_ITERATIONS / NUM_INTERVALS;
        for (int i = 0; i < REPEATS; i++) {
            int counter = 0;
            for (int numIterations = increment; numIterations <= MAX_ITERATIONS; numIterations += increment) {
                long startTime = System.nanoTime();

                agent = new QLearning(
                        domain,
                        0.99,
                        hashingFactory,
                        0.99, 0.1);

                for (int k = 0; k < numIterations; k++) {
                    ea = agent.runLearningEpisode(env);
                    env.resetEnvironment();
                }
                agent.initializeForPlanning(rf, tf, 1);
                p = agent.planFromState(initialState);

                // add to sums
                sumMillis[counter] += (int) (System.nanoTime() - startTime) / 1000000;
                sumReward[counter] += calcRewardInEpisode(ea);
                sumSteps[counter++] += ea.numTimeSteps();
            }
        }

        // Process averages
        for (int i = 0; i < NUM_INTERVALS; i++) {
            sumMillis[i] = sumMillis[i] / REPEATS;
            sumReward[i] = sumReward[i] / REPEATS;
            sumSteps[i] = sumSteps[i] / REPEATS;

            AnalysisAggregator.addMillisecondsToFinishQLearning(sumMillis[i]);
            AnalysisAggregator.addQLearningReward(sumReward[i]);
            AnalysisAggregator.addStepsToFinishQLearning(sumSteps[i]);
        }

        AnalysisAggregator.printQLearningResults();
        MapPrinter.printPolicyMap(getAllStates(domain, rf, tf, initialState), p, gen.getMap());
        System.out.println("\n\n");

        //visualize the value function and policy.
        if (showPolicyMap) {
            simpleValueFunctionVis((ValueFunction) agent, p, initialState, domain, hashingFactory);
        }

    }

    private static List<State> getAllStates(Domain domain,
                                            RewardFunction rf, TerminalFunction tf, State initialState) {
        ValueIteration vi = new ValueIteration(
                domain,
                rf,
                tf,
                0.99,
                new SimpleHashableStateFactory(),
                .5, 100);
        vi.planFromState(initialState);

        return vi.getAllStates();
    }

    public double calcRewardInEpisode(EpisodeAnalysis ea) {
        double myRewards = 0;

        //sum all rewards
        for (int i = 0; i < ea.rewardSequence.size(); i++) {
            myRewards += ea.rewardSequence.get(i);
        }
        return myRewards;
    }

}
