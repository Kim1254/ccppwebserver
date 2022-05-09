package com.gachon.ccppwebserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SpringBootApplication
public class MainController {
	@RequestMapping("/index")
    public ModelAndView getWSView() {
		ModelAndView resultMV = new ModelAndView();
		resultMV.setViewName("index");
        return resultMV;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(MainController.class, args);
	}

}
