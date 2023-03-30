package com.cyber.kitchen.service;

import com.cyber.kitchen.entity.Document;
import com.cyber.kitchen.entity.Message;
import com.cyber.kitchen.repository.DocumentRepository;
import com.cyber.kitchen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    public List<Long> saveDocuments(List<MultipartFile> files) {
        return files.stream().map(file -> {
            try {
                Document document = toDocumentEntity(file);
                documentRepository.save(document);
                return document.getId();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    public Document findDocumentById(Long id){
        return documentRepository.findDocumentById(id);
    }


    @Transactional
    public void save(Document document){
        documentRepository.save(document);
    }

}
