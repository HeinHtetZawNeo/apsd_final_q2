package edu.miu.cse.vsms.service.impl;

import edu.miu.cse.vsms.dto.request.ServiceRequestDto;
import edu.miu.cse.vsms.dto.response.VehicleServiceResponseDto;
import edu.miu.cse.vsms.model.Employee;
import edu.miu.cse.vsms.model.VService;
import edu.miu.cse.vsms.repository.EmployeeRepository;
import edu.miu.cse.vsms.repository.VehicleServiceRepository;
import edu.miu.cse.vsms.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleServiceRepository vehicleServiceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public VehicleServiceResponseDto assignService(ServiceRequestDto request) {
        Employee emp = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id " + request.employeeId()));


        return mapToVehicleServiceResponseDto(vehicleServiceRepository.save(mapToVService(request,emp)));
    }

    private VService mapToVService(ServiceRequestDto serviceRequestDto, Employee emp) {
        VService vService = new VService();
        vService.setServiceName(serviceRequestDto.serviceName());
        vService.setEmployee(emp);
        vService.setCost(serviceRequestDto.cost());
        vService.setVehicleType(serviceRequestDto.vehicleType());
        return vService;
    }

    private VehicleServiceResponseDto mapToVehicleServiceResponseDto(VService vService) {
        return new VehicleServiceResponseDto(
                vService.getId(),
                vService.getServiceName(),
                vService.getCost(),
                vService.getVehicleType()
        );
    }
}
