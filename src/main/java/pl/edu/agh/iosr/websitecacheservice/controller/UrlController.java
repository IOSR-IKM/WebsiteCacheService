package pl.edu.agh.iosr.websitecacheservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.iosr.websitecacheservice.integration.Storage;

@RestController
@RequestMapping("/wcs")
@RequiredArgsConstructor
public class UrlController {

    private final Storage storage;

    @GetMapping("/{shortcut}")
    public ResponseEntity<String> getCachedWebsite(@PathVariable String shortcut) {
        String response = storage.getCachedWebsite(shortcut);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}
