package co.sptnk.service.user.controllers;

import co.sptnk.service.user.model.ServiceConfiguration;
import co.sptnk.service.user.services.IConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/config")
public class ServiceConfigurationController {

    private final IConfigService configService;

    public ServiceConfigurationController(IConfigService service) {
        this.configService = service;
    }

    @PutMapping
    public ResponseEntity<ServiceConfiguration> update(@RequestBody ServiceConfiguration conf) {
        return new ResponseEntity<>(configService.update(conf), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<ServiceConfiguration>> getAll() {
        return new ResponseEntity<>(configService.getAll(), HttpStatus.OK);
    }
}
