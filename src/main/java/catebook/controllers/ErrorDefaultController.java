
package catebook.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorDefaultController implements ErrorController {

    @Override
    @GetMapping("/error")
    public String getErrorPath() {
        return "errorpage";
    }
    
}
