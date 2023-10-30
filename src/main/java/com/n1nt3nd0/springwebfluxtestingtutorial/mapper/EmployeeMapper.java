package com.n1nt3nd0.springwebfluxtestingtutorial.mapper;


import com.n1nt3nd0.springwebfluxtestingtutorial.dto.EmployeeDTO;
import com.n1nt3nd0.springwebfluxtestingtutorial.entity.Employee;

import java.util.UUID;

public class EmployeeMapper {

    public static EmployeeDTO mapToEmployeeDTO(Employee employee){
        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail()
        );
    }

    public static Employee mapToEmployee(EmployeeDTO employeeDTO){
        return new Employee(
                UUID.randomUUID().toString(),
                employeeDTO.getFirstName(),
                employeeDTO.getLastName(),
                employeeDTO.getEmail()
        );
    }
}
