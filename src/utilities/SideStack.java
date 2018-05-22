package utilities;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 *  SideStack: so called because in addition to pushing and popping, it is
 *  also possible to remove any item from anywhere in the stack.
 *  The top element is then popped and put in to that position.
 *  This gives constant time removal, but is not order preserving.
 * @param <T>
 */

public class SideStack<T> implements Iterable<T> {

    public static void main(String[] args) {
        int n = 1000;
        SideStack<Integer> stack = new SideStack(n);
        int nn = 10;
        for (int i=0; i<nn; i++) {
            stack.push(i);
        }



        // using a for(each) loop
        for (Integer x : stack) {
            System.out.println(x);
        }

        // now iterating and sliding out the even numbers

        Iterator<Integer> iterator = stack.iterator();

        while (iterator.hasNext()) {
            Integer x = iterator.next();
            System.out.println("x = " + x);
            if (x % 1 == 0) {
                System.out.println("Removing: " + x);
                iterator.remove();
            }
            System.out.println(stack.stackString());
            System.out.println();
        }



//        for (int i=0; i<nn+1; i++) {
//            System.out.println( i + "\t " + stack.slideOut(i) );
//            System.out.println(stack.stackString());
//        }
    }



    public static int initSize = 100;

    private int n; // stack size
    private int top; // index of the top item
    private Object[] stack; // the data

    public SideStack() {
        this(initSize);
    }

    public SideStack(int n) {
        this.n = n;
        top = -1;
        stack = new Object[n];
    }

    public void push(T x) {
        try {
            stack[top+1] = x;
            top++;
        } catch(Exception e) {
            throw new RuntimeException("Stack Overflow Error: " + top);
        }
    }

    public T slideOut(int ix) {
        // have to ensure that the 'i' is pulled out at this position,
        // and then the one from the end is pulled in

        if (ix > top) {
            throw new RuntimeException("Over the top: " + ix + " > " + top);
        }
        T x = (T) stack[ix];
        stack[ix] = pop();
        return x;
    }

    public T pop() {
        try {
            T x = (T) stack[top];
            top--;
            return x;
        } catch(Exception e) {
            throw new RuntimeException("IntStack pop error: i = " + top);
        }
    }

    public String toString() {
        return n + " : " + top;
    }

    public final boolean isEmpty() {
        return top < 0;
    }

    public final int size() {
        return top + 1;
    }

    public final void reset() {
        top = -1;
    }

    public String stackString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i=0; i<=top; i++) {
            sb.append(stack[i]);
            if (i<top) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    public T peek(int i) {
        return (T) stack[i];
    }


    @Override
    public Iterator<T> iterator() {
        return new SideStackIterator<>();
    }


    @SuppressWarnings("hiding")
    private class SideStackIterator<T> implements Iterator<T> {
        // int size =
        int ix = 0;

        public boolean hasNext() {
            return (ix <= top);
        }

        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            @SuppressWarnings("unchecked")
            T val = (T) peek(ix);
            ix++;
            return val;
        }

        public void remove() {
            // removes the current item
            ix--;
            slideOut(ix);
        }

    }


}
