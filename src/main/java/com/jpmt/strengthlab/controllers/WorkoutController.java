package com.jpmt.strengthlab.controllers;

import com.jpmt.strengthlab.models.dto.ApiErrorResponse;
import com.jpmt.strengthlab.models.dto.workoutentry.WorkoutEntryCreateRequest;
import com.jpmt.strengthlab.models.dto.workoutentry.WorkoutEntryResponse;
import com.jpmt.strengthlab.models.dto.workoutentry.WorkoutEntryUpdateRequest;
import com.jpmt.strengthlab.models.dto.workoutsession.WorkoutDayResponse;
import com.jpmt.strengthlab.models.dto.workoutsession.WorkoutSessionDetailResponse;
import com.jpmt.strengthlab.models.dto.workoutsession.WorkoutSessionRequest;
import com.jpmt.strengthlab.models.dto.workoutsession.WorkoutSessionSummaryResponse;
import com.jpmt.strengthlab.models.dto.workoutset.WorkoutSetRequest;
import com.jpmt.strengthlab.models.dto.workoutset.WorkoutSetResponse;
import com.jpmt.strengthlab.services.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@Tag(name = "Workout", description = "APIs para gestionar sesiones de entrenamiento, entradas y sets")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    // --- Workout Sessions ---
    @PostMapping
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Create workout session", description = "Crea una nueva sesión de entrenamiento",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la sesión",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutSessionRequest.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sesión creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutSessionDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<WorkoutSessionDetailResponse> createWorkoutSession(
            @Valid @RequestBody WorkoutSessionRequest session) {
        WorkoutSessionDetailResponse createdSession = workoutService.saveWorkoutSession(session);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSession.id())
                .toUri();
        return ResponseEntity.created(location).body(createdSession);
    }

    @PostMapping("/template/{templateId}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Create workout from template", description = "Crea una sesión basada en una plantilla existente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la sesión",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutSessionRequest.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sesión creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutSessionDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "Plantilla no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<WorkoutSessionDetailResponse> createWorkoutFromTemplate(
            @Parameter(description = "ID de la plantilla") @PathVariable Long templateId,
            @Valid @RequestBody WorkoutSessionRequest session) {
        WorkoutSessionDetailResponse createdSession = workoutService.createWorkoutFromTemplate(templateId, session);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSession.id())
                .toUri();
        return ResponseEntity.created(location).body(createdSession);
    }

    @GetMapping
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Get workout sessions by date range", description = "Obtiene sesiones en un rango de fechas (por defecto últimos 30 días)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de sesiones obtenida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutSessionSummaryResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<List<WorkoutSessionSummaryResponse>> getWorkoutSessionsByDateRange(
            @Parameter(description = "Fecha inicio (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "Fecha fin (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        if (from == null) from = LocalDate.now().minusDays(30);
        if (to == null) to = LocalDate.now();

        List<WorkoutSessionSummaryResponse> sessions = workoutService.findAllWorkoutSessionsByRangeDate(from, to);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/full/{id}")
    @Operation(summary = "Get a full workout session by date range", description = "Obtiene una sesion completa en un rango de fechas (por defecto today)")
    // <editor-fold desc="OpenAPI annotations">
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de sesiones obtenida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutDayResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<WorkoutDayResponse> getFullWorkoutSessionsById(
            @Parameter(description = "Fecha  (YYYY-MM-DD)") @PathVariable(required = false) Long id) {
        WorkoutDayResponse day = workoutService.findFullDayById(id);

        return ResponseEntity.ok(day);
    }

    @GetMapping("/{id}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Get workout by ID", description = "Obtiene el detalle completo de una sesión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sesión encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutSessionDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<WorkoutSessionDetailResponse> getWorkoutById(
            @Parameter(description = "ID de la sesión") @PathVariable Long id) {
        WorkoutSessionDetailResponse session = workoutService.findWorkoutById(id);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{id}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Delete workout session", description = "Elimina una sesión de entrenamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sesión eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<Void> deleteWorkoutById(
            @Parameter(description = "ID de la sesión") @PathVariable Long id) {
        workoutService.deleteWorkoutById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Workout Entries ---

    @PostMapping("/entries")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Add workout entry", description = "Añade una entrada (ejercicio) a una sesión existente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la entrada",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutEntryCreateRequest.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrada creada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutEntryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sesión o ejercicio no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<WorkoutEntryResponse> addWorkoutEntry(
            @Valid @RequestBody WorkoutEntryCreateRequest entry) {
        WorkoutEntryResponse createdEntry = workoutService.saveWorkoutEntry(entry);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEntry.id())
                .toUri();
        return ResponseEntity.created(location).body(createdEntry);
    }

    @PutMapping("/entries/{id}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Update workout entry", description = "Actualiza una entrada existente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutEntryUpdateRequest.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrada actualizada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutEntryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Entrada no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<WorkoutEntryResponse> updateWorkoutEntry(
            @Parameter(description = "ID de la entrada") @PathVariable Long id,
            @Valid @RequestBody WorkoutEntryUpdateRequest entry) {
        WorkoutEntryResponse updatedEntry = workoutService.updateEntry(id, entry);
        return ResponseEntity.ok(updatedEntry);
    }

    @DeleteMapping("/entries/{id}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Delete workout entry", description = "Elimina una entrada de una sesión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entrada eliminada", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entrada no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<Void> deleteWorkoutEntryById(
            @Parameter(description = "ID de la entrada") @PathVariable Long id) {
        workoutService.deleteWorkoutEntryById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Workout Sets ---

    @GetMapping("/{workoutId}/entries/{entryId}/sets")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Get sets for entry", description = "Obtiene los sets de una entrada específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de sets",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutSetResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<List<WorkoutSetResponse>> getWorkoutSetsByEntryId(
            @Parameter(description = "ID de la sesión (contexto)") @PathVariable Long workoutId,
            @Parameter(description = "ID de la entrada") @PathVariable Long entryId) {
        List<WorkoutSetResponse> sets = workoutService.findAllWorkoutSets(entryId);
        return ResponseEntity.ok(sets);
    }

    @PostMapping("/{workoutId}/entries/{entryId}/sets")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Add workout set", description = "Añade un set a una entrada",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del set",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutSetRequest.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Set creado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutSetResponse.class))),
            @ApiResponse(responseCode = "404", description = "Entrada no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<WorkoutSetResponse> addWorkoutSet(
            @Parameter(description = "ID de la sesión (contexto)") @PathVariable Long workoutId,
            @Parameter(description = "ID de la entrada") @PathVariable Long entryId,
            @Valid @RequestBody WorkoutSetRequest workoutSet) {
        WorkoutSetResponse createdSet = workoutService.saveWorkoutSet(entryId, workoutSet);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSet.id())
                .toUri();
        return ResponseEntity.created(location).body(createdSet);
    }

    @PutMapping("/{workoutId}/entries/{entryId}/sets/{setId}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Update workout set", description = "Actualiza un set existente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del set",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutSetRequest.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Set actualizado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkoutSetResponse.class))),
            @ApiResponse(responseCode = "404", description = "Set no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<WorkoutSetResponse> updateWorkoutSet(
            @Parameter(description = "ID de la sesión (contexto)") @PathVariable Long workoutId,
            @Parameter(description = "ID de la entrada (contexto)") @PathVariable Long entryId,
            @Parameter(description = "ID del set") @PathVariable Long setId,
            @Valid @RequestBody WorkoutSetRequest workoutSet) {
        WorkoutSetResponse updatedSet = workoutService.updateWorkoutSet(setId, workoutSet);
        return ResponseEntity.ok(updatedSet);
    }

    @DeleteMapping("/{workoutId}/entries/{entryId}/sets/{setId}")
    // <editor-fold desc="OpenAPI annotations">
    @Operation(summary = "Delete workout set", description = "Elimina un set")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Set eliminado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Set no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    // </editor-fold>
    public ResponseEntity<Void> deleteWorkoutSetById(
            @Parameter(description = "ID de la sesión (contexto)") @PathVariable Long workoutId,
            @Parameter(description = "ID de la entrada (contexto)") @PathVariable Long entryId,
            @Parameter(description = "ID del set") @PathVariable Long setId) {
        workoutService.deleteWorkoutSetById(setId);
        return ResponseEntity.noContent().build();
    }
}
