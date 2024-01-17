package cloudproject.cloudproject.repository;

import cloudproject.cloudproject.entity.FormData;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FormDataDAO extends JpaRepository<FormData, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO form_data (first_name, last_name, pdf_bytes, reason, selected_date) \n" +
            "VALUES (:first_name, :last_name, :pdf_bytes, :reason, :selected_date)", nativeQuery = true)
    void saveFormDataWithPdf(@Param("first_name") String firstName,
                             @Param("last_name") String lastName,
                             @Param("pdf_bytes") byte[] pdfBytes,
                             @Param("reason") String reason,
                             @Param("selected_date") LocalDate selectedDate);
}
