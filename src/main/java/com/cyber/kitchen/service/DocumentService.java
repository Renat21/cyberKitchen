package com.cyber.kitchen.service;

import com.cyber.kitchen.entity.Document;
import com.cyber.kitchen.repository.DocumentRepository;
import com.cyber.kitchen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocumentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    public Document toDocumentEntity(MultipartFile file) throws IOException {
        return new Document(file.getName(), file.getOriginalFilename(), file.getSize(), file.getContentType(),
                file.getBytes());
    }

//    public void saveDocument(MultipartFile file, User user) throws IOException {
//        if (file.getSize() != 0) {
//            if (user.getDocument() != null) {
//                Document oldDocument = user.getDocument();
//                Document img = toDocumentEntity(file);
//                user.setDocument(img);
//                userRepository.save(user);
//                documentRepository.deleteById(oldDocument.getId());
//            } else {
//                user.setDocument(toDocumentEntity(file));
//                userRepository.save(user);
//            }
//        }
//    }

    public void save(Document document) {
        documentRepository.save(document);
    }


    public Document findDocumentById(Long id){
        return documentRepository.findDocumentById(id);
    }

}
