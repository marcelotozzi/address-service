package com.marcelotozzi.address.api.integration.rest.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by marcelotozzi on 18/04/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZipcodeResponseAPI {
    private String bairro;
    private String cidade;
    private String logradouro;
    private String cep;
    private String estado;

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getCep() {
        return cep;
    }

    public String getEstado() {
        return estado;
    }

}
