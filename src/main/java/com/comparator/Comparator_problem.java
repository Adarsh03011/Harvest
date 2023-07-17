package com.comparator;

import java.util.Comparator;
class Comparator_new<T> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        if (o1.equals(o2)) {
            return 0;
        } else if (o1.hashCode() > o2.hashCode()) {
            return 1;
        } else {
            return -1;
        }
    }
}
    public  class Comparator_problem{

    public static void main(String[] args) {
        MyList<Integer> list2 = new MyList<>();
        list2.add(1);
        list2.add(2);
        list2.add(12);
        list2.add(3);
        list2.add(41);
        list2.add(25);
        list2.add(63);
        list2.add(7);


        System.out.println("Printing list : " + "\n" + list2);
        System.out.println("\n Size of list : "+ list2.size());
        list2.sort(new Comparator_new());
        System.out.println("\n Sorted list"+ "\n" + list2);

        System.out.println("\nElement position : " + list2.binarySearch(new Comparator_new(), 25));
    }
}
