import java.util.List;

public class BankStatementAnalyzerApp {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Please provide the file type and file path as arguments.");
            System.out.println("Usage: java BankStatementAnalyzerApp <fileType> <filePath>");
            return;
        }

        String fileType = args[0];
        String filePath = args[1];


        BankStatementAnalyzer analyzer = BankStatementAnalyzerFactory.createAnalyzer(fileType);


        List<Transaction> transactions = analyzer.parseFile(filePath);


        double totalProfitLoss = analyzer.calculateTotalProfitAndLoss(transactions);
        System.out.println("Total Profit and Loss: " + totalProfitLoss);


        int januaryTransactions = analyzer.countTransactionsInMonth(transactions, "01");
        System.out.println("Transactions in January: " + januaryTransactions);


        List<Transaction> top10Expenses = analyzer.top10Expenses(transactions);
        System.out.println("Top 10 Expenses: " + top10Expenses);


        String highestSpendingCategory = analyzer.getHighestSpendingCategory(transactions);
        System.out.println("Category with Highest Spending: " + highestSpendingCategory);
    }
}
