package com.pm.patientservice.serivce;

import com.pm.patientservice.dto.PatientRequestDto;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;


    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponseDTO> patientResponseDTOS = patients.stream()
                .map(patient -> PatientMapper.toDTO(patient)).toList();

        return patientResponseDTOS;
    }

    public PatientResponseDTO createPatient(PatientRequestDto patientRequestDto) {
        // an email must be unique
        if (patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email "
                                                  + "already exists" + patientRequestDto.getEmail());
        }

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDto));

        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id,
                                            PatientRequestDto patientRequestDto) {

        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDto.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email "
                                                  + "already exists" + patientRequestDto.getEmail());
        }

        patient.setName(patientRequestDto.getName());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

}
