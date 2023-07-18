package com.harvest;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Harvest {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    // Best gatherer in terms of the amount of fruits gathered every month.
    public static double monthly_best_gatherer(String name, List<Farmer> f, int month) throws ParseException{
        double  sum = f.stream()
                .filter(e -> e.getName().equals(name) && month==e.getMonth())
                .map(Farmer::getQuantity)
                .mapToDouble(n->n).summaryStatistics().getSum();
        return sum;
    }
    // Method to get employees that are better at gathering some specific fruit
    public double gatherer_best_specific_fruit(String name , String fruit_name, List<Farmer>f){
        double sum = f.stream()
                .filter(e ->e.getName().equals(name)
                        && (e.getFruit().equals(fruit_name)))
                .map(Farmer::getQuantity)
                .mapToDouble(e->e).sum();
        return sum;
    }

//     method to get the best profitable and least profitable fruit overall
    public double earning_fruit_overall(String name, List<Farmer> f, List<Price> p){
        double sum = 0d;
        for (int i = 0; i < f.size(); i++) {
            for (int j = 0; j < p.size(); j++) {
                if((name.equals(f.get(i).getFruit())) &&
                        (name.equals(p.get(j).getFruit()))){
                    if((f.get(i).getDate().equals(p.get(j).getDate()))){
                        sum = sum + ((f.get(i).getQuantity()) * (p.get(j).getPrice()));
                    }
                }
            }
        }
        return sum;
    }
    // Monthly fruit earning
    public double fruit_earning_monthly(String fruit, List<Farmer> f, List<Price>p, int month) throws ParseException {
        double sum = 0d;
        for (int i = 0; i < f.size(); i++) {
            for (int j = 0; j < p.size(); j++) {
                if((fruit.equals(f.get(i).getFruit())) &&
                        (fruit.equals(p.get(j).getFruit())) &&
                        (f.get(i).getDate().equals(p.get(j).getDate())) &&
                        month == ( f.get(i).getMonth())){
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
                if((name.equals(f.get(i).getName()) ) &&
                        (f.get(i).getFruit().equals(p.get(j).getFruit()) ) &&
                        (f.get(i).getDate().equals(p.get(j).getDate())) &&
                        f.get(i).getMonth()== month){
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
                if((name.equals(f.get(i).getName())) &&
                        (f.get(i).getFruit().equals(p.get(j).getFruit())) &&
                        (f.get(i).getDate().equals(p.get(j).getDate()))){
                    sum = sum + ((f.get(i).getQuantity()) * (p.get(j).getPrice()));
                }
            }
        }
        return sum;
    }
    public static void main(String[] args) throws Exception {
        // reading the file Harvest.csv
        List<Farmer> farmers = getFarmers();
        farmers.remove(0);  // to remove column header

        // reading the file price.csv
        List<Price> prices = getPrices();
        prices.remove(0); // to remove column header

        // String the name of gatherers and fruits in list.
        List<String> name = farmers.stream().map(Farmer::getName).distinct().collect(Collectors.toList());
        List<String> fruit = farmers.stream().map(Farmer::getFruit).distinct().collect(Collectors.toList());
        Harvest obj_harvest = new Harvest();

        // best gatherer in month
        Map<Month, Gatherer> best_gatherer_monthly_fruit = new EnumMap<>(Month.class);
        for(int i = 1;i<=12;i++){
            Gatherer g = null;
            double max = 0;
            for(int j = 0;j< name.size();j++){
                double sum = monthly_best_gatherer(name.get(j),farmers,i);
                if(sum > max){
                    max = sum;
                    g =new Gatherer(name.get(j), sum);
                }
            }
            best_gatherer_monthly_fruit.put((Month.of(i)), g);
        }
        System.out.println("\n Maximum monthly fruit collection by gatherer \n" + best_gatherer_monthly_fruit);

        // Employees that are best at gathering specific fruits.
        Map<String,String> gatherer_collecting_specific_fruit = new HashMap<>();
        for (int i = 0; i < fruit.size(); i++) {
            String s1 = "";
            double max = 0d;
            for (int j = 0; j < name.size(); j++) {
                double sum = obj_harvest.gatherer_best_specific_fruit(name.get(j), fruit.get(i), farmers);
                if(sum > max){
                    max = sum;
                    s1 = name.get(j);
                }
            }
            gatherer_collecting_specific_fruit.put(fruit.get(i),s1);
        }
        System.out.println("\n Gatherer best in collecting specific fruit \n" + gatherer_collecting_specific_fruit );

        // This map is used to store the best earning fruit overall along with its name.
        Map<String,Double> earning_from_fruit_overall = new HashMap<>();
        for (int i =0; i< fruit.size();i++){
            Double sum = obj_harvest.earning_fruit_overall(fruit.get(i), farmers, prices);
            earning_from_fruit_overall.put(fruit.get(i), Double.valueOf(df.format(sum)));
        }
        System.out.println("\n Best earning fruit overall along with its name. \n" + earning_from_fruit_overall);

        // Monthly best profitable fruit and least profitable fruit.
        Map<Month,String> least_earning_fruit = new EnumMap<>(Month.class);
        Map<Month,String> best_earning_fruit = new EnumMap<>(Month.class);
        for (int i = 1; i <= 12; i++) {
            double max_sum = 0d;
            double min_sum = 100000d;
            String fruit_max = "" ;
            String fruit_min = "";
            for (int j = 0; j < fruit.size(); j++) {
                double sum = obj_harvest.fruit_earning_monthly(fruit.get(j),farmers,prices,i );
                if(sum > max_sum){
                    max_sum = sum;
                    fruit_max = fruit.get(j);
                }
                if (sum <= min_sum){
                    min_sum = sum;
                    fruit_min = fruit.get(j);
                }
            }
            least_earning_fruit.put((Month.of(i)),fruit_min);
            best_earning_fruit.put((Month.of(i)),fruit_max);
        }
        System.out.println("\n Monthly best earning fruit log \n" + best_earning_fruit);
        System.out.println("\n Monthly least earning fruit log\n" + least_earning_fruit);

        // This map is used to store overall income of each gatherer.
        Map<String,Double> earning_overall = new HashMap<>();
        for(int i = 0; i< name.size();i++){
            Double sum = obj_harvest.gatherer_income_overall(name.get(i),farmers ,prices);
            earning_overall.put(name.get(i), Double.valueOf(df.format(sum)));
        }
        System.out.println("\n Overall income of each gatherer. \n" + earning_overall);

        // Monthly best gatherer by income
        Map<Month,String> monthly_best_gatherer_income= new EnumMap<>(Month.class);
        for(int i = 1; i <= 12 ;i++){
            String s1 = "";
            double max = 0d;
            for(int j = 0; j< name.size();j++){
                double sum = obj_harvest.gatherer_monthly_income(name.get(j),farmers,prices,i );
                if (sum > max){
                    max = sum;
                    s1 = name.get(j);
                }
            }
            monthly_best_gatherer_income.put((Month.of(i)),s1);
        }
        System.out.println("\n Maximum income generated in each month by gatherer\n " + monthly_best_gatherer_income);
    }

    private static List<Price> getPrices() throws FileNotFoundException {
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
        return prices;
    }

    private static List<Farmer> getFarmers() throws FileNotFoundException {
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
        return farmers;
    }
    private static class Gatherer {
        private String name;
        private Double qty;

        public Gatherer(String name, Double qty) {
            this.name = name;
            this.qty = qty;
        }
        @Override
        public String toString() {
            return "Gatherer{" + "name='" + name + '\'' + ", qty=" + qty + '}';
        }
    }
}