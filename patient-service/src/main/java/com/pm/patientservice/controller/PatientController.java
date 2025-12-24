package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import com.pm.patientservice.serivce.PatientService;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patientResponseDTOS = patientService.getPatients();
        return ResponseEntity.ok().body(patientResponseDTOS);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(
            @RequestBody @Validated({Default.class, CreatePatientValidationGroup.class}) PatientRequestDto patientRequestDto) {
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDto);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable("id") UUID id,
            @RequestBody @Validated({Default.class}) PatientRequestDto patientRequestDto) {
        PatientResponseDTO patientResponseDTO = patientService.updatePatient(id, patientRequestDto);
        return ResponseEntity.ok().body(patientResponseDTO);
    }
}
