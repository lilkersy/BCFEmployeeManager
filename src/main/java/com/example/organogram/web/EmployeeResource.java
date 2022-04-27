package com.example.organogram.web;

import com.example.organogram.model.Employee;
import com.example.organogram.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeResource {
    private final EmployeeService employeeService;

    @PostMapping("/add")
    public Employee addEmployee(@RequestBody Employee employee, @RequestParam(required = false) Long supervisorId) {
        return employeeService.addEmployee(employee, supervisorId);
    }

    @PostMapping("/move")
    public Employee moveEmployee(@RequestBody Employee employee, @RequestParam(required = false) Long supervisorId) {
        return employeeService.moveEmployee(employee, supervisorId);
    }
}
