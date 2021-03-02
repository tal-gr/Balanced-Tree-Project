public class Node extends Vertex{
    private Vertex left;
    private Vertex mid;
    private Vertex right;

    private Node() {
        super();
    }

    protected static Node createNode(){
        return new Node();
    }

    protected Vertex getLeft() {
        return left;
    }

    protected void setLeft(Vertex left) {
        this.left = left;
    }

    protected Vertex getMid() {
        return mid;
    }

    protected void setMid(Vertex mid) {
        this.mid = mid;
    }

    protected Vertex getRight() {
        return right;
    }

    protected void setRight(Vertex right) {
        this.right = right;
    }
}
