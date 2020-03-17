package adrianromanski.webfluxrest.controllers;

import adrianromanski.webfluxrest.domain.Vendor;
import adrianromanski.webfluxrest.repositories.VendorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("/vendors")
    Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping("/vendors/{id}")
    Mono<Vendor> getByID(@PathVariable String id) {
        return vendorRepository.findById(id);
    }
}
