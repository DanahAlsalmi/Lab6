package com.example.lab6.Controller;

import com.example.lab6.Api.ApiResponse;
import com.example.lab6.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    ArrayList<Employee> employees = new ArrayList<>();

    //Get all Employees
    @GetMapping("/employees")
    public ResponseEntity getAllEmployees() {
        return ResponseEntity.status(200).body(employees);
    }

    //Add employee
    @PostMapping("/add")
    public ResponseEntity addEmployee(@Valid @RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        employees.add(employee);
        return ResponseEntity.status(201).body(new ApiResponse("Employee added successfully"));
    }

    //Update employee
    @PutMapping("/update/{id}")
    public ResponseEntity updateEmployee(@Valid @RequestBody Employee employee, @PathVariable String id, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId().equals(id)) {
                employees.set(i, employee);
                return ResponseEntity.status(201).body(new ApiResponse("Employee updated successfully"));
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }

    //Delete an employee
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteEmployee(@PathVariable String id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId().equals(id)) {
                employees.remove(i);
                return ResponseEntity.status(201).body(new ApiResponse("Employee deleted successfully"));
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }

    //Search by Position
    @GetMapping("/search/{position}")
    public ResponseEntity searchEmployee(@PathVariable String position) {
        List<Employee> search = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getEmployeePosition().equalsIgnoreCase(position)) {
                search.add(employee);
            }

        }
        return ResponseEntity.status(200).body(search);
//        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }


    //Get Employee by age range
    @GetMapping("/age-range/{minAge}/{maxAge}")
    public ResponseEntity ageRange(@PathVariable int minAge, @PathVariable int maxAge) {
        List<Employee> range = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getEmployeeAge() >= minAge && employee.getEmployeeAge() <= maxAge) {
                range.add(employee);
            }
        }
        return ResponseEntity.status(200).body(range);
    }

    //Apply for annual leave
    @PutMapping("/apply-leave/{id}")
    public ResponseEntity applyLeave(@PathVariable String id) {
        for (Employee employee : employees) {
            if (employee.getEmployeeId().equals(id)) {
                if (employee.isOnLeave()) {
                    return ResponseEntity.status(201).body(new ApiResponse("Employee already on-leave"));
                }
                if (employee.getAnnualLeave() <= 0) {
                    return ResponseEntity.status(201).body(new ApiResponse("Employee has no annual leave remaining"));
                }
                employee.setOnLeave(true);
                employee.setAnnualLeave(employee.getAnnualLeave() - 1);
                return ResponseEntity.status(200).body(new ApiResponse("Annual leave applied successfully."));
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));
    }

    //Get Employee with no Annual leave
    @GetMapping("/no-annual-leave")
    public ResponseEntity noAnnualLeave() {
        List<Employee> noAnnualLeave = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getAnnualLeave() <= 0) {
                noAnnualLeave.add(employee);
            }
        }
        return ResponseEntity.status(200).body(noAnnualLeave);
    }

    //Promote Employee
    @PutMapping("promote/{supervisorId}/{employeeId}")
    public ResponseEntity promote(@PathVariable String supervisorId, @PathVariable String employeeId) {
        for (Employee sub : employees) {
            if (sub.getEmployeeId().equals(supervisorId) && sub.getEmployeePosition().equalsIgnoreCase("Supervisor")) {
                for (Employee emp : employees) {
                    if (emp.getEmployeeId().equals(employeeId)) {
                        if (emp.getEmployeeAge() < 30) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Employee must be at least 30 years old."));
                        }
                        if (emp.isOnLeave()) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Employee is currently on leave."));
                        }
                        emp.setEmployeePosition("Supervisor");
                        return ResponseEntity.status(200).body(new ApiResponse("Employee promoted successfully"));
                    }
                }
                return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("Requester is not a supervisor or does not exist."));
    }
}
