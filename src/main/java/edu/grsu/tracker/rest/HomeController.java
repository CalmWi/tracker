package edu.grsu.tracker.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Home")
@Controller
public class HomeController {

    @GetMapping("/home")
    public String getHomePage() {
        return "index";
    }
}
