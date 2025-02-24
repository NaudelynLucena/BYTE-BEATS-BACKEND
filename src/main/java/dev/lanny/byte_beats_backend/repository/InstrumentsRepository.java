package dev.lanny.byte_beats_backend.repository;

import java.util.List;

import dev.lanny.byte_beats_backend.models.Instruments;
import dev.lanny.byte_beats_backend.persistence.InstrumentDAO;

public class InstrumentsRepository {
    private final InstrumentDAO instrumentDAO;

    public InstrumentsRepository(InstrumentDAO instrumentDAO) {
        this.instrumentDAO = instrumentDAO;
    }

    public List<Instruments> getAllInstruments() {
        return instrumentDAO.loadInstruments();
    }

    // public Instrument getInstrumentById(int id) {
    // return instrumentDAO.findById(id);
    // }

}
