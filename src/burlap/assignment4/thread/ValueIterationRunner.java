package burlap.assignment4.thread;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.oomdp.core.Domain;
import burlap.oomdp.core.TerminalFunction;
import burlap.oomdp.core.states.State;
import burlap.oomdp.singleagent.RewardFunction;
import burlap.oomdp.statehashing.SimpleHashableStateFactory;

/**
 * Created by avy on 4/17/16.
 */
public class ValueIterationRunner implements Runnable {

    private Domain domain;
    private State initialState;
    private RewardFunction rf;
    private TerminalFunction tf;
    private SimpleHashableStateFactory hashingFactory;
    private int numIterations;
    private AnalysisResult analysisResult;

    public ValueIterationRunner(Domain domain, State initialState, RewardFunction rf, TerminalFunction tf, SimpleHashableStateFactory hashingFactory, int numIterations) {
        this.domain = domain;
        this.initialState = initialState;
        this.rf = rf;
        this.tf = tf;
        this.hashingFactory = hashingFactory;
        this.numIterations = numIterations;
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        ValueIteration vi = new ValueIteration(
                domain,
                rf,
                tf,
                0.99,
                hashingFactory,
                -1, numIterations); //Added a very high delta number in order to guarantee that value iteration occurs the max number of iterations
        //for comparison with the other algorithms.

        // run planning from our initial state
        Policy p = vi.planFromState(initialState);

        // Get time complete
        int elapsed = (int) (System.nanoTime()-startTime)/1000000;

        // Perform episode analysis
        EpisodeAnalysis ea = p.evaluateBehavior(initialState, rf, tf);

        analysisResult = new AnalysisResult();
        analysisResult.setElapsedTime(elapsed);
        analysisResult.setEpisodeAnalysis(ea);
        analysisResult.setPolicy(p);
        analysisResult.setValueIteration(vi);
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }
}
