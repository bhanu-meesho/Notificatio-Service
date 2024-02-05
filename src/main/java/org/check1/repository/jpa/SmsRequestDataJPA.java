package org.check1.jparepository;


import org.check1.entities.databaseentities.SmsRequestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface SmsRequestDataJPA extends JpaRepository<SmsRequestData,Long>
{

}

