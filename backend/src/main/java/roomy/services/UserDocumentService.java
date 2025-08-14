package roomy.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import roomy.dto.UserDocumentDto;
import roomy.entities.User;
import roomy.entities.UserDocument;
import roomy.entities.enums.VerificationStatus;
import roomy.exceptions.ResourceNotFoundException;
import roomy.repositories.UserDocumentRepository;
import roomy.repositories.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDocumentService {

    private final UserDocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${document.upload-dir}")
    private String uploadDir;

    // Upload document
//    public UserDocument uploadDocument(Long userId, MultipartFile file) throws IOException {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        Path uploadPath = Paths.get(uploadDir);
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//
//        String originalFileName = file.getOriginalFilename();
//        String extension = originalFileName != null && originalFileName.contains(".")
//                ? originalFileName.substring(originalFileName.lastIndexOf("."))
//                : "";
//
//        String newFileName = UUID.randomUUID().toString() + extension;
//        Path filePath = uploadPath.resolve(newFileName);
//        file.transferTo(filePath.toFile());
//
//        UserDocument document = new UserDocument();
//        document.setUser(user);
//        document.setDocumentName(originalFileName);
//        document.setDocumentPath("/uploads/documents/" + newFileName);
//        return documentRepository.save(document);
//    }


    public UserDocument uploadDocument(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath(); // ensure absolute path
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("Created folder: " + uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : "";

        String newFileName = UUID.randomUUID().toString() + extension;
        Path filePath = uploadPath.resolve(newFileName);

        System.out.println("Uploading file to: " + filePath); // debug path

        file.transferTo(filePath.toFile());

        UserDocument document = new UserDocument();
        document.setUser(user);
        document.setDocumentName(originalFileName);
        document.setDocumentPath("/uploads/documents/" + newFileName);
        return documentRepository.save(document);
    }


//    // Admin verification
//    public UserDocument verifyDocument(Long documentId, VerificationStatus status) {
//        UserDocument document = documentRepository.findById(documentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
//
//        // Update verification status
//        document.setVerificationStatus(status);
//        UserDocument savedDocument = documentRepository.save(document);
//
//        // Prepare email details
//        String userEmail = document.getUser().getEmail();
//        String subject = "Document Verification Status";
//        String body;
//
//        switch (status) {
//            case APPROVED:
//                body = "Dear " + document.getUser().getName() + ",\n\n"
//                        + "Your document \"" + document.getDocumentName() + "\" has been successfully verified and approved.\n"
//                        + "You can now continue using our platform with verified status.\n\n"
//                        + "Best regards,\nSupport Team";
//                break;
//
//            case REJECTED:
//                body = "Dear " + document.getUser().getName() + ",\n\n"
//                        + "Unfortunately, your document \"" + document.getDocumentName() + "\" could not be approved.\n"
//                        + "Please upload a correct and valid document to proceed with verification.\n\n"
//                        + "Best regards,\nSupport Team";
//                break;
//
//            default:
//                body = "Dear " + document.getUser().getName() + ",\n\n"
//                        + "Your document \"" + document.getDocumentName() + "\" is currently pending review.\n"
//                        + "We will notify you once verification is complete.\n\n"
//                        + "Best regards,\nSupport Team";
//                break;
//        }
//
//        // Send the email
//        emailService.sendEmail(userEmail, subject, body);
//
//        return savedDocument;
//    }


    public UserDocumentDto verifyDocument(Long documentId, VerificationStatus status) {
        UserDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        // Update verification status
        document.setVerificationStatus(status);
        UserDocument savedDocument = documentRepository.save(document);

        // Send email
        String userEmail = savedDocument.getUser().getEmail();
        String subject = "Document Verification Status";
        String body;
        switch (status) {
            case APPROVED:
                body = "Dear " + savedDocument.getUser().getName() + ",\n\n"
                        + "Your document \"" + savedDocument.getDocumentName() + "\" has been approved.";
                break;
            case REJECTED:
                body = "Dear " + savedDocument.getUser().getName() + ",\n\n"
                        + "Your document \"" + savedDocument.getDocumentName() + "\" was rejected. Please upload a valid document.";
                break;
            default:
                body = "Dear " + savedDocument.getUser().getName() + ",\n\n"
                        + "Your document \"" + savedDocument.getDocumentName() + "\" is pending review.";
                break;
        }
        emailService.sendEmail(userEmail, subject, body);

        // Return DTO
        return new UserDocumentDto(
                savedDocument.getId(),
                savedDocument.getDocumentName(),
                savedDocument.getDocumentPath(),
                savedDocument.getVerificationStatus()
        );
    }


    public List<UserDocumentDto> getUserDocuments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return documentRepository.findByUser(user)
                .stream()
                .map(doc -> new UserDocumentDto(
                        doc.getId(),
                        doc.getDocumentName(),
                        doc.getDocumentPath(),
                        doc.getVerificationStatus()
                )) // <- close map parenthesis here
                .collect(Collectors.toList());
    }


}
