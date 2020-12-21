package com.example.ZwitterSpring.controller;


import com.example.ZwitterSpring.domain.Message;
import com.example.ZwitterSpring.domain.User;
import com.example.ZwitterSpring.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    //@GetMapping("/user")
    //public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
    //    return Collections.singletonMap("name", principal.getAttribute("name"));
    //}

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Map<String, Object> model) {
        Iterable<Message> messages = messageRepo.findAll();

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        }
        else
            messages = messageRepo.findAll();

        model.put("messages", messages);
        model.put("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            Map<String, Object> model,
            @AuthenticationPrincipal User user,
            @RequestParam String tag,
            @RequestParam String text,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Message message = new Message(user, tag, text);
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            File uploadDirectory = new File(uploadPath);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            message.setFilename(resultFileName);

            messageRepo.save(message);

            Iterable<Message> messages = messageRepo.findAll();
            model.put("messages", messages);

        }
        return "main";

    }


}
