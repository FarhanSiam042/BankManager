import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class XMLBankStatementAnalyzer implements BankStatementAnalyzer {


    @Override
    public List<Transaction> parseFile(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(Files.newInputStream(Paths.get(filePath)));

            NodeList nodeList = doc.getElementsByTagName("transaction");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String date = element.getElementsByTagName("date").item(0).getTextContent();
                    double amount = Double.parseDouble(element.getElementsByTagName("amount").item(0).getTextContent());
                    String description = element.getElementsByTagName("description").item(0).getTextContent();

                    transactions.add(new Transaction(date, amount, description));
                }
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
