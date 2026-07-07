package org.gendut.memory;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class CircularWeakRefList {
    private volatile Node first;
    private ReferenceQueue<Object> referenceQueue;

    public boolean firstEquals(Object x) {
    
        Object firstObject = first.get();

        while (firstObject == null) {
            cleanUp();
            Node currentFirst = first;
            // if we are in the list of truely equal elements, it should be impossible to get a null value here.
            if (currentFirst == null) {
                return false;
            }
            firstObject = currentFirst.get();
        }

        if (ThreadLocalRandom.current().nextInt(16) == 0) {
            cleanUp();
        }

        //TODO: now compare x with firstObject
        return false;
    }
    
    private class Node extends WeakReference<Object> {
        private Node previous;
        private Node next;

        public Node(Object referent, Node previous, Node next) {
            super(referent, referenceQueue);
            this.previous = previous;
            this.next = next;
        }
    }

    private final ReentrantLock cleanupLock = new ReentrantLock();
    private final Object writeLock = new Object();

    private void cleanUp() {
        if (cleanupLock.tryLock()) {
            try {
                Object x;
                while ((x = referenceQueue.poll()) != null) {
                    synchronized(writeLock) {
                        // Safely remove the individual dead node from the table
                        //TODO: internalRemoveDeadNode(x); 
                        //TODO
                    }
                }
            } finally {
                cleanupLock.unlock();
            }
        }
    }
    
    private  void add(Object x) {
        synchronized(writeLock) {
            //TODO: add, but also make sure that the reference is not already there 
            // (managed nodes should avoid this because thy keep a refernence once 
            // they are in their circular list, but two threads ma rnter here in the exactly same moment)
        }
    }
}