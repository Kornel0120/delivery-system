package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.Payload.Request.EmployeeUserRequest;
import com.example.selectitdelivery.Payload.Response.MessageResponse;
import com.example.selectitdelivery.Payload.Response.UserModifyResponse;
import com.example.selectitdelivery.controller.dto.EmployeeDto;
import com.example.selectitdelivery.controller.dto.EmployeeMapper;
import com.example.selectitdelivery.dao.entity.AppUserEntity;
import com.example.selectitdelivery.dao.entity.RoleEntity;
import com.example.selectitdelivery.dao.model.Employee;
import com.example.selectitdelivery.dao.repositories.AppUserRepository;
import com.example.selectitdelivery.dao.repositories.RoleRepository;
import com.example.selectitdelivery.service.exceptions.EmployeeAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.EmployeeNotFoundException;
import com.example.selectitdelivery.service.implementations.AppUserService;
import com.example.selectitdelivery.service.implementations.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @InjectMocks
    EmployeeController employeeController;
    @Mock
    EmployeeService employeeService;

    @Mock
    AppUserRepository appUserRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    EmployeeMapper employeeMapper;

    @BeforeEach
    public void setup() {
        this.employeeController = new EmployeeController(employeeService, employeeMapper, new AppUserService(appUserRepository), roleRepository);
        this.employeeController.passwordEncoder = passwordEncoder;
    }

    @Test
    void readAllHappyPath() {
        when(employeeService.readAll()).thenReturn(List.of(TestDataProvider.getEmployee()));
        when(employeeMapper.employeeToEmployeeDto(any())).thenReturn(TestDataProvider.getEmployeeDto());
        Collection<EmployeeDto> expected = List.of(TestDataProvider.getEmployeeDto());
        Collection<EmployeeDto> actual = employeeController.getEmployees();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdHappyPath() throws EmployeeNotFoundException {
        when(employeeService.readById(TestDataProvider.getEmployee().getEmpId()))
                .thenReturn(TestDataProvider.getEmployee());
        EmployeeDto expected = TestDataProvider.getEmployeeDto();
        when(employeeMapper.employeeToEmployeeDto(any()))
                .thenReturn(TestDataProvider.getEmployeeDto());

        EmployeeDto actual = employeeController.readById(TestDataProvider.getEmployee().getEmpId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void updateEmployeeHappyPath() throws EmployeeNotFoundException {
        EmployeeDto employeeDto = TestDataProvider.getEmployeeDto();
        Employee employee = TestDataProvider.getEmployee();

        when(employeeMapper.employeeDtoToEmployee(employeeDto)).thenReturn(employee);
        when(employeeService.modify(employee)).thenReturn(employee);

        ResponseEntity<?> expected = TestDataProvider.expectedResponse;
        ResponseEntity<?> actual = employeeController.modify(employeeDto);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createEmployeeHappyPath() throws EmployeeAlreadyExistsException {
        Employee employee = TestDataProvider.getEmployee();
        AppUserEntity newAppUser = new AppUserEntity("EmployeeTest@Test.com", "TestPw", Set.of(new RoleEntity(2,"ROLE_EMPLOYEE"), new RoleEntity(1,"ROLE_ADMIN")));
        EmployeeUserRequest createEmployeeUserRequest = new EmployeeUserRequest(
                employee.getUser().getEmail(),
                employee.getUser().getPassword(),
                Set.of("ROLE_ADMIN","ROLE_EMPLOYEE"),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone()
        );

        Employee newEmployee = new Employee(newAppUser,createEmployeeUserRequest.getFirstName(),createEmployeeUserRequest.getLastName(),createEmployeeUserRequest.getPhone());
        when(passwordEncoder.encode(any())).thenReturn(newAppUser.getPassword());
        when(roleRepository.findByRoleName("ROLE_EMPLOYEE")).thenReturn(Optional.of(new RoleEntity(2,"ROLE_EMPLOYEE")));
        when(roleRepository.findByRoleName("ROLE_ADMIN")).thenReturn(Optional.of(new RoleEntity(1,"ROLE_ADMIN")));
        when(employeeService.record(newEmployee)).thenReturn(newEmployee);

        ResponseEntity<?> expected = ResponseEntity.ok(new MessageResponse("Employee registered successfully!"));
        ResponseEntity<?> actual = employeeController.createEmployee(createEmployeeUserRequest);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void deleteEmployeeHappyPath() throws EmployeeNotFoundException {
        Employee employee = TestDataProvider.getEmployee();
        when(employeeService.readById(TestDataProvider.empId)).thenReturn(employee);
        doNothing().when(employeeService).delete(employee);

        employeeController.delete(TestDataProvider.empId);
    }

    @Test
    void readByIdThrowsEmployeeNotFoundException() throws EmployeeNotFoundException {
        when(employeeService.readById(TestDataProvider.getEmployee().getEmpId()))
                .thenThrow(new EmployeeNotFoundException());

        assertThatThrownBy(() -> {employeeController.readById(TestDataProvider.getEmployee().getEmpId());})
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateThrowsEmployeeNotFoundException() throws EmployeeNotFoundException {
        EmployeeDto employeeDto = TestDataProvider.getEmployeeDto();
        Employee employee = TestDataProvider.getEmployee();

        when(employeeMapper.employeeDtoToEmployee(employeeDto)).thenReturn(employee);
        when(employeeService.modify(employee)).thenThrow(new EmployeeNotFoundException());

        assertThatThrownBy(() -> {employeeController.modify(employeeDto);})
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createEmployeeBadRequestOnTakenEmail() {
        Employee employee = TestDataProvider.getEmployee();
        EmployeeUserRequest createEmployeeUserRequest = new EmployeeUserRequest(
                employee.getUser().getEmail(),
                employee.getUser().getPassword(),
                Set.of("ROLE_ADMIN","ROLE_EMPLOYEE"),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone()
        );

        when(appUserRepository.existsByEmail(any())).thenReturn(true);

        ResponseEntity<?> expected = ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
        ResponseEntity<?> actual = employeeController.createEmployee(createEmployeeUserRequest);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createThrowsResponseStatusException() throws EmployeeAlreadyExistsException {
        Employee employee = TestDataProvider.getEmployee();
        AppUserEntity newAppUser = new AppUserEntity("EmployeeTest@Test.com", "TestPw", Set.of(new RoleEntity(2,"ROLE_EMPLOYEE"), new RoleEntity(1,"ROLE_ADMIN")));
        EmployeeUserRequest createEmployeeUserRequest = new EmployeeUserRequest(
                employee.getUser().getEmail(),
                employee.getUser().getPassword(),
                Set.of("ROLE_ADMIN","ROLE_EMPLOYEE"),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone()
        );

        Employee newEmployee = new Employee(newAppUser,createEmployeeUserRequest.getFirstName(),createEmployeeUserRequest.getLastName(),createEmployeeUserRequest.getPhone());
        when(passwordEncoder.encode(any())).thenReturn(newAppUser.getPassword());
        when(roleRepository.findByRoleName("ROLE_EMPLOYEE")).thenReturn(Optional.of(new RoleEntity(2,"ROLE_EMPLOYEE")));
        when(roleRepository.findByRoleName("ROLE_ADMIN")).thenReturn(Optional.of(new RoleEntity(1,"ROLE_ADMIN")));
        when(employeeService.record(newEmployee)).thenThrow(new EmployeeAlreadyExistsException());

        assertThatThrownBy(() -> {employeeController.createEmployee(createEmployeeUserRequest);})
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createEmployeeWithAdminRoleThrowsRuntimeException () {
        Employee employee = TestDataProvider.getEmployee();
        EmployeeUserRequest createEmployeeUserRequest = new EmployeeUserRequest(
                employee.getUser().getEmail(),
                employee.getUser().getPassword(),
                Set.of("ROLE_ADMIN"),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone()
        );

        assertThatThrownBy(() -> {employeeController.createEmployee(createEmployeeUserRequest);})
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createEmployeeWithEmployeeRoleThrowsRuntimeException () {
        Employee employee = TestDataProvider.getEmployee();
        EmployeeUserRequest createEmployeeUserRequest = new EmployeeUserRequest(
                employee.getUser().getEmail(),
                employee.getUser().getPassword(),
                Set.of("ROLE_EMPLOYEE"),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone()
        );

        assertThatThrownBy(() -> {employeeController.createEmployee(createEmployeeUserRequest);})
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void deleteThrowsResponseStatusException() throws EmployeeNotFoundException {
        final long notFoundEmployeeId = TestDataProvider.empId;

        doThrow(new EmployeeNotFoundException()).when(employeeService).readById(notFoundEmployeeId);

        assertThatThrownBy(() -> {employeeController.delete(notFoundEmployeeId);})
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {
        public static final long empId = 9999;
        public static final AppUserEntity user = new AppUserEntity("EmployeeTest@Test.com","TestPw", Set.of(new RoleEntity(2,"ROLE_EMPLOYEE")));
        public static final String firstName = "TestFirstName";
        public static final String lastName = "TestLastName";
        public static final String phone = "00000000000";

        public static Employee getEmployee() {
            return new Employee(
                    empId,
                    user,
                    firstName,
                    lastName,
                    phone);
        }

        public static EmployeeDto getEmployeeDto() {
            return EmployeeDto.builder()
                    .empId(empId)
                    .user(user)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phone(phone)
                    .build();
        }

        public static ResponseEntity<?> expectedResponse = ResponseEntity.ok(new UserModifyResponse("Employee's account updated successfully.",
                getEmployee().getEmpId(),
                getEmployee().getUser(),
                getEmployee().getFirstName(),
                getEmployee().getLastName(),
                getEmployee().getPhone()));
    }
}
