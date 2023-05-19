import java.util.Random;
import java.util.Scanner;

class DumplingShopSimulator {
    private static final int PORK_DUMPLINGS = 5000;
    private static final int BEEF_DUMPLINGS = 3000;
    private static final int VEGETABLE_DUMPLINGS = 1000;

    private static int availablePorkDumplings = PORK_DUMPLINGS;
    private static int availableBeefDumplings = BEEF_DUMPLINGS;
    private static int availableVegetableDumplings = VEGETABLE_DUMPLINGS;

    private static final Object lock = new Object();

    public static void main(String[] args) {
        int numCustomers = getNumCustomersFromUser();
        Thread[] customerThreads = new Thread[numCustomers];

        for (int i = 0; i < numCustomers; i++) {
            customerThreads[i] = new Thread(() -> {
                int dumplingCount = generateRandomDumplingCount();
                String dumplingType = generateRandomDumplingType();
                orderDumplings(dumplingType, dumplingCount);
            });
            customerThreads[i].start();
        }

        for (int i = 0; i < numCustomers; i++) {
            try {
                customerThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All customers have been served. Closing the dumpling shop.");
    }

    private static int getNumCustomersFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of customers: ");
        return scanner.nextInt();
    }

    private static int generateRandomDumplingCount() {
        Random random = new Random();
        return random.nextInt(41) + 10; // 產生10到50之間的亂數
    }

    private static String generateRandomDumplingType() {
        Random random = new Random();
        int randomNumber = random.nextInt(3);
        switch (randomNumber) {
            case 0:
                return "豬肉水餃";
            case 1:
                return "牛肉水餃";
            default:
                return "蔬菜水餃";
        }
    }

    private static void orderDumplings(String dumplingType, int count) {
        synchronized (lock) {
            if (dumplingType.equals("豬肉水餃") && count <= availablePorkDumplings) {
                System.out.println("Serving " + count + " " + dumplingType);
                availablePorkDumplings -= count;
            } else if (dumplingType.equals("牛肉水餃") && count <= availableBeefDumplings) {
                System.out.println("Serving " + count + " " + dumplingType);
                availableBeefDumplings -= count;
            } else if (dumplingType.equals("蔬菜水餃") && count <= availableVegetableDumplings) {
                System.out.println("Serving " + count + " " + dumplingType);
                availableVegetableDumplings -= count;
            } else {
                System.out.println("Sorry, we don't have enough " + dumplingType + " for your order.");
            }

            try {
                Thread.sleep(3000); // 等待服務生的時間，暫停3秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}