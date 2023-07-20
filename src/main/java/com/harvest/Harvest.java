package com.harvest;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
public class Harvest {
    private static final DecimalFormat df = new DecimalFormat("0.00");

//     method to get the best profitable and least profitable fruit overall
    public double earning_fruit_overall(String name, List<Farmer> f, List<Price> p){
        double sum = 0d;
        for (Farmer farmer : f) {
            for (Price price : p) {
                if ((name.equals(farmer.getFruit())) &&
                        (name.equals(price.getFruit()))) {
                    if ((farmer.getDate().equals(price.getDate()))) {
                        sum = sum + ((farmer.getQuantity()) * (price.getPrice()));
                    }
                }
            }
        }
        return sum;
    }
    // Monthly fruit earning
    public double fruit_earning_monthly(String fruit, List<Farmer> f, List<Price>p, int month)  {
        double sum = 0d;
        for (Farmer farmer : f) {
            for (Price price : p) {
                if ((fruit.equals(farmer.getFruit())) &&
                        (fruit.equals(price.getFruit())) &&
                        (farmer.getDate().equals(price.getDate())) &&
                        month == (farmer.getMonth())) {
                    sum = sum + (farmer.getQuantity() * price.getPrice());
                }
            }
        }
        return sum;
    }
    // Monthly best gatherer with respect to income
    public double gatherer_monthly_income(String name, List<Farmer> f, List<Price>p, int month) {
        double sum = 0d;
        for (Farmer farmer : f) {
            for (Price price : p) {
                if ((name.equals(farmer.getName())) &&
                        (farmer.getFruit().equals(price.getFruit())) &&
                        (farmer.getDate().equals(price.getDate())) &&
                        farmer.getMonth() == month) {
                    sum = sum + ((farmer.getQuantity()) * (price.getPrice()));
                }
            }
        }
        return sum;
    }


    // Method to get gatherer's overall income
    public double gatherer_income_overall(String name, List<Farmer> f, List<Price>p){
        double sum = 0d;
        for (Farmer farmer : f) {
            for (Price price : p) {
                if ((name.equals(farmer.getName())) &&
                        (farmer.getFruit().equals(price.getFruit())) &&
                        (farmer.getDate().equals(price.getDate()))) {
                    sum = sum + ((farmer.getQuantity()) * (price.getPrice()));
                }
            }
        }
        return sum;
    }
    public static  void main(String[] args) throws Exception {
        // reading the file Harvest.csv
        List<Farmer> farmers = getFarmers();
        farmers.remove(0);  // to remove column header

        // reading the file price.csv
        List<Price> prices = getPrices();
        prices.remove(0); // to remove column header

        // Storing the name of gatherers and fruits in list.
        List<String> name = farmers.stream().map(Farmer::getName).distinct().toList();
        List<String> fruit = farmers.stream().map(Farmer::getFruit).distinct().toList();
        Harvest obj_harvest = new Harvest();

        // best gatherer in month
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
        // Employees that are best at gathering specific fruits.

        Map<String, Map<String,Double>> map2 = farmers.stream()
                .collect(
                        Collectors.groupingBy((Farmer::getFruit) ,
                                Collectors.groupingBy(Farmer::getName,
                                        Collectors.mapping(Farmer::getQuantity,
                                                Collectors.reducing(0d,Double::sum)))));


        Map<String , Gatherer> fruitCollection = map2.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        x->new Gatherer(x.getValue().entrySet().stream().max(
                                Comparator.comparingDouble(Map.Entry::getValue)).get().getKey() ,
                                x.getValue().entrySet().stream().max(
                                        Comparator.comparingDouble(Map.Entry::getValue)).get().getValue())));

        System.out.println("\n Gatherer best in collecting specific fruit");
        fruitCollection.forEach((key, value) -> System.out.println(key + ": " + value));

        // This map is used to store the best earning fruit overall along with its name.
        Map<String,Double> earning_from_fruit_overall = new HashMap<>();
        for (String value : fruit) {
            Double sum = obj_harvest.earning_fruit_overall(value, farmers, prices);
            earning_from_fruit_overall.put(value, Double.valueOf(df.format(sum)));
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
            for (String s : fruit) {
                double sum = obj_harvest.fruit_earning_monthly(s, farmers, prices, i);
                if (sum > max_sum) {
                    max_sum = sum;
                    fruit_max = s;
                }
                if (sum <= min_sum) {
                    min_sum = sum;
                    fruit_min = s;
                }
            }
            least_earning_fruit.put((Month.of(i)),fruit_min);
            best_earning_fruit.put((Month.of(i)),fruit_max);
        }
        System.out.println("\n Monthly best earning fruit log \n" + best_earning_fruit);
        System.out.println("\n Monthly least earning fruit log\n" + least_earning_fruit);

        // This map is used to store overall income of each gatherer.
        Map<String,Double> earning_overall = new HashMap<>();
        for (String s : name) {
            Double sum = obj_harvest.gatherer_income_overall(s, farmers, prices);
            earning_overall.put(s, Double.valueOf(df.format(sum)));
        }
        System.out.println("\n Overall income of each gatherer. \n" + earning_overall);

        // Monthly best gatherer by income
        Map<Month,String> monthly_best_gatherer_income= new EnumMap<>(Month.class);
        for(int i = 1; i <= 12 ;i++){
            String s1 = "";
            double max = 0d;
            for (String s : name) {
                double sum = obj_harvest.gatherer_monthly_income(s, farmers, prices, i);
                if (sum > max) {
                    max = sum;
                    s1 = s;
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
    private static class Pricing{
        private String fruit;
        private String date;
        public Pricing(String name ,String date){
            this.fruit = name;
            this.date = date;
        }
        public String getFruit() {
            return fruit;
        }
        public String getDate() {
            return date;
        }
        @Override
        public String toString() {
            return "{" + "name='" + fruit + '\'' + ", date=" + date + '}';
        }

    }
    private static class Gatherer {
        private String name;
        private double qty;

        public Gatherer(String name, Double qty) {
            this.name = name;
            this.qty = qty;
        }
        @Override
        public String toString() {
            return "{" + "name='" + name + '\'' + ", qty=" + qty + '}';
        }
    }
}