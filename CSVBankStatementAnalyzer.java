import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class CSVBankStatementAnalyzer implements BankStatementAnalyzer {

    @Override
    public List<Transaction> parseFile(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] data = line.split(",");
                Transaction transaction = new Transaction(data[0], Double.parseDouble(data[1]), data[2]);
                transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public double calculateTotalProfitAndLoss(List<Transaction> transactions) {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    @Override
    public int countTransactionsInMonth(List<Transaction> transactions, String month) {
        return (int) transactions.stream().filter(t -> t.getDate().substring(3, 5).equals(month)).count();
    }

    @Override
    public List<Transaction> top10Expenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getAmount))
                .limit(10)
                .toList();
    }

    @Override
    public String getHighestSpendingCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(Transaction::getDescription, Collectors.summingDouble(Transaction::getAmount)))
                .entrySet().stream().min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No spending category");
    }
}

