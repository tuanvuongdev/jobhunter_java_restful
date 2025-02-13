package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompaynyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompaynyRepository compaynyRepository;


    public CompanyService(CompaynyRepository compaynyRepository) {
        this.compaynyRepository = compaynyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.compaynyRepository.save(company);
    }

    public ResultPaginationDTO<List<Company>> getAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.compaynyRepository.findAll(spec, pageable);
        ResultPaginationDTO<List<Company>> rs = new ResultPaginationDTO<>();
        Meta mt = new Meta();

        mt.setPage(pageCompany.getNumber() + 1);
        mt.setPages(pageCompany.getTotalPages());
        mt.setPageSize(pageCompany.getSize());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());

        return rs;
    }

    public Company handleUpdateCompany(Company company) throws Exception {
        Optional<Company> companyOptional = this.compaynyRepository.findById(company.getId());

        if (companyOptional.isPresent()) {
            Company currentCompany = companyOptional.get();
            currentCompany.setName(company.getName());
            currentCompany.setDescription(company.getDescription());
            currentCompany.setAddress(company.getAddress());
            currentCompany.setLogo(company.getLogo());
            return this.compaynyRepository.save(currentCompany);
        }
        throw new Exception("Company không tồn tại");
    }

    public void handleDeleteCompany(long id) {
        this.compaynyRepository.deleteById(id);
    }
}
