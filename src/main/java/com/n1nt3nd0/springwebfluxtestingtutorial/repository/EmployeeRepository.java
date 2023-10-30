package com.n1nt3nd0.springwebfluxtestingtutorial.repository;

import com.n1nt3nd0.springwebfluxtestingtutorial.entity.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String> {
}
