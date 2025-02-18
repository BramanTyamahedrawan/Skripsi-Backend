package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Kelas;
import com.doyatama.university.payload.KelasRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.KelasRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class KelasService {
    private KelasRepository kelasRepository = new KelasRepository();
    
    private static final Logger logger = LoggerFactory.getLogger(KelasService.class);

    public PagedResponse<Kelas> getAllKelas(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<Kelas> kelasResponse = new ArrayList<>();
        kelasResponse = kelasRepository.findAll(size);
        

        return new PagedResponse<>(kelasResponse, kelasResponse.size(), "Successfully get data", 200);
    }

    public Kelas createKelas(KelasRequest kelasRequest) throws IOException {
        
        Kelas kelas = new Kelas();
            kelas.setIdKelas(kelasRequest.getIdKelas());
            kelas.setNamaKelas(kelasRequest.getNamaKelas());
            return kelasRepository.save(kelas);
    }        

    public DefaultResponse<Kelas> getKelasById(String kelasId) throws IOException {
        // Retrieve Kelas
        Kelas kelas = kelasRepository.findById(kelasId);
        return new DefaultResponse<>(kelas.isValid() ? kelas : null, kelas.isValid() ? 1 : 0, "Successfully get data");
    }
    
    public Kelas updateKelas(String kelasId, KelasRequest kelasRequest) throws IOException {
        Kelas kelas = new Kelas();
            kelas.setNamaKelas(kelasRequest.getNamaKelas());
            return kelasRepository.update(kelasId, kelas);
    }
    
    public void deleteKelasById(String kelasId) throws IOException {
        Kelas kelasResponse = kelasRepository.findById(kelasId);
        if(kelasResponse.isValid()){
            kelasRepository.deleteById(kelasId);
        }else{
            throw new ResourceNotFoundException("Kelas", "id", kelasId);
        }
    }



    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

}
