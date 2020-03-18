package adrianromanski.webfluxrest.controllers;

import adrianromanski.webfluxrest.domain.Category;
import adrianromanski.webfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    Flux<Category> list() {
        return categoryRepository.findAll();
    }

    @GetMapping("/categories/{id}")
    Mono<Category> getByID(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/categories")
    Mono<Void> create(@RequestBody Publisher<Category> categoryPublisher) {
        return categoryRepository.saveAll(categoryPublisher).then();
    }

    @PutMapping("/categories/{id}")
    Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("/categories/{id}")
    Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {

        Category foundCategory = categoryRepository.findById(id).block();

        if(foundCategory.getDescription() != category.getDescription()) {
            foundCategory.setDescription(category.getDescription());
            return categoryRepository.save(foundCategory);
        }

        return Mono.just(foundCategory);
    }
}
