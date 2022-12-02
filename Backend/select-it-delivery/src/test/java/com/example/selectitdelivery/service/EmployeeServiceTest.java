package com.example.selectitdelivery.service;

import com.example.selectitdelivery.dao.entity.AppUserEntity;
import com.example.selectitdelivery.dao.entity.EmployeeEntity;
import com.example.selectitdelivery.dao.entity.RoleEntity;
import com.example.selectitdelivery.dao.model.Employee;
import com.example.selectitdelivery.dao.repositories.EmployeeRepository;
import com.example.selectitdelivery.service.exceptions.EmployeeAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.EmployeeNotFoundException;
import com.example.selectitdelivery.service.implementations.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void recordEmployeeHappyPath() throws EmployeeAlreadyExistsException {
        Employee employee = TestDataProvider.getEmployee();
        EmployeeEntity employeeEntity = TestDataProvider.getEmployeeEntity();
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());
        when(employeeRepository.save(any())).thenReturn(employeeEntity);
        Employee actual = employeeService.record(employee);
        assertThat(actual).usingRecursiveComparison().isEqualTo(employee);
    }

    @Test
    void readByIdHappyPath() throws EmployeeNotFoundException {
        when(employeeRepository.findById(TestDataProvider.empId))
                .thenReturn(Optional.of(TestDataProvider.getEmployeeEntity()));
        Employee expected = TestDataProvider.getEmployee();
        Employee actual = employeeService.readById(TestDataProvider.empId);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void modifyEmployeeHappyPath() throws EmployeeNotFoundException {
        Employee employee = TestDataProvider.getEmployee();
        EmployeeEntity employeeEntity = TestDataProvider.getEmployeeEntity();
        when(employeeRepository.findById(employee.getEmpId()))
                .thenReturn(Optional.of(employeeEntity));
        when(employeeRepository.save(any())).thenReturn(employeeEntity);
        Employee actual = employeeService.modify(employee);
        assertThat(actual).usingRecursiveComparison().isEqualTo(employee);
    }

    @Test
    void readAllHappyPath() {
        List<EmployeeEntity> employeeEntities = List.of(
                TestDataProvider.getEmployeeEntity()
        );

        Collection<Employee> expectedEmployees = List.of(
                TestDataProvider.getEmployee()
        );

        when(employeeRepository.findAll()).thenReturn(employeeEntities);
        Collection<Employee> actualEmployees = employeeService.readAll();
        assertThat(actualEmployees).usingRecursiveComparison().isEqualTo(expectedEmployees);
    }

    @Test
    void deleteEmployeeHappyPath() throws EmployeeNotFoundException {
        Employee employee = TestDataProvider.getEmployee();
        EmployeeEntity employeeEntity = TestDataProvider.getEmployeeEntity();
        when(employeeRepository.findById(employee.getEmpId()))
                .thenReturn(Optional.of(employeeEntity));
        employeeService.delete(employee);
    }

    @Test
    void recordThrowsEmployeeAlreadyExistsException() {
        Employee employee = TestDataProvider.getEmployee();
        EmployeeEntity employeeEntity = TestDataProvider.getEmployeeEntity();
        when(employeeRepository.findById(TestDataProvider.empId))
                .thenReturn(Optional.ofNullable(employeeEntity));
        assertThatThrownBy(() -> employeeService.record(employee))
                .isInstanceOf(EmployeeAlreadyExistsException.class);
    }

    @Test
    void readByIdThrowsEmployeeNotFoundException() {
        when(employeeRepository.findById(TestDataProvider.empId))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> employeeService.readById(TestDataProvider.empId))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void modifyThrowsEmployeeNotFoundException() {
        Employee employee = TestDataProvider.getEmployee();
        when(employeeRepository.findById(employee.getEmpId()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> employeeService.modify(employee))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void deleteThrowsEmployeeNotFoundException() {
        Employee employee = TestDataProvider.getEmployee();
        assertThatThrownBy(() -> employeeService.delete(employee))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    private static class TestDataProvider {
        public static final long empId = 9999;
        public static final AppUserEntity user = new AppUserEntity(9999,"EmployeeTest@Test.com","TestPw",Set.of(new RoleEntity(9998,"TEST_ROLE")));
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

        public static EmployeeEntity getEmployeeEntity() {
            return EmployeeEntity.builder()
                    .empId(empId)
                    .user(user)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phone(phone)
                    .build();
        }
    }
}
