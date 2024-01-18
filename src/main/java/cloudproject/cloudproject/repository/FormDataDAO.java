package cloudproject.cloudproject.repository;

import cloudproject.cloudproject.entity.FormData;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FormDataDAO extends JpaRepository<FormData, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO form_data (first_name, last_name, reason, selected_date, approved) \n" +
            "VALUES (:first_name, :last_name, :reason, :selected_date, false)", nativeQuery = true)
    void saveFormData(@Param("first_name") String firstName,
                             @Param("last_name") String lastName,
                             @Param("reason") String reason,
                             @Param("selected_date") LocalDate selectedDate);

    List<FormData> findByApproved(boolean approved);

    @Modifying
    @Transactional
    @Query(value = "UPDATE FormData SET approved = true WHERE id = :id")
    void approveFormData(@Param("id") Long id);
}

