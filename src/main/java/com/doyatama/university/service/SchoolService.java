package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.School;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.SchoolRequest;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.util.AppConstants;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolService {
    private SchoolRepository schoolRepository = new SchoolRepository();
    
    private static final Logger logger = LoggerFactory.getLogger(SchoolService.class);
    
    public PagedResponse<School> getAllSchool(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<School> schoolResponse = new ArrayList<>();

        
            schoolResponse = schoolRepository.findAll(size);
        

        return new PagedResponse<>(schoolResponse, schoolResponse.size(), "Successfully get data", 200);
    }
    
    public School createSchool(SchoolRequest schoolRequest) throws IOException {
        School school = new School();
            school.setId(schoolRequest.getId());
            school.setName(schoolRequest.getName());
            school.setAddress(schoolRequest.getAddress());
            return schoolRepository.save(school);
    }
    
    public DefaultResponse<School> getSchoolById(String schoolId) throws IOException {
        // Retrieve School
        School schoolResponse = schoolRepository.findById(schoolId);
        return new DefaultResponse<>(schoolResponse.isValid() ? schoolResponse : null, schoolResponse.isValid() ? 1 : 0, "Successfully get data");
    }
    
    public School updateSchool(String schoolId, SchoolRequest schoolRequest) throws IOException {
        School school = new School();
            school.setName(schoolRequest.getName());
            school.setAddress(schoolRequest.getAddress());
            return schoolRepository.update(schoolId, school);
    }
    
    public void deleteSchoolById(String schoolId) throws IOException {
        School schoolResponse = schoolRepository.findById(schoolId);
        if(schoolResponse.isValid()){
            schoolRepository.deleteById(schoolId);
        }else{
            throw new ResourceNotFoundException("School", "id", schoolId);
        }
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
