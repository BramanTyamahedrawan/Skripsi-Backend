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
import com.doyatama.university.payload.ElemenRequest;
import com.doyatama.university.payload.AcpRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
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

public class AcpService {

    private AcpRepository acpRepository = new AcpRepository();
    private ElemenRepository elemenRepository = new ElemenRepository();
    private TahunAjaranRepository tahunAjaranRepository = new TahunAjaranRepository();
    private KelasRepository kelasRepository = new KelasRepository();
    private SemesterRepository semesterRepository = new SemesterRepository();
    private MapelRepository mapelRepository = new MapelRepository();
    private KonsentrasiKeahlianRepository konsentrasiKeahlianRepository = new KonsentrasiKeahlianRepository();

    public PagedResponse<Acp> getAllAcp(int page, int size, String tahunAjaranID, String semesterID,
            String kelasID, String mapelID, String konsentrasiKeahlianID, String elemenID) throws IOException {
        validatePageNumberAndSize(page, size);

        List<Acp> acpResponse;

        if (mapelID.equalsIgnoreCase("*")) {
            acpResponse = acpRepository.findAll(size);
        } else {
            acpResponse = acpRepository.findAcpByMapel(mapelID, size);
        }

        return new PagedResponse<>(acpResponse, acpResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public Acp createAcp(AcpRequest acpRequest) throws IOException {
        if (acpRepository.existsById(acpRequest.getIdAcp())) {
            throw new BadRequestException("Acp already exists");
        }

        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(acpRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(acpRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(acpRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(acpRequest.getIdMapel());
        KonsentrasiKeahlian konsentrasiKeahlianResponse = konsentrasiKeahlianRepository
                .findById(acpRequest.getIdKonsentrasi());
        Elemen elemenResponse = elemenRepository.findById(acpRequest.getIdElemen());

        Acp acp = new Acp();
        acp.setIdAcp(acpRequest.getIdAcp() == null ? UUID.randomUUID().toString() : acpRequest.getIdAcp());
        acp.setNamaAcp(acpRequest.getNamaAcp());
        acp.setTahunAjaran(tahunAjaranResponse);
        acp.setSemester(semesterResponse);
        acp.setKelas(kelasResponse);
        acp.setMapel(mapelResponse);
        acp.setKonsentrasiKeahlian(konsentrasiKeahlianResponse);
        acp.setElemen(elemenResponse);

        return acpRepository.save(acp);
    }

    public DefaultResponse<Acp> getAcpById(String acpId) throws IOException {
        Acp acpResponse = acpRepository.findAcpById(acpId);
        return new DefaultResponse<>(acpResponse.isValid() ? acpResponse : null, acpResponse.isValid() ? 1 : 0,
                "Successfully get data");
    }

    public Acp updateAcp(String acpId, AcpRequest acpRequest) throws IOException {

        Acp acp = new Acp();

        TahunAjaran tahunAjaranResponse = tahunAjaranRepository.findById(acpRequest.getIdTahun());
        Kelas kelasResponse = kelasRepository.findById(acpRequest.getIdKelas());
        Semester semesterResponse = semesterRepository.findById(acpRequest.getIdSemester());
        Mapel mapelResponse = mapelRepository.findById(acpRequest.getIdMapel());
        KonsentrasiKeahlian konsentrasiKeahlianResponse = konsentrasiKeahlianRepository
                .findById(acpRequest.getIdKonsentrasi());
        Elemen elemenResponse = elemenRepository.findById(acpRequest.getIdElemen());

        acp.setNamaAcp(acpRequest.getNamaAcp());
        acp.setTahunAjaran(tahunAjaranResponse);
        acp.setSemester(semesterResponse);
        acp.setKelas(kelasResponse);
        acp.setMapel(mapelResponse);
        acp.setKonsentrasiKeahlian(konsentrasiKeahlianResponse);
        acp.setElemen(elemenResponse);

        return acpRepository.update(acpId, acp);
    }

    public void deleteAcpById(String acpId) throws IOException {
        Acp acpResponse = acpRepository.findAcpById(acpId);
        if (acpResponse.isValid()) {
            acpRepository.deleteById(acpId);
        } else {
            throw new ResourceNotFoundException("Acp", "id", acpId);
        }
    }
}
