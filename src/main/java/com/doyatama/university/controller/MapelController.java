
package com.doyatama.university.controller;

import com.doyatama.university.model.Mapel;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.MapelRequest;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.service.MapelService;
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
@RequestMapping("/api/mapel")
public class MapelController {
    private MapelService mapelService = new MapelService();

    @GetMapping
    public PagedResponse<Mapel> getMapel(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return mapelService.getAllMapel(page, size);
    }

    @PostMapping
    public ResponseEntity<?> createMapel(@Valid @RequestBody MapelRequest mapelRequest) throws IOException {
        Mapel mapel = mapelService.createMapel(mapelRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{mapelId}")
                .buildAndExpand(mapel.getIdMapel()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Mapel Created Successfully"));
    }

    @GetMapping("/{mapelId}")
    public DefaultResponse<Mapel> getMapelById(@PathVariable String mapelId) throws IOException {
        return mapelService.getMapelById(mapelId);
    }


    @PutMapping("/{mapelId}")
    public ResponseEntity<?> updateMapel(@PathVariable String mapelId,
                                              @Valid @RequestBody MapelRequest mapelRequest) throws IOException {
        Mapel mapel = mapelService.updateMapel(mapelId, mapelRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{mapelId}")
                .buildAndExpand(mapel.getIdMapel()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Mapel Updated Successfully"));
    }

    @DeleteMapping("/{mapelId}")
    public HttpStatus deleteMapel(@PathVariable (value = "mapelId") String mapelId) throws IOException {
        mapelService.deleteMapelById(mapelId);
        return HttpStatus.FORBIDDEN;
    }
}
