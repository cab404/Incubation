package com.cab404.jsontemplate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Pretty simple but powerful and useful JSON template thingy for org.json library.
 * Usage is simple:
 * <p/>
 * <pre>
 *
 * JSONTemplate template = new JSONTemplate(
 * "{
 *      'auth': '%%%',
 *      'data': [
 *          {'section1': '%%%'},
 *          {'section2': '%%%'}
 *      ]
 * }");
 * System.out.println(template.build("test", 12, 23));
 *
 * > {"data":[{"section1":23},{"section2":12}],"auth":"test"}
 * </pre>
 * <p/>
 * Yeah, just replace changeable parts with %%%, or whatever you want (just change {@link JSONTemplate#REPLACE} in your copy).
 * So, that's it.
 * <p/>
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2014 cab404 (cab404.ru)
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy<br/>
 * of this software and associated documentation files (the "Software"), to deal<br/>
 * in the Software without restriction, including without limitation the rights<br/>
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell<br/>
 * copies of the Software, and to permit persons to whom the Software is<br/>
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in<br/>
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR<br/>
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,<br/>
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE<br/>
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER<br/>
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,<br/>
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN<br/>
 * THE SOFTWARE.
 * <p/>
 * Created at 3:44 on 07-12-2014
 *
 * @author cab404
 */
public class JSONTemplate {

    /**
     * Node of a target address.
     */
    private interface JSONAddressNode {
        /**
         * Retrieves an object this node pointing at.
         *
         * @param jsonObject from what to retrieve an object.
         */
        public Object move(Object jsonObject);
        /**
         * Sets value of something this node pointing at
         *
         * @param target where to set a value.
         * @param value  what to set
         */
        public void set(Object target, Object value);
    }

    /**
     * JSONArray index address node
     */
    private static class JSONArrayAddressNode implements JSONAddressNode {
        private final int index;

        public JSONArrayAddressNode(int index) {
            this.index = index;
        }

        @Override
        public Object move(Object jsonObject) {
            try {
                return ((JSONArray) jsonObject).get(index);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void set(Object target, Object value) {
            try {
                ((JSONArray) target).put(index, value);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return "Array:" + index;
        }

    }

    /**
     * JSONObject key address node
     */
    private static class JSONObjectAddressNode implements JSONAddressNode {
        private final String key;

        public JSONObjectAddressNode(String key) {
            this.key = key;
        }

        @Override
        public Object move(Object jsonObject) {
            try {
                return ((JSONObject) jsonObject).get(key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void set(Object target, Object value) {
            try {
                ((JSONObject) target).put(key, value);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return "Object:" + key;
        }
    }


    /**
     * What do we treat as a candidate for replacement
     */
    public final static String REPLACE = "%%%";

    private final String template;
    /**
     * Addresses to replace indexes
     */
    private final List<List<JSONAddressNode>> targets;

    /**
     * Creates JSONObject template from a string.
     *
     * @see com.cab404.jsontemplate.JSONTemplate
     */
    public JSONTemplate(String template) {
        try {
            this.template = template;
            this.targets = new ArrayList<>();

            recurseThroughObject(new ArrayList<JSONAddressNode>(), new JSONObject(template));

        } catch (JSONException e) {
            throw new RuntimeException("Template is not valid json string.", e);
        }


    }

    private void recurseThroughArray(List<JSONAddressNode> cur_path, JSONArray object) throws JSONException {
        for (int key = 0; key < object.length(); key++) {
            Object o = object.get(key);

            if (o instanceof String)
                if (REPLACE.equals(o)) {
                    List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                    copied_path.add(new JSONArrayAddressNode(key));
                    targets.add(copied_path);
                } else
                    continue;

            if (o instanceof JSONArray) {
                List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                copied_path.add(new JSONArrayAddressNode(key));
                recurseThroughArray(copied_path, (JSONArray) o);
            }

            if (o instanceof JSONObject) {
                List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                copied_path.add(new JSONArrayAddressNode(key));
                recurseThroughObject(copied_path, (JSONObject) o);
            }

        }
    }

    private void recurseThroughObject(List<JSONAddressNode> cur_path, JSONObject object) throws JSONException {
        JSONArray names = object.names();
        ArrayList<String> names_array = new ArrayList<>();

        for (int $index = 0; $index < names.length(); $index++) {
            names_array.add((String) names.get($index));
        }

        /*
         * Due to reverse addition into maps (JSONObjects).
         * Yes, I know, that is pretty crappy, but org.json implementation is frozen.
         * So should be no problems.
         */
        Collections.reverse(names_array);

        for (String key : names_array) {
            Object o = object.get(key);

            if (o instanceof String)
                if (REPLACE.equals(o)) {
                    List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                    copied_path.add(new JSONObjectAddressNode(key));
                    targets.add(copied_path);
                }

            if (o instanceof JSONArray) {
                List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                copied_path.add(new JSONObjectAddressNode(key));
                recurseThroughArray(copied_path, (JSONArray) o);
            }

            if (o instanceof JSONObject) {
                List<JSONAddressNode> copied_path = new ArrayList<>(cur_path);
                copied_path.add(new JSONObjectAddressNode(key));
                recurseThroughObject(copied_path, (JSONObject) o);
            }

        }
    }

    /**
     * Builds JSONObject from template and parameters.
     *
     * @throws java.lang.RuntimeException if numbers of method and template parameters are not matching.
     * @see com.cab404.jsontemplate.JSONTemplate
     */
    public JSONObject build(Object... toApply) {

        /* Checking if length of parameter array is matching such in our template */
        if (toApply.length != targets.size())
            throw new RuntimeException(
                    "You must supply exactly " +
                            targets.size() + " object" + (targets.size() == 1 ? "" : "s")
                            + " to this template!"
            );

        JSONObject result;
        try {
            result = new JSONObject(template);
        } catch (JSONException e) {
            /* Really, that's pretty much impossible, only if someone changed 'template' through memory manipulation */
            throw new RuntimeException("THAT SHOULD NOT HAVE HAPPENED", e);
        }

        for (int $target = 0; $target < toApply.length; $target++) {
            List<JSONAddressNode> address = targets.get($target);

            Object current_leaf = result;

            /* Going down to out destination */
            for (int $deep = 0; $deep < address.size() - 1; $deep++)
                current_leaf = address.get($deep).move(current_leaf);

            /* Getting last piece of address and setting value it's pointing to given parameter*/
            address.get(address.size() - 1).set(current_leaf, toApply[$target]);

        }

        return result;
    }


}
