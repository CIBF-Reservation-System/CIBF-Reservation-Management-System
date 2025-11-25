package com.cibf.adminservice.admin.Client;

import com.cibf.adminservice.admin.DTO.Internal.StallServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "stall-service",
        url = "${spring.cloud.openfeign.client.config.stall-service.url}",
        fallback = StallServiceClientFallback.class
)
public interface StallServiceClient {

    @GetMapping("/api/v1/stall/getstalls")
    ResponseEntity<List<StallServiceDTO>> getAllStalls();

    @GetMapping("/api/v1/stall/stall/{stallId}")
    ResponseEntity<StallServiceDTO> getStallById(@PathVariable("stallId") UUID stallId);

    @GetMapping("/api/v1/stall/available")
    ResponseEntity<List<StallServiceDTO>> getAvailableStalls();

    @GetMapping("/api/v1/stall/reserved")
    ResponseEntity<List<StallServiceDTO>> getReservedStalls();

    @PutMapping("/api/v1/stall/updateavailability/{stallId}")
    ResponseEntity<StallServiceDTO> updateStallAvailability(@PathVariable("stallId") UUID stallId);

    @PostMapping("/api/v1/stall/addstall")
    ResponseEntity<StallServiceDTO> saveStall(@RequestBody StallServiceDTO stallDTO);

    @PutMapping("/api/v1/stall/updatestall/{stallId}")
    ResponseEntity<StallServiceDTO> updateStall(@PathVariable("stallId") UUID stallId,
                                                @RequestBody StallServiceDTO stallDTO);

    @DeleteMapping("/api/v1/stall/deletestall/{stallId}")
    ResponseEntity<Void> deleteStall(@PathVariable("stallId") UUID stallId);

    @GetMapping("/api/v1/stall/pending")
    ResponseEntity<List<StallServiceDTO>> getPendingApprovalStalls();

    @PatchMapping("/api/v1/stalls/{stallId}/availability")
    ResponseEntity<StallServiceDTO> updateStallAvailability(
            @PathVariable("stallId") UUID stallId,
            @RequestParam("isAvailable") Boolean isAvailable
    );

}
