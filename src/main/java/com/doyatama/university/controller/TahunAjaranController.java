
package com.doyatama.university.controller;

import com.doyatama.university.model.TahunAjaran;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.TahunAjaranRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.TahunAjaranService;
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

/**
 *
 * @author senja
 */

@RestController
@RequestMapping("/api/tahun")
public class TahunAjaranController {
    private TahunAjaranService tahunService = new TahunAjaranService();

    @GetMapping
    public PagedResponse<TahunAjaran> getTahunAjaran(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return tahunService.getAllTahunAjaran(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createTahunAjaran(@Valid @RequestBody TahunAjaranRequest tahunRequest) throws IOException {
        TahunAjaran tahun = tahunService.createTahunAjaran(tahunRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{tahunId}")
                .buildAndExpand(tahun.getIdTahun()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "TahunAjaran Created Successfully"));
    }

    @GetMapping("/{tahunId}")
    public DefaultResponse<TahunAjaran> getTahunAjaranById(@PathVariable String tahunId) throws IOException {
        return tahunService.getTahunAjaranById(tahunId);
    }


    @PutMapping("/{tahunId}")
    public ResponseEntity<?> updateTahunAjaran(@PathVariable String tahunId,
                                              @Valid @RequestBody TahunAjaranRequest tahunRequest) throws IOException {
        TahunAjaran tahun = tahunService.updateTahunAjaran(tahunId, tahunRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{tahunId}")
                .buildAndExpand(tahun.getIdTahun()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "TahunAjaran Updated Successfully"));
    }

    @DeleteMapping("/{tahunId}")
    public HttpStatus deleteTahunAjaran(@PathVariable (value = "tahunId") String tahunId) throws IOException {
        tahunService.deleteTahunAjaranById(tahunId);
        return HttpStatus.FORBIDDEN;
    }
}
