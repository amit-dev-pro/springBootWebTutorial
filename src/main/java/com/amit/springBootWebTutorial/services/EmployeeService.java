package com.amit.springBootWebTutorial.services;

import com.amit.springBootWebTutorial.dto.EmployeeDto;
import com.amit.springBootWebTutorial.entity.EmployeeEntity;
import com.amit.springBootWebTutorial.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
     private final ModelMapper modelMapper;

     /*find the data by id from the db*/
     public Optional<EmployeeDto> getEmployeeById(Long id) {
         return employeeRepository.findById(id).map(employeeEntity -> modelMapper.map(employeeEntity,EmployeeDto.class));
     }

     /*find all data from the db*/
     public List<EmployeeDto> getAllEmployee(){
         List<EmployeeEntity> employeeEntities=employeeRepository.findAll();
         return employeeEntities.stream()
                 .map(employeeEntity -> modelMapper
                         .map(employeeEntity,EmployeeDto.class))
                 .collect(Collectors.toList());
     }

     /* Below method check the by id data is present or not*/
     public void isExistsByEmployeeId(Long employeeId){
         boolean exits=employeeRepository.existsById(employeeId);
         if(!exits) throw new ResourceNotFoundException("Employee not found with id "+employeeId);
     }

     /*updated the data by id*/
     public EmployeeDto updatedEmployeeById(Long employeeId,EmployeeDto employeeDto){
         isExistsByEmployeeId(employeeId);
         EmployeeEntity employeeEntity=modelMapper.map(employeeDto,EmployeeEntity.class);
         employeeEntity.setId(employeeId);
         EmployeeEntity saveEmployeeEntity=employeeRepository.save(employeeEntity);
         return modelMapper.map(saveEmployeeEntity,EmployeeDto.class);
     }

     /* delete the data by id*/
     public boolean deleteEmployeeById(Long employeeId){
         isExistsByEmployeeId(employeeId);
         employeeRepository.deleteById(employeeId);
         return true;
     }

     public EmployeeDto updatePartialEmployeeById(Long employeeId, Map<String ,Object> updates){
         isExistsByEmployeeId(employeeId);
         EmployeeEntity employeeEntity=employeeRepository.findById(employeeId).get();
         updates.forEach((field,value)->{
              Field fieldToBeUpdated=ReflectionUtils.findField(EmployeeEntity.class,field);
              fieldToBeUpdated.setAccessible(true);
              ReflectionUtils.setField(fieldToBeUpdated,employeeEntity,value);

         });
         return modelMapper.map(employeeRepository.save(employeeEntity),EmployeeDto.class);
     }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
         EmployeeEntity toSaveEntity=modelMapper.map(employeeDto,EmployeeEntity.class);
         EmployeeEntity savedEmployeeEntity=employeeRepository.save(toSaveEntity);
         return modelMapper.map(savedEmployeeEntity,EmployeeDto.class);
    }
}
