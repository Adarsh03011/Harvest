package com.harvest;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.Month;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;


public class Harvest_using_streams {

    private static class DoubleAdd implements BinaryOperator<Double>{
        @Override
        public Double apply(Double o, Double o2) {
            return Double.sum(o, o2);
        }
    }
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static  void main(String[] args) throws Exception {
        // reading the file Harvest.csv
        List<Farmer> farmer1 = getFarmers();
        // reading the file price.csv
        List<Price> prices = getPrices();
        farmer1.remove(0); // to remove column header
        prices.remove(0); // to remove column header

        List<Farmer> farmers = farmer1.stream().filter(x->!x.getDate().equals("2020-01-01")).toList();

        //----------best gatherer in month---------------------------------------------------------------------
        Map<Integer, Map<String, Double>>  map = farmers.stream()
                .collect(
                        Collectors.groupingBy((Farmer::getMonth),
                                Collectors.groupingBy(Farmer::getName,
                                        Collectors.mapping(Farmer::getQuantity,
                                                Collectors.reducing(0d, Double::sum)))));

        Map<Integer , Gatherer> gathererMonthlyCollection=  map.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                               x-> new Gatherer(x.getValue().entrySet().stream()
                                       .max(Comparator.comparingDouble(Map.Entry::getValue)).get().getKey()
                                       , x.getValue().entrySet().stream().
                                       max(Comparator.comparingDouble(Map.Entry::getValue)).get().getValue()
                               ))
                        );
        System.out.println("\n Printing Monthly best Gatherer");
        gathererMonthlyCollection.forEach((key, value) -> System.out.println(Month.of(key) + " :" + value));


        // ---------Employees that are best at gathering specific fruits.----------------------------
        Map<String, Map<String,Double>> map2 = farmers.stream()
                .collect(
                        Collectors.groupingBy((Farmer::getFruit) ,
                                Collectors.groupingBy(Farmer::getName,
                                        Collectors.mapping(Farmer::getQuantity,
                                                Collectors.reducing(0d, Double::sum
                                                )))));

        Map<String , Gatherer> fruitCollection = map2.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        x->new Gatherer(x.getValue().entrySet().stream().max(
                                Comparator.comparingDouble(Map.Entry::getValue)).get().getKey() ,
                                x.getValue().entrySet().stream().max(
                                        Comparator.comparingDouble(Map.Entry::getValue)).get().getValue())));

        System.out.println("\n Gatherer best in collecting specific fruit");
        fruitCollection.forEach((key, value) -> System.out.println(key + ": " + value));
        // ----------Overall Income of Gatherers----------------------------------------------------------------------
        Map<Pricing,Double> ans = prices.stream()
                .collect(Collectors.toMap(x->new Pricing(x.getFruit(),x.getDate()),
                        Price::getPrice));
        Map<String,Double> overall_income = farmers.stream().collect(
                Collectors.groupingBy(Farmer::getName ,
                        Collectors.mapping(x->x.getQuantity()*ans.get(new Pricing(x.getFruit(),x.getDate())),
                                Collectors.reducing(0d,Double::sum))));
        System.out.println("\n Overall Income of gatherers");
        overall_income.forEach((key,value) -> System.out.println(key + ": " + df.format(value)));
        //------------Overall Income from specific fruit-------------------------------------------------------------
        Map<String,Double> best_fruit_overall = farmers.stream().collect(
                Collectors.groupingBy(Farmer::getFruit,
                        Collectors.mapping(x->x.getQuantity()*ans.get(new Pricing(x.getFruit(),x.getDate())),
                                Collectors.reducing(0d,Double::sum))));
        System.out.println("\n Overall income from specific fruit");
        best_fruit_overall.forEach((key ,value) -> System.out.println(key + ": " + df.format(value)));
        //-----------Gatherer Contributing to maximum monthly income -------------------------------------------------
        Map<Integer ,Map<String, Double>> x1 = farmers.stream().collect(
                Collectors.groupingBy(Farmer::getMonth,
                       Collectors.groupingBy(Farmer::getName,
                               Collectors.mapping(x->x.getQuantity()*ans.get(new Pricing(x.getFruit(),x.getDate())),
                                      Collectors.reducing(0d,Double::sum) )) ));


        Map<Integer, MonthlyPrice> Monthly_best_gatherer = x1.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        x->new MonthlyPrice(x.getValue().entrySet().stream().max(
                                Comparator.comparingDouble(Map.Entry::getValue)).get().getKey() ,
                                x.getValue().entrySet().stream().max(
                                        Comparator.comparingDouble(Map.Entry::getValue)).get().getValue())));
        System.out.println("\n Monthly best gatherer ");
        Monthly_best_gatherer.forEach((key,value)-> System.out.println(Month.of(key) + ": " + value));


        // ---------Monthly best profitable fruit and least profitable fruit.------------------------------------
        Map<Integer ,Map<String, Double>> x2 = farmers.stream().collect(
                Collectors.groupingBy(Farmer::getMonth,
                        Collectors.groupingBy(Farmer::getFruit,
                                Collectors.mapping(x->x.getQuantity()*ans.get(new Pricing(x.getFruit(),x.getDate())),
                                        Collectors.reducing(0d,Double::sum) )) ));
        Map<Integer, MonthlyPrice> Monthly_best_fruit = x2.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        x->new MonthlyPrice(x.getValue().entrySet().stream().max(
                                Comparator.comparingDouble(Map.Entry::getValue)).get().getKey() ,
                                x.getValue().entrySet().stream().max(
                                        Comparator.comparingDouble(Map.Entry::getValue)).get().getValue())));
        System.out.println("\n Monthly Most profitable fruit.");
        Monthly_best_fruit.forEach((key,value)-> System.out.println(Month.of(key) + ": " + value));

        // ---------Monthly least profitable fruit and least profitable fruit.------------------------------------
        Map<Integer, MonthlyPrice> Monthly_worst_fruit = x2.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        x->new MonthlyPrice(x.getValue().entrySet().stream().min(
                                Comparator.comparingDouble(Map.Entry::getValue)).get().getKey() ,
                                x.getValue().entrySet().stream().min(
                                        Comparator.comparingDouble(Map.Entry::getValue)).get().getValue())));
        System.out.println("\n Monthly least profitable fruit");
        Monthly_worst_fruit.forEach((key,value)-> System.out.println(Month.of(key) + ": " + value));
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
    private static class MonthlyPrice {
        private final String fruit;
        private final double amount;

        private final int hashCode;
        public MonthlyPrice(String name, double amount){
            this.fruit = name;
            this.amount = amount;
            this.hashCode = Objects.hash(name,amount);
        }
        public String getFruit() {
            return fruit;
        }
        public double getAmount(){
            return amount;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            MonthlyPrice that = (MonthlyPrice) o;
            return fruit.equals(that.fruit) && amount == that.amount;
        }
        @Override
        public int hashCode() {
            return this.hashCode;
        }
        @Override
        public String toString() {
            return "{" + "name='" + fruit + '\'' + ", amount=" + df.format(amount) + '}';
        }
    }
    private static class Pricing {
        private final String fruit;
        private final String date;
        private final int hashCode;
        public Pricing(String name ,String date){
            this.fruit = name;
            this.date = date;
            this.hashCode = Objects.hash(name,date);
        }
        public String getFruit() {
            return fruit;
        }
        public String getDate() {
            return date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Pricing that = (Pricing) o;
            return fruit.equals(that.fruit) && date.equals(that.date);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }
        @Override
        public String toString() {
            return "{" + "name='" + fruit + '\'' + ", date=" + date + '}';
        }
    }
    private static class Gatherer {
        private final String name;
        private final double qty;

        public Gatherer(String name, Double qty) {
            this.name = name;
            this.qty = qty;
        }
        @Override
        public String toString() {
            return "{" + "name='" + name + '\'' + ", qty=" + df.format(qty) + '}';
        }
    }
}