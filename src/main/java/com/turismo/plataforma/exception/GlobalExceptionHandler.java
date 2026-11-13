package com.turismo.plataforma.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Convierte las excepciones en JSON con formato estándar (ProblemDetail):
 * { "status": 400, "title": "...", "detail": "...", "errores": { "campo": "mensaje" } }
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 400: campos que no cumplen las validaciones (campo -> mensaje). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.putIfAbsent(error.getField(), error.getDefaultMessage()));
        ProblemDetail problemDetail = problema(HttpStatus.BAD_REQUEST, "Datos inválidos", "Revisa los campos marcados.");
        problemDetail.setProperty("errores", errores);
        return problemDetail;
    }

    /** 400: JSON mal formado o con un tipo de dato incorrecto (texto donde va un número, fecha inválida...). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail jsonInvalido() {
        return problema(HttpStatus.BAD_REQUEST, "JSON inválido",
                "El cuerpo de la petición no tiene el formato esperado.");
    }

    /** 400: un parámetro de la URL con formato incorrecto (?desde=hoy en lugar de una fecha). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail parametroInvalido(MethodArgumentTypeMismatchException ex) {
        return problema(HttpStatus.BAD_REQUEST, "Parámetro inválido",
                "El parámetro '" + ex.getName() + "' no tiene el formato esperado.");
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    public ProblemDetail solicitudInvalida(SolicitudInvalidaException ex) {
        return problema(HttpStatus.BAD_REQUEST, "Solicitud inválida", ex.getMessage());
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ProblemDetail credenciales(CredencialesInvalidasException ex) {
        return problema(HttpStatus.UNAUTHORIZED, "No autorizado", ex.getMessage());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail noEncontrado(RecursoNoEncontradoException ex) {
        return problema(HttpStatus.NOT_FOUND, "No encontrado", ex.getMessage());
    }

    @ExceptionHandler(ConflictoException.class)
    public ProblemDetail conflicto(ConflictoException ex) {
        return problema(HttpStatus.CONFLICT, "Conflicto", ex.getMessage());
    }

    /** 409: la base de datos rechazó la operación (llave foránea, valor único...). */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail integridad() {
        return problema(HttpStatus.CONFLICT, "Restricción de base de datos",
                "No se puede completar: el registro está relacionado con otros datos o está duplicado.");
    }

    private ProblemDetail problema(HttpStatus status, String titulo, String detalle) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detalle);
        problemDetail.setTitle(titulo);
        return problemDetail;
    }
}
