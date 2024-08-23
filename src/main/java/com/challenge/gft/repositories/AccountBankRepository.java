package com.challenge.gft.repositories;

import com.challenge.gft.entities.AccountBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountBankRepository extends JpaRepository<AccountBank, Long>, JpaSpecificationExecutor<AccountBank> {
    boolean existsByAgency(final String agency);
}
