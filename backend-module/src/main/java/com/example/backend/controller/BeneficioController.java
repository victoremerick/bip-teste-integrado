package com.example.backend.controller;

import com.example.backend.model.BeneficioDto;
import com.example.backend.service.BeneficioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {

    private final BeneficioService service;

    public BeneficioController(BeneficioService service) {
        this.service = service;
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoSuchElementException ex) { return ex.getMessage(); }

    @GetMapping
    public List<BeneficioDto> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public BeneficioDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeneficioDto create(@Valid @RequestBody BeneficioDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public BeneficioDto update(@PathVariable Long id, @Valid @RequestBody BeneficioDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    public static class TransferRequest {
        public Long fromId;
        public Long toId;
        public BigDecimal amount;
        public Long getFromId() { return fromId; }
        public void setFromId(Long fromId) { this.fromId = fromId; }
        public Long getToId() { return toId; }
        public void setToId(Long toId) { this.toId = toId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transfer(@RequestBody TransferRequest req) {
        service.transfer(req.fromId, req.toId, req.amount);
    }
}
