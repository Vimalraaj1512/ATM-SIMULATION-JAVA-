import java.util.Scanner;

public class ATMSystem {
    
    // Fixed PIN
    private static final int FIXED_PIN = 1234;
    private static final int MAX_ATTEMPTS = 3;
    
    // Account type
    private static final int SAVINGS_ACCOUNT = 1;
    private static final int CURRENT_ACCOUNT = 2;
    private static final int SALARY_ACCOUNT = 3;
    
    // Transaction type
    private static final int WITHDRAW = 1;
    private static final int DEPOSIT = 2;
    
    // Minimum balance required
    private static final double SAVINGS_MIN_BALANCE = 1000;
    private static final double CURRENT_MIN_BALANCE = 5000;
    private static final double CURRENT_PENALTY = 200;
    private static final double SALARY_MAX_WITHDRAWAL = 25000;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // STEP 1: PIN VERIFICATION
        if (!verifyPin(scanner)) {
            System.out.println("Card Blocked! Please contact the bank.");
            scanner.close();
            return;
        }
        
        // STEP 2: ACCOUNT SELECTION
        int accountType = selectAccountType(scanner);
        if (accountType == -1) {
            System.out.println("Invalid account type.");
            scanner.close();
            return;
        }
        
        // STEP 3: BALANCE & TRANSACTION
        processTransaction(scanner, accountType);
        
        scanner.close();
    }
    
    // PIN verification
    private static boolean verifyPin(Scanner scanner) {
        int attempts = 0;
        
        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter your 4-digit PIN: ");
            
            // Validate input is an integer
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a 4-digit PIN.");
                scanner.next(); // Clear invalid input
                attempts++;
                continue;
            }
            
            int enteredPin = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if (enteredPin == FIXED_PIN) {
                System.out.println("PIN verified successfully!\n");
                return true;
            } else {
                attempts++;
                int remainingAttempts = MAX_ATTEMPTS - attempts;
                
                if (remainingAttempts > 0) {
                    System.out.println("Incorrect PIN. " + remainingAttempts + 
                                     " attempt(s) remaining.\n");
                }
            }
        }
        
        return false;
    }
    
    // Account type
    private static int selectAccountType(Scanner scanner) {
        System.out.println("---SELECT ACCOUNT TYPE---");
        System.out.println("1.Savings Account");
        System.out.println("2.Current Account");
        System.out.println("3.Salary Account");
        System.out.print("Enter your choice (1-3): ");
        
        if (!scanner.hasNextInt()) {
            return -1;
        }
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // newline
        
        if (choice < 1 || choice > 3) {
            return -1;
        }
        
        // Display account type confirmation
        String[] accountNames = {"", "Savings", "Current", "Salary"};
        System.out.println(accountNames[choice] + " Account selected.\n");
        
        return choice;
    }
    
    // process transactions
    private static void processTransaction(Scanner scanner, int accountType) {
        // current balance
        double balance = getBalance(scanner);
        if (balance < 0) {
            System.out.println("Invalid balance entered.");
            return;
        }
        
        // low balance penalty for Current Account
        if (accountType == CURRENT_ACCOUNT && balance < CURRENT_MIN_BALANCE) {
            System.out.println("Penalty of ₹200 charged for low balance.");
            balance -= CURRENT_PENALTY;
            System.out.printf("Updated balance: %.2f\n\n", balance);
        }
        
        // transaction type
        int transactionType = getTransactionType(scanner);
        if (transactionType == -1) {
            System.out.println("Invalid transaction type.");
            return;
        }
        
        // transaction
        if (transactionType == WITHDRAW) {
            processWithdrawal(scanner, accountType, balance);
        } else if (transactionType == DEPOSIT) {
            processDeposit(scanner, accountType, balance);
        }
    }
    
    // current balance
    private static double getBalance(Scanner scanner) {
        System.out.print("Enter your current balance: ");
        
        if (!scanner.hasNextDouble()) {
            return -1;
        }
        
        double balance = scanner.nextDouble();
        scanner.nextLine(); // newline
        
        if (balance < 0) {
            return -1;
        }
        
        return balance;
    }
    
    // transaction type 
    private static int getTransactionType(Scanner scanner) {
        System.out.println("\n---TRANSACTION TYPE---");
        System.out.println("1. Withdraw");
        System.out.println("2. Deposit");
        System.out.print("Enter your choice (1 or 2): ");
        
        if (!scanner.hasNextInt()) {
            return -1;
        }
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // newline
        
        if (choice != WITHDRAW && choice != DEPOSIT) {
            return -1;
        }
        
        return choice;
    }
    
    // withdrawal
    private static void processWithdrawal(Scanner scanner, int accountType, double balance) {
        System.out.print("Enter withdrawal amount: ");
        
        if (!scanner.hasNextDouble()) {
            System.out.println("Invalid amount entered.");
            return;
        }
        
        double amount = scanner.nextDouble();
        scanner.nextLine(); // newline
        
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }
        
        boolean transactionAllowed = true;
        double newBalance = balance - amount;
        
        // Account type
        switch (accountType) {
            case SAVINGS_ACCOUNT:
                if (newBalance < SAVINGS_MIN_BALANCE) {
                    System.out.println("Transaction failed! Withdrawal not allowed.");
                    System.out.println("Minimum balance of " + SAVINGS_MIN_BALANCE + 
                                     " must be maintained.");
                    transactionAllowed = false;
                }
                break;
                
            case CURRENT_ACCOUNT:
                if (newBalance < CURRENT_MIN_BALANCE) {
                    System.out.println("Transaction failed! Withdrawal not allowed.");
                    System.out.println("Minimum balance of " + CURRENT_MIN_BALANCE + 
                                     " must be maintained.");
                    transactionAllowed = false;
                }
                break;
                
            case SALARY_ACCOUNT:
                if (amount > SALARY_MAX_WITHDRAWAL) {
                    System.out.println("Transaction failed! Withdrawal not allowed.");
                    System.out.println("Maximum withdrawal limit is " + 
                                     SALARY_MAX_WITHDRAWAL + " per transaction.");
                    transactionAllowed = false;
                }
                break;
        }
        
        if (transactionAllowed) {
            balance = newBalance;
            System.out.println("Withdrawal successful!");
            System.out.printf("Remaining balance: %.2f\n", balance);
        } else {
            System.out.printf("Current balance: %.2f\n", balance);
        }
    }
    
    // deposit
    private static void processDeposit(Scanner scanner, int accountType, double balance) {
        System.out.print("Enter deposit amount: ");
        
        if (!scanner.hasNextDouble()) {
            System.out.println("Invalid amount entered.");
            return;
        }
        
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }
        
        balance += amount;
        System.out.println("Deposit successful!");
        System.out.printf("Updated balance: %.2f\n", balance);
        
        // Additional message 
        if (accountType == CURRENT_ACCOUNT && balance < CURRENT_MIN_BALANCE) {
            System.out.println("Note: Your balance is still below the minimum required (" + 
                             CURRENT_MIN_BALANCE + ").");
        }
    }
}