package com.backendLevelup.Backend.service;

import com.backendLevelup.Backend.model.*;
import com.backendLevelup.Backend.repository.BoletaDetalleRepository;
import com.backendLevelup.Backend.repository.BoletaRepository;
import com.backendLevelup.Backend.repository.ProductoRepository;
import com.backendLevelup.Backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BoletaServiceImpl implements BoletaService {

    private final BoletaRepository boletaRepository;
    private final BoletaDetalleRepository detalleRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public BoletaServiceImpl(BoletaRepository boletaRepository,
                             BoletaDetalleRepository detalleRepository,
                             UsuarioRepository usuarioRepository,
                             ProductoRepository productoRepository) {
        this.boletaRepository = boletaRepository;
        this.detalleRepository = detalleRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public Boleta crearBoleta(Long usuarioId, List<BoletaDetalle> detalles) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);
        boleta.setFecha(OffsetDateTime.now());

        long total = detalles.stream().mapToLong(d -> d.getCantidad() * d.getPrecioUnitario()).sum();
        boleta.setTotal(total);

        boleta = boletaRepository.save(boleta);

        for (BoletaDetalle detalle : detalles) {
            detalle.setBoleta(boleta);
            detalleRepository.save(detalle);
            boleta.getDetalles().add(detalle);
        }

        return boleta;
    }

    @Override
    public Boleta obtenerBoleta(Long boletaId) {
        return boletaRepository.findById(boletaId)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada"));
    }

    @Override
    public List<Boleta> obtenerBoletasUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return boletaRepository.findByUsuario(usuario);
    }

    @Override
    @Transactional
    public BoletaDetalle agregarDetalle(Long boletaId, Long productoId, Long cantidad, Long precioUnitario) {
        Boleta boleta = obtenerBoleta(boletaId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        BoletaDetalle detalle = new BoletaDetalle();
        detalle.setBoleta(boleta);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);

        detalleRepository.save(detalle);
        boleta.getDetalles().add(detalle);
        boletaRepository.save(boleta);

        return detalle;
    }
}
