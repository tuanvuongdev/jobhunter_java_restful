package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.repository.CompaynyRepository;

@Service
public class CompanyService {

    private final CompaynyRepository compaynyRepository;


    public CompanyService(CompaynyRepository compaynyRepository) {
        this.compaynyRepository = compaynyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.compaynyRepository.save(company);
    }
}
