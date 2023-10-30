package com.n1nt3nd0.springwebfluxtestingtutorial.controller;

import com.n1nt3nd0.springwebfluxtestingtutorial.dto.EmployeeDTO;
import com.n1nt3nd0.springwebfluxtestingtutorial.entity.Employee;
import com.n1nt3nd0.springwebfluxtestingtutorial.service.EmployeeService;
import com.n1nt3nd0.springwebfluxtestingtutorial.service.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImpl employeeService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EmployeeDTO> saveEmployee(@RequestBody EmployeeDTO employeeDTO){
        return employeeService.saveEmployee(employeeDTO);
    }
    @GetMapping("/getById/{employeeID}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<EmployeeDTO>getEmployeeById(@PathVariable("employeeID") String employeeId){
        return employeeService.getEmployeeById(employeeId);
    }
    @GetMapping("/getAllEmployees")
    @ResponseStatus(HttpStatus.OK)
    public Flux<EmployeeDTO> getAllEmployees(){
        return employeeService.getAllEmployees();
    }
    @PutMapping("/update/{employeeID}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<EmployeeDTO> updateEmployee(@PathVariable("employeeID") String employeeID, @RequestBody EmployeeDTO employeeDTO){
        return employeeService.updateEmployee(employeeDTO, employeeID);
    }
    @DeleteMapping("/delete/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployee(@PathVariable("employeeId")String employeeId){
        return employeeService.deleteEmployee(employeeId);
    }
}
