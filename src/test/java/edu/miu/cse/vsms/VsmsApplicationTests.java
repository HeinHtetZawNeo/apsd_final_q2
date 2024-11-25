package edu.miu.cse.vsms;

import edu.miu.cse.vsms.controller.EmployeeController;
import edu.miu.cse.vsms.dto.request.EmployeeRequestDto;
import edu.miu.cse.vsms.dto.response.EmployeeResponseDto;
import edu.miu.cse.vsms.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class VsmsApplicationTests {

   @Mock
   private EmployeeService employeeService;
   @InjectMocks
   private EmployeeController employeeController;

    @Test
    void createEmployee_ValidInput() throws Exception {
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(
                "Employee1",
                "test@email.com",
                "123123",
                LocalDate.of(2010, 10, 10));
        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(
                1L,
                "Employee1",
                "test@email.com",
                "123123",
                LocalDate.of(2010, 10, 10),
                new ArrayList<>());

        Mockito.when(employeeService.addEmployee(employeeRequestDto)).thenReturn(employeeResponseDto);
        ResponseEntity<EmployeeResponseDto> responseEntity = employeeController.addEmployee(employeeRequestDto);

        assert responseEntity.getStatusCode() == HttpStatus.CREATED;
        Assertions.assertEquals(responseEntity.getBody(), employeeResponseDto);
    }

}
