package com.cab404.codeplay;
/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 01:55 on 08-12-2014
 *
 * @author cab404
 */
public class LevelManager {
    int level = 0;
    int indent = 0;

    public LevelManager(int indent) {
        this.indent = indent;
    }

    public Expression getIndentExpression() {
        return new Expression() {
            @Override
            public CharSequence build(ProbabilityManager manager) {
                StringBuilder expression = new StringBuilder();
                for (int l = 0; l < level * indent; l++) expression.append(' ');
                return expression;
            }
        };
    }

    public Expression getOpenExpression() {
        return new Expression() {
            @Override
            public CharSequence build(ProbabilityManager manager) {
                level++;
                return "";
            }
        };
    }

    public Expression getCloseExpression() {
        return new Expression() {
            @Override
            public CharSequence build(ProbabilityManager manager) {
                if (level != 0)
                    level--;
                else throw new RuntimeException();
                return "";
            }
        };
    }
}
