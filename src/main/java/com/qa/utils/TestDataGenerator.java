package com.qa.utils;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestDataGenerator {

    private static final Logger log = LogManager.getLogger(TestDataGenerator.class);

    // ThreadLocal stores per-thread data so parallel tests don't interfere
    private static final ThreadLocal<String> currentEmail = new ThreadLocal<>();
    private static final ThreadLocal<String> currentPassword = new ThreadLocal<>();
    private static final ThreadLocal<String> currentName = new ThreadLocal<>();
    private static final ThreadLocal<String> currentFirstName = new ThreadLocal<>();
    private static final ThreadLocal<String> currentLastName = new ThreadLocal<>();
    private static final ThreadLocal<String> currentAddress = new ThreadLocal<>();
    private static final ThreadLocal<String> currentState = new ThreadLocal<>();
    private static final ThreadLocal<String> currentCity = new ThreadLocal<>();
    private static final ThreadLocal<String> currentZip = new ThreadLocal<>();
    private static final ThreadLocal<String> currentMobile = new ThreadLocal<>();

    // --- Generators ---

    public static String generateDobDay() {
        String day = String.valueOf(ThreadLocalRandom.current().nextInt(1, 28));
        // Max 28 to avoid invalid dates like Feb 30
        log.info("Generated DOB day: {}", day);
        return day;
    }

    public static String generateDobMonth() {
        String month = String.valueOf(ThreadLocalRandom.current().nextInt(1, 12));
        log.info("Generated DOB month: {}", month);
        return month;
    }

    public static String generateDobYear() {
        int currentYear = LocalDate.now().getYear();
        // Between 18 and 50 years old
        String year = String.valueOf(
            ThreadLocalRandom.current().nextInt(currentYear - 50, currentYear - 18)
        );
        log.info("Generated DOB year: {}", year);
        return year;
    }
    
    public static String generateEmail() {
        String email = "user_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
        currentEmail.set(email);
        log.info("Generated email: {}", email);
        return email;
    }

    public static String generatePassword() {
        String password = UUID.randomUUID().toString().substring(0, 8);
        currentPassword.set(password);
        log.info("Generated password: {}", password);
        return password;
    }

    public static String generateName() {
        String name = "User_" + System.currentTimeMillis();
        currentName.set(name);
        log.info("Generated name: {}", name);
        return name;
    }

    public static String generateFirstName() {
        String firstName = "First_" + UUID.randomUUID().toString().substring(0, 5);
        currentFirstName.set(firstName);
        log.info("Generated first name: {}", firstName);
        return firstName;
    }

    public static String generateLastName() {
        String lastName = "Last_" + UUID.randomUUID().toString().substring(0, 5);
        currentLastName.set(lastName);
        log.info("Generated last name: {}", lastName);
        return lastName;
    }

    public static String generateAddress() {
        String address = UUID.randomUUID().toString().substring(0, 5).toUpperCase()
            + " Test Street";
        currentAddress.set(address);
        log.info("Generated address: {}", address);
        return address;
    }

    public static String generateState() {
        // Fixed realistic value - state doesn't need to be random
        String state = "West Bengal";
        currentState.set(state);
        log.info("Generated state: {}", state);
        return state;
    }

    public static String generateCity() {
        String city = "Siliguri";
        currentCity.set(city);
        log.info("Generated city: {}", city);
        return city;
    }

    public static String generateZip() {
        // Random 6 digit zip
        String zip = String.valueOf(100000 + (int)(Math.random() * 900000));
        currentZip.set(zip);
        log.info("Generated zip: {}", zip);
        return zip;
    }

    public static String generateMobile() {
        // Random 10 digit number starting with 9
        String mobile = "9" + String.valueOf(100000000 + (int)(Math.random() * 900000000));
        currentMobile.set(mobile);
        log.info("Generated mobile: {}", mobile);
        return mobile;
    }

    // --- Getters ---

    public static String getCurrentEmail() { return currentEmail.get(); }
    public static String getCurrentPassword() { return currentPassword.get(); }
    public static String getCurrentName() { return currentName.get(); }
    public static String getCurrentFirstName() { return currentFirstName.get(); }
    public static String getCurrentLastName() { return currentLastName.get(); }
    public static String getCurrentAddress() { return currentAddress.get(); }
    public static String getCurrentState() { return currentState.get(); }
    public static String getCurrentCity() { return currentCity.get(); }
    public static String getCurrentZip() { return currentZip.get(); }
    public static String getCurrentMobile() { return currentMobile.get(); }

    // --- Cleanup ---

    // Call in Hooks @After to prevent memory leaks between tests
    public static void clearAll() {
        currentEmail.remove();
        currentPassword.remove();
        currentName.remove();
        currentFirstName.remove();
        currentLastName.remove();
        currentAddress.remove();
        currentState.remove();
        currentCity.remove();
        currentZip.remove();
        currentMobile.remove();
        log.info("Cleared all test data for current thread");
    }
}