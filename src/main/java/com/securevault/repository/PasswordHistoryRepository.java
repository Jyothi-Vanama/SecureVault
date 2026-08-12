package com.securevault.repository;
import java.util.List;
import com.securevault.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordHistoryRepository
        extends JpaRepository<PasswordHistory, Long> {
            List<PasswordHistory> findTop5ByCredentialCredentialIdOrderByVersionDesc(Long credentialId);
            Optional<PasswordHistory> findTopByCredentialCredentialIdOrderByVersionDesc(Long credentialId);
}