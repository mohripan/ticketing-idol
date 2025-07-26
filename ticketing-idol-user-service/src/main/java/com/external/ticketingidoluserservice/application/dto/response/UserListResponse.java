package com.external.ticketingidoluserservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserListResponse {
    private List<UserResponse> users;
}
