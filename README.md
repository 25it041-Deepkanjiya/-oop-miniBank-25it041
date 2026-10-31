# MiniBank - Java Object-Oriented Programming Project

A console-driven banking management system created for the Object-Oriented Programming with Java (OOPJ) coursework. The application demonstrates fundamental and intermediate Java concepts—ranging from OOP principles, polymorphism, and custom annotations to concurrency, reflection, generics, and file I/O across 14 lab practicals.

---

## Student Information
- **Name:** Deep Kanjiya
- **ID / Roll No:** 25IT041
- **Course:** B.Tech Information Technology
- **Subject:** Object-Oriented Programming with Java (OOPJ)

---

## Quick Start & Execution

### 1. Using PowerShell / Terminal:
From the project root directory:

```powershell
# Create output directory and compile all Java source files
javac -d out (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName })

# Run the MiniBank console application
java -cp out minibank.MiniBank
```

### 2. Standard Command Line (Linux / macOS / Git Bash):
```bash
# Find and compile all source files
find src -name "*.java" > sources.txt
javac -d out @sources.txt

# Execute application
java -cp out minibank.MiniBank
```

---

## Project Overview

The **MiniBank** project implements essential everyday banking operations including opening new accounts, querying account balances, crediting deposits, debiting withdrawals with balance rules, and executing inter-account transfers.

Key technical milestones implemented through the laboratory exercises:
- **Inheritance Hierarchy:** Abstract `Account` base class with `SavingsAccount`, `CurrentAccount` (with Rs. 5000 overdraft), and `FixedDepositAccount` (locked withdrawals).
- **Custom Exception Handling:** Application-specific exception hierarchy (`BankException`, `InsufficientFundsException`, `InvalidAmountException`, `AccountNotFoundException`).
- **Custom Annotations & Reflection:** `@Id`, `@Positive`, and `@MaxLength` runtime validation engine (`AnnotationValidator`).
- **Generic Repository Pattern:** Type-safe in-memory `Repository<T>` utilizing reflection for dynamic `@Id` resolution.
- **Thread Safety & Concurrency:** Synchronized methods, thread-safe collections (`ConcurrentHashMap`, synchronized lists), ordered dual-locking to prevent transfer deadlocks, and multi-threaded simulations using `ExecutorService`.
- **State Persistence & Audit Logging:** Java Object Serialization (`StatePersister`) saving state to `data/accounts.dat` and Java NIO file logging (`TransactionLog`) saving audit trails to `data/transactions.txt`.

---

## Directory Structure

```text
miniBank-25it041/
├── src/
│   └── minibank/
│       ├── MiniBank.java                     # Main console driver with interactive menu
│       ├── annotation/
│       │   ├── Id.java                       # Marks primary key identifier
│       │   ├── MaxLength.java                # Maximum length constraint for strings
│       │   └── Positive.java                 # Non-negative numerical check
│       ├── exception/
│       │   ├── BankException.java            # Root checked bank exception
│       │   ├── AccountNotFoundException.java # Thrown when account query fails
│       │   ├── InsufficientFundsException.java# Thrown on balance/overdraft violation
│       │   └── InvalidAmountException.java   # Thrown on non-positive input amounts
│       ├── model/
│       │   ├── Account.java                  # Abstract base account class
│       │   ├── SavingsAccount.java           # Savings account implementation
│       │   ├── CurrentAccount.java           # Current account with overdraft limit
│       │   ├── FixedDepositAccount.java      # Fixed deposit account
│       │   ├── Customer.java                 # Customer profile holder class
│       │   └── Transaction.java              # Immutable transaction timestamp record
│       ├── repository/
│       │   └── Repository.java               # Generic in-memory storage engine
│       ├── service/
│       │   ├── AccountWorker.java            # Runnable worker for concurrency demo
│       │   └── BankService.java              # Central business logic and transfers
│       └── util/
│           ├── AnnotationValidator.java      # Reflection-based annotation processor
│           ├── StatePersister.java           # Object serialization for state persistence
│           └── TransactionLog.java           # Java NIO text file audit appender
├── .gitignore                                # Git ignore file for compiled classes & data
└── README.md                                 # Project documentation
```

---

## Lab Practicals Covered

| Practical | Topic | Implementation Details |
|---|---|---|
| **PR1** | Basic Setup & Control Flow | Interactive command-line menu interface powered by `Scanner` and `switch` statements |
| **PR2** | Class Inheritance | `Account` abstract superclass extended by `SavingsAccount` and `CurrentAccount` |
| **PR3** | Interfaces & Polymorphism | Common abstract methods (`canWithdraw`, `getAccountType`) and `Comparable<Account>` implementation |
| **PR4** | Validation & Models | Encapsulated entity classes (`Customer`, `Account`) with state checking |
| **PR5** | Method Overriding & Rules | Specialized withdrawal algorithms for savings, current (overdraft), and fixed deposits |
| **PR6** | Custom Exception Handling | Dedicated checked exception classes for domain failures (`BankException` hierarchy) |
| **PR7** | Reflection & Annotations | Runtime inspection of `@Id`, `@MaxLength`, and `@Positive` constraints |
| **PR8** | Object Duplication & State | Object representation, serialization preparation, and defensive state copying |
| **PR9** | Multithreading Basics | Thread-level synchronization (`synchronized` keywords) on account balance mutations |
| **PR10** | Thread Pools & Synchronization | Concurrency simulation using `ExecutorService` thread pool and deadlock-free dual locks |
| **PR11** | File Handling & Java NIO | Object serialization (`ObjectOutputStream`) and appending logs via `java.nio.file.Files` |
| **PR12** | Java Collections Framework | Dynamic account listing, sorted views using `Comparable<Account>` / streams, synchronized lists |
| **PR13** | Generic Repository | Reusable `Repository<T>` with generic type parameters and reflective ID lookup |
| **PR14** | BankService Capstone | Integrated service layer linking repository, models, thread workers, and data persistence |

---

## Features & Menu Walkthrough

1. **Open New Bank Account:** Register Customer name, phone, address, account number, choose between Savings, Current, or Fixed Deposit, and enter opening balance. Runs automatic annotation validation.
2. **View Account Details:** Query any account by account number to view current balance and linked customer details.
3. **Deposit Money:** Add funds to any existing active account with input validation.
4. **Withdraw Money:** Withdraw funds according to account rules (Savings cannot exceed balance; Current allows up to Rs. 5000 overdraft; Fixed Deposit disallows withdrawals).
5. **Transfer Funds:** Secure, deadlock-free balance transfers between two different accounts.
6. **List All Accounts:** Displays all registered accounts sorted by account balance.
7. **Transaction Statement:** View full audit trail with timestamps.
8. **Thread-Safety Demo:** Spawns a pool of 3 worker threads executing 5 concurrent deposits to verify thread safety and race-condition prevention.
9. **Save & Exit:** Serializes account objects to `data/accounts.dat` and flushes transaction records to `data/transactions.txt`.
