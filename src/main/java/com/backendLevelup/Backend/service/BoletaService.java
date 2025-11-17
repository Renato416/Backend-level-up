package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.model.Boleta;
import com.backendLevelup.Backend.model.BoletaDetalle;

import java.util.List;

public interface BoletaService {

    Boleta crearBoleta(Long usuarioId, List<BoletaDetalle> detalles);

    Boleta obtenerBoleta(Long boletaId);

    List<Boleta> obtenerBoletasUsuario(Long usuarioId);

    BoletaDetalle agregarDetalle(Long boletaId, Long productoId, Long cantidad, Long precioUnitario);
}
