package net.koedooder.kvk.pva.repository;

import net.koedooder.kvk.pva.model.PostalCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostalCodeRepository extends JpaRepository<PostalCode, Long> {
    Optional<PostalCode> findByCountryCode(String countryCode);
}
