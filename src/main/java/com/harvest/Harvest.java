package com.harvest;

import java.io.File;
import java.text.ParseException;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class Harvest {
    public double best_gatherer_monthly(String name , List<Farmer> f, int month ) throws ParseException {
        double sum = 0d;
        for(int i =0; i < f.size();i++){
            if(Objects.equals(name, f.get(i).getName())
                    && month == f.get(i).getMonth()){
                sum = sum + f.get(i).getQuantity();
            }
        }
        return sum;
    }

    // Monthly fruit earning
    public double best_fruit_earning_monthly(String fruit,List<Farmer> f, List<Price>p, int month) throws ParseException {
        double sum = 0d;
        for (int i = 0; i < f.size(); i++) {
            for (int j = 0; j < p.size(); j++) {
                if(Objects.equals(fruit, f.get(i).getFruit()) &&
                        Objects.equals(fruit, p.get(j).getFruit()) &&
                        Objects.equals(f.get(i).getDate(), p.get(j).getDate()) &&
                        month == f.get(i).getMonth()){
                    sum = sum + (f.get(i).getQuantity() * p.get(j).getPrice());
                }
            }
        }
        return sum;
    }
    // Monthly best gatherer with respect to income
    public double gatherer_monthly_income(String name, List<Farmer> f, List<Price>p, int month) throws ParseException {
        double sum = 0d;
        for(int i =0; i<f.size();i++){
            for (int j = 0; j < p.size(); j++) {
                if(Objects.equals(name, f.get(i).getName()) &&
                        Objects.equals(f.get(i).getFruit(), p.get(j).getFruit()) &&
                        Objects.equals(f.get(i).getDate(), p.get(j).getDate()) &&
                        f.get(i).getMonth() == month){
                    sum = sum + ((f.get(i).getQuantity()) * (p.get(j).getPrice()));
                }
            }
        }
        return sum;
    }
    // Method to get gatherer's overall income
    public double gatherer_income_overall(String name, List<Farmer> f, List<Price>p){
        double sum = 0d;
        for (int i = 0; i < f.size(); i++) {
            for (int j = 0; j < p.size(); j++) {
                if(Objects.equals(name, f.get(i).getName()) &&
                        Objects.equals(f.get(i).getFruit(), p.get(j).getFruit()) &&
                        Objects.equals(f.get(i).getDate(), p.get(j).getDate())){
                    sum = sum + ((f.get(i).getQuantity()) * (p.get(j).getPrice()));
                }
            }
        }
        return sum;
    }
    // method to get the best earning fruit overall
    public double best_earning_fruit_overall(String name, List<Farmer> f, List<Price> p){
        double sum = 0d;
        for (int i = 0; i < f.size(); i++) {
            for (int j = 0; j < p.size(); j++) {
                if(Objects.equals(name, f.get(i).getFruit()) &&
                        Objects.equals(name, p.get(j).getFruit())){
                    if(Objects.equals(f.get(i).getDate(), p.get(j).getDate())){
                        sum = sum + ((f.get(i).getQuantity()) * (p.get(j).getPrice()));
                    }
                }
            }
        }
        return sum;
    }
    // Method to get employees that are better at gathering some specific fruit
    public double gatherer_best_specific_fruit(String name ,String fruit_name, List<Farmer>f){
        double sum = 0d;
        for(int i =0; i< f.size();i++){
            if(Objects.equals(name, f.get(i).getName()) &&
                    Objects.equals(fruit_name, f.get(i).getFruit())){
                sum = sum + f.get(i).getQuantity();
            }
        }
        return sum;
    }
    public static void main(String[] args) throws Exception {
        // reading the file Harvest.csv
        List<Farmer> farmers = new ArrayList<>();
        Scanner sc = new Scanner(new File("src/main/resources/Harvest.csv"));
        sc.useDelimiter(",");
        while (sc.hasNextLine())
        {
            String line = sc.nextLine();
            String[] arr5 = line.split(",");
            Farmer a = new Farmer(arr5[0], arr5[1], arr5[2], arr5[3]);
            farmers.add(a);
        }
        sc.close();

        // reading the file price.csv
        List<Price> prices = new ArrayList<>();
        Scanner sc1 = new Scanner(new File("src/main/resources/prices.csv"));
        sc1.useDelimiter(",");
        while (sc1.hasNextLine())
        {
            String line = sc1.nextLine();
            String[] arr1 = line.split(",");
            Price a = new Price(arr1[0], arr1[1], arr1[2]);
            prices.add(a);
        }
        sc1.close();
        for (int i = 0; i < 49; i++) {
            farmers.remove(0);
        }

        // String the name of gatherers and fruits in list.
        List<String> name = farmers.stream().map(x -> x.getName()).distinct().collect(Collectors.toList());
        List<String> fruit = farmers.stream().map(x -> x.getFruit()).distinct().collect(Collectors.toList());
        Harvest obj_harvest = new Harvest();

        // This map is used to store the best earning fruit overall along with its name.
        Map<String,Double> best_earning_fruit_overall = new HashMap<>();
        for (int i =0; i< fruit.size();i++){
            Double sum = obj_harvest.best_earning_fruit_overall(fruit.get(i), farmers, prices);
            best_earning_fruit_overall.put(fruit.get(i),sum );
        }
        System.out.println(" Best earning fruit overall along with its name. ");
        System.out.println(best_earning_fruit_overall);

        // This map is used to store overall income of each gatherer.
        Map<String,Double> earning_overall = new HashMap<>();
        for(int i = 0; i< name.size();i++){
            Double sum = obj_harvest.gatherer_income_overall(name.get(i),farmers ,prices);
            earning_overall.put(name.get(i),sum );
        }
        System.out.println("\n Overall income of each gatherer. ");
        System.out.println(earning_overall);

        // Monthly best profitable fruit and least profitable fruit.
        Map<Month,String> least_earning_fruit = new EnumMap<>(Month.class);
        Map<Month,String> best_earning_fruit = new EnumMap<>(Month.class);
        for (int i = 0; i < 12; i++) {
            double max_sum = 0d;
            double min_sum = 100000d;
            String fruit_max = "" ;
            String fruit_min = "";
            for (int j = 0; j < fruit.size(); j++) {
                double sum = obj_harvest.best_fruit_earning_monthly(fruit.get(j),farmers,prices,i );
                if(sum > max_sum){
                    max_sum = sum;
                    fruit_max = fruit.get(j);
                }
                if (sum <= min_sum){
                    min_sum = sum;
                    fruit_min = fruit.get(j);
                }
            }
            least_earning_fruit.put((Month.of(i+1)),fruit_min);
            best_earning_fruit.put((Month.of(i+1)),fruit_max);
        }
        System.out.println("\n Monthly best earning fruit log");
        System.out.println(best_earning_fruit);
        System.out.println("\n Monthly least earning fruit log");
        System.out.println(least_earning_fruit);

        // best gatherer in month
        Map<Month,String> best_gatherer_monthly_fruit = new EnumMap<>(Month.class);

        for(int i = 0;i<12;i++){
            String s1 = "";
            double max = 0;
            for(int j = 0;j< name.size();j++){
                double sum = obj_harvest.best_gatherer_monthly(name.get(j),farmers,i);
                if(sum > max){
                    max = sum;
                    s1 = name.get(j);
                }
            }
            best_gatherer_monthly_fruit.put((Month.of(i + 1)), s1);
        }
        System.out.println("\n Maximum monthly collection by gatherer");
        System.out.println(best_gatherer_monthly_fruit);

        // Monthly best gatherer by income
        Map<Month,String> monthly_best_gatherer_income= new EnumMap<>(Month.class);
        for(int i = 0; i < 12 ;i++){
            String s1 = "";
            double max = 0d;
            for(int j = 0; j< name.size();j++){
                double sum = obj_harvest.gatherer_monthly_income(name.get(j),farmers,prices,i );
                if (sum > max){
                    max = sum;
                    s1 = name.get(j);
                }
            }
            monthly_best_gatherer_income.put((Month.of(i+1)),s1);
        }
        System.out.println("\n Maximum income generated in each month by gatherer");
        System.out.println(monthly_best_gatherer_income);

        // Employees that are best at gathering specific fruits.
        Map<String,String> gatherer_collecting_specific_fruit = new HashMap<>();
        for (int i = 0; i < name.size(); i++) {
            String s1 = "";
            double max = 0d;
            for (int j = 0; j < fruit.size(); j++) {
                double sum = obj_harvest.gatherer_best_specific_fruit(name.get(i), fruit.get(j), farmers);
                if(sum > max){
                    max = sum;
                    s1 = fruit.get(j);
                }
            }
            gatherer_collecting_specific_fruit.put(name.get(i),s1);
        }
        System.out.println("\n Gatherer best in collecting specific fruit");
        System.out.println(gatherer_collecting_specific_fruit);
    }
}