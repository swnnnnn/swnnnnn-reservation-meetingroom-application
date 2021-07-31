package reservation.meetingroom.controller.view;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping(value = "/")
    public String index(){
        return "redirect:/login";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login/login";
    }
}