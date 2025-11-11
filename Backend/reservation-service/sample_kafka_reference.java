package lk.bookfair.reservation.service;

import lk.bookfair.reservation.producer.ReservationEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ═══════════════════════════════════════════════════════════════
 *                    RESERVATION SERVICE
 * ═══════════════════════════════════════════════════════════════
 * 
 * This is where the PRODUCER gets called from
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StallRepository stallRepository;
    private final ReservationEventProducer eventProducer;  // ← Inject producer
    
    /**
     * ───────────────────────────────────────────────────────────
     * CREATE RESERVATION
     * ───────────────────────────────────────────────────────────
     * 
     * FLOW:
     * 1. Validate request
     * 2. Check stall availability
     * 3. Save to database
     * 4. Publish event to Kafka ← PRODUCER CALLED HERE
     * 5. Return response to user
     */
    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        
        log.info("📝 Creating reservation for user: {}", request.getUserId());
        
        // ════════════════════════════════════════════════════
        // STEP 1: Validate and get user
        // ════════════════════════════════════════════════════
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // ════════════════════════════════════════════════════
        // STEP 2: Check stall availability
        // ════════════════════════════════════════════════════
        List<Stall> stalls = stallRepository.findAllById(request.getStallIds());
        
        for (Stall stall : stalls) {
            if (!stall.isAvailable()) {
                throw new StallNotAvailableException("Stall " + stall.getName() + " is not available");
            }
        }
        
        // ════════════════════════════════════════════════════
        // STEP 3: Create and save reservation
        // ════════════════════════════════════════════════════
        Reservation reservation = Reservation.builder()
            .user(user)
            .stalls(stalls)
            .reservationDate(LocalDateTime.now())
            .status(ReservationStatus.CONFIRMED)
            .totalAmount(calculateTotal(stalls))
            .build();
        
        // Save to database
        reservation = reservationRepository.save(reservation);
        
        // Mark stalls as reserved
        stalls.forEach(stall -> {
            stall.setAvailable(false);
            stallRepository.save(stall);
        });
        
        log.info("✅ Reservation saved to database: {}", reservation.getId());
        
        // ════════════════════════════════════════════════════
        // STEP 4: Publish event to Kafka
        // ════════════════════════════════════════════════════
        // 🚨 THIS IS WHERE THE MAGIC HAPPENS!
        // This sends the message that Notification Service will receive
        
        eventProducer.publishReservationEvent(reservation, user);
        
        // ════════════════════════════════════════════════════
        // STEP 5: Return response immediately
        // ════════════════════════════════════════════════════
        // User gets response instantly, email sends in background!
        
        log.info("✅ Reservation created successfully");
        
        return ReservationResponse.builder()
            .reservationId(reservation.getId())
            .message("Reservation successful! Confirmation email will be sent shortly.")
            .build();
    }
}