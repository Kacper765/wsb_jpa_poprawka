package com.capgemini.wsb.persistence.dao.impl;

import com.capgemini.wsb.persistence.dao.DoctorDao;
import com.capgemini.wsb.persistence.entity.DoctorEntity;
import com.capgemini.wsb.persistence.enums.Specialization;
import org.springframework.stereotype.Repository;

import java.util.List;
import javax.persistence.Query;

@Repository
public class DoctorDaoImpl extends AbstractDao<DoctorEntity, Long> implements DoctorDao {
    @Override
    public List<DoctorEntity> findBySpecialization(Specialization specialization) { // Nadpisuje metodę z interfejsu, która wyszukuje lekarzy na podstawie specjalizacji
        Query query = entityManager.createQuery("SELECT d FROM DoctorEntity d " + // Tworzy zapytanie JPQL, które wybiera lekarzy
                        "WHERE d.specialization = :spec") // Dodaje warunek na specjalizację lekarza
                .setParameter("spec", specialization); // Ustawia parametr spec w zapytaniu
        return query.getResultList();
    }


    @Override
    public long countNumOfVisitsWithPatient(String docFirstName, String docLastName, String patientFirstName, String patientLastName) { // Nadpisuje metodę z interfejsu, która liczy liczbę wizyt lekarza z pacjentem
        Query query = entityManager.createQuery("SELECT d FROM DoctorEntity d " + // Tworzy zapytanie JPQL, które wybiera lekarzy
                        "JOIN d.visits v " + // Łączy lekarzy z wizytami
                        "WHERE d.firstName = :docFirstName AND d.lastName = :docLastName " + // Dodaje warunek na imię i nazwisko lekarza
                        "AND v.patient.firstName = :patientFirstName AND v.patient.lastName = :patientLastName") // Dodaje warunek na imię i nazwisko pacjenta
                .setParameter("docFirstName", docFirstName) // Ustawia parametr docFirstName w zapytaniu
                .setParameter("docLastName", docLastName) // Ustawia parametr docLastName w zapytaniu
                .setParameter("patientFirstName", patientFirstName) // Ustawia parametr patientFirstName w zapytaniu
                .setParameter("patientLastName", patientLastName); // Ustawia parametr patientLastName w zapytaniu
        return query.getResultList().size();
    }

}
