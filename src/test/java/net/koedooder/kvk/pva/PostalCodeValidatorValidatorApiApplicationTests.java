package net.koedooder.kvk.pva;

import net.koedooder.kvk.pva.controller.PostalCodeController;
import net.koedooder.kvk.pva.repository.PostalCodeRepository;
import net.koedooder.kvk.pva.service.PostalCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostalCodeValidatorValidatorApiApplicationTests {
	@Autowired
	private PostalCodeController postalCodeController;
	@Autowired
	private PostalCodeService postalCodeService;
	@Autowired
	private PostalCodeRepository postalCodeRepository;
	@Test
	void contextLoads() {
		assertNotNull(postalCodeController);
		assertNotNull(postalCodeService);
		assertNotNull(postalCodeRepository);
	}
}
