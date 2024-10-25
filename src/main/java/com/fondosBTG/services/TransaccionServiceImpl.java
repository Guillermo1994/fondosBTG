package com.fondosBTG.services;

import com.fondosBTG.exception.OperationNotAllowedException;
import com.fondosBTG.exception.ResourceNotFoundException;
import com.fondosBTG.models.Cliente;
import com.fondosBTG.models.Fondo;
import com.fondosBTG.models.Transaccion;
import com.fondosBTG.repositories.ITransaccionRepository;
import com.fondosBTG.services.IServices.IClienteService;
import com.fondosBTG.services.IServices.IFondoService;
import com.fondosBTG.services.IServices.ITransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransaccionServiceImpl implements ITransaccionService {

    @Autowired
    private ITransaccionRepository transaccionRepository;
    @Autowired
    private IFondoService fondoService;
    @Autowired
    private IClienteService clienteService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SmsService smsService;

    @Override
    public String realizarApertura(String clienteId, String fondoId, double monto) {
        // Validar cliente y fondo
        Cliente cliente = clienteService.obtenerClientPorId(clienteId);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente con ID " + clienteId + " no encontrado.");
        }

        Fondo fondo = fondoService.obtenerFondoPorId(fondoId);
        if (fondo == null) {
            throw new ResourceNotFoundException("Fondo con ID " + fondoId + " no encontrado.");
        }

        // Verificar si el cliente tiene saldo suficiente
        if (cliente.getSaldoInicial() < monto) {
            throw new OperationNotAllowedException("Saldo insuficiente para vincularse al fondo " + fondo.getNombre());
        }

        // Verificar si el monto es el valor minimo del fondo
        if (fondo.getMontoMinimo() > monto) {
            throw new OperationNotAllowedException("El monto minimo es insuficiente para vincularse al fondo "
                    + fondo.getNombre());
        }

        // Crear y guardar la transacción de apertura
        Transaccion transaccion = Transaccion.builder()
                .id(UUID.randomUUID().toString())
                .tipo("Apertura")
                .fondoId(fondoId)
                .fondoNombre(fondo.getNombre())
                .monto(monto)
                .fecha(LocalDateTime.now())
                .clienteId(clienteId)
                .build();

        transaccionRepository.save(transaccion);

        // Actualizar saldo del cliente
        cliente.setSaldoInicial(cliente.getSaldoInicial() - monto);
        clienteService.guardarCliente(cliente);

        // Enviar notificación por correo
        String subject = "Suscripción al Fondo " + fondo.getNombre();
        String text = "Estimado " + cliente.getNombre() + ",\n\n" +
                "Ha sido suscrito exitosamente al fondo " + fondo.getNombre() + ".\n" +
                "Monto vinculado: " + monto + ".\n\n" +
                "Saludos,\nSu equipo de Fondos BTG.";
        emailService.enviarEmail(cliente.getEmail(), subject, text);

        // Enviar mensaje SMS
//        String smsMessage = "Estimado " + cliente.getNombre() + ", ha sido suscrito al fondo " + fondo.getNombre()
//                + " con un monto de " + monto + ".";
//        smsService.enviarSms("+57"+cliente.getTelefono(), smsMessage);
        // Asegúrate de que Cliente tenga el método getTelefono()


        return "Transacción de apertura exitosa";
    }

    @Override
    public String realizarCancelacion(String clienteId, String fondoId) {
        // Validar cliente y fondo
        Cliente cliente = clienteService.obtenerClientPorId(clienteId);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente con ID " + clienteId + " no encontrado.");
        }

        Fondo fondo = fondoService.obtenerFondoPorId(fondoId);
        if (fondo == null) {
            throw new ResourceNotFoundException("Fondo con ID " + fondoId + " no encontrado.");
        }

        // Obtener transacciones del cliente
        List<Transaccion> transaccionesCliente = transaccionRepository.findByClienteId(clienteId);
        if (transaccionesCliente == null || transaccionesCliente.isEmpty()) {
            throw new OperationNotAllowedException("El cliente no tiene transacciones previas, cancelación no realizada.");
        }

        // Validar las transacciones
        boolean tieneApertura = transaccionesCliente.stream()
                .anyMatch(t -> t.getTipo().equals("Apertura") && t.getFondoId().equals(fondoId));
        boolean tieneCancelacion = transaccionesCliente.stream()
                .anyMatch(t -> t.getTipo().equals("Cancelación") && t.getFondoId().equals(fondoId));

        if (!tieneApertura) {
            throw new OperationNotAllowedException("No se encontró una transacción de apertura para este fondo.");
        }

        if (tieneCancelacion) {
            throw new OperationNotAllowedException("Ya existe una cancelación previa para este fondo.");
        }

        // Crear y guardar la transacción de cancelación
        Transaccion transaccion = Transaccion.builder()
                .id(UUID.randomUUID().toString())
                .tipo("Cancelación")
                .fondoId(fondoId)
                .fondoNombre(fondo.getNombre())
                .monto(fondo.getMontoMinimo())
                .fecha(LocalDateTime.now())
                .clienteId(clienteId)
                .build();

        transaccionRepository.save(transaccion);

        // Reembolsar el saldo al cliente
        cliente.setSaldoInicial(cliente.getSaldoInicial() + fondo.getMontoMinimo());
        clienteService.guardarCliente(cliente);

        return "Transacción de cancelación exitosa";
    }

    @Override
    public List<Transaccion> verHistorial(String clienteId) {
        return transaccionRepository.findByClienteId(clienteId);
    }
}
