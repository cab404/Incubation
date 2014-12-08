package com.cab404.codeplay;
import java.util.List;
/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 18:17 on 07-12-2014
 *
 * @author cab404
 */
public class RandomExpressions {

    public static class RandomIntegerExpression implements Expression {
        private final int min;
        private final int max;
        public RandomIntegerExpression(int min, int max) {
            this.min = min;
            this.max = max;
        }
        @Override
        public CharSequence build(ProbabilityManager manager) {
            return String.valueOf((int) (Math.random() * (max - min) + min));
        }
    }


    public static class RandomDoubleExpression implements Expression {
        private final double min;
        private final double max;
        public RandomDoubleExpression(double min, double max) {
            this.min = min;
            this.max = max;
        }
        @Override
        public CharSequence build(ProbabilityManager manager) {
            return String.format("%.4f", Math.random() * (max - min) + min).replace(',', '.');
        }
    }


    public static class RandomBoooleanExpression implements Expression {
        @Override
        public CharSequence build(ProbabilityManager manager) {
            return String.valueOf(Math.random() > 0.5);
        }
    }


    public static class RandomParameters implements Expression {
        private final int min;
        private final int max;
        private final List<String> types;

        public RandomParameters(int min, int max, List<String> types) {
            this.min = min;
            this.max = max;
            this.types = types;
        }

        @Override
        public CharSequence build(ProbabilityManager manager) {
            StringBuilder builder = new StringBuilder();

            int param_count = (int) (min + Math.random() * (max - min));

            for (int i = 0; i < param_count; i++)
                builder
                        .append(manager.request(types.get((int) (Math.random() * types.size()))).build(manager))
                        .append(i + 1 == param_count ? "" : ", ");


            return builder;
        }
    }


}
