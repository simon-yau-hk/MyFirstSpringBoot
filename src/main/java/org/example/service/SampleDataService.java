package org.example.service;

import org.example.entity.Bag;
import org.example.entity.BagItem;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SampleDataService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createSampleData() {
        // Check if data already exists
        if (userRepository.count() > 0) {
            System.out.println("🔍 Sample data already exists. Skipping creation.");
            return;
        }

        System.out.println("🏗️ Creating sample data...");

        // Create Users
        User john = createUser("John Doe", "john.doe@example.com");
        User jane = createUser("Jane Smith", "jane.smith@example.com");
        User mike = createUser("Mike Johnson", "mike.johnson@example.com");

        // Create John's bags and items
        createJohnData(john);
        createJaneData(jane);
        createMikeData(mike);

        // Save all users (cascade will save bags and items)
        userRepository.saveAll(List.of(john, jane, mike));

        System.out.println("✅ Sample data created successfully!");
        printDataSummary();
    }

    private User createUser(String name, String email) {
        return new User(name, email);
    }

    private void createJohnData(User john) {
        // Work Bag
        Bag workBag = new Bag("Work Bag", "Professional items for office");
        workBag.addItem(new BagItem("Laptop", "MacBook Pro 16 inch", new BigDecimal("2500.00"), 1));
        workBag.addItem(new BagItem("Wireless Mouse", "Logitech MX Master 3", new BigDecimal("99.99"), 1));
        workBag.addItem(new BagItem("Notebook", "Moleskine ruled notebook", new BigDecimal("25.50"), 2));
        workBag.addItem(new BagItem("Pen Set", "Premium ballpoint pens", new BigDecimal("45.00"), 1));
        john.addBag(workBag);

        // Travel Bag
        Bag travelBag = new Bag("Travel Bag", "Items for business trips");
        travelBag.addItem(new BagItem("Power Bank", "Anker 20000mAh portable charger", new BigDecimal("65.99"), 1));
        travelBag.addItem(new BagItem("Travel Adapter", "Universal travel plug adapter", new BigDecimal("29.99"), 1));
        travelBag.addItem(new BagItem("Headphones", "Sony WH-1000XM4", new BigDecimal("349.99"), 1));
        john.addBag(travelBag);
    }

    private void createJaneData(User jane) {
        // Gym Bag
        Bag gymBag = new Bag("Gym Bag", "Fitness and workout gear");
        gymBag.addItem(new BagItem("Water Bottle", "Stainless steel water bottle", new BigDecimal("24.99"), 1));
        gymBag.addItem(new BagItem("Towel", "Quick-dry microfiber towel", new BigDecimal("19.99"), 2));
        gymBag.addItem(new BagItem("Protein Powder", "Whey protein supplement", new BigDecimal("45.99"), 1));
        gymBag.addItem(new BagItem("Resistance Bands", "Set of 5 resistance bands", new BigDecimal("29.99"), 1));
        jane.addBag(gymBag);

        // Shopping Bag
        Bag shoppingBag = new Bag("Shopping Bag", "Daily shopping items");
        shoppingBag.addItem(new BagItem("Reusable Bags", "Eco-friendly shopping bags", new BigDecimal("15.99"), 5));
        shoppingBag.addItem(new BagItem("Grocery List App", "Premium subscription", new BigDecimal("4.99"), 1));
        shoppingBag.addItem(new BagItem("Coupons", "Digital coupon organizer", new BigDecimal("9.99"), 1));
        jane.addBag(shoppingBag);
    }

    private void createMikeData(User mike) {
        // Tech Bag
        Bag techBag = new Bag("Tech Bag", "Electronic gadgets and accessories");
        techBag.addItem(new BagItem("iPhone", "iPhone 15 Pro", new BigDecimal("1199.00"), 1));
        techBag.addItem(new BagItem("iPad", "iPad Air with Apple Pencil", new BigDecimal("749.00"), 1));
        techBag.addItem(new BagItem("Cables", "USB-C and Lightning cables", new BigDecimal("39.99"), 3));
        techBag.addItem(new BagItem("Wireless Charger", "MagSafe compatible charger", new BigDecimal("79.99"), 1));
        techBag.addItem(new BagItem("AirPods", "AirPods Pro 2nd Generation", new BigDecimal("249.00"), 1));
        mike.addBag(techBag);

        // Camera Bag
        Bag cameraBag = new Bag("Camera Bag", "Photography equipment");
        cameraBag.addItem(new BagItem("DSLR Camera", "Canon EOS R6 Mark II", new BigDecimal("2499.00"), 1));
        cameraBag.addItem(new BagItem("Lens", "Canon RF 24-70mm f/2.8L", new BigDecimal("2299.00"), 1));
        cameraBag.addItem(new BagItem("Tripod", "Carbon fiber tripod", new BigDecimal("299.99"), 1));
        cameraBag.addItem(new BagItem("Memory Cards", "64GB SD cards", new BigDecimal("49.99"), 3));
        mike.addBag(cameraBag);
    }

    @Transactional(readOnly = true)
    public void printDataSummary() {
        List<User> users = userRepository.findAll();
        System.out.println("\n📊 === SAMPLE DATA SUMMARY ===");
        
        for (User user : users) {
            System.out.println("👤 User: " + user.getName() + " (" + user.getEmail() + ")");
            System.out.println("   📁 Bags: " + user.getBags().size());
            
            for (Bag bag : user.getBags()) {
                System.out.println("      🎒 " + bag.getName() + " - " + bag.getItems().size() + " items, Total: $" + bag.getTotalPrice());
                
                for (BagItem item : bag.getItems()) {
                    System.out.println("         📦 " + item.getName() + " - $" + item.getPrice() + " x" + item.getQuantity());
                }
            }
            System.out.println();
        }
        System.out.println("=== END SUMMARY ===\n");
    }

    @Transactional
    public void clearAllData() {
        System.out.println("🗑️ Clearing all sample data...");
        userRepository.deleteAll();
        System.out.println("✅ All data cleared!");
    }

    @Transactional(readOnly = true)
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public boolean hasData() {
        return userRepository.count() > 0;
    }
}
