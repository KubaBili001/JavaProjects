package carsharing;

import java.util.ArrayList;
import carsharing.Car.Car;
import carsharing.Car.CarDaoImpl;
import carsharing.Company.Company;
import carsharing.Company.CompanyDaoImpl;
import carsharing.Customer.Customer;
import carsharing.Customer.CustomerDaoImpl;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Menu {

    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isFinished = false;
    private static int choice;

    public static void initUI() {
        while (!isFinished) {
            log("1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit");

            choice();

            switch (choice) {
                case 1 -> initSubMenu();
                case 2 -> listCustomers();
                case 3 -> createCustomer();
                case 0 -> isFinished = true;
                default -> log("wrong input, try again\n");
            }
        }
    }

    public static void initSubMenu(){
        boolean finish = false;

        while(!finish) {
            log("1. Company list\n2. Create a company\n0. Back");

            choice();

            switch (choice) {
                case 1 -> listCompanies();
                case 2 -> createCompany();
                case 0 -> finish = true;
                default -> log("wrong input, try again\n");
            }
        }
    }

    public static boolean initCompanyMenu(int COMPANY) {
        boolean finish = false;

        while (true) {

            log("1. Car list");
            log("2. Create a car");
            log("0. Back");

            choice();

            switch (choice) {
                case 1 -> listCars(COMPANY);
                case 2 -> createCar(COMPANY);
                case 0 -> {
                    return true;
                }
            }
        }
    }

    public static boolean initCustomerMenu(int CUSTOMER_ID) {
        while (true) {

            log("1. Rent a car\n2. Return a rented car\n3. My rented car\n0. Back");

            choice();

            switch (choice) {
                case 1 -> chooseCompany(CUSTOMER_ID);
                //case 2 ->
                case 0 -> {
                    return true;
                }
            }
        }
        return false;
    }

    public static void listCompanies() {
        ArrayList<Company> companyList = CompanyDaoImpl.listAll();

        boolean finish = false;

        if(companyList.isEmpty()) {
            log("The company list is empty!\n");
        } else {

            while (!finish) {

                log("Choose the company:");
                AtomicInteger counter = new AtomicInteger(0);
                companyList.forEach(company -> System.out.println(counter.incrementAndGet() + ". " + company.getName()));
                log("0. Back");

                choice();

                if (choice == 0) {
                    finish = true;
                } else {
                    finish = initCompanyMenu(choice);
                }
            }
        }
    }

    public static void listCustomers() {
        ArrayList<Customer> customerList = CustomerDaoImpl.listAll();

        boolean finish = false;

        if(customerList.isEmpty()) {
            log("The customer list is empty!\n");
        } else {

            while (!finish) {

                log("Choose the customer:");
                AtomicInteger counter = new AtomicInteger(0);
                customerList.forEach(customer -> System.out.println(counter.incrementAndGet() + ". " + customer.getName()));
                log("0. Back");

                choice();

                if (choice == 0) {
                    finish = true;
                } else {
                    finish = initCustomerMenu(choice);
                }
            }
        }
    }

    public static void listCars(int COMPANY) {
        ArrayList<Car> carList = CarDaoImpl.listCompanyCars(COMPANY);

        if(carList.isEmpty()) {
            log("The car list is empty!\n");
        } else {
            AtomicInteger counter = new AtomicInteger(0);
            carList.forEach(car -> System.out.println(counter.incrementAndGet() + ". " + car.getName()));
            log("");
        }
    }

    public static void createCompany() {
        log("Enter the company name:");

        System.out.print("> ");

        Scanner scanner = new Scanner(System.in);
        String NAME = scanner.nextLine();

        CompanyDaoImpl.addNewRecord(NAME);
    }

    public static void createCar(int COMPANY) {
        log("Enter the car name:");

        System.out.print("> ");

        Scanner scanner = new Scanner(System.in);
        String NAME = scanner.nextLine();

        CarDaoImpl.addNewRecord(NAME,COMPANY);
    }

    public static void createCustomer() {
        log("Enter the customer name:");

        System.out.print("> ");

        Scanner scanner = new Scanner(System.in);
        String NAME = scanner.nextLine();

        CustomerDaoImpl.addNewRecord(NAME);
    }

    public static void choice() {
        System.out.print("> ");
        choice = scanner.nextInt();
        System.out.println();
    }

    public static void chooseCompany(int CUSTOMER_ID) {
        ArrayList<Company> companyList = CompanyDaoImpl.listAll();
        boolean finish = false;

        if(companyList.isEmpty()) {
            log("The company list is empty!\n");
        } else {

            while (!finish) {

                log("Choose the company:");
                AtomicInteger counter = new AtomicInteger(0);
                companyList.forEach(company -> System.out.println(counter.incrementAndGet() + ". " + company.getName()));
                log("0. Back");

                choice();

                if (choice == 0) {
                    finish = true;
                } else {
                    finish = chooseCar(CUSTOMER_ID, choice);
                }
            }
        }
    }

    public static boolean chooseCar(int CUSTOMER_ID, int COMPANY_ID) {
        ArrayList<Car> carList = CarDaoImpl.listCompanyCars(COMPANY_ID);

        if(carList.isEmpty()) {
            log("The car list is empty!\n");
            return true;
        } else {
            AtomicInteger counter = new AtomicInteger(0);
            carList.forEach(car -> System.out.println(counter.incrementAndGet() + ". " + car.getName()));
            log("");

            choice();

            CustomerDaoImpl.updateRecord(CUSTOMER_ID,COMPANY_ID);
        }

        return false;
    }

    public static void log(String msg) {
        System.out.println(msg);
    }
}
