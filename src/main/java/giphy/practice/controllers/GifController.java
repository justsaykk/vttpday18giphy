package giphy.practice.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import giphy.practice.services.ApiService;

@Controller
@RequestMapping("/")
public class GifController {

    @Autowired
    private ApiService apiSvc;

    @GetMapping(path = "/search")
    public String postQuery(
            @RequestParam(name = "q") String q,
            @RequestParam(name = "limit") String limit,
            @RequestParam(name = "rating") String rating,
            Model model) {

        List<String> listOfUrl = apiSvc.fetch(q, limit, rating);
        List<String> listOfUrlDelimited = new LinkedList<>();

        for (String string : listOfUrl) {
            listOfUrlDelimited.add(string.replaceAll("\"", ""));
        }

        model.addAttribute("q", q);
        model.addAttribute("limit", limit);
        model.addAttribute("rating", rating);
        model.addAttribute("list", listOfUrlDelimited);
        return "result";
    }
}
