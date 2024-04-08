package com.plantapp.api.core.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.plantapp.api.core.dto.CreateUserRequest;
import com.plantapp.api.core.entity.User;
import com.plantapp.api.core.enums.Gender;
import com.plantapp.api.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final FirebaseAuth firebaseAuth;
    private final UserRepository userRepository;

    public UserRecord createUser(CreateUserRequest createUserRequest) {
        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                .setEmail(createUserRequest.email())
                .setDisplayName(createUserRequest.username())
                .setPassword(createUserRequest.password());

        UserRecord userRecord = null;
        User user;

        try {
            userRecord = firebaseAuth.createUser(createRequest);
            user = User.builder()
                    .id(userRecord.getUid())
                    .email(userRecord.getEmail())
                    .username(userRecord.getDisplayName())
                    .occupation(createUserRequest.occupation())
                    .gender(Gender.getGender(createUserRequest.gender()))
                    .city(createUserRequest.city())
                    .build();

            userRepository.save(user);
            return userRecord;
        } catch (FirebaseAuthException| RuntimeException e) {
            handleException(e, userRecord);
        }

        return userRecord;
    }

    private void handleException(Exception e, UserRecord userRecord) {
        if (userRecord != null) {
            try {
                firebaseAuth.deleteUser(userRecord.getUid());
            } catch (FirebaseAuthException ex) {
                ex.printStackTrace();
            }
        }
        e.printStackTrace();
        throw new RuntimeException("Unexpected error occurred while creating user", e);
    }

}
