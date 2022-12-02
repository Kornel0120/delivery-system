package com.example.selectitdelivery.service.implementations;

import com.example.selectitdelivery.dao.entity.EmployeeEntity;
import com.example.selectitdelivery.dao.model.Employee;
import com.example.selectitdelivery.dao.repositories.EmployeeRepository;
import com.example.selectitdelivery.service.exceptions.EmployeeAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.EmployeeNotFoundException;
import com.example.selectitdelivery.service.interfaces.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmployeeService implements IEmployeeService {
    private final EmployeeRepository employeeRepository;

    private static Employee convertEntityToModel(EmployeeEntity employee) {
        return new Employee(
                employee.getEmpId(),
                employee.getUser(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone()
        );
    }

    private static EmployeeEntity convertModelToEntity(Employee employee) {
        return EmployeeEntity.builder()
                .empId(employee.getEmpId())
                .user(employee.getUser())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .phone(employee.getPhone())
                .build();
    }

    @Override
    public Employee record(Employee employee) throws EmployeeAlreadyExistsException {
        if(employeeRepository.findById(employee.getEmpId()).isPresent()) {
            throw new EmployeeAlreadyExistsException();
        }

        EmployeeEntity recordEmployeeEntity = employeeRepository.save(convertModelToEntity(employee));

        return convertEntityToModel(recordEmployeeEntity);
    }

    @Override
    public Employee readById(long id) throws EmployeeNotFoundException {
        if(employeeRepository.findById(id).isEmpty()) {
            throw new EmployeeNotFoundException();
        }

        return convertEntityToModel(employeeRepository.findById(id).get());
    }

    @Override
    public Collection<Employee> readAll() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeService::convertEntityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public Employee modify(Employee employee) throws EmployeeNotFoundException {
        EmployeeEntity employeeEntity = convertModelToEntity(employee);
        if(employeeRepository.findById(employeeEntity.getEmpId()).isEmpty()) {
            throw new EmployeeNotFoundException();
        }

        return convertEntityToModel(employeeRepository.save(employeeEntity));
    }

    @Override
    public void delete(Employee employee) throws EmployeeNotFoundException {
        if(employeeRepository.findById(employee.getEmpId()).isEmpty()) {
            throw new EmployeeNotFoundException();
        }

        employeeRepository.delete(convertModelToEntity(employee));
    }
}
