package com.example.democlient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class TokenResponse implements Serializable {
    @JsonProperty("token")
    private final String token;
}
