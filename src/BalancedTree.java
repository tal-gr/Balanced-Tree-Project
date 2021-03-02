public class BalancedTree {
    private Vertex root;

    private void updateKey(Node x) {
        x.key = x.getLeft().key.createCopy();
        x.minKey = x.getLeft().minKey.createCopy();
        if (x.getMid() != null) {
            x.key = x.getMid().key.createCopy();
        }
        if (x.getRight() != null) {
            x.key = x.getRight().key.createCopy();
        }
    }

    private void UpdateSizeAndSum(Node x) {
        x.size = x.getLeft().size;
        x.val = x.getLeft().val.createCopy();
        if (x.getMid() != null) {
            x.size += x.getMid().size;
            x.val.addValue(x.getMid().val.createCopy());
        }
        if (x.getRight() != null) {
            x.size += x.getRight().size;
            x.val.addValue(x.getRight().val.createCopy());
        }
    }

    private void setChildren(Node x, Vertex l, Vertex m, Vertex r) {
        x.setLeft(l);
        x.setMid(m);
        x.setRight(r);
        l.p = x;
        if (m != null) {
            m.p = x;
        }
        if (r != null) {
            r.p = x;
        }
        updateKey(x);
        UpdateSizeAndSum(x);
    }

    private Node insertAndSplit(Node x, Vertex z) {
        Vertex left = x.getLeft();
        Vertex mid = x.getMid();
        Vertex right = x.getRight();
        if (right == null) {
            if (z.key.compareTo(left.key) < 0) {
                setChildren(x, z, left, mid);
            } else if (z.key.compareTo(mid.key) < 0) {
                setChildren(x, left, z, mid);
            } else {
                setChildren(x, left, mid, z);
            }
            return null;
        }
        Node y = Node.createNode();
        if (z.key.compareTo(left.key) < 0) {
            setChildren(x, z, left, null);
            setChildren(y, mid, right, null);
        } else if (z.key.compareTo(mid.key) < 0) {
            setChildren(x, left, z, null);
            setChildren(y, mid, right, null);
        } else if (z.key.compareTo(right.key) < 0) {
            setChildren(x, left, mid, null);
            setChildren(y, z, right, null);
        } else {
            setChildren(x, left, mid, null);
            setChildren(y, right, z, null);
        }
        return y;
    }

    public void insert(Key newKey, Value newValue) {
        Leaf z = Leaf.createLeaf(newKey.createCopy(), newValue.createCopy());
        if (root == null) {
            root = z;
            return;
        }
        if (this.root instanceof Leaf) {
            Leaf x = (Leaf) root;
            Node newRoot = Node.createNode();
            if (newKey.compareTo(root.key) < 0) {
                setChildren(newRoot, z, x, null);
            } else {
                setChildren(newRoot, x, z, null);
            }
            this.root = newRoot;
            return;
        }
        Vertex y1 = this.root;
        while (!(y1 instanceof Leaf)) {
            Node y = (Node) y1;
            if (z.key.compareTo(y.getLeft().key) < 0) {
                y1 = y.getLeft();
            } else if (z.key.compareTo(y.getMid().key) < 0) {
                y1 = y.getMid();
            } else {
                if (y.getRight() != null) {
                    y1 = y.getRight();
                } else {
                    y1 = y.getMid();
                }
            }
        }
        Node x = y1.p;
        Node tempNode = insertAndSplit(x, z);
        while (x != root) {
            x = x.p;
            if (tempNode != null) {
                tempNode = insertAndSplit(x, tempNode);
            } else {
                updateKey(x);
                UpdateSizeAndSum(x);
            }
        }
        if (tempNode != null) {
            Node w = Node.createNode();
            setChildren(w, x, tempNode, null);
            this.root = w;
        }
    }

    public Value search(Key key) {
        try{
            Leaf x = (Leaf) searchRec(root, key);
            return x.val.createCopy();
        }
        catch (NullPointerException E){
            return null;
        }
    }

    private Vertex searchRec(Vertex x, Key key) {
        if (x instanceof Leaf) {
            if (x.key.compareTo(key) == 0) {
                return x;
            }
            return null;
        }
        Node x1 = (Node) x;
        if (key.compareTo(x1.getLeft().key) <= 0) {
            return searchRec(x1.getLeft(), key);
        } else if (key.compareTo(x1.getMid().key) <= 0) {
            return searchRec(x1.getMid(), key);
        } else {
            if (x1.getRight() != null) {
                return searchRec(x1.getRight(), key);
            } else return null;
        }
    }

    private Node borrowOrMerge(Node y) {
        Node z = y.p;
        if (y == z.getLeft()) {
            Node x = (Node) z.getMid();
            if (x.getRight() != null) {
                setChildren(y, y.getLeft(), x.getLeft(), null);
                setChildren(x, x.getMid(), x.getRight(), null);
            } else {
                setChildren(x, y.getLeft(), x.getLeft(), x.getMid());
                setChildren(z, x, z.getRight(), null);
            }
            return z;
        }
        if (y == z.getMid()) {
            Node x = (Node) z.getLeft();
            if (x.getRight() != null) {
                setChildren(y, x.getRight(), y.getLeft(), null);
                setChildren(x, x.getLeft(), x.getMid(), null);
            } else {
                setChildren(x, x.getLeft(), x.getMid(), y.getLeft());
                setChildren(z, x, z.getRight(), null);
            }
            return z;
        }
        Node x = (Node) z.getMid();
        if (x.getRight() != null) {
            setChildren(y, x.getRight(), y.getLeft(), null);
            setChildren(x, x.getLeft(), x.getMid(), null);
        } else {
            setChildren(x, x.getLeft(), x.getMid(), y.getLeft());
            setChildren(z, z.getLeft(), x, null);
        }
        return z;
    }

    public void delete(Key key) {
        Leaf x = (Leaf) searchRec(root, key);
        if (x == null) return;
        Node y = x.p;
        if (y == null) {
            this.root = null;
            return;
        }
        if (x == y.getLeft()) {
            setChildren(y, y.getMid(), y.getRight(), null);
        } else if (x == y.getMid()) {
            setChildren(y, y.getLeft(), y.getRight(), null);
        } else setChildren(y, y.getLeft(), y.getMid(), null);
        while (y != null) {
            if (y.getMid() == null) {
                if (y != root) {
                    y = borrowOrMerge(y);
                } else {
                    this.root = (Vertex) y.getLeft();
                    y.getLeft().p = null;
                    return;
                }
            } else {
                updateKey(y);
                UpdateSizeAndSum(y);
                y = y.p;
            }
        }
    }

    public int rank(Key key) {
        int rank = 1;
        Vertex x;
        try{
             x = searchRec(this.root, key); // throws NullPointerException when root == null
        }
        catch (NullPointerException E){
            return 0;
        }
        if (x == null) {
            return 0;
        }
        Node y = x.p;
        while (y != null) {
            if (x == y.getMid()) {
                rank += y.getLeft().size;
            } else if (x == y.getRight()) {
                rank += (y.getLeft().size + y.getMid().size);
            }
            x = y;
            y = y.p;
        }
        return rank;
    }

    public Key select(int index) {
        if (this.root == null) {
            return null;
        }
        Vertex v = selectRec(root, index);
        if (v != null) {
            return v.key.createCopy();
        }
        return null;
    }

    private Vertex selectRec(Vertex x, int i) {
        if (i < 1 || x.size < i) {
            return null;
        }
        if (x instanceof Leaf) {
            return x;
        }
        Node x1 = (Node) x;
        int s_left = x1.getLeft().size;
        int s_left_mid = x1.getLeft().size + x1.getMid().size;
        if (i <= s_left) {
            return selectRec(x1.getLeft(), i);
        } else if (i <= s_left_mid) {
            return selectRec(x1.getMid(), i - s_left);
        } else {
            return selectRec(x1.getRight(), i - s_left_mid);
        }
    }

    private Boolean toAscend(int originalInterval, int currSum, Vertex x) {
        Node p = x.p;
        if (p.getRight() != null) {
            if (x == p.getLeft()) {
                return (currSum + (p.getMid().size) + (p.getRight().size) < originalInterval);
            }
            if (x == p.getMid()) {
                return (currSum + (p.getRight().size) < originalInterval);
            }
            return currSum < originalInterval;
        }
        if (x == p.getLeft()) {
            return (currSum + (p.getMid().size) < originalInterval);
        }
        return currSum < originalInterval;
    }

    private int sumValsAndSizesOfRightBros(Vertex x, Value sum, int currSize) {
        Node p = x.p;
        if (p.getRight() != null) {
            if (x == p.getLeft()) {
                sum.addValue(p.getMid().val.createCopy());
                sum.addValue(p.getRight().val.createCopy());
                currSize += p.getMid().size + p.getRight().size;
            }
            else if (x == p.getMid()) {
                sum.addValue(p.getRight().val.createCopy());
                currSize += p.getRight().size;
            }
        }
        else {
            if (x == p.getLeft()) {
                sum.addValue(p.getMid().val.createCopy());
                currSize += p.getMid().size;
            }
        }
        return currSize;
    }

    private Object[] findBroToDescend (Vertex x, int sts, Value sumVals){
        Object[] returnArray = new Object[2];
        Node p = x.p;
        if (p.getRight() != null) {
            if (x == p.getLeft()) {
                if (sts <= p.getMid().size) {
                    returnArray[0] = p.getMid();
                    returnArray[1] = sts;
                    return returnArray;
                }
                sumVals.addValue(p.getMid().val.createCopy());
                returnArray[0] = p.getRight();
                returnArray[1] = sts - p.getMid().size;
                return returnArray;
            }
            returnArray[0] = p.getRight();
            returnArray[1] = sts;
            return returnArray;
        }
        returnArray[0] = p.getMid();
        returnArray[1] = sts;
        return returnArray;
    }

    private Key minimum(){
        Vertex x = this.root;
        while (!(x instanceof Leaf)) {
            Node x1 = (Node) x;
            x = x1.getLeft();
        }
        return x.key.createCopy();
    }

    private Key maximum(){
        Vertex x = this.root;
        while (!(x instanceof Leaf)) {
            Node x1 = (Node) x;
            if (x1.getRight() != null) {
                x = x1.getRight();
            }
            else {
                x = x1.getMid();
            }
        }
        return x.key.createCopy();
    }

    private Key customSuccessor(Key key) {
        Vertex x = this.root;
        while (!(x instanceof Leaf)) {
            Node x1 = (Node) x;
            if (key.compareTo(x1.getLeft().key) < 0) {
                x = x1.getLeft();
            }
            else if (key.compareTo(x1.getMid().key) < 0) {
                x = x1.getMid();
            }
            else if (x1.getRight() != null) {
                if (key.compareTo(x1.getRight().key) < 0) {
                    x = x1.getRight();
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }
        return x.key.createCopy();
    }

    private Key customPredecessor(Key key) {
        Vertex x = this.root;
        while (!(x instanceof Leaf)) {
            Node x1 = (Node) x;
            if (x1.getRight() != null) {
                if (key.compareTo(x1.getRight().minKey) > 0) {
                    x = x1.getRight();
                    continue;
                }
            }
            if (key.compareTo(x1.getMid().minKey) > 0) {
                x = x1.getMid();
            }
            else if (key.compareTo(x1.getLeft().minKey) > 0) {
                x = x1.getLeft();
            }
            else {
                return null;
            }
        }
        return x.key.createCopy();
    }

    private Key[] findInterval(Key key1, Key key2) {
        Key newKey1 = key1.createCopy();
        Key newKey2 = key2.createCopy();
        Key minKey = minimum();
        Key maxKey = maximum();
        if (key1.compareTo(minKey) < 0 && key2.compareTo(maxKey) > 0){
            newKey2 = maxKey.createCopy();
            newKey1 = minKey.createCopy();
        }
        else if ((key1.compareTo(minKey) < 0 && key2.compareTo(maxKey) <= 0)){
            newKey1 = minKey.createCopy();
            if (search(key2) == null){
                newKey2 = customPredecessor(key2);
            }
        }
        else if ((key1.compareTo(minKey) >= 0 && key2.compareTo(maxKey) > 0)){
            newKey2 = maxKey.createCopy();
            if (search(key1) == null){
                newKey1 = customSuccessor(key1);
            }
        }
        else {
            if ((search(key1) == null) && (search(key2) == null)) {
                newKey1 = customSuccessor(key1);
                newKey2 = customPredecessor(key2);
            } else if ((search(key1) != null) && (search(key2) == null)) {
                newKey2 = customPredecessor(key2);
            } else if ((search(key1) == null) && (search(key2) != null)) {
                newKey1 = customSuccessor(key1);
            }
        }
        return new Key[]{newKey1, newKey2};
    }

    private boolean checkInterval (Key key1, Key key2){
        Key minKey = minimum();
        Key maxKey = maximum();
        if (key1.compareTo(key2) > 0) {
            return false;
        }
        if ((key1.compareTo(minKey) < 0) && (key2.compareTo(minKey) < 0)){
            return false;
        }
        if ((key1.compareTo(maxKey) > 0) && (key2.compareTo(maxKey) > 0)){
            return false;
        }
        return true;
    }

    private Value sumRightLeaves(int sts, Leaf x, Value sum) {
        Node p = x.p;
        Vertex[] children = {p.getLeft(), p.getMid(), p.getRight()};
        if (p.getRight() != null) {
            if (x == p.getLeft()) {
                for (int i = 1; i < 3; i++) {
                    if (sts > 0) {
                        sum.addValue(children[i].val.createCopy());
                        sts -= 1;
                    }
                }
            }
            else if (x == p.getMid()) {
                if (sts > 0) {
                    sum.addValue(children[2].val.createCopy());
                }
            }
            else{
                return sum;
            }
        }
        else{
            if (x == p.getLeft()) {
                if (sts > 0){
                    sum.addValue(children[1].val.createCopy());
                }
            }
            else {
                return sum;
            }
        }
        return sum;
    }

    public Value sumValuesInInterval(Key key1, Key key2) {
        if (root == null || !(checkInterval(key1,key2))){
            return null;
        }
        if (root.size == 1){
            return root.val.createCopy();
        }
        Key[] keysInterval = findInterval(key1, key2);
        if (keysInterval[0].compareTo(keysInterval[1]) > 0){
            return null;
        }
        int originalSize = (rank(keysInterval[1]) - rank(keysInterval[0]) + 1);
        Vertex x = searchRec(this.root, keysInterval[0]); //initial lower bound
        Value sumOfVals = x.val.createCopy();
        int currentSize = 1;
        int sizeToSum = originalSize - 1;
        while (toAscend(originalSize, currentSize, x)) {
            currentSize = sumValsAndSizesOfRightBros(x, sumOfVals, currentSize);
            sizeToSum = originalSize - currentSize;
            x = x.p;
        }
        if (x instanceof Leaf){
            return sumRightLeaves(sizeToSum, (Leaf) x, sumOfVals);
        }
        Object[] broToDescendArray = findBroToDescend(x, sizeToSum, sumOfVals);
        Node broToDescend = (Node) broToDescendArray[0];
        sizeToSum = (Integer) broToDescendArray[1];
        while (sizeToSum > 0) {
            Vertex[] children = {broToDescend.getLeft(), broToDescend.getMid(), broToDescend.getRight()};
            for (int i = 0; i < 3; i++) {
                if (children[i] != null) {
                    if (children[i].size > sizeToSum) {
                        broToDescend = (Node) children[i];
                        break;
                    }
                    if (children[i].size == sizeToSum) {
                        sumOfVals.addValue(children[i].val);
                        sizeToSum -= children[i].size;
                        break;
                    }
                    if (children[i].size < sizeToSum) {
                        sumOfVals.addValue(children[i].val);
                        sizeToSum -= children[i].size;
                    }
                }
            }
        }
        return sumOfVals;
    }

}