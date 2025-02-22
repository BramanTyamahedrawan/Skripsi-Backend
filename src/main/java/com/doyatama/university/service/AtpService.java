package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.Elemen;
import com.doyatama.university.model.Kelas;
import com.doyatama.university.model.TahunAjaran;
import com.doyatama.university.model.Semester;
import com.doyatama.university.model.Mapel;
import com.doyatama.university.model.KonsentrasiKeahlian;
import com.doyatama.university.model.Acp;
import com.doyatama.university.model.Atp;
import com.doyatama.university.payload.ElemenRequest;
import com.doyatama.university.payload.AtpRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.AtpRepository;
import com.doyatama.university.repository.AcpRepository;
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

public class AtpService {

    private AtpRepository atpRepository = new AtpRepository();
    private AcpRepository acpRepository = new AcpRepository();
    private ElemenRepository elemenRepository = new ElemenRepository();
    private TahunAjaranRepository tahunAjaranRepository = new TahunAjaranRepository();
    private KelasRepository kelasRepository = new KelasRepository();
    private SemesterRepository semesterRepository = new SemesterRepository();
    private MapelRepository mapelRepository = new MapelRepository();
    private KonsentrasiKeahlianRepository konsentrasiKeahlianRepository = new KonsentrasiKeahlianRepository();

    public PagedResponse<Atp> getAllAtp(int page, int size, String tahunAjaranID, String semesterID,
            String kelasID, String mapelID, String konsentrasiKeahlianID, String elemenID, String acpID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<Atp> atpResponse;

        if (mapelID.equalsIgnoreCase("*")) {
            atpResponse = atpRepository.findAll(size);
        } else {
            atpResponse = atpRepository.findAtpByMapel(mapelID, size);
        }

        return new PagedResponse<>(atpResponse, atpResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public Atp createAtp(AtpRequest atpRequest) throws IOException {

        if (atpRepository.existsById(atpRequest.getIdAtp())) {
            throw new BadRequestException("Atp already exists");
        }

        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(atpRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(atpRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(atpRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(atpRequest.getIdMapel());
        KonsentrasiKeahlian konsentrasiKeahlianResponse = konsentrasiKeahlianRepository
                .findById(atpRequest.getIdKonsentrasi());
        Elemen elemenResponse = elemenRepository.findById(atpRequest.getIdElemen());
        Acp acpResponse = acpRepository.findById(atpRequest.getIdAcp());

        Atp atp = new Atp();
        atp.setIdAtp(atpRequest.getIdAtp() == null ? UUID.randomUUID().toString() : atpRequest.getIdAtp());
        atp.setNamaAtp(atpRequest.getNamaAtp());
        atp.setTahunAjaran(tahunAjaranResponse);
        atp.setSemester(semesterResponse);
        atp.setKelas(kelasResponse);
        atp.setMapel(mapelResponse);
        atp.setKonsentrasiKeahlian(konsentrasiKeahlianResponse);
        atp.setElemen(elemenResponse);
        atp.setAcp(acpResponse);

        return atpRepository.save(atp);
    }

    public DefaultResponse<Atp> getAtpById(String atpId) throws IOException {
        Atp atpResponse = atpRepository.findAtpById(atpId);
        return new DefaultResponse<>(atpResponse.isValid() ? atpResponse : null, atpResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public Atp updateAtp(String atpId, AtpRequest atpRequest) throws IOException {

        Atp atp = new Atp();

        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(atpRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(atpRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(atpRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(atpRequest.getIdMapel());
        KonsentrasiKeahlian konsentrasiKeahlianResponse = konsentrasiKeahlianRepository
                .findById(atpRequest.getIdKonsentrasi());
        Elemen elemenResponse = elemenRepository.findById(atpRequest.getIdElemen());
        Acp acpResponse = acpRepository.findById(atpRequest.getIdAcp());

        atp.setNamaAtp(atpRequest.getNamaAtp());
        atp.setTahunAjaran(tahunAjaranResponse);
        atp.setSemester(semesterResponse);
        atp.setKelas(kelasResponse);
        atp.setMapel(mapelResponse);
        atp.setKonsentrasiKeahlian(konsentrasiKeahlianResponse);
        atp.setElemen(elemenResponse);
        atp.setAcp(acpResponse);

        return atpRepository.update(atpId, atp);
    }

    public void deleteAtpById(String atpId) throws IOException {
        Atp atpResponse = atpRepository.findAtpById(atpId);
        if (atpResponse.isValid()) {
            atpRepository.deleteById(atpId);
        } else {
            throw new ResourceNotFoundException("Atp", "id", atpId);

        }
    }
}
