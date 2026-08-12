package com.contasdacasa.despesa.application.usecase;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.despesa.application.domain.Despesa;
import com.contasdacasa.despesa.application.exception.DespesaNaoPertenceACasaException;

import org.springframework.stereotype.Service;

@Service
public class ValidarDespesaPertenceACasaUseCase {

    public void executar(Despesa despesa, Casa casa) {
        if (!despesa.getCasaId().equals(casa.getId())) {
            throw new DespesaNaoPertenceACasaException(despesa.getId(), casa.getId());
        }
    }
}
