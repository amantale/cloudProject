package cloudproject.cloudproject.controller;

import cloudproject.cloudproject.repository.FormDataDAO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping(path = "")
public class FormController {

    @Autowired
    private final FormDataDAO formDataDAO;

    public FormController(FormDataDAO formDataDAO) {
        this.formDataDAO = formDataDAO;
    }

    @GetMapping("/")
    public String displayForm(Model model) {
        // Add model attributes for the form fields
        model.addAttribute("firstName", "");  // Set default or retrieve from database
        model.addAttribute("lastName", "");   // Set default or retrieve from database
        model.addAttribute("selectedDate", ""); // Set default or retrieve from database
        model.addAttribute("reason", "");     // Set default or retrieve from database

        return "index";
    }

    @PostMapping(value = "/submit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> saveForm(@RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectedDate,
                                           @RequestParam String reason,
                                           @RequestParam String pdfData) {
        try {
            // Convert the base64-encoded PDF data to a byte array
            byte[] pdfBytes = Base64.getDecoder().decode(pdfData.split(",")[1]);

            // Print some information about the PDF
            System.out.println("Received PDF data. PDF size: " + pdfBytes.length + " bytes");

            // Extract and print PDF content
            printPdfContent(pdfBytes);
            System.out.println("inside try block before the saving");
            formDataDAO.saveFormDataWithPdf(firstName, lastName, pdfBytes, reason, selectedDate);
            System.out.println("inside try block after the saving");
            return ResponseEntity.ok("Form data saved successfully.");
        } catch (Exception e) {
            System.out.println("inside catch block");
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting form data: " + e.getMessage());
        }
    }

    private void printPdfContent(byte[] pdfBytes) throws IOException {
        String decodedPdf = new String(pdfBytes);
        System.out.println("Decoded PDF Content:\n" + decodedPdf);

        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String pdfContent = pdfTextStripper.getText(document);
            System.out.println("PDF Content:\n" + pdfContent);
        }
    }
}
