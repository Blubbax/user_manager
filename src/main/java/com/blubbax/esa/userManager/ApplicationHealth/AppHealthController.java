package com.blubbax.esa.userManager.ApplicationHealth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AppHealthController {

    private final com.blubbax.esa.userManager.ApplicationHealth.AppHealthService appHealthService;

    public AppHealthController(com.blubbax.esa.userManager.ApplicationHealth.AppHealthService appHealthService) {
        this.appHealthService = appHealthService;
    }

    @Operation(summary = "Get current readiness status")
    @GetMapping("/api/health/readiness")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Readiness check succeeded",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Readiness check failed",
                    content = @Content)
    })
    public ResponseEntity<ReadinessState> getApplicationReadiness() {
        ReadinessState readinessState = this.appHealthService.getReadinessState();
        if (readinessState == ReadinessState.ACCEPTING_TRAFFIC) {
            return ResponseEntity.status(HttpStatus.OK).body(readinessState);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(readinessState);
        }
    }

    @Operation(summary = "Set current readiness status")
    @PostMapping("/api/health/readiness/{readiness}")
    public void setApplicationReadiness(@PathVariable boolean readiness) {
        this.appHealthService.setReadinessState(readiness);
    }

    @Operation(summary = "Get current liveness status")
    @GetMapping("/api/health/liveness")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liveness check succeeded",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Liveness check failed",
                    content = @Content)
    })
    public ResponseEntity<LivenessState> getApplicationLiveness() {
        LivenessState livenessState = this.appHealthService.getLivenessState();
        if (livenessState == LivenessState.CORRECT) {
            return ResponseEntity.status(HttpStatus.OK).body(livenessState);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(livenessState);
        }
    }

    @Operation(summary = "Set current liveness status")
    @PostMapping("/api/health/liveness/{liveness}")
    public void setApplicationLiveness(@PathVariable boolean liveness) {
        this.appHealthService.setLivenessState(liveness);
    }
}
