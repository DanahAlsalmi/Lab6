package com.example.lab6.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class Employee {

    @NotEmpty(message = "Employee ID cannot be NULL.")
    @Size(min = 2, message = "ID must be more then 2 Character")
    private String employeeId;

    @NotEmpty(message = "Employee Name cannot be EMPTY.")
    @Size(min = 4,message = "Name must be more then 4 Character.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must contain only characters")
    private String employeeName;

    @Email(message = "Email should be valid")
    private String employeeEmail;

    //    @Pattern(regexp = "^(\\+9665|05)([0-9]{7})$")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with '05' and contain exactly 10 digits")
    private String employeePhoneNumber;

    @NotNull(message = "Employee Age cannot be NULL.")
    @Positive(message = "Age must be Positive numbers")
    @Min(value = 25,message = "The minimum Age is 25")
    @Max(value = 65,message = "The maximum Age is 65")
    private int employeeAge;

    @NotEmpty(message = " Employee Position cannot be EMPTY.")
    @Pattern(regexp ="Supervisor|Coordinator", message = "Status must be either 'Supervisor', 'Coordinator'" )
    private String employeePosition;

    @AssertFalse
    private boolean onLeave;

    @NotNull(message = "Employee Year cannot be NULL.")
    @PastOrPresent
    private LocalDate employmentYear;

    @NotNull(message = "Annual leave cannot be NULL.")
    @Positive(message = "Annual leave must be a positive number")
    private int annualLeave;

}
