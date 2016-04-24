package burlap.assignment4.util;

import java.util.ArrayList;
import java.util.List;

public final class AnalysisAggregator {
    private static List<Number> numIterations = new ArrayList<Number>();
    private static List<Number> stepsToFinishValueIteration = new ArrayList<Number>();
    private static List<Number> stepsToFinishPolicyIteration = new ArrayList<Number>();
    private static List<Number> stepsToFinishQLearning = new ArrayList<Number>();

    private static List<Number> millisecondsToFinishValueIteration = new ArrayList<Number>();
    private static List<Number> millisecondsToFinishPolicyIteration = new ArrayList<Number>();
    private static List<Number> millisecondsToFinishQLearning = new ArrayList<Number>();

    private static List<Number> rewardsForValueIteration = new ArrayList<Number>();
    private static List<Number> rewardsForPolicyIteration = new ArrayList<Number>();
    private static List<Number> rewardsForQLearning = new ArrayList<Number>();

    public static void addNumberOfIterations(Number numIterations1) {
        numIterations.add(numIterations1);
    }

    public static void addStepsToFinishValueIteration(Number stepsToFinishValueIteration1) {
        stepsToFinishValueIteration.add(stepsToFinishValueIteration1);
    }

    public static void addStepsToFinishPolicyIteration(Number stepsToFinishPolicyIteration1) {
        stepsToFinishPolicyIteration.add(stepsToFinishPolicyIteration1);
    }

    public static void addStepsToFinishQLearning(Number stepsToFinishQLearning1) {
        stepsToFinishQLearning.add(stepsToFinishQLearning1);
    }

    public static void printValueIterationResults() {
        System.out.print("Value Iteration,");
        printList(stepsToFinishValueIteration);
    }

    public static void printPolicyIterationResults() {
        System.out.print("Policy Iteration,");
        printList(stepsToFinishPolicyIteration);
    }

    public static void printQLearningResults() {
        System.out.print("Q Learning,");
        printList(stepsToFinishQLearning);
    }


    public static void addMillisecondsToFinishValueIteration(Number millisecondsToFinishValueIteration1) {
        millisecondsToFinishValueIteration.add(millisecondsToFinishValueIteration1);
    }

    public static void addMillisecondsToFinishPolicyIteration(Number millisecondsToFinishPolicyIteration1) {
        millisecondsToFinishPolicyIteration.add(millisecondsToFinishPolicyIteration1);
    }

    public static void addMillisecondsToFinishQLearning(Number millisecondsToFinishQLearning1) {
        millisecondsToFinishQLearning.add(millisecondsToFinishQLearning1);
    }

    public static void addValueIterationReward(double reward) {
        rewardsForValueIteration.add(reward);
    }

    public static void addPolicyIterationReward(double reward) {
        rewardsForPolicyIteration.add(reward);
    }

    public static void addQLearningReward(double reward) {
        rewardsForQLearning.add(reward);
    }

    public static void printValueIterationTimeResults() {
        System.out.print("Value Iteration,");
        printList(millisecondsToFinishValueIteration);
    }

    public static void printPolicyIterationTimeResults() {
        System.out.print("Policy Iteration,");
        printList(millisecondsToFinishPolicyIteration);
    }

    public static void printQLearningTimeResults() {
        System.out.print("Q Learning,");
        printList(millisecondsToFinishQLearning);
    }

    public static void printValueIterationRewards() {
        System.out.print("Value Iteration Rewards,");
        printNumberList(rewardsForValueIteration);
    }

    public static void printPolicyIterationRewards() {
        System.out.print("Policy Iteration Rewards,");
        printNumberList(rewardsForPolicyIteration);
    }

    public static void printQLearningRewards() {
        System.out.print("Q Learning Rewards,");
        printNumberList(rewardsForQLearning);
    }

    public static void printNumIterations() {
        System.out.print("Iterations,");
        printList(numIterations);
    }

    private static void printList(List<Number> valueList) {
        int counter = 0;
        for (Number value : valueList) {
            System.out.print(String.valueOf(value));
            if (counter != valueList.size() - 1) {
                System.out.print(",");
            }
            counter++;
        }
        System.out.println();
    }

    private static void printNumberList(List<Number> valueList) {
        int counter = 0;
        for (Number value : valueList) {
            System.out.print(String.valueOf(value));
            if (counter != valueList.size() - 1) {
                System.out.print(",");
            }
            counter++;
        }
        System.out.println();
    }

    public static void printAggregateAnalysis() {
        System.out.println("//Aggregate Analysis//\n");
        System.out.println("The data below shows the number of steps/actions the agent required to reach \n"
                + "the terminal state given the number of iterations the algorithm was run.");
        printNumIterations();
        printValueIterationResults();
        printPolicyIterationResults();
        printQLearningResults();
        System.out.println();
        System.out.println("The data below shows the number of milliseconds the algorithm required to generate \n"
                + "the optimal policy given the number of iterations the algorithm was run.");
        printNumIterations();
        printValueIterationTimeResults();
        printPolicyIterationTimeResults();
        printQLearningTimeResults();

        System.out.println("\nThe data below shows the total reward gained for \n"
                + "the optimal policy given the number of iterations the algorithm was run.");
        printNumIterations();
        printValueIterationRewards();
        printPolicyIterationRewards();
        printQLearningRewards();
    }
}
