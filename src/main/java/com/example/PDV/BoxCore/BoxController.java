package com.example.PDV.BoxCore;

import com.example.PDV.BoxCore.BoxDtos.BoxEntryDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/box")
public class BoxController {

    private final BoxService boxService;

    public BoxController(BoxService boxService) {

        this.boxService = boxService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startBox(@RequestBody BoxEntryDto box) {

        boxService.startBox(box);

        return ResponseEntity.status(HttpStatus.CREATED).body("Caixa Iniciado com " +
                "sucesso!");
    }

    @PostMapping("/finish/{id}")
    public ResponseEntity<String> finishBox(@PathVariable Integer id) {

        boxService.finishBox(id);

        return ResponseEntity.status(HttpStatus.OK).body("Caixa encerrado com " +
                "sucesso!");
    }
}
