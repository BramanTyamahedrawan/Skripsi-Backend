package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.model.User;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.UserRepository;
import com.doyatama.university.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class UserService {

    private UserRepository userRepository = new UserRepository();

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public PagedResponse<User> getAllUser(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<User> userResponse = userRepository.findAll(size);


        return new PagedResponse<>(userResponse, userResponse.size(), "Successfully get data", 200);
    }

     public PagedResponse<User> getUserById(String userId) throws IOException {
        // Retrieve User
        User userResponse = userRepository.findById(userId);

        List<User> users = userResponse != null ? Collections.singletonList(userResponse) : Collections.emptyList();

        long totalElements = users.size();
        String message = userResponse != null ? "Successfully get data" : "User not found";
        long statusCode = userResponse != null ? 200 : 404;

        return new PagedResponse<>(users, totalElements, message, statusCode);
    }

    public PagedResponse<User> getUserNotUsedAccount(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<User> userResponse = userRepository.findUsersNotUsedInLectures(size);

        return new PagedResponse<>(userResponse, userResponse.size(), "Successfully get data", 200);
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
