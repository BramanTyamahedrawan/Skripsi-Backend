
package com.doyatama.university.controller;

import com.doyatama.university.model.Kelas;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.KelasRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.KelasService;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;



@RestController
@RequestMapping("/api/kelas")
public class KelasController {
    private KelasService kelasService = new KelasService();

    @GetMapping
    public PagedResponse<Kelas> getKelas(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return kelasService.getAllKelas(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createKelas(@Valid @RequestBody KelasRequest kelasRequest) throws IOException {
        Kelas kelas = kelasService.createKelas(kelasRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{kelasId}")
                .buildAndExpand(kelas.getIdKelas()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Kelas Created Successfully"));
    }

    @GetMapping("/{kelasId}")
    public DefaultResponse<Kelas> getKelasById(@PathVariable String kelasId) throws IOException {
        return kelasService.getKelasById(kelasId);
    }


    @PutMapping("/{kelasId}")
    public ResponseEntity<?> updateKelas(@PathVariable String kelasId,
                                              @Valid @RequestBody KelasRequest kelasRequest) throws IOException {
        Kelas kelas = kelasService.updateKelas(kelasId, kelasRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{kelasId}")
                .buildAndExpand(kelas.getIdKelas()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Kelas Updated Successfully"));
    }

    @DeleteMapping("/{kelasId}")
    public HttpStatus deleteKelas(@PathVariable (value = "kelasId") String kelasId) throws IOException {
        kelasService.deleteKelasById(kelasId);
        return HttpStatus.FORBIDDEN;
    }
}
