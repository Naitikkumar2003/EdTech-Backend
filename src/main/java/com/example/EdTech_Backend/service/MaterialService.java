package com.example.EdTech_Backend.service;

import com.example.EdTech_Backend.Entity.StudyMaterial;
import com.example.EdTech_Backend.Repository.StudyMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final StudyMaterialRepository studyMaterialRepository;

    public List<StudyMaterial> getMaterialsByClass(Long classId) {
        return studyMaterialRepository.findBySchoolClass_Id(classId);
    }
    public ResponseEntity<Resource> downloadMaterial(Long id) throws Exception {

        StudyMaterial studyMaterial = studyMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not available"));

        File file =new File(studyMaterial.getFilePath());
        if(!file.exists()) {
            throw  new RuntimeException("file not found");
        }
        Resource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + studyMaterial.getFilename() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    }
}
