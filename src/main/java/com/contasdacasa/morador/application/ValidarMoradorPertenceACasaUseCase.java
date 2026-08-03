package com.contasdacasa.morador.application;

import com.contasdacasa.casa.domain.Casa;
import com.contasdacasa.morador.domain.Morador;
import com.contasdacasa.morador.domain.MoradorNaoPertenceACasaException;
import org.springframework.stereotype.Service;

@Service
public class ValidarMoradorPertenceACasaUseCase {

  public void executar(Morador morador, Casa casa) {
    if (!morador.getCasaId().equals(casa.getId())) {
      throw new MoradorNaoPertenceACasaException(morador.getId(), casa.getId());
    }
  }
}
