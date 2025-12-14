import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class SimpleReport {

  public static void main(String[] args) throws Exception {
    List<Person> people = List.of(
        new Person("Alice Johnson", "alice@example.com"),
        new Person("Bob Smith", "bob@example.com"),
        new Person("Carol Lee", "carol@example.com")
    );

    JasperReport jasperReport = JasperCompileManager.compileReport("src\\main\\resources\\simple_report.jrxml");
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
    Map<String, Object> params = new HashMap<>(); // add report parameters here if needed

    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

    String outputPath = "target/simple_report.pdf";
    JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

    String pdfContent = readPdfContent(outputPath);
    System.out.println(pdfContent);
    assert (pdfContent.contains("Expected Data Point"));

    System.out.println("PDF generated at: " + outputPath);
  }

  private static String readPdfContent(String outputPath) {
    try (PdfReader reader = new PdfReader(outputPath)) {
      StringBuilder text = new StringBuilder();
      int pages = reader.getNumberOfPages();
      for( int i=1; i <=pages; i++) {
        text.append(new PdfTextExtractor(reader).getTextFromPage(i));
        if (i < pages) {
          text.append(System.lineSeparator());
        }

      }
       return text.toString();
    } catch (IOException e) {
      throw new RuntimeException("Failed to read PDF: " + outputPath, e);
    }
  }
}
