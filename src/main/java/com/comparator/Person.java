package com.comparator;

public class Person{

        String name;
        int id;
        public Person(String name, int id){
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }
        public int  getId() {
            return (id);
        }

        @Override
        public String toString() {
            return name + " " + id;
        }

    }
