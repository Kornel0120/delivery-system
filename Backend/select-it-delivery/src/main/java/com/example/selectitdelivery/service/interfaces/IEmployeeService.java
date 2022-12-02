package com.example.selectitdelivery.service.interfaces;

import com.example.selectitdelivery.dao.model.Employee;
import com.example.selectitdelivery.service.exceptions.EmployeeAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.EmployeeNotFoundException;

import java.util.Collection;

public interface IEmployeeService {
    Employee record(Employee employee) throws EmployeeAlreadyExistsException;
    Employee readById(long id) throws EmployeeNotFoundException;
    Collection<Employee> readAll();
    Employee modify(Employee employee) throws EmployeeNotFoundException;
    void delete(Employee employee) throws EmployeeNotFoundException;
}
