package com.nam.service;

import com.nam.exception.UserException;
import com.nam.model.ERole;
import com.nam.model.Employee;
import com.nam.model.Role;
import com.nam.model.User;
import com.nam.payload.request.SignupEmployeeRequest;
import com.nam.repository.EmployeeRepository;
import com.nam.repository.RoleRepository;
import com.nam.repository.UserRepository;
import com.nam.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }

        throw new UserException("User not found with id: " + userId);
    }


    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User Not Found with email: " + email));

        return user;
    }

    @Override
    public List<Employee> deleteUser(Long id) throws UserException {
        userRepository.deleteById(id);
        return (List<Employee>) employeeRepository.findAll();
    }

    @Override
    public Employee createEmployee(SignupEmployeeRequest employeeRequest) throws UserException {
        userRepository.findByEmail(employeeRequest.getEmail())
                .ifPresent(user -> {
                    try {
                        throw new UserException("User already exists with email: " + employeeRequest.getEmail());
                    } catch (UserException e) {
                        throw new RuntimeException(e);
                    }
                });
        //Immutable
        Set<Role> roles = Set.of(roleRepository.findByName(ERole.ROLE_EMPLOYEE).get());
        //Mutable
/*        Set<Role> roles = new HashSet<>();
        Optional<Role> role = roleRepository.findByName(ERole.ROLE_STUDENT);
        roles.add(role.get());*/

        Employee employee = Employee.builder()
                .firstName(employeeRequest.getFirstName()).lastName(employeeRequest.getLastName())
                .email(employeeRequest.getEmail()).password(passwordEncoder.encode(employeeRequest.getPassword()))
                .roles(roles)
                .monthSalary(employeeRequest.getMonthSalary())
                .build();

        Employee savedEmployee = userRepository.save(employee);

//        Salary salary = Salary.builder()
//                .employee(savedEmployee)
//                .totalSalary(employeeRequest.getTotalSalary())
//                .month(LocalDateTime.now().getMonthValue())
//                .year(LocalDateTime.now().getYear())
//                .build();

        return savedEmployee;
    }
}