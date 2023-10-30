package com.n1nt3nd0.springwebfluxtestingtutorial.service;

import com.n1nt3nd0.springwebfluxtestingtutorial.dto.EmployeeDTO;
import com.n1nt3nd0.springwebfluxtestingtutorial.entity.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Mono<EmployeeDTO>saveEmployee(EmployeeDTO employeeDTO);
    Mono<EmployeeDTO> getEmployeeById(String employeeId);
    Flux<EmployeeDTO> getAllEmployees();
    Mono<EmployeeDTO> updateEmployee(EmployeeDTO employeeDTO, String emplyeeId);
    Mono<Void> deleteEmployee(String employeeId);
}
