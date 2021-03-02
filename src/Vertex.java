 public abstract class Vertex {
    protected Node p;
    protected Key key;
    protected int size;
    protected Value val;
    protected Key minKey;

    protected Vertex(){
        this.size = 0;
    }
}
