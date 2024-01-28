package com.team65.isaproject.controller;

import com.team65.isaproject.dto.AppointmentDTO;
import com.team65.isaproject.mapper.Mapper;
import com.team65.isaproject.model.appointment.Appointment;
import com.team65.isaproject.service.AppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/appointments")
@Tag(name = "Appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Mapper<Appointment, AppointmentDTO> mapper;

    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN')")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {

        return appointmentService.create(appointmentDTO).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping(consumes = "application/json", value = "/createWithoutMail")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN')")
    public ResponseEntity<AppointmentDTO> createAppointmentWithoutMail(@RequestBody AppointmentDTO appointmentDTO) {

        return appointmentService.createAppointmentWithoutMail(appointmentDTO).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN')")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable Integer id)
    {
        Appointment appointment = appointmentService.findById(id);

        if (appointment == null)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(mapper.mapToDto(appointment, AppointmentDTO.class), HttpStatus.OK);
    }

    @GetMapping(value = "/byCompanyId/{id}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN')")
    public ResponseEntity<List<AppointmentDTO>> getAllByCompanyId(@PathVariable Integer id){
        List<Appointment> appointments = appointmentService.getAllAppointmentsByCompanyId(id);

        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();

        for(Appointment a : appointments){
            appointmentDTOS.add(mapper.mapToDto(a, AppointmentDTO.class));
        }

        if(appointmentDTOS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(appointmentDTOS, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN')")
    public ResponseEntity<AppointmentDTO> updateAppointment(@RequestBody AppointmentDTO appointmentDTO){
        var appointment = appointmentService.update(appointmentDTO);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping(value = "/byUserId/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AppointmentDTO>> getAllByUserId(@PathVariable Integer id){
        List<Appointment> appointments = appointmentService.getAllAppointmentsByUserId(id);

        List<AppointmentDTO> appointmentDTOS = new ArrayList<>();

        for(Appointment a : appointments){
            appointmentDTOS.add(mapper.mapToDto(a, AppointmentDTO.class));
        }

        if(appointmentDTOS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(appointmentDTOS, HttpStatus.OK);
    }
}
