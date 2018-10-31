package com.tallercm.david.ejercicio2;

import android.annotation.SuppressLint;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

//import android.util.Log;

/**
 * This class will parse a plist xml file and store the contents in a
 * hierarchical HashMap of <String, Object> tuples, where the Object value could
 * be another HashMap, an ArrayList, Boolean, Date, String or Integer value.
 * 
 * Use the getConfiguration methods to retrieve the values from the parsed plist
 * file.
 * 
 * The key names for nested dictionary references must be of the form :
 * Dict1KeyName.Dict2KeyName.ElementKeyName
 * 
 * @author akoscz
 * 
 */

public class XMLPropertyListConfiguration {

    // private static final String TAG = "plist";

    /**
     * The nested (hierarchical) HashMap which holds our key-value pairs of our
     * plist file.
     */
    protected HashMap<String, Object> mPlistHashMap;

    /**
     * Constructor. Parse a plist file from the given InputStream.
     * 
     * @param inputStream
     *            The InputStream which has the bytes of the plist file we need
     *            to parse.
     */
    public XMLPropertyListConfiguration(InputStream inputStream) {
        mPlistHashMap = new HashMap<String, Object>();
        if (inputStream != null) {
            parse(inputStream);
        }
    }

    /**
     * Get an String configuration value for the given key.
     * 
     * @param keyName
     *            The name of the key to look up in the configuration
     *            dictionary.
     * @return The String value of the specified key.
     */
    public String getConfiguration(String keyName) {
        return (String) getConfigurationObject(keyName);
    }

