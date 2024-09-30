import java.util.List;

public interface BankStatementAnalyzer {
    List<Transaction> parseFile(String filePath);
    double calculateTotalProfitAndLoss(List<Transaction> transactions);
    int countTransactionsInMonth(List<Transaction> transactions, String month);
    List<Transaction> top10Expenses(List<Transaction> transactions);
    String getHighestSpendingCategory(List<Transaction> transactions);
}
