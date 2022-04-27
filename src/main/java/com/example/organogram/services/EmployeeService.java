package com.example.organogram.services;

import com.example.organogram.model.Employee;
import com.example.organogram.model.EmployeeRepository;
import com.example.organogram.services.errors.SubordinatesExistException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public Employee addEmployee(Employee employee, Long supervisorId) {
        if (supervisorId != null) {
            employeeRepository.findById(supervisorId).ifPresent(employee::setSupervisor);
        }
        return employeeRepository.save(employee);
    }

    public Employee moveEmployee(Employee employee, Long supervisorId) {
        if (supervisorId != null) {
            employeeRepository.findById(supervisorId).ifPresent(employee::setSupervisor);
        }
        return employeeRepository.save(employee);
    }

    public PagedResult list(String keyword, int startPage, int pageSize) {
        Specification<Employee> specification = Specification.where(filterAll());
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.toLowerCase();
            specification = specification.and(filterByName(keyword));
        }
        Page<Employee> page = employeeRepository.findAll(specification, PageRequest.of(startPage, pageSize));
        return new PagedResult(page.getContent(), page.getTotalElements(), page.getTotalPages());
    }

    public void deleteEmployee(Long employeeId) {
        employeeRepository.findById(employeeId).ifPresent(employee -> {
            if (employeeRepository.existsBySupervisor(employee)) {
                throw new SubordinatesExistException();
            }
            employeeRepository.delete(employee);
        });
    }

    private Specification<Employee> filterAll() {
        return (r, cq, cb) -> cb.isNotNull(r.get("id"));
    }

    private Specification<Employee> filterByName(String keyword) {
        return (root, cq, cb) -> cb.like(cb.lower(root.get("name")), "%" + keyword + "%");
    }
}
