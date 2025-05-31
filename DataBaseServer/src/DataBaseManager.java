//ICSD21028 -- Konstantinos Katsaros
//ICSD21049 -- Aristeidis Papadopoulos

package DataBaseServer.src;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataBaseManager {
    private ConcurrentMap<String, User> users = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Event> events = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Reservation> reservations = new ConcurrentHashMap<>();
    private ConcurrentMap<String, List<String>> userReservations = new ConcurrentHashMap<>();

    // Χειρισμός αιτημάτων
    public Response handleRequest(Request request) {
    try {
        switch (request.getType()) {
            case REGISTER_USER -> {
                User user = (User) request.getData();
                return registerUser(user);
            }
            case DELETE_USER -> {
                String username = (String) request.getData();
                return deleteUser(username);
            }
            case ADD_EVENT -> {
                Event event = (Event) request.getData();
                return addEvent(event);
            }
            case DEACTIVATE_EVENT -> {
                String eventId = (String) request.getData();
                return deactivateEvent(eventId);
            }
            case GET_EVENTS -> {
                return getActiveEvents();
            }
            case GET_EVENT_DETAILS -> {
                String eventId = (String) request.getData();
                return getEventDetails(eventId);
            }
            case RESERVE_TICKETS -> {
                Reservation reservation = (Reservation) request.getData();
                return reserveTickets(reservation);
            }
            case PROCESS_PAYMENT -> {
                String reservationId = (String) request.getData();
                return processPayment(reservationId);
            }
            case CANCEL_RESERVATION -> {
                String reservationId = (String) request.getData();
                return cancelReservation(reservationId);
            }
            default -> {
                return new Response(ResponseStatus.ERROR, "Unknown request type", null);
            }
        }
    } catch (Exception e) {
        return new Response(ResponseStatus.ERROR, "Error processing request: " + e.getMessage(), null);
    }
}

    // Μέθοδοι διαχείρισης δεδομένων
    private Response registerUser(User user) {
        if (users.containsKey(user.getUsername())) {
            return new Response(ResponseStatus.ERROR, "Username already exists", null);
        }
        users.put(user.getUsername(), user);
        userReservations.put(user.getUsername(), new ArrayList<>());
        return new Response(ResponseStatus.SUCCESS, "User registered successfully", null);
    }

    private Response deleteUser(String username) {
        if (!users.containsKey(username)) {
            return new Response(ResponseStatus.ERROR, "User not found", null);
        }
        
        // Διαγραφή κρατήσεων χρήστη
        List<String> userResIds = userReservations.getOrDefault(username, new ArrayList<>());
        for (String resId : userResIds) {
            reservations.remove(resId);
        }
        userReservations.remove(username);
        users.remove(username);
        
        return new Response(ResponseStatus.SUCCESS, "User deleted successfully", null);
    }

    private Response addEvent(Event event) {
        if (events.containsKey(event.getEventId())) {
            return new Response(ResponseStatus.ERROR, "Event ID already exists", null);
        }
        events.put(event.getEventId(), event);
        return new Response(ResponseStatus.SUCCESS, "Event added successfully", null);
    }

    private Response deactivateEvent(String eventId) {
        Event event = events.get(eventId);
        if (event == null) {
            return new Response(ResponseStatus.ERROR, "Event not found", null);
        }
        event.deactivate();
        return new Response(ResponseStatus.SUCCESS, "Event deactivated successfully", null);
    }

    private Response getActiveEvents() {
        List<Event> activeEvents = events.values().stream()
            .filter(Event::isActive)
            .toList();
        return new Response(ResponseStatus.SUCCESS, "Active events retrieved", activeEvents);
    }

    private Response getEventDetails(String eventId) {
        Event event = events.get(eventId);
        if (event == null || !event.isActive()) {
            return new Response(ResponseStatus.ERROR, "Event not found or inactive", null);
        }
        return new Response(ResponseStatus.SUCCESS, "Event details", event);
    }

    private Response reserveTickets(Reservation reservation) {
        Event event = events.get(reservation.getEventId());
        if (event == null || !event.isActive()) {
            return new Response(ResponseStatus.ERROR, "Event not found or inactive", null);
        }
        
        Performance performance = event.getPerformance(reservation.getPerformanceId());
        if (performance == null) {
            return new Response(ResponseStatus.ERROR, "Performance not found", null);
        }
        
        synchronized (performance) {
            if (performance.getAvailableSeats() < reservation.getSeats()) {
                return new Response(ResponseStatus.ERROR, "Not enough available seats", null);
            }
            performance.setAvailableSeats(performance.getAvailableSeats() - reservation.getSeats());
        }
        
        reservations.put(reservation.getReservationId(), reservation);
        userReservations.computeIfAbsent(reservation.getUsername(), k -> new ArrayList<>())
            .add(reservation.getReservationId());
        
        return new Response(ResponseStatus.SUCCESS, "Reservation successful", reservation);
    }

    private Response processPayment(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            return new Response(ResponseStatus.ERROR, "Reservation not found", null);
        }
        
        if (reservation.isCancelled()) {
            return new Response(ResponseStatus.ERROR, "Reservation is cancelled", null);
        }
        
        reservation.setPaid(true);
        return new Response(ResponseStatus.SUCCESS, "Payment processed successfully", null);
    }

    private Response cancelReservation(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            return new Response(ResponseStatus.ERROR, "Reservation not found", null);
        }
        
        if (reservation.isPaid()) {
            return new Response(ResponseStatus.ERROR, "Cannot cancel paid reservation", null);
        }
        
        Event event = events.get(reservation.getEventId());
        if (event == null) {
            return new Response(ResponseStatus.ERROR, "Event not found", null);
        }
        
        Performance performance = event.getPerformance(reservation.getPerformanceId());
        if (performance == null) {
            return new Response(ResponseStatus.ERROR, "Performance not found", null);
        }
        
        // Έλεγχος ημερομηνίας ακύρωσης
        if (LocalDateTime.now().plusDays(1).isAfter(performance.getStartTime())) {
            return new Response(ResponseStatus.ERROR, "Cannot cancel on event day", null);
        }
        
        synchronized (performance) {
            performance.setAvailableSeats(performance.getAvailableSeats() + reservation.getSeats());
        }
        
        reservation.setCancelled(true);
        return new Response(ResponseStatus.SUCCESS, "Reservation cancelled successfully", null);
    }
}