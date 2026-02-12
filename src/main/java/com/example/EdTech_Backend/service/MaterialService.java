package com.example.EdTech_Backend.service;

import com.example.EdTech_Backend.Entity.StudyMaterial;
import com.example.EdTech_Backend.Repository.StudyMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final StudyMaterialRepository studyMaterialRepository;

    public List<StudyMaterial> getMaterialsByClass(Long classId) {
        return studyMaterialRepository.findBySchoolClass_Id(classId);
    }
}
