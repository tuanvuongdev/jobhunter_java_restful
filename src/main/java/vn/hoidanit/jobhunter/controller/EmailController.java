package vn.hoidanit.jobhunter.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;

    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send email successfull")
//    @Scheduled(cron = "*/10 * * * * *")
//    @Transactional
    public String sendSimpleEmail() {
//        this.emailService.sendEmailSync(
//                "vuongminh.wedding@gmail.com",
//                "Test send email",
//                "<h1> <b>Hello world</b> </h1>",
//                false,
//                true
//        );
//        this.emailService.sendEmailFromTemplateSync(
//                "vuongminh.wedding@gmail.com",
//                "Test send email",
//                "job"
//        );
        this.subscriberService.sendSubscribersEmailJobs();
        return "ok";
    }
}
