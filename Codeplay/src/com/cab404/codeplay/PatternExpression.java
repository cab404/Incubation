package com.cab404.codeplay;
import java.util.ArrayList;
/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 11:55 on 07-12-2014
 *
 * @author cab404
 */
public class PatternExpression implements Expression {

    public static final String START = "${", END = "}";
    ArrayList<String> parts, names;

    public PatternExpression(String expression) {
        int last = 0;
        parts = new ArrayList<>();
        names = new ArrayList<>();

        for (int i = expression.indexOf(START, last); i != -1; i = expression.indexOf(START, last)) {
            int start = i + START.length();
            int end = expression.indexOf(END, start);
            parts.add(expression.substring(last, i));
            names.add(expression.substring(start, end));
            last = end + END.length();
        }
        parts.add(expression.substring(last, expression.length()));
    }

    public CharSequence build(ProbabilityManager manager) {
        StringBuilder result = new StringBuilder();

        ArrayList<CharSequence> built = new ArrayList<>();
        for (String type : names)
            built.add(manager.request(type).build(manager));


        for (int i = 0; i < built.size(); i++)
            result
                    .append(parts.get(i))
                    .append(built.get(i));

        return result.append(parts.get(parts.size() - 1));
    }

}
