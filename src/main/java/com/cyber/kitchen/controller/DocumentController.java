package com.cyber.kitchen.controller;

import com.cyber.kitchen.entity.Document;
import com.cyber.kitchen.repository.DocumentRepository;
import com.cyber.kitchen.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class DocumentController {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DocumentService documentService;

    @GetMapping("/document/{id}")
    public ResponseEntity<?> getDocument(@PathVariable Long id){
        Document document = documentRepository.findDocumentById(id);
        return ResponseEntity.ok().header("fileName",
                        document.getOriginalFileName()).contentType(
                        MediaType.valueOf(document.getContentType())).contentLength(document.getSize()).
                body(new InputStreamResource(new ByteArrayInputStream(document.getBytes())));
    }


    @RequestMapping(value = "/document/create", method = RequestMethod.POST)
    @ResponseBody
    public List<Long> processReloadData(@RequestParam("file") List<MultipartFile> files) {
        return documentService.saveDocuments(files);
    }


//    @RequestMapping(value = "/document/createDocument", method = RequestMethod.POST)
//    @ResponseBody
//    public List<Document> createDocument(@RequestParam("file") List<MultipartFile> files) {
//        return files.stream().map(file -> {
//            try {
//                Document document = documentService.toDocumentEntity(file);
//                documentService.save(document);
//                return document;
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).toList();
//    }
}