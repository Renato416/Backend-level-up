package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.model.Carrito;
import com.backendLevelup.Backend.model.CarritoDetalle;
import com.backendLevelup.Backend.model.Producto;
import com.backendLevelup.Backend.model.Usuario;
import com.backendLevelup.Backend.repository.CarritoDetalleRepository;
import com.backendLevelup.Backend.repository.CarritoRepository;
import com.backendLevelup.Backend.repository.ProductoRepository;
import com.backendLevelup.Backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoDetalleRepository detalleRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public CarritoServiceImpl(CarritoRepository carritoRepository,
                              CarritoDetalleRepository detalleRepository,
                              UsuarioRepository usuarioRepository,
                              ProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.detalleRepository = detalleRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public Carrito obtenerCarritoUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuario(usuario);
                    return carritoRepository.save(nuevo);
                });
    }

    @Override
    @Transactional
    public CarritoDetalle agregarProducto(Long usuarioId, Long productoId, Long cantidad) {
        Carrito carrito = obtenerCarritoUsuario(usuarioId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Optional<CarritoDetalle> detalleExistente = carrito.getDetalles().stream()
                .filter(d -> d.getProducto().getId().equals(productoId))
                .findFirst();

        CarritoDetalle detalle;
        if (detalleExistente.isPresent()) {
            detalle = detalleExistente.get();
            detalle.setCantidad(detalle.getCantidad() + cantidad);
        } else {
            detalle = new CarritoDetalle();
            detalle.setCarrito(carrito);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            carrito.getDetalles().add(detalle);
        }

        carritoRepository.save(carrito);
        return detalle;
    }

    @Override
    @Transactional
    public CarritoDetalle actualizarCantidad(Long usuarioId, Long productoId, Long cantidad) {
        Carrito carrito = obtenerCarritoUsuario(usuarioId);

        CarritoDetalle detalle = carrito.getDetalles().stream()
                .filter(d -> d.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en carrito"));

        detalle.setCantidad(cantidad);
        carritoRepository.save(carrito);
        return detalle;
    }

    @Override
    @Transactional
    public void eliminarProducto(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerCarritoUsuario(usuarioId);

        CarritoDetalle detalle = carrito.getDetalles().stream()
                .filter(d -> d.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en carrito"));

        carrito.getDetalles().remove(detalle);
        detalleRepository.delete(detalle);
    }

    @Override
    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerCarritoUsuario(usuarioId);

        carrito.getDetalles().forEach(detalleRepository::delete);
        carrito.getDetalles().clear();
        carritoRepository.save(carrito);
    }
}
