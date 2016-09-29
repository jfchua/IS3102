package application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    //@ResponseBody
 /*   @RequestMapping("/")
    public String getHomePage() {
        LOGGER.debug("Getting home page");
        String msg = new String("home");
        return msg;
    }x
    */
    @RequestMapping(value="/",method = RequestMethod.GET)
    public String homepage(){
        return "forward:index.html";

    }

}
