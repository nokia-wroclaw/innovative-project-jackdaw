package com.jackdaw.producer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataValidatorTests {

    private DataValidator dataValidator;
    private String[] validRecord;

    @Before
    public void setUp() {
        dataValidator = new DataValidator();
        validRecord = new String[]{"test", "test", "Internacional", "departureEstimate", "test", "Realizado",
                "test", "test", "test", "test", "test", "test", "test", "test", "test",
                "0.0", "0.0", "0.0", "0.0"};
    }

    @Test
    public void recordFailsToPassDataValidationWithInvalidFlightType() {
        //given
        String[] invalidRecord = validRecord;
        invalidRecord[2] = "Invalid";
        //when
        boolean result = dataValidator.isDataValid(invalidRecord);
        //then
        assertFalse(result);
    }

    @Test
    public void recordFailsToPassDataValidationWithInvalidTimeType() {
        //given
        String[] invalidRecord = validRecord;
        invalidRecord[3] = "Invalid";
        //when
        boolean result = dataValidator.isDataValid(invalidRecord);
        //then
        assertFalse(result);
    }

    @Test
    public void recordFailsToPassDataValidationWithInvalidFlightSituation() {
        //given
        String[] invalidRecord = validRecord;
        invalidRecord[5] = "Invalid";
        //when
        boolean result = dataValidator.isDataValid(invalidRecord);
        //then
        assertFalse(result);
    }

    @Test
    public void validRecordPassesDataValidation() {
        //when
        boolean result = dataValidator.isDataValid(validRecord);
        //then
        assertTrue(result);
    }


}
