package com.skripsi.area31.repository;

import com.skripsi.area31.model.course.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface ComplaintRepository extends MongoRepository<Complaint, String> {
    Complaint findComplaintByIdComplaint(String idComplaint);

    List<Complaint> findAllByIdUser(String idUser);

    List<Complaint> findAllByIdUserAndIdCourse(String idStudent, String idCourse);

    List<Complaint> findTop5ByIdInstructor(String idInstructor);

    Page<Complaint> findAllByIdInstructor(String idComplaint, Pageable pageable);
}
