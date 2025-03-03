package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Semester;
import com.doyatama.university.payload.SemesterRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.SemesterRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SemesterService {
    private SemesterRepository semesterRepository = new SemesterRepository();

    // private static final Logger logger =
    // LoggerFactory.getLogger(SemesterService.class);

    public PagedResponse<Semester> getAllSemester(int page, int size, String userID) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<Semester> semesterResponse = new ArrayList<>();

        if (userID.equalsIgnoreCase("*")) {
            semesterResponse = semesterRepository.findAll(size);
        } else {
            semesterResponse = semesterRepository.findSemesterByUser(userID, size);
        }

        return new PagedResponse<>(semesterResponse, semesterResponse.size(), "Successfully get data", 200);
    }

    public Semester createSemester(SemesterRequest semesterRequest) throws IOException {

        if (semesterRequest.getIdSemester() == null) {
            semesterRequest.setIdSemester(UUID.randomUUID().toString());
        }

        if (semesterRepository.existById(semesterRequest.getIdSemester())) {
            throw new IllegalArgumentException("Semester already exist");
        }

        Semester semester = new Semester();
        semester.setIdSemester(semesterRequest.getIdSemester());
        semester.setNamaSemester(semesterRequest.getNamaSemester());
        return semesterRepository.save(semester);
    }

    public DefaultResponse<Semester> getSemesterById(String semesterId) throws IOException {
        // Retrieve Semester
        Semester semester = semesterRepository.findById(semesterId);
        return new DefaultResponse<>(semester.isValid() ? semester : null, semester.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public Semester updateSemester(String semesterId, SemesterRequest semesterRequest) throws IOException {
        Semester semester = new Semester();
        semester.setNamaSemester(semesterRequest.getNamaSemester());
        return semesterRepository.update(semesterId, semester);
    }

    public void deleteSemesterById(String semesterId) throws IOException {
        Semester semesterResponse = semesterRepository.findById(semesterId);
        if (semesterResponse.isValid()) {
            semesterRepository.deleteById(semesterId);
        } else {
            throw new ResourceNotFoundException("Semester", "id", semesterId);
        }
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
