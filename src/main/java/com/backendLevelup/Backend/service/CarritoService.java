package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.model.Carrito;
import com.backendLevelup.Backend.model.CarritoDetalle;

public interface CarritoService {

    Carrito obtenerCarritoUsuario(Long usuarioId);

    CarritoDetalle agregarProducto(Long usuarioId, Long productoId, Long cantidad);

    CarritoDetalle actualizarCantidad(Long usuarioId, Long productoId, Long cantidad);

    void eliminarProducto(Long usuarioId, Long productoId);

    void vaciarCarrito(Long usuarioId);
}
