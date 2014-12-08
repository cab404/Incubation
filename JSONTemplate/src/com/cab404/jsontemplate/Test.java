package com.cab404.jsontemplate;
/**
 * Well, sorry for no comments here!
 * Still you can send me your question to me@cab404.ru!
 * <p/>
 * Created at 04:14 on 07-12-2014
 *
 * @author cab404
 */
public class Test {
    public static void main(String[] args) {
        JSONTemplate template = new JSONTemplate("{'auth':'%%%','data':['%%%',{'section1':'%%%','a':'%%%'},{'section2':'%%%'}]}");
        System.out.println(template.build("test", 1, 2, 3, 4));
    }
}
