package edu.miu.cse.vsms.service.impl;

import edu.miu.cse.vsms.dto.request.EmployeeRequestDto;
import edu.miu.cse.vsms.dto.response.EmployeeResponseDto;
import edu.miu.cse.vsms.dto.response.VehicleServiceResponseDto;
import edu.miu.cse.vsms.exception.ResourceNotFoundException;
import edu.miu.cse.vsms.model.Employee;
import edu.miu.cse.vsms.repository.EmployeeRepository;
import edu.miu.cse.vsms.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponseDto addEmployee(EmployeeRequestDto request) {
        Employee emp = employeeRepository.save(mapToEmployee(request));
        return mapToResponseDto(emp);
    }

    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeRepository.findAll().stream().map(x->mapToResponseDto(x)).toList();
    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {
        return employeeRepository.findById(id).map(x->mapToResponseDto(x)).orElseThrow(()->new ResourceNotFoundException("Employee Not Found"));
    }

    @Override
    public EmployeeResponseDto partiallyUpdateEmployee(Long id, Map<String, Object> updates) {
        // Fetch the employee by ID or throw an exception if not found
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id " + id));

        // Apply each update based on the key
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    employee.setName((String) value);
                    break;
                case "email":
                    employee.setEmail((String) value);
                    break;
                case "phone":
                    employee.setPhone((String) value);
                    break;
                case "hireDate":
                    employee.setHireDate(LocalDate.parse((String) value));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + key);
            }
        });
        return mapToResponseDto(employeeRepository.save(employee));
    }

    private EmployeeResponseDto mapToResponseDto(Employee employee) {
        List<VehicleServiceResponseDto> serviceDtos = employee.getVServices().stream()
                .map(service -> new VehicleServiceResponseDto(
                        service.getId(),
                        service.getServiceName(),
                        service.getCost(),
                        service.getVehicleType()
                )).toList();

        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getHireDate(),
                serviceDtos
        );
    }
    private Employee mapToEmployee(EmployeeRequestDto employeeRequestDto) {
        Employee employee = new Employee();
        employee.setName(employeeRequestDto.name());
        employee.setEmail(employeeRequestDto.email());
        employee.setPhone(employeeRequestDto.phone());
        employee.setHireDate(employeeRequestDto.hireDate());
        return employee;
    }
}
