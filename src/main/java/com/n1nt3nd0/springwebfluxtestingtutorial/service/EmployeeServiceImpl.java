package com.n1nt3nd0.springwebfluxtestingtutorial.service;

import com.n1nt3nd0.springwebfluxtestingtutorial.dto.EmployeeDTO;
import com.n1nt3nd0.springwebfluxtestingtutorial.entity.Employee;
import com.n1nt3nd0.springwebfluxtestingtutorial.mapper.EmployeeMapper;
import com.n1nt3nd0.springwebfluxtestingtutorial.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Override
    public Mono<EmployeeDTO> saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDTO);
        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        logger.info("Employee saved.");
        return savedEmployee.map(empl -> EmployeeMapper.mapToEmployeeDTO(empl));
    }

    @Override
    public Mono<EmployeeDTO> getEmployeeById(String employeeId) {
        Mono<Employee> savedEmployee = employeeRepository.findById(employeeId);
        return savedEmployee.map(employee -> EmployeeMapper.mapToEmployeeDTO(employee));
    }

    @Override
    public Flux<EmployeeDTO> getAllEmployees() {
        Flux<Employee> allEmployees = employeeRepository.findAll();
        return allEmployees.map(employee -> EmployeeMapper.mapToEmployeeDTO(employee))
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDTO> updateEmployee(EmployeeDTO employeeDTO, String employeeId) {
        Mono<Employee> savedEmployee = employeeRepository.findById(employeeId);
        Mono<Employee> updateEmployee = savedEmployee.flatMap(((employee) -> {
            employee.setId(employeeId);
            employee.setFirstName(employeeDTO.getFirstName());
            employee.setLastName(employeeDTO.getLastName());
            employee.setEmail(employeeDTO.getEmail());
            return employeeRepository.save(employee);
        }));
        return updateEmployee.map(employee -> EmployeeMapper.mapToEmployeeDTO(employee));
    }

    @Override
    public Mono<Void> deleteEmployee(String employeeId) {
        return employeeRepository.deleteById(employeeId);
    }
}
