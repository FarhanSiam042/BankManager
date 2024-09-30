public class BankStatementAnalyzerFactory {


    public static BankStatementAnalyzer createAnalyzer(String fileType) {
        switch (fileType.toLowerCase()) {
            case "csv":
                return new CSVBankStatementAnalyzer();
            case "json":
                return new JSONBankStatementAnalyzer();
            case "xml":
                return new XMLBankStatementAnalyzer();
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
}

