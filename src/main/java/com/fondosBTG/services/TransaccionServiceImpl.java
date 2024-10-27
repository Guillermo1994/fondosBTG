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
    public String realizarApertura(String clienteId, String fondoId, double monto, String canalNotificacion) {
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

        // Notificación
        String subject = "Suscripción al Fondo " + fondo.getNombre();
        String mensaje = "Estimado " + cliente.getNombre() + ",\n\n" +
                "Ha sido suscrito exitosamente al fondo " + fondo.getNombre() + ".\n" +
                "Monto vinculado: " + monto + ".\n\n" +
                "Saludos,\nSu equipo de Fondos BTG.";

        // Enviar notificación según el canal seleccionado
        if ("EMAIL".equalsIgnoreCase(canalNotificacion)) {
            emailService.enviarEmail(cliente.getEmail(), subject, mensaje);
        } else if ("SMS".equalsIgnoreCase(canalNotificacion)) {
            String smsMessage = "Estimado " + cliente.getNombre() + ", ha sido suscrito al fondo " + fondo.getNombre()
                    + " con un monto de " + monto + ".";
            smsService.enviarSms("+57" + cliente.getTelefono(), smsMessage);
        } else {
            throw new IllegalArgumentException("Canal de notificación no válido: " + canalNotificacion);
        }

        return "Transacción de apertura exitosa";
    }

    @Override
    public String realizarCancelacion(String clienteId, String fondoId, String transaccionId) {
        // Validar cliente y fondo
        Cliente cliente = clienteService.obtenerClientPorId(clienteId);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente con ID " + clienteId + " no encontrado.");
        }

        Fondo fondo = fondoService.obtenerFondoPorId(fondoId);
        if (fondo == null) {
            throw new ResourceNotFoundException("Fondo con ID " + fondoId + " no encontrado.");
        }

        // Buscar la transacción por ID y validar que no sea de tipo "Apertura"
        Transaccion transaccion = transaccionRepository.findById(transaccionId).orElseThrow(
                () -> new ResourceNotFoundException("Transacción con ID " + transaccionId + " no encontrada.")
        );

        if (transaccion.getTipo().equals("Cancelación")) {
            throw new OperationNotAllowedException("No se puede cancelar una transacción de tipo Cancelación.");
        }

        // Validar que la transacción pertenece al cliente y al fondo correctos
        if (!transaccion.getClienteId().equals(clienteId) || !transaccion.getFondoId().equals(fondoId)) {
            throw new OperationNotAllowedException("La transacción no corresponde al cliente o fondo indicado.");
        }

        // Crear y guardar la nueva transacción de cancelación
        Transaccion cancelacion = Transaccion.builder()
                .id(transaccion.getId())
                .tipo("Cancelación")
                .fondoId(fondoId)
                .fondoNombre(fondo.getNombre())
                .monto(transaccion.getMonto())
                .fecha(LocalDateTime.now())
                .clienteId(transaccion.getClienteId())
                .build();

        transaccionRepository.save(cancelacion);

        // Reembolsar el saldo al cliente
        cliente.setSaldoInicial(cliente.getSaldoInicial() + transaccion.getMonto());
        clienteService.guardarCliente(cliente);

        return "Transacción de cancelación exitosa";
    }

    @Override
    public List<Transaccion> verHistorial(String clienteId) {
        return transaccionRepository.findByClienteId(clienteId);
    }
}
