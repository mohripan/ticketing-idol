package com.external.ticketingidoluserservice.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerRegistrationRequest {
    private String username;
    private String email;
    private String password;
    private String organizationName;
}
