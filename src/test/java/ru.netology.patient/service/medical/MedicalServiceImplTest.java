package ru.netology.patient.service.medical;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MedicalServiceImplTest {

    private static MedicalService medicalService;
//    private static ArgumentCaptor<String> captor;
//    private static SendAlertServiceImpl alertService;
    //private static int messageQuantity  =0;
    private static PatientInfoRepository patientInfo;


    @BeforeAll
    public static void init() {
        patientInfo = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfo.getById("1"))
                .thenReturn(new PatientInfo("1", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

//        alertService = Mockito.mock(SendAlertServiceImpl.class);
//        medicalService = new MedicalServiceImpl(patientInfo, alertService);

    }
//    @BeforeEach
//    public static void refresh(){
//        PatientInfoRepository patientInfo = Mockito.mock(PatientInfoRepository.class);
//        Mockito.when(patientInfo.getById("1"))
//                .thenReturn(new PatientInfo("1", "Иван", "Петров", LocalDate.of(1980, 11, 26),
//                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));
//
//        alertService = Mockito.mock(SendAlertServiceImpl.class);
//        medicalService = new MedicalServiceImpl(patientInfo, alertService);
//    }
    @Test
    void testCheckBloodPressure() {

        SendAlertServiceImpl alertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfo, alertService);
        String patientId = "1";
        medicalService.checkBloodPressure(patientId, new BloodPressure(120, 85));
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(alertService, times(1)).send(captor.capture());
        assertThat(String.format("Warning, patient with id: %s, need help", patientId), equalTo(captor.getValue()));

    }
    @Test
    void testCheckBloodPressureNoMessage() {
        SendAlertServiceImpl alertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfo, alertService);
        String patientId = "1";
        medicalService.checkBloodPressure(patientId, new BloodPressure(120, 80));
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(alertService, times(0)).send(captor.capture());

    }

    @Test
    void testCheckTemperature() {

        SendAlertServiceImpl alertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfo, alertService);
        String patientId = "1";
        medicalService.checkTemperature(patientId, new BigDecimal("35.0"));
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(alertService, times(1)).send(captor.capture());
        assertThat(String.format("Warning, patient with id: %s, need help", patientId), equalTo(captor.getValue()));

    }
    @Test
    void testCheckTemperatureNoMessage() {

        SendAlertServiceImpl alertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfo, alertService);
        String patientId = "1";
        medicalService.checkTemperature(patientId, new BigDecimal("36.6"));
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(alertService, times(0)).send(captor.capture());

    }

}