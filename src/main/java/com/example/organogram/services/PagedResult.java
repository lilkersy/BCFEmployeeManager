package com.example.organogram.services;

import com.example.organogram.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagedResult {
    private List<Employee> data;
    private long totalSize;
    private long totalPages;
}
