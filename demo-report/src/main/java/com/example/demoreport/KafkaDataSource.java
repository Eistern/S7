package com.example.demoreport;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaDataSource implements JRDataSource {

    @Override
    public boolean next() throws JRException {

        return false;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        jrField.getName(); //This is field name of target class
        return null;
    }
}
