package application.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/error")
public class ErrorHandler implements EmbeddedServletContainerCustomizer
{
    @Override
    public void customize(final ConfigurableEmbeddedServletContainer factory)
    {
    	  ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
          ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
          ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
    	
    	factory.addErrorPages(error401Page,error404Page,error500Page);
       
    }

    
  /*  @RequestMapping("unexpected")
    @ResponseBody
    public String unexpectedError(final HttpServletRequest request)
    {
        return "Exception: " + request.getAttribute("javax.servlet.error.exception");
    }

    @RequestMapping("methodnotallowed")
    @ResponseBody
    public String methodNotAllowed()
    {
        return "405";
    }
    @RequestMapping("notfound")
    @ResponseBody
    public String notFound()
    {
        return "404.html";
    }
    @RequestMapping("unauthorized")
    @ResponseBody
    public String unauthorized()
    {
        return "401";
    }
    @RequestMapping("internalservererror")
    @ResponseBody
    public String internalServerError()
    {
        return "500";
    }*/
}