//package com.nam.service;
//
//import com.nam.exception.UserException;
//import com.nam.model.ERole;
//import com.nam.model.Employee;
//import com.nam.model.Role;
//import com.nam.model.User;
//import com.nam.payload.request.SignupEmployeeRequest;
//import com.nam.repository.RoleRepository;
//import com.nam.repository.UserRepository;
//import com.nam.security.jwt.JwtProvider;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.willDoNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    JwtProvider jwtProvider;
//
//    @Mock
//    RoleRepository roleRepository;
//
//    @Mock
//    PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    UserServiceImpl userService;
//
//    User user;
//    Set<Role> roles = new HashSet<>();
//    String JWT = "eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MDIyNjAyNTEsImV4cCI6MTcwMzEwNjI1MSwiZW1haWwiOiIxMjNAZ21haWwuY29tIn0.sb46h-G_UiX257hF98DIiJRFcy2MMSFeHPskHR8RJgnwl3-PzJJUI_bDQ6PJHCqw";
//
//    @BeforeEach
//    void setUp() {
//        roles.add(new Role(1L, ERole.ROLE_ADMIN));
//
//        user = User.builder()
//                .id(1L)
//                .firstName("Nguyen Hoang").lastName("Nam")
//                .password("123")
//                .email("1@gmail.com")
//                .roles(roles)
//                .createdAt(LocalDateTime.now())
//                .build();
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    // JUnit test for findUserById method
//    @DisplayName("JUnit test for findUserById method")
//    @Test
//    void givenUserObject_whenFindUserById_thenReturnUserObject() throws UserException {
//        // given - precondition or setup
//        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
//
//        // when -  action or the behaviour that we are going test
//        User findUser = userService.findUserById(user.getId());
//        System.out.println(findUser);
//
//        // then - verify the output
//        assertThat(findUser).isEqualTo(user);
//        verify(userRepository, times(1)).findById(user.getId());
//    }
//
//    @DisplayName("JUnit test for findUserById method which throws exception")
//    @Test
//    void givenUserObject_whenFindUserById_thenThrowUserException() throws UserException {
//        // given - precondition or setup
//        Long userId = 1L;
//        given(userRepository.findById(anyLong())).willReturn(Optional.empty());
//
//        // when -  action or the behaviour that we are going to test
//        UserException exception = assertThrows(UserException.class, () -> userService.findUserById(userId));
//
//        // then - verify the output
//        verify(userRepository, times(1)).findById(userId);
//        String expectedMessage = "User not found with id: " + userId;
//        String actualMessage = exception.getMessage();
//        assertThat(actualMessage).isEqualTo(expectedMessage);
//    }
//
//    @DisplayName("JUnit test for findUserProfileByJwt method")
//    @Test
//    void givenJWT_whenFindUserProfileByJwt_thenReturnUserObject() throws UserException {
//        // given - precondition or setup
//        given(jwtProvider.getEmailFromToken(JWT)).willReturn(user.getEmail());
//        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
//
//        // when -  action or the behaviour that we are going test
//        User findUser = userService.findUserProfileByJwt(JWT);
//
//        // then - verify the output
//        assertThat(findUser).isEqualTo(user);
//        verify(jwtProvider, times(1)).getEmailFromToken(JWT);
//        verify(userRepository, times(1)).findByEmail(user.getEmail());
//    }
//
//    @DisplayName("JUnit test for findUserProfileByJwt method which throws exception")
//    @Test
//    void givenJWT_whenFindUserProfileByJwt_thenThrowException() throws UserException {
//        // given - precondition or setup
//        given(jwtProvider.getEmailFromToken(JWT)).willReturn(user.getEmail());
//        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
//
//        // when -  action or the behaviour that we are going to test
//        UserException exception = assertThrows(UserException.class, () -> userService.findUserProfileByJwt(JWT));
//
//        // then - verify the output
//        verify(jwtProvider, times(1)).getEmailFromToken(JWT);
//        verify(userRepository, times(1)).findByEmail(user.getEmail());
//
//        String expectedMessage = "User Not Found with email: " + user.getEmail();
//        String actualMessage = exception.getMessage();
//        assertThat(actualMessage).isEqualTo(expectedMessage);
//    }
//
//    @DisplayName("JUnit test for deleteUser method")
//    @Test
//    void givenUserId_whenDeleteUser_thenNothing() throws UserException {
//        // given - precondition or setup
//        Long userId = 1L;
//
//        willDoNothing().given(userRepository).deleteById(userId);
//
//        // when -  action or the behaviour that we are going test
//        userService.deleteUser(userId);
//
//        // then - verify the output
//        verify(userRepository, times(1)).deleteById(userId);
//    }
//
//    @DisplayName("JUnit test for createStudent method")
//    @Test
//    void givenStudentRequest_whenCreateStudent_thenShouldSaveStudentObject() throws UserException {
//        // given - precondition or setup
//        SignupEmployeeRequest studentRequest = SignupEmployeeRequest.builder()
//                .firstName("John").lastName("Cena")
//                .email("1@gmail.com")
//                .build();
//        Role role = Role.builder()
//                .id(1L)
//                .name(ERole.ROLE_STUDENT)
//                .build();
//
//        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
//        given(roleRepository.findByName(ERole.ROLE_STUDENT)).willReturn(Optional.of(role));
////        given(userRepository.save(studentRequest)).willReturn(studentRequest);
//
//        // when -  action or the behaviour that we are going test
//        Employee createdEmployee = userService.createStudent(studentRequest);
//
//        // Then
//        assertThat(createdEmployee.getEmail()).isEqualTo(studentRequest.getEmail());
//        assertThat(createdEmployee.getFirstName()).isEqualTo(studentRequest.getFirstName());
//        assertThat(createdEmployee.getLastName()).isEqualTo(studentRequest.getLastName());
//        assertThat(createdEmployee.getStudentId()).isEqualTo(studentRequest.getStudentId());
//        assertThat(createdEmployee.getStudentClass()).isEqualTo(studentRequest.getStudentClass());
//        assertThat(createdEmployee.getRoles()).hasSize(1);
//        assertThat(createdEmployee.getId()).isNotNull();
//
//        verify(userRepository).findByEmail(studentRequest.getEmail());
//        verify(roleRepository).findByName(ERole.ROLE_STUDENT);
//        verify(userRepository, times(1)).save(any(Employee.class));
//    }
//}