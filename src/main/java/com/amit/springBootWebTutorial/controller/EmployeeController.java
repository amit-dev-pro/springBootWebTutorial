package com.amit.springBootWebTutorial.controller;

import com.amit.springBootWebTutorial.dto.EmployeeDto;
import com.amit.springBootWebTutorial.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    /* Access the data from db on this mapping*/
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable(name="employeeId") Long id ){
        Optional<EmployeeDto> employeeDto=employeeService.getEmployeeById(id);
        return employeeDto.map(employeeDto1 -> ResponseEntity.ok(employeeDto1))
                .orElseThrow(()->new ResourceNotFoundException("Employee not found with id "+id));
    }

    /* Get all the data from the db by this mapping*/
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployee(@RequestParam(required = false,name="inputAge") Integer age,
                                                            @RequestParam(required = false) String sortBy){
        return ResponseEntity.ok(employeeService.getAllEmployee());
    }

    /* Post the data into db using this mapping*/
    @PostMapping
    public ResponseEntity<EmployeeDto> createNewEmployee(@Valid @RequestBody EmployeeDto employeeDto){
        EmployeeDto saveEmployee=employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(saveEmployee, HttpStatus.CREATED);
    }

    /* updated the data  by this mapping*/
    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> updatedEmployeeById(@RequestBody @Valid EmployeeDto employeeDto,
                                                           @PathVariable Long employeeId){
        return ResponseEntity.ok(employeeService.updatedEmployeeById(employeeId,employeeDto));
    }

    /*  deleted data by id by this mapping*/
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Boolean> deleteEmployeeById(@PathVariable Long employeeId){
        boolean gotDeleted= employeeService.deleteEmployeeById(employeeId);
        if(gotDeleted) return ResponseEntity.ok(true);
        return ResponseEntity.notFound().build();
    }

    /* update data partial into the db by this mapping */
    @PatchMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> updatedPartialEmployeeById(@RequestBody Map<String, Object> updated,
                                                                  @PathVariable Long employeeId){
        EmployeeDto employeeDto=employeeService.updatePartialEmployeeById(employeeId,updated);
        return ResponseEntity.ok(employeeDto);
    }


}
