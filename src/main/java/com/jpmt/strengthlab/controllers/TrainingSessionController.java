package com.jpmt.strengthlab.controllers;

import com.jpmt.strengthlab.models.dto.ApiErrorResponse;
import com.jpmt.strengthlab.models.dto.trainingsessiontemplate.TrainingSessionTemplateDetailResponse;
import com.jpmt.strengthlab.models.dto.trainingsessiontemplate.TrainingSessionTemplateRequest;
import com.jpmt.strengthlab.models.dto.trainingsessiontemplate.TrainingSessionTemplateSummaryResponse;
import com.jpmt.strengthlab.models.dto.trainingsettemplate.TrainingSetTemplateRequest;
import com.jpmt.strengthlab.models.dto.trainingsettemplate.TrainingSetTemplateResponse;
import com.jpmt.strengthlab.models.dto.trainingsettemplate.TrainingSetTemplateUpdateRequest;
import com.jpmt.strengthlab.services.TrainingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/training-session")
@Tag(name = "Training Session", description = "APIs para gestionar plantillas de sesiones de entrenamiento")
public class TrainingSessionController {

    private final TrainingSessionService service;

    public TrainingSessionController(TrainingSessionService service) {
        this.service = service;
    }

    // --- Training Session Templates ---
    @GetMapping
    @Operation(summary = "List training session templates", description = "Devuelve un resumen de todas las plantillas de sesiones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSessionTemplateSummaryResponse.class)))
    })

    public ResponseEntity<List<TrainingSessionTemplateSummaryResponse>> findAllSession() {
        List<TrainingSessionTemplateSummaryResponse> result = service.findAllSession();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Search session templates by block and week")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSessionTemplateSummaryResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<List<TrainingSessionTemplateSummaryResponse>> findAllSessionByBlockNameAndWeekNumber(
            @Parameter(description = "Nombre del bloque") @RequestParam String blockName,
            @Parameter(description = "Número de semana") @RequestParam Integer weekNumber) {
        List<TrainingSessionTemplateSummaryResponse> result = service.findAllSessionByBlockNameAndWeekNumber(blockName, weekNumber);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Get session template detail by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plantilla encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSessionTemplateDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "Plantilla no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<TrainingSessionTemplateDetailResponse> findSesionById(
            @Parameter(description = "ID de la plantilla de sesión") @PathVariable Long id) {
        TrainingSessionTemplateDetailResponse result = service.findSessionById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Create session template",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload de la plantilla",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSessionTemplateRequest.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plantilla creada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSessionTemplateDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Entrada inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<TrainingSessionTemplateDetailResponse> saveSession(
            @Valid @RequestBody TrainingSessionTemplateRequest session) {
        TrainingSessionTemplateDetailResponse savedSession = service.saveSession(session);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedSession.id())
                .toUri();
        return ResponseEntity.created(location).body(savedSession);
    }

    @PutMapping("/{id}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Update session template",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload de la plantilla",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSessionTemplateRequest.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plantilla actualizada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSessionTemplateDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "Plantilla no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Entrada inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<TrainingSessionTemplateDetailResponse> updateSession(
            @Parameter(description = "ID de la plantilla") @PathVariable Long id,
            @Valid @RequestBody TrainingSessionTemplateRequest session) {
        TrainingSessionTemplateDetailResponse result = service.updateSession(id, session);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Delete session template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plantilla eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plantilla no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<Void> deleteSessionById(
            @Parameter(description = "ID de la plantilla") @PathVariable Long id) {
        service.deleteSessionById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Training Set Templates ---
    @GetMapping("/{sessionId}/sets/{setId}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Get set template by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plantilla de set encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSetTemplateResponse.class))),
            @ApiResponse(responseCode = "404", description = "Plantilla de set no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<TrainingSetTemplateResponse> findSetById(
            @Parameter(description = "ID de la sesión (contexto)") @PathVariable Long sessionId,
            @Parameter(description = "ID de la plantilla de set") @PathVariable Long setId) {
        return ResponseEntity.ok(this.service.findSetById(setId));
    }

    @GetMapping("/{sessionId}/sets")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "List set templates for a session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSetTemplateResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<List<TrainingSetTemplateResponse>> findResumeSetById(
            @Parameter(description = "ID de la plantilla de sesión") @PathVariable Long sessionId) {
        List<TrainingSetTemplateResponse> response = service.findBySessionIdWithExercise(sessionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{sessionId}/sets")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Add set template to session",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload del set",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSetTemplateRequest.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plantilla de set creada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSetTemplateResponse.class))),
            @ApiResponse(responseCode = "404", description = "Plantilla de sesión no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<TrainingSetTemplateResponse> saveSet(
            @Parameter(description = "ID de la plantilla de sesión") @PathVariable Long sessionId,
            @Valid @RequestBody TrainingSetTemplateRequest set) {
        TrainingSetTemplateResponse savedSet = this.service.saveSet(sessionId, set);
        return ResponseEntity.status(201).body(savedSet);
    }

    @PutMapping("/{sessionId}/sets/{setId}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Update set template",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload del set",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSetTemplateUpdateRequest.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plantilla de set actualizada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TrainingSetTemplateResponse.class))),
            @ApiResponse(responseCode = "404", description = "Plantilla de set no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<TrainingSetTemplateResponse> updateSet(
            @Parameter(description = "ID de la sesión (contexto)") @PathVariable Long sessionId,
            @Parameter(description = "ID de la plantilla de set") @PathVariable Long setId,
            @Valid @RequestBody TrainingSetTemplateUpdateRequest set) {
        return ResponseEntity.ok(this.service.updateSet(setId, set));
    }

    @DeleteMapping("/{sessionId}/sets/{setId}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Delete set template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plantilla de set eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plantilla de set no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<Void> deleteSetById(
            @Parameter(description = "ID de la sesión (contexto)") @PathVariable Long sessionId,
            @Parameter(description = "ID de la plantilla de set") @PathVariable Long setId) {
        this.service.deleteSetById(setId);
        return ResponseEntity.noContent().build();
    }
}
