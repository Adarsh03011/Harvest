package com.comparator;

import java.util.ArrayList;
//import java.util.Comparator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyList<T> {


    private List<T> list;
    public MyList() {
        list = new ArrayList<>();
    }

    public int length(List<T> l){
        if(l.size() == 0){
            return 0;
        }
        return 1 + length(l.subList(1,l.size()));
    }
    public int binarySearch(Comparator<T> comparator , T a){

        int l_bound = 0;
        int u_bound = list.size()-1;
        while (l_bound < u_bound){
            int mid = (l_bound+u_bound)/2;
            T mid_ele = list.get(mid);
            int comparison = comparator.compare(mid_ele,a);
            if(comparison == 0){
                return mid;
            } else if(comparison > 0 ) {
                u_bound = mid-1;
            }else {
                l_bound = mid + 1;
            }
        }
        return  -1;
    }

    public void sort(Comparator<T> comparator) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                T element1 = list.get(j);
                T element2 = list.get(j + 1);
                if (comparator.compare(element1, element2) > 0) {
                    Collections.swap(list ,j,j+1);
                }
            }
        }
    }

    public int size(){
        return length(list);
    }
    public void add(T e){
         list.add(e);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}