    /**
     * Get a String configuration value for the given key. If there is no value
     * for the given key, then return the default value.
     * 
     * @param keyName
     *            The name of the key to look up in the configuration
     *            dictionary.
     * @param defaultValue
     *            The default value to return if they key has no associated
     *            value.
     * @return The String value of the specified key, or defaultValue if the
     *         value for keyName is null.
     */
    public String getConfigurationWithDefault(String keyName, String defaultValue) {
        String value = getConfiguration(keyName);
        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    /**
     * Get an Integer configuration value for the given key.
     * 
     * @param keyName
     *            The name of the key to look up in the configuration
     *            dictionary.
     * @return The Integer value of the specified key.
     */
    public Integer getConfigurationInteger(String keyName) {
        return (Integer) getConfigurationObject(keyName);
    }

    /**
     * Get an Integer configuration value for the given key. If there is no
     * value for the given key, then return the default value.
     * 
     * @param keyName
     *            The name of the key to look up in the configuration
     *            dictionary.
     * @param defaultValue
     *            The default value to return if they key has no associated
     *            value.
     * @return The Integer value of the specified key, or defaultValue if the
     *         value for keyName is null.
     */
    public Integer getConfigurationIntegerWithDefault(String keyName, Integer defaultValue) {
        Integer value = getConfigurationInteger(keyName);
        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    /**
     * Get a Date configuration value for the given key.
     * 
     * @param keyName
     *            The name of the key to look up in the configuration
     *            dictionary.
     * @return The Date value of the specified key.
     */
    public Date getConfigurationDate(String keyName) {
        return (Date) getConfigurationObject(keyName);
    }

    /**
     * Get a Date configuration value for the given key. If there is no value
     * for the given key, then return the default value.
     * 
     * @param keyName
     *            The name of the key to look up in the configuration
     *            dictionary.
     * @param defaultValue
     *            The default value to return if they key has no associated
     *            value.
     * @return The Date value of the specified key, or defaultValue if the value
     *         for keyName is null.
     */
    public Date getConfigurationDateWithDefault(String keyName, Date defaultValue) {
        Date value = getConfigurationDate(keyName);
        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    /**
     * Get a Boolean configuration value for the given key.
     * 
     * @param keyName
     *            The name of the key to look up in the configuration
     *            dictionary.
     * @return The Boolean value of the specified key.
     */
    public Boolean getConfigurationBoolean(String keyName) {
        return (Boolean) getConfigurationObject(keyName);
    }

    /**
     * Get a Boolean configuration value for the given key. If there is no
     * value for the given key, then return the default value.
     * 
     * @param keyName
     *            The name of the key to look up in the configuration
     *            dictionary.
     * @param defaultValue
     *            The default value to return if they key has no associated
     *            value.
     * @return The Boolean value of the specified key, or defaultValue if the
     *         value for keyName is null.
     */
    public Boolean getConfigurationBooleanWithDefault(String keyName,
            Boolean defaultValue) {
        Boolean value = getConfigurationBoolean(keyName);
        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    /**
     * Utility method which uses a XmlPullParser to iterate through the XML
     * elements and build up a hierarchical HashMap representing the key-value
     * pairs of the plist configuration file.
     * 
     * @param inputStream
     *            The InputStream which contains the plist XML file.
     */
    public void parse(InputStream inputStream) {
        mPlistHashMap.clear();
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            int arrayDepth = 0;

            boolean done = false;
            boolean parsingArray = false;

            String name = null;
            String key = null;

            Stack<HashMap<String, Object>> stack = new Stack<HashMap<String, Object>>();
            HashMap<String, Object> dict = null;
            ArrayList<Object> array = null;

            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    // Log.d(TAG, "START_DOCUMENT");
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();

                    if (name.equalsIgnoreCase("dict")) {
                        // root dict element
                        if (key == null) {
                            mPlistHashMap.clear();
                            dict = mPlistHashMap;
                        } else if (parsingArray) {
                            // Log.d(TAG, "START_TAG dict : inside array");
                            HashMap<String, Object> childDict = new HashMap<String, Object>();
                            array.add(childDict);
                            stack.push(dict);
                            dict = childDict;
                        } else {
                            // Log.d(TAG, "START_TAG dict : " + key);
                            HashMap<String, Object> childDict = new HashMap<String, Object>();
                            dict.put(key, childDict);
                            stack.push(dict);
                            dict = childDict;
                        }
                    } else if (name.equalsIgnoreCase("key")) {
                        key = parser.nextText();
                    } else if (name.equalsIgnoreCase("integer")) {
                        dict.put(key, Integer.valueOf(parser.nextText()));
                    } else if (name.equalsIgnoreCase("string")) {
                        if (parsingArray && (parser.getDepth() == (arrayDepth + 1))) {
                            array.add(parser.nextText());
                        } else {
                            dict.put(key, parser.nextText());
                        }
                    } else if (name.equalsIgnoreCase("array")) {
                        parsingArray = true;
                        array = new ArrayList<Object>();
                        dict.put(key, array);
                        arrayDepth = parser.getDepth();
                    } else if (name.equalsIgnoreCase("date")) {
                        dict.put(key, parseDate(parser.nextText()));
                    } else if (name.equalsIgnoreCase("true")) {
                        dict.put(key, Boolean.TRUE);
                    } else if (name.equalsIgnoreCase("false")) {
                        dict.put(key, Boolean.FALSE);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();

                    if (name.equalsIgnoreCase("dict")) {
                        // Log.d(TAG, "END_TAG dict");
                        if (!stack.empty()) {
                            dict = stack.pop();
                        }
                    } else if (name.equalsIgnoreCase("array")) {
                        parsingArray = false;
                        array = null;
                    } else if (name.equalsIgnoreCase("plist")) {
                        done = true;
                    }

                    break;
                case XmlPullParser.END_DOCUMENT:
                    // Log.d(TAG, "END_DOCUMENT");
                    break;

                }
                eventType = parser.next();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to parse an ISO8601 string to a Date object.
     * http://www.java2s.com/Code/Java/Data-Type/ISO8601dateparsingutility.htm
     * 
     * @param input
     *            The ISO8601 date string
     * @return The Date object representing the ISO8601 date string.
     * @throws java.text.ParseException
     */
    @SuppressLint("SimpleDateFormat")
    public static Date parseDate(String input) throws java.text.ParseException {

        // NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        // things a bit. Before we go on we have to repair this.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

        // this is zero time so we need to add that TZ indicator for
        if (input.endsWith("Z")) {
            input = input.substring(0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;

            String s0 = input.substring(0, input.length() - inset);
            String s1 = input.substring(input.length() - inset, input.length());

            input = s0 + "GMT" + s1;
        }

        return df.parse(input);
    }

    /**
     * Utility method which tokenizes the given keyName using the "." delimiter
     * and then looks up each token in the configuration dictionary. If the
     * token key points to a dictionary then it proceeds to the next token key
     * and looks up value of the token key in the dictionary it found from the
     * previous token key.
     * 
     * @param keyName
     *            The fully qualified key name.
     * @return The Object value associated with the given key, or null if the
     *         key does not exist.
     */
    @SuppressWarnings("unchecked")
    protected Object getConfigurationObject(String keyName) {
        String[] tokens = keyName.split("\\.");
    	
        if (tokens.length > 1) {
            HashMap<String, Object> dict = mPlistHashMap;
            Object obj;
            for (int i = 0; i < tokens.length; i++) {
                obj = dict.get(tokens[i]);
                if (obj instanceof HashMap<?, ?>) {
                    dict = (HashMap<String, Object>) obj;
                    continue;
                }
                return obj;
            }
        }

        return mPlistHashMap.get(keyName);
    }
}
