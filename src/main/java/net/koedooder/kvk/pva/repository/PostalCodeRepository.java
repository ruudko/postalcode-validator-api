package net.koedooder.kvk.pva.repository;

import net.koedooder.kvk.pva.model.PostalCodeValidator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostalCodeRepository extends JpaRepository<PostalCodeValidator, Long> {
    Optional<PostalCodeValidator> findByCountryCode(String countryCode);
}
