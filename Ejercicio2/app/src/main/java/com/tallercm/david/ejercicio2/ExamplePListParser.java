package com.tallercm.david.ejercicio2;

import android.util.Log;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ExamplePListParser extends XMLPropertyListConfiguration {
    private static final String TAG = "ExamplePListParser";

    public ExamplePListParser(InputStream inputStream) {
        super(inputStream);
    }

    /**
     * Obtiene un elemento de la lista especificando su ID.
     * @param id
     * @return
     */
     public Object getProducto(int id) {
        return getConfigurationObject("PRODUCTO" + id);
     }


    /**
     * Debug method. Print all the "dict" key names from our plist configuration
     * file
     */
    public void dumpKeys() {
        printHashKeys("root", mPlistHashMap);
    }

    /**
     * Debug method. Iterate through all the methods of this class and print our
     * the resulting values.
     */
    public void dumpValues() {

        try {
            Class<? extends XMLPropertyListConfiguration> c = this.getClass();
            Method m[] = c.getDeclaredMethods();
            for (int i = 0; i < m.length; i++) {
                // only invoke getter methods
                if (m[i].getName().startsWith("get")) {
                    // Log.d(TAG, m[i].getName());
                    if (m[i].getReturnType() == Integer.class) {
                        Log.d(TAG, m[i].getName() + " --> " + (Integer) m[i].invoke(this, (Object[]) null));
                    } else if (m[i].getReturnType() == ArrayList.class) {
                        Log.d(TAG, m[i].getName() + " --> Array");
                        dumpArrayList((ArrayList<?>) m[i].invoke(this, (Object[]) null));
                    } else if (m[i].getReturnType() == Date.class) {
                        Log.d(TAG, m[i].getName() + " --> " + (Date) m[i].invoke(this, (Object[]) null));
                    } else if (m[i].getReturnType() == Boolean.class) {
                        Log.d(TAG, m[i].getName() + " --> " + (Boolean) m[i].invoke(this, (Object[]) null));
                    } else if (m[i].getReturnType() == String.class) {
                        Log.d(TAG, m[i].getName() + " --> " + (String) m[i].invoke(this, (Object[]) null));
                    } else {
                        Log.d(TAG, m[i].getName() + " --> UNSUPPORTED TYPE");
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void dumpArrayList(ArrayList<?> list) {
        for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
            Object o = iter.next();
            if (o instanceof String) {
                Log.d(TAG, "\t" + (String) o);
            } else if (o instanceof Integer) {
                Log.d(TAG, "\t" + (Integer) o);
            } else if (o instanceof HashMap) {
                Log.d(TAG, "\tHashMap");
                @SuppressWarnings("unchecked")
                HashMap<String, Object> hash = (HashMap<String, Object>) o;
                for (Iterator<String> hashIter = hash.keySet().iterator(); hashIter.hasNext();) {
                    String key = hashIter.next();
                    Object value = hash.get(key);
                    if (value instanceof Integer) {
                        Log.d(TAG, "\t\t " + key + " = " + (Integer) value);
                    } else if (value instanceof String) {
                        Log.d(TAG, "\t\t " + key + " = " + (String) value);
                    }
                }
            }
        }
    }

    /**
     * Debug method. Iterate through all the keys in the HashMap (dict) and
     * print the key names for each child HashMap (dict).
     */
    @SuppressWarnings("unchecked")
    private void printHashKeys(String key, HashMap<String, Object> map) {
        Set<String> keys = map.keySet();
        Log.d(TAG, key + " --> " + keys.toString());
        for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
            key = iter.next();
            Object o = map.get(key);
            if (o instanceof HashMap) {
                printHashKeys(key, (HashMap<String, Object>) o);
            }
        }
    }
}
