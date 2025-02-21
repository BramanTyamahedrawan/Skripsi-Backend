package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Elemen;
import com.doyatama.university.model.Kelas;
import com.doyatama.university.model.TahunAjaran;
import com.doyatama.university.model.Semester;
import com.doyatama.university.model.Mapel;
import com.doyatama.university.model.KonsentrasiKeahlian;
import com.doyatama.university.payload.ElemenRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.ElemenRepository;
import com.doyatama.university.repository.KelasRepository;
import com.doyatama.university.repository.KonsentrasiKeahlianRepository;
import com.doyatama.university.repository.MapelRepository;
import com.doyatama.university.repository.SemesterRepository;
import com.doyatama.university.repository.TahunAjaranRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

public class ElemenService {

    private ElemenRepository elemenRepository = new ElemenRepository();
    private TahunAjaranRepository tahunAjaranRepository = new TahunAjaranRepository();
    private KelasRepository kelasRepository = new KelasRepository();
    private SemesterRepository semesterRepository = new SemesterRepository();
    private MapelRepository mapelRepository = new MapelRepository();
    private KonsentrasiKeahlianRepository konsentrasiKeahlianRepository = new KonsentrasiKeahlianRepository();

    public PagedResponse<Elemen> getAllElemen(int page, int size, String mapelID, String tahunAjaranID,
            String semesterID, String kelasID, String konsentrasiKeahlianID) throws IOException {
        validatePageNumberAndSize(page, size);

        List<Elemen> elemenResponse;

        if (mapelID.equalsIgnoreCase("*")) {
            elemenResponse = elemenRepository.findAll(size);
        } else {
            elemenResponse = elemenRepository.findElemenByMapel(mapelID, size);
        }

        return new PagedResponse<>(elemenResponse, elemenResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public Elemen createElemen(ElemenRequest elemenRequest) throws IOException {
        if (elemenRepository.existsById(elemenRequest.getIdElemen())) {
            throw new IllegalArgumentException("Elemen already exist");
        }

        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(elemenRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(elemenRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(elemenRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(elemenRequest.getIdMapel());
        KonsentrasiKeahlian konsentrasiKeahlianResponse = konsentrasiKeahlianRepository
                .findById(elemenRequest.getIdKonsentrasi());

        Elemen elemen = new Elemen();

        elemen.setIdElemen(elemenRequest.getIdElemen() == null ? UUID.randomUUID().toString()
                : elemenRequest.getIdElemen());
        elemen.setNamaElemen(elemenRequest.getNamaElemen());
        elemen.setTahunAjaran(tahunAjaranResponse);
        elemen.setKelas(kelasResponse);
        elemen.setSemester(semesterResponse);
        elemen.setMapel(mapelResponse);
        elemen.setKonsentrasiKeahlian(konsentrasiKeahlianResponse);

        return elemenRepository.save(elemen);
    }

    public DefaultResponse<Elemen> getElemenById(String elemenId) throws IOException {
        Elemen elemenResponse = elemenRepository.findElemenById(elemenId);
        return new DefaultResponse<>(elemenResponse.isValid() ? elemenResponse : null, elemenResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public Elemen updateElemen(String elemenId, ElemenRequest elemenRequest) throws IOException {
        Elemen elemen = new Elemen();
        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(elemenRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(elemenRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(elemenRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(elemenRequest.getIdMapel());
        KonsentrasiKeahlian konsentrasiKeahlianResponse = konsentrasiKeahlianRepository
                .findById(elemenRequest.getIdKonsentrasi());

        if (mapelResponse.getIdMapel() != null && semesterResponse.getIdSemester() != null) {
            elemen.setNamaElemen(elemenRequest.getNamaElemen());
            elemen.setTahunAjaran(tahunAjaranResponse);
            elemen.setKelas(kelasResponse);
            elemen.setSemester(semesterResponse);
            elemen.setMapel(mapelResponse);
            elemen.setKonsentrasiKeahlian(konsentrasiKeahlianResponse);

            return elemenRepository.update(elemenId, elemen);
        } else {
            return null;
        }
    }

    public void deleteElemenById(String elemenId) throws IOException {
        Elemen elemenResponse = elemenRepository.findElemenById(elemenId);
        if (elemenResponse.isValid()) {
            elemenRepository.deleteById(elemenId);
        } else {
            throw new ResourceNotFoundException("Elemen", "id", elemenId);
        }
    }

}
