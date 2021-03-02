public class OurTest {

//    private static void printSearch(MyKey key, MyValue result);
//    {
//        System.out.print("Search for key: " + key.toString()+
//                " resulted in value: ");
//        System.out.println(result);
//    }
//
//    public static void main(String[] args) {
//
//        BalancedTree T = new BalancedTree();
//
//        MyKey[] myKeysArray = new MyKey[2];
//        MyValue[] myValueArray = new MyValue[2];
//
//        for (int i = 0; i < 2; i++) {
//            myKeysArray[i] = new MyKey("b", i);
//            myValueArray[i] = new MyValue(i);
//            T.insert(myKeysArray[i], myValueArray[i]);
//        }
//
////        for(int i = 1; i >= 0 ;i--)
////        {
////            T.delete(myKeysArray[i]);
////        }
//
//        for (int i = 1; i >= 0; i--) {
//            System.out.println("Actual Value: " + myValueArray[i].toString());
//            System.out.println("Value found: " + T.search(myKeysArray[i]));
//        }
//    }

    public static void main(String[] args) {
        BalancedTree T = new BalancedTree();

        MyKey[] myKeysArray = new MyKey[Constants.SMALL_TEST_SIZE];
        MyValue[] myValueArray = new MyValue[Constants.SMALL_TEST_SIZE];

        for (int i = 0; i < Constants.SMALL_TEST_SIZE; i++) {
            myKeysArray[i] = new MyKey("b", i);
            myValueArray[i] = new MyValue(i);
        }

        for (int i = 0; i < Constants.SMALL_TEST_SIZE; i++) {
            T.insert(myKeysArray[i], myValueArray[i]);
//            System.out.println("key:" + myKeysArray[i] + T.rank(myKeysArray[i]));
        }

        T.delete(myKeysArray[5]);

        System.out.println(T.sumValuesInInterval(myKeysArray[6], myKeysArray[6]));

//        for(int i = Constants.SMALL_TEST_SIZE - 1; i >= 0 ;i--)
//        {
//            System.out.println("Key at index " + i + " is " + T.select(i));
//        }
//        System.out.println("Key at index " + 20 + " is " + T.select(20));


//        for (int i = Constants.SMALL_TEST_SIZE - 1; i >= 0; i--) {
//            T.delete(myKeysArray[i]);
//        }

//        for (int i = Constants.SMALL_TEST_SIZE - 1; i >= 0; i--) {
//            System.out.println("Actual Value: " + myValueArray[i].toString());
//            System.out.println("Value found: " + T.search(myKeysArray[i]).toString());
//        }
    }
}




