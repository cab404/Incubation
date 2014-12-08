package com.cab404.codeplay;
import java.util.Arrays;
/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 11:55 on 07-12-2014
 *
 * @author cab404
 */
public class CodeGenerator {
    ProbabilityManager manager = new ProbabilityManager();

    public CodeGenerator() {
        manager.add(new PatternExpression("${bool} || ${bool}"), 0.1f, "bool");
        manager.add(new PatternExpression("${bool} == ${bool}"), 0.1f, "bool");
        manager.add(new PatternExpression("${bool} && ${bool}"), 0.1f, "bool");

        manager.add(new PatternExpression("${bool} ^ ${bool}"), 0.1f, "bool");
        manager.add(new PatternExpression("${bool} & ${bool}"), 0.1f, "bool");
        manager.add(new PatternExpression("${bool} | ${bool}"), 0.1f, "bool");

        manager.add(new PatternExpression("(${int} ^ ${int})"), 0.1f, "int");
        manager.add(new PatternExpression("(${int} & ${int})"), 0.1f, "int");
        manager.add(new PatternExpression("(${int} | ${int})"), 0.1f, "int");

        manager.add(new PatternExpression("(${fp} < ${fp})"), 0.1f, "bool");
        manager.add(new PatternExpression("(${fp} > ${fp})"), 0.1f, "bool");
        manager.add(new PatternExpression("(${fp} == ${fp})"), 0.1f, "bool");
        manager.add(new PatternExpression("(${fp} != ${fp})"), 0.1f, "bool");
        manager.add(new PatternExpression("(${fp} - ${fp})"), 0.1f, "fp");
        manager.add(new PatternExpression("(${fp} + ${fp})"), 0.1f, "fp");
        manager.add(new PatternExpression("(${fp} * ${fp})"), 0.1f, "fp");
        manager.add(new PatternExpression("(${fp} / ${fp})"), 0.1f, "fp");

        manager.add(new PatternExpression("${int}"), 0.5f, "fp");
        manager.add(new PatternExpression("\"${name}\""), 0.5f, "str");
        manager.add(new PatternExpression("${name}"), 0.2f, "str");
        manager.add(new PatternExpression("${name}"), 0.2f, "int");
        manager.add(new PatternExpression("${name}"), 0.2f, "fp");
        manager.add(new PatternExpression("${name}"), 0.2f, "bool");

        manager.add(new RandomExpressions.RandomDoubleExpression(-100, 100), 0.5f, "fp");
        manager.add(new RandomExpressions.RandomIntegerExpression(-100, 100), 0.5f, "int");
        manager.add(new RandomExpressions.RandomBoooleanExpression(), 0.5f, "bool");
        manager.add(new RandomExpressions.RandomParameters(0, 3, Arrays.asList("int", "fp", "bool", "str")), 0.5f, "params");

        manager.add(new PatternExpression("alpha"), 1f, "name");
        manager.add(new PatternExpression("beta"), 1f, "name");
        manager.add(new PatternExpression("foo"), 1f, "name");
        manager.add(new PatternExpression("bar"), 1f, "name");
        manager.add(new PatternExpression("pony"), 1f, "name");
        manager.add(new PatternExpression("gamma"), 1f, "name");
        manager.add(new PatternExpression("eradios"), 1f, "name");
        manager.add(new PatternExpression("deriatio"), 1f, "name");
        manager.add(new PatternExpression("something"), 1f, "name");

        LevelManager man = new LevelManager(3);
        manager.add(man.getIndentExpression(), 1, "Level");
        manager.add(man.getOpenExpression(), 1, "Open");
        manager.add(man.getCloseExpression(), 1, "Close");

        manager.add(new PatternExpression("${Level}${name} = ${name}(${params})"), 0.5f, "core");
        manager.add(new PatternExpression("${Level}assert ${bool}"), 0.5f, "core");
        manager.add(new PatternExpression("${Level}{${Open}"), 0.1f, "core");
        manager.add(new PatternExpression("${Close}${Level}}"), 0.1f, "core");
        manager.add(new PatternExpression("\n"), 0.1f, "core");

    }
    public CharSequence getLine() {
        try {
            return manager.request("core").build(manager);
        } catch (Exception e) {
            return "";
        }
    }


}
