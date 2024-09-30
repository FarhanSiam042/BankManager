import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONBankStatementAnalyzer implements BankStatementAnalyzer {


    @Override
    public List<Transaction> parseFile(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        try {

            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String date = obj.getString("date");
                double amount = obj.getDouble("amount");
                String description = obj.getString("description");
                // Create a new Transaction object and add it to the list
                transactions.add(new Transaction(date, amount, description));
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
        return (int) transactions.stream()
                .filter(t -> t.getDate().substring(3, 5).equals(month))
                .count();
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

