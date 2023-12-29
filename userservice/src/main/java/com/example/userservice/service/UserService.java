package com.example.userservice.service;

import com.example.userservice.clients.CompanyServiceFeignClient;
import com.example.userservice.entity.PgUser;
import com.example.userservice.repository.PgUserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.userservice.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final PgUserRepository userRepository;
    private final CompanyServiceFeignClient companyServiceFeignClient;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.producer.topic.company-deleted-user}")
    private String companyDeletedUserTopic;

    @KafkaListener(topics = "${spring.kafka.consumer.topic.company-deleted}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleCompanyDeleted(Long companyId) {
        List<PgUser> users = userRepository.getUsersByCompanyId(companyId);
        users.forEach(user -> {
            user.setCompanyId(null);
            userRepository.save(user);
        });
        kafkaTemplate.send(companyDeletedUserTopic, companyId.toString());
    }

    @Transactional
    public String getUserName(Long id){
        PgUser pgUser = userRepository.getPgUserById(id);
        return pgUser.getName();
    }

    public List<UserDto> getUsers(){
        List<PgUser> pgUsers = userRepository.findAll();
        List<UserDto> userDTOS = new ArrayList<>();
        for (PgUser pgUser:pgUsers) {
            UserDto userDTO = new UserDto();
            userDTO.setId(pgUser.getId());
            userDTO.setName(pgUser.getName());
            userDTO.setEnabled(pgUser.isEnabled());
            userDTO.setLogin(pgUser.getLogin());
            userDTO.setPassword(pgUser.getPassword());
            userDTO.setEmail(pgUser.getEmail());
            if(pgUser.getCompanyId() == null){
                userDTOS.add(userDTO);
            }
            else{
                userDTO.setCompanyId(pgUser.getCompanyId());
                String name = companyServiceFeignClient.getNameById(pgUser.getCompanyId());
                userDTO.setCompanyName(name);
                userDTOS.add(userDTO);
            }

        }
        return userDTOS;
    }

    @Transactional
    public void changeStatusUser(Long id){
        PgUser user = userRepository.getPgUserById(id);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    @Transactional
    public void changeUserInfo(UserDto dto){
        PgUser user = userRepository.getPgUserById(dto.getId());
        if (user==null){
            throw new EntityNotFoundException( "Такого пользователя еще нет" );
        }
        boolean isExist = companyServiceFeignClient.existById(dto.getCompanyId());
        if(!isExist){
            throw new EntityNotFoundException( "Компании с таким айди %s не существует".formatted(dto.getCompanyId()));
        }
        user.setName(dto.getName());
        user.setCompanyId(dto.getCompanyId());
        user.setEmail(dto.getEmail());
        userRepository.save(user);
    }

    @Transactional
    public boolean findUser (Long id){
        PgUser pgUser = userRepository.getPgUserById(id);
        return pgUser != null && pgUser.isEnabled();
    }

    @Transactional
    public Long createUser(UserDto dto){
        Boolean isExist = companyServiceFeignClient.existById(dto.getCompanyId());
        if(!isExist){
            throw new EntityNotFoundException(
                    "Компания с иднтификатором %s не существует".formatted(dto.getCompanyId())
            );

        }
        return userRepository.save(dto.toEntity()).getId();
    }
}
