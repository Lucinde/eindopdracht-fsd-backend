package com.lucinde.plannerpro.dtos;
//Aangezien dit een soort Dto is deze toegevoegd aan dtos ipv payload
public class AuthenticationResponse {
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
