package com.example.organogram.services;

import com.example.organogram.model.Employee;
import com.example.organogram.model.EmployeeRepository;
import com.example.organogram.services.errors.SubordinatesExistException;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(RandomBeansExtension.class)
public class EmployeeServiceTest {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;
    @Random(excludes = {"id", "supervisor"}, type = Employee.class, size = 2)
    private List<Employee> supervisors;

    @BeforeEach
    public void setup() {
        supervisors = employeeRepository.saveAll(supervisors);
    }

    @AfterEach
    public void teardown() {
        employeeRepository.deleteAll();
    }

    @Test
    public void testSaveEmployeeWithoutSupervisor(@Random(excludes = {"id", "supervisor"}) Employee employee) {
        Employee emp = employeeService.addEmployee(employee, null);

        assertNotNull(emp.getId());
    }

    @Test
    public void testSaveEmployeeWithSupervisor(@Random(excludes = {"id", "supervisor"}) Employee employee) {
        Employee emp = employeeService.addEmployee(employee, supervisors.get(0).getId());

        assertNotNull(emp.getId());
        assertTrue(employeeRepository.existsBySupervisor(supervisors.get(0)));
    }

    @Test
    public void testMoveEmployee(@Random(excludes = {"id", "supervisor"}) Employee employee) {
        employee.setSupervisor(supervisors.get(0));
        employee = employeeRepository.save(employee);
        assertTrue(employeeRepository.existsBySupervisor(supervisors.get(0)));
        assertFalse(employeeRepository.existsBySupervisor(supervisors.get(1)));

        employeeService.moveEmployee(employee, supervisors.get(1).getId());
        assertTrue(employeeRepository.existsBySupervisor(supervisors.get(1)));
        assertFalse(employeeRepository.existsBySupervisor(supervisors.get(0)));
    }

    @Test
    public void testDeleteSupervisorWithSubordinates(@Random(excludes = {"id", "supervisor"}) Employee employee) {
        employee.setSupervisor(supervisors.get(0));
        employeeRepository.save(employee);
        assertThrows(SubordinatesExistException.class, () -> {
            employeeService.deleteEmployee(supervisors.get(0).getId());
        });
        assertNotNull(employeeRepository.getById(supervisors.get(0).getId()));
    }

    @Test
    @Transactional
    public void testDeleteSupervisorWithoutSubordinates() {

        employeeService.deleteEmployee(supervisors.get(0).getId());
        assertThat(supervisors.get(0)).isNotIn(employeeRepository.findAll());
    }

    @Test
    public void testList(@Random(excludes = {"id", "supervisor"}) Employee employee) {
        employee = employeeRepository.save(employee);
        PagedResult result = employeeService.list("", 0, Integer.MAX_VALUE);
        assertThat(employee).isIn(result.getData());
        assertNotEquals(0, result.getTotalSize());
        assertNotEquals(0, result.getTotalPages());
    }
}
