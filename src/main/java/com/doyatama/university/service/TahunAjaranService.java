package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.TahunAjaran;
import com.doyatama.university.payload.TahunAjaranRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.TahunAjaranRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class TahunAjaranService {
    private TahunAjaranRepository tahunRepository = new TahunAjaranRepository();
    
    private static final Logger logger = LoggerFactory.getLogger(TahunAjaranService.class);

    public PagedResponse<TahunAjaran> getAllTahunAjaran(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<TahunAjaran> tahunResponse = new ArrayList<>();
        tahunResponse = tahunRepository.findAll(size);
        

        return new PagedResponse<>(tahunResponse, tahunResponse.size(), "Successfully get data", 200);
    }

    public TahunAjaran createTahunAjaran(TahunAjaranRequest tahunRequest) throws IOException {
        
        TahunAjaran tahun = new TahunAjaran();
            tahun.setIdTahun(tahunRequest.getIdTahun());
            tahun.setTahunAjaran(tahunRequest.getTahunAjaran());
            return tahunRepository.save(tahun);
    }        

    public DefaultResponse<TahunAjaran> getTahunAjaranById(String tahunId) throws IOException {
        // Retrieve TahunAjaran
        TahunAjaran tahun = tahunRepository.findById(tahunId);
        return new DefaultResponse<>(tahun.isValid() ? tahun : null, tahun.isValid() ? 1 : 0, "Successfully get data");
    }
    
    public TahunAjaran updateTahunAjaran(String tahunId, TahunAjaranRequest tahunRequest) throws IOException {
        TahunAjaran tahun = new TahunAjaran();
            tahun.setTahunAjaran(tahunRequest.getTahunAjaran());
            return tahunRepository.update(tahunId, tahun);
    }
    
    public void deleteTahunAjaranById(String tahunId) throws IOException {
        TahunAjaran tahunResponse = tahunRepository.findById(tahunId);
        if(tahunResponse.isValid()){
            tahunRepository.deleteById(tahunId);
        }else{
            throw new ResourceNotFoundException("TahunAjaran", "id", tahunId);
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
