package com.example.companyservice.service;

import com.example.companyservice.clients.UsServiceFeignClient;
import com.example.companyservice.entity.PgCompany;
import com.example.companyservice.model.CompanyDto;
import com.example.companyservice.repository.PgCompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    private final PgCompanyRepository companyRepository;
    private final UsServiceFeignClient usServiceFeignClient;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.producer.topic.company-deleted}")
    private String companyDeletedTopic;

    @KafkaListener(topics = "${spring.kafka.consumer.topic.company-deleted-user}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void handleCompanyDeleted(String id) {
        Optional<PgCompany> pgCompany = companyRepository.findById(Long.parseLong(id));
        if(pgCompany.isPresent())
            companyRepository.delete(pgCompany.get());
        else
            throw new EntityNotFoundException("Компания не найдена");
    }

    public ResponseEntity<String> deleteCompany(String id)  {
        PgCompany pgCompany = companyRepository.findById(Long.parseLong(id)).orElseThrow(() ->
                new EntityNotFoundException("Компания не найдена"));
        pgCompany.setDeleted(true);
        companyRepository.save(pgCompany);
        kafkaTemplate.send(companyDeletedTopic, id);

        return ResponseEntity.ok("Компания удалена");
    }

    @Transactional
    public boolean findCompany (Long id){
        PgCompany pgCompany = companyRepository.getPgCompanyById(id);
        return pgCompany != null;
    }

    @Transactional
    public String getCompanyName (Long id){
        PgCompany pgCompany = companyRepository.getPgCompanyById(id);
        return pgCompany.getName();
    }

    @Transactional
    public List<CompanyDto> getCompanies(){
        List<PgCompany> pgCompanies = companyRepository.findAll();
        List<CompanyDto> companyDTOS = new ArrayList<>();
        for (PgCompany pgCompany:pgCompanies) {
            CompanyDto companyDTO = new CompanyDto();
            companyDTO.setId(pgCompany.getId());
            companyDTO.setName(pgCompany.getName());
            companyDTO.setOgrn(pgCompany.getOgrn());
            companyDTO.setDescription(pgCompany.getDescription());
            companyDTO.setDirectorId(pgCompany.getDirectorId());
            String name = usServiceFeignClient.getNameById(pgCompany.getDirectorId());
            companyDTO.setDirectorFio(name);
            companyDTOS.add(companyDTO);
        }
        return companyDTOS;
    }

    @Transactional
    public Long createCompany(CompanyDto dto){
        Boolean isExist = usServiceFeignClient.existById(dto.getDirectorId());
        if(!isExist){
            throw new EntityNotFoundException(
                    "Пользователя с идентификатором %s не существует".formatted(dto.getDirectorId())
            );

        }
        return companyRepository.save(dto.toEntity()).getId();
    }
}
