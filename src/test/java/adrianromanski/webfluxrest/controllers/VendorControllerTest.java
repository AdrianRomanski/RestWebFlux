package adrianromanski.webfluxrest.controllers;

import adrianromanski.webfluxrest.domain.Vendor;
import adrianromanski.webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);

        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void list() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Test").build(),
                        Vendor.builder().firstName("Test2").build()));

        webTestClient.get()
                .uri("/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getByID() {
        given(vendorRepository.findById("someID"))
                .willReturn(Mono.just(Vendor.builder().firstName("Test").build()));

        webTestClient.get()
                .uri("/vendors/someID")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void createVendor() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("Walter").lastName("White").build());

        webTestClient.post()
                .uri("/vendors")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdate() {
        given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("Chuck").lastName("Norris").build());

        webTestClient.put()
                .uri("/vendors/someid")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}