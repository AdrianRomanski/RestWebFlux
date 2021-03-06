package adrianromanski.webfluxrest.controllers;

import adrianromanski.webfluxrest.domain.Category;
import adrianromanski.webfluxrest.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;


    @Before
    public void setUp() throws Exception {
        categoryRepository = Mockito.mock(CategoryRepository.class);

        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void list() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("Cat1").build(),
                Category.builder().description("Cat2").build()));

        webTestClient.get()
                .uri("/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void getByID() {
        given(categoryRepository.findById("someID"))
                .willReturn(Mono.just(Category.builder().description("Cat1").build()));
        webTestClient.get().
                uri("/categories/someID")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    public void testCreateCategory() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just(Category.builder().description("Some Cat").build());

        webTestClient.post()
                .uri("/categories")
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdate() {
        given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate  = Mono.just(Category.builder().description("Some Cat").build());

        webTestClient.put()
                .uri("/categories/someid")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void testPatchWithChanges() {

        given(categoryRepository.findById(anyString())).willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate  = Mono.just(Category.builder().description("Some Cat").build());

        webTestClient.patch()
                .uri("/categories/1")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any());
    }

    @Test
    public void testPatchNoChanges() {

        given(categoryRepository.findById(anyString())).willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate  = Mono.just(Category.builder().build());

        webTestClient.patch()
                .uri("/categories/1")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, never()).save(any());
    }
}