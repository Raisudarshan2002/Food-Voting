package com.example.voteBestFood.controller;

import com.example.voteBestFood.entity.Food;
import com.example.voteBestFood.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    private final String UPLOAD_DIR = "uploads/";

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("foods", foodRepository.findAllByOrderByUpvotesDesc());
        return "index";
    }

    @GetMapping("/upload")
    public String uploadForm() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFood(@RequestParam String name,
                             @RequestParam String description,
                             @RequestParam MultipartFile image) throws IOException {

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());

        Food food = new Food();
        food.setName(name);
        food.setDescription(description);
        food.setImagePath("/uploads/" + fileName);

        foodRepository.save(food);
        return "redirect:/";
    }

    @PostMapping("/food/{id}/upvote")
    public String upvote(@PathVariable Long id) {
        foodRepository.incrementUpvote(id);
        return "redirect:/";
    }

    @PostMapping("/food/{id}/downvote")
    public String downvote(@PathVariable Long id) {
        foodRepository.incrementDownvote(id);
        return "redirect:/";
    }

    @PostMapping("/food/{id}/delete")
    public String delete(@PathVariable Long id) {
        foodRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/winner")
    public String winner(Model model) {
        List<Food> foods = foodRepository.findAllByOrderByUpvotesDesc();
        if (!foods.isEmpty()) {
            model.addAttribute("winner", foods.get(0));
        }
        return "winner";
    }
}
