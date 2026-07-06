import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class CircularWeakRefList {
    
    private ReferenceQueue<Object> referenceQueue;
    
    private class Node extends WeakReference<Object> {
        private Node previous;
        private Node next;

        public Node(Object referent, Node previous, Node next) {
            super(referent, referenceQueue);
            
        }
        


        
    }
    
    public CircularWeakRefList() {
        this.first = null;
        this.referenceQueue = new ReferenceQueue<>();
    }
}