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

/**
 * Servicio para gestionar transacciones de fondos, como apertura y cancelación de suscripciones.
 *
 * @author Guillermo Ramirez
 */
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

    /**
     * Realiza una apertura de suscripción al fondo indicado para el cliente especificado, con un monto específico.
     * <p>
     * Verifica que el cliente tenga saldo suficiente y que el monto sea igual o superior al monto mínimo del fondo.
     * Notifica al cliente vía email o SMS según el canal de notificación elegido.
     *
     * @param clienteId         ID del cliente que realiza la suscripción.
     * @param fondoId           ID del fondo al cual se suscribe el cliente.
     * @param monto             Monto a vincular en la suscripción.
     * @param canalNotificacion Canal de notificación a utilizar (EMAIL o SMS).
     * @return Un mensaje de éxito tras realizar la apertura.
     * @throws ResourceNotFoundException    Si el cliente o el fondo no se encuentran.
     * @throws OperationNotAllowedException Si el cliente no tiene saldo suficiente o el monto no cumple con el mínimo
     *                                      requerido.
     * @throws IllegalArgumentException     Si el canal de notificación no es válido.
     */
    @Override
    public String realizarApertura(String clienteId, String fondoId, double monto, String canalNotificacion) {
        Cliente cliente = clienteService.obtenerClientPorId(clienteId);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente con ID " + clienteId + " no encontrado.");
        }

        Fondo fondo = fondoService.obtenerFondoPorId(fondoId);
        if (fondo == null) {
            throw new ResourceNotFoundException("Fondo con ID " + fondoId + " no encontrado.");
        }

        if (cliente.getSaldoInicial() < monto) {
            throw new OperationNotAllowedException("Saldo insuficiente para vincularse al fondo "
                    + fondo.getNombre());
        }

        if (fondo.getMontoMinimo() > monto) {
            throw new OperationNotAllowedException("El monto minimo es insuficiente para vincularse al fondo "
                    + fondo.getNombre());
        }

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

        cliente.setSaldoInicial(cliente.getSaldoInicial() - monto);
        clienteService.guardarCliente(cliente);

        String subject = "Suscripción al Fondo " + fondo.getNombre();
        String mensaje = "Estimado " + cliente.getNombre() + ",\n\n"
                + "Ha sido suscrito exitosamente al fondo " + fondo.getNombre() + ".\n"
                + "Monto vinculado: " + monto + ".\n\n" + "Saludos,\nSu equipo de Fondos BTG.";

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

    /**
     * Realiza una cancelación de suscripción al fondo indicado para el cliente especificado, utilizando el ID de
     * transacción.
     * <p>
     * Valida que la transacción sea de tipo "Apertura" y que pertenezca al cliente y fondo indicados. Reembolsa el
     * saldo al cliente y guarda la transacción de cancelación.
     *
     * @param clienteId     ID del cliente que realiza la cancelación.
     * @param fondoId       ID del fondo cuya suscripción se desea cancelar.
     * @param transaccionId ID de la transacción de apertura a cancelar.
     * @return Un mensaje de éxito tras realizar la cancelación.
     * @throws ResourceNotFoundException    Si el cliente, fondo o transacción no se encuentran.
     * @throws OperationNotAllowedException Si la transacción no es de tipo apertura o no pertenece al cliente y fondo
     *                                      indicados.
     */
    @Override
    public String realizarCancelacion(String clienteId, String fondoId, String transaccionId) {
        Cliente cliente = clienteService.obtenerClientPorId(clienteId);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente con ID " + clienteId + " no encontrado.");
        }

        Fondo fondo = fondoService.obtenerFondoPorId(fondoId);
        if (fondo == null) {
            throw new ResourceNotFoundException("Fondo con ID " + fondoId + " no encontrado.");
        }

        Transaccion transaccion = transaccionRepository.findById(transaccionId).orElseThrow(
                () -> new ResourceNotFoundException("Transacción con ID " + transaccionId + " no encontrada.")
        );

        if (transaccion.getTipo().equals("Cancelación")) {
            throw new OperationNotAllowedException("No se puede cancelar una transacción de tipo Cancelación.");
        }

        if (!transaccion.getClienteId().equals(clienteId) || !transaccion.getFondoId().equals(fondoId)) {
            throw new OperationNotAllowedException("La transacción no corresponde al cliente o fondo indicado.");
        }

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

        cliente.setSaldoInicial(cliente.getSaldoInicial() + transaccion.getMonto());
        clienteService.guardarCliente(cliente);

        return "Transacción de cancelación exitosa";
    }

    /**
     * Retorna el historial de transacciones de un cliente.
     *
     * @param clienteId ID del cliente cuyo historial de transacciones se desea consultar.
     * @return Lista de transacciones del cliente.
     */
    @Override
    public List<Transaccion> verHistorial(String clienteId) {
        return transaccionRepository.findByClienteId(clienteId);
    }
}
