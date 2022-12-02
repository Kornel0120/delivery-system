package com.example.selectitdelivery.controller.dto;

import com.example.selectitdelivery.dao.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface EmployeeMapper {
    EmployeeDto employeeToEmployeeDto(Employee employee);

    Employee employeeDtoToEmployee(EmployeeDto employeeDto);
}
