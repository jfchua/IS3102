package application;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import session.SessionListener;
@EnableAutoConfiguration
//@ComponentScan
@Configuration
@SpringBootApplication
@EnableAsync(proxyTargetClass=true)
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
    	 SpringApplication.run(Application.class, args);
         
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
               
    }
    
    /*@Component
    public class MyHttpSessionListener implements javax.servlet.http.HttpSessionListener, ApplicationContextAware {

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            if (applicationContext instanceof WebApplicationContext) {
                ((WebApplicationContext) applicationContext).getServletContext().addListener(this);
            } else {
                //Either throw an exception or fail gracefully, up to you
                throw new RuntimeException("Must be inside a web application context");
            }
        }

        @Override
		public void sessionCreated(HttpSessionEvent event) {
			System.out.println("HttpSessionListener started"); 
	    	HttpSession session = event.getSession();
	    	event.getSession().setMaxInactiveInterval(5*60);
	    	 System.out.println("ID=" + session.getId() + " MaxInactiveInterval="
	    			 + session.getMaxInactiveInterval());
			
		}

		@Override
		public void sessionDestroyed(HttpSessionEvent arg0) {
			System.out.println("HttpSessionListener destroyed");
			
		}                
    }*/

}