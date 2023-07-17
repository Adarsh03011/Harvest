package com.comparator;

import java.util.Comparator;
class Comparator_new<T> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        if(o1.equals(o2)){
            return 0;
        }
        else if(o1.hashCode() > o2.hashCode()){
            return 1;
        }
        else {
            return -1;
        }
    }

    public static class Comparator_problem{

    public static void main(String[] args) {
//        MyList<Person> mylist = new MyList<>();
//        mylist.add(new Person("Adarsh", 1));
//        mylist.add(new Person("Akash", 2));
//        mylist.add(new Person("Akarsh", 3));
//        mylist.add(new Person("Avinash", 4));
//        mylist.add(new Person("Abhishek", 5));
//        mylist.add(new Person("Elvish", 6));
//        System.out.println(mylist.size());

        MyList<Integer> list2 = new MyList<>();
        list2.add(1);
        list2.add(2);
        list2.add(12);
        list2.add(3);
        list2.add(41);
        list2.add(25);
        list2.add(63);
        list2.add(7);
//        System.out.println(mylist);
//        System.out.println(mylist.binarySearch(  new Comparator_new() , new Person("Akash", 2)));
        System.out.println(list2);
        System.out.println(list2.size());
        list2.sort(new Comparator_new());
        System.out.println(list2);
        System.out.println(list2.binarySearch(new Comparator_new(), 25));

    }
}
}