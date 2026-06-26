package com.jobflow.jobflow.services;

import com.jobflow.jobflow.models.Resume;
import com.jobflow.jobflow.models.User;
import com.jobflow.jobflow.repositories.ResumeRepository;
import com.jobflow.jobflow.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final String uploadDir = "uploads/resumes/";

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository,JwtService jwtService) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public Resume addResume(MultipartFile file, String tokenBearer) throws Exception{
        if(tokenBearer == null || !tokenBearer.startsWith("Bearer ")){
            throw new Exception("Token de sécurité manquant");
        }
        tokenBearer = tokenBearer.substring(7);

        Long userId = jwtService.extractUserId(tokenBearer);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Utilisateur non trouvé"));

        if(file.isEmpty()){
            throw new Exception("fichier non trouvable");
        }

        if(file.getSize()>5*1024*1024){
            throw new Exception("La taille du fichier doit être inferieur a 5Mo");
        }

        if(!"application/pdf".equals(file.getContentType())){
            throw new Exception("Seuls les pdf sont autorisés");
        }

        File directory = new File(uploadDir);
        if(!directory.exists()){
            directory.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        Path path = Paths.get(uploadDir + fileName);

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        Resume resume = new Resume();
        resume.setFileName(fileName);
        resume.setFilePath(path.toString());
        resume.setUploadedDate(LocalDateTime.now());
        resume.setUser(user);

        return resumeRepository.save(resume);
    }

}
