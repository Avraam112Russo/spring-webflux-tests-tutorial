package com.n1nt3nd0.springwebfluxtestingtutorial.integration;

import com.n1nt3nd0.springwebfluxtestingtutorial.dto.EmployeeDTO;
import com.n1nt3nd0.springwebfluxtestingtutorial.entity.Employee;
import com.n1nt3nd0.springwebfluxtestingtutorial.repository.EmployeeRepository;
import com.n1nt3nd0.springwebfluxtestingtutorial.service.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class EmployeeControllerIntegrationTests {
    @Autowired
    private EmployeeServiceImpl employeeService;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository employeeRepository;


    @BeforeEach
    public void before(){
        log.info("Before method is work");
        employeeRepository.deleteAll().subscribe();
    }


    @DisplayName("Integration test for save employee operation")
    @Test
    public void saveEmployeeTest(){
       EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .firstName("Russo")
                .lastName("Zaripov")
                .email("russo@mail.ru")
                .build();

       WebTestClient.ResponseSpec responseSpec = webTestClient.post()
               .uri("/api/v1/test/save")
               .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON)
               .body(Mono.just(employeeDTO), EmployeeDTO.class)
               .exchange();
        responseSpec.expectStatus().isCreated()
                .expectBody()
                .consumeWith(response -> System.out.println(response))
                .jsonPath("$.firstName").isEqualTo(employeeDTO.getFirstName())
                .jsonPath("$.email").isEqualTo(employeeDTO.getEmail());
    }
    @DisplayName("Integration test for get employee by id operation")
    @Test
    public void getEmployeeByIdTest(){
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .firstName("Russo")
                .lastName("Zaripov")
                .email("russo@mail.ru")
                .build();
        EmployeeDTO employee = employeeService.saveEmployee(employeeDTO).block();
        webTestClient.get().uri("/api/v1/test/getById/{employeeID}", Collections.singletonMap("employeeID", employee.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> System.out.println(response))
                .jsonPath("$.firstName").isEqualTo("Russo")
                .jsonPath("$.lastName").isEqualTo("Zaripov");
    }
    @DisplayName("Integration test for get all employees operation")
    @Test
    public void getAllEmployeesTest(){
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .firstName("Russo")
                .lastName("Zaripov")
                .email("russo@mail.ru")
                .build();
        EmployeeDTO employeeDTO_2 = EmployeeDTO.builder()
                .firstName("Елена")
                .lastName("Соколова")
                .email("russo@mail.ru")
                .build();
        List<EmployeeDTO> collect = List.of(employeeDTO, employeeDTO_2).stream().map(emp -> employeeService.saveEmployee(emp).block()).collect(Collectors.toList());
        webTestClient.get()
                .uri("/api/v1/test/getAllEmployees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDTO.class)
                .consumeWith(response -> System.out.println(response))
                .hasSize(collect.size());
    }

    @DisplayName("Integration test for update employee operation")
    @Test
    public void updateEmployeeTest(){
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .firstName("Russo")
                .lastName("Zaripov")
                .email("russo@mail.ru")
                .build();
        EmployeeDTO savedEmployee = employeeService.saveEmployee(employeeDTO).block();

        EmployeeDTO updateEmployeeDTO = EmployeeDTO.builder()
                .firstName("update")
                .lastName("update")
                .email("update@mail.ru")
                .build();
        webTestClient.put().uri("/api/v1/test/update/{employeeID}", Collections.singletonMap("employeeID", savedEmployee.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateEmployeeDTO), EmployeeDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> System.out.println(response))
                .jsonPath("$.firstName").isEqualTo(updateEmployeeDTO.getFirstName())
                .jsonPath("$.lastName").isEqualTo(updateEmployeeDTO.getLastName());
    }
    @DisplayName("Integration test for delete employee operation")
    @Test
    public void deleteEmployeeTest(){
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .firstName("Russo")
                .lastName("Zaripov")
                .email("russo@mail.ru")
                .build();
        EmployeeDTO savedEmployee = employeeService.saveEmployee(employeeDTO).block();
        webTestClient.delete().uri("/api/v1/test/delete/{employeeId}", Collections.singletonMap("employeeId", savedEmployee.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(response -> System.out.println(response));
    }
}
