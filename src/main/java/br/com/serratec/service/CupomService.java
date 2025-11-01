package br.com.serratec.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.serratec.entity.Cupom;
import br.com.serratec.exception.ResourceNotFoundException;
import br.com.serratec.repository.CupomRepository;

@Service
public class CupomService {

    private final CupomRepository cupomRepo;

    public CupomService(CupomRepository cupomRepo) {
        this.cupomRepo = cupomRepo;
    }

    @Transactional
    public Cupom criar(Cupom cupom) {
        return cupomRepo.save(cupom);
    }

    @Transactional(readOnly = true)
    public List<Cupom> listarTodos() {
        return cupomRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Cupom buscarPorId(Long id) {
        return cupomRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cupom não encontrado"));
    }

    @Transactional
    public Cupom atualizar(Long id, Cupom dados) {
        Cupom existente = buscarPorId(id);

        if (dados.getCodigo() != null) {
            existente.setCodigo(dados.getCodigo());
        }

        if (dados.getDesconto() != null) {
            existente.setDesconto(dados.getDesconto());
        }

        return cupomRepo.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Cupom existente = buscarPorId(id);
        cupomRepo.delete(existente);
    }
}