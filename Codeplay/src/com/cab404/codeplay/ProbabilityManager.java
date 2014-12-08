package com.cab404.codeplay;
import java.util.*;
/**
 * Gives you expressions.
 * <p/>
 * Created at 11:58 on 07-12-2014
 *
 * @author cab404
 */
public class ProbabilityManager {

    Map<String, List<Probability>> probabilities;

    public ProbabilityManager() {
        probabilities = new HashMap<>();
    }

    public void add(Expression ex, float probability, String type) {
        List<Probability> list = probabilities.get(type);
        if (list == null) {list = new ArrayList<>(); probabilities.put(type, list);}

        list.add(new Probability(probability, ex));

    }

    public Expression request(String type) {
        List<Probability> listed = probabilities.get(type);
        Collections.sort(listed);

        float sum = 0;
        for (Probability p : listed)
            sum += p.probability;

        /* Cause' of rounding errors */
        sum *= 0.999f;

        float random = (float) (Math.random() * sum);

        for (Probability p : listed) {
            random -= p.probability;
            if (random <= 0)
                return p.value;
        }
        throw new RuntimeException("SHOULD NOT HAVE HAPPENED");
    }


    /**
     * Probability of expression to happen.
     * <p/>
     * Created at 12:46 on 07-12-2014
     *
     * @author cab404
     */
    private static class Probability implements Comparable<Probability> {
        private final float probability;
        private final Expression value;

        public Probability(float probability, Expression value) {
            this.probability = probability;
            this.value = value;
        }

        @Override
        public int compareTo(Probability probability) {
            return (int) Math.signum(probability.probability - this.probability);
        }
    }
}
