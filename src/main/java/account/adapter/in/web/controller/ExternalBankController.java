package account.adapter.in.web.controller;

import account.application.service.ExternalBankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external-bank")
public class ExternalBankController {

    private final ExternalBankService externalBankService;

    public ExternalBankController(ExternalBankService externalBankService) {
        this.externalBankService = externalBankService;
    }

    @GetMapping("/availability")
    public ResponseEntity<String> checkAvailability() {

        String response = externalBankService.checkBankAvailability();
        return ResponseEntity.ok(response);
    }
}
