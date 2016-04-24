package burlap.assignment4.thread;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.EpisodeAnalysis;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;

/**
 * Created by avy on 4/17/16.
 */
public class AnalysisResult {
    private Policy policy;
    private EpisodeAnalysis episodeAnalysis;
    private int elapsedTime;
    private ValueIteration valueIteration;

    public ValueIteration getValueIteration() {
        return valueIteration;
    }

    public void setValueIteration(ValueIteration valueIteration) {
        this.valueIteration = valueIteration;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public EpisodeAnalysis getEpisodeAnalysis() {
        return episodeAnalysis;
    }

    public void setEpisodeAnalysis(EpisodeAnalysis episodeAnalysis) {
        this.episodeAnalysis = episodeAnalysis;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
