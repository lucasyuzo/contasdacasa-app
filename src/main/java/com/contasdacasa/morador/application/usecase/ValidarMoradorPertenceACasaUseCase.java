package com.contasdacasa.morador.application.usecase;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.morador.application.domain.Morador;
import com.contasdacasa.morador.application.exception.MoradorNaoPertenceACasaException;
import org.springframework.stereotype.Service;

@Service
public class ValidarMoradorPertenceACasaUseCase {

  public void executar(Morador morador, Casa casa) {
    if (!morador.getCasaId().equals(casa.getId())) {
      throw new MoradorNaoPertenceACasaException(morador.getId(), casa.getId());
    }
  }
}
