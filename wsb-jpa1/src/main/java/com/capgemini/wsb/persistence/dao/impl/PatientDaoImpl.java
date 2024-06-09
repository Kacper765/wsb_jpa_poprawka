package com.capgemini.wsb.persistence.dao.impl;

import com.capgemini.wsb.persistence.dao.PatientDao;
import com.capgemini.wsb.persistence.entity.PatientEntity;
import com.capgemini.wsb.persistence.enums.TreatmentType;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class PatientDaoImpl extends AbstractDao<PatientEntity, Long> implements PatientDao {


    @Override
    public List<PatientEntity> findByDoctor(String firstName, String lastName) { // Nadpisuje metodę z interfejsu, która wyszukuje pacjentów na podstawie imienia i nazwiska lekarza
        Query query = entityManager.createQuery("SELECT p FROM PatientEntity p " + // Tworzy zapytanie JPQL, które wybiera pacjentów
                        "JOIN p.visits v " + // Łączy pacjentów z wizytami
                        "JOIN v.doctor d " + // Łączy wizyty z lekarzami
                        "WHERE v.doctor.firstName = :firstName AND v.doctor.lastName = :lastName") // Dodaje warunek na imię i nazwisko lekarza
                .setParameter("firstName", firstName) // Ustawia parametr firstName w zapytaniu
                .setParameter("lastName", lastName); // Ustawia parametr lastName w zapytaniu
        return query.getResultList();
    }


    @Override
    public List<PatientEntity> findPatientsHavingTreatmentType(TreatmentType treatmentType) { // Nadpisuje metodę z interfejsu, która wyszukuje pacjentów na podstawie typu leczenia
        Query query = entityManager.createQuery("SELECT distinct p FROM PatientEntity p " + // Tworzy zapytanie JPQL, które wybiera unikalnych pacjentów
                "JOIN p.visits v " + // Łączy pacjentów z wizytami
                "JOIN v.medicalTreatments mt " + // Łączy wizyty z zabiegami medycznymi
                "WHERE mt.type = :treatmentType") // Dodaje warunek na typ leczenia
                .setParameter("treatmentType", treatmentType); // Ustawia parametr treatmentType w zapytaniu
        return query.getResultList();
    }


    @Override
    public List<PatientEntity> findPatientsSharingSameLocationWithDoc(String firstName, String lastName) { // Nadpisuje metodę z interfejsu, która wyszukuje pacjentów na podstawie wspólnej lokalizacji z lekarzem
        Query query = entityManager.createQuery("SELECT p FROM PatientEntity p " + // Tworzy zapytanie JPQL, które wybiera pacjentów
                        "JOIN p.addresses a " + // Łączy pacjentów z adresami
                        "JOIN a.doctors d " + // Łączy adresy z lekarzami
                        "WHERE d.firstName = :docFirstName AND d.lastName = :docLastName") // Dodaje warunek na imię i nazwisko lekarza
                .setParameter("docFirstName", firstName) // Ustawia parametr docFirstName w zapytaniu
                .setParameter("docLastName", lastName); // Ustawia parametr docLastName w zapytaniu
        return query.getResultList();
    }


    @Override
    public List<PatientEntity> findPatientsWithoutLocation() { // Nadpisuje metodę z interfejsu, która wyszukuje pacjentów bez adresu
        Query query = entityManager.createQuery("SELECT p FROM PatientEntity p WHERE size(p.addresses) = 0"); // Tworzy zapytanie JPQL, które wybiera pacjentów bez adresów
        return query.getResultList();
    }

}
