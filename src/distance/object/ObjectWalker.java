package distance.object;

//import org.jdom.Comment;
//import org.jdom.Element;
//import wox.serial.EncodeBase64;
//import wox.serial.ObjectWriter;
//import wox.serial.Util;

import wox.serial.Util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import static wox.serial.Serial.primitiveArrays;


// import crjaim.EncodeBase64;

/**
 *  A simple but useful Object to XML serialiser.
 *  By Simon M. Lucas, August 2004
 *  Base 64 modifications by Carlos R. Jaimez Gonzalez
 */

public class ObjectWalker {

    HashMap<Object,Integer> obRef;
    HashMap<Object,Integer> typeCount;

    int count;
    boolean writePrimitiveTypes = true;
    boolean doStatic = true;

    // not much point writing out final values - at least yet -
    // the reader is not able to set them (though there's probably
    // a hidden way of doing this
    boolean doFinal = false;


    public ObjectWalker() {
        //System.out.println("inside SimpleWriter Constructor...");
        obRef = new HashMap<>();
        typeCount = new HashMap<>();
        count = 0;
    }

    public ObjectWalker walk(Object ob) {
        if (ob == null) {
            // a null object is represented by an empty Object tag with no attributes
            return this;
        }

        if (obRef.get(ob) != null) {
            // already seen it do do nothing
//            el = new Element(OBJECT);
//            el.setAttribute(IDREF, obRef.get(ob).toString());
            return this;
        }
        // a previously unseen object...
        obRef.put(ob, count++);

        if (Util.stringable(ob)) {
            System.out.println("Stringable: " + stringify(ob));
//            el = new Element(OBJECT);
//            el.setAttribute(TYPE, ob.getClass().getName());
//            el.setText(stringify(ob));
        } else if (ob.getClass().isArray()) {
            // el = writeArray(ob);
        } else {
            walkFields(ob);
        }
        // el.setAttribute(ID, obRef.get(ob).toString());
        return this;
    }

    public ObjectWalker writeArray(Object ob) {
        if (isPrimitiveArray(ob.getClass())) {
            writePrimitiveArray(ob);
            return this;
        } else {
            writeObjectArray(ob);
            return this;
        }
    }

    public ObjectWalker writeObjectArray(Object ob) {
        // Element el = new Element(ARRAY);
        // el.setAttribute
        // int[].class.
        // Array.

//        el.setAttribute(TYPE, ob.getClass().getComponentType().getName());
//        int len = Array.getLength(ob);
//        el.setAttribute(LENGTH, "" + len);
//        for (int i = 0; i < len; i++) {
//            el.addContent(write(Array.get(ob, i)));
//        }
        return this;
    }

    public ObjectWalker writePrimitiveArray(Object ob) {
//        Element el = new Element(ARRAY);
//        el.setAttribute(TYPE, ob.getClass().getComponentType().getName());
//        int len = Array.getLength(ob);
        //CJ this should not be here beacsue the lenght for the byte[] can be different
        //el.setAttribute(LENGTH, "" + len);

//        if (ob instanceof byte[]) {
//            el.setText(byteArrayString((byte[]) ob, el));
//        } else {
//            el.setAttribute(LENGTH, "" + len);
//            el.setText(arrayString(ob, len));
//        }
        return this;
    }

    public String arrayString(Object ob, int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(Array.get(ob, i).toString());
        }
        return sb.toString();
    }

    public ObjectWalker count(HashMap<Object, Integer> map, Object ob) {
        Integer x = map.get(ob);
        if (x == null) x = 0;
        map.put(ob, x+1);
        return this;
    }

    private ObjectWalker countType(Object ob) {
        count(typeCount, ob);
        return this;
    }


    public void walkFields(Object o) {
        // get the class of the object
        // get its fields
        // then get the value of each one
        // and call write to put the value in the Element

        Class cl = o.getClass();
        countType(cl);

        ArrayList<Field> fields = getFields(cl);
        String name = null;
        for (Field field : fields) {
            if ((doStatic || !Modifier.isStatic(field.getModifiers())) &&
                    (doFinal || !Modifier.isFinal(field.getModifiers())))
                try {
                    field.setAccessible(true);
                    name = field.getName();
                    // need to handle shadowed fields in some way...
                    // one way is to add info about the declaring class
                    // but this will bloat the XML file if we di it for
                    // every field - might be better to just do it for
                    // the shadowed fields
                    // name += "." + fields[i].getDeclaringClass().getName();
                    // fields[i].
                    Object value = field.get(o);
                    walk(value);
                    if (shadowed(fields, name)) {
//                        field.setAttribute(DECLARED, fields[i].getDeclaringClass().getName());
                    }
                    if (field.getType().isPrimitive()) {
                        // this is not always necessary - so it's optional
                        if (writePrimitiveTypes) {
                            // field.setAttribute(TYPE, fields[i].getType().getName());
                        }
                        // field.setAttribute(VALUE, value.toString());

                    } else {
                        // field.addContent(write(value));
                    }
                    // parent.addContent(field);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e);
                    // at least comment on what went wrong
                    // parent.addContent(new Comment(e.toString()));
                }
        }
    }

    private boolean shadowed(ArrayList<Field> fields, String fieldName) {
        // count the number of fields with the name fieldName
        // return true if greater than 1
        int count = 0;
        for (Field field : fields) {
            if (fieldName.equals(field.getName())) {
                count++;
            }
        }
        return count > 1;
    }

    public static String stringify(Object ob) {
        if (ob instanceof Class) {
            return ((Class) ob).getName();
        } else {
            return ob.toString();
        }
    }

    public static ArrayList<Field> getFields(Class c) {
        ArrayList<Field> fieldList = new ArrayList<>();
        while (!(c == null)) { // c.equals( Object.class ) ) {
            for (Field field : c.getDeclaredFields()) {
                fieldList.add(field);
            }
            c = c.getSuperclass();
        }
        return fieldList;
    }

    public static ArrayList<Object> getValues(Object o, ArrayList<Field> fields) {
        ArrayList<Object> values = new ArrayList<>();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object fo = field.get(o);
                values.add(fo);
                System.out.println(field.getName() + "\t " + fo );
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return values;
    }


    public boolean isPrimitiveArray(Class c) {
        for (int i = 0; i < primitiveArrays.length; i++) {
            if (c.equals(primitiveArrays[i])) {
                return true;
            }
        }
        return false;
    }


    public ObjectWalker printSummary() {
        System.out.println(obRef);
        System.out.println(typeCount);
        return this;
    }
}
