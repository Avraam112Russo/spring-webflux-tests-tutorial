package com.n1nt3nd0.springwebfluxtestingtutorial.controller;

import com.n1nt3nd0.springwebfluxtestingtutorial.dto.EmployeeDTO;
import com.n1nt3nd0.springwebfluxtestingtutorial.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private EmployeeServiceImpl employeeService;

    EmployeeDTO employeeDTO;
    @BeforeEach
    public void setUp(){
        employeeDTO = EmployeeDTO.builder()
                .firstName("Russo")
                .lastName("Zaripov")
                .email("russo@mail.ru")
                .build();

    }

    @DisplayName("junit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee(){
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDTO.class)))
                .willReturn(Mono.just(employeeDTO));
        WebTestClient.ResponseSpec responseSpec = webTestClient.post().uri("/api/v1/test/save")
                .contentType(MediaType.APPLICATION_JSON)// request
                .accept(MediaType.APPLICATION_JSON)// response
                .body(Mono.just(employeeDTO), EmployeeDTO.class)
                .exchange();
        responseSpec.expectStatus().isCreated()
                .expectBody()
                .consumeWith((response) -> System.out.println(response))
                .jsonPath("$.firstName").isEqualTo(employeeDTO.getFirstName())
                .jsonPath("$.email").isEqualTo(employeeDTO.getEmail());
    }
    @DisplayName("junit test for get employee by id operation")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){

        String employeeId = "123";
        BDDMockito.given(employeeService.getEmployeeById(ArgumentMatchers.anyString()))
                .willReturn(Mono.just(employeeDTO));

        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri("/api/v1/test/getById/{employeeID}",
                        Collections.singletonMap("employeeID", employeeId)// передаем pathvariable в виде map(key, value)
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
        responseSpec.expectStatus().isOk()
                .expectBody()
                .consumeWith((response) -> System.out.println(response))
                .jsonPath("$.firstName").isEqualTo(employeeDTO.getFirstName())
                .jsonPath("$.email").isEqualTo(employeeDTO.getEmail());
    }
    @DisplayName("junit test for get all employees operation")
    @Test
    public void givenAllEmployees_whenGetAllEmployees_thenReturnEmployeeList(){

        EmployeeDTO employeeDTO_2 = EmployeeDTO.builder()
                .firstName("Elena")
                .lastName("Sokolova")
                .email("elena@mail.ru")
                .build();
        List<EmployeeDTO> employeeDTOList = new ArrayList<>(List.of(employeeDTO, employeeDTO_2));
        BDDMockito.given(employeeService.getAllEmployees()).willReturn(Flux.fromIterable(employeeDTOList));

        WebTestClient.ResponseSpec responseSpec = webTestClient.get().uri("/api/v1/test/getAllEmployees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
        responseSpec.expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> System.out.println(response))
                .jsonPath("$.size()").isEqualTo(2);
    }
    @DisplayName("junit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject(){
        String employeeId = "123";
        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(EmployeeDTO.class), ArgumentMatchers.anyString()))
                .willReturn(Mono.just(employeeDTO));
        WebTestClient.ResponseSpec responseSpec = webTestClient.put().uri("/api/v1/test/update/{employeeID}",
                        Collections.singletonMap("employeeID", employeeId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDTO), EmployeeDTO.class)
                .exchange();
        responseSpec.expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> System.out.println(response))
                .jsonPath("$.firstName").isEqualTo(employeeDTO.getFirstName())
                .jsonPath("$.email").isEqualTo(employeeDTO.getEmail());
    }

    @DisplayName("junit test for delete employee operation")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing(){
        String employeeId = "123";
        BDDMockito.given(employeeService.deleteEmployee(employeeId))
                .willReturn(Mono.empty());
        WebTestClient.ResponseSpec responseSpec = webTestClient.delete()
                .uri("/api/v1/test/delete/{employeeId}", Collections.singletonMap("employeeId", employeeId))
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
        responseSpec.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(response -> System.out.println(response));
    }
}
