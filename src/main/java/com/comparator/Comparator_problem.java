package com.comparator;

import java.util.Comparator;

    public  class Comparator_problem{

    public static void main(String[] args) {
        MyList<Person> mylist = new MyList<>();
        mylist.add(new Person("Adarsh", 5));
        mylist.add(new Person("Akash", 2));
        mylist.add(new Person("Akarsh", 3));
        mylist.add(new Person("Avinash", 4));
        mylist.add(new Person("Abhishek", 1));
        mylist.add(new Person("Elvish", 6));
        System.out.println("Printing Original List\n" + mylist);
        System.out.println("\nSize of list : " + mylist.size());

        mylist.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getId() - o2.getId();
            }
        });
        System.out.println("\nSorted list\n" + mylist);
        int index = mylist.binarySearch(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getId() - o2.getId();
            }
        },new Person("Adarsh",5));
        System.out.println("\nPerson found at index : " + index);

    }
}
