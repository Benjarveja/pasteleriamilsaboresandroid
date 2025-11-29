package cl.milsabores.pasteleria.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReferenceDataService {

    private final List<Map<String, String>> paymentMethods = List.of(
            Map.of("id", "card", "label", "Tarjeta de crédito o débito"),
            Map.of("id", "transfer", "label", "Transferencia bancaria")
    );

    private final List<Map<String, String>> pickupBranches = List.of(
            Map.of("id", "providencia", "label", "Casa Matriz - Av. Providencia 1234"),
            Map.of("id", "las-condes", "label", "Las Condes - Av. Apoquindo 5432"),
            Map.of("id", "nunoa", "label", "Ñuñoa - Av. Irarrázaval 3456"),
            Map.of("id", "santiago-centro", "label", "Santiago Centro - Av. Libertador Bernardo O'Higgins 987")
    );

    private final List<Map<String, String>> pickupSlots = List.of(
            Map.of("id", "morning", "label", "09:00 - 11:00 hrs"),
            Map.of("id", "midday", "label", "11:00 - 14:00 hrs"),
            Map.of("id", "afternoon", "label", "14:00 - 17:00 hrs"),
            Map.of("id", "evening", "label", "17:00 - 19:30 hrs")
    );

    public String findPaymentLabel(String id) {
        return paymentMethods.stream()
                .filter(method -> method.get("id").equals(id))
                .map(method -> method.get("label"))
                .findFirst()
                .orElse(id);
    }

    public String findBranchLabel(String id) {
        return pickupBranches.stream()
                .filter(branch -> branch.get("id").equals(id))
                .map(branch -> branch.get("label"))
                .findFirst()
                .orElse(id);
    }

    public String findPickupSlotLabel(String id) {
        return pickupSlots.stream()
                .filter(slot -> slot.get("id").equals(id))
                .map(slot -> slot.get("label"))
                .findFirst()
                .orElse(id);
    }
}

