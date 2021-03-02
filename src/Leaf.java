public class Leaf extends Vertex{

    private Leaf(Key key, Value value){
        super();
        this.key = key;
        this.val = value;
        this.size = 1;
        this.minKey = key;
    }

    protected static Leaf createLeaf(Key key, Value value){
        Key newKey = key.createCopy();
        Value newVal = value.createCopy();
        return new Leaf(newKey, newVal);
    }
}