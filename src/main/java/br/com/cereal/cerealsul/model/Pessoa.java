package br.com.cereal.cerealsul.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@MappedSuperclass
public class Pessoa {
    @NotBlank
    private String local;
    private String cpf;
    private String cnpj;
    private String regiao;
    private String rua;
    private String bairro;
    private String inscEst;

    public TipoPessoa getTipoPessoa() {
        if (this.getCpf() != null && !this.getCpf().trim().equals("")) {
            return TipoPessoa.PESSOA_FISICA;
        }
        return TipoPessoa.PESSOA_JURIDICA;
    }
}