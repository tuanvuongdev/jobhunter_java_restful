package vn.hoidanit.jobhunter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.response.email.ResEmailJob;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriberService {

    private final Logger log = LoggerFactory.getLogger(SubscriberService.class);

    private final SubscriberRepository subscriberRepository;

    private final SkillRepository skillRepository;

    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository, JobRepository jobRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

//    @Scheduled(cron = "*/10 * * * * *")
//    public void testCron() {
//        System.out.println(">>>TEST CRON");
//    }

    public Subscriber handleCreateSubscriber(Subscriber sub) throws IdInvalidException {
        Optional<Subscriber> sOptional = this.subscriberRepository.findByEmail(sub.getEmail());
        if (sOptional.isPresent()) {
            throw new IdInvalidException("Subscriber with email = " + sub.getEmail() + " already existed");
        }

        if (sub.getSkills() != null) {
            List<Long> listSkillIds = sub.getSkills().stream().map(Skill::getId).toList();
            List<Skill> listSkills = this.skillRepository.findAllById(listSkillIds);

            sub.setSkills(listSkills);
        }

        log.debug("Request to create subcriber: {}", sub);

        return this.subscriberRepository.save(sub);
    }

    public Subscriber handleUpdateSubscriber(Subscriber sub) throws IdInvalidException {
        Optional<Subscriber> sOptional = this.subscriberRepository.findById(sub.getId());
        if (sOptional.isEmpty()) {
            throw new IdInvalidException("Subscriber with id = " + sub.getId() + " is not exist");
        }

        Subscriber currentSubscriber = sOptional.get();
        if (sub.getSkills() != null) {
            List<Long> listSkillIds = sub.getSkills().stream().map(Skill::getId).toList();
            List<Skill> listSkills = this.skillRepository.findAllById(listSkillIds);

            currentSubscriber.setSkills(listSkills);
        }

        return this.subscriberRepository.save(currentSubscriber);
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (!listSubs.isEmpty()) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && !listSkills.isEmpty()) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && !listJobs.isEmpty()) {

                        List<ResEmailJob> arr = listJobs.stream().map(
                                this::convertJobToSendEmail).toList();

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(skill -> new ResEmailJob.SkillEmail(skill.getName())).toList();
        res.setSkills(s);

        return res;
    }

    public Subscriber findByEmail(String email) {
        Optional<Subscriber> sOptional = this.subscriberRepository.findByEmail(email);
        return sOptional.orElse(null);
    }
}
