package carsharing;

import java.util.ArrayList;
import carsharing.Car.Car;
import carsharing.Car.CarDaoImpl;
import carsharing.Company.Company;
import carsharing.Company.CompanyDaoImpl;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isFinished = false;
    private static int choice;

    public static void initUI() {
        while (!isFinished) {
            log("1. Log in as a manager\n0. Exit");


            /*2. Log in as a customer
            3. Create a customer*/

            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> initSubMenu();
                //case 2 ->
                //case 3 ->
                case 0 -> isFinished = true;
                default -> log("wrong input, try again\n");
            }
        }
    }

    public static void initSubMenu(){
        boolean finish = false;

        while(!finish) {
            log("1. Company list\n2. Create a company\n0. Back");

            choice = scanner.nextInt();
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

        while (!finish) {

            log("1. Car list");
            log("2. Create a car");
            log("0. Back");

            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> listCars(COMPANY);
                case 2 -> createCar(COMPANY);
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
                companyList.forEach(company -> System.out.println(company.toString()));
                log("0. Back");

                choice = scanner.nextInt();

                if (choice == 0) {
                    finish = true;
                } else {
                    finish = initCompanyMenu(choice);
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
        }
    }

    public static void createCompany() {
        log("Enter the company name:");

        Scanner scanner = new Scanner(System.in);
        String NAME = scanner.nextLine();

        CompanyDaoImpl.addNewRecord(NAME);
    }

    public static void createCar(int COMPANY) {
        log("Enter the car name:");

        Scanner scanner = new Scanner(System.in);
        String NAME = scanner.nextLine();

        CarDaoImpl.addNewRecord(NAME,COMPANY);
    }


    public static void log(String msg) {
        System.out.println(msg);
    }
}
