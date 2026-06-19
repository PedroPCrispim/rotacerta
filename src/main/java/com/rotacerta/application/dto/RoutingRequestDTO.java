package com.rotacerta.application.dto;

import java.util.UUID;

public class RoutingRequestDTO {
    private UUID depotAddressId;

    public RoutingRequestDTO() {}

    public UUID getDepotAddressId() { return depotAddressId; }
    public void setDepotAddressId(UUID depotAddressId) { this.depotAddressId = depotAddressId; }
}
