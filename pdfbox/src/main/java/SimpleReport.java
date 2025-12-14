import java.io.File;
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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

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


    // Use PDFBox to read content and assert specific text is present
     String pdfContent = readPdfContent(outputPath);
    System.out.println(pdfContent);
     assert (pdfContent.contains("Expected Data Point"));

    System.out.println("PDF generated at: " + outputPath);
  }

  private static String readPdfContent(String outputPath) {
      try (PDDocument document = PDDocument.load(new File(outputPath))) {
        PDFTextStripper stripper = new PDFTextStripper();
        return stripper.getText(document);
      } catch (IOException e) {
        throw new RuntimeException("Failed to read PDF: " + outputPath, e);
      }
  }
}
