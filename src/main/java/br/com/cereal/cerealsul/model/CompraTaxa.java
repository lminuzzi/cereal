package br.com.cereal.cerealsul.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@NoArgsConstructor
@Data
@Embeddable
public class CompraTaxa {
    @Transient
    private Double taxaFunRural;
    @Transient
    private Double taxaSenar;
    @Transient
    private Double taxaPat;
    @Transient
    private Double taxaIcmsProdutor;
}
